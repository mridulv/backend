package hello;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONObject;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import twitter4j.JSONArray;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class UserSideBar {
	String entity;
	int hashPresent = 0;
	long id;
	long followers;
	User user;
	String url;
	String init_query;
	
	public UserSideBar(String name) throws TwitterException{
		this.entity = name;
		Twitter twitter = new TwitterFactory().getInstance();
        user = twitter.showUser(entity);
        followers = user.getFollowersCount();
        url = user.getProfileImageURL();
        id = user.getId();
	}
	
	public JSONArray getUserSideBar() throws ClassNotFoundException, SQLException{
		JSONArray user_rating = getUserRating();		
		return user_rating;
	}
	
	public JSONArray getUserRating() throws SQLException, ClassNotFoundException{
		JSONArray mainjson = new JSONArray();
		
		Class.forName(conn.dbClass);
        Connection connection = (Connection) DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);
        
        String query = "SELECT count(*) as total,MAX(retweet) as max ,tweet , AVG(retweet) as aver , AVG(rating) as rating  FROM analysis_tweets_new WHERE user_id = "+ id;
        
        System.out.println(query);
        Statement stmt = (Statement) connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        String tweet;
        JSONObject json = new JSONObject();        
        while(rs.next()){
        	double rating  = rs.getDouble("rating");
        	rating = (rating + (61.8149737505)*rating + 9972.69849548)/2;
        	double retweet = rs.getDouble("aver");
        	tweet = rs.getString("tweet");
        	json.put("followers", followers);
        	json.put("image", url);
        	json.put("rating", rating);
        	json.put("retweet", retweet);
        	json.put("tweet", tweet);
        	mainjson.put(json);
        }
        
        return mainjson;
	}
	
}
