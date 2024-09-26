/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package affichage;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import utilitaire.Utilitaire;

/**
 *
 * @author nyamp
 */
public class ObjectChart {
    String x;
    double y;
    String key;

    public ObjectChart(String x, String y, String key) throws ParseException {
        this.setX(x);
        this.setY(y);
        this.setKey(key);
        
    }
    
    public String getDataToString(){
        return String.format("%s;%s;%s;",Utilitaire.champNull(x),y,Utilitaire.champNull(key));
    }
    
    public ObjectChart(String  x, String y) throws ParseException {
        this.setX(x);
        this.setY(y);
    }

    public ObjectChart(String  x, double[]liste) {
        this.setX(x);
    }

    public ObjectChart() {
    }
    
    public String getX() {
        return x;
    }

    public void setY(String y) throws ParseException {
        if(y!=null){
            //this.y = Utilitaire.stringToDouble(y); //TALOA
            NumberFormat nf = NumberFormat.getInstance(Locale.FRENCH); //AMZAO
            this.y = nf.parse(y).doubleValue(); //AMZAO
        }
            
    }

    public void setY(double Y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public void setX(String x) {
        this.x = x;
    }
    
    public static List<ObjectChart> getListe(String[][] data,int x,int y) throws ParseException{
        int taille=0;
        if(data!=null){
            taille=data.length;
        }   
        List<ObjectChart> liste=new ArrayList<ObjectChart>(taille) ;
        for(int i=0;i<taille;i++){
            liste.add(new ObjectChart(data[i][x],data[i][y]));
        }
        return liste;
    }
    
    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
}
