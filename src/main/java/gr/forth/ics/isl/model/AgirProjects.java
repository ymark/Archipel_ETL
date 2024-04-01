package gr.forth.ics.isl.model;

import gr.forth.ics.isl.common.AgirResources;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@XmlRootElement(name = AgirResources.ROOT)
public class AgirProjects {
    private List<AgirProject> projectList;

    public AgirProjects() {}

    public AgirProjects(List<AgirProject>projectList) {
        this.projectList = projectList;
    }

    @XmlElement(name = AgirResources.PROJET)
    public List<AgirProject> getProjectList() {
        return this.projectList;
    }

    public void setProjectList(List<AgirProject> projectList) {
        this.projectList = projectList;
    }
}
