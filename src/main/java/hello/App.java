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
	
    public App(String Entity,Date startDate,Date endDate,String location, String gender) throws TwitterException {
    	this.startDate = startDate;
    	this.endDate = endDate;
    	this.location = location;
    	this.gender = gender;
    	this.entity = Entity;
    	this.location = location;
    	this.gender = gender;
    	
    	this.startTime = startDate.getTime()/1000;
    	this.endTime = endDate.getTime()/1000;
    	
        Twitter twitter = new TwitterFactory().getInstance();
        User user = twitter.showUser(entity);
        
        this.id = user.getId();
    	
    	this.sqlclass = new sqlClass(Entity,startTime,endTime,this.location,this.gender,this.id);
    	
    	System.out.println(startDate + " " + startTime);
    	System.out.println(endTime);
    }
    
    public App(String Entity,Date startDate,Date endDate,String location, String gender,int pieVal) throws TwitterException {
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
    	
        Twitter twitter = new TwitterFactory().getInstance();
        User user = twitter.showUser(entity);
        
        this.id = user.getId();
    	
    	this.sqlclass = new sqlClass(Entity,startTime,endTime,this.location,this.gender,this.id,this.pieVal);
    	
    	System.out.println(startDate + " " + startTime);
    	System.out.println(endTime);
    }
    
    public Date getstartDate() {
        return startDate;
    }
    
    // This is the function which needs to be overridden
    public JSONArray getData() throws ClassNotFoundException, SQLException, TwitterException, JSONException{
        
        Date startDate = new Date();
        
        int array[] = new int[numberOfEnty];
        
        JSONArray result = sqlclass.getResult();
        
        int count=0;
        int i = 0;
        
        
        for(i=0; i < result.length();i++){
        	
            JSONObject json = result.getJSONObject(i);
            long seconds = json.getLong("second");
            int val = (int)((((double)seconds-(double)startTime)/((double)endTime-(double)startTime))*numberOfEnty); 
            array[val] += 1;
            count++;
        }

        JSONArray json = new JSONArray();
    	
        int check = 0;
        
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

    public Date getendDate() {
        return endDate;
    }
    
	
}
