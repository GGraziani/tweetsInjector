package ch.usi.si.msde.ddm.tweetsinjector.logics;

import org.apache.spark.sql.SparkSession;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.GraphDatabase;


public class ConnectService {

    // Mongodb
    private static final String URL = "bolt://localhost:7687";
    private  static final String USER = "neo4j";
    private static final String PASSWORD = "1234";

    // Spark
    private static final String SPARK_APP_NAME = "twitterInjectorDB";

    public static org.neo4j.driver.v1.Driver getNeo4jDriver() {
        return   GraphDatabase.driver(URL, AuthTokens.basic(USER, PASSWORD));
    }

    public static SparkSession getSparkSession(){
        return SparkSession.builder().master("local").appName(SPARK_APP_NAME).getOrCreate();
    }
}
