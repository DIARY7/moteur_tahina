/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MSI
 */
public class CynthiaConf {
    public static Properties properties = new Properties();
    public static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    public static Properties load() {
        try {
            if(properties.isEmpty())
                properties.load(classLoader.getResourceAsStream("apj.properties"));
            return properties;
        } catch (IOException ex) {
            Logger.getLogger(CynthiaConf.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}