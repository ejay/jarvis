package Dropwizard.resources;

import javax.ws.rs.*;

/**
 * Created by Jorrit on 5-1-2016.
 */
@Path("/console")
public class ConsoleTimeResource {

	public ConsoleTimeResource() {}

	//test with: curl.exe -i -d "{'testing':'content'}" http://localhost:11000/console --header "Content-Type:application/json"
	@POST
	@Consumes("application/json , text/plain")//accept both declared json and plain text that may be json
	public void eatPosts(String post){
		//do something with your post
		System.out.println(post);

	}

	@GET
	@Produces("application/json")
	public String getPrefs(){
		return "{\"property\":\"alarm\",\"time\":\"8:00\"}";
	}
}
