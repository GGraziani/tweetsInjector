package ch.usi.si.msde.ddm.tweetsinjector.entities;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    /* Entities lists */
    private static List<HashTag> hashTags = new ArrayList<>();
    private static List<Location> locations = new ArrayList<>();
    public List<User> users = new ArrayList<>();
    public List<String> usersId = new ArrayList<>();
    public List<Tweet> tweets = new ArrayList<>();


    public List<HashTag> getHashTags() {
        return hashTags;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<String> getUsersId() {
        return usersId;
    }


    public List<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }
}
