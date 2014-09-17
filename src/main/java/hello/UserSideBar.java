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
        
        String query = "SELECT tweet , COUNT( * ) , AVG( a.rating ) as aver , MAX( a.rating ) as max FROM ( SELECT *  FROM  final_tweet_analysis  WHERE user_id = " + id + " ) AS a";
        String query2 = "SELECT * FROM final_tweet_analysis WHERE  rating=(SELECT MAX(rating) FROM final_tweet_analysis where user_id =" + id + ") and user_id = " + id;
        System.out.println(query2);
        
        Statement stmt = (Statement) connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        Statement stmt2 = (Statement) connection.createStatement();
        ResultSet rs2 = stmt2.executeQuery(query2);
        
        rs2.next();
        
        String tweet;
        JSONObject json = new JSONObject();        
        while(rs.next()){
        	tweet = rs2.getString("tweet");
        	
        	double retweet = rs.getDouble("aver");
        	retweet = (2*retweet - 132.30818861707201)/1.88490419729401604;
        	long se_retweet = (long) retweet;
        	
        	double max = rs.getDouble("max");
        	max = (2*max - 132.30818861707201)/1.88490419729401604;
        	long se_max = (long)max;
        	
        	System.out.println(se_max);
        	
        	json.put("followers", followers);
        	json.put("image", url);
        	json.put("rating", se_retweet);
        	json.put("retweet", se_max);
        	json.put("tweet", tweet);
        	mainjson.put(json);
        }
        
        return mainjson;
	}
}
