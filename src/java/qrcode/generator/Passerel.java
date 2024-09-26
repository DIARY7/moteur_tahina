/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qrcode.generator;

import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Cette classe permet de générer une image code QR
 * 
 * @author BICI
 */
public class Passerel {
    private boolean isCrypt=false;
    private String texte="";
    private GeneratorQrCode g;
    private SimpleGenerator sg;

    /**
     * Constructeur par defaut
     */
    public Passerel(){
        
    }

    /**
     * Obtenir la valeur du texte pour construire une code QR
     * @return valeur de type string
     */
    public String getTexte(){
        return this.texte;
    }
    public void setTexte(String txt){
        this.texte=txt;
    }
    public boolean getCrypt(){
        return this.isCrypt;
    }
    public void setCrypt(boolean crypt){
        this.isCrypt=crypt;
    }

    /**
     * Constructeur
     * @param text eprésente le texte qui doit être crypté
     * @param iscrypt etat du crypatge soit false soit true
     */
    public Passerel(String text,boolean iscrypt){
        this.texte=text;
        this.isCrypt=iscrypt;
    }

    /**
     * Génère une image de code QR à partir d'une chaîne de texte donnée.
     * @return  renvoie l'image de code QR générée sous la forme d'un objet BufferedImage.
     * @throws Exception
     */
    public BufferedImage Generate() throws Exception{
        if(!texte.equals("")){
            if (!isCrypt){
                sg=new SimpleGenerator(texte);
                return sg.createImages();
            }
            else{
                g=new GeneratorQrCode(texte);
                return g.createImages();
            }
        }
        return null;
    }
    
}
