package importUtils.importable;

import java.util.*;
import java.sql.Connection;
/**
 * Cette interface définit la classe de l'import de la classe ClassMAPtable
 * <p>
 *  Ci-dessous un exemple d'implémentation de la classe:
 * </p>
 * La classe test est une classe qui implémente ClassMAPtable
 * <pre>
 *  public class TestImport extends Test implements Importable{
 *      //Nous devons créer une vue avec ces attributs et d'autres attributs que nous ne voulons pas importer dans le fichier
 *      private String filee; 
 *      //Cet attribut represente le fichier à importer 
 *      private int avecEntete;
 *      // Cet attribut indique par un entier si un fichier a un en-tête ou non
 *      private Date dateN;
 *      @Override
*       public void buildSelf(HashMap hashmap, Connection con) throws Exception {
*           // Cette fonction consiste à attribuer les valeurs des attributs de la classe
*           try{
*               this.setAge(this.convertInt((String) hashmap.get("age")));
*               this.setNom((String) hashmap.get("nom"));
*               this.setPrenom((String) hashmap.get("prenom"));
*               this.setArgent(this.convertDouble((String) hashmap.get("argent")));
*           }
*           catch(Exception e){
*              throw e;
*           }
*       }
*       @Override
*       public String[] importedFields() {
*           // cette fonction retoune les noms des attributs à importer dans le fichier
*           String[] fields = {"nom","prenom","argent","age"};
*           return fields;
*       }
*       @Override
*       public Importable duplicate() throws Exception {
*           // cette fonction duplique l'objet correspondant à l'importation 
*          TestImport dup = new TestImport();
*          // attribue une valeur à tout attribut qui n'est pas importé dans le fichier
*          dup.setDate(new Date());
*          return dup;
*        }
*       @Override
*       public boolean withHeader() {
*           // cette fonction retourne un boolean true si l'attribut avecEntete correspond à un fichier avec header , sinon false
*           return this.avecEntete == 0;
*      }
*
*    }

 * </pre>

 * @author Soa
 */
public interface Importable {
    /**
     * Cette fonction consiste à attribuer les valeurs des attributs de la classe
     * @param hashmap : représente les valeurs des attributs pour une ligne d'insertion
     * @param con : connexion ouverte à la base de donnée
     * @throws Exception 
     */
    public void buildSelf(HashMap<String,String> hashmap, Connection con) throws Exception;
    
    /**
     * Cette fonction consiste à retourner les noms de colonnes qu'on veut importer dans excel
     * @return 
     */
    public String[] importedFields();
    
    /**
     * Cette fonction consiste à dupliquer l'objet souhaité pour l'insertion . La classe de l'objet doit implementer cette interface 
     * @return
     * @throws Exception 
     */
    public Importable duplicate() throws Exception;
    
    /**
     * Cette fonction renvoie si le fichier a un en-tête ou non
     * @return 
     */
    public boolean withHeader();
}
