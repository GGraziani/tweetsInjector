package ch.usi.si.msde.ddm.tweetsinjector.logics;

import ch.usi.si.msde.ddm.tweetsinjector.entities.Graph;

import java.sql.SQLException;

public class HiveInjector {

    private static String driverName = "org.apache.hive.jdbc.HiveDriver";


    public void inject(Graph graph) throws SQLException{
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
//         Not implemented => problems in configuration
//        Connection con = DriverManager.getConnection("jdbc:hive2://localhost:10000/default", "", "");

    }
}
