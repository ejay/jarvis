package gcal;


import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.JsonToken;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Preconditions;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;


/**
 * Created by Jorrit on 15-1-2016.
 */
public class GoogleCalendarTestingThing {
	public enum  Response{
		Auth_Pending,Slowdown,Token;
	}

	/** Global instance of the HTTP transport. */
	private static HttpTransport httpTransport;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	//Totally not safe but whatever will delete after project....
	private static final String CLIENT_ID = "491174436153-0pufbh7f345ab6k2mrjc1j31jre2j9eb.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "ytwCuI481bkFtR2iGKCLyAWG";

	private OAuthToken authToken;
	private static com.google.api.services.calendar.Calendar client;
	//https://github.com/google/google-api-java-client-samples/tree/master/calendar-cmdline-sample/src/main/java/com/google/api/services/samples/calendar/cmdline
	//https://console.developers.google.com/home/dashboard?authuser=0&project=alien-marking-119110
	//https://developers.google.com/identity/protocols/googlescopes   relevant scope: https://www.googleapis.com/auth/calendar.readonly
	//https://developers.google.com/apis-explorer/#p/calendar/v3/calendar.events.list?calendarId=primary&maxResults=10&orderBy=startTime&singleEvents=true&timeMin=2016-01-14T00%253A00%253A00%252B02%253A00&_h=3&
	//https://developers.google.com/apis-explorer/#p/calendar/v3/calendar.calendarList.list?_h=1&

	//https://developers.google.com/identity/protocols/OAuth2ForDevices

	public void getToken(){
		authToken = new OAuthToken();
		if(!authToken.load()){//if loading failed
			triggercode();
		}
	}

	public void triggercode() {
		httpTransport = new NetHttpTransport();
		HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
		try {
			HttpRequest httpRequest = requestFactory.buildPostRequest(new GenericUrl("https://accounts.google.com/o/oauth2/device/code"), new StupidContent());
			HttpResponse execute = httpRequest.execute();
			//System.out.println(execute.parseAsString());
			OAuthResponse rep = parseOAuthResponse(execute.getContent());

			System.out.println(rep);
			System.out.println("To access your Google Calendar, you need to do some additional steps.");
			System.out.println("Please go to: " + rep.getVerification_url());
			System.out.printf("And use the following token to identify yourself: " + rep.getUser_code());
			browse(rep.getVerification_url());
			//Now onto polling google for out token!

			boolean tokenGot = false;
			httpRequest = requestFactory.buildPostRequest(new GenericUrl("https://www.googleapis.com/oauth2/v4/token"),
					new StupidContent("client_id="+ CLIENT_ID +
							"&client_secret="+ CLIENT_SECRET +
							"&code="+rep.getDevice_code()+
							"&grant_type=http://oauth.net/grant_type/device/1.0"));
			int intervalMultiplier = 1000;
			while (!tokenGot){
				Thread.sleep(rep.getInterval()*intervalMultiplier);//use multiplier if we get slowdown msgs from google
				try{
					execute = httpRequest.execute();
					parseOAuthToken(execute.getContent());
					tokenGot = true;
				} catch (HttpResponseException e){
					if(e.getContent().contains("authorization_pending")){
						System.out.print(".");
					}else if(e.getContent().contains("slow_down")){
						intervalMultiplier *= 2;
					}else if(e.getContent().contains("invalid_grant")){
						System.out.println("Google already gave us a token, but we lost it :(. Tokens expire in an hour, so please try again later...");
						System.out.println(e.getContent());
					}else{
						System.out.println("OH noes! An unexpected error has occurred :(");
						System.out.println(e.getContent());
						break;
					}
				}
			}
			System.out.println(intervalMultiplier);
			System.out.println("Got your approval from google!");
			System.out.println(authToken);
			authToken.tokenTime();
			authToken.store();


		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private OAuthResponse parseOAuthResponse(InputStream content) throws IOException {
		JsonParser parser = JSON_FACTORY.createJsonParser(content);
		OAuthResponse rep = new OAuthResponse();
		//parser.parse(rep);//should have parsed automatically... does not work.
		while (parser.getCurrentToken() != JsonToken.END_OBJECT){
			if(parser.getCurrentToken() == JsonToken.FIELD_NAME){
				String field = parser.getText();
				parser.nextToken();
				switch (field){
					case "device_code": rep.setDevice_code(parser.getText());
						break;
					case "user_code": rep.setUser_code(parser.getText());
						break;
					case "verification_url": rep.setVerification_url(parser.getText());
						break;
					case "expires_in": rep.setExpires_in(parser.getIntValue());
						break;
					case "interval": rep.setInterval(parser.getIntValue());
						break;
				}

			}

			parser.nextToken();
		}
		parser.close();
		return rep;
	}

	private Response parseOAuthToken(InputStream content) throws IOException {
		JsonParser parser = JSON_FACTORY.createJsonParser(content);
		OAuthToken rep = new OAuthToken();
		//parser.parse(rep);//should have parsed automatically... does not work.
		Response type = Response.Token;
		while (parser.getCurrentToken() != JsonToken.END_OBJECT){
			if(parser.getCurrentToken() == JsonToken.FIELD_NAME){
				String field = parser.getText();
				parser.nextToken();
				switch (field){
					case "access_token": rep.setAccess_token(parser.getText());
						break;
					case "user_code": rep.setToken_type(parser.getText());
						break;
					case "expires_in": rep.setExpires_in(parser.getIntValue());
						break;
					case "verification_url": rep.setRefresh_token(parser.getText());
						break;
					case "error":
						if(parser.getText().equalsIgnoreCase("authorization_pending")) {
							type = Response.Auth_Pending;
						}else if(parser.getText().equalsIgnoreCase("slow_down")) {
							type = Response.Slowdown;
						}
						break;
				}

			}

			parser.nextToken();//always get the next token
		}
		authToken = rep;
		return type;
	}





//		try {
//			CalendarList feed = client.calendarList().list().execute();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		Events feed = client.events().list(calendar.getId()).execute();







	/**
	 * Credits to some google example git repo
	 * Open a browser at the given URL using {@link Desktop} if available, or alternatively output the
	 * URL to {@link System#out} for command-line applications.
	 *
	 * @param url URL to browse
	 */
	public static void browse(String url) {
		Preconditions.checkNotNull(url);
		// Ask user to open in their browser using copy-paste
		//System.out.println("Please open the following address in your browser:");
		//System.out.println("  " + url);
		// Attempt to open it in the browser
		try {
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				if (desktop.isSupported(Desktop.Action.BROWSE)) {
					//System.out.println("Attempting to open that address in the default browser now...");
					desktop.browse(URI.create(url));
				}
			}
		} catch (IOException e) {
			System.out.println("Unable to open browser: " + e);
		} catch (InternalError e) {
			// A bug in a JRE can cause Desktop.isDesktopSupported() to throw an
			// InternalError rather than returning false. The error reads,
			// "Can't connect to X11 window server using ':0.0' as the value of the
			// DISPLAY variable." The exact error message may vary slightly.
			System.out.println("Unable to open browser: " + e);
		}
	}


}
