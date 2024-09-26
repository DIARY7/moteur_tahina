/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package importUtils.inputStreamTranslater.imp;

import importUtils.inputStreamTranslater.InputStreamTranslater;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * classe pour transformer l'inputstream d'un fichier excel en une liste d'HashMap
 * @author Soa
 */
public class XlsTranslater implements InputStreamTranslater{
    
    /**
     * Cette fonction consiste à transformer l'inputstream d'un fichier excel en une liste d'HashMap
     * @param inputStream : l'inputstream du fichier excel importé
     * @param fields : liste des noms des champs à importer
     * @param bool : true si le fichier a un entête sinon false
     * @return
     * @throws Exception 
     */
    @Override
    public List<HashMap<String, String>> translate(InputStream inputStream,String[] fields,boolean bool) throws Exception {
        try {
            final Workbook book = WorkbookFactory.create(inputStream);
            final Sheet sheet = book.getSheetAt(0);
            int[] numCell = this.populate(fields);
            int beginIndex = 0;
            List<String> columnValues = this.getColumnValues(sheet.getRow(0));
            if(this.isEntete(columnValues, fields) != bool){
                if(bool){
                    throw new Exception("Il n y a pas d entête");
                }
                else{
                    throw new Exception("Il y a des/un entête(s)");
                }
            }
            if(bool){
                numCell = this.getCorrespondance(columnValues, fields);
                beginIndex = 1;
            }
            int totalLigne = sheet.getLastRowNum();
            List<HashMap<String, String>> list = new ArrayList<>();
            for(int i = beginIndex;i <= totalLigne;i++){
                HashMap<String, String> hash = this.translate(sheet.getRow(i), fields, numCell);
                list.add(hash);
            }
            return list;
        }
        catch(Exception e){
             throw e;
        }
    }
    
    /**
     * Cette fonction convertit une ligne de l'inputStream en un HashMap en prenant les valeurs de chaque champ de la ligne. 
     * @param row : ligne du fichier
     * @param fields : liste des noms des champs à importer
     * @param numCell : indice des champs de la ligne par rapport aux entetes du fichier et la des noms des attributs à importer 
     * @return 
     */
    protected HashMap<String,String> translate(Row row,String[] fields,int[] numCell){
        HashMap<String, String> hash = new HashMap();
        for(int y=0;y<fields.length;y++){
            if (row.getCell(numCell[y]) != null && row.getCell(numCell[y]).getCellType() != Cell.CELL_TYPE_BLANK) {
                row.getCell(numCell[y]).setCellType(Cell.CELL_TYPE_STRING);
                hash.put(fields[y],row.getCell(numCell[y]).getStringCellValue());

            }
            else if(row.getCell(numCell[y]) != null && row.getCell(numCell[y]).getCellType() == Cell.CELL_TYPE_BLANK){
                hash.put(fields[y],"");
            }
        }
        return hash;
    }
    
    /**
     * Cette fonction consiste à donner par défaut l'indice des champs de la ligne par rapport aux entetes du fichier et la des noms des attributs à importer 
     * @param fields :liste des noms des champs à importer
     * @return 
     */
    protected int[] populate(String[] fields){
        int[] numCell = new int[fields.length];
        for(int i=0;i<fields.length;i++){
            numCell[i] = i;
        }
        return numCell;
    }
    
    /**
     * Cette fonction consiste à savoir si le fichier a un entête ou non
     * @param columnValues : valeur des colonnes de la première ligne du fichier
     * @param fields :liste des noms des champs à importer 
     * @return 
     */
    protected boolean isEntete(List<String> columnValues,String[] fields){

        for (String field : fields) {
            int check = columnValues.indexOf(field.toLowerCase());
            if(check!=-1){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Cette fonction consiste à trouver l'indice des champs de la ligne par rapport aux entetes du fichier et la des noms des attributs à importer 
     * @param columnValues: valeur des colonnes de la première ligne du fichier
     * @param fields:liste des noms des champs à importer e
     * @return
     * @throws Exception 
     */
    protected int[] getCorrespondance(List<String> columnValues,String[] fields) throws Exception{
        int[] numCell = new int[fields.length];
        for(int i=0;i<fields.length;i++){
            numCell[i] = columnValues.indexOf(fields[i].toLowerCase());
            if(numCell[i]<0){
                throw new Exception("La colonne "+fields[i]+" n existe pas dans l excel");
            }
        }
        return numCell;
    }
    
    /**
     * Cette fonction consiste à avoir toutes les valeurs dans chaque colonne de la ligne
     * @param row : une ligne du fichier
     * @return 
     */
    private  List<String> getColumnValues(Row row){
        List<String> list=new ArrayList();
        int column = 0;
        while(row != null && row.getCell(column) != null && row.getCell(column).getCellType() != Cell.CELL_TYPE_BLANK){
            row.getCell(column).setCellType(Cell.CELL_TYPE_STRING);
            list.add(row.getCell(column).getStringCellValue().toLowerCase());
            column++;
        }
        return list;
    }
}
