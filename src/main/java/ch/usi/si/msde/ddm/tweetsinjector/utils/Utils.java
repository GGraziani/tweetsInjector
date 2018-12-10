package ch.usi.si.msde.ddm.tweetsinjector.utils;

import ch.usi.si.msde.ddm.tweetsinjector.entities.*;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Utils {

    private Utils(){
        // Nothing to initialize
    }

    public static void parseArgs(String[] args, Params p){
        for(String s: args){

            String[] str = new String[2];
            if (s.contains("="))
                str = s.split("=");
            else
                str[0] = s;

            switch (str[0].toLowerCase()) {
                case "-db":
                    DB database = p.matchStr(str[1]);
                    if(database != null)
                        p.database = database;
                    break;
                case "-test":
                    p.test = true;
                    break;
                default:
                    break;
            }
        }
    }

    public static List<File> sortSegments(List<File> files) {
        files.sort((file1, file2) -> Integer.parseInt(file1.getName().substring(file1.getName().indexOf('-')+1)) - Integer.parseInt(file2.getName().substring(file2.getName().indexOf('-')+1)));
        return files;
    }

    public static List<String> readFileToArrayList(String path) throws IOException {
        BufferedReader bufReader = new BufferedReader(new FileReader(path));
        ArrayList<String> lines = new ArrayList<String>();
        String line = bufReader.readLine();
        while (line != null) {
            lines.add(line);
            line = bufReader.readLine();
        }
        bufReader.close();

        return lines;
    }

    public static Boolean isRetweet(JSONObject tweet) {
        return tweet.has("retweeted_status");
    }

    /**
     * Checks whether the tweet text contains some bad words and return true in case
     */
    public static boolean containsObscenity(JSONObject tweet, Boolean isExtended, List<String> badWords){
        JSONObject obj = new JSONObject(tweet.toString());
        String text;
        if (isExtended) {
            text = obj.getJSONObject("extended_tweet").getString("full_text");
        } else {
            text = obj.getString("text");
        }
        return Utils.findStringMatch(text.toLowerCase(), badWords);
    }

    /**
     * Checks whether the tweet contains an "extended_tweet" field
     */
    public static Boolean isExtendedTweet(JSONObject tweet) {
        return tweet.has("extended_tweet");
    }

    private static boolean findStringMatch(String text, List<String> list){
        for (String s: list) {
            if(text.contains(s.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public static void addHashTag(HashTag hashtag, Graph g){

        if(!containsHashTag(g.getHashTags(),hashtag))
            g.getHashTags().add(hashtag);
    }

    private static Boolean containsHashTag(List<HashTag> hashTags, HashTag hashtag){
        for (HashTag ht : hashTags) {
            if ( ht.getText().equals(hashtag.getText()))
                return true;
        }
        return false;
    }

    public static void addLocation(Location location, Graph g){
        if(!locationExists(g.getLocations(), location))
            g.getLocations().add(location);
    }

    private static Boolean locationExists(List<Location> locations, Location location){
        for(Location loc : locations){
            if (loc.equals(location)) return true;
        }
        return false;
    }

    public static void addUser(User user, Graph g){
        if(userExists(g.getUsersId(), user)){
            updateUserTweets(g.getUsers(), user);
        } else {
            g.getUsersId().add(user.getId());
            g.getUsers().add(user);
        }
    }

    private static Boolean userExists(List<String> users, User user){
        return users.contains(user.getId());
    }

    private static void updateUserTweets(List<User> users, User user){
        for(User usr : users){
            if(usr.getId().equals(user.getId())){
                usr.addAllTweets(user.getTweetsIds());
                return;
            }
        }
    }

    public static void addTweet(Tweet tweet,Graph g){
        g.tweets.add(tweet);
    }
}
