package gr.forth.ics.isl.syntaxNormalizer;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import gr.forth.ics.isl.common.Resources;
import gr.forth.ics.isl.common.Utils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Yannis Marketakis (marketak 'at' forth 'dot' ics 'dot' gr)
 */

public class JsonPubsNormalizer{
    private static final Logger log=LogManager.getLogger(JsonPubsNormalizer.class);

    public void syntaxNormalize(File sourceFolder, File targetFolder) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        for(File subFolder : sourceFolder.listFiles()){
            if(!subFolder.isHidden() && new File(subFolder.getAbsolutePath()+"/"+ Resources.INTERVENANTS_FOLDER_NAME).exists()) {
               for(File intervenantsFile : new File(subFolder.getAbsolutePath()+"/"+ Resources.INTERVENANTS_FOLDER_NAME).listFiles()){
                   if(FilenameUtils.getExtension(intervenantsFile.getName()).equalsIgnoreCase(Resources.CSV_EXTENSION)){
                       Multimap<Integer, Pair<String,String>>contents=parseIntervenants(intervenantsFile);
                       //export multimap contents to XML file
                   }
               }
            }
        }
    }

    private Multimap<Integer, Pair<String,String>> parseIntervenants(File intervenantsCsvFile) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        log.debug("parsing file {}",intervenantsCsvFile.getAbsolutePath());
        CSVParser csvParser= CSVFormat.DEFAULT.parse(new InputStreamReader(new FileInputStream(intervenantsCsvFile), "UTF-8"));
        int lineCounter=1;
        Map<Integer,String> columnMap=new TreeMap<>();
        Multimap<Integer, Pair<String,String>> contentsMap= TreeMultimap.create();
        for(CSVRecord record : csvParser){
            if(lineCounter==1){
                for(int i=0;i< record.size();i++){
                    if(i==0 && record.get(i).isBlank()){
                        columnMap.put(i,Resources.LABEL_LOCAL_ID);
                        continue;
                    }
                    String translatedColumnHeader=Utils.parseFrenchColumn(record.get(i));
                    if(translatedColumnHeader!=null){
                        columnMap.put(i,translatedColumnHeader);
                    }
                }
            }
            else{
                for(int i=0;i< record.size();i++){
                    if(columnMap.containsKey(i)){
                        contentsMap.put(lineCounter,Pair.of(columnMap.get(i),record.get(i)));
                    }
                }
            }
            lineCounter+=1;
        }
        return contentsMap;
    }

    public static void main(String[] args) throws Exception{
        new JsonPubsNormalizer().syntaxNormalize(new File("sample/input2/"),new File("sample/output"));
    }
}
