package gr.forth.ics.isl.model;

import gr.forth.ics.isl.common.AgirResources;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@XmlRootElement(name = AgirResources.ROOT)
public class AgirOrganizations {
    private List<AgirOrganization> orgList;

    public AgirOrganizations() {}

    public AgirOrganizations(List<AgirOrganization> list) {
        this.orgList = list;
    }

    @XmlElement(name = AgirResources.ORGANISME)
    public List<AgirOrganization> getOrgList() {
        return this.orgList;
    }

    public void setOrgList(List<AgirOrganization> list) {
        this.orgList = list;
    }
}
