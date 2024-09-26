package historique;

import java.util.Properties;
import javax.naming.*;

public class HistoriqueEJBClient {

    public static HistoriqueRemote lookupHistoriqueEJBBeanRemote() {
        try {
            Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
            properties.put(Context.PROVIDER_URL, "remote://127.0.0.1:4447");
            properties.put(Context.SECURITY_PRINCIPAL, "cnaps");
            properties.put(Context.SECURITY_CREDENTIALS, "cnaps123");
            properties.put("jboss.naming.client.ejb.context", true);
            final Context context = new InitialContext(properties);
            return (HistoriqueRemote) context.lookup("cnaps/cnaps-ejb/UserEJBBean!user.UserEJBRemote");//TO CHANGE
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
    
    public static HistoriqueLocal lookupHistoriqueEJBBeanLocal() {
        try {
            Context c = new InitialContext();
            return (HistoriqueLocal) c.lookup("java:module/HistoriqueBean!historique.HistoriqueLocal");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
}
