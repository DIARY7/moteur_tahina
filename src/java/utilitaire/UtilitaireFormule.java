/**
 * 6 oct. 2015
 *
 * @author Manda
 */
package utilitaire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
/**
 * Classe d'utilitaire pour génerer des valeurs à partir de variables et de formules
 * 
 *  
 * @author BICI
 */
public class UtilitaireFormule {
    /**
     * Evaluer une expression
     * @param expression formule(exemple: [variable1]+[variable2]*0,5)
     * @param variables liste des clés/valeurs 
     * avec en clé le nom du variable et en valeur la valeur du variable
     * @return resultat du calcul
     * @throws EvaluationException
     */
    public static double eval(String expression, HashMap<String, String> variables) throws EvaluationException {
        expression = expression.replace(",",".");
        expression = expression.replace("[", "#{").replace("]", "}");
        return evalWithCurlyBrackets(expression, variables);
    }
    /**
     * Evaluer une expression
     * @param expression formule(exemple: #{variable1} + #{variable2}*0.5)
     * @param variables liste des clés/valeurs 
     * avec en clé le nom du variable et en valeur la valeur du variable
     * @return resultat du calcul
     * @throws EvaluationException
     */
    public static double evalWithCurlyBrackets(String expression, HashMap<String, String> variables) throws EvaluationException {
        Evaluator expr = new Evaluator();
        expr.setVariables(variables);
        String evaluate = expr.evaluate(expression);
        return Double.parseDouble(evaluate);
    }
    /**
     * Verifier si la formule est du type utilisant []
     * @param expression formule à tester
     * @return
     */
    public static boolean isFormuleWithBrackets(String expression){
        return (expression.contains("[") && expression.contains("]"));
    }
    /**
     * Extraire les variables dans les brackets([])
     * @param expression
     * @return
     * @throws Exception
     */
    public static String[] extractExpressionWithBrackets(String expression) throws Exception{
        Pattern p = Pattern.compile("\\[(.*?)\\]");
        Matcher m = p.matcher(expression);
        List<String> toRet = new ArrayList<>();
        int nb=0;
        while(m.find()) {
            toRet.add(m.group(1));
            nb++;
        }
        if(nb==0){
            throw new EvaluationException("Formula invalid, use Brackets []");
        }
        String[] ret = new String[toRet.size()];
        ret = toRet.toArray(ret);
        return ret;
    }

}
