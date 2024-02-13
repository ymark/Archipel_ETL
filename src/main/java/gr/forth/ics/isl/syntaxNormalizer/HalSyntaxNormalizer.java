package gr.forth.ics.isl.syntaxNormalizer;

import gr.forth.ics.isl.common.HALResources;
import gr.forth.ics.isl.model.HalAuthor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HalSyntaxNormalizer {
    private static final Logger log= LogManager.getLogger(HalSyntaxNormalizer.class);

    private void harvestData(String collectionName) throws IOException {
        log.info("Harvest all resources from HAL under colletion '{}'",collectionName);
        URL url=new URL(HALResources.HAL_REST_API_PREFIX+collectionName+"/"+HALResources.HAL_REST_API_AUTHORS_PARAMETERS);
        log.debug("Hitting URL '{}'",url.toString());
        HttpURLConnection con=(HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        if(!response.isEmpty()) {
            JSONObject jsonResultObject = new JSONObject(response.toString()).getJSONObject(HALResources.RESPONSE);
            JSONArray resultsArray=jsonResultObject.getJSONArray(HALResources.DOCS);
            for(int i=0;i<resultsArray.length();i++){
                for(HalAuthor author : this.identifyAuthors(resultsArray.getJSONObject(i))){
                    System.out.println(author);
                }
            }
        }else{
            log.error("Received empty result as response");
        }
    }

    Collection<HalAuthor> identifyAuthors(JSONObject authorsJsonObject){
        Map<Integer,HalAuthor> retMap=new HashMap<>();
        JSONArray authorsCompactForm=authorsJsonObject.getJSONArray(HALResources.AUTH_IDHASPRIMARYSTRUCTURE_FS);
        for(int i=0;i<authorsCompactForm.length();i++){
            String authDetailsStr=authorsCompactForm.getString(i);
            log.debug("parsing author details in compact form '{}'",authDetailsStr);
            HalAuthor author=new HalAuthor();
            author.setPersonIdInternal(Integer.valueOf(authDetailsStr.substring(0,authDetailsStr.indexOf(HALResources.FACET_SEP)-1).split("-")[0]));
            if(retMap.containsKey(author.getPersonIdInternal())){
                HalAuthor existingAuthor=retMap.get(author.getPersonIdInternal());
                existingAuthor.addOrganization(Integer.valueOf(authDetailsStr.substring(authDetailsStr.indexOf(HALResources.JOIN_SEP)+HALResources.JOIN_SEP.length()+1,
                                                               authDetailsStr.lastIndexOf(HALResources.FACET_SEP)-1)),
                                                authDetailsStr.substring(authDetailsStr.lastIndexOf(HALResources.FACET_SEP)+HALResources.FACET_SEP.length()+1));

            }else{
                author.setPersonIdHal(Integer.valueOf(authDetailsStr.substring(0,authDetailsStr.indexOf(HALResources.FACET_SEP)-1).split("-")[1]));
                author.setAuthorName(authDetailsStr.substring(authDetailsStr.indexOf(HALResources.FACET_SEP)+HALResources.FACET_SEP.length()+1,
                        authDetailsStr.indexOf(HALResources.JOIN_SEP)-1));
                author.addOrganization(Integer.valueOf(authDetailsStr.substring(authDetailsStr.indexOf(HALResources.JOIN_SEP)+HALResources.JOIN_SEP.length()+1,
                                                        authDetailsStr.lastIndexOf(HALResources.FACET_SEP)-1)),
                                       authDetailsStr.substring(authDetailsStr.lastIndexOf(HALResources.FACET_SEP)+HALResources.FACET_SEP.length()+1));
                retMap.put(author.getPersonIdInternal(),author);
            }
        }
        return retMap.values();
    }

    public static void main(String[] args) throws IOException {
        new HalSyntaxNormalizer().harvestData(HALResources.INRAP);
    }
}
