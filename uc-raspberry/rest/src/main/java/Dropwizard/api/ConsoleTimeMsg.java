package Dropwizard.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jorrit on 5-1-2016.
 */
public class ConsoleTimeMsg {
	private String property;//whether it is to set alarm or time before calendar events or something else
	private String time;// time to set, format depends on affected property.


	public ConsoleTimeMsg(String property,String time){
		this.property = property;
		this.time = time;
	}

	@JsonProperty
	public String getProperty() {
		return property;
	}

	@JsonProperty
	public String getTime(){
		return time;
	}

	@JsonProperty
	public void setProperty(String property){
		this.property = property;
	}
	@JsonProperty
	public void setTime(String time){
		this.time = time;
	}

}
