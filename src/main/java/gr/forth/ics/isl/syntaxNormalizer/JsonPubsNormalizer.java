package gr.forth.ics.isl.syntaxNormalizer;

import java.io.File;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Yannis Marketakis (marketak 'at' forth 'dot' ics 'dot' gr)
 */

public class JsonPubsNormalizer implements SyntaxNormalizer{
    private static final Logger log=LogManager.getLogger(JsonPubsNormalizer.class);

    public void syntaxNormalize(File sourceFolder, File targetFolder){
        for(File subFolder : sourceFolder.listFiles()){
            if(!subFolder.isHidden()) {
                log.debug("Found folder " + subFolder.getName());

            }
        }
    }

    public static void main(String[] args){
        new JsonPubsNormalizer().syntaxNormalize(new File("sample/input/"),new File("sample/output"));
    }
}
