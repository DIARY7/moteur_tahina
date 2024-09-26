package utilitaire;

import bean.CGenUtil;
import bean.UnionIntraTable;
import java.sql.Connection;
import bean.UnionIntraTableUtil;

/**
 * Utilitaire pour faciliter la manipulation des {@link bean.UnionIntraTable}
 */
public class UtilitaireMetier {
	/**
	 * Constructeur par défaut
	 */
    public UtilitaireMetier() {
    }
	 /**
     * Lier un objet à un objet fille l'aide de la classe de mapping {@link bean.UnionIntraTable}
	 * avec etat fixé à 0
     * @param nomtableMappage nom de table en base à utiliser pour stocker les liaisons
     * @param nomFonctMap nom de procedure pour génerer les valeurs ID
     * @param suffixeMap suffixe pour le primary key
     * @param idMere id de l'objet mère à lier
     * @param idFille id à lier à la mère
     * @param rem remarque sur la liaison
     * @param montant valeur à mapper avec la liaison
     * @param refU reference de l'utilisateur qui fait la liaison
     * @param c connexion ouverte à la base de données
	 * @return id de l'objet de liaison 
     * @throws Exception
     */
    public static String mapperMereToFille(String nomtableMappage, String nomFonctMap, String suffixeMap, String idMere, String idFille, String rem, String montant, String refU, Connection c) throws Exception {
	return (mapperMereToFille(nomtableMappage, nomFonctMap, suffixeMap, idMere, idFille, rem, montant, refU, "0", c));
    }
   /**
     *Lier un objet à un objet fille à l'aide de la classe de mapping {@link bean.UnionIntraTable}
     * @param nomtableMappage nom de table en base à utiliser pour stocker les liaisons
     * @param nomFonctMap nom de procedure pour génerer les valeurs ID
     * @param suffixeMap suffixe pour le primary key
     * @param idMere id de l'objet mère à lier
     * @param idFille id à lier à la mère
     * @param rem remarque sur la liaison
     * @param montant valeur à mapper avec la liaison
     * @param refU reference de l'utilisateur qui fait la liaison
	 * @param etat etat à mettre pour les objets de liaison
     * @param c connexion ouverte à la base de données
     * @return id de l'objet de liaison
     * @throws Exception
     */
    public static String mapperMereToFille(String nomtableMappage, String nomFonctMap, String suffixeMap, String idMere, String idFille, String rem, String montant, String refU, String etat, Connection c) throws Exception {
	try {
	    int tab[] = {2, 3};
	    String val[] = {idMere, idFille};
	    UnionIntraTable uit = new UnionIntraTable();
	    uit.setNomTable(nomtableMappage);
	    uit.setId1(idMere);
	    uit.setId2(idFille);
	    UnionIntraTable[] uu = (UnionIntraTable[]) CGenUtil.rechercher(uit, null, null, c, "");
	    if (uu.length > 0) {
		throw new Exception("Objet d�j� attribu�");
	    }
	    UnionIntraTable u = null;
	    u = new UnionIntraTable(nomtableMappage, nomFonctMap, suffixeMap, idMere, idFille, rem, utilitaire.Utilitaire.stringToDouble(montant), utilitaire.Utilitaire.stringToInt(etat));
	    u.insertToTableWithHisto(refU, c);
	    return u.getId();
	} catch (Exception ex) {
	    ex.printStackTrace();
	    c.rollback();
	    return null;
	}
    }
	/**
     * Lier un objet à un objet fille à l'aide de la classe de mapping {@link bean.UnionIntraTable}
	 * avec initialisation interne de connexion à la base
     * @param nomtableMappage nom de table en base à utiliser pour stocker les liaisons
     * @param nomFonctMap nom de procedure pour génerer les valeurs ID
     * @param suffixeMap suffixe pour le primary key
     * @param idMere id de l'objet mère à lier
     * @param idFille id à lier à la mère
     * @param rem remarque sur la liaison
     * @param montant valeur à mapper avec la liaison
     * @param refU reference de l'utilisateur qui fait la liaison
	 * @param etat etat à mettre pour les objets de liaison
     * @return id de l'objet de liaison
     */
    public static String mapperMereToFille(String nomtableMappage, String nomFonctMap, String suffixeMap, String idMere, String idFille, String rem, String montant, String refU, String etat) throws Exception {
	Connection c = null;
	try {
	    c = new utilitaire.UtilDB().GetConn();
	    c.setAutoCommit(false);
	    String g = mapperMereToFille(nomtableMappage, nomFonctMap, suffixeMap, idMere, idFille, rem, montant, refU, etat, c);
	    c.commit();
	    return g;
	} catch (Exception ex) {
	    c.rollback();
	    return null;
	} finally {
	    if (c != null) {
		c.close();
	    }
	}
    }
	/**
     * Lier un objet à un objet fille à l'aide de la classe de mapping {@link bean.UnionIntraTable}
	 * avec initialisation interne de connexion à la base
	 * et etat fixé à 0
     * @param nomtableMappage nom de table en base à utiliser pour stocker les liaisons
     * @param nomFonctMap nom de procedure pour génerer les valeurs ID
     * @param suffixeMap suffixe pour le primary key
     * @param idMere id de l'objet mère à lier
     * @param idFille id à lier à la mère
     * @param rem remarque sur la liaison
     * @param montant valeur à mapper avec la liaison
     * @param refU reference de l'utilisateur qui fait la liaison
	 * @param etat etat à mettre pour les objets de liaison
     * @return id de l'objet de liaison
     */
    public static String mapperMereToFille(String nomtableMappage, String nomFonctMap, String suffixeMap, String idMere, String idFille, String rem, String montant, String refU) throws Exception {
	return (mapperMereToFille(nomtableMappage, nomFonctMap, suffixeMap, idMere, idFille, rem, montant, refU, "0"));
    }
	 /**
     * Supprimer la liaison entre un objet et un objet fille
     * @param String nom de la table stockant les liaisons
     * @param idMere id de la mère
     * @param idFille id de fille à supprimer
	 * @param refU id de l'utilisateur qui fait la suppression
     * @param c connexion ouverte à la base de données
     * */
    public static void deleteMereToFille(String nomtable, String idMere, String idFille, String refU, Connection c) throws Exception {
	try {
	    UnionIntraTable uti = new UnionIntraTable();
	    uti.setNomTable(nomtable);
	    uti.setId1(idMere);
	    uti.setId2(idFille);
	    //System.out.println("========nomtable:"+uti.getNomTable()+" Id1:"+uti.getId1()+" Id2:"+uti.getId2());
	    UnionIntraTable[] ui = (UnionIntraTable[]) CGenUtil.rechercher(uti, null, null, c, "");
	    //System.out.println(" ========= length "+ui.length+ " ui nomtable:"+ui[0].getNomTable());
	    if (ui != null && ui.length > 0) {
		ui[0].setNomTable(nomtable);
		ui[0].deleteToTableWithHisto(refU, c);
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw new Exception(ex.getMessage());
	}
    }
	/** Lier un objet à un objet fille à l'aide de la classe de mapping {@link bean.UnionIntraTable}
	* @param nomtableMappage nom de table en base à utiliser pour stocker les liaisons
	* @param nomFonctMap nom de procedure pour génerer les valeurs ID
	* @param suffixeMap suffixe pour le primary key
	* @param idMere id de l'objet mère à lier
	* @param idFille id à lier à la mère
	* @param rem remarque sur la liaison
	* @param montant valeur à mapper avec la liaison
	* @param refU reference de l'utilisateur qui fait la liaison
	* @return id de l'objet de liaison
	* @throws Exception
	*/
    public static String mapperMereToFilleMetier(String nomtableMappage, String nomFonctMap, String suffixeMap, String idMere, String idFille, String rem, String montant, String refU) throws Exception {
	return (mapperMereToFilleMetier(nomtableMappage, nomFonctMap, suffixeMap, idMere, idFille, rem, montant, refU, "0"));
    }
	/** Lier un objet à un objet fille à l'aide de la classe de mapping {@link bean.UnionIntraTable}
	* @param nomtableMappage nom de table en base à utiliser pour stocker les liaisons
	* @param nomFonctMap nom de procedure pour génerer les valeurs ID
	* @param suffixeMap suffixe pour le primary key
	* @param idMere id de l'objet mère à lier
	* @param idFille id à lier à la mère
	* @param rem remarque sur la liaison
	* @param montant valeur à mapper avec la liaison
	* @param refU reference de l'utilisateur qui fait la liaison
	* @param etat etat à mettre pour les objets de liaison
	* @return id de l'objet de liaison
	* @throws Exception
	*/
    public static String mapperMereToFilleMetier(String nomtableMappage, String nomFonctMap, String suffixeMap, String idMere, String idFille, String rem, String montant, String refU, String etat) throws Exception {
	Connection c = null;
	try {
	    c = new utilitaire.UtilDB().GetConn();
	    c.setAutoCommit(false);
	    String g = mapperMereToFilleMetier(nomtableMappage, nomFonctMap, suffixeMap, idMere, idFille, rem, montant, refU, etat, c);
	    c.commit();
	    return g;
	} catch (Exception ex) {
	    c.rollback();
	    return null;
	} finally {
	    if (c != null) {
		c.close();
	    }
	}
    }
	   /**
     *Lier un objet à un objet fille à l'aide de la classe de mapping {@link bean.UnionIntraTable}
     * @param nomtableMappage nom de table en base à utiliser pour stocker les liaisons
     * @param nomFonctMap nom de procedure pour génerer les valeurs ID
     * @param suffixeMap suffixe pour le primary key
     * @param idMere id de l'objet mère à lier
     * @param idFille id à lier à la mère
     * @param rem remarque sur la liaison
     * @param montant valeur à mapper avec la liaison
     * @param refU reference de l'utilisateur qui fait la liaison
     * @param c connexion ouverte à la base de données
     * @return id de l'objet de liaison
     * @throws Exception
     */
    public static String mapperMereToFilleMetier(String nomtableMappage, String nomFonctMap, String suffixeMap, String idMere, String idFille, String rem, String montant, String refU, Connection c) throws Exception {
	return (mapperMereToFilleMetier(nomtableMappage, nomFonctMap, suffixeMap, idMere, idFille, rem, montant, refU, "0", c));
    }
	/**
     *Lier un objet à un objet fille à l'aide de la classe de mapping {@link bean.UnionIntraTable}
     * @param nomtableMappage nom de table en base à utiliser pour stocker les liaisons
     * @param nomFonctMap nom de procedure pour génerer les valeurs ID
     * @param suffixeMap suffixe pour le primary key
     * @param idMere id de l'objet mère à lier
     * @param idFille id à lier à la mère
     * @param rem remarque sur la liaison
     * @param montant valeur à mapper avec la liaison
     * @param refU reference de l'utilisateur qui fait la liaison
	 * @param etat etat à mettre pour les objets de liaison
     * @param c connexion ouverte à la base de données
     * @return id de l'objet de liaison
     * @throws Exception
     */
    public static String mapperMereToFilleMetier(String nomtableMappage, String nomFonctMap, String suffixeMap, String idMere, String idFille, String rem, String montant, String refU, String etat, Connection c) throws Exception {
	try {
	    int tab[] = {2, 3};
	    String val[] = {idMere, idFille};
	    UnionIntraTable uit = new UnionIntraTable();
	    uit.setNomTable(nomtableMappage);
	    uit.setId1(idMere);
	    uit.setId2(idFille);
	    UnionIntraTable[] uu = (UnionIntraTable[]) CGenUtil.rechercher(uit, null, null, c, "");
	    UnionIntraTable u = null;
	    u = new UnionIntraTable(nomtableMappage, nomFonctMap, suffixeMap, idMere, idFille, rem, utilitaire.Utilitaire.stringToDouble(montant), utilitaire.Utilitaire.stringToInt(etat));
	    if (uu.length > 0) {
		throw new Exception("Objet d�j� attribu�");
	    }
            u.insertToTableWithHisto(refU, c);
	    
	    return u.getId();
	} catch (Exception ex) {
	    ex.printStackTrace();
	    c.rollback();
	    throw ex;
	}
    }
}
