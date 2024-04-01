package gr.forth.ics.isl.model;

import gr.forth.ics.isl.common.AgirResources;
import lombok.Data;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class AgirOrganization {
    @XmlElement(name=AgirResources.CODE_ORGANISME)
    private String codeOrganisme;
    @XmlElement(name=AgirResources.LIBELLE)
    private String label;
    @XmlElement(name=AgirResources.TYPE_ORGANISME)
    private int organizationType;
    @XmlElement(name=AgirResources.STATUT)
    private int statut;
    @XmlElement(name=AgirResources.ADDRESSE1)
    private String address1;
    @XmlElement(name=AgirResources.ADDRESSE2)
    private String address2;
    @XmlElement(name=AgirResources.COMMUNE)
    private String commune;
    @XmlElement(name=AgirResources.CODE_POSTAL)
    private String postalCode;
    @XmlElement(name=AgirResources.PAYS_CODE_ISO)
    private String paysIsoCode;
}