package hello;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.util.Pair;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import weka.core.Stopwords;

public class MainBar {
	
	private Date startDate;
	private Date endDate;
	private String entity;
	private String geo;
	private String gender;
	private JSONArray json;
	private long startTime;
	private long endTime;
	private sqlClass sqlclass;
	private long id;
	private int pieVal;
	private Info info;
	private int hashPresent;
	private String init_query;
	
	public MainBar(String Entity,Date startDate,Date endDate,String location, String gender) throws TwitterException {
    	this.startDate = startDate;
    	this.endDate = endDate;
    	this.geo = location;
    	this.gender = gender;
    	this.entity = Entity;
    	this.gender = gender;
    	this.info = new Info();
    	
    	hashPresent = 0;
    	
    	this.startTime = startDate.getTime()/1000;
    	this.endTime = endDate.getTime()/1000;
    	
    	System.out.println(Entity);
    	
    	if (entity.charAt(0) == '@'){
    		Twitter twitter = new TwitterFactory().getInstance();
    		entity = entity.substring(1,entity.length());
            User user = twitter.showUser(entity);
            
            this.id = user.getId();
            hashPresent = 0;
    	}
    	else if(entity.charAt(0) == '#'){
    		System.out.println("idhar hooaoaaaoa");
    		entity = entity.substring(1,entity.length());
    		hashPresent = 1;
    	}
    	
    	if (hashPresent == 0){
        	init_query = "WHERE key_val LIKE '1"+String.valueOf(id)+"%'";
        }
        else{
        	init_query = "WHERE tweet LIKE '%#"+entity+"%'";
        }
    }

	public JSONArray getMainBar() throws ClassNotFoundException, SQLException, JSONException, InvalidFormatException, IOException, TwitterException{
		JSONArray mainjson = new JSONArray();
		JSONArray keyword_info = getKeywordInformation();
		JSONArray hashtag_info = getHashTagInformation();
		JSONArray influential_users = getInfluentialUsers();
		JSONArray correlated_users = getCorrelatedUsers();
		
		mainjson.put(keyword_info);
		mainjson.put(hashtag_info);
		mainjson.put(influential_users);
		mainjson.put(correlated_users);
		return mainjson;
	}
	
	public JSONArray getKeywordInformation() throws JSONException, ClassNotFoundException, SQLException, InvalidFormatException, IOException{
        
		String query = "SELECT * FROM analysis_tweets_new "+init_query+" and seconds < " + endTime + " and seconds > " + startTime + " ";
        System.out.println(query);
        
        if (!geo.equals("world"))
        	query = query + " and country LIKE '"+geo+"%'";
        if (!gender.equals("all"))
        	query = query + " and groups LIKE '"+gender+"%'";
        
		JSONArray mainjson = info.getKeywordInfo(query);
		return mainjson;
	}
	
	public JSONArray getHashTagInformation() throws ClassNotFoundException, SQLException, JSONException{
        
		String query = "SELECT * FROM analysis_tweets_new "+init_query+" and seconds < " + endTime + " and seconds > " + startTime + " ";
		System.out.println(query);
        
        if (!geo.equals("world"))
        	query = query + " and country LIKE '"+geo+"%'";
        if (!gender.equals("all"))
        	query = query + " and groups LIKE '"+gender+"%'";
      
        JSONArray mainjson = info.getInfo(query);
        return mainjson;
	}
	
	public JSONArray getInfluentialUsers() throws SQLException, ClassNotFoundException, JSONException, TwitterException{
		JSONArray mainjson = new JSONArray();
		
        Class.forName(conn.dbClass);
        Connection connection = (Connection) DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        Map<Long, Long> hashMap = new HashMap<Long, Long>();
        helper2 bvc = new helper2(hashMap);
        TreeMap<Long, Long> sorted_map = new TreeMap<Long, Long>(bvc);

        String query = "SELECT count(*) as total, avg(rating) as rating_avg, followers , user_id from analysis_tweets_new "+init_query+" GROUP BY user_id";
        System.out.println(query);
        Statement stmt = (Statement) connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()){
            long user_id = rs.getLong("user_id");
            double rating_avg = rs.getLong("rating_avg");

            double normalized_rating = (rating_avg + (61.8149737505)*rating_avg + 9972.69849548)/2;
            hashMap.put(user_id,(long)rating_avg);
        }

        sorted_map.putAll(hashMap);
        
        int count = 0;
        
        for (Map.Entry<Long , Long> entry : sorted_map.entrySet()) {
            if (count == 5)
                break;
            System.out.println("the maximum influential user id " + entry.getKey() + " with a score of " + entry.getValue());
            JSONObject json = new JSONObject();
            if (!getUser(entry.getKey()).equals("unknown")){
	            json.put("user", getUser(entry.getKey()));
	            json.put("score", entry.getValue());
	            mainjson.put(json);
	            count++;
            }
        }
		
		return mainjson;
	}
	
	public JSONArray getCorrelatedUsers() throws ClassNotFoundException, SQLException, InvalidFormatException, IOException, JSONException, TwitterException{
		JSONArray mainjson = new JSONArray();
		
		Class.forName(conn.dbClass);
        Connection connection2 = (Connection) DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        java.util.Date startDate = new java.util.Date();

        Map<Long,Double> hashMap = new HashMap<Long, Double>();
        helper4 bvc =  new helper4(hashMap);
        TreeMap<Long,Double> sorted_map = new TreeMap<Long,Double>(bvc);

        Map<String,Double> myMap = getEntityVector();

        String queryAux = "SELECT * from user_vector";
        Statement stmtAux = (Statement) connection2.createStatement();
        ResultSet rsAux = stmtAux.executeQuery(queryAux);

        while (rsAux.next()) {
            long user = rsAux.getLong("user_id");
            long rating = rsAux.getLong("rating");

            Map<String, Double> myMapAux = new HashMap<String, Double>();
            String sAux = rsAux.getString("keyword_vector");
            sAux = sAux.substring(1, sAux.length() - 1);
            if (!sAux.equals("")) {
                String[] pairsAux = sAux.split(",");
                for (int i = 0; i < pairsAux.length / 2; i++) {
                    String pair = pairsAux[i];
                    String[] keyValue = pair.split(":");
                    int val = keyValue[0].length();
                    myMapAux.put(keyValue[0].substring(1,val - 1), Double.valueOf(keyValue[1]));
                }

                double score = 0;

                for (Map.Entry<String, Double> entry : myMap.entrySet()) {
                    String key = entry.getKey();
                    double value = entry.getValue();
                    if (myMapAux.containsKey(key)) {
                        score = score + myMapAux.get(key) * value;
                    }
                }

                score = score * rating;

                hashMap.put(user , score);
            }
        }

        sorted_map.putAll(hashMap);
        System.out.println(sorted_map.toString());

        int count = 0 ;

        for (Map.Entry<Long , Double> entry : sorted_map.entrySet()) {
            String query2 = "SELECT * FROM analysis_tweets_new WHERE key_val LIKE '1"+entity+"%' and user_id = " +entry.getKey();
            Statement stmt2 = (Statement) connection2.createStatement();
            ResultSet rs2 = stmt2.executeQuery(query2);
            if (count == 5)
                break;
            if (!rs2.next()){
                System.out.println("the maximum correlated user id " + entry.getKey() + " with a score of " + entry.getValue());
                JSONObject json = new JSONObject();
                if (!getUser(entry.getKey()).equals("unknown")){
	                json.put("user", getUser(entry.getKey()));
	                json.put("score", entry.getValue());
	                mainjson.put(json);
	                count++;
                }
            }
        }
		return mainjson;
	}
	
	private TreeMap getEntityVector() throws InvalidFormatException, IOException, ClassNotFoundException, SQLException{
		
        InputStream modelIn = new FileInputStream("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\en-token.bin");
        TokenizerModel model = new TokenizerModel(modelIn);
        Tokenizer tokenizer = new TokenizerME(model);

        InputStream modelIn2 = new FileInputStream("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\en-pos-maxent.bin");
        POSModel model2 = new POSModel(modelIn2);
        POSTaggerME tagger = new POSTaggerME(model2);

        Class.forName(conn.dbClass);
        Connection connection = (Connection) DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);
        Connection connection2 = (Connection) DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        String query = "SELECT * from analysis_tweets_new "+init_query+" and lang LIKE 'en'";
        System.out.println(query);

        Statement stmt = (Statement) connection.createStatement();
        stmt.setFetchSize(Integer.MIN_VALUE);
        ResultSet rs = stmt.executeQuery(query);

        int count = 0;

        Map<String, Double> hashMap = new HashMap<String, Double>();
        Map<String, Double> hashMap2 = new HashMap<String, Double>();
        helper3 bvc = new helper3(hashMap2);
        TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);

        while (rs.next()) {
            count++;
            String arr = rs.getString("tweet");
            double rating = rs.getDouble("rating");

            int keyword_value = 0;
            arr = arr.toLowerCase().replaceAll("@\\p{L}+","").replaceAll("#\\p{L}+", "").replaceAll("[^'\\w\\s\\,]", "").replaceAll("'\\p{L}+"," ").replaceAll("'"," ").replaceAll("http\\s*(\\w+)", "");
            String tokens[] = tokenizer.tokenize(arr);
            String arrText[] = tagger.tag(tokens);

            for (String match : arrText) {
                Matcher matcher = Pattern.compile("N\\s*(\\w+)").matcher(match);
                if (matcher.find()){
                    String group = tokens[keyword_value];
                    if (hashMap.containsKey(group)) {
                        double value = hashMap.get(group);
                        value = value + rating;
                        hashMap.put(group, value);
                    } else {
                        double value = rating;
                        hashMap.put(group, value);
                    }
                }
                keyword_value++;
            }
        }

        for(String key : hashMap.keySet()){
            double value = hashMap.get(key);

            String queryNew = "SELECT * FROM keywords_new WHERE term LIKE '"+key+"' ";
            Statement stmtNew = (Statement) connection2.createStatement();
            ResultSet rsNew = stmtNew.executeQuery(queryNew);

            if (rsNew.absolute(1)){
                double num = rsNew.getDouble("value");
                value = value/num;
                hashMap2.put(key,value);
            }
        }

        sorted_map.putAll(hashMap2);
        System.out.println(sorted_map);
        return sorted_map;
    }
	
	private String getUser(long id){
		Twitter twitter = new TwitterFactory().getInstance();
		System.out.println(id);
		User user;
		try {
			user = twitter.showUser(id);
			return user.getScreenName();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "unknown";
		}
	}
	
}
