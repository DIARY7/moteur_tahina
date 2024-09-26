/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package historique;

import bean.ClassMAPTable;

/**
 * Objet pour stocker les paramètres de cryptage de mot de passe d'un utilisateur
 * Cet objet est important pour le login
 * 
 * 
 * @author BICI
 */

public class ParamCrypt extends ClassMAPTable {

	public String id;
	public int niveau;
	public int croissante;
	public String idUtilisateur;

	public ParamCrypt()
	{
		this.setNomTable("ParamCrypt");
	}
	public ParamCrypt(String id, int niveau, int croissante, String idUtilisateur) {
		this.id = id;
		this.niveau = niveau;
		this.croissante = croissante;
		this.idUtilisateur = idUtilisateur;
		this.setNomTable("ParamCrypt");
	}

	public ParamCrypt(int niveau, int croissante, String idUtilisateur) {
		this.niveau = niveau;
		this.croissante = croissante;
		this.idUtilisateur = idUtilisateur;

		this.setNomTable("ParamCrypt");
		this.setIndicePk("CRY");
		this.setNomProcedureSequence("getseqParamCrypt");
		this.setId(makePK());
	}
	/**
	 * Implémentation de la fonction pour retourner le nom de la colonne d'identifiant en base
	 */
	public String getAttributIDName() {
		return "id";
	}
	/**
	 * implémentation de la fonction pour avoir la valeur de l'identifiant
	 */
	public String getTuppleID() {
		return String.valueOf(id);
	}

	/**
	 * 
	 * @return sens du cryptage
	 */
	public int getCroissante() {
		return croissante;
	}

	public void setCroissante(int croissante) {
		this.croissante = croissante;
	}
	/**
	 * 
	 * @return identifiant du cryptage
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 
	 * @return identifiant de l'utilisateur à qui le cryptage s'applique
	 */
	public String getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(String idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}
	/**
	 * 
	 * @return niveau de cryptage(utilisé dans l'algorithme de cryptage ou decryptage)
	 */
	public int getNiveau() {
		return niveau;
	}

	public void setNiveau(int niveau) {
		this.niveau = niveau;
	}



}
