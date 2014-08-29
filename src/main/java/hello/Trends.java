package hello;

import org.json.simple.JSONArray;

public class Trends {
	private JSONArray json;
	public Trends(){
		this.json = new JSONArray();
	}
	
	public JSONArray getTrends(){
		return json;
	}
	
}
