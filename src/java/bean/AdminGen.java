
package bean;

import utilitaire.Utilitaire;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Utilitaire pour interagir avec les objets de mapping
 * localement.
 * 
 * 
 * @author BICI
 * @version 1.0
 */
public class AdminGen {

    /**
     * Pour un attribut donné, prendre les valeurs distincts de ce dernier
     * @param liste : liste des objets de mapping à extraire les valeurs
     * @param attr : attribut concerné
     * @return Liste de valeur distinct de l'attribut pour une liste
     * @throws Exception
     */
    public static String[] getDistinct(ClassMAPTable[] liste,String attr)throws Exception
    {
        List<String> ls=new ArrayList<String>();
        for(ClassMAPTable c:liste)
        {
            String valeur=(String)CGenUtil.getValeurFieldByMethod(c, attr);
            if(ls.contains(valeur)==false)ls.add(valeur);
        }
        return ls.toArray(new String[ls.size()]);
    }
    /**
     * @deprecated à ne pas utiliser cela va faire un null pointer exception
     * @param aCopier 
     * @return
     * @throws Exception
     */
    public static ClassMAPTable[] copier(ClassMAPTable[] aCopier) throws Exception {
        ClassMAPTable[] retour = new ClassMAPTable[aCopier.length];
        for (int i = 0; i < aCopier.length; i++) {
            //retour[i]=(ClassMAPTable)aCopier.clone();
            retour[i].genererPKInterne();
        }
        return retour;
    }
    /**
     * @deprecated à ne pas utiliser cela va créer des connexions pour chaque objet
     * @param aCopier
     * @throws Exception
     */
    public static void copierToTable(ClassMAPTable[] aCopier) throws Exception {
        for (int i = 0; i < aCopier.length; i++) {
            aCopier[i].genererPKInterne();
            aCopier[i].insertToTableWithHisto("6");
        }
    }
    /**
     * @deprecated à ne pas utiliser cela va créer des connexions pour chaque objet
     * @param a
     * @throws Exception 
     */
    public static void insertGroupe(ClassMAPTable[] a) throws Exception {
        for (int i = 0; i < a.length; i++) {
            a[i].insertToTableWithHisto("6");
        }

    }
    /**
     * @deprecated à ne pas utiliser cela va créer des connexions pour chaque objet
     * @param a
     * @throws Exception
     */
    public static void updateGroupe(ClassMAPTable[] a) throws Exception {
        for (int i = 0; i < a.length; i++) {
            a[i].insertToTableWithHisto("6");
        }

    }

    public AdminGen() {
    }

    /**
     * Filtrer liste d'objets selon valeurs d'attributs
     * @param liste liste d'objets
     * @param attributET liste d'attributs de critère 
     * @param valET liste des valeurs des attributs de critères
     * @return null si liste d'objet d'entrée vide, sinon liste des objets correspondants au critère
     * @throws Exception
     */

    public static ClassMAPTable[] findInList(ClassMAPTable[] liste, String[] attributET, String[] valET) throws Exception {
        ClassMAPTable[] retour = null;
        if (liste.length == 0) {
            return null;
        }
        Vector temp = new Vector();
        for (int i = 0; i < liste.length; i++) {
            Object valeurListe[] = new Object[attributET.length];
            int nbMitovy = 0;
            for (int iAtr = 0; iAtr < attributET.length; iAtr++) {
                valeurListe[iAtr] = CGenUtil.getValeurFieldByMethod(liste[i], attributET[iAtr]);
                if (valeurListe[iAtr].toString().compareTo(valET[iAtr]) == 0) {
                    nbMitovy++;
                } else {
                    break;
                }
            }
            if (nbMitovy == attributET.length) {
                temp.add(liste[i]);
            }
        }
        retour = (ClassMAPTable[]) java.lang.reflect.Array.newInstance(liste[0].getClass().newInstance().getClass(), temp.size());
        temp.copyInto(retour);
        return retour;
    }
    /**
     * Filtrer liste d'objets selon valeurs d'attributs
     * @param liste liste d'objets
     * @param attributET liste d'attributs de critère 
     * @param valET liste des valeurs des attributs de critères
     * @return null si liste d'objet d'entrée vide, sinon liste des objets correspondants au critère
     * @throws Exception
     */
    public static ClassMAPTable[] findAvecOrder(ClassMAPTable[] liste, String[] attributET, String[] valET) throws Exception {
        ClassMAPTable[] retour = null;
        if (liste.length == 0) {
            return null;
        }
        Vector temp = new Vector();
        for (int i = 0; i < liste.length; i++) {
            Object valeurListe[] = new Object[attributET.length];
            int nbMitovy = 0;
            for (int iAtr = 0; iAtr < attributET.length; iAtr++) {
                valeurListe[iAtr] = CGenUtil.getValeurFieldByMethod(liste[i], attributET[iAtr]);
                //System.out.println("Comparaison = "+valeurListe[iAtr]+" >> "+valET[iAtr]+" >> Colonne = "+attributET[iAtr] +"==");
                if (valeurListe[iAtr] != null && valET[iAtr]!=null && valeurListe[iAtr].toString().compareTo(valET[iAtr]) == 0) {
                    nbMitovy++;
                } else {
                    break;
                }
            }
            if (nbMitovy == attributET.length) {
                temp.add(liste[i]);
            } else if (temp.size() > 0) {
                break;
            }
        }

        retour = (ClassMAPTable[]) java.lang.reflect.Array.newInstance(liste[0].getClass().newInstance().getClass(), temp.size());
        temp.copyInto(retour);
        return retour;
    }
    /**
     * Filtrer liste d'objets selon valeurs d'attributs avec retour unique
     * @param liste liste d'objets
     * @param attributET liste d'attributs de critère 
     * @param valET liste des valeurs des attributs de critères
     * @return null si liste d'objet d'entrée vide, premier objet de la liste correspondant au critère
     * @throws Exception
     */
    public static ClassMAPTable findUnique(ClassMAPTable[] liste, String[] attributET, String[] valET) throws Exception {

        for (int i = 0; i < liste.length; i++) {
            Object valeurListe[] = new Object[attributET.length];
            int nbMitovy = 0;
            for (int iAtr = 0; iAtr < attributET.length; iAtr++) {
                valeurListe[iAtr] = CGenUtil.getValeurFieldByMethod(liste[i], attributET[iAtr]);
                if(valeurListe[iAtr]==null) continue;
                if (valeurListe[iAtr].toString().compareTo(valET[iAtr]) == 0) {
                    nbMitovy++;
                } else {
                    break;
                }
            }
            if (nbMitovy == attributET.length) {
                return liste[i];
            }
        }
        return null;
    }
    /**
     * Sommer les valeurs de certains attributs sur les résultats de filtre donné
     * @param liste liste d'objets
     * @param attributET liste d'attributs de critère 
     * @param valET liste des valeurs des attributs de critères
     * @param asommer liste des attributs à sommer
     * @return liste des valeurs sommés après filtre
     * @throws Exception
     */
    public static double[] findAvecSommeOrder(ClassMAPTable[] liste, String[] attributET, String[] valET, String[] asommer) throws Exception {
        ClassMAPTable[] retour = null;
        if (liste.length == 0) {
            return null;
        }
        Vector temp = new Vector();
        double[] valiny = new double[asommer.length];
        for (int i = 0; i < liste.length; i++) {
            Object valeurListe[] = new Object[attributET.length];
            int nbMitovy = 0;
            for (int iAtr = 0; iAtr < attributET.length; iAtr++) {
                valeurListe[iAtr] = CGenUtil.getValeurFieldByMethod(liste[i], attributET[iAtr]);
                if (valeurListe[iAtr] != null && valeurListe[iAtr].toString() != null && valeurListe[iAtr].toString().compareTo(valET[iAtr]) == 0) {
                    nbMitovy++;
                } else {
                    break;
                }
            }
            if (nbMitovy == attributET.length) {
                for (int isom = 0; isom < asommer.length; isom++) {
                    valiny[isom] += new Double(CGenUtil.getValeurFieldByMethod(liste[i], asommer[isom]).toString()).doubleValue();
                }
                temp.add(liste[i]);
            } else if (temp.size() > 0) {
                break;
            }
        }

        return valiny;
    }
    /**
     * Filtrer liste d'objets selon valeurs d'attributs
     * @param liste liste d'objets
     * @param attributET liste d'attributs de critère 
     * @param valET liste des valeurs des attributs de critères
     * @return null si liste d'objet d'entrée vide, sinon liste des objets correspondants au critère
     * @throws Exception
     */
    public static ClassMAPTable[] find(ClassMAPTable[] liste, String[] attributET, String[] valET) throws Exception {
        ClassMAPTable[] retour = null;
        Vector temp = new Vector();
        for (int i = 0; i < liste.length; i++) {
            Object valeurListe[] = new Object[attributET.length];
            int nbMitovy = 0;
            for (int iAtr = 0; iAtr < attributET.length; iAtr++) {
                valeurListe[iAtr] = CGenUtil.getValeurFieldByMethod(liste[i], attributET[iAtr]);
                if(valeurListe[iAtr]==null) continue;
                if (valeurListe[iAtr].toString().compareTo(valET[iAtr]) == 0) {
                    nbMitovy++;
                } else {
                    break;
                }
            }
            if (nbMitovy == attributET.length) {
                temp.add(liste[i]);
            }
        }
        retour = new ClassMAPTable[temp.size()];
        temp.copyInto(retour);
        return retour;
    }
/**
 * Construire des resultat groupé de somme selon indexs de champs
 * @param e liste des objets
 * @param numColGroupe : index du champs de groupe
 * @param numColSomme : index du champs de somme
 * @return liste de resultats groupés
 */
    public static ResultatGroupe[] findGroupe(ClassMAPTable[] e, int numColGroupe, int numColSomme) {
        ResultatGroupe[] rg = null;
        Vector rTemp = new Vector();
        if (e.length > 0) //s il y a element
        {
            Object o = e[0].getValeur(numColSomme);
            Double montD = (Double) o;
            double mont = montD.doubleValue();
            rTemp.add(new ResultatGroupe((String) e[0].getValeur(numColGroupe), mont, 1));
        }
        for (int i = 1; i < e.length; i++) {
            ResultatGroupe et = (ResultatGroupe) utilitaire.Utilitaire.extraire(rTemp, 0, (String) e[i].getValeur(numColGroupe));
            if (et == null) {
                Object o = e[i].getValeur(numColSomme);
                Double montD = (Double) o;
                double mont = montD.doubleValue();
                rTemp.add(new ResultatGroupe((String) e[i].getValeur(numColGroupe), mont, 1));
            } else {
                Object o = e[i].getValeur(numColSomme);
                Double montD = (Double) o;
                double mont = montD.doubleValue();
                et.setSomme(et.getSomme() + mont);
                et.setNombre(et.getNombre() + 1);
            }
        }
        rg = new ResultatGroupe[rTemp.size()];
        rTemp.copyInto(rg);
        return rg;
    }
    /**
     * @deprecated aucune implémentation
     * @param e
     * @param numColGroupe
     * @param numColSomme
     * @return toujours vrai
     */
    public static boolean testDedansIgnoreCase(ClassMAPTable[] e, int[] numColGroupe, int[] numColSomme) {
        return true;
    }
    /**
     * Construire des resultat groupé de sommes multiples et de champs multiples selon indexs de champs
     * @param e : liste d'objet à extraire les valeurs
     * @param numColGroupe : liste des index de valeur de groupage
     * @param numColSomme : liste des index de valeur de sommes
     * @return liste de resultat groupés avec sommes multiples et libellés multiples
     */
    public static RgMultiple[] findGroupeMultiple(ClassMAPTable[] e, int[] numColGroupe, int[] numColSomme) {
        RgMultiple[] rg = null;
        Vector rTemp = new Vector();
        try {
            if (e.length > 0) //s il y a element
            {
                double montD[] = new double[numColSomme.length];
                for (int i = 0; i < numColSomme.length; i++) {
                    Object o = e[0].getValeur(numColSomme[i]);
                    Double temp = (Double) o;
                    montD[i] = temp.doubleValue();
                }
                String colListe[] = new String[numColGroupe.length];
                for (int i = 0; i < numColGroupe.length; i++) {
                    colListe[i] = (String) e[0].getValeur(numColGroupe[i]);
                }
                rTemp.add(new RgMultiple(colListe, montD, 1));
            }
            for (int i = 1; i < e.length; i++) {
                String colVal[] = new String[numColGroupe.length];
                for (int k = 0; k < numColGroupe.length; k++) {
                    colVal[k] = (String) e[i].getValeur(numColGroupe[k]);
                }
                RgMultiple et = (RgMultiple) Utilitaire.extraireMultiple(rTemp, 0, numColGroupe, colVal);
                if (et == null) {
                    double montD[] = new double[numColSomme.length];
                    for (int ik = 0; ik < numColSomme.length; ik++) {
                        Object o = e[i].getValeur(numColSomme[ik]);
                        Double temp = (Double) o;
                        montD[ik] = temp.doubleValue();
                    }
                    String colListe[] = new String[numColGroupe.length];
                    for (int ij = 0; ij < numColGroupe.length; ij++) {
                        colListe[ij] = (String) e[i].getValeur(numColGroupe[ij]);
                    }
                    rTemp.add(new RgMultiple(colListe, montD, 1));
                } else {
                    double montD[] = new double[numColSomme.length];
                    for (int ik = 0; ik < numColSomme.length; ik++) {
                        Object o = e[i].getValeur(numColSomme[ik]);
                        Double temp = (Double) o;
                        montD[ik] = temp.doubleValue() + et.getSommeGroupe()[ik];
                    }
                    et.setSommeGroupe(montD);
                    et.setNombre(et.getNombre() + 1);
                }
            }
            rg = new RgMultiple[rTemp.size()];
            rTemp.copyInto(rg);
            return rg;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Sommer la valeur d'un attribut d'une liste d'objet
     * @param c : liste d'objets
     * @param indice : index de la colonne à sommer
     * @return valeur de la somme du champ
     */
    public static double calculSommeDouble(ClassMAPTable c[], int indice) {
        double somme = 0.0F;
        double f3;
        try {
            for (int i = 0; i < c.length; i++) {
                Object o = c[i].getValeur(indice);
                somme += ((Number) o).doubleValue();
            }
            double f = somme;
            return f;
        } catch (Exception ex) {
            ex.printStackTrace();
            double f1 = 0.0F;
            return f1;
        }
    }
    /**
     * Sommer la valeur d'un attribut d'une liste d'objet
     * @param c : liste d'objets
     * @param nomCol : nom de la colonne à sommer
     * @return valeur de la somme du champ
     */
    public static double calculSommeDouble(ClassMAPTable c[], String nomCol)
    {
        double somme = 0.0F;
        double f3;
        try
        {
          //System.out.println("Nombre de c= "+c.length);
            for(int i = 0; i < c.length; i++)
            {
              //System.out.println("AVANT    tour="+i+" valeur=");
              Object o=bean.CGenUtil.getValeurFieldByMethod(c[i],nomCol);
              //System.out.println("tour="+i+" valeur="+o);

              somme+=((Number)o).doubleValue();
            }
            double f = somme;
            return f;
        }
        catch(Exception ex)
        {
          ex.printStackTrace();
            double f1 = 0.0F;
            return f1;
        }
      }
    /**
     * Sommer les valeurs d' attributs d'une liste d'objet
     * @param c : liste d'objets
     * @param indice : liste d'index des colonnes à sommer
     * @return liste des valeurs sommées
     */
    public static double[] calculSommeDouble(ClassMAPTable c[], String[] indice) {
        double somme[] = new double[indice.length];
        try {
            for (int i = 0; i < c.length; i++) {
                for(int j = 0; j < indice.length; j++){
                    //Object o = c[i].getValeur(indice[j]);
                    Object o = CGenUtil.getValeurFieldByMethod(c[i], indice[j]);
                    somme[j] += ((Number) o).doubleValue();
                }
            }
            return somme;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Sommer et formatter la valeur d'un attribut d'une liste d'objet
     * @param c : liste d'objets
     * @param indice : index de la colonne à sommer
     * @return valeur formattée (standard monnaie) de la somme du champ
     */
    public static String calculSommeDoubleFormatte(ClassMAPTable c[], int indice) {
        return utilitaire.Utilitaire.formaterAr(calculSommeDouble(c, indice));
    }
    /**
     * Sommer la valeur d'un attribut d'une liste d'objet
     * @param c : liste d'objets
     * @param indice : index de la colonne à sommer
     * @return valeur de la somme du champ
     */
    public static float calculSomme(ClassMAPTable c[], int indice) {
        float somme = 0.0F;
        float f3;
        try {
            for (int i = 0; i < c.length; i++) {
                Double d = new Double((String) c[i].getValeur(indice));
                somme += d.doubleValue();
                //somme += c[i].getFieldList()[indice].getFloat(c[i]);
            }

            float f = somme;
            return f;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }
}
