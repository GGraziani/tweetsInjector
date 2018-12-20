package ch.usi.si.msde.ddm.tweetsinjector.utils;

import ch.usi.si.msde.ddm.tweetsinjector.entities.*;
import org.apache.spark.sql.Row;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

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

    /**
     * Checks whether the list contains a String matching the input String "text"
     */
    private static boolean findStringMatch(String text, List<String> list){
        for (String s: list) {
            if(text.contains(s.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the hashtag to the list of hashtags by avoiding duplicates
     */
    public static void addHashTag(HashTag hashtag, Graph g){

        if(!containsHashTag(g.getHashTags(),hashtag))
            g.getHashTags().add(hashtag);
    }

    /**
     * Checks whether the list of hashtags contains an hashtag matching the given one
     */
    private static Boolean containsHashTag(List<HashTag> hashTags, HashTag hashtag){
        for (HashTag ht : hashTags) {
            if ( ht.getText().equals(hashtag.getText()))
                return true;
        }
        return false;
    }

    /**
     * Adds the location to the list of locations by avoiding duplicates
     */
    public static void addLocation(Location location, Graph g){
        if(!locationExists(g.getLocations(), location))
            g.getLocations().add(location);
    }

    /**
     * Checks whether the list of locations contains a location matching the given one
     */
    private static Boolean locationExists(List<Location> locations, Location location){
        for(Location loc : locations){
            if (loc.equals(location)) return true;
        }
        return false;
    }

    /**
     * Adds the user to the list of users by avoiding duplicates
     */
    public static void addUser(User user, Graph g){
        if(userExists(g.getUsersId(), user)){
            updateUserTweets(g.getUsers(), user);
        } else {
            g.getUsersId().add(user.getId());
            g.getUsers().add(user);
        }
    }

    /**
     * Checks whether the list of users contains a user matching the given one
     */
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

    public static String separator(){
        return "\n-------------------------------------------\n";
    }

    public static String millisToSecAndMillis(long durationInMillis){
        long millis = durationInMillis % 1000;
        long second = (durationInMillis / 1000) % 60;

        return String.format("%2d.%d s", second, millis);
    }

    public static Row[] sort(List<Row> df){
        Row[] ar = df.toArray(new Row[df.size()]);
        for (int i = (ar.length - 1); i >= 0; i--)
        {
            for (int j = 1; j <= i; j++)
            {
                if (ar[j-1].getInt(0) < ar[j].getInt(0))
                {
                    Row temp = ar[j-1];
                    ar[j-1] = ar[j];
                    ar[j] = temp;
                } } }
        return ar;
    }


}
