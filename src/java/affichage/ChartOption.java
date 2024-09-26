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
import java.util.UUID;

import bean.CGenUtil;
import bean.ClassMAPTable;
import utilitaire.Utilitaire;


/**
 *
 * @author nyamp
 */
public class ChartOption {
    private TableauRecherche tableau ;
    private int x;
    private int y;
    private Charts chart;
    private String[] couleur;
    private String parametrePdf;
    private int tailleSomme;
    private String donneScript;
    private List<ObjectChart> liste=new ArrayList();
    private String colOnClick;
    private String parentName;
    

    public ChartOption(TableauRecherche tableau,Charts chart,String[]couleur,int x,int y,int colsome) throws Exception{
        this.setTableau(tableau);
        this.setTailleSomme(colsome);
        this.setChart(chart);
        this.setCouleur(couleur);
        this.setX(x);
        this.setY(y);
    }
    public void setData() throws Exception{
        if(Charts.isCercle(this.getChart().getType())){
            setDataCercle();
        }
        else{
             setDataAutre();
        }
        this.setparametrePdf();
      
    }
    
    private void setDataCercle() throws ParseException{
        String donnerTemp="";
        String dataTemp="";
        String labelTemp="";
        String colorTemp="";
        int taille=this.getTableau().getData().length;
        int indice=0;
        String[][] donne=this.getTableau().getDataDirecte();
        for(int i=0;i<taille;i++,indice++){
             liste=ObjectChart.getListe(donne,this.getX(),this.getY());
            if(indice==this.getCouleur().length){
                indice=0;
            }
            if(i!=0){
                donnerTemp+=",";
                dataTemp+=",";
                labelTemp+=",";
                colorTemp+=",";
            }
                //ordonne[index]+=Utilitaire.stringToDouble(valeurChamp); --TALOHA
                NumberFormat nf = NumberFormat.getInstance(Locale.FRENCH); //AMZAO
                //ordonne[index]+=nf.parse(donne[i][y]).doubleValue(); //AMZAO
            dataTemp+=nf.parse(donne[i][y]).doubleValue();
            labelTemp+="'"+donne[indice][x]+"'";
            colorTemp+="'"+this.couleur[indice]+"'";
            /*donnerTemp+="{\n" +
            "        value: "+nf.parse(donne[i][y]).doubleValue()+",\n" +
            "        color: \""+this.couleur[indice]+"\",\n" +
            "        highlight: \""+this.couleur[indice]+"\",\n" +
            "        label: \""+donne[indice][x]+"\"\n" +
            "      }";*/
        }
            //donneScript="["+donnerTemp+"] ;\n";
            donneScript="{\n" +
            "datasets:[{data: ["+dataTemp+"],"+"backgroundColor:["+colorTemp+"]"+"}],\n"+
            "labels:["+labelTemp+"]"+
            "}\n;";
    }
    private void setparametrePdf(){
        parametrePdf="";
        for(int i=0;i<liste.size();i++){
            parametrePdf+=liste.get(i).getDataToString();
        }
    }
    private void setDataAutre() throws Exception{
        int taille=this.getTableau().getData().length;
        System.out.println("------TAILLE "+taille);
        String[][] donne=this.getTableau().getDataDirecte();
        String[] entete=this.getTableau().getLibeEntete();
        String valeurChamp = null;
        String libelle="";
        int tailleEntete=entete.length;
        String[]ordonne=new String [this.getTailleSomme()];
        String donneX="";
        String couleurTemp="#00a65a";

        for(int i=0;i<taille;i++){            
            valeurChamp = donne[i][x];
            if(i!=0){
                libelle+=",";
            }
            libelle+="'"+valeurChamp+"'";
            for(int j=tailleEntete-this.getTailleSomme(),index=0;j<tailleEntete;j++,index++){
                liste.add(new ObjectChart(this.getTableau().getLibeEntete()[j],this.getTableau().getDataDirecte()[i][j],this.getTableau().getDataDirecte()[i][this.getX()]));
                valeurChamp = donne[i][j];
                if(ordonne[index]==null ){
                    ordonne[index]="";
                }
                if(i!=0){
                    ordonne[index]+=",";
                }
                //ordonne[index]+=Utilitaire.stringToDouble(valeurChamp); --TALOHA
                NumberFormat nf = NumberFormat.getInstance(Locale.FRENCH); //AMZAO
                ordonne[index]+=nf.parse(valeurChamp).doubleValue(); //AMZAO
                if(i==taille-1){
                      if(index!=0){
                        donneX+=",";
                    }
                    
                   if(this.couleur[index]!=null){
                        couleurTemp=this.couleur[index];
                    }
                    donneX+=
                    "        {\n" +
                    "          label: \""+this.tableau.getLibelleAffiche()[j]+"\",\n" +
                    "          backgroundColor:  \""+couleurTemp+"\",\n" +
                    "          borderColor: \""+couleurTemp+"\",\n" +
                    "          pointColor: \""+couleurTemp+"\",\n" +
                    "          pointStrokeColor: \""+couleurTemp+"\",\n" +
                    "          pointHighlightFill: \"#fff\",\n" +
                    "          pointHighlightStroke: \""+couleurTemp+"\",\n" +
                    "          data: ["+ordonne[index]+"]\n" +
                    "        }"  ;
                }
            }
        }
        this.donneScript="{\n" +
            "      labels: ["+libelle+"],\n" +
            "      datasets: [\n" +donneX+
            "        ]" +
            "    };"+" \n";
    }
     
    public String getScript() {
        
        String script =
        "<script>\n" +
        "  $(function () {\n"
                + "var rectangleSet = false;" +
        getFunctionOnClick() +
        "    let data = " +this.donneScript+
        "    let chartCanvas = $(\"#"+parentName+"\").get(0).getContext(\"2d\");\n" +
        //"    var chart = new Chart(chartCanvas);\n" +
           this.chart.getChartOption()+
        "  });\n" +
        "</script>";
        return script;
    }

    public ChartOption (){
        
    }

    public TableauRecherche getTableau() {
        return tableau;
    }

    public String getParametrePdf() {
        return parametrePdf;
    }

    private void setParametrePdf(String parametrePdf) {
        this.parametrePdf = parametrePdf;
    }

    public int getTailleSomme() {
        return tailleSomme;
    }

    public void setTailleSomme(int tailleSomme) {
        this.tailleSomme = tailleSomme;
    }

    public void setTableau(TableauRecherche tableau) {
        this.tableau = tableau;
    }

    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        if(x<0 || (this.tableau!=null && x>this.getTableau().getLibeEntete().length)){
            x=0;
        }
        else{
            this.x = x;
        }
    }
    

    public int getY() {
        return y;
    }

    public void setY(int y) {
        if(y<0 || y<this.tableau.getLibeEntete().length - this.tailleSomme){
            this.y=this.tableau.getLibeEntete().length - this.tailleSomme;
        }
        else{
            this.y = y;
        }
        
    }

    public Charts getChart() {
        return chart;
    }

    public void setChart(Charts chart) {
        this.chart = chart;
    }

    public String[] getCouleur() {
        return couleur;
    }

    public void setCouleur(String[] couleurs) throws Exception {
        int taille=0;
        this.couleur = couleurs;
        if(this.getTableau()!=null){
            if(Charts.isCercle(this.chart.getType())){
                taille=this.tableau.getData().length;
            }
            else{
                taille=this.tableau.getLibeEntete().length;
            }
            if(couleurs==null || couleurs.length==0){
                Couleur color=new Couleur(0,255,0);
                Couleur[]colors = color.genererCouleur(100,taille);
                this.couleur=new String[taille];
                for(int i=0;i<taille;i++){
                    this.couleur[i]=colors[i].getCouleur();
                }
            }
            else{
                this.couleur=new String[taille];
                Couleur color=new Couleur(50,60,50);
                 Couleur[] colors=color.genererCouleur(50,taille);
                 int j=0;
                 int i=0;
                for(i=i;i<couleurs.length && i<taille;i++){
                    if(couleurs[i]!=null){
                        this.couleur[i] = couleurs[i];
                    }
                    else{
                        this.couleur[i]=colors[j].getCouleur();
                        j++;
                    }
                }
                for(j=j;i<taille;j++,i++){
                    this.couleur[i] = colors[j].getCouleur();
                }

            }
        }
    }
    
    public String getColOnClick() {
        return colOnClick;
    }
    public void setColOnClick(String colOnClick) {
        this.colOnClick = colOnClick;
    }

    public String getFunctionOnClick(){
        try{
            if(this.colOnClick!=null){
                String nomVariable = colOnClick;
                String toReturn = "let "+nomVariable+" = [";
                ClassMAPTable[] data = this.getTableau().getData();
                String[] values = new String[data.length];
                for(int i=0;i<data.length;i++){
                    values[i] = CGenUtil.getValeurInsert(data[i], this.colOnClick);
                }
                toReturn += Utilitaire.tabToString(values, "'", ",");
                toReturn += "];";
                this.chart.setNomVariable(nomVariable);
                return toReturn;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String getParentName() {
        return parentName;
    }
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
    
}
