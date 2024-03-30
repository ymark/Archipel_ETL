package gr.forth.ics.isl.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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