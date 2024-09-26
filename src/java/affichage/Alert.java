
package affichage;

/**
 * Affichage pour créer une alerte (message d'erreur, message de succès)
 * 
 * @author BICI
 */
public class Alert {
    private String html;
    /**
     * Construire une alerte
     * @param message message de l'alerte
     */
    public Alert(String message){
        String header = " <script src=\"/cnaps-war/plugins/jQuery/jQuery-2.1.4.min.js\"></script>\n"
                        +"<script src=\"/cnaps-war/bootstrap/js/bootstrap.min.js\"></script>\n"
                        +"<link href=\"/cnaps-war/bootstrap/css/bootstrap.min.css\" rel=\"stylesheet\" />"
                        +"<script>\n" 
                        +"function fermer(){\n" 
                        +"history.back();\n" 
                        +"};\n" 
                        +"</script>"+
                        "<div class=\"modal fade\" id=\"modalalert\">\n" +
                        "<div class=\"modal-dialog modal-sm\">\n" + 
                        "<div class=\"modal-content\">\n" +
                        "<div class=\"modal-header\" style=\"color: red\">\n" +
                        "<h4 class=\"modal-title\"><span class=\"glyphicon glyphicon-alert\"></span> Attention :</h4>\n" +
                        "</div>\n" +
                        "<div class=\"modal-body\">\n" +
                        "<h4 id=\"erreurcontent\"></h4>\n" +
                        "</div>\n" +
                        "<div class=\"modal-footer\">\n" +
                        "<button class=\"btn btn-danger\" onclick=\"fermer()\">OK</button> \n" +
                        "</div>\n" +
                        "</div><!-- /.modal-content -->\n" +
                        "</div><!-- /.modal-dialog -->\n" +
                        "</div><!-- /.modal -->\n"
                        +"<script language=\"JavaScript\"> "
                        +"document.getElementById(\"erreurcontent\").innerHTML='"+message+"';"
                        +"$('#modalalert').modal('show');"
                        + "</script>";
        this.setHtml(header);
    }

    public Alert() {
    }
    public String getHtml(){
        return this.html;
    }
    public String getSuccesAlert(String message){
        String header = " <script src=\"/cnaps-war/plugins/jQuery/jQuery-2.1.4.min.js\"></script>\n"
                        +"<script src=\"/cnaps-war/bootstrap/js/bootstrap.min.js\"></script>\n"
                        +"<link href=\"/cnaps-war/bootstrap/css/bootstrap.min.css\" rel=\"stylesheet\" />"
                        +"<script>\n" 
                        +"function fermer(){\n" 
                        +"history.back();\n" 
                        +"};\n" 
                        +"</script>"+
                        "<div class=\"modal fade\" id=\"modalalert\">\n" +
                        "<div class=\"modal-dialog modal-sm\">\n" + 
                        "<div class=\"modal-content\">\n" +
                        "<div class=\"modal-header\" style=\"color: red\">\n" +
                        "<h4 class=\"modal-title\"><span class=\"glyphicon glyphicon-succes\"></span> Merci :</h4>\n" +
                        "</div>\n" +
                        "<div class=\"modal-body\">\n" +
                        "<h4 id=\"erreurcontent\"></h4>\n" +
                        "</div>\n" +
                        "<div class=\"modal-footer\">\n" +
                        "<button class=\"btn btn-primary\" onclick=\"fermer()\">OK</button> \n" +
                        "</div>\n" +
                        "</div><!-- /.modal-content -->\n" +
                        "</div><!-- /.modal-dialog -->\n" +
                        "</div><!-- /.modal -->\n"
                        +"<script language=\"JavaScript\"> "
                        +"document.getElementById(\"erreurcontent\").innerHTML='"+message+"';"
                        +"$('#modalalert').modal('show');"
                        + "</script>";
        return header;
    }
    public void setHtml(String ht){
        this.html = ht;
    }
}
