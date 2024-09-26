/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package importUtils.fileItemParser;

import importUtils.inputStreamTranslater.InputStreamTranslater;
import importUtils.inputStreamTranslater.imp.XlsTranslater;


/**
 * Cet enum contient toutes les stratégies pour analyser le fichier selon son type
 * pour ajouter une nouvelle stratégie, vous devez fournir le type du résultat de l'analyse sous forme de chaîne
 * et un objet qui est l'implémentation de la méthode d'analyse ;
 * @author Soa
 */
public enum ISTtranslaterEnum {
    /**
     * extension xls
     */
    XLS("xls",new XlsTranslater()),
     /**
     * extension xlsx
     */
    XLSX("xlsx",new XlsTranslater());
    /**
     * type du fichier importé
     */
    private final String type;
    /**
     * instance de la classe qui analyse le fichier
     */
    private final InputStreamTranslater parser;
    
    /**
     * 
     * @param type : type du fichier importé
     * @param parser : classe qui analyse le fichier
     */
    private ISTtranslaterEnum(String type,InputStreamTranslater parser){
        this.type=type;
        this.parser=parser;
    }
    
    /**
     * retourne la classe parser
     * @return 
     */
    public InputStreamTranslater parser(){
        return this.parser;
    }
    
    /**
     * retourne le type de l'extension du fichier
     * @return 
     */
    public String type(){
        return this.type;
    }
}
