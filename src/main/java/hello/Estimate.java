package hello;

import javafx.scene.paint.Stop;
import javafx.util.Pair;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import weka.core.Stopwords;

import javax.xml.crypto.dom.DOMCryptoContext;
import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Estimate {

    public static double findRelation(String tweet ,Tokenizer tokenizer,POSTaggerME tagger , Connection connection) throws SQLException {

        tweet = tweet.toLowerCase().replaceAll("@\\p{L}+","").replaceAll("#\\p{L}+", "").replaceAll("[^'\\w\\s\\,]", "").replaceAll("http\\s*(\\w+)", "");
        String tokens[] = tokenizer.tokenize(tweet);
        String arrText[] = tagger.tag(tokens);

        double score = 0;
        int count = 0;
        int keyword_value = 0;
        for (String match : arrText) {
            Matcher matcher = Pattern.compile("N\\s*(\\w+)").matcher(match);
            if (matcher.find()) {
                String group = tokens[keyword_value];
                System.out.println(tokens[keyword_value]);
                String query = "SELECT * FROM keywords_new where term = '"+group+"' and count_val > 1";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next())
                {
                    double newScore = rs.getDouble("value");
                    long val = rs.getLong("count_val");

                    score = score + newScore/(val-1);
                }
                count++;
            }
            keyword_value++;
        }

        if (count == 0)
            return score;
        else
            return score/count;
    }

    public static Double findRelation2(String tweet,Connection connection) throws SQLException {

        Matcher matcher = Pattern.compile("#\\s*(\\w+)").matcher(tweet);
        double score = 0;
        int count  = 0;
        while (matcher.find()) {
            String query = "SELECT * FROM hashtags where term = '"+matcher.group(1)+"' and count_val > 1";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next())
            {
                double newScore = rs.getDouble("value");
                long val = rs.getLong("count_val");

                score = score + newScore/(val-1);
            }
            count++;
        }

        if (count == 0)
            return score;
        else
            return score/count;
    }

    public static double findRelation3(String text ,Connection connection) throws SQLException {

        Matcher matcher = Pattern.compile("@\\s*(\\w+)").matcher(text);
        double score = 0;
        int count = 0;
        while (matcher.find()) {
            String query = "SELECT * FROM mentions where term = '"+matcher.group(1)+"' and count_val > 1";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next())
            {
                //System.out.println("MENTION FOUND");
                double newScore = rs.getDouble("value");
                long val = rs.getLong("count_val");

                score = score + newScore/(val-1);
            }
            count++;
        }

        //System.out.println(score);
        if (count == 0)
            return score;
        else
            return score/count;
    }

    public static Pair<Double,Pair<Double,Double>> getRetweetCount(Connection connection,Tokenizer tokenizer,POSTaggerME tagger,String tweet) throws SQLException {

        double val1 = findRelation(tweet.toLowerCase(),tokenizer,tagger,connection);
        double val2 = findRelation2(tweet.toLowerCase(),connection);
        double val3 = findRelation3(tweet.toLowerCase(),connection);

        System.out.println(val1 + " " + val2 + " " + val3);
        Pair<Double,Pair<Double,Double>> pairPair = new Pair<Double, Pair<Double, Double>>(val1,new Pair<Double,Double>(val2,val3));

        return pairPair;
    }

}

