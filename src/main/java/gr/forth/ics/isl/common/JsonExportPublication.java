package gr.forth.ics.isl.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

    public Element toXml(Document doc){
        Element publicationElement=doc.createElement(JsonExportResources.PUBLICATION_INFO);
        if(this.inrapPublicationCode!=null){
            Element inrapCodeElement=doc.createElement(JsonExportResources.PUBLICATION_INRAP_CODE);
            inrapCodeElement.setTextContent(this.getInrapPublicationCode());
            publicationElement.appendChild(inrapCodeElement);
        }
        if(this.locationRegion!=null){
            Element regionElement=doc.createElement(JsonExportResources.LOCALISATION_REGION);
            regionElement.setTextContent(this.getLocationRegion());
            publicationElement.appendChild(regionElement);
        }
        if(this.locationDepartment!=null){
            Element depElement=doc.createElement(JsonExportResources.LOCALISATION_DEPARTMENT);
            depElement.setTextContent(this.getLocationDepartment());
            publicationElement.appendChild(depElement);
        }
        if(this.locationAddress!=null){
            Element addrElement=doc.createElement(JsonExportResources.LOCALISATION_ADDRESS);
            addrElement.setTextContent(this.getLocationAddress());
            publicationElement.appendChild(addrElement);
        }
        for(String communeText : this.locationCommunesCollection){
            Element communeElement=doc.createElement(JsonExportResources.LOCALISATION_COMMUNE);
            communeElement.setTextContent(communeText);
            publicationElement.appendChild(communeElement);
        }
        if(!this.codesMap.isEmpty()){
//            Element codesElement=doc.createElement(JsonExportResources.CODES_XML);
            for(String codeName : codesMap.keySet()){
                Element codeInfoElement=doc.createElement(JsonExportResources.CODE_INFO);
                Element codeNameElement=doc.createElement(JsonExportResources.CODE_NAME);
                codeNameElement.setTextContent(codeName);
                Element codeValueElement=doc.createElement(JsonExportResources.CODE_VALUE);
                codeValueElement.setTextContent(this.codesMap.get(codeName));
                codeInfoElement.appendChild(codeNameElement);
                codeInfoElement.appendChild(codeValueElement);
                publicationElement.appendChild(codeInfoElement);
            }
//            publicationElement.appendChild(codesElement);
        }


        return publicationElement;
    }
}
