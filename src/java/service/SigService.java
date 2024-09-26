/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.CGenUtil;
import bean.ClassGeom;
import bean.ClassMAPTable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import org.postgis.*;
/**
 * Cette classe contient des fonctions pour manipuler des objets géométriques: 
 * <ul>
 *  <li>Extraire les points à partir des objets géometriques</li>
 *  <li>Trouver les objets dans les rayons de deux objets</li>
 *  <li>Trouver le type de forme de l'objet géometrique</li>
 * </ul>
 * 
 * @author BICI
 */
public class SigService {
   
    /**
     * Permet d'instancier une classe
     * @param className nom de la classe
     * @return instance d'objet
     * @throws Exception
     */
    public Object avoirInstance(String className) throws Exception{
        Object o=null;
        try{
            Class<?> clazz = Class.forName(className);
            Constructor<?> ctor = clazz.getConstructor(null);
            o = ctor.newInstance(null);
        }catch(Exception e){
            throw e;
        }
        return o;
    }

    /**
     * Revoie tous les attribut de la ClassMAPTable c 
     * @param c objet de mapping representant une table/view dans une base de données
     * @return tableau des attribut de la classe 
     * @throws Exception
     */
    public String[] getFieldName(ClassMAPTable c) throws Exception{
        String[] val=null;
        try{
            Field[] liste=c.getFieldList();
            if(liste!=null){
                val=new String[liste.length];
                for(int i=0;i<liste.length;i++){
                    val[i]=liste[i].getName();
                }
            }
        }catch(Exception e){
            throw e;
        }
        return val;
    }

    /**
     * 
     * @param functionName
     * @param couche1
     * @param couche2
     * @return
     * @throws Exception
     */
    public ClassGeom[] callfunction(String functionName, String couche1, String couche2) throws Exception{
        ClassGeom[] liste=null;
        try{
            Class<?> base = Class.forName("bean.ClassGeom");
            
            Class<?> clazz = Class.forName(couche1);
            Constructor<?> ctor = clazz.getConstructor(null);
            Object object = ctor.newInstance(null);
        
            Class<?> clazz1 = Class.forName(couche2);
            Constructor<?> ctor1 = clazz1.getConstructor(null);
            Object object1 = ctor1.newInstance(null);
            
            Class<?> clazz2 = Class.forName("java.lang.String");
            
            Class[] cArg = new Class[3];
            cArg[0]=base;
            cArg[1]=base;
            cArg[2]=clazz2;
            
            String aw="";
            Object[] liste11={object,object1,aw};
            if(functionName.compareTo("around")==0){
                liste11=new Object[4];
                liste11[0]=object;
                liste11[1]=object1;
                liste11[2]="1";
                liste11[3]=aw;
                
                cArg = new Class[4];
                cArg[0]=base;
                cArg[1]=base;
                cArg[2]=clazz2;
                cArg[3]=Class.forName("java.lang.String");
            }
            liste=(ClassGeom[])this.getClass().getMethod(functionName,cArg).invoke(this, liste11);
        }catch(Exception e){
            throw e;
        }
        return liste;
    }

    /**
     * Création d'une liste de type HashMap<String,String>
     * @return une liste de type  HashMap<String,String>
     */
    public HashMap<String,String> getFonction(){
        HashMap<String,String> liste=new HashMap<String,String>();
        liste.put("Autour de","around");
        liste.put("Est disjoint de","isDisjoint");
        liste.put("Intersecte","intersect");
        liste.put("Contient","contains");
        liste.put("Coupe","cut");
        liste.put("Touche","touch");
        return liste;
    }

    /**
     * Trouver toutes les instances de la classe "surround" 
     * qui se trouvent à une certaine distance d'une instance de la classe "center"
     * @param surround objet géometrique
     * @param center objet géometrique
     * @param distance c'est la distance  entre les géométries des instances surround et center
     * @param aw critère apres where
     * @param request
     * @return tableau de ClassGeom
     * @throws Exception
     */
    public ClassGeom[] around(ClassGeom surround, ClassGeom center, String distance,String aw,javax.servlet.http.HttpServletRequest request) throws Exception{
        ClassGeom[] liste=null;
        try{
            String wr="";
            Field[] critere = surround.getFieldList();
            for(int i=0;i<critere.length;i++){
                Object val =surround.getValField(critere[i]);
                if(val!=null && request.getParameter(critere[i].getName())!=null){
                    if(val instanceof Integer || val instanceof Double)
                        wr += " and b."+critere[i].getName()+" = '"+val+"'";
                    else
                        wr += " and b."+critere[i].getName()+" like '%"+val+"%'";
                }
            }
            String requete="SELECT a.*,ST_Distance(a.geom, b.geom)\n" +
                    "FROM "+surround.getNomTable()+" a\n" +
                    "LEFT JOIN "+center.getNomTable()+" b\n" +
                    "ON ST_DWithin(a.geom, b.geom, "+distance+")\n" +
                    "where ST_Distance(a.geom, b.geom)>0 "+wr+" "+aw;
            System.out.println(requete);
            liste=(ClassGeom[])CGenUtil.rechercher(surround, requete);
        }catch(Exception e){
            throw e;
        }
        return liste;
    }

    /**
     * trouver toutes les instances de la classe "surround"
     * qui croisent une instance de la classe "center" 
     * @param surround objet géometrique
     * @param center objet géometrique
     * @param aw critère apres where
     * @return tableau de ClassGeom
     * @throws Exception
     */
    public ClassGeom[] intersect(ClassGeom surround, ClassGeom center,String aw) throws Exception{
        ClassGeom[] liste=null;
        try{
            String requete="SELECT a.*,ST_Intersects(a.geom, b.geom)\n" +
            "FROM "+surround.getNomTable()+" a\n" +
            "LEFT JOIN "+center.getNomTable()+" b\n" +
            "ON ST_Intersects(a.geom, b.geom)\n" +
            "where ST_Intersects(a.geom, b.geom) is not null "+aw;
            System.out.println("requete="+requete);
            liste=(ClassGeom[])CGenUtil.rechercher(surround, requete);
        }catch(Exception e){
            throw e;
        }
        return liste;
    }

    /**
     * Trouver toutes les instances de la classe "surround"
     *  qui sont disjointes d'une instance de la classe "center"
     * @param surround objet géometrique
     * @param center objet géometrique
     * @param aw critère apres where
     * @return tableau de ClassGeom
     * @throws Exception
     */
    public ClassGeom[] isDisjoint(ClassGeom surround, ClassGeom center, String aw) throws Exception{
        ClassGeom[] liste=null;
        try{
            String requete="SELECT a.*,ST_Disjoint(a.geom, b.geom)\n" +
            "FROM "+surround.getNomTable()+" a\n" +
            "LEFT JOIN "+center.getNomTable()+" b\n" +
            "ON ST_Disjoint(a.geom, b.geom)\n" +
            "where ST_Disjoint(a.geom, b.geom) is not null "+aw;
            liste=(ClassGeom[])CGenUtil.rechercher(surround, requete);
            System.out.println(requete);
        }catch(Exception e){
            throw e;
        }
        return liste;
    }
    /**
     * Trouver toutes les instances de la classe "surround" qui contiennent une instance de la classe "center".
     * @param surround objet géometrique
     * @param center objet géometrique
     * @param aw critère apres where
     * @return tableau de ClassGeom
     * @throws Exception
     */
    public ClassGeom[] contains(ClassGeom surround, ClassGeom center,String aw) throws Exception{
        ClassGeom[] liste=null;
        try{
            String requete="SELECT a.*,ST_Contains(a.geom, b.geom)\n" +
            "FROM "+surround.getNomTable()+" a\n" +
            "LEFT JOIN "+center.getNomTable()+" b\n" +
            "ON ST_Contains(a.geom, b.geom)\n" +
            "where ST_Contains(a.geom, b.geom) is not null "+aw;
            System.out.println(requete);
            liste=(ClassGeom[])CGenUtil.rechercher(surround, requete);
        }catch(Exception e){
            throw e;
        }
        return liste;
    }

    /**
     * Trouver toutes les instances de la classe "surround" qui croisent une instance de la classe "center".
     * @param surround objet géometrique
     * @param surround objet géometrique
     * @param center objet géometrique
     * @param aw critère apres where
     * @return tableau de ClassGeom
     * @throws Exception
     */
    public ClassGeom[] cut(ClassGeom surround, ClassGeom center,String aw) throws Exception{
        ClassGeom[] liste=null;
        try{
            String requete="SELECT a.*,ST_Crosses(a.geom, b.geom)\n" +
            "FROM "+surround.getNomTable()+" a\n" +
            "LEFT JOIN "+center.getNomTable()+" b\n" +
            "ON ST_Crosses(a.geom, b.geom)\n" +
            "where ST_Crosses(a.geom, b.geom) is not null "+aw;
            System.out.println(requete);
            liste=(ClassGeom[])CGenUtil.rechercher(surround, requete);
        }catch(Exception e){
            throw e;
        }
        return liste;
    }

    /**
     * Trouver toutes les instances de la classe "surround" qui touchent une instance de la classe "center". 
     * @param surround objet géometrique
     * @param center objet géometrique
     * @param aw critère apres where
     * @return tableau de ClassGeom
     * @throws Exception
     */
    public ClassGeom[] touch(ClassGeom surround, ClassGeom center,String aw) throws Exception{
        ClassGeom[] liste=null;
        try{
            String requete="SELECT a.*,ST_Touches(a.geom, b.geom)\n" +
            "FROM "+surround.getNomTable()+" a\n" +
            "LEFT JOIN "+center.getNomTable()+" b\n" +
            "ON ST_Touches(a.geom, b.geom)\n" +
            "where ST_Touches(a.geom, b.geom) is not null "+aw;
            System.out.println(requete);
            liste=(ClassGeom[])CGenUtil.rechercher(surround, requete);
        }catch(Exception e){
            throw e;
        }
        return liste;
    }
    
    /**
     * Savoir la forme geometrique de objet géometrique
     * @param geometry objet géometrique
     * @return une nombre de type int
     * @throws Exception
     */
    public int getTypeGeom(ClassGeom geometry) throws Exception{
        int val=0;
        try{
            if(CGenUtil.getValeurFieldByMethod(geometry, "geom")!=null){
                String value=CGenUtil.getValeurFieldByMethod(geometry, "geom").toString();
                if(value.contains("POINT")){
                    return 1;
                } if(value.contains("POLYGON")){
                    return 2;
                } if(value.contains("LINESTRING")){
                    return 3;
                } if(value.contains("CIRCLE")){
                    return 4;
                }
            }
        }catch(Exception e){
            throw e;
        }
        return val;
    }

    /**
     * Déterminer le type de géométrie en appelant la méthode 
     * <pre>getTypeGeom(ClassGeom geometry)</pre>
     * @param geometry objet géometrique
     * @return une valeur entière correspondant à un type de géométrie spécifique,
     *  tel qu'un point (1), un polygone (2) ou une chaîne de lignes (3).
     * @throws Exception
     */
    public Point[] getAllPoints(ClassGeom geometry) throws Exception{
        Point[] liste=null;
        try{
            int test=getTypeGeom(geometry);
            switch (test) {
                case 1:
                    liste=new Point[1];
                    liste[0]=new Point(CGenUtil.getValeurFieldByMethod(geometry, "geom").toString());
                    break;
                case 2:
                    Polygon p=new Polygon(CGenUtil.getValeurFieldByMethod(geometry, "geom").toString());
                    liste=p.getRing(0).getPoints() ;
                    break;
                case 3:
                    LineString l=new LineString(CGenUtil.getValeurFieldByMethod(geometry, "geom").toString());
                    liste=l.getPoints() ;
                    break;
            }
        }catch(Exception e){
            throw e;
        }
        return liste;
    }
}
