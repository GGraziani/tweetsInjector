package ch.usi.si.msde.ddm.tweetsinjector.entities;

import java.util.ArrayList;

public class Tweet {

    private String id;
    private String author_id;
    private String text;
    private ArrayList<HashTag> hashTags;
    private ArrayList<String> mentions;
    private Location location;
    private String createdAt;

    public Tweet(String id, String author_id, String text, ArrayList<HashTag> hashTags, ArrayList<String> mentions, Location location, String createdAt) {
        this.id = id;
        this.author_id = author_id;
        this.text = text;
        this.hashTags = new ArrayList<>(hashTags);
        this.mentions = new ArrayList<>(mentions);
        this.location = location;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public String getText() {
        return text;
    }


    public ArrayList<HashTag> getHashTags() {
        return hashTags;
    }

    public ArrayList<String> getMentions() {
        return mentions;
    }

    public Location getLocation() {
        return location;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
