package ch.usi.si.msde.ddm.tweetsinjector.logics;

import ch.usi.si.msde.ddm.tweetsinjector.entities.Graph;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkJobInfo;
import org.apache.spark.SparkStageInfo;
import org.apache.spark.api.java.JavaFutureAction;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.SparkSession;

public class SparkInjector {

    public SparkInjector(){

//        Session

    }


    public void inject(Graph graph) {

        // get or create new spark session
        SparkSession spark = ConnectService.getSparkSession();

        // get a new spark context
        JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());



    }
}
