package ch.usi.si.msde.ddm.tweetsinjector.entities;

public class Location {

    private String code;
    private String name;

    public Location(String code, String name) {
        this.code = code;
        this.name = name;

    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;
        return getName().equals( ( (Location) obj).getName());
    }
}