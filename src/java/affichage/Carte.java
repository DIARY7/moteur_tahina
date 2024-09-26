/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package affichage;

import bean.CGenUtil;
import bean.ClassGeom;
import bean.ClassMAPTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.postgis.Point;
import service.SigService;
//import sig.conf.PersonneMalade;

/**
 * Cette classe permet de génerer du javascript et du html près à être utilisé pour la géneration de
 * carte.
 * 
 * <p>Prenons en exemple, des objets de type <code>PointDeVente</code> qui sont des {@link bean.ClassGeom}</p>
 * <pre>
 *  //
 *  HashMap<String,String> legends=new HashMap<String,String>();
    leg.put("#b6fa41", "Point de vente KETRIKA");
    leg.put("#fa7fc3", "Point de vente PHO");
    Carte carte=new Carte();
    PointDeVente[] points = (PointDeVente)CGenUtil.rechercher(new PointDeVente(),null,null,null,"");
    carte.setData(points);
    //définir les légendes
    carte.createLegend("Type de point",leg);
    //Génerer le javascript de la carte sans les données
    out.println(carte.getHtml()); 
    //Génerer le javascript qui fournit les données
    out.println(carte.showGeometry(false,""));

 * </pre>
    <p>Il faut améliorer legende</p>
 * @author BICI
 */
public class Carte {
    private bean.ClassMAPTable[] data;
    private String libelleMarker="id";
    private boolean isCreate=false;
    private String legende;
    
    private String[] libelleMarkerlist;
    
    private String lien;
    
    public boolean isIsCreate() {
        return isCreate;
    }

    public void setIsCreate(boolean isCreate) {
        this.isCreate = isCreate;
    }

    public String[] getLibelleMarkerlist() {
        return libelleMarkerlist;
    }

    public void setLibelleMarkerlist(String[] libelleMarkerlist) {
        this.libelleMarkerlist = libelleMarkerlist;
    }
    
    /**
     * Constructeur par defaut
     */
    public Carte() {
    }
    
    /**
     * Constructeur 
     * @param data objet de mapping ClassMAPTable , 
     * liste coordonnées des points...
     */
    public Carte(ClassMAPTable[] data) {
        this.setData(data);
    }

    /**
     * Constructeur
     * @param data liste de données à afficher
     * @param libelleMarker legende des popups et de la carte
     */
    public Carte(ClassMAPTable[] data, String libelleMarker) {
        this.data = data;
        this.libelleMarker = libelleMarker;
    }
    /**
     * Constructeur
     * @param data liste de données à afficher
     * @param libelleMarker legende de la carte
     * @param libelleMarkerList liste des legendes pour les popups
     */
    public Carte(ClassMAPTable[] data, String libelleMarker,String[]libelleMarkerlist)
    {
        this.data = data;
        this.libelleMarker = libelleMarker;
        this.libelleMarkerlist=libelleMarkerlist;
    }
    public ClassMAPTable[] getData() {
        return data;
    }
    /**
     * Modifier les données pour la géneration de carte
     * @param data données pour la generation de carte
     */
    public void setData(ClassMAPTable[] data) {
        this.data = data;
    }
    /**
     * Ajouter des données supplémentaires aux données déjà donnés dans le constructeur
     * @param data2 data supplementaire
     */
    public void addData(ClassMAPTable[]data2){
        ArrayList<ClassMAPTable> taloha=new ArrayList<ClassMAPTable>();
        for(ClassMAPTable avant : data){
            taloha.add(avant);
        }
        for(ClassMAPTable nouveau : data2){
            taloha.add(nouveau);
        }
        data=new ClassGeom[taloha.size()];
        for(int i=0;i<taloha.size();i++){
            data[i]=taloha.get(i);
        }   
    }

    /**
     * Obtenir le libelle à mettre pour les popups si aucune liste de libelle existe
     * @return
     */
    public String getLIbelleMarker() {
        return libelleMarker;
    }

    public void setLibelleMarker(String libelle) {
        this.libelleMarker = libelle;
    }
    

     /**
     * Générer un script contenant des marqueurs pour une carte
     * sur la base des données contenues dans le tableau de données
     * Elle utilise la classe <strong> utilitaire  CGenUtil </strong> pour obtenir les valeurs
     *  des <strong> champs lattitude et libelleMarker </strong> des objets ClassMAPTable du tableau.
     * 
     * @return code JavaScript sous  forme d'une chaîne de caractères.
     * @throws Exception
     */
    public String getMarkers() throws Exception{
        String retour="";
        bean.ClassMAPTable[] liste=data;
        try{
            if(liste!=null && liste.length>0){
                retour+="<script>";
                for(int i=0;i<liste.length;i++){
                    retour+="var marker=L.marker(["+CGenUtil.getValeurFieldByMethod(liste[i], "lattitude")+", "+CGenUtil.getValeurFieldByMethod(liste[i], "longitude")+"]).addTo(map);\n" +
    "        marker.bindPopup(\"<b>"+CGenUtil.getValeurFieldByMethod(liste[i], libelleMarker)+"</b>\");";
                }
                retour+="</script>";
            }
        }catch(Exception e){
            throw e;
        }
        return retour;
    }

    /**
     * Générer un script contenant des marqueurs pour une carte
     * sur la base des données contenues dans le tableau de données
     * Elle utilise la classe <strong> utilitaire  CGenUtil </strong> pour obtenir les valeurs
     *  des <strong> champs geom et libelleMarker </strong> des objets ClassMAPTable du tableau.
     * 
     * @return code JavaScript sous  forme d'une chaîne de caractères.
     * @throws Exception
     */
    public String getMarkers2() throws Exception{
        String retour="";
        bean.ClassMAPTable[] liste=data;
        try{
            if(liste!=null && liste.length>0){
                retour+="<script>";
                Point p=null;
                for(int i=0;i<liste.length;i++){
                    p=new Point(CGenUtil.getValeurFieldByMethod(liste[i], "geom").toString());
                    retour+="var marker=L.marker(["+p.getX()+", "+p.getY()+"]).addTo(map);\n" +
    "        marker.bindPopup(\"<b>"+CGenUtil.getValeurFieldByMethod(liste[i], libelleMarker)+"</b>\");";
                }
                retour+="</script>";
            }
        }catch(Exception e){
            throw e;
        }
        return retour;
    }

    /**
     * générer du code JavaScript pour créer des cercles sur une carte .
     * Les cercles sont générés sur la base des coordonnées des points stockés dans un tableau d'objets
     *  ClassMAPTable appelé data.
     * @return  code JavaScript 
     * @throws Exception
     */
    public String getCircles2() throws Exception{
        String retour="";
        bean.ClassMAPTable[] liste=data;
        try{
            if(liste!=null && liste.length>0){
                retour+="<script>";
                Point p=null;
                for(int i=0;i<liste.length;i++){
                    p=new Point(CGenUtil.getValeurFieldByMethod(liste[i], "geom").toString());
                    retour+="var circle = L.circle(["+p.getX()+", "+p.getY()+"], {\n" +
"                color: 'red',\n" +
"                fillColor: '#f03',\n" +
"                fillOpacity: 1,\n" +
"                radius: 5\n" +
"        }).addTo(map);\n" +
"        circle.bindPopup(\"<b>"+CGenUtil.getValeurFieldByMethod(liste[i], libelleMarker)+"</b>\");";
                }
                retour+="</script>";
            }
        }catch(Exception e){
            throw e;
        }
        return retour;
    }

    /**
     * Génère du code JavaScript pour afficher des cercles sur une carte 
     * Le rayon des cercles est fixé à 5 pixels et leur couleur est également fixée
     *  au rouge avec une couleur de remplissage de #f03.
     * @return code JavaScript
     * @throws Exception
     */
    public String getCircles() throws Exception{
        String retour="";
        bean.ClassMAPTable[] liste=data;
        try{
            if(liste!=null && liste.length>0){
                retour+="<script>";
                for(int i=0;i<liste.length;i++){
                    retour+="var circle = L.circle(["+CGenUtil.getValeurFieldByMethod(liste[i], "lattitude")+", "+CGenUtil.getValeurFieldByMethod(liste[i], "longitude")+"], {\n" +
            "                color: 'red',\n" +
            "                fillColor: '#f03',\n" +
            "                fillOpacity: 1,\n" +
            "                radius: 5\n" +
            "        }).addTo(map);\n" +
            "        circle.bindPopup(\" "+CGenUtil.getValeurFieldByMethod(liste[i], libelleMarker)+" \").openPopup();";
                    if(libelleMarker!=null){
                        retour+="circle.bindPopup(\" "+CGenUtil.getValeurFieldByMethod(liste[i], libelleMarker)+" \").openPopup();";
                    }
                }
                retour+="</script>";
            }
        }catch(Exception e){
            throw e;
        }
        return retour;
    }

    /**
     * Génère une chaîne représentant le code JavaScript permettant d'ajouter un ou plusieurs marqueurs
     *  Polygon avec des couleurs personnalisées à une carte .
     * @param liste un tableau de points représentant les coordonnées où
     * les marqueurs doivent être ajoutés
     * @param legende une chaîne contenant une étiquette ou une légende
     * @return
     */
    public String drawPolygon(Point[] liste,String legende){
        String retour="<script>";
        if(liste!=null && liste.length>0){
            retour+="var polygon = L.polygon([";
            for(int i=0;i<liste.length;i++){
                retour+="["+liste[i].getX()+","+liste[i].getY()+"]";
                if(i!=liste.length-1){
                    retour+=",";
                }
            }
            retour+="]).addTo(map);";
            retour+="polygon.bindPopup(\" "+legende+" \").openPopup();";
        }
        retour+="</script>";
        return retour;
    }

    /**
     * Génère une chaîne représentant le code JavaScript permettant d'ajouter un ou plusieurs marqueurs
     *  circulaires avec des couleurs personnalisées à une carte .
     * @param liste un tableau de points représentant les coordonnées où
     * les marqueurs doivent être ajoutés
     * @param legende une chaîne contenant une étiquette ou une légende
     * @param couleur une chaîne facultative contenant une couleur personnalisée pour le marqueur 
     * (rouge par défaut si elle n'est pas fournie ou si elle est vide)
     * @return
     */
    public String drawCircleMarker(Point[] liste,String legende,String couleur){
        String retour="";
        if(liste!=null && liste.length>0){
            retour+="<script> var alavany=circle.length;";
            if(couleur==null || couleur.compareTo("")==0) couleur="red";
            for(int i=0;i<liste.length;i++){
                retour+="circle[alavany] = L.circle(["+liste[i].getX()+", "+liste[i].getY()+"], {\n" +
                "                color: '"+couleur+"',\n" +
                "                fillColor: '"+couleur+"',\n" +
                "                fillOpacity: 1,\n" +
                "                radius: 5\n" +
                "        }).addTo(map);\n" +
                "        circle[alavany].bindPopup(\" "+legende+" \").openPopup();";                               
            }
            retour+="</script>";
        }
        return retour;
    }

    /**
     * Génère une chaîne représentant le code JavaScript permettant d'ajouter un ou plusieurs marqueurs
     *  avec des icônes personnalisées à une carte .
     * @param liste un tableau de points représentant les coordonnées où
     *  les marqueurs doivent être ajoutés
     * @param legende une chaîne contenant une étiquette ou une légende
     * @return code JavaScript
     */
    public String drawMarker(Point[] liste,String legende){
        String retour="";
        if(liste!=null && liste.length>0){
            retour+="<script>var longueur=marker.length;";
            for(int i=0;i<liste.length;i++){
                retour+="var greenIcon = L.icon({\n" +
                "    iconUrl: 'https://img.icons8.com/plasticine/344/bridge.png',\n" +
                "    iconSize:     [20, 20] \n" +
                "});";
                retour+="marker[longueur]=L.marker(["+liste[i].getX()+", "+liste[i].getY()+"], {icon: greenIcon}).addTo(map);";
                retour+="marker[longueur].bindPopup(\" "+legende+" \").openPopup();";
            }
            retour+="</script>";
        }
        return retour;
    }

    /**
     * Génère une chaîne représentant le code JavaScript permettant d'ajouter un ou plusieurs marqueurs
     *  avec des icônes personnalisées à une carte .
     * @param liste un tableau de points représentant les coordonnées où
     *  les marqueurs doivent être ajoutés
     * @param legende une chaîne contenant une étiquette ou une légende
     * @param image une chaîne représentant l'URL du fichier image à utiliser comme icône de marqueur.
     * @return code JavaScript
     */
    public String drawImage(Point[] liste,String legende,String image){
        String retour="";
        if(liste!=null && liste.length>0){
            retour+="<script>";
            for(int i=0;i<liste.length;i++){
                retour+="var greenIcon = L.icon({\n" +
                "    iconUrl: '"+image+"',\n" +
                "    iconSize:     [20, 20] \n" +
                "});";
                retour+="var marker=L.marker(["+liste[i].getX()+", "+liste[i].getY()+"], {icon: greenIcon}).addTo(map);";
                retour+="marker.bindPopup(\" "+legende+" \").openPopup();";
            }
            retour+="</script>";
        }
        return retour;
    }

    /**
     * Gènère une chaîne représentant le code JavaScript permettant
     *  de dessiner une polyligne .
     * @param liste un tableau de points représentant les points de la polyligne
     * @param color une couleur (sous forme de chaîne) pour la polyligne
     * @return chaîne représentant le code JavaScript
     */
    public String drawStyledPolyline(Point[] liste,String color){
        String retour="";
        if(liste!=null && liste.length>0){
            retour="<script>var longueur1=polyline.length;"
                    + "polyline[longueur1] = L.polyline([";
            for(int i=0;i<liste.length;i++){
                retour+="["+liste[i].getX()+","+liste[i].getY()+"]";
                if(i!=liste.length-1){
                    retour+=",";
                }
            }
            retour+="]).addTo(map);";
            if(color!=null && color.compareTo("")!=0){
                retour+="polyline[longueur1].setStyle({\n" +
                "    color: '"+color+"'\n" +
                "});";
            }
            retour+="</script>";
        }
        return retour;
    }
    public String getCouleur(bean.ClassGeom geom){
        String color="red";
        return color;
    }

    /**
     * Génère du code HTML pour afficher différents types de géométries sur une carte
     * @param circles un booléen qui indique s'il faut afficher les marqueurs de cercle ou non
     * @param color une chaîne de caractères "color" qui spécifie la couleur des géométries à afficher.
     * @return code HTML généré pour l'affichage des géométries.
     * @throws Exception
     */
    public String showGeometry(Boolean circles,String color) throws Exception{
        String retour="";
        SigService sig=new SigService();
        bean.ClassGeom[] liste=(bean.ClassGeom[])data;
        String sousCat=null;
        String lienImage=null; String coul=null;
        try{
            if(liste!=null && liste.length>0){
                Point[] p=null;
                int test=0;
                for(int i=0;i<liste.length;i++){
                    p=sig.getAllPoints(liste[i]);
                    if(p!=null && p.length>0){
                        test=sig.getTypeGeom(liste[i]);
                        String after="";
                        if(getLien() != null && !getLien().isEmpty())
                            after = "<br/><center><a href='"+getLien()+"&"+liste[i].getAttributIDName()+"="+liste[i].getTuppleID()+"'><i class='glyphicon glyphicon-info-sign'></i> Voir Fiche</a></center>";
                            String markernom="";
                            if( libelleMarkerlist!=null && libelleMarkerlist.length!=0)
                            {
                                for(int j=0;j<libelleMarkerlist.length;j++){
                                    String valeur=""+CGenUtil.getValeurFieldByMethod(liste[i], libelleMarkerlist[j]);
                                    if(valeur!=null && valeur.compareToIgnoreCase("null")!=0){
                                    markernom+=""+valeur+"<br/>";
                                    }
                                }
                            }
                            else{
                                markernom=libelleMarker;
                            }
                        switch (test) {
                            case 1:
                                color=getCouleur(liste[i]);
                                 retour+=drawCircleMarker(p,markernom+after,color);
                                break;
                            case 2:
                                retour+=drawPolygon(p,((String)CGenUtil.getValeurFieldByMethod(liste[i], libelleMarker))+after);
                                break;
                            case 3:
                                 retour+=drawStyledPolyline(p,null);
                                break;
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        return retour;
    }

    /**
     * Générer une code javasacript pour créer une legende  
     * @param legendeMarker 
     * @param couleurNom liste des couleurs
     */
    public void createLegend(String legendeMarker,HashMap<String,String> couleurNom){
        legende="<script>var legend = L.control({ position: \"bottomright\" });\n" +
"			legend.onAdd = function(mymap) {\n" +
"                       var div = L.DomUtil.create(\"div\", \"legend\");\n" +
"                       div.innerHTML += \"<h4>L&eacute;gendes</h4>\";\n";
        if(legendeMarker!=null && legendeMarker.compareTo("")!=0)
            legende+="div.innerHTML += '<i class=\"icon\" style=\"background-image: url(https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.5.1/images/marker-icon.png);background-repeat: no-repeat;\"></i><span>"+legendeMarker+"</span><br>';\n";
        if(couleurNom!=null && !couleurNom.isEmpty()){
            for(Map.Entry mapentry : couleurNom.entrySet()){
                if(mapentry.getKey().toString().startsWith("https") && !mapentry.getKey().toString().endsWith(" ")){
                    legende+="div.innerHTML += '<img src="+mapentry.getKey()+"><span>"+mapentry.getValue()+"</span><br>';\n";
                }if(mapentry.getKey().toString().startsWith("https") && mapentry.getKey().toString().endsWith(" ")){
                    String[] liste=mapentry.getKey().toString().split("!");
                    legende+="div.innerHTML += '<img src="+liste[0]+"><i style=\"background: "+liste[1]+";\"></i><span>"+mapentry.getValue()+"</span><br>';\n";
                }if(!mapentry.getKey().toString().startsWith("https") && !mapentry.getKey().toString().endsWith(" ")){
                    legende+="div.innerHTML += '<i style=\"background: "+mapentry.getKey()+";\"></i><span>"+mapentry.getValue()+"</span><br>';\n";
                }
            }
        }
        legende+="return div;\n" +
                    "}\n" +
                "\n" +
                "legend.addTo(map);</script>";
    }
    public String getHtml(){        
        String retour=ElementCarte.getElementBase();
        if(isIsCreate()){
            return retour+ElementCarte.getElementCreate()+ElementCarte.getPosition;
        }
        return retour+this.legende+ElementCarte.printable;
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }
}
