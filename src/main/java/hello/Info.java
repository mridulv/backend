package hello;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
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
import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import weka.core.Stopwords;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class Info {
	public JSONArray getKeywordInfo(String query) throws InvalidFormatException, IOException, SQLException, ClassNotFoundException, JSONException{
		InputStream modelIn = new FileInputStream("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\en-token.bin");
        TokenizerModel model = new TokenizerModel(modelIn);
        Tokenizer tokenizer = new TokenizerME(model);

        InputStream modelIn2 = new FileInputStream("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\en-pos-maxent.bin");
        POSModel model2 = new POSModel(modelIn2);
        POSTaggerME tagger = new POSTaggerME(model2);
        
		JSONArray mainjson = new JSONArray();
		
		Class.forName(conn.dbClass);
        Connection connection = (Connection) DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);
        
        System.out.println(query);
        Statement stmt = (Statement) connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        Map<String, Double> hashMap = new HashMap<String, Double>();
        Map<String, Double> hashMap2 = new HashMap<String, Double>();
        helper bvc =  new helper(hashMap);
        TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);
        
        Pair<Integer,Double> pair;

        int count = 0;

        Stopwords stopwords = new Stopwords();

        while (rs.next()) {
            String text = rs.getString("tweet");
            double rating = rs.getLong("rating");

            int keyword_value = 0;
            text = text.toLowerCase().replaceAll("@\\p{L}+","").replaceAll("#\\p{L}+", "").replaceAll("[^'\\w\\s\\,]", "").replaceAll("'\\p{L}+"," ").replaceAll("http\\s*(\\w+)", "");
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
      	    
            String queryNew = "SELECT * FROM keywords_new WHERE term LIKE '"+key+"' ";
            Statement stmtNew = (Statement) connection.createStatement();
            ResultSet rsNew = stmtNew.executeQuery(queryNew);
            
            if (rsNew.absolute(1)){
         	   double num = rsNew.getDouble("value");
         	   value = value/num;
         	   hashMap2.put(key,value);
            }
        }
        
        sorted_map.putAll(hashMap2);
        count = 0;
        
        for(String key : sorted_map.keySet()){
        	JSONObject json = new JSONObject();
        	double value = hashMap2.get(key);
        	json.put("key", key);
        	json.put("value", value);
        	mainjson.put(json);
        	
        	count++;
        	if (count == 5)
        		break;
        }
        
        return mainjson;
	}
	
	public JSONArray getInfo(String query) throws ClassNotFoundException, SQLException, JSONException{
		JSONArray mainjson = new JSONArray();
		
		Class.forName(conn.dbClass);
        Connection connection = (Connection) DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        Statement stmt = (Statement) connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        Map<String, Double> hashMap = new HashMap<String, Double>();
        Map<String, Double> hashMap2 = new HashMap<String, Double>();
        helper bvc =  new helper(hashMap);
        TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);

        int count = 0;

        Stopwords stopwords = new Stopwords();

        while (rs.next()) {
        	String text = rs.getString("tweet");
            double rating = rs.getLong("rating");
            Matcher matcher = Pattern.compile("#\\s*(\\w+)").matcher(text.toLowerCase());
            while (matcher.find()) {
                if (hashMap.containsKey(matcher.group(1))) {
                    double value = hashMap.get(matcher.group(1));
                    value = value + rating;
                    hashMap.put(matcher.group(1) , value);
                } else {
                    double value = rating;
                    hashMap.put(matcher.group(1),value);
                }
            }

        }
        
        for(String key : hashMap.keySet()){
      	   double value = hashMap.get(key);
      	   
             String queryNew = "SELECT * FROM hashtags WHERE term LIKE '"+key+"' ";
             Statement stmtNew = (Statement) connection.createStatement();
             ResultSet rsNew = stmtNew.executeQuery(queryNew);
             
             if (rsNew.absolute(1)){
          	   double num = rsNew.getDouble("value");
          	   value = value/num;
          	   hashMap2.put(key,value);
             }
         }
         
         sorted_map.putAll(hashMap2);
         
         count = 0;
         
         for(String key : sorted_map.keySet()){
         	JSONObject json = new JSONObject();
         	double value = hashMap2.get(key);
         	json.put("key", key);
         	json.put("value", value);
         	mainjson.put(json);
         	
         	count++;
         	if (count ==5)
         		break;
         }
         
		return mainjson;
	}
}
