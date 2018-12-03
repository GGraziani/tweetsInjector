package ch.usi.si.msde.ddm.tweetsinjector.app;

import ch.usi.si.msde.ddm.tweetsinjector.entities.Graph;
import ch.usi.si.msde.ddm.tweetsinjector.io.XMLReader;
import ch.usi.si.msde.ddm.tweetsinjector.logics.GraphBuilder;
import ch.usi.si.msde.ddm.tweetsinjector.logics.Injector;
import ch.usi.si.msde.ddm.tweetsinjector.utils.Params;

import java.io.IOException;
import java.text.ParseException;

public class App {


    public static void main(String[] args) throws IOException {
        Params p = XMLReader.readParamsFromXMLFile("./src/main/resources/params/defauls.xml");

        GraphBuilder graphBuilder = new GraphBuilder(p);
        Graph graph = graphBuilder.buildGraph();

        Injector injector = new Injector();
        injector.inject(graph);


    }
}


