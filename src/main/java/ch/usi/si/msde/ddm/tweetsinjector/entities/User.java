package ch.usi.si.msde.ddm.tweetsinjector.entities;

import java.util.ArrayList;
import java.util.Collections;

public class User implements Comparable<User> {

    private String id;
    private String name;
    private ArrayList<String> tweetsIds;

    public User(String id, String name, String tweetId) {
        this.id = id;
        this.name = name;
        this.tweetsIds = new ArrayList<>(Collections.singletonList(tweetId));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getTweetsIds() {
        return tweetsIds;
    }

    public void addTweet(String tweet) {
        // Todo duplicate
        tweetsIds.add(tweet);
    }

    @Override
    public int compareTo(User usr) {
        return id.compareTo(usr.getId());
    }

    @Override
    public String toString() {
        return tweetsIds.toString();
    }
}
