package ch.usi.si.msde.ddm.tweetsinjector.logics;

import ch.usi.si.msde.ddm.tweetsinjector.entities.*;

import ch.usi.si.msde.ddm.tweetsinjector.utils.Utils;

import javafx.util.Pair;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkJobInfo;
import org.apache.spark.SparkStageInfo;
import org.apache.spark.api.java.JavaFutureAction;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.StructType;
import scala.Function1;
import scala.collection.mutable.WrappedArray;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class SparkInjector {

    private SparkSession spark;

    public void inject(Graph graph) {

        // get or create new spark session
        spark = ConnectService.getSparkSession();

        JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());

        System.out.print(Utils.separator() + "Creating dataframes...");

        Map<String, Dataset<Row>> entities = new HashMap<>();

        entities.put("locations", spark.createDataFrame(graph.getLocations(), Location.class));
        entities.put("hashtags", spark.createDataFrame(graph.getHashTags(), HashTag.class));
        entities.put("users", spark.createDataFrame(graph.getUsers(), User.class));
        entities.put("tweets", spark.createDataFrame(graph.getTweets(), Tweet.class));

        System.out.println(" OK" + Utils.separator());

        System.out.println("Created the following dataframes:");
        entities.keySet().forEach(k -> System.out.println("\t- " + k + " (" + entities.get(k).collectAsList().size() + " rows)"));

        runQueries(entities);
    }


    private void runQueries(Map<String,Dataset<Row>> entities){
        System.out.println(Utils.separator()+"Executing queries"+Utils.separator());

        firstQuery(entities);
        secondQuery(entities);
        thirdQuery(entities);
        fourthQuery(entities);
        fifthQuery(entities);
    }

    private void firstQuery(Map<String,Dataset<Row>> entities){
        long startTime;
        long stopTime;

        System.out.println("1. All the tweets posted from the United States");

        startTime = System.currentTimeMillis();
        Dataset<Row> usTweets = entities.get("tweets").filter("location LIKE '%(US)%'");
        stopTime = System.currentTimeMillis();
        System.out.println("Founded "+usTweets.collectAsList().size()+" total results. (Execution time ="+Utils.millisToSecAndMillis(stopTime - startTime)+")");
        usTweets.show();
    }

    private void secondQuery(Map<String, Dataset<Row>> entities) {
        long startTime;
        long stopTime;

        System.out.println("2. All the tweets that mention Theresa May");

        startTime = System.currentTimeMillis();
        Dataset<Row> tmTweets = entities.get("tweets").filter("text LIKE '%Theresa May%'");
        stopTime = System.currentTimeMillis();
        System.out.println("Founded "+tmTweets.collectAsList().size()+" total results. (Execution time ="+Utils.millisToSecAndMillis(stopTime - startTime)+")");
        tmTweets.show();
    }

    private void thirdQuery(Map<String, Dataset<Row>> entities){
        long startTime;
        long stopTime;

        System.out.println("3. List of the first 5 users which liked number of tweets from 60000 to 70000");

        startTime = System.currentTimeMillis();

        Dataset<Row> favUsers = entities.get("users").filter("favouritesCount BETWEEN 60000 AND 70000").select("favouritesCount","name");
        List<Row> arr = new ArrayList<>(favUsers.collectAsList());
        Collections.sort(arr, (a,b) -> a.getInt(0) >= b.getInt(0) ? -1 : 1);

        Dataset<Row> fiveUsers = spark.createDataFrame(arr.subList(0,5), new StructType()
                .add("favouritesCount", "int")
                .add("name", "string"));


        stopTime = System.currentTimeMillis();
        System.out.println("Founded "+fiveUsers.collectAsList().size()+" total results. (Execution time ="+Utils.millisToSecAndMillis(stopTime - startTime)+")");

        fiveUsers.show();
    }

    private void fourthQuery(Map<String,Dataset<Row>> entities){

        long startTime;
        long stopTime;

        System.out.println("4. Users who follow more than 60,000 other accounts and use english as default language");

        startTime = System.currentTimeMillis();
        Dataset<Row> usTweets = entities.get("users").filter("friendsCount > 60000 AND lang = 'en'");
        stopTime = System.currentTimeMillis();
        System.out.println("Founded "+usTweets.collectAsList().size()+" total results. (Execution time ="+Utils.millisToSecAndMillis(stopTime - startTime)+")");
        usTweets.show();
    }

    private void fifthQuery(Map<String,Dataset<Row>> entities){

        long startTime;
        long stopTime;

        System.out.println("5. Top 5 mentioned users");

        startTime = System.currentTimeMillis();

        Dataset<Row> usersWithMentions = entities.get("tweets").select("mentions").filter("size(mentions) != 0");
        List<Row> mentionsList = new ArrayList<>(usersWithMentions.collectAsList());
        Map<String, Integer> mentionsDict = new HashMap<>();

        mentionsList.forEach( row -> row.getList(0).forEach(el -> {
            if (mentionsDict.containsKey(el)) {
                mentionsDict.put( (String) el, mentionsDict.get(el)+1);
            } else {
                mentionsDict.put( (String) el, 1);
            }
        }));

        List<Pair> mentions = mentionsDict.keySet().stream().map( k -> new Pair(k, mentionsDict.get(k))).collect(Collectors.toList());
        Collections.sort(mentions, (a,b) -> {
            if((Integer) a.getValue() > (Integer) b.getValue())
                return -1;
            else if((Integer) a.getValue() < (Integer) b.getValue())
                return 1;
            else
                return 0;
        });

        Dataset<Row> mt = entities.get("users").filter("id = "+mentions.get(0).getKey()+
                        " OR id = "+mentions.get(1).getKey()+
                        " OR id = "+mentions.get(2).getKey()+
                        " OR id = "+mentions.get(3).getKey()+
                        " OR id = "+mentions.get(4).getKey());

        stopTime = System.currentTimeMillis();
        System.out.println("Founded "+mt.collectAsList().size()+" total results. (Execution time ="+Utils.millisToSecAndMillis(stopTime - startTime)+")");

        mt.show();
    }
}
