package gr.forth.ics.isl.model;

import gr.forth.ics.isl.common.HALResources;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

@Data @NoArgsConstructor
public class HalAuthor {
    private String authorName;
    private String personIdInternal;
    private String personIdHal;
    private Map<String,String> organizationsMap=new HashMap<>();

    public void addOrganization(String id, String name){
        organizationsMap.put(id,name);
    }

    public Element toXml(Document doc){
        Element authorElement=doc.createElement(HALResources.AUTHOR_ELEMENT);
        if(personIdInternal!=null){
            Element personIdInternalElement=doc.createElement(HALResources.AUTHOR_ID_INTERNAL_ELEMENT);
            personIdInternalElement.setTextContent(this.getPersonIdInternal());
            authorElement.appendChild(personIdInternalElement);
        }
        if(personIdHal!=null && !personIdHal.equalsIgnoreCase("0")){
            Element personIdHalElement=doc.createElement(HALResources.AUTHOR_ID_HAL_ELEMENT);
            personIdHalElement.setTextContent(this.getPersonIdHal());
            authorElement.appendChild(personIdHalElement);
        }
        if(authorName!=null){
            Element authorNameElement=doc.createElement(HALResources.AUTHOR_NAME_ELEMENT);
            authorNameElement.setTextContent(this.getAuthorName());
            authorElement.appendChild(authorNameElement);
        }
        if(!this.getOrganizationsMap().isEmpty()){
            Element organizationsElement=doc.createElement(HALResources.AUTHOR_ORGANIZATIONS_ELEMENT);
            for(String organizationId : this.getOrganizationsMap().keySet()){
                Element organizationElement=doc.createElement(HALResources.AUTHOR_ORGANIZATION_ELEMENT);
                Element orgIdElement=doc.createElement(HALResources.AUTHOR_ID_HAL_ELEMENT);
                orgIdElement.setTextContent(organizationId);
                Element orgNameElement=doc.createElement(HALResources.AUTHOR_NAME_ELEMENT);
                orgNameElement.setTextContent(this.getOrganizationsMap().get(organizationId));
                organizationElement.appendChild(orgIdElement);
                organizationElement.appendChild(orgNameElement);
                organizationsElement.appendChild(organizationElement);
            }
            authorElement.appendChild(organizationsElement);
        }
        return authorElement;
    }
}