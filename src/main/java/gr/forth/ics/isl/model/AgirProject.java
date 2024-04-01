package gr.forth.ics.isl.model;

import lombok.Data;
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
public class AgirProject {
    private int projectId;       //projet.projet_id
    private String codeOperation;   //projet.code_operation
    private String year;            //projet.annee
    private int responsibleId;      //projet.responsable
    private int duration;           //projet.duree
    private String title;           //projet.intitule
    private String resume;          //projet.resume
    private String researchAxisInrap;   //projet.axe_researche_inrap
    private float budget;           //projet.budget_accorde
    private String code;            //projet.code_co
    private String codeTypology;    //projet.code_typologie
    private String subType;         // projet.code_sous_type
    private int status;             //projet.statut
    private String addresse1;       //projet.adresse1
    private String addresse2;       //projet.adresse2
    private String postalCode;      //projet.code_postal
    private String city;            //projet.ville
    private String organizer;       //projet.organisateur
    private String organizerInstitution; //projet.institution_organisatrice
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