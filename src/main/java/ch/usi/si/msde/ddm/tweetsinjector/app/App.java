package ch.usi.si.msde.ddm.tweetsinjector.app;

import ch.usi.si.msde.ddm.tweetsinjector.entities.Graph;
import ch.usi.si.msde.ddm.tweetsinjector.io.XMLReader;
import ch.usi.si.msde.ddm.tweetsinjector.logics.GraphBuilder;
import ch.usi.si.msde.ddm.tweetsinjector.logics.MongodbInjector;
import ch.usi.si.msde.ddm.tweetsinjector.logics.Neo4jInjector;
import ch.usi.si.msde.ddm.tweetsinjector.utils.Params;
import ch.usi.si.msde.ddm.tweetsinjector.utils.Utils;
import java.io.IOException;

public class App {


    public static void main(String[] args) throws IOException {
        Params p = XMLReader.readParamsFromXMLFile("./src/main/resources/params/defauls.xml");
        Utils.parseArgs(args, p);

        GraphBuilder graphBuilder = new GraphBuilder(p);
        Graph graph = graphBuilder.buildGraph();

        switch (p.database){


            case NEO4J:
                Neo4jInjector neo4jInjector= new Neo4jInjector();
                neo4jInjector.inject(graph);
            case MONGODB:
                MongodbInjector mongodbInjector = new MongodbInjector();
                mongodbInjector.inject(graph);

        }



    }
}


