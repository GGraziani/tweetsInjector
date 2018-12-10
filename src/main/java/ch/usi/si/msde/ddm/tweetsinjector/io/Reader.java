package ch.usi.si.msde.ddm.tweetsinjector.io;
import ch.usi.si.msde.ddm.tweetsinjector.utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;

public class Reader {

    private BufferedReader br;

    public Reader(File file){
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (IOException e){
            Utils.LOGGER.log( Level.SEVERE, "Caught the following exception:",e);
        }
    }

    public String nextLine() throws IOException{
        return br.readLine();
    }

    public void close() throws IOException{
        br.close();
    }
}
