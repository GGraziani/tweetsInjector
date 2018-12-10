package ch.usi.si.msde.ddm.tweetsinjector.logics;

import ch.usi.si.msde.ddm.tweetsinjector.entities.Graph;
import ch.usi.si.msde.ddm.tweetsinjector.utils.ConnectService;

import org.neo4j.driver.v1.*;

import java.util.*;

import static org.neo4j.driver.v1.Values.parameters;

public class Neo4jInjector {

    private final Driver driver;

    public Neo4jInjector() {
        driver = ConnectService.getDriver();
    }

    public void inject(Graph graph) {
        System.out.print("Injecting graph into Neo4j....");

        HashMap<String, List<String>> usersTweets = new HashMap<>();

        graph.getLocations().forEach(location -> write(
                "Location",
                "SET a.name = $name " +
                        "SET a.code = $code ",
                parameters("name", location.getName(), "code", location.getCode())));

        graph.getHashTags().forEach((hashTag -> write(
                "HashTag",
                "SET a.text = $text ",
                parameters("text", hashTag.getText()))));


        graph.getUsers().forEach(usr -> {

            // Saving information about tweets for each user
            usersTweets.put(usr.getId(), usr.getTweetsIds());

            write(
                    "User",
                    "SET a.uid = $id " +
                            "SET a.name = $name ",
                    parameters("id", usr.getId(), "name", usr.getName()));
        });

        graph.tweets.forEach(twt -> {

            write(
                    "Tweet",
                    "SET a.tid = $id " +
                            "SET a.text = $text " +
                            "SET a.created_at = $created_at ",
                    parameters("id", twt.getId(), "text", twt.getText(), "created_at", twt.getCreatedAt()));

            if (!twt.getHashTags().isEmpty()) {
                twt.getHashTags().forEach(hts -> connect(
                        "Tweet",
                        "HashTag",
                        "where a.tid=$tid and b.text=$text ",
                        "has_hashtag ",
                        parameters("tid", twt.getId(), "text", hts.getText())
                ));
            }

            if (!twt.getMentions().isEmpty()) {
                twt.getMentions().forEach(mention -> connect(
                        "Tweet",
                        "User",
                        "where a.tid=$tid and b.uid=$uid ",
                        "mentions ",
                        parameters("tid", twt.getId(), "uid", mention)
                ));
            }

            if (twt.getLocation() != null)
                connect("Tweet",
                        "Location",
                        "where a.tid=$tid and b.name=$name ",
                        "is_located",
                        parameters("tid", twt.getId(), "name", twt.getLocation().getName()));
        });

        usersTweets.keySet().forEach(uid ->
                usersTweets.get(uid).forEach(tid -> connect(
                        "User",
                        "Tweet",
                        "where a.uid=$uid and b.tid=$tid ",
                        "tweets",
                        parameters("uid", uid, "tid", tid)
                ))
        );

        System.out.print("OK");
    }

    private void write(
            String entity,
            String properties,
            Value parameters) {

        try (Session session = driver.session()) {

            session.writeTransaction(transaction -> {
                StatementResult result = transaction.run("CREATE (a:" + entity + ") " + properties + "RETURN a.name + ', from node ' + id(a)", parameters);
                return result.single().get(0).toString();
            });
        }
    }

    private void connect(
            String entity1,
            String entity2,
            String properties,
            String relation,
            Value parameters) {
        try (Session session = driver.session()) {
            session.writeTransaction(transaction -> {
                transaction.run("Match (a:" + entity1 + "), (b:" + entity2 + ") " + properties + "create (a)-[:" + relation + "]->(b)" + "return a", parameters);
                return null;
            });
        }
    }
}
