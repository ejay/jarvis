package gcal;

import com.google.api.client.http.HttpContent;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Jorrit on 19-1-2016.
 */
public class StupidContent implements HttpContent {

	public StupidContent(String content){
		this.content = content;
	}

	public StupidContent(){}

	String content = "client_id=491174436153-0pufbh7f345ab6k2mrjc1j31jre2j9eb.apps.googleusercontent.com&scope=https://www.googleapis.com/auth/calendar.readonly";

	@Override
	public long getLength() throws IOException {
		return content.getBytes().length;
	}

	@Override
	public String getType() {
		return "application/x-www-form-urlencoded";
	}

	@Override
	public boolean retrySupported() {
		return false;
	}

	@Override
	public void writeTo(OutputStream out) throws IOException {
		out.write(content.getBytes());
		out.flush();
	}
}
