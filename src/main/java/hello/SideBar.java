package hello;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javafx.util.*;


public class SideBar {
	String entity;
	int hashPresent = 0;
	long id;
	User user;
	String init_query;
	Info info;
	
	public SideBar(String name) throws TwitterException{
		this.entity = name;
		this.info = new Info();
    	if (entity.charAt(0) == '@'){
    		Twitter twitter = new TwitterFactory().getInstance();
    		entity = entity.substring(1,entity.length());
            user = twitter.showUser(entity);
            
            id = user.getId();
            
            hashPresent = 0;
    	}
    	else if(entity.charAt(0) == '#'){
    		entity = entity.substring(1,entity.length());
    		hashPresent = 1;
    	}
    	
    	if (hashPresent == 0){
        	init_query = "WHERE key_val LIKE '1"+String.valueOf(id)+"%'";
        }
        else{
        	init_query = "WHERE tweet LIKE '%"+entity+"%'";
        }
	}
	
	public JSONArray getEntityInformation() throws JSONException, ClassNotFoundException, SQLException{
		JSONArray mainjson = new JSONArray();
		JSONObject json = new JSONObject();
		
		if (hashPresent == 0){
			json.put("name", user.getName());
			json.put("desc", user.getDescription());
			json.put("followers", user.getFollowersCount());
			json.put("image", user.getProfileImageURL());
			json.put("rank", getRank(user.getId()));
		}
		else {
			json.put("name", this.entity);
		}
		
		 return mainjson.put(json);
	}
	
	public JSONArray getKeywordInformation() throws JSONException, ClassNotFoundException, SQLException, InvalidFormatException, IOException{
        String query = "SELECT * FROM analysis_tweets_new "+init_query+" and lang LIKE 'en'";
		JSONArray mainjson = info.getKeywordInfo(query);
		return mainjson;
	}
	
	public JSONArray getHashTagInformation() throws ClassNotFoundException, SQLException, JSONException{
        String query = "SELECT * FROM analysis_tweets_new "+init_query;
        JSONArray mainjson = info.getInfo(query);
        return mainjson;
	}
	
	public JSONArray getTrendingInformation() throws ClassNotFoundException, SQLException, JSONException{
		String query = "SELECT * FROM analysis_tweets_new ORDER BY seconds DESC LIMIT 1000";
		JSONArray mainjson = info.getInfo(query);
		return mainjson;
	}
	
	public JSONArray getSideBar() throws ClassNotFoundException, JSONException, SQLException, InvalidFormatException, IOException{
		JSONArray entity_information = getEntityInformation();
		JSONArray trending_keyword = getKeywordInformation();
		JSONArray trending_hashtag = getHashTagInformation();
		JSONArray trends = getTrendingInformation();
		
		System.out.print(trending_keyword);
		System.out.print(trending_hashtag);
		System.out.print(trends);
		
		JSONArray mainjson = new JSONArray();
		mainjson.put(entity_information);
		mainjson.put(trending_keyword);
		mainjson.put(trending_hashtag);
		mainjson.put(trends);
		
		return mainjson;
	}

	private Pair<Long, Long> getRank(long user_id) throws ClassNotFoundException, SQLException{
		Class.forName(conn.dbClass);
        Connection connection = (Connection) DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        String query = "SELECT mention_id,avg(rating) as total FROM analysis_tweets_new where key_val LIKE '1%' GROUP BY mention_id ORDER BY total DESC";
        Statement stmt = (Statement) connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        long count = 0;
        long rank = 0;

        System.out.println(count);
        while (rs.next()){
            count++;
            if (rs.getString("mention_id").equals(entity)){
                rank = count;
            }
        }
        
        return new Pair<Long,Long>(rank,count);
	}
	
}
