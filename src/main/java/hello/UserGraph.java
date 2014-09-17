package hello;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class UserGraph {
	String entity;
	int hashPresent = 0;
	long id;
	long followers;
	User user;
	String url;
	String init_query;
	int val;
	
	public UserGraph(String name,int value) throws TwitterException{
		this.val = value;
		this.entity = name;
		Twitter twitter = new TwitterFactory().getInstance();
        user = twitter.showUser(entity);
        followers = user.getFollowersCount();
        url = user.getProfileImageURL();
        id = user.getId();
	}
	
	public JSONArray getUserGraph() throws SQLException, ClassNotFoundException, JSONException{
		JSONArray mainjson = new JSONArray();
		
		Class.forName(conn.dbClass);
        Connection connection = (Connection) DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        String query = "SELECT * FROM final_tweet_analysis where user_id ="+id;

        Statement stmt = (Statement) connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        Map<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("Mon",0);
        hashMap.put("Tue",1);
        hashMap.put("Wed",2);
        hashMap.put("Thu",3);
        hashMap.put("Fri",4);
        hashMap.put("Sat",5);
        hashMap.put("Sun",6);

        double time[] = new double[24];
        double day[] = new double[7];

        while (rs.next()){
                String tim = rs.getString("timestamp");
                double rating = rs.getDouble("rating");

                String dayVal = tim.split(" ")[0];
                Integer timeVal = Integer.valueOf(tim.split(" ")[3].substring(0,2));

                time[timeVal] = time[timeVal] + rating;
                day[hashMap.get(dayVal)] = day[hashMap.get(dayVal)] + rating;
        }
        
        JSONArray json_time = new JSONArray();
        int count = 0;
        for (double a : time){
        	JSONObject json = new JSONObject();
        	json.put("name",String.valueOf(count+"-"+(count+1)));
        	json.put("y",a);
        	json_time.put(json);
        	count++;
        }
        
        JSONArray json_day = new JSONArray();
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
            
            JSONObject json = new JSONObject();
        	json.put("name",key);
        	json.put("y",day[value]);
        	json_day.put(json);
        }
        
        System.out.print(json_time);
        System.out.print(json_day);
        
        if (val == 0)
        	return json_time;
        else
        	return json_day;
	}
	
}
