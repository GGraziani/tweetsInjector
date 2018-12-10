package ch.usi.si.msde.ddm.tweetsinjector.logics;

import ch.usi.si.msde.ddm.tweetsinjector.entities.*;
import ch.usi.si.msde.ddm.tweetsinjector.io.Reader;
import ch.usi.si.msde.ddm.tweetsinjector.utils.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GraphBuilder {

    private static Graph graph = new Graph();
    private static List<String> badWords;
    private List<File> files;
    private Params p;

    public GraphBuilder(Params p) throws IOException{
        this.p =p;
        File data = new File(p.segments);
        File[] content = data.listFiles();
        if (content != null) {
            files = Utils.sortSegments(new ArrayList<>(Arrays.asList(content)));
            badWords = Utils.readFileToArrayList(p.lists+"/badWords");
        }
    }

    public Graph buildGraph() {
        launchScan(files, p);
        return graph;
    }

    /**
     * Scans each line of the segment (json file) and discards obscene content
     * it and launches the "create entities and relations" procedure.
     */
    private static void launchScan(List<File> files, Params p) {
        System.out.print("Scanning data and creating entities/relations...");

        if(p.test) {
            // For testing purposes

            for (int i = 0; i < 10; i++)
                scanSegment(files.get(i));
        }
        else {
            files.forEach(GraphBuilder::scanSegment);
        }

        System.out.println("OK\n\nFound:" +
                "\n\t- Locations: "+graph.getLocations().size()+
                "\n\t- Hashtags: "+graph.getHashTags().size()+
                "\n\t- Users: "+graph.getUsers().size()+
                "\n\t- Tweets: "+graph.getTweets().size()+
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
                    if(!Utils.containsObscenity(tweet, isExtended, badWords)){
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

        JSONObject jsObject = isExtended? new JSONObject(tweet.toString()).getJSONObject("extended_tweet") : new JSONObject(tweet.toString());
        JSONArray jsArray = jsObject.getJSONObject("entities").getJSONArray("hashtags");

        return (ArrayList<HashTag>) IntStream.range(0,jsArray.length()).mapToObj(i -> new HashTag(jsArray.getJSONObject(i).getString("text"))).collect(Collectors.toList());
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
    }

    private static User getUser(JSONObject tweet){
        JSONObject user = tweet.getJSONObject("user");

        return new User(
                user.getString("id_str"),
                user.getString("name"),
                tweet.getString("id_str"),
                user.getString("lang"),
                user.getInt("friends_count"),
                user.getInt("favourites_count"));
    }

    private static void addUser(User user){
        Utils.addUser(user, graph);
    }

    private static Tweet getTweet(JSONObject tweet, String authorId, ArrayList<HashTag> hashTags, Location location, Boolean isExtended) {

        String text = isExtended? tweet.getJSONObject("extended_tweet").getString("full_text") : tweet.getString("text");

        JSONArray jsArray = isExtended?
                tweet.getJSONObject("extended_tweet").getJSONObject("entities").getJSONArray("user_mentions") :
                tweet.getJSONObject("entities").getJSONArray("user_mentions");

        ArrayList<String> mentions = (ArrayList<String>) IntStream.range(0,jsArray.length()).mapToObj(i -> jsArray.getJSONObject(i).getString("id_str")).collect(Collectors.toList());

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
