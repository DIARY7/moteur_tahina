package importUtils.inputStreamTranslater;

import java.util.*;
import java.io.InputStream;

/**
 * interface qui définit la classe qui va transformer l'inputstream du fichier en une liste de hashMap
 * @author Soa
 */
public interface InputStreamTranslater {
    /**
     * Cette fonction consiste à transformer l'inputstream en une liste d'HashMap
     * @param inputStream : l'inputstream du fichier importé
     * @param fields : liste des noms des champs à importer
     * @param bool : true si le fichier a un entête sinon false
     * @return
     * @throws Exception 
     */
  public List<HashMap<String, String>> translate(InputStream inputStream,String[] fields,boolean bool) throws Exception;
}