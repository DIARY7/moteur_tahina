/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitaire;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * À utiliser pour l'envoi des emails
 *
 * @author Tafitasoa
 */
public class EnvoiMailService {
    
    /**
     * Fonction permettant d'envoyer un email sans piece jointe
     * <h3>Exemple d'utilisation</h3>
     * <pre>
     * EnvoiMailService.envoiMail("rakotobema@gmail.com","BICI","Objet du mail","Corps du mail","bici@gmail.com","motdepasse123");
     * </pre>
     * @param destinataire Adresse email du destinataire
     * @param envoyeur 'From' du mail
     * @param objetMail Objet du mail
     * @param textMail Corps du mail
     * @param utilisateur Adresse mail du compte avec lequel le mail sera envoyé
     * @param mdp Mot de passe du compte
     * @throws Exception
     */
    public static void envoiMail(String destinataire, String envoyeur, String objetMail, String textMail, String utilisateur, String mdp) throws Exception{
        try{
//            String smtpHost = "192.168.1.104";
//            String from = "cnaps-dn@cnaps.mg";
//            String to = "t.andriamanana@gmail.com";
//            String username = "cnapsdns@cnaps.mg";
//            String password = "cnapsdns";
            String smtpHost = "192.168.1.104";
            String from = envoyeur;
            String to = destinataire;
            String username = utilisateur;
            String password = mdp;

            Properties props = new Properties();
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.auth", "true");
            //props.put("mail.smtp.port", "995");
            //props.put("mail.smtp.port", "25");
            //props.put("mail.smtp.ssl.enable", "true");
            //props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getDefaultInstance(props);
            session.setDebug(true);

            MimeMessage message = new MimeMessage(session);   
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(objetMail);
            message.setText(textMail);

            Transport tr = session.getTransport("smtp");
            tr.connect(smtpHost, 25, username, password);
            message.saveChanges();

            // tr.send(message);
            /** Genere l'erreur. Avec l authentification, oblige d utiliser sendMessage meme pour une seule adresse... */

            tr.sendMessage(message,message.getAllRecipients());
            tr.close();
        }catch(Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }
    

    /**
     * Fonction permettant d'envoyer un email avec pieces jointes
     * <h3>Exemple d'utilisation</h3>
     * <pre>
     * String[] pieces = {"data.sql","data2.sql"};
     * String cheminServeur = "F:/dossier/data";
     * EnvoiMailService.envoiMailAvecPieceJointe("rakotobema@gmail.com","BICI","Objet du mail","Corps du mail",pieces,cheminServeur,"bici@gmail.com","motdepasse123");
     * </pre>
     * @param destinataire Adresse email du destinataire
     * @param envoyeur 'From' du mail
     * @param objetMail Objet du mail
     * @param textMail Corps du mail
     * @param pieces Liste des noms des fichiers à joindre
     * @param cheminServeur Chemin du repertoire contenant les fichiers à joindre
     * @param utilisateur Adresse mail du compte avec lequel le mail sera envoyé
     * @param mdp Mot de passe du compte
     * @throws Exception
     */
    public static void envoiMailAvecPieceJointe(String destinataire, String envoyeur, String objetMail, String textMail, String[] pieces, String cheminServeur, String utilisateur, String mdp) throws Exception {
        try{
            String smtpHost = "192.168.1.104";
            String from = envoyeur;
            String to = destinataire;
            final String username = utilisateur;
            final String password = mdp;

            Properties props = new Properties();
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.auth", "true");
            
            Session session=Session.getInstance(props, new Authenticator(){
                @Override
                protected PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication(username, password);
                }
            });
            session.setDebug(true);

            MimeMessage message = new MimeMessage(session);   
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            
            //Corps (texte)
            Multipart multipart=new MimeMultipart();
            BodyPart partieMessage=new MimeBodyPart();
            partieMessage.setText(textMail); //Pour envoyer texte brute
            //partieMessage.setContent(message, "text/html"); //Pour envoyer HTML
            multipart.addBodyPart(partieMessage);
            
            //Pi�ces jointes
            if(pieces != null && pieces.length > 0){
                for(int i = 0; i < pieces.length; i++){
                    partieMessage=new MimeBodyPart();
                    DataSource source = new FileDataSource(cheminServeur+"/"+pieces[i]);
                    partieMessage.setDataHandler(new DataHandler(source));
                    partieMessage.setFileName(pieces[i].replaceAll(".*/", ""));
                    multipart.addBodyPart(partieMessage);
                }
            }
            
            message.setSubject(objetMail);
            message.setContent(multipart);
            Transport.send(message);
            message.saveChanges();
        }catch(Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }
}
