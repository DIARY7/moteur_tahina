/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package importUtils.fileItemParser;

import importUtils.inputStreamTranslater.InputStreamTranslater;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.fileupload.FileItem;

/**
 * Classe pour analyser et convertir le fichier selon son type
 * Le ISTtranslaterEnum.java est utilisé pour lister toutes les stratégies disponibles.
 * @author Soa
 */
public class FileItemParser {
    
    /**
     * cet attribut contient l'entrée de chaque stratégie où la clé est le type du fichier
     * 
     */
    private static final HashMap<String,InputStreamTranslater> CONTEXT;
  
    /**
     * inclusion de toutes les stratégies dans CONTEXT
     */
    static{
        CONTEXT= new HashMap<String, InputStreamTranslater>();
        ISTtranslaterEnum[] map= ISTtranslaterEnum.values();
        for(ISTtranslaterEnum state : map) CONTEXT.put(state.type(), state.parser());
    }
    
    /**
     *  Cette fonction consiste à analyser l'extension du fichier et à transformer le fichier en une liste d'hashMap
     * @param fileItem : le fichier importé
     * @param fields : liste des noms des champs à importer
     * @param bool : true si le fichier a un entête sinon false
     * @return
     * @throws Exception 
     */
    public List<HashMap<String, String>> parse(FileItem fileItem,String[] fields,boolean bool) throws Exception{
        try{
            String fileName = fileItem.getName();
            System.out.println("filename "+fileName);
            String extension = fileName.split("\\.")[fileName.split("\\.").length-1];
            System.out.println("extension "+extension);
            InputStreamTranslater translater = CONTEXT.get(extension);
            return translater.translate(fileItem.getInputStream(), fields, bool);
        }
        catch(Exception e){
            throw e;
        }
    }
}
