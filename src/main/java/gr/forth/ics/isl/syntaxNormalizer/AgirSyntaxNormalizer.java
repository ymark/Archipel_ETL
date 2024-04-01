package gr.forth.ics.isl.syntaxNormalizer;

import gr.forth.ics.isl.common.AgirResources;
import gr.forth.ics.isl.model.AgirProject;
import gr.forth.ics.isl.model.AgirProjects;
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
                if(projectObject.has(AgirResources.PROJECT_ID)) {
                    agirProject.setProjectId(projectObject.getInt(AgirResources.PROJECT_ID));
                }
                if(projectObject.has(AgirResources.CODE_OPERATION)) {
                    agirProject.setCodeOperation(projectObject.getString(AgirResources.CODE_OPERATION));
                }
                if(projectObject.has(AgirResources.ANNEE)) {
                    agirProject.setYear(projectObject.getString(AgirResources.ANNEE));
                }
                if(projectObject.has(AgirResources.RESPONSABLE) && !projectObject.isNull(AgirResources.RESPONSABLE)) {
                    agirProject.setResponsibleId(projectObject.getInt(AgirResources.RESPONSABLE));
                }
                if(projectObject.has(AgirResources.DUREE) && !projectObject.isNull(AgirResources.DUREE)) {
                    agirProject.setDuration(projectObject.getInt(AgirResources.DUREE));
                }
                if(projectObject.has(AgirResources.INTITULE)) {
                    agirProject.setTitle(projectObject.getString(AgirResources.INTITULE));
                }
                if(projectObject.has(AgirResources.RESUME) && !projectObject.isNull(AgirResources.RESUME)) {
                    agirProject.setResume(projectObject.getString(AgirResources.RESUME));
                }
                if(projectObject.has(AgirResources.CODE_SOUS_TYPE) && !projectObject.isNull(AgirResources.CODE_SOUS_TYPE)) {
                    agirProject.setSubType(projectObject.getString(AgirResources.CODE_SOUS_TYPE));
                }
                if(projectObject.has(AgirResources.AXE_RECHERHCE_INRAP) && !projectObject.isNull(AgirResources.AXE_RECHERHCE_INRAP)) {
                    agirProject.setResearchAxisInrap(projectObject.getString(AgirResources.AXE_RECHERHCE_INRAP));
                }
                if(projectObject.has(AgirResources.BUDGET_ACCORDE) && !projectObject.isNull(AgirResources.BUDGET_ACCORDE)) {
                    agirProject.setBudget(projectObject.getFloat(AgirResources.BUDGET_ACCORDE));
                }
                if(projectObject.has(AgirResources.CODE_CO) && !projectObject.isNull(AgirResources.CODE_CO)) {
                    agirProject.setCode(projectObject.getString(AgirResources.CODE_CO));
                }
                if(projectObject.has(AgirResources.CODE_TYPOLOGIE) && !projectObject.isNull(AgirResources.CODE_TYPOLOGIE)) {
                    agirProject.setCodeTypology(projectObject.getString(AgirResources.CODE_TYPOLOGIE));
                }
                if(projectObject.has(AgirResources.STATUT) && !projectObject.isNull(AgirResources.STATUT)) {
                    agirProject.setStatus(projectObject.getInt(AgirResources.STATUT));
                }
                if(projectObject.has(AgirResources.ADDRESSE1) && !projectObject.isNull(AgirResources.ADDRESSE1)) {
                    agirProject.setAddresse1(projectObject.getString(AgirResources.ADDRESSE1));
                }
                if(projectObject.has(AgirResources.ADDRESSE2) && !projectObject.isNull(AgirResources.ADDRESSE2)) {
                    agirProject.setAddresse2(projectObject.getString(AgirResources.ADDRESSE2));
                }
                if(projectObject.has(AgirResources.CODE_POSTAL) && !projectObject.isNull(AgirResources.CODE_POSTAL)) {
                    agirProject.setPostalCode(projectObject.getString(AgirResources.CODE_POSTAL));
                }
                if(projectObject.has(AgirResources.VILLE) && !projectObject.isNull(AgirResources.VILLE)) {
                    agirProject.setCity(projectObject.getString(AgirResources.VILLE));
                }
                if(projectObject.has(AgirResources.ORGANISATEUR) && !projectObject.isNull(AgirResources.ORGANISATEUR)) {
                    agirProject.setOrganizer(projectObject.getString(AgirResources.ORGANISATEUR));
                }
                if(projectObject.has(AgirResources.INSTITUTION_ORGANISATRICE) && !projectObject.isNull(AgirResources.INSTITUTION_ORGANISATRICE)) {
                    agirProject.setOrganizerInstitution(projectObject.getString(AgirResources.INSTITUTION_ORGANISATRICE));
                }
                if(projectObject.has(AgirResources.OPERATIONS_LIEES) && !projectObject.isNull(AgirResources.OPERATIONS_LIEES)){
                    agirProject.parseAndDefineOperationsList(projectObject.getString(AgirResources.OPERATIONS_LIEES));
                }
                projectsList.add(agirProject);
            }
        }
        // Create a JAXB context
        AgirProjects agirProjects=new AgirProjects(projectsList);
        JAXBContext jaxbContext = JAXBContext.newInstance(AgirProjects.class);

        // Marshal the object to XML
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        StringWriter writer = new StringWriter();
        marshaller.marshal(agirProjects, writer);

        OutputStream outputStream=new FileOutputStream("projet.xml");
        OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream,"UTF-8");
        outputStreamWriter.write(writer.toString());
        outputStreamWriter.flush();
        outputStreamWriter.close();

    }

    public static void main(String[] args) throws Exception {
        normalizeProjects(new File("projet.json"),new File("projet.xml"));
    }
}
