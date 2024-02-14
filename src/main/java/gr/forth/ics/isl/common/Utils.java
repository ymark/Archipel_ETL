package gr.forth.ics.isl.common;

import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class Utils {
    public static String parseFrenchColumn(String frenchColumn){
        switch(frenchColumn){
            case JsonExportResources.LABEL_1:
            case JsonExportResources.LABEL_2:
            case JsonExportResources.LABEL_3:
                return JsonExportResources.LABEL_+frenchColumn;
            case JsonExportResources.LABEL_FONCTION:
                return JsonExportResources.LABEL_FUNCTION;
            case JsonExportResources.LABEL_FONCTIONS:
                return JsonExportResources.LABEL_FUNCTIONS;
            case JsonExportResources.LABEL_NB_JOURS:
            case JsonExportResources.LABEL_NBRE_DE_JOURS:
                return JsonExportResources.LABEL_NUMBER_OF_DAYS;
            case JsonExportResources.LABEL_ORGANISME_D_APPARTENANCE:
                return JsonExportResources.LABEL_ORGANIZATION_OF_MEMBERSHIP;
            case JsonExportResources.LABEL_PRENOM_NOM:
                return JsonExportResources.LABEL_FIRST_LAST_NAME;
            case JsonExportResources.LABEL_TYPE_INTERVENANT:
                return JsonExportResources.LABEL_CONTRIBUTOR_TYPE;
            default:
                return null;    // do not export in XML
        }
    }

    public static void exportXmlToFile(Document document, File file) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        StreamResult result = new StreamResult(file);
        DOMSource source = new DOMSource(document);
        transformer.transform(source, result);
    }
}