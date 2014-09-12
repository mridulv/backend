package hello;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class App {

	private Date startDate;
	private Date endDate;
	private String entity;
	private String location;
	private String gender;
	private JSONArray json;
	private long startTime;
	public static final int numberOfEnty = 500;
	private long endTime;
	private sqlClass sqlclass;
	private long id;
	private int pieVal;
	private String analysis;
	
    public App(String Entity,Date startDate,Date endDate,String location, String gender,String analysis) throws TwitterException {
    	this.startDate = startDate;
    	this.endDate = endDate;
    	this.location = location;
    	this.gender = gender;
    	this.entity = Entity;
    	this.location = location;
    	this.gender = gender;
    	this.analysis = analysis;
    	
    	int hashPresent = 0;
    	
    	this.startTime = startDate.getTime()/1000;
    	this.endTime = endDate.getTime()/1000;
    	
    	if (entity.charAt(0) == '@'){
    		Twitter twitter = new TwitterFactory().getInstance();
    		entity = entity.substring(1,entity.length());
            User user = twitter.showUser(entity);
            
            this.id = user.getId();
            hashPresent = 0;
    	}
    	else if(entity.charAt(0) == '#'){
    		entity = entity.substring(1,entity.length());
    		hashPresent = 1;
    	}
    	
    	this.sqlclass = new sqlClass(Entity,startTime,endTime,this.location,this.gender,this.id,hashPresent,analysis);
    	
    	System.out.println(startDate + " " + startTime);
    	System.out.println(endTime);
    }
    
    public App(String Entity,Date startDate,Date endDate,String location, String gender,int pieVal,String analysis) throws TwitterException {
    	this.startDate = startDate;
    	this.endDate = endDate;
    	this.location = location;
    	this.gender = gender;
    	this.entity = Entity;
    	this.location = location;
    	this.gender = gender;
    	this.pieVal = pieVal;
    	
    	this.startTime = startDate.getTime()/1000;
    	this.endTime = endDate.getTime()/1000;
    	
    	int hashPresent = 0;
    	
    	if (entity.charAt(0) == '@'){
    		Twitter twitter = new TwitterFactory().getInstance();
        	entity = entity.substring(1,entity.length());
            User user = twitter.showUser(entity);
            
            this.id = user.getId();
            hashPresent = 0;
    	}
    	else if(entity.charAt(0) == '#'){
        	entity = entity.substring(1,entity.length());
    		hashPresent = 1;
    	}
    	
    	this.sqlclass = new sqlClass(entity,startTime,endTime,this.location,this.gender,this.id,hashPresent,analysis);
    	
    	System.out.println(startDate + " " + startTime);
    	System.out.println(endTime);
    }
    
    public Date getstartDate() {
        return startDate;
    }
    
    // This is the function which needs to be overridden
    public JSONArray getData() throws ClassNotFoundException, SQLException, TwitterException, JSONException{
        
        Date startDate = new Date();
        
        double array[] = new double[numberOfEnty];
        
        JSONArray result = sqlclass.getResult();
        
        int count=0;
        int i = 0;
        
        
        for(i=0; i < result.length();i++){
        	
            JSONObject json = result.getJSONObject(i);
            long seconds = json.getLong("second");
            double value_main = Double.valueOf(json.getString("value"));
            int val = (int)((((double)seconds-(double)startTime)/((double)endTime-(double)startTime))*numberOfEnty); 
            array[val] += value_main;
            count++;
        }

        JSONArray json = new JSONArray();
    	
        double check = 0;
        
    	for (i = 0;i < array.length ;i ++){
    		
        	JSONObject json_in = new JSONObject();
    		json_in.put("id", 1);
    		json_in.put("val", array[i]);
    		json.put(json_in);   	
    		
    		check = check + array[i];
    	}
    	
        System.out.println(json.toString());
        
        return json;
    }
    
    public JSONObject getMap() throws ClassNotFoundException, SQLException, TwitterException, JSONException{
        
        JSONObject result = sqlclass.getMapResult();
        return result;
    }
    
    public JSONArray getPie() throws ClassNotFoundException, SQLException, TwitterException, JSONException{
        
        JSONArray result = sqlclass.getPieResult();
        return result;
    }
    
    public JSONArray getTrends() throws ClassNotFoundException, SQLException, JSONException{
    	JSONArray result = sqlclass.getTrendResult();
    	return result;
    }
    
    public JSONArray getJSON() throws JSONException{
    	// Method Signature to call the required Function to give the required sentiment analysis for the
    	// time period
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
    	System.out.println(" ");
    	System.out.println(" ");
    	System.out.println(" ");
    	System.out.println("Get the JSON Request for the specified interval" + formatter.format(startDate) + formatter.format(endDate));
    	json = new JSONArray();
    	
    	for (int i = 0;i < 100 ;i ++){
        	JSONObject json_in = new JSONObject();
    		json_in.put("id", 1);
    		json_in.put("time","2014-08-01:"+i );
    		json_in.put("val", i);
    		
    		json.put(json_in);
    	}
    	
    	return json;
    }
	
}
