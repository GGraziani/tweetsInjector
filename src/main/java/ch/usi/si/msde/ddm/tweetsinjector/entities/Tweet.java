package ch.usi.si.msde.ddm.tweetsinjector.entities;

import java.util.ArrayList;
import java.util.List;

public class Tweet {

    private String id;
    private String authorId;
    private String text;
    private List<String> hashTags;
    private List<String> mentions;
    private String location;
    private String createdAt;

    public Tweet(String id, String authorId, String text, List<String> hashTags, List<String> mentions, String location, String createdAt) {
        this.id = id;
        this.authorId = authorId;
        this.text = text;
        this.hashTags = new ArrayList<>(hashTags);
        this.mentions = new ArrayList<>(mentions);
        this.location = location;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getText() {
        return text;
    }


    public List<String> getHashTags() {
        return hashTags;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }

    public String getLocation() {
        return location;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
