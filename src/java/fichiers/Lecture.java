package fichiers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Cette classe sert à lire une fichier
 * @author BICI
 * 
 */
public class Lecture {
    private static FileInputStream inputStream=null;
    private static InputStreamReader lecteur =null;
    private static BufferedReader buffer =null;
    

    /**
     * Sert à lire une fichier 
     * @param cheminFichier chemin là ou se trouve le fichier
     * @return le fichier 
     * @throws Exception
     */
    public static ArrayList<String> lireFichier(String cheminFichier) throws Exception{
        ArrayList<String> liste=null;
        String ligne=null;
        try{
            if(cheminFichier==null || cheminFichier.compareTo("")==0){
                throw new Exception("Chemin invalide");
            }
            liste=new ArrayList<String>();
            inputStream = new FileInputStream (cheminFichier);
            lecteur= new InputStreamReader(inputStream, "ISO-8859-1"); 
            buffer = new BufferedReader (lecteur, 8192);
            
            while((ligne = buffer.readLine()) != null){
                liste.add(ligne);
            }
            return liste;
        }catch(Exception e){
            throw e;
        }finally{ 
            if(buffer!=null) buffer.close();
            if(lecteur!=null) lecteur.close();
            if(inputStream!=null)inputStream.close();
        }
    }
}