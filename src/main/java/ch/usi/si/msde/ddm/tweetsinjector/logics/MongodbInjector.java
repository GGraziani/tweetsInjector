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

//        HashMap<String, ArrayList<String>> usersTweets = new HashMap<>();

        database.createCollection("locations", null);
        DBCollection locations = database.getCollection("locations");
        graph.locations.forEach(location -> {
            BasicDBObject loc = new BasicDBObject();
            loc.put("name", location.getName());
            loc.put("code", location.getCode());
            locations.insert(loc);
        });

        database.createCollection("hashtags", null);
        DBCollection hashtags = database.getCollection("hashtags");
        graph.hashTags.forEach(hashTag -> {
            BasicDBObject ht = new BasicDBObject();
            ht.put("text", hashTag.getText());
            hashtags.insert(ht);
        });


    }

}
