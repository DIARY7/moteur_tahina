/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package importUtils;
import importUtils.importable.Importable;
import bean.ClassMAPTable;
import importUtils.fileItemParser.FileItemParser;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.fileupload.FileItem;
/**
 * Classe pour importer le fichier dans la base de donnée 
 * @author Soa
 */
public class Importer {
    /**
     * Cette fonction consiste à créer une liste d'hashMap , de créer pour chaque hashMap un objet et de l'inserer dans la base 
     * @param importable : classe de l'import de la classe ClassMAPTable
     * @param fileItem : le fichier importé
     * @param refUser : id de l'utilisateur
     * @param connection : connexion ouverte à la base de donnée
     * @throws Exception 
     */
    public void importFile(Importable importable,FileItem fileItem, String refUser ,Connection connection) throws Exception{
        try{ 
            FileItemParser parser = new FileItemParser();
            String[] fields = importable.importedFields();
            List<HashMap<String, String>> list = parser.parse(fileItem, fields, importable.withHeader());
            for(int i=0;i<list.size();i++){
                Importable imp = importable.duplicate();
                imp.buildSelf(list.get(i), connection);
                ClassMAPTable cmt = (ClassMAPTable)imp;
                cmt.createObject(refUser, connection);
            }
        }
        catch(Exception e){
            throw e;
        }
       
    }
}
