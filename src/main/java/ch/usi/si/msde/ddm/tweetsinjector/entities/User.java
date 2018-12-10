package ch.usi.si.msde.ddm.tweetsinjector.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {

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

    public List<String> getTweetsIds() {
        return tweetsIds;
    }

    public void addTweet(String tweet) {
        tweetsIds.add(tweet);
    }

    @Override
    public String toString() {
        return tweetsIds.toString();
    }
}
