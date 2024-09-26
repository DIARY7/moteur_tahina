/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.servlet;


import utilitaire.ChiffreLettre;
import utilitaire.Utilitaire;

/**
 *
 * @author Murielle
 */
public class UtilitaireFormat {

    public static String generateEntete(String titre, String logo, String width, String height) {
        String htmlStringEntete = "<html><head><title></title><style>td{ font-size: 10%;}</style></head><body>";
        htmlStringEntete = "<div align='center' style='max-width:"+width+";float:left'>";
        htmlStringEntete += "<img width='"+width+"' height='"+height+"' src='" + logo + "'/>";
        htmlStringEntete += "</div>";
        htmlStringEntete += "<div align='right' style='margin-left:120px;float:left'>";
        //htmlStringEntete += "<h6>Port Tamatave";
        //htmlStringEntete += "<br/>Lot II E 2 CG Iadiambola Ampasampito";
        //htmlStringEntete += "<br/>Antananarivo 101";
        //htmlStringEntete += "<br/>Tel. (261) 20 26 213 44 - (261) 34 12 680 09</h6>";
        htmlStringEntete += "<br/>";
        htmlStringEntete += "</div>";
        htmlStringEntete += "<div align='center'>";
        htmlStringEntete += "<h3><b>" + Utilitaire.champNull(titre) + "</b></h3>";
        htmlStringEntete += "</div>";
        htmlStringEntete += "<br/>";
        return htmlStringEntete;
    }
    
    public static String generateEntete(String titre, String logo) {
        return generateEntete(titre, logo, "50px", "50px");
    }

    public static String generateBasPage(int nombre, double montant) {
        String htmlStringBasPage = "<div align='left'><h5>Arr�t� le nombre � " + Utilitaire.formaterSansVirgule(nombre) + "</h5>";
        if (montant > 0) {
            htmlStringBasPage += "<h5>Arr�t� le pr�sent �tat � la somme de " + ChiffreLettre.convertRealToString(montant) + " Ariary</h5></div>";
        }
        return htmlStringBasPage;
    }
}
