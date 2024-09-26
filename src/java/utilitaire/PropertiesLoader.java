/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitaire;

import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Audace
 */
public class PropertiesLoader {
    private static PropertiesLoader propertiesLoader;
    private Properties properties;

    private PropertiesLoader() {
        try {
            properties = new Properties();
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("apj.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PropertiesLoader getInstance() {
        if (propertiesLoader == null) {
            propertiesLoader = new PropertiesLoader();
        }
        return propertiesLoader;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
    
    
}
