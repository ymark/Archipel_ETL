package gr.forth.ics.isl.model;

import gr.forth.ics.isl.common.AgirResources;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/** Model for the table projet
 *
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class AgirProject {
    @XmlElement(name= AgirResources.PROJET_ID)
    private int projectId;       //projet.projet_id
    @XmlElement(name= AgirResources.CODE_OPERATION)
    private String codeOperation;   //projet.code_operation
    @XmlElement(name= AgirResources.ANNEE)
    private String year;            //projet.annee
    @XmlElement(name= AgirResources.RESPONSABLE)
    private int responsibleId;      //projet.responsable
    @XmlElement(name= AgirResources.DUREE)
    private int duration;           //projet.duree
    @XmlElement(name= AgirResources.INTITULE)
    private String title;           //projet.intitule
    @XmlElement(name= AgirResources.RESUME)
    private String resume;          //projet.resume
    @XmlElement(name= AgirResources.AXE_RECHERHCE_INRAP)
    private String researchAxisInrap;   //projet.axe_researche_inrap
    @XmlElement(name= AgirResources.BUDGET_ACCORDE)
    private float budget;           //projet.budget_accorde
    @XmlElement(name= AgirResources.CODE_CO)
    private String code;            //projet.code_co
    @XmlElement(name= AgirResources.CODE_TYPOLOGIE)
    private String codeTypology;    //projet.code_typologie
    @XmlElement(name= AgirResources.CODE_SOUS_TYPE)
    private String subType;         // projet.code_sous_type
    @XmlElement(name= AgirResources.STATUT)
    private int status;             //projet.statut
    @XmlElement(name= AgirResources.ADDRESSE1)
    private String addresse1;       //projet.adresse1
    @XmlElement(name= AgirResources.ADDRESSE2)
    private String addresse2;       //projet.adresse2
    @XmlElement(name= AgirResources.CODE_POSTAL)
    private String postalCode;      //projet.code_postal
    @XmlElement(name= AgirResources.VILLE)
    private String city;            //projet.ville
    @XmlElement(name= AgirResources.ORGANISATEUR)
    private String organizer;       //projet.organisateur
    @XmlElement(name= AgirResources.INSTITUTION_ORGANISATRICE)
    private String organizerInstitution; //projet.institution_organisatrice
    @XmlElement(name= AgirResources.OPERATIONS_LIEES)
    private Set<String> operationsList=new HashSet<>(); //projet.operations_liees (normalized)

    public void addOperationCode(String operationCode){
        this.operationsList.add(operationCode);
    }

    public void parseAndDefineOperationsList(String operationListStr){
        List<String> values = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"((F|D|R)\\d+)\"");
        Matcher matcher = pattern.matcher(operationListStr);
        while (matcher.find()) {
            values.add(matcher.group(1));
        }
        this.operationsList.addAll(values);
    }
}