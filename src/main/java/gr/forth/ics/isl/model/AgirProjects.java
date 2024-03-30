package gr.forth.ics.isl.model;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@XmlRootElement(name = "projects")
public class AgirProjects {
    private List<AgirProject> projectList;

    public AgirProjects() {}

    public AgirProjects(List<AgirProject>projectList) {
        this.projectList = projectList;
    }

    @XmlElement(name = "project")
    public List<AgirProject> getProjectList() {
        return this.projectList;
    }

    public void setProjectList(List<AgirProject> projectList) {
        this.projectList = projectList;
    }
}
