package ch.usi.si.msde.ddm.tweetsinjector.utils;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.GraphDatabase;

public class ConnectService {

    private static final String URL = "bolt://localhost:7687";
    private  static final String USER = "neo4j";
    private static final String PASSWORD = "1234";


    public static org.neo4j.driver.v1.Driver getDriver(){
        return   GraphDatabase.driver(URL, AuthTokens.basic(USER, PASSWORD));
    }
}
