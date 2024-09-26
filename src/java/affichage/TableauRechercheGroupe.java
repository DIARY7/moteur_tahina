/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package affichage;

import bean.CGenUtil;
import bean.ClassMAPTable;
import bean.EtiquetteLigneCol;
import bean.LigneCroises;
import bean.ValeurCroises;
import bean.ValeurEtiquette;
import java.lang.reflect.Field;
import java.util.ArrayList;
import utilitaire.Utilitaire;
import java.util.HashMap;

/**
 *
 * @author root
 */
public class TableauRechercheGroupe extends TableauRecherche {

    ArrayList<String> ligne=new ArrayList();
    ArrayList<String> colonne=new ArrayList();
    HashMap<String,ValeurCroises> valeur=new HashMap<String,ValeurCroises>();
    HashMap<String,ValeurCroises> somme=new HashMap<String,ValeurCroises>();
    String[] attrLig;
    String[] attrColonne;
    public void setAttrLig(String[] a)
    {
        attrLig=a;
    }
    public void setAttrColonne(String[] c)
    {
        attrColonne=c;
    }
    public void setAttrLigneColonne(String[] lign,String[]colonne)
    {
        ArrayList<String> ligne=new ArrayList<String>();
        for(int i=0;i<lign.length;i++)
        {
            
            if(Utilitaire.estIlDedans(lign[i],colonne)==-1)
            {
                ligne.add(lign[i]);
            }
        }
        this.setAttrLig(getAttr(ligne));
        this.setAttrColonne(colonne);
    }
    public void setAttrLigneColonne(String[] c)
    {
        ArrayList<String> col=new ArrayList<String>();
        ArrayList<String> ligne=new ArrayList<String>();
        for(int i=0;i<c.length;i++)
        {
            if(i%2==0)
            {
                ligne.add(c[i]);
            }
            else
            {
                col.add(c[i]);
            }
        }
        this.setAttrLig(getAttr(ligne));
        this.setAttrColonne(getAttr(col));
    }
    public static String[] getAttr(ArrayList<String>c)
    {
        String [] attr=new String[c.size()];
        c.toArray(attr);
        return attr;
    }
    
    public TableauRechercheGroupe(ClassMAPTable[] liste, String[] attrLigneC,String[] attrCentre,String lien) throws Exception
    {
        this.setAttrLigneColonne(attrLigneC);
        transformer(liste,attrCentre,lien);
        super.setValeurEtiquette(getValeur());
    }
    public void afficher(String[] aAfficher)
    {
        for(int i=0;i<aAfficher.length;i++)
        {
            System.out.println("a Afficher "+aAfficher[i]);
        }
    }
    public TableauRechercheGroupe(ClassMAPTable[] liste, String[] attrLigne,String[]attrCol,String[] attrCentre,String lien) throws Exception
    {
        this.setAttrLigneColonne(attrLigne,attrCol);
        transformer(liste,attrCentre,lien);
        super.setValeurEtiquette(getValeur());
    }
    public void transformer(ClassMAPTable[] liste,String[] attrCentre,String lie) throws Exception
    {
        if(liste.length==0) return;
        String lien=lie;
        for(int i=0; i<liste.length;i++)
        {   
            double[] valeurCentre=new double[attrCentre.length];
            for(int iCentre=0;iCentre<attrCentre.length;iCentre++)
                valeurCentre[iCentre]=Utilitaire.stringToDouble(CGenUtil.getValeurFieldByMethod(liste[i], attrCentre[iCentre]).toString());
        
            String lig=transformer(liste[i], this.attrLig);
            String col=transformer(liste[i],this.attrColonne);
            if(estDejaDansListe(ligne, lig)==false)
            {
                ligne.add(lig);
            }            
            
            if(estDejaDansListe(colonne, col)==false)
            {
                colonne.add(col);
            }
            ValeurCroises valCroise=new ValeurCroises(valeurCentre,lien+makeLien(lig,col));
            valeur.put(lig+"::"+col, valCroise);
            getSomme(lig,valeurCentre,lien+makeLien(lig,""));
            getSomme(col,valeurCentre,lien+makeLien("",col));   
        }
    }
    public  void getSomme(String ligCol,double[] valeur,String lien)
    {
        ValeurCroises efaAo=somme.get(ligCol);
        if(efaAo!=null)
        {
            double[] nouvelleVal=new double[valeur.length];
            for(int i=0;i<valeur.length;i++)
            {
                nouvelleVal[i]=efaAo.getListeVal()[i]+valeur[i];
            }
            efaAo.setListeVal(nouvelleVal);
        }
        else
        {
            somme.put(ligCol, new ValeurCroises(valeur,lien));
        }
        
    }
    public  void getSomme(String ligCol,double valeur,String lien)
    {
        ValeurCroises efaAo=somme.get(ligCol);
        if(efaAo!=null)
        {
            
            efaAo.setValeur(efaAo.getValeur()+valeur);
        }
        else
        {
            somme.put(ligCol, new ValeurCroises(valeur,lien));
        }
    }
    public String makeLien(String ligne,String colonne)
    {
        String[] listeL=getAttrSansSep(ligne);
        
        String[] listeC=getAttrSansSep(colonne);
        String retour="";
        if(ligne!=null&&ligne.compareToIgnoreCase("")!=0)
        {
            for(int l=0;l<attrLig.length;l++)
            {
                retour=retour+"&"+attrLig[l]+"="+ValeurEtiquette.formatter(listeL[l]);
            }
        }
        if(colonne!=null&&colonne.compareToIgnoreCase("")!=0)
        {
            for(int c=0;c<attrColonne.length;c++)
            {
                retour=retour+"&"+attrColonne[c]+"="+ValeurEtiquette.formatter(listeC[c]);
            }
        }
        return retour;
    }
    public static void init(ValeurEtiquette[][] retour)
    {
        for(int i=0;i<retour.length;i++)
        {
            for(int j=0;j<retour[i].length;j++)
            {
                retour[i][j]=new ValeurEtiquette("","");
            }
        }
    }
    public ValeurEtiquette[][] getValeur() throws Exception
    {
        int nbLigne=attrLig.length;
        int nbColonne=attrColonne.length;
        ValeurEtiquette[][] retour=new ValeurEtiquette[ligne.size()+nbColonne+1][colonne.size()+nbLigne+1];
        init(retour);
        for(int i=attrColonne.length;i<ligne.size()+attrColonne.length;i++) // Remplir gauche par ligne
        {
            String[] listeLigne=getAttrSansSep(ligne.get(i-attrColonne.length));
            for(int c=0;c<listeLigne.length;c++)
            {
                retour[i][c]=new ValeurEtiquette(listeLigne[c],"");
            }
        }
        
        for(int i=attrLig.length;i<colonne.size()+attrLig.length;i++) // Remplir haut par colonne
        {
            String[] listeColonne=getAttrSansSep(colonne.get(i-attrLig.length));
            for(int c=0;c<listeColonne.length;c++)
            {
                
                retour[c][i]=new ValeurEtiquette(listeColonne[c],"");
            }
        }
            
       
        for(int iLigne=nbColonne;iLigne<ligne.size()+nbColonne+1;iLigne++)
        {
            for(int iColonne=nbLigne;iColonne<colonne.size()+nbLigne+1;iColonne++)
            {
                ValeurCroises val=null;
                if(iLigne==ligne.size()+nbColonne&&(iColonne<colonne.size()+nbLigne)) val=somme.get(colonne.get(iColonne-nbLigne));
                else if(iColonne==colonne.size()+nbLigne&&(iLigne<ligne.size()+nbColonne)) val=somme.get(ligne.get(iLigne-nbColonne));
                else if((iLigne<ligne.size()+nbColonne)&&(iColonne<colonne.size()+nbLigne))val=valeur.get(ligne.get(iLigne-nbColonne)+"::"+colonne.get(iColonne-nbLigne));
                if(val!=null)
                {
                    retour[iLigne][iColonne]=new ValeurEtiquette(val);
                }
                
            }
        }
        return retour;
    }
    public static String[] getAttrSansSep(String valeur)
    {
        
        
        String[] retour=valeur.split("\\.\\.");
        if(retour.length<=1)
        {
            String[] ret={valeur,""};
            return ret;
        }
        return retour;
    }
    public static String transformer(ClassMAPTable e, String[] attr) throws Exception
    {
        String retour="";
        
        for(int iLigne=0;iLigne<attr.length;iLigne++)
        {
            String valeurLigne="";
            Object o=CGenUtil.getValeurFieldByMethod(e, attr[iLigne]);
            
            if(o!=null)
            {
                valeurLigne=o.toString();
            }
            retour=retour+valeurLigne;
            if(iLigne<attr.length-1&&valeurLigne.compareToIgnoreCase("")!=0)
            {
                retour=retour+"..";
            }
        }
        return retour;
        
    }
    public static boolean estDejaDansListe(ArrayList<String> liste,String valeur) throws Exception
    {
        for(String val:liste)
        {
            if(val.compareToIgnoreCase(valeur)==0) return true;
        }
        return false;
    }
    public boolean estDejaDansEtiquette(ArrayList<EtiquetteLigneCol> liste,EtiquetteLigneCol valeur) throws Exception
    {
        for(EtiquetteLigneCol lc:liste)
        {
            return lc.comparer(valeur);
        }
        return false;
    }
    public int[] getPropEntet() {

        int[] ret = new int[colonne.size()+attrLig.length+1];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = 100 / ret.length;
        }
        return ret;

        
    }
    public void makeHtml() throws Exception {
        if (getValeurEtiquette() == null || getValeurEtiquette().length == 0) {
            return;
        }
        String temp = "";
        String tempcsv = "";
        String tempxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \r\n <tableau>";
        temp += "<div class=\"row\">";
        temp += "<div class=\"row col-md-12\">";
        temp += "<div class=\"box\">";
        temp += "<div class=\"box-header\">";
        temp += "<h3 class=\"box-title\" align=\"center\">" + getTitre() + "</h3>";
        temp += "</div>";
        temp += "<div class=\"box-body table-responsive no-padding\">";
        temp += "<div id=\"selectnonee\">";
        temp += "<table width=\"" + getTailleTableau() + "\" border=\"0\" align=\"center\" cellpadding=\"3\" cellspacing=\"3\" class=\"table table-hover table-bordered\">";
        int nombreLigne = 0, nombreColonne = 0;
        nombreLigne = getValeurEtiquette().length;

        
        nombreColonne = getValeurEtiquette()[0].length;
        
        temp += "<tbody>";
        for (int i = 0; i < nombreLigne; i++) 
        {

            temp += "<tr>";
            tempxml += "<row>";
            int j = 0, l = 0;
            for (j = 0; j < nombreColonne; j++) 
            {  
                String lien="";
                String apresLien="";
                if(getValeurEtiquette()[i][j].getLien()!=null&&getValeurEtiquette()[i][j].getLien().compareTo("")!=0)
                {
                    lien="<a href='"+getValeurEtiquette()[i][j].getLien() +"'>";
                    apresLien="</a>";
                }
                temp += "<td width=\"" + getPropEntet()[j] + "%\" align=\"" + getValeurEtiquette()[i][j].getAlign() + "\" >" + lien + Utilitaire.champNull(getValeurEtiquette()[i][j].getValeur()) + apresLien + "</td>";
               
                tempcsv += Utilitaire.verifNumerique(getValeurEtiquette()[i][j].getValeur());
                //tempxml += "<" + getLibelleAffiche()[j] + ">" + Utilitaire.verifNumerique(getValeurEtiquette()[i][j].getValeur()) + "</" + getLibelleAffiche()[j] + ">\r\n";
                if (j != nombreColonne - 1) {
                    tempcsv += ";";
                }
            }
            temp += "</tr>";
            tempcsv += "\r\n";
            tempxml += "</row>\r\n";
        }
        temp += "</tbody>";
        tempxml += "</tableau>\r\n";
        temp += "</table>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        temp += "</div>";
        setHtml(temp);
        setExpcsv(tempcsv);
        setExpxml(tempxml);
    }
}
