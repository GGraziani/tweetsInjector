package ch.usi.si.msde.ddm.tweetsinjector.utils;

import ch.usi.si.msde.ddm.tweetsinjector.entities.*;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Utils {


    public static ArrayList<File> sortSegments(ArrayList<File> files) {
        files.sort((file1, file2) -> Integer.parseInt(file1.getName().substring(file1.getName().indexOf('-')+1)) - Integer.parseInt(file2.getName().substring(file2.getName().indexOf('-')+1)));
        return files;
    }

    public static ArrayList<String> readFileToArrayList(String path) throws IOException {
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
    public static boolean contains_obscenity(JSONObject tweet, Boolean isExtended, ArrayList<String> badWords){
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

    public static boolean findStringMatch(String text, ArrayList<String> list){
        for (String s: list) {
            if(text.contains(s.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public static void addHashTag(HashTag hashtag, Graph g){

        if(!containsHashTag(g.hashTags,hashtag))
            g.hashTags.add(hashtag);
    }

    private static Boolean containsHashTag(ArrayList<HashTag> hashTags, HashTag hashtag){
        for (HashTag ht : hashTags) {
            if ( ht.getText().equals(hashtag.getText()))
                return true;
        }
        return false;
    }

    public static void addLocation(Location location, Graph g){
        if(!locationExists(g.locations, location))
            g.locations.add(location);
    }

    private static Boolean locationExists(ArrayList<Location> locations, Location location){
        for(Location loc : locations){
            if (loc.compareTo(location) == 0) return true;
        }
        return false;
    }

    public static void addUser(User user, Graph g){
        if(userExists(g.usersId, user)){
            updateUserTweets(g.users, user);
        } else {
            g.usersId.add(user.getId());
            g.users.add(user);
        }
    }

    private static Boolean userExists(ArrayList<String> users, User user){
        return users.contains(user.getId());
    }

    private static void updateUserTweets(ArrayList<User> users, User user){
        for(User usr : users){
            if(usr.getId().equals(user.getId())){
                usr.addTweet(user.getTweetsIds().get(0));
                return;
            }
        }
    }

    public static void addTweet(Tweet tweet,Graph g){
        g.tweets.add(tweet);
    }
}
