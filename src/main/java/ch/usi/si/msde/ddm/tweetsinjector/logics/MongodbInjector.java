package ch.usi.si.msde.ddm.tweetsinjector.logics;

import ch.usi.si.msde.ddm.tweetsinjector.entities.Graph;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class MongodbInjector {

    private MongoClient mongoClient;
    private DB database;

    public MongodbInjector() throws UnknownHostException {

        mongoClient = new MongoClient("localhost");
        database = mongoClient.getDB("mongoTweet");
    }

    public void inject(Graph graph) {
        System.out.print("Injecting graph into MongoDB....");

        database.dropDatabase();

        HashMap<String, ArrayList<String>> usersTweets = new HashMap<>();

        database.createCollection("locations", null);
        DBCollection locations = database.getCollection("locations");
        graph.locations.forEach(location -> {
            BasicDBObject tLocation = new BasicDBObject();
            tLocation.put("name", location.getName());
            tLocation.put("code", location.getCode());
            locations.insert(tLocation);
        });

        database.createCollection("hashtags", null);
        DBCollection hashtags = database.getCollection("hashtags");
        graph.hashTags.forEach(hashTag -> {
            BasicDBObject tHashTag = new BasicDBObject();
            tHashTag.put("text", hashTag.getText());
            hashtags.insert(tHashTag);
        });

        database.createCollection("users", null);
        DBCollection users = database.getCollection(("users"));
        graph.users.forEach(usr -> {
            usersTweets.put(usr.getId(), usr.getTweetsIds());
            BasicDBObject tUser = new BasicDBObject();
            tUser.put("uid", usr.getId());
            tUser.put("name", usr.getName());
            users.insert(tUser);
        });

        database.createCollection("tweets", null);
        DBCollection tweets = database.getCollection("tweets");
        graph.tweets.forEach(tweet -> {
            BasicDBObject tTweet = new BasicDBObject();
            tTweet.put("tid", tweet.getId());
            tTweet.put("text", tweet.getText());
            tTweet.put("created_at", tweet.getCreatedAt());
            tweets.insert(tTweet);











        });

    }

}
