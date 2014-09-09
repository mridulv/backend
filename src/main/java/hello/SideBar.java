package hello;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import ch.qos.logback.core.boolex.Matcher;

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
	
	public SideBar(String name) throws TwitterException{
		this.entity = name;
    	if (entity.charAt(0) == '@'){
    		Twitter twitter = new TwitterFactory().getInstance();
    		entity = entity.substring(1,entity.length());
            user = twitter.showUser(entity);
            
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
		InputStream modelIn = new FileInputStream("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\en-token.bin");
        TokenizerModel model = new TokenizerModel(modelIn);
        Tokenizer tokenizer = new TokenizerME(model);

        InputStream modelIn2 = new FileInputStream("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\en-pos-maxent.bin");
        POSModel model2 = new POSModel(modelIn2);
        POSTaggerME tagger = new POSTaggerME(model2);
        
		JSONArray mainjson = new JSONArray();
		
		Class.forName(conn.dbClass);
        Connection connection = (Connection) DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);
        
        String query = "SELECT * FROM analysis_tweets_new "+init_query+" and lang LIKE 'en'";
        Statement stmt = (Statement) connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        Map<String, Double> hashMap = new HashMap<String, Double>();
        helper bvc =  new helper(hashMap);
        TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);
        
        Pair<Integer,Double> pair;

        int count = 0;

        Stopwords stopwords = new Stopwords();

        while (rs.next()) {
            String text = rs.getString("tweet");
            double rating = rs.getLong("rating");

            int keyword_value = 0;
            text = text.toLowerCase().replaceAll("@\\p{L}+","").replaceAll("#\\p{L}+", "").replaceAll("[^'\\w\\s\\,]", "").replaceAll("http\\s*(\\w+)", "");
            String tokens[] = tokenizer.tokenize(text);
            String arrText[] = tagger.tag(tokens);

            for (String match : arrText) {
                java.util.regex.Matcher matcher = Pattern.compile("N\\s*(\\w+)").matcher(match);
                if (matcher.find()){
                    String group = tokens[keyword_value];
                    if (hashMap.containsKey(group)) {
                        double value = hashMap.get(group);
                        value = value + rating;
                        hashMap.put(group,value);
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
     	   
            String queryNew = "SELECT * FROM keyword_new WHERE term LIKE '"+key+"' ";
            Statement stmtNew = (Statement) connection.createStatement();
            ResultSet rsNew = stmtNew.executeQuery(queryNew);
            
            if (rsNew.absolute(1)){
         	   double num = rsNew.getDouble("value");
         	   value = value/num;
         	   hashMap.put(key,value);
            }
        }
        
        sorted_map.putAll(hashMap);
        
        count = 0;
        
        for(String key : hashMap.keySet()){
        	JSONObject json = new JSONObject();
        	double value = hashMap.get(key);
        	json.put("key", key);
        	json.put("value", value);
        	mainjson.put(json);
        	
        	count++;
        	if (count ==5)
        		break;
        }
		
		return mainjson;
	}
	
	public JSONArray getHashTagInformation(){
		JSONArray mainjson = new JSONArray();
		return mainjson;
	}
	
	public JSONArray getSideBar() throws ClassNotFoundException, JSONException, SQLException, InvalidFormatException, IOException{
		JSONArray entity_information = getEntityInformation();
		JSONArray trending_keyword = getKeywordInformation();
		JSONArray trending_hashtag = getHashTagInformation();
		return entity_information;
	}

	private Pair<Long, Long> getRank(long user_id) throws ClassNotFoundException, SQLException{
		Class.forName(conn.dbClass);
        Connection connection = (Connection) DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        String query = "SELECT mention_id,avg(rating) as total FROM analysis_tweets_new where key_val LIKE '1%' GROUP BY mention_id ORDER BY total DESC";
        Statement stmt = (Statement) connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        long count = 0;
        long rank = 0;

        String user = "MLStadium";

        System.out.println(count);
        while (rs.next()){
            count++;
            if (rs.getString("mention_id").equals(user)){
                rank = count;
            }
        }
        
        return new Pair<Long,Long>(rank,count);
	}
	
}