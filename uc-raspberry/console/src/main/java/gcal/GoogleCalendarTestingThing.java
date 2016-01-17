package gcal;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.Events;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created by Jorrit on 15-1-2016.
 */
public class GoogleCalendarTestingThing {
	private static final String APPLICATION_NAME = "RUG-jarvis/0.1";
	private static final java.io.File DATA_STORE_DIR =
			new java.io.File(System.getProperty("user.home"), ".store/calendar_sample");
	/**
	 * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
	 * globally shared instance across your application.
	 */
	private static FileDataStoreFactory dataStoreFactory;

	/** Global instance of the HTTP transport. */
	private static HttpTransport httpTransport;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private static com.google.api.services.calendar.Calendar client;
	//https://github.com/google/google-api-java-client-samples/tree/master/calendar-cmdline-sample/src/main/java/com/google/api/services/samples/calendar/cmdline
	//https://console.developers.google.com/home/dashboard?authuser=0&project=alien-marking-119110
	//https://developers.google.com/identity/protocols/googlescopes   relevant scope: https://www.googleapis.com/auth/calendar.readonly
	//https://developers.google.com/apis-explorer/#p/calendar/v3/calendar.events.list?calendarId=primary&maxResults=10&orderBy=startTime&singleEvents=true&timeMin=2016-01-14T00%253A00%253A00%252B02%253A00&_h=3&
	//https://developers.google.com/apis-explorer/#p/calendar/v3/calendar.calendarList.list?_h=1&
	public void doSomething(){
		Credential cred = null;

		AuthorizationCodeFlow acf = new AuthorizationCodeFlow();
		try {
			cred = acf.loadCredential("killernooby");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(cred != null){

		}else{
			acf.newAuthorizationUrl().setScopes();
		}

		GoogleAuthorizationCodeFlow gacf = new GoogleAuthorizationCodeFlow(httpTransport,)


		try {
			CalendarList feed = client.calendarList().list().execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Events feed = client.events().list(calendar.getId()).execute();
	}


}
