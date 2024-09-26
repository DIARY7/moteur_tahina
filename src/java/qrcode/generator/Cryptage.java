package qrcode.generator;

/**
 * Cette permet de crypter une message , une valeur
 * <h3>Exemple d'utilisation</h3>
 * <p>La fonction ci-dessous permet de cripter le message  et utilise la fonction 
 * <strong>calculate()</strong>
 * </p>
 * <pre>
 * public String Cryptee(String txtClair){
        String textCrypte="";
        for (int i = 0; i < txtClair.length(); i++)
        {
        	if (i==txtClair.length()-1) {
                    Cryptage s = new Cryptage((int)txtClair.charAt(i)-65, 13, 899);
                    textCrypte += "" + s.calculate();
			}
        	else{
                    Cryptage s = new Cryptage((int)txtClair.charAt(i)-65, 13, 899);
                    textCrypte += "" + s.calculate() + ",";
        	}
        }
        return textCrypte;
	}
 * </pre>
 * 
 */
public class Cryptage {
        public int M;
        public int E;
        public int N;

        /**
         * Constructeur
         * @param m 
         * @param e variable à convertir en binaire
         * @param n le diviseur pour le modulo
         */
        public Cryptage(int m,int e, int n)
        {
            this.M = m;
            this.E = e;
            this.N = n;
        }
        /**
         * calcule la valeur du message crypté c
         * étant donné le message original M, l'exposant de cryptage E et le module N.
         * @return la valeur après cryptage
         */
        public int calculate()
        {
            int c = 1;
            String b = new Binarizer(this.E).toBin();
            int k = b.length();
            for (int i = 0; i < k; i++){
                if (b.charAt(i) == '0'){
                    c = (puissance(c, 2)) % this.N;
                }
                else{
                    c = (puissance(c, 2) * this.M) % this.N;
                }
            }
            return c;
        }

        /**
         * Le but de cette méthode est de calculer la valeur de x élevée à la puissance n.
         * <h3>Exemple</h3>
         * <pre>
         *  Ex: 2^3 
         *  ici, x = 2 et n = 3
         * </pre>
         * @param x la valeur 
         * @param n puissance
         * @return le resultat apres la calcul de la puissance
         */
        private int puissance(int x, int n){
            int resPuis = 1;
            if (x==0) return 0;
            if (n == 0) return 1;
            for (int i = 0; i < n; i++){
                resPuis = resPuis * x;
            }
            return resPuis;
            
        }
    }

