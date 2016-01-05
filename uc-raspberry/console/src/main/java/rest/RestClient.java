package rest;

import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Jorrit on 5-1-2016.
 */
public class RestClient {
	Client client;
	WebTarget target;
	public RestClient(){
		client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class)
				.build();
		target = client.target("http://localhost:11000").path("console");
	}

	public void connect(){
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(new ConsoleTimeMsg("alarm","8:00"),MediaType.APPLICATION_JSON_TYPE));

		if(response.getStatus() >= 200 && response.getStatus() < 300){
			System.out.println("Message sent successfully!");
		}else{
			System.out.println(response.getStatusInfo().toString());
		}
	}

	public void get(){
		Response response = target.request().get();
		if(response.getStatus() >= 200 && response.getStatus() < 300){
			String res = response.readEntity(String.class);
			System.out.println("MSG was:" + res);
		}else{
			System.out.println(response.getStatusInfo().toString());
		}

	}
}
