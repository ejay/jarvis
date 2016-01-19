package gcal;

import com.google.api.client.util.DateTime;

import java.io.*;
import java.time.Instant;
import java.util.TimeZone;

/**
 * Created by Jorrit on 19-1-2016.
 */
public class OAuthToken implements Serializable{
	private String access_token;
	private String token_type;
	private int expires_in;
	private String refresh_token;
	private DateTime tokenReceived;

	public OAuthToken(){}

	public OAuthToken(String access_token, String token_type, int expires_in, String refresh_token){
		this.setAccess_token(access_token);
		this.setToken_type(token_type);
		this.setExpires_in(expires_in);
		this.setRefresh_token(refresh_token);
	}

	public void store(){
		try (
				OutputStream file = new FileOutputStream("OAuth.token");
				OutputStream buffer = new BufferedOutputStream(file);
				ObjectOutput output = new ObjectOutputStream(buffer);
		){
			output.writeObject(this);
		} catch(IOException ex){
			ex.printStackTrace();
		}
	}

	public boolean load(){
		try(
				InputStream file = new FileInputStream("OAuth.token");
				InputStream buffer = new BufferedInputStream(file);
				ObjectInput input = new ObjectInputStream (buffer);
		){
			//deserialize the List
			OAuthToken token = (OAuthToken) input.readObject();
			//display its data
			this.setAccess_token(token.getAccess_token());
			this.setToken_type(token.getToken_type());
			this.setExpires_in(token.getExpires_in());
			this.setRefresh_token(token.getRefresh_token());
		}
		catch(ClassNotFoundException ex){
			ex.printStackTrace();
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		return (refresh_token != null);
	}

	public void tokenTime(){
		tokenReceived = new DateTime(Instant.now().toEpochMilli());
	}
	public String toString(){
		return "OAuthToken: access_token: " + getAccess_token() + "; token_type: " + getToken_type()
				+ "; expires_in: " + getExpires_in() + "; refresh_token: " + getRefresh_token() + ";";
	}


	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public DateTime getTokenReceived() {
		return tokenReceived;
	}

	public void setTokenReceived(DateTime tokenReceived) {
		this.tokenReceived = tokenReceived;
	}
}
