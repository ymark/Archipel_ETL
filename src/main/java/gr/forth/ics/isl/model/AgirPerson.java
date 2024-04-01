package gr.forth.ics.isl.model;

import lombok.Data;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@Data
public class AgirPerson {
    private int personId;           //personne.id
    private String matricule;       //personne.matricule
    private String name;            //personne.nom
    private String surname;         //personne.prenom
    private String civility;        //personne.civilite
    private int statut;             //personne.statut
    private String email;           //personne.email
    private String category;        //personne.categorie
    private String contrat;         //personne.contrat
    private String filiere;         //personne.filiere
    private String titre;           //personne.titre
    private String administrativeAddr1;     //personne.adresse_administrative1
    private String administrativeAddr2;     //personne.adresse_administrative2
    private String administrativePostalCode;//personne.cp_administrative
    private String administrativeCommune;   //personne.commune_administrative
    private String administrativeIsoCode;   //personne.code_iso_pays_administrative
    private String telephonePersonal;       //personne.telephone_personnel
    private String telephoneMobile;         //personne.telephone_mobile
    private String telephoneProfessional;   //personne.telephone_professionnel
    private String employer;                //personne.employeur
    private String codeCo;                  //personne.code_co
    private String lab;                     //personne.laboratoire
}