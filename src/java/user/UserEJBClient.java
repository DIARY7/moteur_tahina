// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   UserEJBClient.java
package user;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Classe permettant de récuperer les instances de {@link user.UserEJB} dans un contexte
 * local ou à distance.
 * <p>
 *  Par exemple pour initialiser un {@link user.UserEJB} du contexte local:
 * </p>
 * <pre>
 *  UserEJB userConttext = UserEJBClient.lookupUserEJBBeanLocal();
 * </pre>
 * <p>
 * En quoi utiliser un client est important pour la récuperation de bean ?
 * </p>
 * <p>
 * <b>C'est simple si on veut faire de modification 
 * sur le scope du bean(singleton ou géneré à chaque appel) ou encore sur les implémentations 
 * à utiliser, on va avoir un bean normalisé pour tout le contexte de l'application.
 * </b>
 * </p>
 */
public class UserEJBClient {

    public static UserEJBRemote lookupUserEJBBeanRemote() {
        try {
            Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
            properties.put(Context.PROVIDER_URL, "remote://127.0.0.1:4447");
            properties.put(Context.SECURITY_PRINCIPAL, "cnaps");
            properties.put(Context.SECURITY_CREDENTIALS, "cnaps123");
            properties.put("jboss.naming.client.ejb.context", true);
            final Context context = new InitialContext(properties);
            return (UserEJBRemote) context.lookup("cnaps/cnaps-ejb/UserEJBBean!user.UserEJBRemote");
        } catch (NamingException ne) {
//            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    public static UserEJB lookupUserEJBBeanLocal() {
        try {
            Context c = new InitialContext();
            return (UserEJB) c.lookup("java:module/UserEJBBean!user.UserEJB");
        } catch (NamingException ne) {
//            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
