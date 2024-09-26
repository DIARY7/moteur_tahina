/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package affichage;

import bean.ClassMAPTable;
import java.lang.reflect.Field;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;

/**
 *
 * @author andya
 */
public class PageUpload extends PageInsert {

    protected HashMap<String, String> valeurFromFileUpload;
    protected List<FileItem> fileItems;

    public PageUpload() {
    }

    public PageUpload(ClassMAPTable o, HttpServletRequest req, user.UserEJB u) throws Exception {
        setBase(o);
        setReq(req);
        setUtilisateur(u);
        makeFormulaire();
    }

    public PageUpload(ClassMAPTable o, HttpServletRequest req) throws Exception {
        setBase(o);
        setReq(req);
    }

    public PageUpload(DiskFileItem[] items) throws Exception {
        init(items);
        setBase(getInitiale());
    }

    public HashMap<String, String> getValeurFromFileUpload() {
        return valeurFromFileUpload;
    }

    public List<FileItem> getFileItems() {
        return fileItems;
    }

    protected ClassMAPTable getInitiale() throws Exception {
        String classe = getValeur("classe");
        return (ClassMAPTable) Class.forName(classe).newInstance();
    }

    public String getValeur(String nom) throws Exception {
        return valeurFromFileUpload.get(nom);
    }

    private void init(DiskFileItem[] items) throws Exception {
        valeurFromFileUpload = new HashMap();
        fileItems = new ArrayList<>();
        for (FileItem item : items) {
            if (item.isFormField()) {
                valeurFromFileUpload.put(item.getFieldName(), item.getString("UTF-8"));
            } else {
                fileItems.add(item);
            }
        }
    }

    public ClassMAPTable getObjectAvecValeur() throws Exception {
        try {
            if (valeurFromFileUpload == null) {
                return super.getObjectAvecValeur();
            }
            Field[] tempChamp = bean.ListeColonneTable.getFieldListeHeritage(getBase());
            for (int i = 0; i < tempChamp.length; i++) {
                Field f = tempChamp[i];
                String valeur = getValeur(f.getName());
                if (valeur == null) {
                    continue;
                }
                getBase().setMode("modif");
                if (f.getType().getName().compareToIgnoreCase("java.lang.String") == 0) {
                    bean.CGenUtil.setValChamp(getBase(), f, valeur);
                }
                if (f.getType().getName().compareToIgnoreCase("java.sql.Date") == 0) {
                    bean.CGenUtil.setValChamp(getBase(), f, utilitaire.Utilitaire.string_date("dd/MM/yyyy", valeur));
                }
                if (f.getType().getName().compareToIgnoreCase("java.lang.Double") == 0) {
                    bean.CGenUtil.setValChamp(getBase(), f, new Double(utilitaire.Utilitaire.stringToDouble(valeur)));

                }
                if (f.getType().getName().compareToIgnoreCase("java.lang.Integer") == 0) {
                    if (valeur == null || valeur.compareToIgnoreCase("") == 0) {
                        bean.CGenUtil.setValChamp(getBase(), f, new Integer(0));
                    } else {
                        bean.CGenUtil.setValChamp(getBase(), f, new Integer(valeur));
                    }
                }
                if (f.getType().getName().compareToIgnoreCase("float") == 0) {
                    bean.CGenUtil.setValChamp(getBase(), f, new Float(utilitaire.Utilitaire.stringToFloat(valeur)));
                }
                if (f.getType().getName().compareToIgnoreCase("double") == 0) {
                    bean.CGenUtil.setValChamp(getBase(), f, new Double(utilitaire.Utilitaire.stringToDouble(valeur)));
                }
                if (f.getType().getName().compareToIgnoreCase("int") == 0) {
                    bean.CGenUtil.setValChamp(getBase(), f, new Integer(utilitaire.Utilitaire.stringToInt(valeur)));
                }
                if (f.getType().getName().compareToIgnoreCase("java.sql.Time") == 0) {
                    String[] val = valeur.split(":");
                    bean.CGenUtil.setValChamp(getBase(), f,
                            new Time(Integer.parseInt(val[0]), Integer.parseInt(val[1]), Integer.parseInt(val[2])));
                }

            }
            String nomTable = getValeur("nomtable");
            if (nomTable != null) {
                getBase().setNomTable(nomTable);
            }
            return getBase();
        } catch (NumberFormatException n) {
            throw new Exception("format de nombre invalide");
        } catch (Exception e) {
            throw e;
        }
    }
}
