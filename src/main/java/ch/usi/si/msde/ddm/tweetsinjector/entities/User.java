package ch.usi.si.msde.ddm.tweetsinjector.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {

    private String id;
    private String name;
    private int friendsCount;
    private int favouritesCount;
    private ArrayList<String> tweetsIds;
    private String lang;

    public User(String id, String name, String tweetId, String lang, int friendsCount, int favouritesCount) {
        this.id = id;
        this.name = name;
        this.tweetsIds = new ArrayList<>(Collections.singletonList(tweetId));
        this.lang = lang;
        this.friendsCount = friendsCount;
        this.favouritesCount = favouritesCount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getFriendsCount(){
        return friendsCount;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public String getLang(){
        return lang;
    }

    public List<String> getTweetsIds() {
        return tweetsIds;
    }

    public void addAllTweets(List<String> tweets) {
        tweetsIds.addAll(tweets);
    }
}
