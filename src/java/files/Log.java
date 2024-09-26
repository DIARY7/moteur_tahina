/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package files;

/**
 *
 * @author MSI
 */

public final class Log
{

    public Log()
    {
        numero = 0;
    }

    public static final void log(String s)
    {
        if(afficherTemoin == 1)
            System.out.println(" Log : ".concat(String.valueOf(String.valueOf(s))));
    }

    public static void setLog(String log)
    {
        if(afficherTemoin != 1)
            System.out.println(" - ".concat(String.valueOf(String.valueOf(log))));
    }

    public static void afficher()
    {
        afficherTemoin = 1;
    }

    public static void masquer()
    {
        afficherTemoin = 0;
    }

    public static void logFile(String s1)
    {
    }

    int numero;
    public static byte afficherTemoin = 1;

}
