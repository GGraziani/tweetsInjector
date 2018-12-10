package ch.usi.si.msde.ddm.tweetsinjector.logics;

import ch.usi.si.msde.ddm.tweetsinjector.entities.Graph;
import ch.usi.si.msde.ddm.tweetsinjector.entities.HashTag;

import com.mongodb.*;
import java.net.UnknownHostException;
import java.util.stream.Collectors;

public class MongodbInjector {

    private MongoClient mongoClient;
    private DB database;

    public MongodbInjector() throws UnknownHostException {
        mongoClient = new MongoClient("localhost");
        database = mongoClient.getDB("mongoTweet");
    }

    public void inject(Graph graph) {
        System.out.print("Injecting graph into MongoDB....");

        // Drop the previous database
        database.dropDatabase();

        database.createCollection("users", null);
        DBCollection users = database.getCollection(("users"));
        graph.getUsers().forEach(usr -> {

            BasicDBObject tUser = new BasicDBObject();

            tUser.put("uid", usr.getId());
            tUser.put("name", usr.getName());
            tUser.put("lang", usr.getLang());
            tUser.put("friends_count", usr.getFriendsCount());
            tUser.put("favourites_count", usr.getFavouritesCount());

            users.insert(tUser);
        });

        database.createCollection("tweets", null);
        DBCollection tweets = database.getCollection("tweets");
        graph.tweets.forEach(tweet -> {

            BasicDBObject tTweet = new BasicDBObject();

            tTweet.put("tid", tweet.getId());
            tTweet.put("authorId", tweet.getAuthor_id());
            tTweet.put("text", tweet.getText());
            tTweet.put("hashtags", tweet.getHashTags().stream().map(HashTag::getText).collect(Collectors.toList()));

            tTweet.put("mentions", tweet.getMentions());
            if(tweet.getLocation() != null){
                tTweet.put("location", tweet.getLocation().toString());
            }
            tTweet.put("created_at", tweet.getCreatedAt());

            tweets.insert(tTweet);
        });

        System.out.println("OK");
    }

}
