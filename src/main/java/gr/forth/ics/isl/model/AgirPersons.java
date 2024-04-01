package gr.forth.ics.isl.model;

import gr.forth.ics.isl.common.AgirResources;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@XmlRootElement(name = AgirResources.ROOT)
public class AgirPersons {
    private List<AgirPerson> personsList;

    public AgirPersons() {}

    public AgirPersons(List<AgirPerson>projectList) {
        this.personsList = projectList;
    }

    @XmlElement(name = AgirResources.PERSONNE)
    public List<AgirPerson> getProjectList() {
        return this.personsList;
    }

    public void setProjectList(List<AgirPerson> projectList) {
        this.personsList = projectList;
    }
}
