package affichage;

import bean.ClassMAPTable;
import java.lang.reflect.Field;
import javax.servlet.http.HttpServletRequest;

import com.itextpdf.text.pdf.parser.Vector;

import utilitaire.Utilitaire;

public class PageGraph extends PageRechercheGroupe{
    private String jsonx="";
    private String jsony="";
   // private String chart="barchart";
    private  int height=230;
    private ChartOption chartOption;
    private String[] couleur;
    private int x=-1;
    private int y=-1;
    private Charts chart=new Charts();
    private String nomBoutonImprimer="Imprimer"; 
    private String entete="Graphique";
    private String chartName="barChart";

    public PageGraph(bean.ClassMAPTable o, HttpServletRequest req, String[] vrt, String[] listInterval, int nbRange, String[] colGr, String[] sommGr, int nbCol, int somGr) throws Exception {
        super(o,req,vrt,listInterval,nbRange,colGr,sommGr,nbCol,somGr);
        this.couleur=new String[this.getNbColGroupe()+this.getNbSommeGrp()];
    }

    public String getJsonx() {
        return jsonx;
    }

    public void setJsonx(String jsonx) {
        this.jsonx = jsonx;
    }

    public String getJsony() {
        return jsony;
    }

    public void setJsony(String jsony) {
        this.jsony = jsony;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Charts getChart() {
        return chart;
    }

    public void setChart(Charts chart) {
        this.chart = chart;
    }

    @Override
    public void creerObjetPage() throws Exception {
//         if(this.isPremier()){
//            
//         }
//         
         
         System.out.println("order ============="+ this.getOrdre());
        getValeurFormulaire(); //Recuperation des valeurs choisi
        
        preparerData(); // Recuperation des donnees de la base

        makeTableauRecap();

        String[] enteteAuto = Utilitaire.ajouterTableauString(getColGroupe(), getSommeGroupe());
        String critereLienTab = "<a href=" + getLien() + "?but=" + getApres() + "&numPag=1" + getApresLienPage() + formu.getListeCritereString();

        ClassMAPTable crt = getCritere();
        Field[] listF = crt.getClass().getDeclaredFields();
        setTableau(new TableauRecherche(getListe(), enteteAuto, critereLienTab, listF)); // Formation du tableau de resultat
        verifierXY();  
        this.chartOption=new ChartOption(this.getTableau(),this.getChart(),couleur,x,y,this.getNbSommeGrp());
        this.chartOption.setParentName(this.chartName);
        this.chartOption.setData();
    }
    
       @Override
    public void makeHtml(){
        
        String text=" <!--BAR CHART--> \n" +
"          <div class=\"box box-success\">\n" +
"            <div class=\"box-header with-border\">\n" +
"              <h3 class=\"box-title\">"+this.getTitre()+"</h3>\n" +
"\n" +
"            </div>\n" +
"            <div class=\"box-body\">\n" +
"              <div class=\"chart\">\n" +
"                <canvas id=\""+chartName+"\" style=\"height:"+this.getHeight()+"px\"></canvas>\n" +
"              </div>\n" +
"            </div>\n" +
"             <!--/.box-body--> \n" +
"          </div>"+this.chartOption.getScript()+
                 "\n" ;
        this.setHtml(text);
        
           System.out.println("-------------------------------------------------------------------------");
           System.out.println(text);
           System.out.println("-------------------------------------------------------------------------");
        
        
    }

    public ChartOption getChartOption() {
        return chartOption;
    }

    public void setChartOption(ChartOption chartOption) {
        this.chartOption = chartOption;
    }
    
    public int getIndiceColone(String colonneString ){
        int colonne=-1;
        String[]entete=Utilitaire.concatener(this.getColGroupe(), this.getColSomme());
        int taille=entete.length;
        for(int i=0;i<taille;i++){
            if(entete[i].equalsIgnoreCase(colonneString)){
                colonne=i;
                break;
            }
        }
        return colonne;
    }
    
    public void setCouleur(String colonne,String color) throws Exception{
        int indice=getIndiceColone(colonne);
        if(indice<0){
            throw new Exception("Colonne introuvable");
        }
        else{
            this.couleur[indice]=color;
        }
    }
    
      private void verifierXY(){
        int difference=this.getTableau().getLibeEntete().length-this.getNbSommeGrp();
        
        if(this.getTableau()!=null){
            if(x>difference){
                x=0;
                y-=difference;
            }
        }
    }
    
    
    public int getX() {
        return x;
    }

    public void setX(String colonne) {
        this.x = getIndiceColone(colonne);
    }
    public void setY(int Y) {
        if(Y>=this.getNbSommeGrp()+this.getNbColGroupe()){
            this.y=this.getNbColGroupe();
        }
        else{
            this.y = Y;
        }
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        if(x>this.getNbColGroupe()){
            this.x=0;
        }
        else{
            this.x = x;
        }
    }
    

    public void setY(String colonne) {
        this.y = getIndiceColone(colonne);
    }
    
    public void setCouleur(String[] couleurs){
        this.couleur=couleurs;
    }
    
    /**
     * 
     * @return  Boutton de Imprimer
     */
    @Override
    public String getBasPage(){
        String  temp ="<form action=\"../ChartServlet\" methode=\"post\">\n" +
        "  <input type=\"hidden\" name=\"data\" value=\""+this.chartOption.getParametrePdf()+"\">\n" +
        "  <input type=\"hidden\" name=\"chart\" value=\""+this.getChart().getType()+"\">\n" +
        "  <input type=\"hidden\" name=\"titre\" value=\""+this.getTitre()+"\">\n" +
        "  <input type=\"hidden\" name=\"entete\" value=\""+this.getEntete()+"\">\n" +
        "  <input class=\"btn btn-default\" type=\"submit\" value=\""+this.getNomBoutonImprimer()+"\">\n" +
        "</form>";
        //temp+=super.getBasPage();
        return temp; 
    }

    public String getNomBoutonImprimer() {
        return nomBoutonImprimer;
    }
    
    /**
     * Pour modifier la valeur du bouton  imprimer
     * @param nomBoutonImprimer 
     */
    public void setNomBoutonImprimer(String nomBoutonImprimer) {
        this.nomBoutonImprimer = nomBoutonImprimer;
    }

    public String getEntete() {
        return entete;
    }

    public void setEntete(String entete) {
        this.entete = entete;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    @Override
    public void makeCritere() throws Exception {
        String ordreTempo = this.getOrdre();
        super.makeCritere();
        if(ordreTempo!=null&&ordreTempo.compareTo("")!=0){
            this.setOrdre(ordreTempo);
        }
        if(this.getOrdre()==null){
            this.setOrdre(" order by "+Utilitaire.tabToString(this.getColGroupe(), "", ",")+" asc");
        }
    }
    
}
