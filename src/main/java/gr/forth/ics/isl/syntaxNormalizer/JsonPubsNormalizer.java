package gr.forth.ics.isl.syntaxNormalizer;

import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * Yannis Marketakis (marketak 'at' forth 'dot' ics 'dot' gr)
 */

public class JsonPubsNormalizer{
    private static final Logger log=LogManager.getLogger(JsonPubsNormalizer.class);
    private Set<String> unusedIntervenantsColumns=new HashSet<>();

    public void syntaxNormalize(File sourceFolder, File targetFolder) throws FileNotFoundException, UnsupportedEncodingException, IOException, ParserConfigurationException, TransformerException {
        for(File subFolder : sourceFolder.listFiles()){
            if(!subFolder.isHidden() && new File(subFolder.getAbsolutePath()+"/"+ Resources.INTERVENANTS_FOLDER_NAME).exists()) {
               for(File intervenantsFile : new File(subFolder.getAbsolutePath()+"/"+ Resources.INTERVENANTS_FOLDER_NAME).listFiles()){
                   if(FilenameUtils.getExtension(intervenantsFile.getName()).equalsIgnoreCase(Resources.CSV_EXTENSION)){
                       Multimap<Integer, Pair<String,String>>contentsMap=parseIntervenants(intervenantsFile);
                       Document doc=xmlifyIntervenants(contentsMap);
                       this.createFolderHierarchyAndExportXML(intervenantsFile,targetFolder,doc);
                   }
               }
            }
        }
        log.info("The following intervenants headers were not mapped/used {}",this.unusedIntervenantsColumns);
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
                    }else{
                        this.unusedIntervenantsColumns.add(record.get(i));
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

    public Document xmlifyIntervenants(Multimap<Integer, Pair<String,String>> contentsMap) throws ParserConfigurationException {
        Document doc= DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element rootElement=doc.createElement(Resources.DATAROOT);
        doc.appendChild(rootElement);
        for(Integer lineIndex : contentsMap.keySet()){
            Element entryElement=doc.createElement(Resources.ENTRY);
            rootElement.appendChild(entryElement);
            for(Pair<String,String> pairContents : contentsMap.get(lineIndex)){
                Element infoElement=doc.createElement(pairContents.getLeft());
                if(pairContents.getRight()!=null && !pairContents.getRight().isBlank()) {
                    infoElement.setTextContent(pairContents.getRight().trim());
                }else{
                    infoElement.setTextContent("");
                }
                entryElement.appendChild(infoElement);
            }
        }
        return doc;
    }

    private void createFolderHierarchyAndExportXML(File inputWorkingDocument, File outputRootFolder,Document doc) throws TransformerException {
        String outputFolderName=inputWorkingDocument.getParentFile().getParentFile().getName();
        new File(outputRootFolder.getAbsolutePath()+"/"+outputFolderName).mkdir();

        Utils.exportXmlToFile(doc,new File(outputRootFolder.getAbsolutePath()+"/"
                                                    +outputFolderName+"/"
                                                    +FilenameUtils.getBaseName(inputWorkingDocument.getName())+"."+Resources.XML_EXTENSION));
    }

    public static void main(String[] args) throws Exception{
        new JsonPubsNormalizer().syntaxNormalize(new File("sample/input/"),new File("sample/output"));
    }
}
