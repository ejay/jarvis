package gcal;

/**
 * Created by Jorrit on 19-1-2016.
 */
public class OAuthToken {
	private String access_token;
	private String token_type;
	private int expires_in;
	private String refresh_token;

	public OAuthToken(){}

	public OAuthToken(String access_token, String token_type, int expires_in, String refresh_token){
		this.setAccess_token(access_token);
		this.setToken_type(token_type);
		this.setExpires_in(expires_in);
		this.setRefresh_token(refresh_token);
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
}
