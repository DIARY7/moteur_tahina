/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package affichage;

/**
 *
 * @author nyamp
 */
public class Couleur {

    private static final int couleurMax = 255;
    private static final int opaciteMax = 1;
    int r, v, b;
    double a = opaciteMax;

    public Couleur(int r, int v, int b) throws Exception {
        this.setR(r);
        this.setV(v);
        this.setB(b);
    }
    
    public Couleur(int r, int v, int b, double a) throws Exception {
        this.setR(r);
        this.setV(v);
        this.setB(b);
        this.setA(a);
    }

    public Couleur[] genererCouleur(int pas, int taille) throws Exception {
        Couleur[] couleurs = new Couleur[taille];
        int[] rvb = new int[3];
        if(taille>0)
            couleurs[0] = this;
        int indice, temp, max = 2;

        if (this.getR() > this.getV() && this.getR() > this.getB()) {
            max = 0;
        } else if (this.getV() > this.getB() && this.getV() > this.getB()) {
            max = 1;
        } else if (this.getB() > this.getV() && this.getB() > this.getR()) {
            max = 2;
        }
        rvb[0] = this.getR();
        rvb[1] = this.getV();
        rvb[2] = this.getB();
        for (int i = 1; i < taille; i++) {
            temp = pas;
            indice = max;
            while ((rvb[indice] += temp) > couleurMax) {
                rvb[indice] = rvb[indice] - couleurMax - 1;
                temp = 1;
                if (indice == 0) {
                    indice = 2;
                } else {
                    indice--;
                }
            }
            couleurs[i] = new Couleur(rvb[0], rvb[1], rvb[2]);

        }

        return couleurs;
    }

    public Couleur() {

    }

    public int getR() {
        return r;
    }

    public void setR(int r) throws Exception {
        if (r < 0 || r > couleurMax) {
            throw new Exception("Erreur : r invalide");
        }
        this.r = r;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) throws Exception {
        if (v < 0 || v > couleurMax) {
            throw new Exception("Erreur : v invalide");
        }
        this.v = v;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) throws Exception {
        if (b < 0 || b > couleurMax) {
            throw new Exception("Erreur : b invalide");
        }
        this.b = b;
    }

    public double getA() {

        return a;
    }

    public void addR(int pas) {
        int temp = this.r + pas;

        if (temp > couleurMax) {
            this.r = couleurMax;
            temp = temp - couleurMax;
            this.addV(temp);
        } else if (temp == couleurMax * 2) {
            this.r = 0;
        } else {
            this.r = temp;
        }
    }

    public void addV(int pas) {
        int temp = this.v + pas;

        if (temp > couleurMax) {
            this.v = couleurMax;
            temp = temp - couleurMax;
            this.addB(temp);
        } else if (temp == couleurMax * 2) {
            this.v = 0;
        } else {
            this.v = temp;
        }
    }

    public void addB(int pas) {
        int temp = this.b + pas;

        if (temp > couleurMax && temp < couleurMax * 2) {
            this.b = couleurMax;
            temp = temp - couleurMax;
            this.addR(temp);
        } else if (temp == couleurMax * 2) {
            this.b = 0;
        } else {
            this.b = temp;
        }
    }

    public void setA(double a) throws Exception {
        if (a < 0 || a > opaciteMax) {
            throw new Exception("Erreur : a invalide");
        }
        this.a = a;
    }

    public String getCouleur() {
        return String.format("rgba(%s,%s,%s,%s)", r, v, b, a);
    }

    public void addRVB(int pas) throws Exception {
        int tr = this.r, tv = this.v, tb = this.b;
        tr += pas;
        if (tr > couleurMax) {
            tr = couleurMax;
            tv += pas;
            if (tv > couleurMax) {
                tv = couleurMax;
                tb += pas;
                if (tb > couleurMax) {
                    tb = couleurMax;
                }
            }
        }
        this.setR(tr);
        this.setV(tv);
        this.setB(tb);
    }

    public void addAll(int pas) throws Exception {
        int temp = this.r;
        temp += pas;
        if (temp > couleurMax) {
            temp = couleurMax;
        }
        this.setR(temp);
        temp = this.v;
        temp += pas;
        if (temp > couleurMax) {
            temp = couleurMax;
        }
        this.setV(temp);
        temp = this.b;
        temp += pas;
        if (temp > couleurMax) {
            temp = couleurMax;
        }
        this.setB(temp);
    }

//    public void addMax(int pas){
//        if(this.r> this.v && this.r> this.b){
//            this.v=
//        }
//        if(this.v> this.r && this.v> this.b){
//            
//        }
//        if(this.b> this.r && this.b> this.v){
//            
//        }
//    }
    public Couleur[] getCouleurs(int pas, int taille) throws Exception {
        Couleur[] couleurs = new Couleur[taille];
        couleurs[0] = this;
        boolean monter = true;
        int indice = 1;
        int tr, tv, tb;
        for (int i = 1; i < taille; i++) {
            couleurs[i] = new Couleur(couleurs[i - 1].getR(), couleurs[i - 1].getV(), couleurs[i - 1].getB());
            couleurs[i].addR(pas);
//            couleurs[i].addAll(pas);
            if (couleurs[i].equals(couleurs[i - 1])) {
                indice++;
                if (indice % 3 == 0) {
                    couleurs[i].setR(0);
                }
                if (indice % 3 == 1) {
                    couleurs[i].setV(0);
                }
                if (indice % 3 == 2) {
                    couleurs[i].setB(0);
                }

            }

        }

        return couleurs;
    }

    public boolean equals(Couleur couleur) {
        boolean verifier = false;
        if (this.r == couleur.r && this.v == couleur.v && this.b == couleur.b && this.a == couleur.a) {
            verifier = true;
        }

        return verifier;
    }
}
