package gr.forth.ics.isl.syntaxNormalizer;

import gr.forth.ics.isl.common.AgirResources;
import gr.forth.ics.isl.common.Utils;
import gr.forth.ics.isl.model.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class AgirSyntaxNormalizer {
    private static final Logger log= LogManager.getLogger(AgirSyntaxNormalizer.class);

    private static void normalizeProjects(File sourceJsonFile, File sourceXmlFile) throws Exception, JAXBException {
        List<AgirProject> projectsList=new ArrayList<>();

        String contents=Utils.getTextualContents(sourceJsonFile);
        JSONObject completeJson=new JSONObject(contents);

        if(completeJson.has(AgirResources.ROWS)){
            JSONArray projectsArray=completeJson.getJSONArray(AgirResources.ROWS);
            log.info("Parsing projects from Agir. Found {} project entries",projectsArray.length());

            for(int i=0;i<projectsArray.length();i++){
                JSONObject projectObject=projectsArray.getJSONObject(i);
                AgirProject agirProject=new AgirProject();
                if(projectObject.has(AgirResources.PROJET_ID)) {
                    agirProject.setProjectId(projectObject.getInt(AgirResources.PROJET_ID));
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

        AgirProjects agirProjects=new AgirProjects(projectsList);
        JAXBContext jaxbContext = JAXBContext.newInstance(AgirProjects.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter writer = new StringWriter();
        marshaller.marshal(agirProjects, writer);
        Utils.writeToXml(writer,sourceXmlFile);
    }

    private static void normalizePersons(File sourceJsonFile, File sourceXmlFile) throws Exception, JAXBException {
        List<AgirPerson> personsList=new ArrayList<>();

        String contents=Utils.getTextualContents(sourceJsonFile);
        JSONObject completeJson=new JSONObject(contents);

        if(completeJson.has(AgirResources.ROWS)){
            JSONArray personsArray=completeJson.getJSONArray(AgirResources.ROWS);
            log.info("Parsing persons from Agir. Found {} person entries",personsArray.length());

            for(int i=0;i<personsArray.length();i++){
                JSONObject personObject=personsArray.getJSONObject(i);
                AgirPerson agirPerson=new AgirPerson();

                if(personObject.has(AgirResources.ID)) {
                    agirPerson.setPersonId(personObject.getInt(AgirResources.ID));
                }
                if(personObject.has(AgirResources.MATRICULE) && !personObject.isNull(AgirResources.MATRICULE)) {
                    agirPerson.setMatricule(personObject.getString(AgirResources.MATRICULE));
                }
                if(personObject.has(AgirResources.NOM)) {
                    agirPerson.setName(personObject.getString(AgirResources.NOM));
                }
                if(personObject.has(AgirResources.PRENOM)) {
                    agirPerson.setSurname(personObject.getString(AgirResources.PRENOM));
                }
                if(personObject.has(AgirResources.CIVILITE)) {
                    agirPerson.setCivility(personObject.getString(AgirResources.CIVILITE));
                }
                if(personObject.has(AgirResources.STATUT)) {
                    agirPerson.setStatut(personObject.getInt(AgirResources.STATUT));
                }
                if(personObject.has(AgirResources.EMAIL) && !personObject.isNull(AgirResources.EMAIL)) {
                    agirPerson.setEmail(personObject.getString(AgirResources.EMAIL));
                }
                if(personObject.has(AgirResources.CATEGORIE) && !personObject.isNull(AgirResources.CATEGORIE)) {
                    agirPerson.setCategory(personObject.getString(AgirResources.CATEGORIE));
                }
                if(personObject.has(AgirResources.CONTRAT) && !personObject.isNull(AgirResources.CONTRAT)) {
                    agirPerson.setContrat(personObject.getString(AgirResources.CONTRAT));
                }
                if(personObject.has(AgirResources.FILIERE) && !personObject.isNull(AgirResources.FILIERE)) {
                    agirPerson.setFiliere(personObject.getString(AgirResources.FILIERE));
                }
                if(personObject.has(AgirResources.TITRE) && !personObject.isNull(AgirResources.TITRE)) {
                    agirPerson.setTitre(personObject.getString(AgirResources.TITRE));
                }
                if(personObject.has(AgirResources.ADDRESSE_ADMINISTRATIVE1) && !personObject.isNull(AgirResources.ADDRESSE_ADMINISTRATIVE1)) {
                    agirPerson.setAdministrativeAddr1(personObject.getString(AgirResources.ADDRESSE_ADMINISTRATIVE1));
                }
                if(personObject.has(AgirResources.ADDRESSE_ADMINISTRATIVE2) && !personObject.isNull(AgirResources.ADDRESSE_ADMINISTRATIVE2)) {
                    agirPerson.setAdministrativeAddr2(personObject.getString(AgirResources.ADDRESSE_ADMINISTRATIVE2));
                }
                if(personObject.has(AgirResources.CP_ADMINISTRATIVE) && !personObject.isNull(AgirResources.CP_ADMINISTRATIVE)) {
                    agirPerson.setAdministrativePostalCode(personObject.getString(AgirResources.CP_ADMINISTRATIVE));
                }
                if(personObject.has(AgirResources.COMMUNE_ADMINISTRATIVE) && !personObject.isNull(AgirResources.COMMUNE_ADMINISTRATIVE)) {
                    agirPerson.setAdministrativeCommune(personObject.getString(AgirResources.COMMUNE_ADMINISTRATIVE));
                }
                if(personObject.has(AgirResources.CODE_ISO_PAYS_ADMINISTRATIVE) && !personObject.isNull(AgirResources.CODE_ISO_PAYS_ADMINISTRATIVE)) {
                    agirPerson.setAdministrativeIsoCode(personObject.getString(AgirResources.CODE_ISO_PAYS_ADMINISTRATIVE));
                }
                if(personObject.has(AgirResources.TELEPHONE_PERSONNEL) && !personObject.isNull(AgirResources.TELEPHONE_PERSONNEL)) {
                    agirPerson.setTelephonePersonal(personObject.getString(AgirResources.TELEPHONE_PERSONNEL));
                }
                if(personObject.has(AgirResources.TELEPHONE_MOBILE) && !personObject.isNull(AgirResources.TELEPHONE_MOBILE)) {
                    agirPerson.setTelephoneMobile(personObject.getString(AgirResources.TELEPHONE_MOBILE));
                }
                if(personObject.has(AgirResources.TELEPHONE_PROFESSIONEL) && !personObject.isNull(AgirResources.TELEPHONE_PROFESSIONEL)) {
                    agirPerson.setTelephoneProfessional(personObject.getString(AgirResources.TELEPHONE_PROFESSIONEL));
                }
                if(personObject.has(AgirResources.EMPLOYER) && !personObject.isNull(AgirResources.EMPLOYER)) {
                    agirPerson.setEmployer(personObject.getString(AgirResources.EMPLOYER));
                }
                if(personObject.has(AgirResources.CODE_CO) && !personObject.isNull(AgirResources.CODE_CO)) {
                    agirPerson.setCodeCo(personObject.getString(AgirResources.CODE_CO));
                }
                if(personObject.has(AgirResources.LABORATOIRE) && !personObject.isNull(AgirResources.LABORATOIRE)) {
                    agirPerson.setLab(personObject.getString(AgirResources.LABORATOIRE));
                }

                personsList.add(agirPerson);
            }
        }

        AgirPersons agirPersons=new AgirPersons(personsList);
        JAXBContext jaxbContext = JAXBContext.newInstance(AgirPersons.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter writer = new StringWriter();
        marshaller.marshal(agirPersons, writer);
        Utils.writeToXml(writer,sourceXmlFile);
    }

    private static void normalizeOrganisations(File sourceJsonFile, File sourceXmlFile) throws Exception, JAXBException {
        List<AgirOrganization> list=new ArrayList<>();

        String contents=Utils.getTextualContents(sourceJsonFile);
        JSONObject completeJson=new JSONObject(contents);

        if(completeJson.has(AgirResources.ROWS)){
            JSONArray orgsArray=completeJson.getJSONArray(AgirResources.ROWS);
            log.info("Parsing organizations from Agir. Found {} organization entries",orgsArray.length());

            for(int i=0;i<orgsArray.length();i++){
                JSONObject orgObject=orgsArray.getJSONObject(i);
                AgirOrganization agirOrganization=new AgirOrganization();

                if(orgObject.has(AgirResources.CODE_ORGANISME)) {
                    agirOrganization.setCodeOrganisme(orgObject.getString(AgirResources.CODE_ORGANISME));
                }
                if(orgObject.has(AgirResources.LIBELLE) && !orgObject.isNull(AgirResources.LIBELLE)) {
                    agirOrganization.setLabel(orgObject.getString(AgirResources.LIBELLE));
                }
                if(orgObject.has(AgirResources.TYPE_ORGANISME) && !orgObject.isNull(AgirResources.TYPE_ORGANISME)) {
                    agirOrganization.setOrganizationType(orgObject.getInt(AgirResources.TYPE_ORGANISME));
                }
                if(orgObject.has(AgirResources.STATUT) && !orgObject.isNull(AgirResources.STATUT)) {
                    agirOrganization.setStatut(orgObject.getInt(AgirResources.STATUT));
                }
                if(orgObject.has(AgirResources.ADDRESSE1) && !orgObject.isNull(AgirResources.ADDRESSE1)) {
                    agirOrganization.setAddress1(orgObject.getString(AgirResources.ADDRESSE1));
                }
                if(orgObject.has(AgirResources.ADDRESSE2) && !orgObject.isNull(AgirResources.ADDRESSE2)) {
                    agirOrganization.setAddress2(orgObject.getString(AgirResources.ADDRESSE2));
                }
                if(orgObject.has(AgirResources.COMMUNE) && !orgObject.isNull(AgirResources.COMMUNE)) {
                    agirOrganization.setCommune(orgObject.getString(AgirResources.COMMUNE));
                }
                if(orgObject.has(AgirResources.CODE_POSTAL) && !orgObject.isNull(AgirResources.CODE_POSTAL)) {
                    agirOrganization.setPostalCode(orgObject.getString(AgirResources.CODE_POSTAL));
                }
                if(orgObject.has(AgirResources.PAYS_CODE_ISO) && !orgObject.isNull(AgirResources.PAYS_CODE_ISO)) {
                    agirOrganization.setPaysIsoCode(orgObject.getString(AgirResources.PAYS_CODE_ISO));
                }
                list.add(agirOrganization);
            }
        }

        AgirOrganizations agirOrgs=new AgirOrganizations(list);
        JAXBContext jaxbContext = JAXBContext.newInstance(AgirOrganizations.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter writer = new StringWriter();
        marshaller.marshal(agirOrgs, writer);
        Utils.writeToXml(writer,sourceXmlFile);
    }

    private static void xmlifyGeneric(File sourceJsonFile, File targetXmlFile) throws Exception, JAXBException {
        System.out.println("Transforming "+sourceJsonFile.getAbsolutePath());
        String contents=Utils.getTextualContents(sourceJsonFile);
        JSONObject completeJson=new JSONObject(contents);

        List<String> elementNames=new ArrayList<>();
        StringBuilder xmlBuilder=new StringBuilder();
        xmlBuilder.append("<root>");
        if(completeJson.has(AgirResources.ROWS)) {
            JSONArray itemsArray = completeJson.getJSONArray(AgirResources.ROWS);
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject itemObject = itemsArray.getJSONObject(i);
                xmlBuilder.append(toXmlObject(FilenameUtils.getBaseName(sourceJsonFile.getAbsolutePath()),itemObject));
            }
        }
        xmlBuilder.append("</root>");
        writeStringToXmlFile(xmlBuilder.toString(),targetXmlFile);
    }

    public static void writeStringToXmlFile(String xml, File xmlFile) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        StreamResult result = new StreamResult(xmlFile);
        transformer.transform(new DOMSource(document), result);
    }

    private static String toXmlObject(String elementName, JSONObject itemObject){
        StringBuilder xmlBuilder=new StringBuilder();
        xmlBuilder.append("<").append(elementName).append(">");
        for(String key : itemObject.keySet()){
            if(itemObject.isNull(key)){
                xmlBuilder.append("<").append(key).append("/>");
            }else if(key.equalsIgnoreCase("detail_mission")){
                continue;
            }
            else{
                Object value=itemObject.get(key);
                    xmlBuilder.append("<").append(key).append(">").append(value.toString().replaceAll("&","&amp;").replaceAll("<","&lt;").replaceAll(">","&gt;")).append("</").append(key).append(">");
            }
        }
        xmlBuilder.append("</").append(elementName).append(">");
        return xmlBuilder.toString();
    }

    private static void xmlifyGenericFromFolder(File fromFolder, File toFolder) throws Exception {
        for(File fromFile : fromFolder.listFiles()){
            xmlifyGeneric(fromFile,new File(toFolder.getAbsolutePath()+"/"+FilenameUtils.getBaseName(fromFile.getAbsolutePath())+".xml"));
        }
    }

    public static void main(String[] args) throws Exception {
//        normalizeProjects(new File("projet.json"),new File("projet.xml"));
//        normalizePersons(new File("personne.json"),new File("personne.xml"));
//        normalizeOrganisations(new File("organisme.json"),new File("organisme.xml"));
//        xmlifyGeneric(new File("in/ref_code_operation.json"),new File("out/ref_code_operation.xml"));
        xmlifyGenericFromFolder(new File("in"),new File("out"));

    }
}
