package i18n;
import java.util.*;

/**
 * Cette classe contiennent des objets spécifiques aux paramètres régionaux
 * @author BICI
 */
public class i18nProperties{

	private Locale locale;
	private ResourceBundle resourceBundle;

	/**
	 * 
	 * @return la variable d'instance resourceBundle
	 */
	public Locale getLocale(){
		return locale;
	}

	/**
	 * @deprecated code commenté
	 * @param local
	 */
	public void setLocale(String local){
		//Locale l=new Locale(local,"");
		//this.locale=l;
	}
	/**
	 * 
	 * @return la variable d'instance resourceBundle
	 */
	public ResourceBundle getResourceBundle(){
		return resourceBundle;
	}
	public void setResourceBundle(String resourceBundle){
		ResourceBundle rB=ResourceBundle.getBundle(resourceBundle,getLocale());
		this.resourceBundle=rB;
	}
}