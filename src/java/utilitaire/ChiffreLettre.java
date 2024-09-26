
package utilitaire;

import java.math.BigDecimal;

/**
* Utilité pour transformer des chiffres en lettres en français
* @author BICI
*/

public class ChiffreLettre
{

   public ChiffreLettre()
   {
   }

   /**
    * Convertir un nombre entier en lettres
    * @param intToString nombre entier à convertir
    * @return représentation en lettre du nombre
    */
   public static String convertIntToString(long intToString)
   {
       long valeur = intToString;
       String result = "";
       if(valeur < (long)0)
       {
           result = "-";
           valeur = (long)-1 * valeur;
       }
       if(valeur > 0x3b9aca00L)
       {
           long valeurSupMilliard = valeur / (long)0x3b9aca00;
           result = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(result)))).append(" ").append(convertIntToString(valeurSupMilliard)).append(" milliard")));
           valeur -= valeurSupMilliard * (long)0x3b9aca00;
       }
       if(valeur < (long)0x3b9aca00 && valeur > (long)0xf4240)
       {
           int valeurMillion = (int)valeur / 0xf4240;
           result = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(result)))).append(" ").append(convertIntToString(valeurMillion)).append(" million(s)")));
           valeur -= valeurMillion * 0xf4240;
       }
       if(valeur < (long)0xf4240 && valeur > (long)1000)
       {
           int valeurMille = (int)valeur / 1000;
           if(valeurMille > 1)
               result = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(result)))).append(" ").append(convertIntToString(valeurMille)).append(" mille")));
           else
               result = String.valueOf(String.valueOf(result)).concat(" mille");
           valeur -= valeurMille * 1000;
       }
       if(valeur < (long)1000 && valeur > (long)100)
       {
           int valeurCent = (int)valeur / 100;
           if(valeurCent > 1)
               result = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(result)))).append(" ").append(convertIntToString(valeurCent)).append(" cent")));
           else
               result = String.valueOf(String.valueOf(result)).concat(" cent");
           valeur -= valeurCent * 100;
       }
       if(valeur == (long)100)
       {
           result = String.valueOf(String.valueOf(result)).concat(" cent ");
           valeur -= 100;
       }
       if(valeur == (long)1000)
       {
           result = String.valueOf(String.valueOf(result)).concat(" mille ");
           valeur -= 1000;
       }
       if(valeur == (long)0xf4240)
       {
           result = String.valueOf(String.valueOf(result)).concat(" un million ");
           valeur -= 0xf4240;
       }
       if(valeur == (long)0x3b9aca00)
       {
           result = String.valueOf(String.valueOf(result)).concat(" un milliard ");
           valeur -= 0x3b9aca00;
       }
       switch((int)valeur)
       {
       case 0: // '\0'
           if(result == "" || result == "-")
               result = "zero";
           break;

       case 1: // '\001'
           result = String.valueOf(String.valueOf(result)).concat(" un");
           break;

       case 2: // '\002'
           result = String.valueOf(String.valueOf(result)).concat(" deux");
           break;

       case 3: // '\003'
           result = String.valueOf(String.valueOf(result)).concat(" trois");
           break;

       case 4: // '\004'
           result = String.valueOf(String.valueOf(result)).concat(" quatre");
           break;

       case 5: // '\005'
           result = String.valueOf(String.valueOf(result)).concat(" cinq");
           break;

       case 6: // '\006'
           result = String.valueOf(String.valueOf(result)).concat(" six");
           break;

       case 7: // '\007'
           result = String.valueOf(String.valueOf(result)).concat(" sept");
           break;

       case 8: // '\b'
           result = String.valueOf(String.valueOf(result)).concat(" huit");
           break;

       case 9: // '\t'
           result = String.valueOf(String.valueOf(result)).concat(" neuf");
           break;

       case 10: // '\n'
           result = String.valueOf(String.valueOf(result)).concat(" dix");
           break;

       case 11: // '\013'
           result = String.valueOf(String.valueOf(result)).concat(" onze");
           break;

       case 12: // '\f'
           result = String.valueOf(String.valueOf(result)).concat(" douze");
           break;

       case 13: // '\r'
           result = String.valueOf(String.valueOf(result)).concat(" treize");
           break;

       case 14: // '\016'
           result = String.valueOf(String.valueOf(result)).concat(" quatorze");
           break;

       case 15: // '\017'
           result = String.valueOf(String.valueOf(result)).concat(" quinze");
           break;

       case 16: // '\020'
           result = String.valueOf(String.valueOf(result)).concat(" seize");
           break;

       case 17: // '\021'
           result = String.valueOf(String.valueOf(result)).concat(" dix sept");
           break;

       case 18: // '\022'
           result = String.valueOf(String.valueOf(result)).concat(" dix huit");
           break;

       case 19: // '\023'
           result = String.valueOf(String.valueOf(result)).concat(" dix neuf");
           break;

       case 20: // '\024'
           result = String.valueOf(String.valueOf(result)).concat(" vingt");
           break;

       case 30: // '\036'
           result = String.valueOf(String.valueOf(result)).concat(" trente");
           break;

       case 40: // '('
           result = String.valueOf(String.valueOf(result)).concat(" quarante");
           break;

       case 50: // '2'
           result = String.valueOf(String.valueOf(result)).concat(" cinquante");
           break;

       case 60: // '<'
           result = String.valueOf(String.valueOf(result)).concat(" soixante");
           break;

       case 70: // 'F'
           result = String.valueOf(String.valueOf(result)).concat(" soixante dix");
           break;

       case 80: // 'P'
           result = String.valueOf(String.valueOf(result)).concat(" quatre vingts");
           break;

       case 81: // 'Q'
           result = String.valueOf(String.valueOf(result)).concat(" quatre vingts un");
           break;

       case 90: // 'Z'
           result = String.valueOf(String.valueOf(result)).concat(" quatre vingts-dix");
           break;

       case 91: // '['
           result = String.valueOf(String.valueOf(result)).concat(" quatre vingts onze");
           break;

       case 21: // '\025'
       case 22: // '\026'
       case 23: // '\027'
       case 24: // '\030'
       case 25: // '\031'
       case 26: // '\032'
       case 27: // '\033'
       case 28: // '\034'
       case 29: // '\035'
       case 31: // '\037'
       case 32: // ' '
       case 33: // '!'
       case 34: // '"'
       case 35: // '#'
       case 36: // '$'
       case 37: // '%'
       case 38: // '&'
       case 39: // '\''
       case 41: // ')'
       case 42: // '*'
       case 43: // '+'
       case 44: // ','
       case 45: // '-'
       case 46: // '.'
       case 47: // '/'
       case 48: // '0'
       case 49: // '1'
       case 51: // '3'
       case 52: // '4'
       case 53: // '5'
       case 54: // '6'
       case 55: // '7'
       case 56: // '8'
       case 57: // '9'
       case 58: // ':'
       case 59: // ';'
       case 61: // '='
       case 62: // '>'
       case 63: // '?'
       case 64: // '@'
       case 65: // 'A'
       case 66: // 'B'
       case 67: // 'C'
       case 68: // 'D'
       case 69: // 'E'
       case 71: // 'G'
       case 72: // 'H'
       case 73: // 'I'
       case 74: // 'J'
       case 75: // 'K'
       case 76: // 'L'
       case 77: // 'M'
       case 78: // 'N'
       case 79: // 'O'
       case 82: // 'R'
       case 83: // 'S'
       case 84: // 'T'
       case 85: // 'U'
       case 86: // 'V'
       case 87: // 'W'
       case 88: // 'X'
       case 89: // 'Y'
       default:
           int valeurDizaine = (int)(valeur / (long)10) * 10;
           int valeurUnite = (int)(valeur % (long)10);
           if(valeur > (long)16 && valeur < (long)20 || valeur > (long)70 && valeur < (long)80 || valeur > (long)90 && valeur < (long)100)
           {
               valeurDizaine -= 10;
               valeurUnite += 10;
           }
           if(valeurDizaine == 0)
           {
               result = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(result)))).append(" ").append(convertIntToString(valeurUnite))));
               break;
           }
           if(valeurUnite == 0)
           {
               result = String.valueOf(String.valueOf(result)).concat(" ");
               break;
           }
           if(valeurUnite == 1)
               result = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(result)))).append(" ").append(convertIntToString(valeurDizaine)).append(" et ").append(convertIntToString(valeurUnite))));
           else
               result = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(result)))).append(" ").append(convertIntToString(valeurDizaine)).append("-").append(convertIntToString(valeurUnite))));
           break;
       }
       return result.trim();
   }
   /**
    * 
    * @param intToString nombre décimal à mettre en lettre
    * @return représentation en lettre du nombre
    */
   public static String convertRealToString(double intToString)
   {
       double valeurApresVirgule = intToString - (double)(long)intToString;
       long valeurAvantVirgule = (long)(intToString - valeurApresVirgule);
       String result = "";
       double arr = Utilitaire.arrondir(valeurApresVirgule, 2);
       BigDecimal cent = new BigDecimal("100");
       BigDecimal apr = cent.multiply(new BigDecimal(String.valueOf(arr)));
       long apres = apr.longValue();
       if(apres == (long)0)
           result = convertIntToString(valeurAvantVirgule);
       else
       if(apres >= (long)10)
           result = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(convertIntToString(valeurAvantVirgule))))).append(" , ").append(convertIntToString(apres))));
       else
           result = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(convertIntToString(valeurAvantVirgule))))).append(", zero ").append(convertIntToString(apres))));
       return result;
   }
   /**
    * 
    * @param intToString nombre décimal à mettre en lettre
    * @return indice 0 : représentation en lettre du nombre avant la virgule
    * ; indice 1 : représentation en lettre du nombre après la virgule
    */
   public static String[] convertRealToStringSepare(double intToString)
   {
       double valeurApresVirgule = intToString - (double)(long)intToString;
       long valeurAvantVirgule = (long)(intToString - valeurApresVirgule);
       String result[] = new String[2];
       double arr = Utilitaire.arrondir(valeurApresVirgule, 2);
       BigDecimal cent = new BigDecimal("100");
       BigDecimal apr = cent.multiply(new BigDecimal(String.valueOf(arr)));
       long apres = apr.longValue();
       if(apres == (long)0)
       {
           result[0] = convertIntToString(valeurAvantVirgule);
           result[1] = "";
       } else
       if(apres >= (long)10)
       {
           result[0] = convertIntToString(valeurAvantVirgule);
           result[1] = convertIntToString(apres);
       } else
       {
           result[0] = convertIntToString(valeurAvantVirgule);
           result[1] = "zero ".concat(String.valueOf(String.valueOf(convertIntToString(apres))));
       }
       return result;
   }
   /**
    * 
    * @param intToString nombre décimal à mettre en lettre
    * @param devise suffixe de devise
    * @return représentation en lettre du nombre avec devise à la fin
    */
   public static String convertRealToStringDevise(double intToString,String devise)
   {
       double valeurApresVirgule = intToString - (double)(long)intToString;
       long valeurAvantVirgule = (long)(intToString - valeurApresVirgule);
       String result = "";
       double arr = Utilitaire.arrondir(valeurApresVirgule, 2);
       BigDecimal cent = new BigDecimal("100");
       BigDecimal apr = cent.multiply(new BigDecimal(String.valueOf(arr)));
       long apres = apr.longValue();
//        System.out.println("APRES--------------".concat(String.valueOf(String.valueOf(apres))));
       if(apres == (long)0)
           result = convertIntToString(valeurAvantVirgule) + "  " + devise+"  ";
       else
       if(apres >= (long)10)
           result = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(convertIntToString(valeurAvantVirgule))))).append("  "+devise+"  ").append(convertIntToString(apres))));
       else
           result = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(convertIntToString(valeurAvantVirgule))))).append("  "+devise+"  zero ").append(convertIntToString(apres))));
       return result;
   }
   
}
