package ch.usi.si.msde.ddm.tweetsinjector.io;


import ch.usi.si.msde.ddm.tweetsinjector.utils.Params;

import javax.xml.bind.JAXB;
import java.io.File;

public class XMLReader {

    public static Params readParamsFromXMLFile(String indexParamFile){

        Params p = null;
        try {
            p = JAXB.unmarshal(new File(indexParamFile), Params.class);
        } catch (Exception e){
            System.out.println(" caught a " + e.getClass() +
                    "\n with message: " + e.getMessage());
            System.exit(1);
        }

        return p;
    }
}


