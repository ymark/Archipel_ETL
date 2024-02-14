package gr.forth.ics.isl.syntaxNormalizer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import gr.forth.ics.isl.common.JsonExportPublication;
import gr.forth.ics.isl.common.JsonExportResources;
import gr.forth.ics.isl.common.Utils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.JSONObject;
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
            if(!subFolder.isHidden() && new File(subFolder.getAbsolutePath()+"/"+ JsonExportResources.INTERVENANTS_FOLDER_NAME).exists()) {
               for(File intervenantsFile : new File(subFolder.getAbsolutePath()+"/"+ JsonExportResources.INTERVENANTS_FOLDER_NAME).listFiles()){
                   if(FilenameUtils.getExtension(intervenantsFile.getName()).equalsIgnoreCase(JsonExportResources.CSV_EXTENSION)){
                       Multimap<Integer, Pair<String,String>>contentsMap=parseIntervenants(intervenantsFile);
                       JsonExportPublication jsonExportPubl=parsePublicationJson(subFolder);
                       Document doc=xmlifyIntervenants(contentsMap,jsonExportPubl);
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
                        columnMap.put(i, JsonExportResources.LABEL_LOCAL_ID);
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

    public Document xmlifyIntervenants(Multimap<Integer, Pair<String,String>> contentsMap,JsonExportPublication publicationInfo) throws ParserConfigurationException {
        Document doc= DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element rootElement=doc.createElement(JsonExportResources.DATAROOT);
        doc.appendChild(rootElement);
        rootElement.appendChild(publicationInfo.toXml(doc));
        for(Integer lineIndex : contentsMap.keySet()){
            Element entryElement=doc.createElement(JsonExportResources.ENTRY);
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
                                                    +FilenameUtils.getBaseName(inputWorkingDocument.getName())+"."+ JsonExportResources.XML_EXTENSION));
    }

    private JsonExportPublication parsePublicationJson(File publicationFolder) throws IOException {
        JsonExportPublication jsonExportPubl=new JsonExportPublication();
        jsonExportPubl.setInrapPublicationCode(publicationFolder.getName());
        File jsonPublFile=new File(publicationFolder.getAbsolutePath()+"/"+publicationFolder.getName()+"."+JsonExportResources.JSON_EXTENSION);
        if(jsonPublFile.exists()){
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(jsonPublFile), StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder jsonContentsBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                jsonContentsBuilder.append(inputLine);
            }
            in.close();

            JSONObject jsonObject=new JSONObject(jsonContentsBuilder.toString());
            if(jsonObject.has(JsonExportResources.FICHE_SIGNALETIQUE)){
                JSONObject ficheSignalObject=jsonObject.getJSONObject(JsonExportResources.FICHE_SIGNALETIQUE);
                if(ficheSignalObject.has(JsonExportResources.CODES) && ficheSignalObject.get(JsonExportResources.CODES) instanceof JSONObject){
                    JSONObject codesObject=ficheSignalObject.getJSONObject(JsonExportResources.CODES);
                    for(String codeName : codesObject.keySet()){
                        jsonExportPubl.addCode(codeName,codesObject.getString(codeName));
                    }
                }
                if(ficheSignalObject.has(JsonExportResources.LOCALISATION)){
                    JSONObject localisationObject=ficheSignalObject.getJSONObject(JsonExportResources.LOCALISATION);
                    if(localisationObject.has(JsonExportResources.REGION)){
                        jsonExportPubl.setLocationRegion(localisationObject.getString(JsonExportResources.REGION));
                    }
                    if(localisationObject.has(JsonExportResources.DEPARTMENT)){
                        jsonExportPubl.setLocationDepartment(localisationObject.getString(JsonExportResources.DEPARTMENT));
                    }
                    if(localisationObject.has(JsonExportResources.ADDRESSE_OU_LIEU_DIT)){
                        jsonExportPubl.setLocationAddress(localisationObject.getString(JsonExportResources.ADDRESSE_OU_LIEU_DIT));
                    }
                    if(localisationObject.has(JsonExportResources.COMMUNE)){
                        for(int i=0;i<localisationObject.getJSONArray(JsonExportResources.COMMUNE).length();i++){
                            jsonExportPubl.addLocationCommune(localisationObject.getJSONArray(JsonExportResources.COMMUNE).getString(i));
                        }
                    }
                }
            }
        }
        return jsonExportPubl;
    }

    public static void main(String[] args) throws Exception{
        new JsonPubsNormalizer().syntaxNormalize(new File("sample/input/"),new File("sample/output"));
    }
}
