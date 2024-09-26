/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitaire;
import bean.CGenUtil;
import bean.ClassMAPTable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import reporting.ReportingCdn;

public class UtilExcel {
    static XSSFRow row;
    static java.util.Properties prop = configuration.CynthiaConf.load();
    static String chemin = prop.getProperty("cdnPDF")+prop.getProperty("cdnDN")+"/";

    /**
     * Sert à savoir si le nom de fichier existe dejà ou pas 
     * @param fileName nom du fichier
     * @return
     */
    public static boolean checkExist(String fileName) {
        File f = new File(chemin+fileName);
        
        if(f.exists()) return true;
        return false;
    }

    /**
     * Sert à compter la ligne de l'excel à créer
     * @param file le nom du fichier
     * @return le nombre de ligne de l'excel
     * 
     */
    public int countRowExcel(String file) throws FileNotFoundException, IOException{
        FileInputStream fis = new FileInputStream(
        new File(chemin+file));
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet spreadsheet = workbook.getSheetAt(0);
        Iterator < Row > rowIterator = spreadsheet.iterator();
        int a=0;
        while (rowIterator.hasNext()) 
        {
            row = (XSSFRow) rowIterator.next();
            a++;
        }
        fis.close();
        return a;
    }

    /**
     * Sert à créer le fichier excel 
     * @param nomfichier
     * @param nomfeuille
     * @param entete
     * @param fonctionnalite
     * @param donnee
     * @throws Exception
     */
    public static void createExcel(String nomfichier, String nomfeuille, String[] entete, ReportingCdn.Fonctionnalite fonctionnalite,List donnee) throws Exception{
        try{
            XSSFWorkbook workbook = new XSSFWorkbook(); 
            XSSFSheet spreadsheet = workbook.createSheet(nomfeuille);
            FileOutputStream out = new FileOutputStream(new File(ReportingCdn.cheminReport(fonctionnalite)+nomfichier));
            spreadsheet = workbook.getSheetAt(0);
            int rowid = 0;
            row = spreadsheet.createRow(rowid++);
            int cellid = 0;
            int a=0;
            while(a<entete.length){
                Cell cell = row.createCell(cellid++);
                cell.setCellValue(entete[a]);
                a++;
            }
            for(int i=0;i<donnee.size();i++){
                Field[] fields = donnee.get(i).getClass().getDeclaredFields();
                cellid = 0;
                row = spreadsheet.createRow(rowid++);
                for(int e=0;e<fields.length;e++){
                    Object o = CGenUtil.getValeurFieldByMethod((ClassMAPTable) donnee.get(i), fields[e]);
                    Cell cell = row.createCell(cellid++);
                    if(fields[e].getType().getName().equalsIgnoreCase("java.lang.String")) cell.setCellValue((String) o);
                    if(fields[e].getType().getName().equalsIgnoreCase("java.sql.Date")) if(o!=null) cell.setCellValue((java.util.Date) o);
                    if(fields[e].getType().getName().equalsIgnoreCase("int")) cell.setCellValue((int) o);
                    if(fields[e].getType().getName().equalsIgnoreCase("double")) cell.setCellValue((double) o);
                }
            }
              workbook.write(out);
        }
        catch(FileNotFoundException e){
           e.printStackTrace();
           throw e;
        }
    }
    public static void createexcel(String nomfichier, String nomfeuille, String[] entete, List donnee) throws Exception{
        createExcel(nomfichier, nomfeuille, entete, ReportingCdn.Fonctionnalite.DN, donnee);
    }

    /**
     * Effacer l'emplacement du fichier
     * @param path 
     * @return boolean
     */
    public boolean deleteDirectory(File path){
        boolean resultat=true;
        if(path.exists()){
            File[] files=path.listFiles();
            for(int i=0;i<files.length;i++){
                if(files[i].isDirectory()){
                    resultat &= deleteDirectory(files[i]);
                }
                else{
                    resultat &=files[i].delete();
                }
            }
        }
        return resultat;
    }

    /**
     * Ajouter des lignes sur Excel 
     * @param fichier nom du fichier
     * @param rowtoadd ligne à ajouter
     * @throws Exception
     */
    public void addrowonexcel(String fichier, XSSFRow rowtoadd) throws Exception{
        try{
            File file = new File(chemin+fichier);
            FileInputStream fIP = new FileInputStream(file);
            //Get the workbook instance for XLSX file 
            XSSFWorkbook workbook = new XSSFWorkbook(fIP);
            //Create a blank sheet
            XSSFSheet spreadsheet = workbook.getSheetAt(0);
            Iterator < Row > rowIterator = spreadsheet.iterator();
            int id=0;
            while (rowIterator.hasNext()){
                row = (XSSFRow) rowIterator.next();
                id++;
            }
            row = spreadsheet.createRow(id);
            Iterator < Cell > cellIterator = rowtoadd.cellIterator();
            int cellid = 0;
            while (cellIterator.hasNext()){
                Cell cell = cellIterator.next();
                Cell cell2 = row.createCell(cellid++);
                 switch (cell.getCellType()) 
                {
                   case Cell.CELL_TYPE_NUMERIC:
                   cell2.setCellValue(cell.getNumericCellValue());
                   break;
                   case Cell.CELL_TYPE_STRING:
                   cell2.setCellValue(cell.getStringCellValue());
                   break;
                }
            }
            FileOutputStream out = new FileOutputStream(new File(chemin+fichier));
            workbook.write(out);
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
            throw e;
        }
    }
}
