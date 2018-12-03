package ch.usi.si.msde.ddm.tweetsinjector.logics;

import ch.usi.si.msde.ddm.tweetsinjector.entities.*;
import ch.usi.si.msde.ddm.tweetsinjector.io.Reader;
import ch.usi.si.msde.ddm.tweetsinjector.utils.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class GraphBuilder {

    private static Graph graph;
    private static ArrayList<String> badWords;
    private static ArrayList<File> files;

    public GraphBuilder(Params p) throws IOException{
        File data = new File(p.segments);
        files = Utils.sortSegments(new ArrayList<>(Arrays.asList(data.listFiles())));
        badWords = Utils.readFileToArrayList(p.lists+"/badWords");
        graph = new Graph();
    }

    public Graph buildGraph() {
        launchScan(files);
        return graph;
    }

    /**
     * Scans each line of the segment (json file) and discards obscene content
     * it and launches the "create entities and relations" procedure.
     */
    private static void launchScan(ArrayList<File> files) {
        System.out.print("Scanning data and creating entities/relations...");

        // For testing !!!!
        scanSegment(files.get(0));

//        files.forEach(GraphBuilder::scanSegment);

        System.out.println("OK\n\nFound:" +
                "\n\t- Locations: "+graph.locations.size()+
                "\n\t- Hashtags: "+graph.hashTags.size()+
                "\n\t- Users: "+graph.users.size()+
                "\n\t- Tweets: "+graph.tweets.size()+
                "\n");
    }

    private static void scanSegment(File file) {
        Reader reader = new Reader(file);
        String rawObj;
        JSONObject tweet;
        Boolean isExtended;

        try {
            while ((rawObj = reader.nextLine()) != null) {
                tweet = new JSONObject(rawObj);
                if (!Utils.isRetweet(tweet)) {
                    isExtended = Utils.isExtendedTweet(tweet);
                    if(!Utils.contains_obscenity(tweet, isExtended, badWords)){
                        createEntities(tweet, isExtended);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the tweet object and create all the entities and relations
     */
    private static void createEntities(JSONObject tweetObj, Boolean isExtended){

        ArrayList<HashTag> hashTags = getHashTags(tweetObj, isExtended);
        Location location = getLocation(tweetObj);
        User user = getUser(tweetObj);
        Tweet tweet = getTweet(tweetObj, user.getId(), hashTags, location, isExtended);

//        add from here

        addHashTags(hashTags);
        addLocation(location);
        addUser(user);
        addTweet(tweet);
    }

    /**
     * Extract the hashTags from the tweet object
     */
    private static ArrayList<HashTag> getHashTags(JSONObject tweet, Boolean isExtended) {

        ArrayList<HashTag> hashTags = new ArrayList<>();
        JSONObject obj = isExtended? new JSONObject(tweet.toString()).getJSONObject("extended_tweet") : new JSONObject(tweet.toString());

        obj.getJSONObject("entities").getJSONArray("hashtags").forEach(
                ht -> hashTags.add( new HashTag( ( (JSONObject) ht ).getString("text") ) ) );

        return hashTags;
    }

    private static void addHashTags(ArrayList<HashTag> hashTags){
        hashTags.forEach(hashTag -> Utils.addHashTag( hashTag, graph));
    }

    private static Location getLocation(JSONObject tweet){

        if(!tweet.get("place").toString().equals("null")){
            JSONObject place = tweet.getJSONObject("place");

            return new Location(place.getString("country_code"), place.getString("full_name"));
        }
        return null;
    }

    private static void addLocation(Location location){
        if (location != null)
            Utils.addLocation(location, graph);
    };

    private static User getUser(JSONObject tweet){
        JSONObject user = tweet.getJSONObject("user");
        return new User(
                user.getString("id_str"),
                user.getString("name"),
                tweet.getString("id_str"));
    }

    private static void addUser(User user){
        Utils.addUser(user, graph);
    }

    private static Tweet getTweet(JSONObject tweet, String authorId, ArrayList<HashTag> hashTags, Location location, Boolean isExtended) {

        String text = isExtended? tweet.getJSONObject("extended_tweet").getString("full_text") : tweet.getString("text");

        JSONArray user_mentions = isExtended?
                tweet.getJSONObject("extended_tweet").getJSONObject("entities").getJSONArray("user_mentions") :
                tweet.getJSONObject("entities").getJSONArray("user_mentions");

        ArrayList<String> mentions = new ArrayList<>();
        user_mentions.forEach( mention -> mentions.add( ( (JSONObject) mention ).getString("id_str") ) );



        return new Tweet(
                tweet.getString("id_str"),
                authorId, text,
                hashTags,
                mentions,
                location,
                tweet.getString("created_at"));
    }

    private static void addTweet(Tweet tweet){
        Utils.addTweet(tweet, graph);
    }

}
