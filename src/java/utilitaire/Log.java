// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Log.java

package utilitaire;

import java.io.PrintStream;

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
