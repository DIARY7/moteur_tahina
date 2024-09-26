

package affichage;
/**
 * Utilitaire d'affichage pour garder les informations sur la consultation d'un champ
 * 
 * 
 * @author BICI
 */
public class PageConsulteLien{
        /**
         * Constructeur par défaut
         */
        public PageConsulteLien(){
        }
        /**
         * 
         * @param lien lien vers la page
         * @param queryString paramètre supplémentaire pour la navigation vers le lien
         */
        public PageConsulteLien(String lien,String queryString){
            setLien(lien);
            setQueryString(queryString);
        }
        private String lien;
        /**
         * 
         * @return lien vers la page de consultation
         */
        public String getLien() {
            return lien;
        }

        public void setLien(String lien) {
            this.lien = lien;
        }
        /**
         * 
         * @return paramètre supplémentaire pour la navigation vers le lien
         */
        public String getQueryString() {
            return queryString;
        }

        public void setQueryString(String queryString) {
            this.queryString = queryString;
        }
        private String queryString;
    }
