package gr.forth.ics.isl.common;

public class Utils {
    public static String parseFrenchColumn(String frenchColumn){
        switch(frenchColumn){
            case Resources.LABEL_1:
            case Resources.LABEL_2:
            case Resources.LABEL_3:
                return Resources.LABEL_+frenchColumn;
            case Resources.LABEL_FONCTION:
                return Resources.LABEL_FUNCTION;
            case Resources.LABEL_FONCTIONS:
                return Resources.LABEL_FUNCTIONS;
            case Resources.LABEL_NB_JOURS:
            case Resources.LABEL_NBRE_DE_JOURS:
                return Resources.LABEL_NUMBER_OF_DAYS;
            case Resources.LABEL_ORGANISME_D_APPARTENANCE:
                return Resources.LABEL_ORGANIZATION_OF_MEMBERSHIP;
            case Resources.LABEL_PRENOM_NOM:
                return Resources.LABEL_FIRST_LAST_NAME;
            case Resources.LABEL_TYPE_INTERVENANT:
                return Resources.LABEL_CONTRIBUTOR_TYPE;
            default:
                return null;    // do not export in XML
        }
    }
}