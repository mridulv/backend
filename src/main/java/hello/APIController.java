package hello;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import twitter4j.*;

@Controller
@RequestMapping("/")
public class APIController {
	
	static int p = 0;
	static BlockingQueue<String> arr;
	long sinceId = 0;

    @RequestMapping(value="/graph",method = RequestMethod.GET)
    public @ResponseBody String todo(
    		@RequestParam(value="entity", required=false, defaultValue="apple") String entity,
            @RequestParam(value="start", required=false, defaultValue="2014-08-01") String start,
            @RequestParam(value="end", required=false, defaultValue="2014-08-02") String end ,
            @RequestParam(value="gender", required=false, defaultValue="all") String gender ,
            @RequestParam(value="geo", required=false, defaultValue="all") String country) throws ClassNotFoundException, SQLException, TwitterException, JSONException {
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
     
    	Date startDate;
    	Date endDate;
    	
    	JSONArray mainjson = new JSONArray();
    	JSONArray json = new JSONArray();
		try {
			startDate = formatter.parse(start);
	    	endDate = formatter.parse(end);
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	System.out.println("Time ananlysis for the Required Interval");
	    	
	    	App app = new App(entity,startDate,endDate,country,gender);
	    	json = app.getData();
	    	mainjson.put(json);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mainjson.toString();
    	//return new Greeting(counter.incrementAndGet(),String.format(template, name),"new program");
    }
    
    @RequestMapping(value="/map",method = RequestMethod.GET)
    public @ResponseBody String todo_map(
    		@RequestParam(value="entity", required=false, defaultValue="apple") String entity,
            @RequestParam(value="start", required=false, defaultValue="2014-08-01") String start,
            @RequestParam(value="end", required=false, defaultValue="2014-08-02") String end ,
            @RequestParam(value="gender", required=false, defaultValue="all") String gender ,
            @RequestParam(value="geo", required=false, defaultValue="all") String country) throws ClassNotFoundException, SQLException, TwitterException, JSONException {
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        
    	Date startDate;
    	Date endDate;
    	
    	JSONArray mainjson = new JSONArray();
    	JSONObject json = new JSONObject();
		try {
			startDate = formatter.parse(start);
	    	endDate = formatter.parse(end);
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	System.out.println("Time ananlysis for the Required Interval");
	    	
	    	App app = new App(entity,startDate,endDate,country,gender);
	    	json = app.getMap();
	    	mainjson.put(json);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mainjson.toString();
    	
    	//return new Greeting(counter.incrementAndGet(),String.format(template, name),"new program");
    }
    
    @RequestMapping(value="/pie",method = RequestMethod.GET)
    public @ResponseBody String todo_pie(
    		@RequestParam(value="pie", required=false, defaultValue="0") int pieVal,
    		@RequestParam(value="entity", required=false, defaultValue="apple") String entity,
            @RequestParam(value="start", required=false, defaultValue="2014-08-01") String start,
            @RequestParam(value="end", required=false, defaultValue="2014-08-02") String end ,
            @RequestParam(value="gender", required=false, defaultValue="all") String gender ,
            @RequestParam(value="geo", required=false, defaultValue="all") String country) throws ClassNotFoundException, SQLException, TwitterException, JSONException {
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        
    	Date startDate;
    	Date endDate;
    	
    	JSONArray json = new JSONArray();
		try {
			startDate = formatter.parse(start);
	    	endDate = formatter.parse(end);
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	System.out.println("Time ananlysis for the Required Interval");
	    	
	    	App app = new App(entity,startDate,endDate,country,gender,pieVal);
	    	json = app.getPie();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
    	
    	//return new Greeting(counter.incrementAndGet(),String.format(template, name),"new program");
    }
    
    @RequestMapping(value="/graphcompare",method = RequestMethod.GET)
    public @ResponseBody String todo(
    		@RequestParam(value="entity", required=false, defaultValue="twitter") String entity,
    		@RequestParam(value="entity2", required=false, defaultValue="facebook") String entity2,
            @RequestParam(value="start", required=false, defaultValue="2014-08-01") String start,
            @RequestParam(value="end", required=false, defaultValue="2014-08-02") String end ,
            @RequestParam(value="gender", required=false, defaultValue="all") String gender ,
            @RequestParam(value="geo", required=false, defaultValue="all") String country) throws ClassNotFoundException, SQLException, TwitterException, JSONException {
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
     
    	Date startDate;
    	Date endDate;
    	
    	JSONArray mainjson = new JSONArray();
    	
    	JSONArray json = new JSONArray();
    	JSONArray json2 = new JSONArray();
		try {
			startDate = formatter.parse(start);
	    	endDate = formatter.parse(end);
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	System.out.println("Time ananlysis for the Required Interval");
	    	
	    	App app = new App(entity,startDate,endDate,country,gender);
	    	App app2 = new App(entity2,startDate,endDate,country,gender);
	    	
	    	json = app.getData();
	    	json2 = app2.getData();
	    	
	    	mainjson.put(json);
	    	mainjson.put(json2);
	    	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mainjson.toString();
    	//return new Greeting(counter.incrementAndGet(),String.format(template, name),"new program");
    }
    
    @RequestMapping(value="/mapcompare",method = RequestMethod.GET)
    public @ResponseBody String todo_map(
    		@RequestParam(value="entity", required=false, defaultValue="twitter") String entity,
    		@RequestParam(value="entity2", required=false, defaultValue="facebook") String entity2,
            @RequestParam(value="start", required=false, defaultValue="2014-08-01") String start,
            @RequestParam(value="end", required=false, defaultValue="2014-08-02") String end ,
            @RequestParam(value="gender", required=false, defaultValue="all") String gender ,
            @RequestParam(value="geo", required=false, defaultValue="all") String country) throws ClassNotFoundException, SQLException, TwitterException, JSONException {
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
     
    	Date startDate;
    	Date endDate;
    	
    	JSONArray mainjson = new JSONArray();
    	
    	JSONObject json = new JSONObject();
    	JSONObject json2 = new JSONObject();
		try {
			startDate = formatter.parse(start);
	    	endDate = formatter.parse(end);
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	System.out.println("Time ananlysis for the Required Interval");
	    	
	    	App app = new App(entity,startDate,endDate,country,gender);
	    	App app2 = new App(entity2,startDate,endDate,country,gender);
	    	
	    	json = app.getMap();
	    	json2 = app2.getMap();
	    	
	    	mainjson.put(json);
	    	mainjson.put(json2);
	    	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mainjson.toString();
    	//return new Greeting(counter.incrementAndGet(),String.format(template, name),"new program");
    }
    
    @RequestMapping(value="/piecompare",method = RequestMethod.GET)
    public @ResponseBody String todo_pie(
    		@RequestParam(value="entity", required=false, defaultValue="twitter") String entity,
    		@RequestParam(value="entity2", required=false, defaultValue="facebook") String entity2,
            @RequestParam(value="start", required=false, defaultValue="2014-08-01") String start,
            @RequestParam(value="end", required=false, defaultValue="2014-08-02") String end ,
            @RequestParam(value="gender", required=false, defaultValue="all") String gender ,
            @RequestParam(value="geo", required=false, defaultValue="all") String country) throws ClassNotFoundException, SQLException, TwitterException, JSONException {
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
     
    	Date startDate;
    	Date endDate;
    	
    	JSONArray mainjson = new JSONArray();
    	
    	JSONObject json = new JSONObject();
    	JSONObject json2 = new JSONObject();
		try {
			startDate = formatter.parse(start);
	    	endDate = formatter.parse(end);
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	System.out.println("Time ananlysis for the Required Interval");
	    	
	    	App app = new App(entity,startDate,endDate,country,gender);
	    	App app2 = new App(entity2,startDate,endDate,country,gender);
	    	
	    	json = app.getMap();
	    	json2 = app2.getMap();
	    	
	    	mainjson.put(json);
	    	mainjson.put(json2);
	    	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mainjson.toString();
    	//return new Greeting(counter.incrementAndGet(),String.format(template, name),"new program");
    }
    
    @RequestMapping(value="/trending",method = RequestMethod.GET)
    public @ResponseBody String trends(
    		@RequestParam(value="entity", required=false, defaultValue="apple") String entity,
            @RequestParam(value="start", required=false, defaultValue="2014-08-01") String start,
            @RequestParam(value="end", required=false, defaultValue="2014-08-02") String end ,
            @RequestParam(value="gender", required=false, defaultValue="all") String gender ,
            @RequestParam(value="geo", required=false, defaultValue="all") String country) throws ClassNotFoundException, SQLException, TwitterException, JSONException {
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
     
    	Date startDate;
    	Date endDate;
    	
    	JSONArray json = new JSONArray();
		try {
			startDate = formatter.parse(start);
	    	endDate = formatter.parse(end);
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	System.out.println("Time ananlysis for the Required Interval");
	    	
	    	App app = new App(entity,startDate,endDate,country,gender);
	    	json = app.getTrends();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
    	//return new Greeting(counter.incrementAndGet(),String.format(template, name),"new program");
    }
        
//    @RequestMapping(value="/ticker",method = RequestMethod.GET)
//    public @ResponseBody String ticker(
//    		@RequestParam(value="entity", required=false, defaultValue="apple") String entity) throws TwitterException {
//    	Twitter twitter = TwitterFactory.getSingleton();
//        Query query = new Query("@"+entity);
//        query.count(1);
//        query.setSinceId(sinceId);
//        QueryResult result = twitter.search(query);
//        if (result.getTweets().size() > 0){
//        	System.out.print(result.getCount());
//	        sinceId = result.getMaxId();
//	        System.out.println(result.getTweets().get(0).getText());
//			return result.getTweets().get(0).getText();
//        }
//        else
//        	return "empty";
//    }
   
}