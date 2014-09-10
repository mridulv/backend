package hello;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.util.Pair;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

import com.mysql.jdbc.Connection;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class TweetRating {
	String entity;
	int hashPresent = 0;
	long id;
	long followers;
	User user;
	String url;
	String init_query;
	String tweet;
	int val;
	
	public TweetRating(String entity,String tweet) throws TwitterException{
		this.tweet = tweet;
		this.entity = entity;
		Twitter twitter = new TwitterFactory().getInstance();
        user = twitter.showUser(entity);
        followers = user.getFollowersCount();
        url = user.getProfileImageURL();
        id = user.getId();
	}
	
	public double getTweetRating() throws ClassNotFoundException, SQLException, InvalidFormatException, IOException, TwitterException{
		Class.forName(conn.dbClass);
        Connection connection = (Connection) DriverManager.getConnection(conn.dbUrl, conn.username, conn.password);

        InputStream modelIn = new FileInputStream("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\en-token.bin");
        TokenizerModel model = new TokenizerModel(modelIn);
        Tokenizer tokenizer = new TokenizerME(model);

        InputStream modelIn2 = new FileInputStream("C:\\Users\\mridul.v\\Downloads\\twitter_Project\\en-pos-maxent.bin");
        POSModel model2 = new POSModel(modelIn2);
        POSTaggerME tagger = new POSTaggerME(model2);

        String text = tweet;

        Estimate estimate = new Estimate();
        Pair<Double, Pair<Double, Double>> pairPair = estimate.getRetweetCount(connection,tokenizer,tagger,text);

        double c1 = 1.02903521e-03;
        double c2 = 1.96047162e+00;
        double c3 = 2.42172591e+00;
        double c4 = -5.12693532e-01;

        double estimateRating = c1*followers + c2*pairPair.getKey() + c3*pairPair.getValue().getKey() + c4*pairPair.getValue().getValue();
        estimateRating = (estimateRating*2 - 132.30818861707201)/(1.88490419729401604);
        return estimateRating;
	}
	
}
