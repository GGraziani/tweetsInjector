package ch.usi.si.msde.ddm.tweetsinjector.entities;

public class Location implements Comparable<Location>{

    private String code;
    private String name;

    public Location(String code, String name){
        this.code = code;
        this.name = name;

    }

    public String getCode() {
        return code;
    }

    public String getName(){
        return name;
    }

    @Override
    public int compareTo(Location loc) {
        return name.compareTo(loc.getName());
    }

    @Override
    public String toString() {
        return "{"+name+", "+code+"}";
    }
}

