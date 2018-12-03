package ch.usi.si.msde.ddm.tweetsinjector.entities;

public class HashTag implements Comparable<HashTag>{

    private String text;

    public HashTag(String text){
        this.text = text;

    }

    public String getText() {
        return text;
    }

    @Override
    public int compareTo(HashTag ht) {
        return text.compareTo(ht.getText());
    }

    @Override
    public String toString() {
        return text;
    }
}
