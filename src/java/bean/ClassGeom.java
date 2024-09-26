/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;
import org.postgis.PGgeometry;
/**
 * Cette classe  qui représente un objet avec un attribut géometrique.
 * <p>Par exemple, on veut enregistrer des coordonnées spatiales des points de vente</p>
 * <pre>
 *     public class PointDeVente extends ClassGeom{
 *     public void controlerUpdate(Connection c) {
 *         //Logique de controle pour les mises à jour
 *         super.controlerUpdate(c);
 *     }
 *     public void controler(Connection c) {
 *            //Logique de controle pour insert
 *            super.controler(c); 
 *     }
 *      public ClassMapTable updateObject(Connection c) throws Exception{
 *          //logique de mise à jour supplémentaire
 *          super.updateObject(c);
 *      }
 *      public ClassMapTable createObject(Connection c) throws Exception{
 *          //logique supplémentaire de création
 *          super.createObject(c);
 *      }
 *     }
 *  
 * </pre>
 * <p>Comme on peut voir, ça reste une {@link bean.ClassMAPTable} mais avec un attribut géometrique en plus.</p>
 * @author BICI
 */
public abstract class ClassGeom extends ClassEtat{

    private org.postgis.PGgeometry geom;

    /**
     * Renvoie le champs geom 
     * @return geom qui represente un objet geométrique
     */
    public PGgeometry getGeom() {
        return geom;
    }

    public void setGeom(PGgeometry geom) {
        this.geom = geom;
    }
   
   
    
}
