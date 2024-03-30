package gr.forth.ics.isl.model;

import lombok.Data;
import java.util.HashSet;
import java.util.Set;


/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@Data
public class AgirProject {
    private String codeOperation;
    private String year;
    private int responsibleId;
    private int duration;
    private String title;
    private String resume;
    private Set<String> operationsList=new HashSet<>();

    public void addOperationCode(String operationCode){
        this.operationsList.add(operationCode);
    }
}