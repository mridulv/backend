package hello;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import weka.core.Stopwords;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class sqlClass {
	String dbUrl ;
	String dbClass;
	String username;
	String password;
	String entity;
	long startTime;
	long endTime;
	String geo;
	String gender;
	long id;
	int pieVal;
	String init_query;
	
	public sqlClass(String Entity,long startTime,long endTime,String geo,String gender,long id,int hashPresent){
		
		this.dbUrl = "jdbc:mysql://localhost/test";
        this.dbClass = "com.mysql.jdbc.Driver";
        this.username = "root";
        this.password = "";
        this.entity = Entity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.gender = gender;
        this.geo = geo;
        this.id = id;
        this.pieVal = pieVal;
        
        if (hashPresent == 0){
        	init_query = "WHERE key_val LIKE '1"+String.valueOf(id)+"%'";
        }
        else{
        	init_query = "WHERE tweet LIKE '%"+entity+"%'";
        }
        
	}
	
	public sqlClass(String Entity,long startTime,long endTime,String geo,String gender,long id,int pieVal,int hashPresent){
		
		this.dbUrl = "jdbc:mysql://localhost/test";	
        this.dbClass = "com.mysql.jdbc.Driver";
        this.username = "root";
        this.password = "";
        this.entity = Entity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.gender = gender;
        this.geo = geo;
        this.id = id;
        this.pieVal = pieVal;
        
        if (hashPresent == 0){
        	init_query = "WHERE key_val LIKE '1"+String.valueOf(id)+"%'";
        }
        else{
        	init_query = "WHERE tweet LIKE '%"+entity+"%'";
        }
        
	}
	
	public JSONArray getResult() throws TwitterException, ClassNotFoundException, SQLException, JSONException{

        Class.forName(dbClass);
        Connection connection = (Connection) DriverManager.getConnection(dbUrl, username, password);
        
        Date startDate = new Date();
        //String query = "SELECT * FROM tweets WHERE key_val LIKE '"+String.valueOf(id)+"%' and seconds < " + endTime + " and seconds > " + startTime + " ";
        String query = "SELECT * FROM analysis_tweets_new "+init_query+" and seconds < " + endTime + " and seconds > " + startTime + " ";
        
        if (!geo.equals("World"))
        	query = query + " and country LIKE '"+geo+"%'";
        if (!gender.equals("all"))
        	query = query + " and groups LIKE '"+gender+"%'";
        
        System.out.println(query);
        Statement stmt = (Statement) connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        Date endDate = new Date();
        long msElapsedTime = startDate.getTime() - endDate.getTime();
        
        JSONArray json = new JSONArray();        
        
        while(rs.next()){
            JSONObject json_in = new JSONObject();
            
            String text = rs.getString("tweet");
            System.out.println(text);
            long seconds = rs.getLong("seconds");
            
            json_in.put("text", text);
    		json_in.put("second", seconds);
    		
    		json.put(json_in);  
        }
        
        
        return json;
	}
	
	public JSONObject getMapResult() throws TwitterException, ClassNotFoundException, SQLException, JSONException{

        Class.forName(dbClass);
        Connection connection = (Connection) DriverManager.getConnection(dbUrl, username, password);
        
        Date startDate = new Date();
        //String query = "SELECT * FROM tweets WHERE key_val LIKE '"+String.valueOf(id)+"%' and seconds < " + endTime + " and seconds > " + startTime + " ";
        String query = "SELECT count(*) AS val , GROUP_CONCAT(id SEPARATOR ', ') as ids, country FROM analysis_tweets_new "+init_query+" and seconds < " + endTime + " and seconds > " + startTime + " ";
        
        if (!geo.equals("World"))
        	query = query + " and country LIKE '"+geo+"%'";
        if (!gender.equals("all"))
        	query = query + " and groups LIKE '"+gender+"%'";
        
        query = query + " GROUP BY country";
        
        System.out.println(query);
        Statement stmt = (Statement) connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        Date endDate = new Date();
        long msElapsedTime = startDate.getTime() - endDate.getTime();
        
        JSONObject json = new JSONObject();        
        
        while(rs.next()){
            
            String text = rs.getString("ids");
            System.out.println(text);
            int occur = rs.getInt("val");
            String country = rs.getString("country");

            if (country.length() < 5 && country.length() > 0){
                country = country.substring(0,2);
            	json.put(country, occur);
            }
    		
        }
        
        
        return json;
	}
	
	public JSONArray getPieResult() throws TwitterException, ClassNotFoundException, SQLException, JSONException{

        Class.forName(dbClass);
        Connection connection = (Connection) DriverManager.getConnection(dbUrl, username, password);
        Statement stmt = (Statement) connection.createStatement();
        
        Date startDate = new Date();
        
        JSONArray mainjson = new JSONArray();
        
        if (pieVal == 0){
		    //String query = "SELECT * FROM tweets WHERE key_val LIKE '"+String.valueOf(id)+"%' and seconds < " + endTime + " and seconds > " + startTime + " ";
		    String query = "SELECT count(*) AS val , GROUP_CONCAT(id SEPARATOR ', ') as ids, country FROM analysis_tweets_new "+init_query+" and seconds < " + endTime + " and seconds > " + startTime + " ";
		    
		    if (!geo.equals("World"))
		    	query = query + " and country LIKE '"+geo+"%'";
		    if (!gender.equals("all"))
		    	query = query + " and groups LIKE '"+gender+"%'";
		    
		    query = query + " GROUP BY country";
		    
		    System.out.println(query);
		    
		    ResultSet rs = stmt.executeQuery(query);
		    
		    rs.last();
		    int total = rs.getRow();
		    rs.beforeFirst();
		
		    System.out.println(total);
		    
		    Date endDate = new Date();
		    long msElapsedTime = startDate.getTime() - endDate.getTime();        
		    
		    while(rs.next()){
			    JSONObject json = new JSONObject();
			    
		        String text = rs.getString("ids");
		        System.out.println(text);
		        int occur = rs.getInt("val");
		        String country = rs.getString("country");
		
		        if (country.length() < 5 && country.length() > 0){
		        	double final_occur = ((double)occur/(double)total)*100;
		            country = country.substring(0,2);
		        	json.put("name",country);
		        	json.put("y", final_occur);
		        	mainjson.put(json);
		        }
				
		    }
        }
        else {
        	String query = "SELECT * FROM analysis_tweets_new where key_val LIKE '120536157%'";

            ResultSet rs = stmt.executeQuery(query);

            Map<String, Integer> hashMap = new HashMap<String, Integer>();
            hashMap.put("Mon",0);
            hashMap.put("Tue",1);
            hashMap.put("Wed",2);
            hashMap.put("Thu",3);
            hashMap.put("Fri",4);
            hashMap.put("Sat",5);
            hashMap.put("Sun",6);

            Map<String, Integer> hashMap2 = new HashMap<String, Integer>();
            hashMap2.put("male",0);
            hashMap2.put("female",1);
            hashMap2.put("unknown",2);
            
            double gender[] = new double[3];
            double day[] = new double[7];

            while (rs.next()){
                String tim = rs.getString("timestamp");
                String gen = rs.getString("groups");
                double rating = rs.getDouble("rating");

                String dayVal = tim.split(" ")[0];
                Integer timeVal = Integer.valueOf(tim.split(" ")[3].substring(0,2));

                gender[hashMap2.get(gen)] = gender[hashMap2.get(gen)] + rating;
                day[hashMap.get(dayVal)] = day[hashMap.get(dayVal)] + rating;
            }
            
            if (pieVal == 1){
            	for(Map.Entry<String,Integer> entry : hashMap2.entrySet()){
            		JSONObject json = new JSONObject();
            		
            		String key = entry.getKey();
                	int value = entry.getValue();
                	
                	double val = gender[value];
                	
                	json.put("name",key);
		        	json.put("y",val);
		        	mainjson.put(json);
            	}
            }
            else if (pieVal == 2){
            	for(Map.Entry<String,Integer> entry : hashMap2.entrySet()){
            		JSONObject json = new JSONObject();
            		
            		String key = entry.getKey();
                	int value = entry.getValue();
                	
                	double val = gender[value];
                	
                	json.put("name",key);
		        	json.put("y",val);
		        	mainjson.put(json);
            	}
            }
        }
        
        System.out.println(mainjson.toString());
        return mainjson;
	}

	public JSONArray getTrendResult() throws ClassNotFoundException, SQLException, JSONException{
		
		Stopwords stopwords = new Stopwords();
		
        Class.forName(dbClass);
        Connection connection = (Connection) DriverManager.getConnection(dbUrl, username, password);
        
        Map<String, Integer> m1 = new HashMap<String, Integer>();
        
        Map<String, Double> m2 = new HashMap<String, Double>();
        helper bvc =  new helper(m2);
        TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);
        
        Date startDate = new Date();
        //String query = "SELECT * FROM tweets WHERE key_val LIKE '"+String.valueOf(id)+"%' and seconds < " + endTime + " and seconds > " + startTime + " ";
        String query = "SELECT * FROM analysis_tweets_new "+init_query+" and seconds < " + endTime + " and seconds > " + startTime + " ";
        
        if (!geo.equals("World"))
        	query = query + " and country LIKE '"+geo+"%'";
        if (!gender.equals("all"))
        	query = query + " and groups LIKE '"+gender+"%'";
        
        System.out.println(query);
        Statement stmt = (Statement) connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        Date endDate = new Date();
        long msElapsedTime = startDate.getTime() - endDate.getTime();
        
        JSONArray json = new JSONArray();        
        
        while(rs.next()){
            
            String text = rs.getString("tweet");
            text = text.toLowerCase().replaceAll(" http.*?\\s", " ").replaceAll("[^\\w\\s\\,]","").replaceAll(","," ").replaceAll("http\\s*(\\w+)","");
            
            String arrText[] = text.split(" ");
            for(String str:arrText){
            	if (str.equals("trends"))
            		System.out.println(rs.getString("id"));
                if (!stopwords.is(str)) {
	            	if (m1.containsKey(str)){
	            		int k = m1.get(str);
	            		m1.put(str, k+1);
	            	}
	            	else{
	            		m1.put(str, 1);
	            	}
                }
            }
        }
        
        System.out.println(m1.toString());
        
        for(String key : m1.keySet()){
        	   double value = m1.get(key);
        	   
               String queryNew = "SELECT * FROM term_frequency WHERE keyword LIKE '"+key+"' ";
               Statement stmtNew = (Statement) connection.createStatement();
               ResultSet rsNew = stmtNew.executeQuery(queryNew);
               
               if (rsNew.absolute(1)){
            	   long num = rsNew.getLong("occurence");
               	   //System.out.println(num + " " + value);
               	   value = value/num;
               	   //System.out.println(num + " " + value);
               	   m2.put(key,value);
               }
        }
        
        System.out.println(m2);
        sorted_map.putAll(m2);
        System.out.println(sorted_map);
        
        for(Map.Entry<String,Double> entry : sorted_map.entrySet()){
        	String key = entry.getKey();
        	double value = entry.getValue();
        	
	        if (key != null){
	     	   
	     	   JSONObject jsonObj = new JSONObject();
	     	   
	     	   jsonObj.put("keyword",key);
	     	   jsonObj.put("value",value);
	     	   
	     	   json.put(jsonObj);
	        }
        }
        
        System.out.println(json.toString());
        return json;
	}
	
public JSONArray getTrendResult2() throws ClassNotFoundException, SQLException, JSONException{
		
        Class.forName(dbClass);
        Connection connection = (Connection) DriverManager.getConnection(dbUrl, username, password);
        
        Map<String, Double> m1 = new HashMap<String, Double>();
        helper bvc =  new helper(m1);
        TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);
        
        Date startDate = new Date();
        //String query = "SELECT * FROM tweets WHERE key_val LIKE '"+String.valueOf(id)+"%' and seconds < " + endTime + " and seconds > " + startTime + " ";
        String query = "SELECT * FROM analysis_tweets WHERE key_val LIKE '1"+String.valueOf(id)+"%' and seconds < " + endTime + " and seconds > " + startTime + " ";
        
        if (!geo.equals("World"))
        	query = query + " and country LIKE '"+geo+"%'";
        if (!gender.equals("all"))
        	query = query + " and groups LIKE '"+gender+"%'";
        
        System.out.println(query);
        Statement stmt = (Statement) connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        Date endDate = new Date();
        long msElapsedTime = startDate.getTime() - endDate.getTime();
        
        JSONArray json = new JSONArray();        
        
        while(rs.next()){
        	String tweet = rs.getString("tweet");
        	Matcher matcher = Pattern.compile("#\\s*(\\w+)").matcher(tweet);
        	while (matcher.find()) {
        		String str = matcher.group(1);
        		str = str.toLowerCase();
        		if (m1.containsKey(str)){
            		double k = m1.get(str);
            		m1.put(str, (double) k+1);
            	}
            	else{
            		m1.put(str, 1.0);
            	}
        	}
        }
        
        sorted_map.putAll(m1);
        
        for(Map.Entry<String,Double> entry : sorted_map.entrySet()){
        	String key = entry.getKey();
        	double value = entry.getValue();
        	
	        if (key != null){
	     	   
	     	   JSONObject jsonObj = new JSONObject();
	     	   
	     	   jsonObj.put("keyword","#"+key);
	     	   jsonObj.put("value",value);
	     	   
	     	   json.put(jsonObj);
	        }
        }
        
        System.out.println(json.toString());
        return json;
	}

}