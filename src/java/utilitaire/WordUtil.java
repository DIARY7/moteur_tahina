/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitaire;

import java.lang.reflect.Method;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author BICI
 */
 import java.io.*;

 
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
 
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;


/**
 * Cette classe génère des documents Word
 * 
 * @author BICI
 */
public class WordUtil {
    
	/**
	 * Générer plusieurs documents Word à partir d'un seul modèle de document et de plusieurs objets d'entrée
	 * @param docSource le chemin d'accès au document
	 * @param o tableau d'objet
	 * @param docName le nom du document généré
	 * @param response la réponse HTTP
	 * @throws Exception
	 */
    public void generateWordByTemplateTableTable(String docSource,Object[] o,String docName,HttpServletResponse response)throws Exception{
		XWPFDocument doc = new XWPFDocument(OPCPackage.open(docSource));
		for(int i=0;i<o.length;i++){
			generateWordByTemplateTable(doc,o[i]);
		}
		response.setContentType("text/plain");
		response.setContentType("application/ms-word");
		response.setHeader("Content-disposition", "attachment;fileName="+docName+".docx");
		OutputStream out =response.getOutputStream();
		doc.write(out);
		out.close();
	}
	
	/**
	 * Générer un document Word à partir d'un objet unique et d'un document modèle
	 * @param doc représente le document modèle
	 * @param o un objet contenant les données à insérer dans le modèle
	 * @param docName le nom du document généré.
	 * @throws Exception
	 */
	public  void generateWordByTemplate(XWPFDocument doc,Object o,String docName) throws Exception{
		String[] listColonne=Utilitaire.getNomColonne(o);
		String[] listColonneUpper=toUpperFirst(listColonne);
		for (XWPFParagraph p : doc.getParagraphs()) {
			List<XWPFRun> runs = p.getRuns();
			if (runs != null) {
				for (XWPFRun r : runs) {
					String text = r.getText(0);
					for(int i=0;i<listColonne.length;i++){
						if (text.contains(listColonne[i])) {
							Method methodGet=this.getMethodObject(o,listColonneUpper[i],"get");
							text = text.replace(listColonne[i],(String)methodGet.invoke(o, null));
							r.setText(text);
						}
					}
				}
			}
		} 
    }


	/**
	 * Générer des documents Word sur la base d'un modèle de tableau
	 *  contenant des données alimentées de manière dynamique.
	 * @param doc représentant le document 
	 * @param o objet de mapping pour extraire les données du tableau modèle.
	 * @throws Exception
	 */
    public  void generateWordByTemplateTable(XWPFDocument doc,Object o) throws Exception{

       	String[] listColonne=Utilitaire.getNomColonne(o);
        String[] listColonneUpper=toUpperFirst(listColonne);
		for (XWPFTable tbl : doc.getTables()) {
			   for (XWPFTableRow row : tbl.getRows()) {
			      for (XWPFTableCell cell : row.getTableCells()) {
			         for (XWPFParagraph p : cell.getParagraphs()) {
			            for (XWPFRun r : p.getRuns()) {
			              String text = r.getText(0);
                                      for(int i=0;i<listColonne.length;i++){
                                           if (text.contains(listColonne[i])) {
                                                Method methodGet=this.getMethodObject(o,listColonneUpper[i],"get");
                                                text = text.replace(listColonne[i],String.valueOf(methodGet.invoke(o, null)));
                                                r.setText(text,0);
                                            }
                                      }
			             
			            }
			         }
			      }
			   }
		}
    }

	/**
	 * 	Convertir de la première lettre de chaque élément en majuscules
	 * @param list liste de chaine de caractère 
	 * @return un tableau de chainde de caractère avec tout les debuts du chaine est en majuscule
	 */
    public  String [] toUpperFirst(String[] list){
        String[] listUpper=new String[list.length];
        for(int i=0;i<list.length;i++){
            char[] separe=list[i].toCharArray();
            String up=new String(list[i].toUpperCase());
            char[] upper=up.toCharArray();
            separe[0]=upper[0];
            listUpper[i]=new String(separe);
        }
        return listUpper;
    }
    

	/**
	 * Permet à obtenir la methode convient au paramètre <strong>attribut et fonction </strong> donné 
	 * @param o Objet de mapping concernée
	 * @param attribut 
	 * @param fonction
	 * @return La methode 
	 * @throws Exception
	 */
    public  Method getMethodObject (Object o, String attribut, String fonction) throws Exception{
		try{
			Method[] listeMethods = o.getClass().getMethods();
			int nb = listeMethods.length;
			for (int a=0; a<nb; a++){
				if (listeMethods[a].getName().equals(fonction+attribut)) {
					return listeMethods[a];
				}
			}
			return null;
		}
		catch(Exception e){
			throw new Exception("Erreur dans WordUtil getMethodObject :"+e.getMessage());
		}
	}
}
