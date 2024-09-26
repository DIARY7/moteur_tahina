/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reporting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Cette classe permet de géré les fichiers dans le CDN 
 * <h3>Exemple d'utilisation</h3>
 * <pre>
 * ReportingCdn.exportFile(print, fonctionnalite, fileNameFinal, typeFile);
 * </pre>
 * @author BICI
 */
public class ReportingCdn {
    
    static java.util.Properties prop = configuration.CynthiaConf.load();
    static String chemin = prop.getProperty("cdnPDF");
    
    public enum Fonctionnalite {
        TRESORERIE, DN, PAIE, RECETTE, DEFAUT
    }

    /**
     * Cette fonction permet d'obtenir l'extension du type de fichier
     * @param reportType differents format type de fichier
     * @return l'extion du type de fichier 
     */
    public static String getExtension (ReportingUtils.ReportType reportType){
        switch(reportType){
            case PDF :
                return ".pdf";
            case XLSX :
                return ".xlsx";
            case XLS :
                    return ".xls";
        }
        return null;
    }

    /**
     * Permet de déterminer le chemin ou l'emplacement de diverses
     * ressources liées à différentes fonctionnalités.
     * @param fonctionnalite
     * @return soit la valeur de la propriété correspondante soit null
     */
    public static String cheminFonct (Fonctionnalite fonctionnalite){
        java.util.Properties prop = configuration.CynthiaConf.load();
        switch(fonctionnalite){
            case TRESORERIE :
                return prop.getProperty("cdnTresorerie");
            case DN :
                return prop.getProperty("cdnDN");
            case PAIE :
                return prop.getProperty("cdnPaie");
            case RECETTE :
                return prop.getProperty("cdnRecette");
            case DEFAUT :
                return "/";
        }
        return null;
    }

    /**
     * utilisée pour générer des chemins de fichiers permettant de générer des rapports liés à une fonctionnalité spécifique,
     * @param fonctionnalite contient cinq constantes, qui sont "TRESORERIE", "DN", "PAIE", "RECETTE" et "DEFAUT"
     * @return le chemin de fichiers
     */
    public static String cheminReport(Fonctionnalite fonctionnalite){
        return chemin + cheminFonct(fonctionnalite)+"/";
    }

    /**
     * Sert à obtenir le fichier dans le chemin demandé
     * @param filename le nom du fichier
     * @param fonctionnalite contient cinq constantes, qui sont "TRESORERIE", "DN", "PAIE", "RECETTE" et "DEFAUT"
     * @return le fichier 
     */
    public static File getFile (String filename, Fonctionnalite fonctionnalite){
        File file = new File(cheminReport(fonctionnalite)+"/"+filename);
        return file;
    }

    /**
     * Cette méthode exporte le rapport sous forme de fichier Excel dans le chemin d'accès spécifié.
     * @param print 
     * @param fonctionnalite contient cinq constantes, qui sont "TRESORERIE", "DN", "PAIE", "RECETTE" et "DEFAUT"
     * @param fileName  le nom du fichier
     * @throws FileNotFoundException
     * @throws JRException
     */
    private static void exportFileServeurXLS (JasperPrint print, Fonctionnalite fonctionnalite, String fileName) throws FileNotFoundException, JRException{
        String fonct=cheminFonct(fonctionnalite);
        OutputStream output = new FileOutputStream(new File(chemin+fonct+"/"+fileName+".xlsx")); 
        ReportingUtils reporting = new ReportingUtils();
        reporting.exportReport(print, ReportingUtils.ReportType.XLSX, output); 
    }

    /**
     * c
     * @param print
     * @param fonctionnalite contient cinq constantes, qui sont "TRESORERIE", "DN", "PAIE", "RECETTE" et "DEFAUT"
     * @param fileName le nom du fichier
     * @throws FileNotFoundException
     * @throws JRException
     */
    private static void exportFileServeurPDF (JasperPrint print, Fonctionnalite fonctionnalite, String fileName) throws FileNotFoundException, JRException{
        String fonct=cheminFonct(fonctionnalite);
        OutputStream output = new FileOutputStream(new File(chemin+fonct+"/"+fileName+".pdf")); 
        JasperExportManager.exportReportToPdfStream(print, output); 
    }

    /**
     * Cette méthode exporte le rapport sous forme de fichier PDF ou Excel 
     * @param print
     * @param fonctionnalite  contient cinq constantes, qui sont "TRESORERIE", "DN", "PAIE", "RECETTE" et "DEFAUT
     * @param fileName le nom du fichier
     * @param typeFile type de fichier  
     * @throws FileNotFoundException
     * @throws JRException
     */
    public static void exportFile(JasperPrint print, Fonctionnalite fonctionnalite, String fileName, ReportingUtils.ReportType typeFile) throws FileNotFoundException, JRException{
        switch(typeFile){
            case XLSX :
                exportFileServeurXLS(print, fonctionnalite, fileName);
            case PDF :
                exportFileServeurPDF(print, fonctionnalite, fileName);
        }
    }

    /**
     * Cette fonction appelle la fonction avec type de fichier PDF
     * <pre> exportFile(JasperPrint print, Fonctionnalite fonctionnalite, String fileName, ReportingUtils.ReportType typeFile) </pre>
     * @param print
     * @param fonctionnalite contient cinq constantes, qui sont "TRESORERIE", "DN", "PAIE", "RECETTE" et "DEFAUT
     * @param fileName le nom du fichier
     * @throws FileNotFoundException
     * @throws JRException
     */
    public static void exportFile(JasperPrint print, Fonctionnalite fonctionnalite, String fileName) throws FileNotFoundException, JRException{
        exportFile(print, fonctionnalite, fileName, ReportingUtils.ReportType.PDF);
    }

    /**
     * Cette fonction appelle la fonction avec type de fichier XLSX
     * <pre> exportFile(JasperPrint print, Fonctionnalite fonctionnalite, String fileName, ReportingUtils.ReportType typeFile) </pre>
     * @param print
     * @param fonctionnalite contient cinq constantes, qui sont "TRESORERIE", "DN", "PAIE", "RECETTE" et "DEFAUT
     * @param fileName le nom du fichier
     * @throws FileNotFoundException
     * @throws JRException
     */
    public static void exportFileXLS(JasperPrint print, Fonctionnalite fonctionnalite, String fileName) throws FileNotFoundException, JRException{
        exportFile(print, fonctionnalite, fileName, ReportingUtils.ReportType.XLSX);
    }

    /**
     * Cette fonction gère si le fichier existe dèja ou non
     * @param fonctionnalite contient cinq constantes, qui sont "TRESORERIE", "DN", "PAIE", "RECETTE" et "DEFAUT
     * @param fileName le nom du fichier
     * @return true or false
     * @throws FileNotFoundException
     */
    public static boolean checkExistingFile (Fonctionnalite fonctionnalite, String fileName) throws FileNotFoundException{
        File file = getFile(fileName, fonctionnalite);
        if(file.exists()) return true;  
        return false;
    }

    /**
     * Cette methode sert à supprimer un fichier 
     * @param path chemin 
     * @param id identifiant du fichier
     * @throws Exception
     */
    public static void deleteFiles(File path, String id) throws Exception{
        if(path.exists()){
            path.delete();
            UtilitaireServeurPDF.deleteFromServeur(id);
        }
    }
}
