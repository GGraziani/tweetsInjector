package ch.usi.si.msde.ddm.tweetsinjector.utils;

public class Params {
    public String ds;
    public String segments;
    public String lists;
    public DB database;
    public Boolean test;

    public DB matchStr(String a){
        try {
            return DB.valueOf(a);
        } catch (IllegalArgumentException e){
            return null;
        }
    }
}



