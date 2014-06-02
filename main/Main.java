package main;

import output.Html;
import pret.ParametresPret;
import pret.Amortissement;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


/** 
 * Programme qui permet de calculer les modalités de remboursement de prêt.
 * 
 * L'intrant du logiciel est un dossier contenant des fichiers de type JSON.
 * Chaque fichier contient à son tour les paramètres du prêt à traiter.
 * 
 * L'extrant du logiciel est un dossier contenant lui aussi des fichiers JSON, 
 * relatifs aux intrants et qui décrivent les modalités de remboursement du prêt
 * ( table d'amortissement ), ainsi qu'un dossier contenant les mêmes calculs mais 
 * sous forme de documents HTML.
 * 
 * Le programme devrait être appelé par la commande suivante après le build: 
 * java -jar "nomFichier.jar" repertoireIntrant repertoireExtrantJSON repertoireExtrantHTML
 * 
 * @author Jude Seide
 * 
 */
public class Main {

    //constantes globales
    final static char SEPARATEUR_OS = File.separatorChar;
    final static String RETOUR_LIGNE = System.getProperty("line.separator");

    /**
     * crée un dossier dans le chemin spécifié
     * @param chemin le chemin du dossier a créer
     */
    public static void creerDossier(String chemin) {
        if (!(new File(chemin)).exists()) {
            boolean success = (new File(chemin)).mkdirs();
            if (!success) {
                System.out.println("Extrants : probleme de creation!");
            }
        }
    }

    /**
     * s'assure que la chaîne de caractère donnée est un chemin
     * qui existe et que c'est bel est bien un répertoire.
     * @param nomDossier le nom du dossier supposé
     */
    public static void validerDossier(String nomDossier) {
        if (!(new File(nomDossier).isDirectory())) {
            System.out.println(nomDossier + ": n'existe pas ou n'est pas un repertoire valide!");
            System.exit(-1);
        }
    }

    /**
     * prépare la table d'amortissement en y chargeant un élément temporaire servant à simplifier
     * le code de la méthode chargerTabAmortissement.      
     * @param pret
     * @param nbVersements
     * @return temp amortissement temporaire servira plus tard pour faire fonctionner la méthode de 
     * chargement réel de la table
     */
    public static Amortissement generer1erePeriodeTemp(ParametresPret pret, int nbVersements) {

        Amortissement temp = new Amortissement(); // periode = 1        
        temp.setCapitalDebut(pret.getMontant());
        temp.setVersementTotal(pret.getMontant(), nbVersements, pret.interet());
        pret.setVersementPeriodique(temp.getVersementTotal());
        temp.setCapitalFin(temp.getCapitalDebut(), BigDecimal.ZERO);

        return temp;
    }

    /**
     * sert à charger la table d'amortissement après que les calculs soient finalisé grâce 
     * Tab[0] est un élément temporaire à supprimer avant d'affecter la table dans un objet de type prêt.
     * aux setters.
     * @param tab table d'amortissement du prêt
     * @param pret prêt traité
     * @param nbVersements nombre de périodes d'amortissement
     */
    public static void chargerTabAmortissement(List<Amortissement> tab, ParametresPret pret, int nbVersements) {

        tab.add(generer1erePeriodeTemp(pret, nbVersements)); //element temporaire

        for (int i = 1; i <= nbVersements; ++i) {

            Amortissement prec = tab.get(i - 1);
            Amortissement amort = new Amortissement();
            amort.setCapitalDebut(prec.getCapitalFin());
            amort.setVersementTotal(amort.getCapitalDebut(), nbVersements - i + 1, pret.interet());
            amort.setVersementInteret(amort.getCapitalDebut(), pret.interet());
            amort.setVersementCapital(amort.getVersementTotal(), amort.getVersementInteret());
            amort.setCapitalFin(amort.getCapitalDebut(), amort.getVersementCapital());
            amort.setVersementTotalCumulatif(prec, amort.getVersementTotal());
            amort.setVersementInteretCumulatif(prec, amort.getVersementInteret());
            amort.setVersementCapitalCumulatif(prec, amort.getVersementCapital());

            tab.add(amort);
        }
    }

    /**
     * @param args arguments dans la ligne de commande
     */
    public static void main(String[] args) {
        final int INDICE_TEMP = 0;
        final int MAX_ARGS = 3;
        final int ARGS_INTRANT = 0;
        final int ARGS_EXT_JSON = 1;
        final int ARGS_EXT_HTML = 2;

        //on s'assure qu'il y a bien 3 arguments sur la ligne de commande
        if (args.length != MAX_ARGS) {
            System.out.println("Erreur avec l'entree d'arguments du programme ( repertoires )");
            System.exit(-1);
        }

        String nomDossierIntrant = args[ARGS_INTRANT];
        validerDossier(nomDossierIntrant);
        
        String nomDossierExtrantJSON = args[ARGS_EXT_JSON];
        String nomDossierExtrantHTML = args[ARGS_EXT_HTML];        

        FilenameFilter filtre = new FiltreJSON();
        File fichiersDossierIntrant = new File(nomDossierIntrant);

        String[] tabIntrantsJson = fichiersDossierIntrant.list(filtre);        
        if (tabIntrantsJson.length < 1){
            System.out.println("  Aucun fichier JSON dans le dossier des intrants!");
            System.exit(0);
        }
        
        creerDossier(nomDossierExtrantJSON);
        creerDossier(nomDossierExtrantHTML);

        ObjectMapper mapper = new ObjectMapper();

        if (tabIntrantsJson != null) {
            for (String fichier : tabIntrantsJson) {
                try {

                    //lecture d'un fichier de pret
                    System.out.println(RETOUR_LIGNE + "Traitement du fichier " + fichier + "...");
                    ParametresPret pret = mapper.readValue(new File(nomDossierIntrant + SEPARATEUR_OS + fichier), ParametresPret.class);

                    //ArrayList contenant les amortissements, le 1er element est temporaire
                    int nbVersements = pret.nbVersements();
                    List<Amortissement> tabAmortissement = new ArrayList<Amortissement>(nbVersements + 1);

                    //calcul des periodes de remboursement
                    chargerTabAmortissement(tabAmortissement, pret, nbVersements);

                    //suppression de l'élément tempporaire dans la table                    
                    tabAmortissement.remove(INDICE_TEMP);

                    pret.setAmortissement(tabAmortissement);

                    //ecriture d'un fichier d'amortissement relatif au pret lu
                    System.out.println("    generation du fichier JSON correspondant.");
                    mapper.defaultPrettyPrintingWriter().writeValue(new File(nomDossierExtrantJSON + SEPARATEUR_OS + fichier), pret);

                    //ecrituer du fichier de sortie en format HTML
                    fichier = Html.extraireSouschaine(fichier, 0, '.');
                    Html.genererHTML(nomDossierExtrantHTML + SEPARATEUR_OS + fichier, pret);
                    System.out.println("    generation du fichier HTML correspondant.");
                    
                    //remise du total des periodes a zero pour les fichiers suivants
                    Amortissement.setTotPeriodes(-1);

                } catch (JsonGenerationException jge) {
                    System.out.println("Erreur de generation JSON..." + RETOUR_LIGNE + jge.getMessage());
                } catch (JsonMappingException jme) {
                    System.out.println("Erreur de Lecture JSON..." + RETOUR_LIGNE + jme.getMessage());
                } catch (IOException e) {
                    System.out.println("Erreur avec les fichiers..." + RETOUR_LIGNE + e.getMessage());
                }

            }
            Html.genererCSS(nomDossierExtrantHTML + SEPARATEUR_OS);
        }
    }
}
