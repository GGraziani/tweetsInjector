package ch.usi.si.msde.ddm.tweetsinjector.entities;

import java.util.ArrayList;
import java.util.List;

public class Tweet {

    private String id;
    private String author_id;
    private String text;
    private List<HashTag> hashTags;
    private List<String> mentions;
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


    public List<HashTag> getHashTags() {
        return hashTags;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }

    public Location getLocation() {
        return location;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
