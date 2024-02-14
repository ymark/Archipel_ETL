package gr.forth.ics.isl.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data @NoArgsConstructor
public class JsonExportPublication {
    private Map<String,String> codesMap=new HashMap<>();
    private String inrapPublicationCode;
    private String locationRegion;
    private String locationDepartment;
    private String locationAddress;
    private Set<String> locationCommunesCollection=new HashSet<>();

    public void addCode(String codeName, String codeValue){
        this.codesMap.put(codeName,codeValue);
    }

    public void addLocationCommune(String communeName){
        this.locationCommunesCollection.add(communeName);
    }
}
