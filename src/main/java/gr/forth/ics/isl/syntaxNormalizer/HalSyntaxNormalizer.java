package gr.forth.ics.isl.syntaxNormalizer;

import gr.forth.ics.isl.common.HALResources;
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
                System.out.println(resultsArray.get(i));
            }
        }else{
            log.error("Received empty result as response");
        }


    }

    public static void main(String[] args) throws IOException {
        new HalSyntaxNormalizer().harvestData(HALResources.INRAP);
    }
}
