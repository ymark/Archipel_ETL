package gr.forth.ics.isl.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data @NoArgsConstructor
public class HalAuthor {
    private String authorName;
    private Integer personIdInternal;
    private Integer personIdHal;
    private Map<Integer,String> organizationsMap=new HashMap<>();

    public void addOrganization(int id, String name){
        organizationsMap.put(id,name);
    }
}