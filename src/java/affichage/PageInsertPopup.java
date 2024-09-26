package affichage;

import javax.servlet.http.HttpServletRequest;

import bean.ClassMAPTable;

public class PageInsertPopup extends PageInsert {
    protected String champReturn;
    protected String champUrl;

    public String getChampReturn() {
        return champReturn;
    }
    public void setChampReturn(String champReturn) {
        this.champReturn = champReturn;
    }
    public String getChampUrl() {
        return champUrl;
    }
    public void setChampUrl(String champUrl) {
        this.champUrl = champUrl;
    }

    public PageInsertPopup(ClassMAPTable o, HttpServletRequest req, user.UserEJB u) throws Exception {
        super(o,req,u);
    }
    
    @Override
    public void makeFormulaire() throws Exception {
        super.makeFormulaire();
        setChampReturn(getReq().getParameter("champReturn"));
        setChampUrl(getReq().getParameter("champUrl"));
    }

    public String getHtmlAddOnPopup() {
        String rep = "";
        rep += "<input type=\"hidden\" name=\"champReturn\" value=\""+this.champReturn+"\" />";
        rep += "<input type=\"hidden\" name=\"champUrl\" value=\""+this.champUrl+"\" />";
        rep += "<input type=\"hidden\" name=\"rajoutLien\" value=\"champReturn-champUrl"+this.getRajoutLienAPartirChampUrl()+"\" />";
        return rep;
    }

    private String getRajoutLienAPartirChampUrl() {
        String[] champs = this.champUrl.split(";");
        if(champs[0].compareToIgnoreCase(champs[1])!=0){
            return "-"+champs[1];
        }
        return "";
    }
}
