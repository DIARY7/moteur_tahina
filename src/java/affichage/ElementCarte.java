/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package affichage;

/**
 * Cette classe fournit du html générique nécessaire pour l'affichage d'une carte
 * <h3>Exemple d'utilistion</h3>
 * <h5>Pour une creation du map avec position : </h5>
 * <pre>
 * public String getHtml(){        
        String retour=ElementCarte.getElementBase();
        if(isIsCreate()){
            return retour+ElementCarte.getElementCreate()+ElementCarte.getPosition;
        }
        return retour+this.legende+ElementCarte.printable;
    }

 * </pre>
 * @author BICI
 */
public class ElementCarte {
    private static String elementCreate="<script>map.addControl(new L.Control.Draw({\n" +
        "        edit: {\n" +
        "            featureGroup: drawnItems,\n" +
        "            poly: {\n" +
        "                allowIntersection: false\n" +
        "            }\n" +
        "        },\n" +
        "        draw: {\n" +
        "            polygon: {\n" +
        "                allowIntersection: false,\n" +
        "                showArea: true\n" +
        "            }\n" +
        "        }\n" +
        "    }));\n" +
        "\n" +
        "    map.on(L.Draw.Event.CREATED, function (event) {\n" +
        "        var layer = event.layer;\n" +
        "\n" +
        "        drawnItems.addLayer(layer);\n" +
        "    });"
            
            + "</script>";

    private static String elementBaseEssai=""+
                "		<input id=\"1\" name=\"1\" value=\"-18.9798776\" type=\"hidden\">\n" +
"		<input id=\"2\" name=\"2\" value=\"47.5327563\" type=\"hidden\">"+
"			<div id=\"mapid\"></div>	\n" +
"		\n"+
              "<script>\n" +  "var map=L.map('mapid');"+
        "setVueMap(document.getElementById('1').value,document.getElementById('2').value,map); "  + 
        "function setVueMap(latti,longi,map){"+    
"	map.setView([latti,longi], 16);\n" +
"\n" +
"	L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {\n" +
"	attribution: 'Map data &copy; <a href=\"https://www.openstreetmap.org/\">OpenStreetMap</a> contributors, <a href=\"https://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>, Imagery © <a href=\"https://www.mapbox.com/\">Mapbox</a>',\n" +
"	maxZoom: 18,\n" +
"	id: 'mapbox.streets',\n" +
"	accessToken: 'pk.eyJ1IjoibWV2YTEyNTgiLCJhIjoiY2p6djRudzU2MDB6bTNwbXI0M3BlcXNjMyJ9.Knx_B_WAZUJyu8Mzv97jag'\n" +
"	}).addTo(map);\n" +
"	var osmUrl = 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',\n" +
"            osmAttrib = '&copy; <a href=\"http://openstreetmap.org/copyright\">OpenStreetMap</a> contributors',\n" +
"            osm = L.tileLayer(osmUrl, { maxZoom: 18, attribution: osmAttrib }),\n" +
"            drawnItems = L.featureGroup().addTo(map);\n" +
"    L.control.layers({\n" +
"        'osm': osm.addTo(map),\n" +
"        \"google\": L.tileLayer('http://www.google.cn/maps/vt?lyrs=s@189&gl=cn&x={x}&y={y}&z={z}', {\n" +
"            attribution: 'google'\n" +
"        })\n" +
"    }, { 'drawlayer': drawnItems }, { position: 'topleft', collapsed: false }).addTo(map);\n" +
      "}"+        
"</script>";
    
    private static String elementBase="<div class=\"row\">\n" + "<div class=\"col-md-12\">"+
            
"			<div id=\"mapid\"></div></div>	\n" +
"		</div>\n"+
              "<script>\n" +
"	var map = L.map('mapid').setView([-18.9987355,47.5301833], 15);\n  var circle=[];var marker=[];var polyline=[];" +
"\n" +
"	var tiles=L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {\n" +
"	attribution: 'Map data &copy; <a href=\"https://www.openstreetmap.org/\">OpenStreetMap</a> contributors, <a href=\"https://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>, Imagery © <a href=\"https://www.mapbox.com/\">Mapbox</a>',\n" +
"	maxZoom: 18,\n" +
"	id: 'mapbox.streets',\n" +
"	accessToken: 'pk.eyJ1IjoibWV2YTEyNTgiLCJhIjoiY2p6djRudzU2MDB6bTNwbXI0M3BlcXNjMyJ9.Knx_B_WAZUJyu8Mzv97jag'\n" +
"	}).addTo(map);\n" +
"	var osmUrl = 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',\n" +
"            osmAttrib = '&copy; <a href=\"http://openstreetmap.org/copyright\">OpenStreetMap</a> contributors',\n" +
"            osm = L.tileLayer(osmUrl, { maxZoom: 18, attribution: osmAttrib }),\n" +
"            drawnItems = L.featureGroup().addTo(map);\n" +
"    L.control.layers({\n" +
"        'osm': osm.addTo(map),\n" +
"        \"google\": L.tileLayer('http://www.google.cn/maps/vt?lyrs=s@189&gl=cn&x={x}&y={y}&z={z}', {\n" +
"            attribution: 'google'\n" +
"        })\n" +
"    }, { 'drawlayer': drawnItems }, { position: 'topleft', collapsed: false }).addTo(map);\n" +
"\n" +  
"</script>";

    public static String clickInfo="<script>"
                + "	function onMapClick(e) {\n" +
"			dialog.open();\n" +
"	}\n" +
"	map.on('click', onMapClick);\n" 
                + "</script>";

    public static String showModal="<script>\n" +
"   dialog.open();"+
"    </script>";

    public static String getPosition="<script>\n" +
"map.on(L.Draw.Event.CREATED,function(e){\n" +
"           var layer=e.layer; var type = e.layerType;\n" +
"           drawnItems.addLayer(layer);\n" +
            "console.log(type);"+
            "if(type === 'circle'){"+
                "var result=\"(\"+layer.getLatLng().lat+\" \"+layer.getLatLng().lng+\",\"+layer.getRadius()+\")\";"+
                "result=\"CIRCLE\"+result;"+ "console.log(result);"+
                "$('#geom').val(result);"+
            "}"+
            "if(type === 'polyline'){"+
                "var array=layer.getLatLngs();"+
                "var result=\"(\";"+
                "for(var i=0;i<array.length;i++){"+
                    "if(i===array.length-1){"+
                    "   result+=array[i].lat+\" \"+array[i].lng+\")\";"+
                    "}else{"+
                "   result+=array[i].lat+\" \"+array[i].lng+\",\";"+
                    "}}"+
                "result=\"LINESTRING\"+result;"+
                "console.log(result);"+
                "$('#geom').val(result);"+
            "}if (type === 'rectangle' || type === 'polygon' ) {\n" +               
                "var array=layer.getLatLngs();"+
                "var first=\"\";"+
                "var result=\"(\";"+
                "for(var i=0;i<array[0].length;i++){"+
                "if(i===array[0].length-1){"+
                "   result+=array[0][i].lat+\" \"+array[0][i].lng+\",\"+first+\")\";"+
                "}else{"+
                    "if(i===0){first= array[0][i].lat+\" \"+array[0][i].lng;}"+
                "   result+=array[0][i].lat+\" \"+array[0][i].lng+\",\";"+
                "}}"+
                "result=\"POLYGON(\"+result+\")\";"+
                "$('#geom').val(result);"+
            "}if(type === 'marker' || type === 'circlemarker'){"+
                "var point=\"POINT(\"+layer.getLatLng().lat+\" \"+layer.getLatLng().lng+\")\";"+
                "$('#geom').val(point);"+
            "}"+
            
"        });" +
            
"    </script>";

   public static String legende="<script>var legend = L.control({ position: \"bottomright\" });\n" +
"			legend.onAdd = function(mymap) {\n" +
"				var div = L.DomUtil.create(\"div\", \"legend\");\n" +
"				div.innerHTML += \"<h4>L&eacute;gendes</h4>\";\n" +
"				div.innerHTML += '<i style=\"background: red;\"></i><span>Incident</span><br>';\n" +
"				div.innerHTML += '<i class=\"icon\" style=\"background-image: url(https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.5.1/images/marker-icon.png);background-repeat: no-repeat;\"></i><span>Personne</span><br>';\n" +
"				return div;\n" +
"			}\n" +
"			  \n" +
"			legend.addTo(map);</script>";

    public static String printable="<script>L.easyPrint({\n" +
"      tileLayer: tiles,\n" +
"      sizeModes: ['CurrentSize', 'A4Landscape', 'A4Portrait'],\n" +
"      filename: 'myMap'\n" +
"		}).addTo(map);</script>";
    
    public static String dialog="<script>var dialog = L.control.dialog(options)\n" +
"              .setContent(\"<p>Hello! Welcome to your nice new dialog box!</p>\")\n" +
"              .addTo(map);dialog.open();</script>";

    /**
     * 
     * @return
     */
    public static String getElementBaseEssai() {
        return elementBaseEssai;
    }
    

    /**
     * Générer un code javascript pour avoir la position sur le map
     * @return un code javascript
     */
    public static String getGetPosition() {
        return getPosition;
    }
    
    /**
     * Générer un code javascript pour l'apparation du modal 
     * @return  javascript 
     */
    public static String getShowModal() {
        return showModal;
    }
    
    /**
     * générer une fonction javascript sert à ouvrir une dialoge sur le map
     * @return la fonction javascript
     */
    public static String getClickInfo() {
        return clickInfo;
    }
    
    /**
     * générer une  javascript pour la création du map
     * @return javascript
     */
    public static String getElementBase() {
        return elementBase;
    }
    
    /**
     * générer une  javascript pour la création des élements du map
     * @return javascript
     */
    public static String getElementCreate() {
        return elementCreate;
    }
    
    
}
