package hello;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import opennlp.tools.util.InvalidFormatException;

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
    public @ResponseBody String todo_main(
    		@RequestParam(value="entity", required=false, defaultValue="apple") String entity,
            @RequestParam(value="start", required=false, defaultValue="2014-08-01") String start,
            @RequestParam(value="end", required=false, defaultValue="2014-08-02") String end ,
            @RequestParam(value="gender", required=false, defaultValue="all") String gender ,
            @RequestParam(value="geo", required=false, defaultValue="all") String country,
            @RequestParam(value="analysis", required=false, defaultValue="mention") String analysis) throws ClassNotFoundException, SQLException, TwitterException, JSONException {
    	
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
	    	
	    	App app = new App(entity,startDate,endDate,country,gender,analysis);
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
    public @ResponseBody String todo_mainMap(
    		@RequestParam(value="entity", required=false, defaultValue="apple") String entity,
            @RequestParam(value="start", required=false, defaultValue="2014-08-01") String start,
            @RequestParam(value="end", required=false, defaultValue="2014-08-02") String end ,
            @RequestParam(value="gender", required=false, defaultValue="all") String gender ,
            @RequestParam(value="geo", required=false, defaultValue="all") String country,
            @RequestParam(value="analysis", required=false, defaultValue="mention") String analysis) throws ClassNotFoundException, SQLException, TwitterException, JSONException {
    	
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
	    	
	    	App app = new App(entity,startDate,endDate,country,gender,analysis);
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
    public @ResponseBody String todo_mainPie(
    		@RequestParam(value="pie", required=false, defaultValue="0") int pieVal,
    		@RequestParam(value="entity", required=false, defaultValue="apple") String entity,
            @RequestParam(value="start", required=false, defaultValue="2014-08-01") String start,
            @RequestParam(value="end", required=false, defaultValue="2014-08-02") String end ,
            @RequestParam(value="gender", required=false, defaultValue="all") String gender ,
            @RequestParam(value="geo", required=false, defaultValue="all") String country,
            @RequestParam(value="analysis", required=false, defaultValue="mention") String analysis) throws ClassNotFoundException, SQLException, TwitterException, JSONException {
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	System.out.println(pieVal);
        
    	Date startDate;
    	Date endDate;
    	
    	JSONArray json = new JSONArray();
		try {
			startDate = formatter.parse(start);
	    	endDate = formatter.parse(end);
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	System.out.println("Time ananlysis for the Required Interval");
	    	
	    	App app = new App(entity,startDate,endDate,country,gender,pieVal,analysis);
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
            @RequestParam(value="geo", required=false, defaultValue="all") String country,
            @RequestParam(value="analysis", required=false, defaultValue="mention") String analysis) throws ClassNotFoundException, SQLException, TwitterException, JSONException {
    	
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
	    	
	    	App app = new App(entity,startDate,endDate,country,gender,analysis);
	    	App app2 = new App(entity2,startDate,endDate,country,gender,analysis);
	    	
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
            @RequestParam(value="geo", required=false, defaultValue="all") String country,
            @RequestParam(value="analysis", required=false, defaultValue="mention") String analysis) throws ClassNotFoundException, SQLException, TwitterException, JSONException {
    	
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
	    	
	    	App app = new App(entity,startDate,endDate,country,gender,analysis);
	    	App app2 = new App(entity2,startDate,endDate,country,gender,analysis);
	    	
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
    		@RequestParam(value="pie", required=false, defaultValue="0") int pieVal,
    		@RequestParam(value="entity", required=false, defaultValue="twitter") String entity,
    		@RequestParam(value="entity2", required=false, defaultValue="facebook") String entity2,
            @RequestParam(value="start", required=false, defaultValue="2014-08-01") String start,
            @RequestParam(value="end", required=false, defaultValue="2014-08-02") String end ,
            @RequestParam(value="gender", required=false, defaultValue="all") String gender ,
            @RequestParam(value="geo", required=false, defaultValue="all") String country,
            @RequestParam(value="analysis", required=false, defaultValue="mention") String analysis) throws ClassNotFoundException, SQLException, TwitterException, JSONException {
    	
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
	    	
	    	App app = new App(entity,startDate,endDate,country,gender,analysis);
	    	App app2 = new App(entity2,startDate,endDate,country,gender,analysis);
	    	
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
    
    @RequestMapping(value="/compare_timeline",method = RequestMethod.GET)
    public @ResponseBody String todo_compare(
    		@RequestParam(value="entity", required=false, defaultValue="twitter") String entities,
            @RequestParam(value="start", required=false, defaultValue="2014-08-01") String start,
            @RequestParam(value="end", required=false, defaultValue="2014-08-02") String end ,
            @RequestParam(value="gender", required=false, defaultValue="all") String gender ,
            @RequestParam(value="geo", required=false, defaultValue="all") String country,
            @RequestParam(value="analysis", required=false, defaultValue="mention") String analysis) throws ClassNotFoundException, SQLException, TwitterException, JSONException {
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
     
    	Date startDate;
    	Date endDate;
    	
    	String en[] = entities.split(",");
    	
    	JSONArray mainjson = new JSONArray();
    	
    	JSONArray json = new JSONArray();
		try {
			for (String entity : en){
				startDate = formatter.parse(start);
		    	endDate = formatter.parse(end);
		    	System.out.println(startDate);
		    	System.out.println(endDate);
		    	System.out.println("Time ananlysis for the Required Interval iisiiss");
		    	System.out.println(analysis);
		    	
		    	App app = new App(entity,startDate,endDate,country,gender,analysis);
		    	
		    	json = app.getData();
		    	mainjson.put(json);
			}
	    	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mainjson.toString();
    	//return new Greeting(counter.incrementAndGet(),String.format(template, name),"new program");
    }
    
    @RequestMapping(value="/compare_map",method = RequestMethod.GET)
    public @ResponseBody String todo_compare_map(
    		@RequestParam(value="entity", required=false, defaultValue="twitter") String entities,
            @RequestParam(value="start", required=false, defaultValue="2014-08-01") String start,
            @RequestParam(value="end", required=false, defaultValue="2014-08-02") String end ,
            @RequestParam(value="gender", required=false, defaultValue="all") String gender ,
            @RequestParam(value="geo", required=false, defaultValue="all") String country,
            @RequestParam(value="analysis", required=false, defaultValue="mention") String analysis) throws ClassNotFoundException, SQLException, TwitterException, JSONException {
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
     
    	Date startDate;
    	Date endDate;
    	
    	String en[] = entities.split(",");
    	
    	JSONArray mainjson = new JSONArray();
    	
    	JSONObject json = new JSONObject();
		try {
			for (String entity : en){
				startDate = formatter.parse(start);
		    	endDate = formatter.parse(end);
		    	System.out.println(startDate);
		    	System.out.println(endDate);
		    	System.out.println("Time ananlysis for the Required Interval");
		    	
		    	App app = new App(entity,startDate,endDate,country,gender,analysis);
		    	
		    	json = app.getMap();
		    	mainjson.put(json);
			}
	    	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mainjson.toString();
    	//return new Greeting(counter.incrementAndGet(),String.format(template, name),"new program");
    }
    
    @RequestMapping(value="/compare_pie",method = RequestMethod.GET)
    public @ResponseBody String todo_compare_pie(
    		@RequestParam(value="pie", required=false, defaultValue="0") int pieVal,
    		@RequestParam(value="entity", required=false, defaultValue="apple") String entities,
            @RequestParam(value="start", required=false, defaultValue="2014-08-01") String start,
            @RequestParam(value="end", required=false, defaultValue="2014-08-02") String end ,
            @RequestParam(value="gender", required=false, defaultValue="all") String gender ,
            @RequestParam(value="geo", required=false, defaultValue="all") String country,
            @RequestParam(value="analysis", required=false, defaultValue="mention") String analysis) throws ClassNotFoundException, SQLException, TwitterException, JSONException {
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
     
    	Date startDate;
    	Date endDate;
    	
    	String en[] = entities.split(",");
    	
    	JSONArray mainjson = new JSONArray();
    	
    	JSONArray json = new JSONArray();
		try {
			for (String entity : en){
				startDate = formatter.parse(start);
		    	endDate = formatter.parse(end);
		    	System.out.println(startDate);
		    	System.out.println(endDate);
		    	System.out.println("Time ananlysis for the Required Interval");
		    	
		    	App app = new App(entity,startDate,endDate,country,gender,pieVal,analysis);
		    	
		    	json = app.getPie();
		    	mainjson.put(json);
			}
	    	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mainjson.toString();
    	//return new Greeting(counter.incrementAndGet(),String.format(template, name),"new program");
    }
    
    @RequestMapping(value="/sidebar",method = RequestMethod.GET)
    public @ResponseBody String sidebar (
    		@RequestParam(value="entity", required=false, defaultValue="apple") String entity) throws ClassNotFoundException, SQLException, TwitterException, JSONException, InvalidFormatException, IOException {
     
    	Date startDate;
    	Date endDate;
    	
    	JSONArray json = new JSONArray();
    	
    	System.out.println("Time ananlysis for the Required Interval");
    	
    	SideBar sidebar = new SideBar(entity);
    	json = sidebar.getSideBar();
		
		return json.toString();
    	//return new Greeting(counter.incrementAndGet(),String.format(template, name),"new program");
    }
     
    @RequestMapping(value="/usersidebar",method = RequestMethod.GET)
    public @ResponseBody String userSideBar (
    		@RequestParam(value="entity", required=false, defaultValue="apple") String entity) throws ClassNotFoundException, SQLException, TwitterException, JSONException, InvalidFormatException, IOException {
     
    	Date startDate;
    	Date endDate;
    	
    	JSONArray json = new JSONArray();
    	
    	System.out.println("Time ananlysis for the Required Interval");
    	
    	UserSideBar sidebar = new UserSideBar(entity);
    	json = sidebar.getUserSideBar();
		
		return json.toString();
    	//return new Greeting(counter.incrementAndGet(),String.format(template, name),"new program");
    }
    
    @RequestMapping(value="/mainbar",method = RequestMethod.GET)
    public @ResponseBody String mainbar (
    		@RequestParam(value="entity", required=false, defaultValue="apple") String entity,
            @RequestParam(value="start", required=false, defaultValue="2014-08-01") String start,
            @RequestParam(value="end", required=false, defaultValue="2014-08-02") String end ,
            @RequestParam(value="gender", required=false, defaultValue="all") String gender ,
            @RequestParam(value="geo", required=false, defaultValue="all") String country) throws ClassNotFoundException, SQLException, TwitterException, JSONException, InvalidFormatException, IOException {
     
    	System.out.println(entity);
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
	    	
	    	MainBar app = new MainBar(entity,startDate,endDate,country,gender);
	    	json = app.getMainBar();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
    	//return new Greeting(counter.incrementAndGet(),String.format(template, name),"new program");
    }
    
    @RequestMapping(value="/usertweet",method = RequestMethod.GET)
    public @ResponseBody String usertweet (
    		@RequestParam(value="entity", required=false, defaultValue="apple") String entity,
    		@RequestParam(value="value", required=false, defaultValue="0") String val) throws ClassNotFoundException, SQLException, TwitterException, JSONException, InvalidFormatException, IOException {
    	
    	JSONArray json = new JSONArray();
		System.out.println("Time ananlysis for the Required Interval");
		
		int value = Integer.valueOf(val);
		UserGraph usergraph = new UserGraph(entity,value);
		json = usergraph.getUserGraph();
		return json.toString();
    }
    
    @RequestMapping(value="/tweetrating",method = RequestMethod.GET)
    public @ResponseBody String tweetRating (
    		@RequestParam(value="entity", required=false, defaultValue="apple") String entity,
    		@RequestParam(value="tweet", required=false, defaultValue="apple") String tweet) throws ClassNotFoundException, SQLException, TwitterException, JSONException, InvalidFormatException, IOException {
    	
		System.out.println("Time ananlysis for the Required Interval");
		
		TweetRating usergraph = new TweetRating(entity,tweet);
		double retweet_count = usergraph.getTweetRating();
		return String.valueOf(retweet_count);
    }
}