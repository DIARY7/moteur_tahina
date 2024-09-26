/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package affichage;

/**
 *
 * @author nyamp
 */
public class Charts {
    private boolean scaleShowGridLines=false;
    private int scaleGridLineWidth=1;
    private String scaleGridLineColor="rgba(0,0,0,.05)";
    private boolean scaleShowHorizontalLines=true;
    private boolean scaleShowVerticalLines=true;
    private int barValueSpacing=5;
    private int barDatasetSpacing=1;
    private boolean responsive=true;
    private boolean pointDot=false;
    private int pointDotRadius=4;
    
    private String type="barchart";
    private String lienOnClick;
    private String nomVariable;
    private String libelleOnClick;
    

    public String getChartOption(){
        String option=null;
        if(type.equalsIgnoreCase("barchart")){
            option=
            "var barChartOptions                  = {\n" +
            "      //Boolean - Whether the scale should start at zero, or an order of magnitude down from the lowest value\n" +
            "      scaleBeginAtZero        : true,\n" +
            "      //Boolean - Whether grid lines are shown across the chart\n" +
            "      scaleShowGridLines      : "+scaleShowGridLines+",\n" +
            "      //String - Colour of the grid lines\n" +
            "      scaleGridLineColor      : '"+scaleGridLineColor+"',\n" +
            "      //Number - Width of the grid lines\n" +
            "      scaleGridLineWidth      : "+scaleGridLineWidth+",\n" +
            "      //Boolean - Whether to show horizontal lines (except X axis)\n" +
            "      scaleShowHorizontalLines: "+scaleShowHorizontalLines+",\n" +
            "      //Boolean - Whether to show vertical lines (except Y axis)\n" +
            "      scaleShowVerticalLines  : "+scaleGridLineWidth+",\n" +
            "      //Boolean - If there is a stroke on each bar\n" +
            "      barShowStroke           : true,\n" +
            "      //Number - Pixel width of the bar stroke\n" +
            "      barStrokeWidth          : 2,\n" +
            "      //Number - Spacing between each of the X value sets\n" +
            "      barValueSpacing         : "+barValueSpacing+",\n" +
            "      //Number - Spacing between data sets within X values\n" +
            "      barDatasetSpacing       : "+barDatasetSpacing+",\n" +
            "      //String - A legend template\n" +
            //"      legendTemplate          : '<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<datasets.length; i++){%><li><span style=\"background-color:<%=datasets[i].fillColor%>\"></span><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>',\n" +
            "      //Boolean - whether to make the chart responsive\n" +
            "      responsive              : "+responsive+",\n" +
            onClickOptions()+
            "      maintainAspectRatio     : true\n" +
            
            "    }\n" +
            "\n" +
            "    barChartOptions.datasetFill = false;\n" +
            "let chart = new Chart(chartCanvas,{type:'bar',\n"+
            "data:data,options:barChartOptions})";
        }
        else if(type.equalsIgnoreCase("areachart")){
            option=
            "    var areaChartOptions = {\n" +
            "      //Boolean - If we should show the scale at all\n" +
            "      showScale               : true,\n" +
            "      //Boolean - Whether grid lines are shown across the chart\n" +
            "      scaleShowGridLines      :  "+scaleShowGridLines+",\n" +
            "      //String - Colour of the grid lines\n" +
            "      scaleGridLineColor      : ' "+scaleGridLineColor+"',\n" +
            "      //Number - Width of the grid lines\n" +
            "      scaleGridLineWidth      :  "+scaleGridLineWidth+",\n" +
            "      //Boolean - Whether to show horizontal lines (except X axis)\n" +
            "      scaleShowHorizontalLines:  "+scaleShowHorizontalLines+",\n" +
            "      //Boolean - Whether to show vertical lines (except Y axis)\n" +
            "      scaleShowVerticalLines  :  "+scaleShowVerticalLines+",\n" +
            "      //Boolean - Whether the line is curved between points\n" +
            "      bezierCurve             : true,\n" +
            "      //Number - Tension of the bezier curve between points\n" +
            "      bezierCurveTension      : 0.3,\n" +
            "      //Boolean - Whether to show a dot for each point\n" +
            "      pointDot                : "+pointDot+",\n" +
            "      //Number - Radius of each point dot in pixels\n" +
            "      pointDotRadius          : "+pointDotRadius+",\n" +
            "      //Number - Pixel width of point dot stroke\n" +
            "      pointDotStrokeWidth     : 1,\n" +
            "      //Number - amount extra to add to the radius to cater for hit detection outside the drawn point\n" +
            "      pointHitDetectionRadius : 20,\n" +
            "      //Boolean - Whether to show a stroke for datasets\n" +
            "      datasetStroke           : true,\n" +
            "      //Number - Pixel width of dataset stroke\n" +
            "      datasetStrokeWidth      : 2,\n" +
            "      //Boolean - Whether to fill the dataset with a color\n" +
            "      datasetFill             : true,\n" +
            "      //String - A legend template\n" +
            //"      legendTemplate          : '<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<datasets.length; i++){%><li><span style=\"background-color:<%=datasets[i].lineColor%>\"></span><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>',\n" +
            "      //Boolean - whether to maintain the starting aspect ratio or not when responsive, if set to false, will take up entire container\n" +
            "      maintainAspectRatio     : true,\n" +
            "}";
        }
        else if(type.equalsIgnoreCase("donutchart")){
            option=
            " var pieOptions     = {\n" +
            "      //Boolean - Whether we should show a stroke on each segment\n" +
            "      segmentShowStroke    : true,\n" +
            "      //String - The colour of each segment stroke\n" +
            "      segmentStrokeColor   : '#fff',\n" +
            "      //Number - The width of each segment stroke\n" +
            "      segmentStrokeWidth   : 2,\n" +
            "      //Number - The percentage of the chart that we cut out of the middle\n" +
            "      percentageInnerCutout: 50, // This is 0 for Pie charts\n" +
            "      //Number - Amount of animation steps\n" +
            "      animationSteps       : 100,\n" +
            "      //String - Animation easing effect\n" +
            "      animationEasing      : 'easeOutBounce',\n" +
            "      //Boolean - Whether we animate the rotation of the Doughnut\n" +
            "      animateRotate        : true,\n" +
            "      //Boolean - Whether we animate scaling the Doughnut from the centre\n" +
            "      animateScale         : false,\n" +
            "      //Boolean - whether to make the chart responsive to window resizing\n" +
            "      responsive           : "+responsive+",\n" +
            "      // Boolean - whether to maintain the starting aspect ratio or not when responsive, if set to false, will take up entire container\n" +
            "      maintainAspectRatio  : true,\n" +
            "      //String - A legend template\n" +
            //"      legendTemplate       : '<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<segments.length; i++){%><li><span style=\"background-color:<%=segments[i].fillColor%>\"></span><%if(segments[i].label){%><%=segments[i].label%><%}%></li><%}%></ul>'\n" +
            "    }\n" +
            "    //Create pie or douhnut chart\n" +
            "    // You can switch between pie and douhnut using the method below.\n" +
            "    chart.Doughnut(data, pieOptions)";
        }
        else if(type.equalsIgnoreCase("linechart")){
            option= 
            "    var areaChartOptions = {\n" +
            "      //Boolean - If we should show the scale at all\n" +
            "      showScale               : true,\n" +
            "      //Boolean - Whether grid lines are shown across the chart\n" +
            "      scaleShowGridLines      : "+scaleShowGridLines+",\n" +
            "      //String - Colour of the grid lines\n" +
            "      scaleGridLineColor      : '"+scaleGridLineColor+"',\n" +
            "      //Number - Width of the grid lines\n" +
            "      scaleGridLineWidth      : "+scaleGridLineWidth+",\n" +
            "      //Boolean - Whether to show horizontal lines (except X axis)\n" +
            "      scaleShowHorizontalLines: "+scaleShowHorizontalLines+",\n" +
            "      //Boolean - Whether to show vertical lines (except Y axis)\n" +
            "      scaleShowVerticalLines  : "+scaleShowVerticalLines+",\n" +
            "      //Boolean - Whether the line is curved between points\n" +
            "      bezierCurve             : true,\n" +
            "      //Number - Tension of the bezier curve between points\n" +
            "      bezierCurveTension      : 0.3,\n" +
            "      //Boolean - Whether to show a dot for each point\n" +
            "      pointDot                : "+pointDot+",\n" +
            "      //Number - Radius of each point dot in pixels\n" +
            "      pointDotRadius          : "+pointDotRadius+",\n" +
            "      //Number - Pixel width of point dot stroke\n" +
            "      pointDotStrokeWidth     : 1,\n" +
            "      //Number - amount extra to add to the radius to cater for hit detection outside the drawn point\n" +
            "      pointHitDetectionRadius : 20,\n" +
            "      //Boolean - Whether to show a stroke for datasets\n" +
            "      datasetStroke           : true,\n" +
            "      //Number - Pixel width of dataset stroke\n" +
            "      datasetStrokeWidth      : 2,\n" +
            "      //Boolean - Whether to fill the dataset with a color\n" +
            "      datasetFill             : false,\n" +
            "      //String - A legend template\n" +
            //"      legendTemplate          : '<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<datasets.length; i++){%><li><span style=\"background-color:<%=datasets[i].lineColor%>\"></span><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>',\n" +
            "      //Boolean - whether to maintain the starting aspect ratio or not when responsive, if set to false, will take up entire container\n" +
            "      maintainAspectRatio     : true,\n" +
            "      //Boolean - whether to make the chart responsive to window resizing\n" +
            "      responsive              : "+responsive+"\n" +
            "    }\n" +
            "\n" +
            "    //Create the line chart\n" +
            "    chart.Line(data, areaChartOptions)";
        }
        else if(type.equalsIgnoreCase("piechart")){
            option=
            " var pieOptions     = {\n" +
            "      //Boolean - Whether we should show a stroke on each segment\n" +
            "      segmentShowStroke    : true,\n" +
            "      //String - The colour of each segment stroke\n" +
            "      segmentStrokeColor   : '#fff',\n" +
            "      //Number - The width of each segment stroke\n" +
            "      segmentStrokeWidth   : 2,\n" +
            "      //Number - The percentage of the chart that we cut out of the middle\n" +
            "      percentageInnerCutout: 0, // This is 0 for Pie charts\n" +
            "      //Number - Amount of animation steps\n" +
            "      animationSteps       : 100,\n" +
            "      //String - Animation easing effect\n" +
            "      animationEasing      : 'easeOutBounce',\n" +
            "      //Boolean - Whether we animate the rotation of the Doughnut\n" +
            "      animateRotate        : true,\n" +
            "      //Boolean - Whether we animate scaling the Doughnut from the centre\n" +
            "      animateScale         : false,\n" +
            "      //Boolean - whether to make the chart responsive to window resizing\n" +
            "      responsive           : "+responsive+",\n" +
            "      // Boolean - whether to maintain the starting aspect ratio or not when responsive, if set to false, will take up entire container\n" +
            "      maintainAspectRatio  : true,\n" +
            "      //String - A legend template\n" +
            //"      legendTemplate       : '<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<segments.length; i++){%><li><span style=\"background-color:<%=segments[i].fillColor%>\"></span><%if(segments[i].label){%><%=segments[i].label%><%}%></li><%}%></ul>'\n" +
            "    }\n" +
            "    //Create pie or douhnut chart\n" +
            "    // You can switch between pie and douhnut using the method below.\n" +
            "    pieOptions.datasetFill = false;\n" +
            "let chart = new Chart(chartCanvas,{type:'pie',\n"+
            "data:data,options:pieOptions})";
        }
        return option;
    }
    
    
    public static String getChartOption(String chart) throws Exception{
        Charts charts =new Charts(chart);
        String option=charts.getChartOption();
        return option;
        
    }
    public static boolean isCercle(String chart){
        boolean verifier=false;
        if(chart.equalsIgnoreCase("donutchart") ||  chart.equalsIgnoreCase("piechart")){
            verifier=true;
        }
        return verifier;
    }
    public static boolean verifierChart(String chart){
        boolean verifier=false;
        if(chart.equalsIgnoreCase("barchart") ||  chart.equalsIgnoreCase("areachart") ||  chart.equalsIgnoreCase("donutchart") ||  chart.equalsIgnoreCase("linechart") ||  chart.equalsIgnoreCase("piechart")){
            verifier=true;
        }
        return verifier;
    }
    
    public Charts(String type) throws Exception{
        this.setType(type);
    }

    public Charts(){
        
    }

    public boolean isScaleShowGridLines() {
        return scaleShowGridLines;
    }
    
    /**
     * rendre la grille visible ou non
     * @param scaleShowGridLines  
     */
    public void setScaleShowGridLines(boolean scaleShowGridLines) {
        this.scaleShowGridLines = scaleShowGridLines;
    }

    public int getScaleGridLineWidth() {
        return scaleGridLineWidth;
    }
    /**
     * taille de la grille si c'est visible
     * la taille par defaut est de 1
     * @param scaleGridLineWidth 
     */
    public void setScaleGridLineWidth(int scaleGridLineWidth) {
        this.scaleGridLineWidth = scaleGridLineWidth;
    }

    public String getScaleGridLineColor() {
        return scaleGridLineColor;
    }

    /**
     * couleur de la grille si c'est visible
     * @param scaleGridLineColor 
     */
    public void setScaleGridLineColor(String scaleGridLineColor) {
        this.scaleGridLineColor = scaleGridLineColor;
    }

    public boolean isScaleShowHorizontalLines() {
        return scaleShowHorizontalLines;
    }
    /**
     * rendre l'axe horirentale visible ou non
     * @param scaleShowHorizontalLines 
     */
    public void setScaleShowHorizontalLines(boolean scaleShowHorizontalLines) {
        this.scaleShowHorizontalLines = scaleShowHorizontalLines;
    }

    public boolean isScaleShowVerticalLines() {
        return scaleShowVerticalLines;
    }
    
    /**
     * rendre l'axe verticale visible ou non
     * @param scaleShowVerticalLines 
     */
    public void setScaleShowVerticalLines(boolean scaleShowVerticalLines) {
        this.scaleShowVerticalLines = scaleShowVerticalLines;
    }
    
    public int getBarValueSpacing() {
        return barValueSpacing;
    }
    
    /**
     * modifier l'espace entre les par s'il s'agit d'un barchart
     * @param barValueSpacing 
     */
    public void setBarValueSpacing(int barValueSpacing) {
        this.barValueSpacing = barValueSpacing;
    }
    
    public int getBarDatasetSpacing() {
        return barDatasetSpacing;
    }

    /**
     * modifier l'espace entre les difference donneé  s'il s'agit d'un barchart
     * @return 
     */
    public void setBarDatasetSpacing(int barDatasetSpacing) {
        this.barDatasetSpacing = barDatasetSpacing;
    }

    public boolean isResponsive() {
        return responsive;
    }
    
    /**
     * s'il il faut rendre le graphique reactif (respensive)
     * @param responsive 
     */
    private void setResponsive(boolean responsive) {
        this.responsive = responsive;
    }

    public boolean isPointDot() {
        return pointDot;
    }
    
    /**
     * rendre le point visible s'il s'agit de linechart ou areachart
     * la valeur par defaut est false
     * @param pointDot 
     */
    public void setPointDot(boolean pointDot) {
        this.pointDot = pointDot;
    }

    public int getPointDotRadius() {
        return pointDotRadius;
    }
    
    /**
     * modifier le bordure   du point si c'est visible , s'il s'agit de linechart ou areachart
     * la valeur par default est 4
     * @param pointDotRadius 
     */
    public void setPointDotRadius(int pointDotRadius) {
        this.pointDotRadius = pointDotRadius;
    }

    public String getType() {
        return type;
    }
    
    /**
     * modifier le type de chart qui est par defaut le barchart
     * @param type 
     * @throws java.lang.Exception  si le chart choisi n'est pas dans la liste suivante :
     * barchart , linechart,areachart,piechart,donutchart
     */
    public void setType(String type) throws Exception {
        if(!verifierChart(type)){
          throw new Exception("Chart invalide") ; 
        }
        this.type = type;
    }

    public String getLienOnClick() {
        return lienOnClick;
    }


    public void setLienOnClick(String lienOnClick) {
        this.lienOnClick = lienOnClick;
    }


    public String getNomVariable() {
        return nomVariable;
    }


    public void setNomVariable(String nomVariable) {
        this.nomVariable = nomVariable;
    }


    public String getLibelleOnClick() {
        return libelleOnClick;
    }


    public void setLibelleOnClick(String libelleOnClick) {
        this.libelleOnClick = libelleOnClick;
    }

    public String onClickOptions(){
        if(this.lienOnClick!=null){
            String option = "onClick: function(e) {\n" +
                "var bar = this.getElementAtEvent(e)[0];\n" +
                "var index = bar._index;\n"+
                "window.location.href =`"+ lienOnClick + "&"+libelleOnClick+
                "=${"+ nomVariable+"[index]}`;"
                +"},";
            return option;
        }
        return "";
    }

}
