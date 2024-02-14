package gr.forth.ics.isl.syntaxNormalizer;

import gr.forth.ics.isl.common.HALResources;
import gr.forth.ics.isl.common.JsonExportResources;
import gr.forth.ics.isl.common.Utils;
import gr.forth.ics.isl.model.HalAuthor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HalSyntaxNormalizer {
    private static final Logger log= LogManager.getLogger(HalSyntaxNormalizer.class);

    private void harvestData(String collectionName,String authorsFilename) throws IOException, ParserConfigurationException, TransformerException {
        List<HalAuthor> authorsCollection=new ArrayList<>();
        log.info("Harvest all resources from HAL under colletion '{}'",collectionName);
        String baseUrl=HALResources.HAL_REST_API_PREFIX+collectionName+"/"+HALResources.HAL_REST_API_AUTHORS_PARAMETERS+"&"+HALResources.ROWS+"="+HALResources.NUMBER_OF_RESULTS;
        int start=0;
        while(true) {
            URL url = new URL(baseUrl+"&"+HALResources.START+"="+start);
            log.debug("Hitting URL '{}'", url.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            if (!response.isEmpty()) {
                JSONObject jsonResultObject = new JSONObject(response.toString()).getJSONObject(HALResources.RESPONSE);
                JSONArray resultsArray = jsonResultObject.getJSONArray(HALResources.DOCS);
                if (!resultsArray.isEmpty()) {
                    for (int i = 0; i < resultsArray.length(); i++) {
                        authorsCollection.addAll(this.identifyAuthors(resultsArray.getJSONObject(i)));
                    }
                }else{
                    break;
                }
            } else {
                log.error("Received empty result as response");
                break;
            }
            start+=HALResources.NUMBER_OF_RESULTS;
        }
        Document document=this.xmlifyAuthors(authorsCollection);
        Utils.exportXmlToFile(document,new File(authorsFilename));
    }

    private Document xmlifyAuthors(Collection<HalAuthor> authorsCollection) throws ParserConfigurationException {
        Document doc= DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element rootElement=doc.createElement(JsonExportResources.DATAROOT);
        doc.appendChild(rootElement);
        for(HalAuthor author : authorsCollection){
            rootElement.appendChild(author.toXml(doc));
        }
        return doc;
    }

    Collection<HalAuthor> identifyAuthors(JSONObject authorsJsonObject){
        Map<String,HalAuthor> retMap=new HashMap<>();
        if(authorsJsonObject.has(HALResources.AUTH_IDHASPRIMARYSTRUCTURE_FS)){
            JSONArray authorsCompactForm = authorsJsonObject.getJSONArray(HALResources.AUTH_IDHASPRIMARYSTRUCTURE_FS);
            for (int i = 0; i < authorsCompactForm.length(); i++) {
                String authDetailsStr = authorsCompactForm.getString(i);
                log.debug("parsing author details in compact form '{}'", authDetailsStr);
                HalAuthor author = new HalAuthor();
                author.setPersonIdInternal(authDetailsStr.substring(0, authDetailsStr.indexOf(HALResources.FACET_SEP) - 1).split("-")[0]);
                if (retMap.containsKey(author.getPersonIdInternal())) {
                    HalAuthor existingAuthor = retMap.get(author.getPersonIdInternal());
                    existingAuthor.addOrganization(authDetailsStr.substring(authDetailsStr.indexOf(HALResources.JOIN_SEP) + HALResources.JOIN_SEP.length() + 1,
                                    authDetailsStr.lastIndexOf(HALResources.FACET_SEP) - 1),
                            authDetailsStr.substring(authDetailsStr.lastIndexOf(HALResources.FACET_SEP) + HALResources.FACET_SEP.length() + 1));

                } else {
                    author.setPersonIdHal(authDetailsStr.substring(0, authDetailsStr.indexOf(HALResources.FACET_SEP) - 1).split("-")[1]);
                    author.setAuthorName(authDetailsStr.substring(authDetailsStr.indexOf(HALResources.FACET_SEP) + HALResources.FACET_SEP.length() + 1,
                            authDetailsStr.indexOf(HALResources.JOIN_SEP) - 1));
                    author.addOrganization(authDetailsStr.substring(authDetailsStr.indexOf(HALResources.JOIN_SEP) + HALResources.JOIN_SEP.length() + 1,
                                    authDetailsStr.lastIndexOf(HALResources.FACET_SEP) - 1),
                            authDetailsStr.substring(authDetailsStr.lastIndexOf(HALResources.FACET_SEP) + HALResources.FACET_SEP.length() + 1));
                    retMap.put(author.getPersonIdInternal(), author);
                }
            }
        }
        return retMap.values();
    }

    public static void main(String[] args) throws Exception {
        new HalSyntaxNormalizer().harvestData(HALResources.INRAP,"hal_inraps_authors.xml");
    }
}
