package ch.usi.si.msde.ddm.tweetsinjector.entities;

import java.io.Serializable;

public class HashTag{

    private String text;

    public HashTag(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
