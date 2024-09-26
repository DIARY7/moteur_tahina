package qrcode.generator;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Cette classe sert à la conversion d'une variable en Binaire 
 * <h3>Exemple d'utilisation</h3>
 * <pre>
 * String b = new Binarizer(this.E).toBin();
 * </pre>
 */
public class Binarizer{
        public int E;

        /**
         * Constructeur
         * @param e variable à convertir en binaire
         */
        public Binarizer(int e){
            this.E = e;
        }

        /**
         * Convertir la valeur de la variable this.E en une représentation binaire sous forme de chaîne. 
         * @return chaine binaire
         */
        public String toBin(){
            String Resultat = "";
            int i = 0;
            int q;
          ArrayList<Boolean> res=new ArrayList<Boolean>();
            if (this.E == 1){
                boolean b = true;
                res.add(b);
            }
            else if(this.E==0){
                boolean b = false;
                res.add(b);
            }
            while ((q = this.E / 2)!=0){
                if (q != 1){
                    int r = this.E % 2;
                    if (r == 1){
                        boolean b = true;
                        res.add(b);
                    }
                    else{
                        boolean b = false;
                        res.add(b);
                    }
                    this.E = q;
                    i++;
                }
                else if (q==1){
                    int r = this.E % 2;
                    if (r == 1){
                        boolean b = true;
                        res.add(b);
                    }
                    else{
                        boolean b = false;
                        res.add(b);
                    }
                    boolean bol = true;
                    res.add(bol);
                    break;
                }
                
            }
            BitSet bt = new BitSet(res.size());
            int k=0;
            for (int j =res.size()-1; j >=0; j--){
                bt.set(j, res.get(k));
                k++;
            }
            for (int j = 0; j < bt.length(); j++){
                if (bt.get(j))
                    Resultat += "1";
                else Resultat += "0";
            }

            return Resultat;
        }
}

