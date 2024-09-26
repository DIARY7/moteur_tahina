/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package historique;

public class MapUtilisateurServiceDirection extends MapUtilisateur{
    String service;
    String direction;

    public MapUtilisateurServiceDirection(int refus, String loginus, String pwdus, String nomus, String adrus, String telus, String idrol, String service, String direction) throws Exception{
        super(refus, loginus, pwdus, nomus, adrus, telus, idrol);
        this.setService(service);
        this.setDirection(direction);
    }    
    
    public MapUtilisateurServiceDirection(){
        super.setNomTable("utilisateurrole");
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
    
    public String getTuppleID() {
        //return String.valueOf(getRefuser()+"/"+getPwduser());
        return String.valueOf(getRefuser());
    }
    
}
