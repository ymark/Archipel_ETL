package gr.forth.ics.isl.syntaxNormalizer;

import gr.forth.ics.isl.common.AgirResources;
import gr.forth.ics.isl.model.AgirProject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class AgirSyntaxNormalizer {
    private static final Logger log= LogManager.getLogger(AgirSyntaxNormalizer.class);

    private static void normalizeProjects(File sourceJsonFile, File sourceXmlFile) throws Exception, JAXBException {
        List<AgirProject> projectsList=new ArrayList<>();

        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(sourceJsonFile), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder jsonContentsBuilder = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            jsonContentsBuilder.append(inputLine);
        }
        in.close();

        JSONObject completeJson=new JSONObject(jsonContentsBuilder.toString());
        if(completeJson.has(AgirResources.ROWS)){
            JSONArray projectsArray=completeJson.getJSONArray(AgirResources.ROWS);
            log.info("Parsing projects from Agir. Found {} project entries",projectsArray.length());

            for(int i=0;i<projectsArray.length();i++){
                JSONObject projectObject=projectsArray.getJSONObject(i);
                AgirProject agirProject=new AgirProject();
                if(projectObject.has(AgirResources.CODE_OPERATION)) {
                    agirProject.setCodeOperation(projectObject.getString(AgirResources.CODE_OPERATION));
                }
                if(projectObject.has(AgirResources.ANNEE)) {
                    agirProject.setYear(projectObject.getString(AgirResources.ANNEE));
                }
                if(projectObject.has(AgirResources.RESPONSABLE)) {
                    agirProject.setResponsibleId(projectObject.getInt(AgirResources.RESPONSABLE));
                }
                if(projectObject.has(AgirResources.DUREE)) {
                    agirProject.setDuration(projectObject.getInt(AgirResources.DUREE));
                }
                if(projectObject.has(AgirResources.INTITULE)) {
                    agirProject.setTitle(projectObject.getString(AgirResources.INTITULE));
                }
                if(projectObject.has(AgirResources.RESUME)) {
                    agirProject.setResume(projectObject.getString(AgirResources.RESUME));
                }
                projectsList.add(agirProject);
                break;
            }
        }
        // Create a JAXB context
        JAXBContext jaxbContext = JAXBContext.newInstance(ArrayList.class);

        // Marshal the object to XML
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        StringWriter writer = new StringWriter();
        marshaller.marshal(projectsList, writer);

        // Print the XML representation
        System.out.println(writer.toString());

    }

    public static void main(String[] args) throws Exception {
        normalizeProjects(new File("projet.json"),new File("projet.xml"));
    }
}
