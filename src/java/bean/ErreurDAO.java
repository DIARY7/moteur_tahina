package bean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import files.Log;
import java.sql.Connection;

/**
 * Erreur spécial lié à l'accès de données
 * 
 * 
 * @author MSI
 */

public class ErreurDAO extends Exception
{
    /**
     * Constructeur par défaut
     * @param msgErreur message d'erreur
     */
    public ErreurDAO(String msgErreur)
    {
        super(msgErreur);
    }
    /**
     * @deprecated ne devrait pas être utilisé, les rollbacks devraient se faire dans les métiers
     * Constructeur avec connexion pour rollback
     * @param c connexion ouverte à la base de données
     * @param msgErreur message d'erreur
     */
    public ErreurDAO(Connection c, String msgErreur)
    {
        super(msgErreur);
        try
        {
            c.rollback();
        }
        catch(Exception e)
        {
            Log.log("Erreur annulation dans ErrerDAO.java ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
    }
}
