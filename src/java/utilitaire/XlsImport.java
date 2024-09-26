package utilitaire;

import bean.Champ;
import bean.CGenUtil;
import bean.ClassEtat;
import bean.ClassMAPTable;
import bean.ListeColonneTable;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import user.UserEJBBean;
/**
 * Utilitaire pour les importations XLS et le mapping en objet de mapping
 * <p>
 *    Par exemple on a telechargé un fichier sur <code>/tmp/import.xls</code> et on sait que ce fichier
 *    c'est un import d'objet <code>Client</code>, on a le code suivant :
 * </p>
 * <pre>
 *  String url = "/temp/import.xls";
 *  XlsImport.importerAvecId(url, new Client());
 * </pre>
 * <p>Cela va mapper les colonnes de base de données en ordre au numéro de colonnes et faire une insertion. </p>
 * @Author BICI
 */
public class XlsImport {

    private static Workbook workbook;
    private static Sheet sheet;

    /**
     * Sert à faire une importation d'un fichier excel avec ID à partir d'un fichier et d'une {@link bean.ClassMAPTable}
     * @param fileUrl le chemin ou le fichier se trouve
     * @param o objet de mapping à enregistrer
     * @throws Exception
     */
    public static void importerAvecId(String fileUrl, ClassMAPTable o) throws Exception {
        Connection conn = null;
        try {
            conn = new UtilDB().GetConn();
            workbook = WorkbookFactory.create(new File(fileUrl));
            sheet = workbook.getSheetAt(0);
            int indice = 1; // miala ny indice 0 satria titre
            Row row = sheet.getRow(indice);
            while (row != null) {
                int cellNumber = row.getPhysicalNumberOfCells();
                Champ[] listeChamps = ListeColonneTable.getFromListe(o, null);
                if (cellNumber != listeChamps.length - 1) {
                    throw new Exception("erreur");
                }
                for (int co = 0; co < cellNumber; co++) {
                    Cell c = row.getCell(co);
                    if (listeChamps[co + 1].getTypeJava().equals("java.lang.String")) {
                        String valeur = "";
                        try {
                            valeur = c.getStringCellValue();
                        } catch (Exception ex) {
                            try {
                                valeur = String.valueOf((int) c.getNumericCellValue());
                            } catch (Exception e) {
                                valeur = Utilitaire.format(c.getDateCellValue());
                            }
                        }
                        CGenUtil.setValChamp(o, listeChamps[co + 1].getNomClasse(), valeur);
                    } else if (listeChamps[co + 1].getTypeJava().equals("int")) {
                        CGenUtil.setValChamp(o, listeChamps[co + 1].getNomClasse(), new Integer((int) c.getNumericCellValue()));
                    } else if (listeChamps[co + 1].getTypeJava().equals("double")) {
                        CGenUtil.setValChamp(o, listeChamps[co + 1].getNomClasse(), new Double(c.getNumericCellValue()));
                    } else if (listeChamps[co + 1].getTypeJava().equals("java.sql.Date")) {
                        CGenUtil.setValChamp(o, listeChamps[co + 1].getNomClasse(), new Date(c.getDateCellValue().getTime()));
                    }
                }
                o.construirePK(conn);
                o.insertToTable();
                indice++;
                row = sheet.getRow(indice);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Erreur lors de l'import du fichier Excel");
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * Sert à faire une importation d'un fichier excel à partir d'un fichier et d'une {@link bean.ClassMAPTable}
     * <p>La feuille à importer est la feuille numero 0</p>
     * @param fileUrl le chemin ou le fichier se trouve
     * @param o objet de mapping à enregistrer
     * @throws Exception
     */
    public static void importerTous(String fileUrl, ClassMAPTable o) throws Exception {
        try {
            workbook = WorkbookFactory.create(new File(fileUrl));
            sheet = workbook.getSheetAt(0);
            int indice = 1; // miala ny indice 0 satria titre
            Row row = sheet.getRow(indice);
            while (row != null) {
                int cellNumber = row.getPhysicalNumberOfCells();
                Champ[] listeChamps = ListeColonneTable.getFromListe(o, null);
                if (cellNumber != listeChamps.length) {
                    throw new Exception("erreur");
                }
                for (int co = 0; co < cellNumber; co++) {
                    Cell c = row.getCell(co);
                    if (listeChamps[co].getTypeJava().equals("java.lang.String")) {
                        String valeur = "";
                        try {
                            valeur = c.getStringCellValue();
                        } catch (Exception ex) {
                            try {
                                valeur = String.valueOf((int) c.getNumericCellValue());
                            } catch (Exception e) {
                                valeur = Utilitaire.format(c.getDateCellValue());
                            }
                        }
                        CGenUtil.setValChamp(o, listeChamps[co].getNomClasse(), valeur);
                    } else if (listeChamps[co].getTypeJava().equals("int")) {
                        CGenUtil.setValChamp(o, listeChamps[co].getNomClasse(), new Integer((int) c.getNumericCellValue()));
                    } else if (listeChamps[co].getTypeJava().equals("double")) {
                        CGenUtil.setValChamp(o, listeChamps[co].getNomClasse(), new Double(c.getNumericCellValue()));
                    } else if (listeChamps[co].getTypeJava().equals("java.sql.Date")) {
                        CGenUtil.setValChamp(o, listeChamps[co].getNomClasse(), new Date(c.getDateCellValue().getTime()));
                    }
                }
                o.insertToTable();
                indice++;
                row = sheet.getRow(indice);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Erreur lors de l'import du fichier Excel");
        }
    }

    /**
     * Impoter avec Id un fichier excel et setter les valeurs du mapping o 
      * @param fileUrl chemin ou se trouve le fichier à exporter
     * @param o objet de mapping à enregistrer
     * @param sheetAt numéro de la feuille à importer
     * @throws Exception
     */
    public static void importerAvecId(String fileUrl, ClassMAPTable o, String sheetAt) throws Exception {
        try {
            Connection conn = new UtilDB().GetConn();
            workbook = WorkbookFactory.create(new File(fileUrl));
            sheet = workbook.getSheet(sheetAt);
            int indice = 1; // miala ny indice 0 satria titre
            Row row = sheet.getRow(indice);
            while (row != null) {
                int cellNumber = row.getPhysicalNumberOfCells();
                Champ[] listeChamps = ListeColonneTable.getFromListe(o, null);
                if (cellNumber != listeChamps.length - 1) {
                    throw new Exception("erreur");
                }
                for (int co = 0; co < cellNumber; co++) {
                    Cell c = row.getCell(co);
                    if (listeChamps[co + 1].getTypeJava().equals("java.lang.String")) {
                        String valeur = "";
                        try {
                            valeur = c.getStringCellValue();
                        } catch (Exception ex) {
                            try {
                                valeur = String.valueOf((int) c.getNumericCellValue());
                            } catch (Exception e) {
                                valeur = Utilitaire.format(c.getDateCellValue());
                            }
                        }
                        CGenUtil.setValChamp(o, listeChamps[co + 1].getNomClasse(), valeur);
                    } else if (listeChamps[co + 1].getTypeJava().equals("int")) {
                        CGenUtil.setValChamp(o, listeChamps[co + 1].getNomClasse(), new Integer((int) c.getNumericCellValue()));
                    } else if (listeChamps[co + 1].getTypeJava().equals("double")) {
                        CGenUtil.setValChamp(o, listeChamps[co + 1].getNomClasse(), new Double(c.getNumericCellValue()));
                    } else if (listeChamps[co + 1].getTypeJava().equals("java.sql.Date")) {
                        CGenUtil.setValChamp(o, listeChamps[co + 1].getNomClasse(), new Date(c.getDateCellValue().getTime()));
                    }
                }
                o.construirePK(conn);
                o.insertToTable();
                indice++;
                row = sheet.getRow(indice);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Erreur lors de l'import du fichier Excel");
        }
    }

    /**
    * Impoter tous le contenu d' un fichier excel et setter les valeurs du mapping o 
    * @param fileUrl chemin ou se trouve le fichier à importer
    * @param o objet de mapping à enregistrer
    * @param sheetAt numéro de la feuille à importer
    * @throws Exception
    */
    public static void importerTous(String fileUrl, ClassMAPTable o, String sheetAt) throws Exception {
        try {
            workbook = WorkbookFactory.create(new File(fileUrl));
            sheet = workbook.getSheet(sheetAt);
            int indice = 1; // miala ny indice 0 satria titre
            Row row = sheet.getRow(indice);
            while (row != null) {
                int cellNumber = row.getPhysicalNumberOfCells();
                Champ[] listeChamps = ListeColonneTable.getFromListe(o, null);
                if (cellNumber != listeChamps.length) {
                    throw new Exception("erreur");
                }
                for (int co = 0; co < cellNumber; co++) {
                    Cell c = row.getCell(co);
                    if (listeChamps[co].getTypeJava().equals("java.lang.String")) {
                        String valeur = "";
                        try {
                            valeur = c.getStringCellValue();
                        } catch (Exception ex) {
                            try {
                                valeur = String.valueOf((int) c.getNumericCellValue());
                            } catch (Exception e) {
                                valeur = Utilitaire.format(c.getDateCellValue());
                            }
                        }
                        CGenUtil.setValChamp(o, listeChamps[co].getNomClasse(), valeur);
                    } else if (listeChamps[co].getTypeJava().equals("int")) {
                        CGenUtil.setValChamp(o, listeChamps[co].getNomClasse(), new Integer((int) c.getNumericCellValue()));
                    } else if (listeChamps[co].getTypeJava().equals("double")) {
                        CGenUtil.setValChamp(o, listeChamps[co].getNomClasse(), new Double(c.getNumericCellValue()));
                    } else if (listeChamps[co].getTypeJava().equals("java.sql.Date")) {
                        CGenUtil.setValChamp(o, listeChamps[co].getNomClasse(), new Date(c.getDateCellValue().getTime()));
                    }
                }
                o.insertToTable();
                indice++;
                row = sheet.getRow(indice);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Erreur lors de l'import du fichier Excel");
        }
    }

    /**
     * Setter les champs dans le ClassMAPTable d'apres le fichier Excel
     * @param fileUrl chemin ou se trouve le fichier à importer
     * @param o  objet vide de la classe à importer
     * @return tableau d'objet de mapping avec des valeurs
     * @throws Exception
     */
    public static ClassMAPTable[] toObject(String fileUrl, ClassMAPTable o) throws Exception {

        ClassMAPTable[] rst = null;
        ArrayList<Object> ob = new ArrayList<>();
        Class c = Class.forName(o.getClassName());
        Method method;
        Field[] champs = c.getDeclaredFields();

        workbook = WorkbookFactory.create(new File(fileUrl));
        sheet = workbook.getSheetAt(0);
        int indice = 1; // miala ny indice 0 satria titre
        Row row = sheet.getRow(indice);
        rst = new ClassMAPTable[sheet.getPhysicalNumberOfRows() - 1];
        int ind = 0;
        while (row != null) {
            System.out.println("niditra ato a = " + row);
            Object obj = c.newInstance();
            int cellNumber = row.getPhysicalNumberOfCells();
            Object value = null;
            if (cellNumber != champs.length) {
                throw new Exception("erreur import, colonnes non synchronis�s");
            }
            for (int co = 0; co < champs.length; co++) {
                method = obj.getClass().getMethod("set" + Utilitaire.convertDebutMajuscule(champs[co].getName()), champs[co].getType());
                if (champs[co].getType().getName().contains("String")) {
                    try {
                        method.invoke(obj, row.getCell(co).getStringCellValue());
                    } catch (Exception e) {
                        try {
                            method.invoke(obj, String.valueOf((double) row.getCell(co).getNumericCellValue()));
                        } catch (Exception exp) {
                            try {
                                method.invoke(obj, String.valueOf((int) row.getCell(co).getNumericCellValue()));
                            } catch (Exception excep) {
                                method.invoke(obj, String.valueOf(row.getCell(co).getDateCellValue()));
                            }

                        }
                    }
                } else if (champs[co].getType().getName().contains("int")) {
                    method.invoke(obj, row.getCell(co).getNumericCellValue());
                } else if (champs[co].getType().getName().contains("double")) {
                    method.invoke(obj, row.getCell(co).getNumericCellValue());
                } else if (champs[co].getType().getName().contains("Date")) {
                    method.invoke(obj, new java.sql.Date(row.getCell(co).getDateCellValue().getTime()));
                }
            }
            ob.add(obj);
            ind++;
            indice++;
            row = sheet.getRow(indice);
        }
        rst = (ClassMAPTable[]) ob.toArray();
        return rst;
    }

    /**
     * Permet d'obtenir un tableau d'objets qui contient tous les
     * objets nouvellement créés à partir des données lues dans le flux d'entrée représentant un excel
     * @param is  flux d'entrée IO representant un fichier excel 
     * @param o contient des informations sur la classe à instancier pour chaque objet dans le tableau résultant
     * @return liste d'objets de mapping avec valeur
     * @throws Exception
     */
    public static Object[] toObject(InputStream is, ClassMAPTable o) throws Exception {
        ClassMAPTable[] rst = null;
        ArrayList<Object> ob = new ArrayList<>();
        Class c = Class.forName(o.getClassName());
        Method method;
        Field[] champs = c.getDeclaredFields();

        workbook = WorkbookFactory.create(is);
        sheet = workbook.getSheetAt(0);
        int indice = 0; // miala ny indice 0 satria titre
        Row row = sheet.getRow(indice);
        rst = new ClassMAPTable[sheet.getPhysicalNumberOfRows() - 1];
        int ind = 0;
        while (row != null) {
            Object obj = c.newInstance();
            int cellNumber = row.getPhysicalNumberOfCells();
            Object value = null;
            System.out.println("CELL : " + cellNumber + " et CHAMPS : " + champs.length);
            for (int co = 0; co < cellNumber; co++) {
                method = obj.getClass().getMethod("set" + Utilitaire.convertDebutMajuscule(champs[co].getName()), champs[co].getType());
                System.out.println("value : " + row.getCell(co));
                if (champs[co].getType().getName().contains("String")) {
                    try {
                        method.invoke(obj, row.getCell(co).getStringCellValue());
                    } catch (Exception e) {
                        try {
                            String stringToPut = String.valueOf(row.getCell(co).getNumericCellValue());
                            if (stringToPut.split(".").length > 1 && Double.valueOf(stringToPut.split(".")[1]) > 0.00) {
                                method.invoke(obj, String.valueOf((double) row.getCell(co).getNumericCellValue()));
                            } else {
                                method.invoke(obj, String.valueOf((int) row.getCell(co).getNumericCellValue()));
                            }
                        } catch (Exception exp) {
                            try {
                                method.invoke(obj, String.valueOf((int) row.getCell(co).getNumericCellValue()));
                            } catch (Exception excep) {
                                method.invoke(obj, String.valueOf(row.getCell(co).getDateCellValue()));
                            }

                        }
                    }
                } else if (champs[co].getType().getName().contains("int")) {
                    method.invoke(obj, row.getCell(co).getNumericCellValue());
                } else if (champs[co].getType().getName().contains("double")) {
                    try {
                        if (row.getCell(co).getStringCellValue() != null && !((String) row.getCell(co).getStringCellValue()).equals("") && ((String) row.getCell(co).getStringCellValue()).contains(".")) {
                            String valeur = (String) row.getCell(co).getStringCellValue();
                            if ("".equals(valeur)) {
                                valeur = "0.0";
                            }
                            System.out.println("valeur = " + valeur);
                            method.invoke(obj, Double.valueOf(valeur));
                        }
                    } catch (Exception e) {
                        method.invoke(obj, row.getCell(co).getNumericCellValue());
                    }
                } else if (champs[co].getType().getName().contains("Date")) {
                    method.invoke(obj, new java.sql.Date(row.getCell(co).getDateCellValue().getTime()));
                }
            }
            ob.add(obj);
            ind++;
            indice++;
            row = sheet.getRow(indice);
        }
        return ob.toArray();
    }

    /**
     * elle renvoie un tableau d'instances d'objets représentant les données .
     * @deprecated la trosième paramètre n'est même pas utilisée dans le corps du fonction 
     * @param is flux d'entrée IO representant un fichier excel
     * @param o qui contient des informations sur la classe à instancier pour chaque objet dans le tableau résultant
     * @param ligneDebut Un entier qui spécifie l'index de la ligne de départ dans les données d'entrée à lire.
     * @return liste d'objet de mappings
     * @throws Exception
     */
    public static Object[] toObject(InputStream is, ClassMAPTable o, int ligneDebut) throws Exception {
        ClassMAPTable[] rst = null;
        ArrayList<Object> ob = new ArrayList<>();
        Class c = Class.forName(o.getClassName());
        Method method;
        Field[] champs = c.getDeclaredFields();

        workbook = WorkbookFactory.create(is);
        sheet = workbook.getSheetAt(0);
        int indice = 0; // miala ny indice 0 satria titre
        Row row = sheet.getRow(indice);
        rst = new ClassMAPTable[sheet.getPhysicalNumberOfRows() - 1];
        int ind = 0;
        while (row != null) {
            Object obj = c.newInstance();
            int cellNumber = row.getPhysicalNumberOfCells();
            Object value = null;
            if (cellNumber != champs.length) {
                throw new Exception("erreur import, colonnes non synchronis�s");
            }
            for (int co = 0; co < champs.length; co++) {
                method = obj.getClass().getMethod("set" + Utilitaire.convertDebutMajuscule(champs[co].getName()), champs[co].getType());
                if (champs[co].getType().getName().contains("String")) {
                    try {
                        method.invoke(obj, row.getCell(co).getStringCellValue());
                    } catch (Exception e) {
                        try {
                            method.invoke(obj, String.valueOf((int) row.getCell(co).getNumericCellValue()));
                        } catch (Exception exp) {
                            method.invoke(obj, String.valueOf(row.getCell(co).getDateCellValue()));
                        }
                    }
                } else if (champs[co].getType().getName().contains("nteger")) {
                    method.invoke(obj, row.getCell(co).getNumericCellValue());
                } else if (champs[co].getType().getName().contains("double")) {
                    method.invoke(obj, row.getCell(co).getNumericCellValue());
                } else if (champs[co].getType().getName().contains("Date")) {
                    method.invoke(obj, new java.sql.Date(row.getCell(co).getDateCellValue().getTime()));
                }
            }
            ob.add(obj);
            ind++;
            indice++;
            row = sheet.getRow(indice);
        }
        return ob.toArray();
    }

    /**
     * Cette fonction permet d'importer des données  un class Etat.
     * On ignore les champs <code>etat</code> et <code>iduser</code>. 
     * Ils sont à mettre à 0.
     * @param is qui est la source de données à lire représentant un fichier excel
     * @param o qui contient des informations sur la classe à instancier pour chaque objet dans le tableau résultant
     * @return tableau de classeEtat representant les données
     * @throws Exception
     */
    public static ClassEtat[] importClassEtatUser(InputStream is, ClassEtat o) throws Exception {
        ClassEtat[] rst = null;
        UserEJBBean u = new UserEJBBean();
        String user = u.getUser().getTuppleID();
        int etat = 1;
        ArrayList<ClassEtat> ob = new ArrayList<>();
        Class c = Class.forName(o.getClassName());
        Method method;
        Field[] champs = c.getDeclaredFields();
        workbook = WorkbookFactory.create(is);
        sheet = workbook.getSheetAt(0);
        int indice = 0; // miala ny indice 0 satria titre
        Row row = sheet.getRow(indice);
        rst = new ClassEtat[sheet.getPhysicalNumberOfRows() - 1];
        int ind = 0;
        while (row != null) {
            Object obj = c.newInstance();
            int cellNumber = row.getPhysicalNumberOfCells();
            Object value = null;
            if ((cellNumber != champs.length) || (cellNumber + 2 != champs.length)) { //  + 2 satria raha tsy misy iduser sy etat ilay excel
                throw new Exception("erreur import, colonnes non synchronis�s");
            }
            for (int co = 0; co < champs.length; co++) {
                if (champs[co].getName().compareToIgnoreCase("etat") == 0 || champs[co].getName().compareToIgnoreCase("iduser") == 0) {
                    continue;
                }
                method = obj.getClass().getMethod("set" + Utilitaire.convertDebutMajuscule(champs[co].getName()), champs[co].getType());
                if (champs[co].getType().getName().contains("String")) {
                    try {
                        method.invoke(obj, row.getCell(co).getStringCellValue());
                    } catch (Exception e) {
                        try {
                            method.invoke(obj, String.valueOf((int) row.getCell(co).getNumericCellValue()));
                        } catch (Exception exp) {
                            method.invoke(obj, String.valueOf(row.getCell(co).getDateCellValue()));
                        }
                    }
                } else if (champs[co].getType().getName().contains("Integer")) {
                    method.invoke(obj, row.getCell(co).getNumericCellValue());
                } else if (champs[co].getType().getName().contains("Double")) {
                    method.invoke(obj, row.getCell(co).getNumericCellValue());
                } else if (champs[co].getType().getName().contains("Date")) {
                    method.invoke(obj, new java.sql.Date(row.getCell(co).getDateCellValue().getTime()));
                }
            }
            ClassEtat cEtat = (ClassEtat) obj;
            cEtat.setEtat(etat);
            cEtat.setIduser(user);
            ob.add(cEtat);
            ind++;
            indice++;
            row = sheet.getRow(indice);
        }
        return rst;
    }

    /**
     *  L'objectif de cette méthode est de lire les données d'un fichier Excel et de les affecter à
     *  des objets d'une classe donnée.
     *  suppose que la première ligne de la feuille Excel contient les noms des champs à mapper,
     *  et utilise la réflexion pour obtenir dynamiquement les champs
     *  de la classe cible pour mapper les données.
     * @param is qui est la source de données à lire
     * @param o qui contient des informations sur la classe à instancier pour chaque objet dans le tableau résultant
     * @return tableau d'Objet
     * @throws Exception
     */
    public static Object[] toObjectChamp(InputStream is, ClassMAPTable o ) throws Exception {
        ClassMAPTable[] rst = null;
        ArrayList<Object> ob = new ArrayList<>();
        Class c = Class.forName(o.getClassName());
        Method method;

        workbook = WorkbookFactory.create(is);
        sheet = workbook.getSheetAt(0);
        int indice = 1; // miala ny indice 0 satria titre
        Row row = sheet.getRow(indice);
        rst = new ClassMAPTable[sheet.getPhysicalNumberOfRows() - 1];
        int ind = 0;
        /* Get Champ */
        Row tmp = sheet.getRow(0);
        int dim = tmp.getPhysicalNumberOfCells();
        String[] lsName = new String[ dim ] ;
        Field[] champs = new Field[dim];
        
        for( int i = 0 ; i < dim ;i++  ){
            System.out.println("Champ ---->" + tmp.getCell(i).getStringCellValue() );
            lsName[i] = tmp.getCell(i).getStringCellValue() + "";
            champs[i] = c.getDeclaredField( tmp.getCell(i).getStringCellValue() );
        }
        while (row != null) {
            System.out.println("indice ------>" + indice );
            Object obj = c.newInstance();
            Object value = null;
            for (int co = 0; co < dim ; co++) {
                method = obj.getClass().getMethod("set" + Utilitaire.convertDebutMajuscule(champs[co].getName()), champs[co].getType());
                if (champs[co].getType().getName().contains("String")) {
                    try {
                        method.invoke(obj, row.getCell(co).getStringCellValue());
                    } catch (Exception e) {
                        try {
                            method.invoke(obj, String.valueOf((int) row.getCell(co).getNumericCellValue()));
                        } catch (Exception exp) {
                            method.invoke(obj, String.valueOf(row.getCell(co).getDateCellValue()));
                        }
                    }
                } else if (champs[co].getType().getName().contains("nteger")) {
                    method.invoke(obj, row.getCell(co).getNumericCellValue());
                } else if (champs[co].getType().getName().contains("double")) {
                    method.invoke(obj, row.getCell(co).getNumericCellValue());
                } else if (champs[co].getType().getName().contains("Date")) {
                    method.invoke(obj, new java.sql.Date(row.getCell(co).getDateCellValue().getTime()));
                }
            }
            ob.add(obj);
            ind++;
            indice++;
            row = sheet.getRow(indice);
        }
        return ob.toArray();
    }

    /**
     * Permet d'obtenir un tableau d'objets représentant les données lues dans le fichier Excel
     * . Contrairement à {@link utilitaire.XlsImport.toObjectChamp#}, cette fonction va ignorer les cellules vides
     * @param is qui est la source de données à lire
     * @param o qui contient des informations sur la classe à instancier pour chaque objet dans le tableau résultant
     * @return tableau d'objet
     * @throws Exception
     */
    public static Object[] toObjectChampAnnonce(InputStream is, ClassMAPTable o ) throws Exception {
        ClassMAPTable[] rst = null;
        ArrayList<Object> ob = new ArrayList<>();
        Class c = Class.forName(o.getClassName());
        Method method;

        workbook = WorkbookFactory.create(is);
        sheet = workbook.getSheetAt(0);
        int indice = 1; // miala ny indice 0 satria titre
        Row row = sheet.getRow(indice);
        rst = new ClassMAPTable[sheet.getPhysicalNumberOfRows() - 1];
        int ind = 0;
        /* Get Champ */
        Row tmp = sheet.getRow(0);
        int dim = tmp.getPhysicalNumberOfCells();
        String[] lsName = new String[ dim ] ;
        Field[] champs = new Field[dim];
        
        for( int i = 0 ; i < dim ;i++  ){
            lsName[i] = tmp.getCell(i).getStringCellValue() + "";
            champs[i] = c.getDeclaredField( tmp.getCell(i).getStringCellValue() );
        }
        while (row != null) {
            Object obj = c.newInstance();
            Object value = null;
            for (int co = 0; co < dim ; co++) {
                method = obj.getClass().getMethod("set" + Utilitaire.convertDebutMajuscule(champs[co].getName()), champs[co].getType());
                if(row.getCell(co)!=null &&  row.getCell(co).getCellType() != Cell.CELL_TYPE_BLANK ){
                    
                    if (champs[co].getType().getName().contains("String")) {
                        try {
                            if(!row.getCell(co).getStringCellValue().isEmpty()){
                            method.invoke(obj, row.getCell(co).getStringCellValue());
                            }
                            else{
                                System.out.println(" stringgg : "+champs[co].getName());
                            }
                        } catch (Exception e) {
                            try {
                                method.invoke(obj, String.valueOf((int) row.getCell(co).getNumericCellValue()));
                            } catch (Exception exp) {
                                method.invoke(obj, String.valueOf(row.getCell(co).getDateCellValue()));
                            }
                        }
                    } else if (champs[co].getType().getName().contains("nt")) {
                        int val=0;
                        if( row.getCell(co).getCellType()==Cell.CELL_TYPE_NUMERIC)val=(int)row.getCell(co).getNumericCellValue();
                        else val=Utilitaire.stringToInt(row.getCell(co).getStringCellValue());
                        method.invoke(obj, val);
                    } else if (champs[co].getType().getName().contains("ouble")) {
                        double val=0;
                        if( row.getCell(co).getCellType()==Cell.CELL_TYPE_NUMERIC)val=row.getCell(co).getNumericCellValue();
                        else val=Utilitaire.stringToDouble(row.getCell(co).getStringCellValue());
                        method.invoke(obj, val);
                    } else if (champs[co].getType().getName().contains("ate")) {
                      
                        method.invoke(obj, Utilitaire.stringDate(row.getCell(co).getStringCellValue()));
                    }
                }
            }
            ob.add(obj);
            ind++;
            indice++;
            row = sheet.getRow(indice);
        }
        return ob.toArray();
    }

    /**
     * Permet de convertir les données dans une feuille excel en une liste d'objets d'une classe spécifique
     * @param is qui est la source de données à lire representant un fichier excel
     * @param o   fournit des informations sur la classe des objets à créer
     * @return un tableau d'objets avec les valeurs
     * @throws Exception
     */
    public static Object[] toObject2(InputStream is, ClassMAPTable o) throws Exception {
	ClassMAPTable[] rst = null;
	ArrayList<Object> ob = new ArrayList<>();
	Class c = Class.forName(o.getClassName());
	Method method;
	Field[] champs = c.getDeclaredFields();

	workbook = WorkbookFactory.create(is);
	sheet = workbook.getSheetAt(0);
	int indice = 0; // miala ny indice 0 satria titre
	Row row = sheet.getRow(indice);
	rst = new ClassMAPTable[sheet.getPhysicalNumberOfRows() - 1];
	int ind = 1;
        indice++;
        row = sheet.getRow(indice);
	while (row != null) {
	    Object obj = c.newInstance();
	    int cellNumber = row.getPhysicalNumberOfCells();
	    Object value = null;
            int nbRowInvalid = 0;
            for (int co = 0; co < cellNumber; co++) {
                if(row.getCell(co) == null || row.getCell(co).getCellType() ==  Cell.CELL_TYPE_BLANK|| row.getCell(co).toString().trim().compareTo("")==0){
                    nbRowInvalid++;
                }
            }
            if(nbRowInvalid>=cellNumber){
                break;
            }  
	    for (int co = 0; co < champs.length; co++) {
                System.out.println("------------------"+co);
		method = obj.getClass().getMethod("set" + Utilitaire.convertDebutMajuscule(champs[co].getName()), champs[co].getType());
                
                if (champs[co].getType().getName().contains("String")) {
		    try {
                        method.invoke(obj,row.getCell(co, row.CREATE_NULL_AS_BLANK).getStringCellValue());
		    } catch (Exception e) {
			try {
                            String stringToPut = String.valueOf(row.getCell(co).getNumericCellValue());
                            if(stringToPut.split(".").length>1 && Double.valueOf(stringToPut.split(".")[1])>0.00){
                                method.invoke(obj, String.valueOf((double) row.getCell(co).getNumericCellValue()));
                            }
                            else{
                                method.invoke(obj, String.valueOf((int) row.getCell(co).getNumericCellValue()));
                            }
			} catch (Exception exp) {
                            try{
                                method.invoke(obj, String.valueOf((int) row.getCell(co).getNumericCellValue()));
                            }
                            catch(Exception excep){
                                method.invoke(obj, String.valueOf(row.getCell(co).getDateCellValue()));
                            }
			    
			}
		    }
		} else if (champs[co].getType().getName().contains("int")) {
                    String valeur = (String)row.getCell(co).toString();
		    method.invoke(obj, (int)Double.parseDouble(valeur));
		} else if (champs[co].getType().getName().contains("double")) {
                    try{
                        if(row.getCell(co).getStringCellValue()!= null  && !((String)row.getCell(co).getStringCellValue()).equals("") && ((String)row.getCell(co).getStringCellValue()).contains(".")){
                            String valeur = (String)row.getCell(co).getStringCellValue();
                            if("".equals(valeur)) valeur = "0.0";
                            System.out.println("valeur = " + valeur);
                            method.invoke(obj, Double.valueOf(valeur));
                        }
                    }
                    catch(Exception e){
                        method.invoke(obj, row.getCell(co).getNumericCellValue());
                    }
		} else if (champs[co].getType().getName().contains("Date")) {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    if(row.getCell(co).getCellType()==0){
                        format = new SimpleDateFormat("MM/dd/yy");
                    }
                    DataFormatter formatter = new DataFormatter(); //creating formatter using the default locale
                    String dateString = formatter.formatCellValue(row.getCell(co));  
                    java.util.Date parsed =  format.parse(dateString);
                    java.sql.Date sql = new java.sql.Date(parsed.getTime());
                    method.invoke(obj, sql);
		}
	    }
	    ob.add(obj);
	    ind++;
	    indice++;
	    row = sheet.getRow(indice);
	}
	return ob.toArray();
    }
    
    
    
    
    
}
