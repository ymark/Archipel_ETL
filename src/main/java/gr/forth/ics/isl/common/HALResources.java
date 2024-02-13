package gr.forth.ics.isl.common;

public class HALResources {
    /* HAL REST API resources */
    public static final String NUMBER_OF_RESULTS="1000";
    public static final String INRAP="inrap";
    public static final String HAL_REST_API_PREFIX="https://api.archives-ouvertes.fr/search/";
//    public static final String HAL_REST_API_AUTHORS_PARAMETERS="?q=*%3A*&fl=authIdHal_s,authIdHal_i,authFullName_s,authIdFormPerson_s,authIdPerson_i,authFirstName_s,authLastName_s,authOrganismId_i,authOrganism_s,authUrl_s";
    public static final String HAL_REST_API_AUTHORS_PARAMETERS="?q=*%3A*&fl=authIdHal_i,authId_i,authFirstName_s,authLastName_s,authUrl_s,authIdPerson_i,authIdHasPrimaryStructure_fs,authIdLastNameFirstName_fs";
    public static final String ROWS="rows";
    public static final String START="start";

    /* HAL JSON response resources */
    public static final String AUTH_ID_PERSON_I="authIdPerson_i";
    public static final String AUTH_IDHASPRIMARYSTRUCTURE_FS="authIdHasPrimaryStructure_fs";
    public static final String AUTH_IDlASTNAMEFIRSTNAME_FS="authIdLastNameFirstName_fs";
    public static final String AUTH_FIRSTNAME_S="authFirstName_s";
    public static final String AUTH_lASTNAME_S="authLastName_s";
    public static final String DOCS="docs";
    public static final String FACET_SEP="FacetSep";
    public static final String JOIN_SEP="JoinSep";
    public static final String RESPONSE="response";

    /* HAL XML resources */
    public static final String AUTHOR_ELEMENT="author";
    public static final String AUTHOR_NAME_ELEMENT="name";
    public static final String AUTHOR_ID_INTERNAL_ELEMENT="id_internal";
    public static final String AUTHOR_ID_HAL_ELEMENT="id_hal";
    public static final String AUTHOR_ORGANIZATION_ELEMENT="organization";
    public static final String AUTHOR_ORGANIZATIONS_ELEMENT="organizations";

}
