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
public class AgirPerson {
    @XmlElement(name= AgirResources.ID)
    private int personId;           //personne.id
    @XmlElement(name= AgirResources.MATRICULE)
    private String matricule;       //personne.matricule
    @XmlElement(name= AgirResources.NOM)
    private String name;            //personne.nom
    @XmlElement(name= AgirResources.PRENOM)
    private String surname;         //personne.prenom
    @XmlElement(name= AgirResources.CIVILITE)
    private String civility;        //personne.civilite
    @XmlElement(name= AgirResources.STATUT)
    private int statut;             //personne.statut
    @XmlElement(name= AgirResources.EMAIL)
    private String email;           //personne.email
    @XmlElement(name= AgirResources.CATEGORIE)
    private String category;        //personne.categorie
    @XmlElement(name= AgirResources.CONTRAT)
    private String contrat;         //personne.contrat
    @XmlElement(name= AgirResources.FILIERE)
    private String filiere;         //personne.filiere
    @XmlElement(name= AgirResources.TITRE)
    private String titre;           //personne.titre
    @XmlElement(name= AgirResources.ADDRESSE_ADMINISTRATIVE1)
    private String administrativeAddr1;     //personne.adresse_administrative1
    @XmlElement(name= AgirResources.ADDRESSE_ADMINISTRATIVE2)
    private String administrativeAddr2;     //personne.adresse_administrative2
    @XmlElement(name= AgirResources.CP_ADMINISTRATIVE)
    private String administrativePostalCode;//personne.cp_administrative
    @XmlElement(name= AgirResources.COMMUNE_ADMINISTRATIVE)
    private String administrativeCommune;   //personne.commune_administrative
    @XmlElement(name= AgirResources.CODE_ISO_PAYS_ADMINISTRATIVE)
    private String administrativeIsoCode;   //personne.code_iso_pays_administrative
    @XmlElement(name= AgirResources.TELEPHONE_PERSONNEL)
    private String telephonePersonal;       //personne.telephone_personnel
    @XmlElement(name= AgirResources.TELEPHONE_MOBILE)
    private String telephoneMobile;         //personne.telephone_mobile
    @XmlElement(name= AgirResources.TELEPHONE_PROFESSIONEL)
    private String telephoneProfessional;   //personne.telephone_professionnel
    @XmlElement(name= AgirResources.EMPLOYER)
    private String employer;                //personne.employeur
    @XmlElement(name= AgirResources.CODE_CO)
    private String codeCo;                  //personne.code_co
    @XmlElement(name= AgirResources.LABORATOIRE)
    private String lab;                     //personne.laboratoire
}