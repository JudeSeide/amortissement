
package output;

import pret.ParametresPret;
import pret.Amortissement;
import org.apache.ecs.*;
import org.apache.ecs.xhtml.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Classe contenant les méthodes qui génèrent un fichier HTML (et CSS) à partir d'un objet prêt contenant
 * les paramètres initiaux d'un prêt.
 */
public class Html {
        final static char SEPARATEUR_OS = File.separatorChar;
        final static String RETOUR_LIGNE = System.getProperty("line.separator");
        final static String STYLE_CSS = "body{margin:50px 25px;}"
            + RETOUR_LIGNE + "h2{padding: 3px;text-align: center;text-decoration: underline;background-color: #F0F0F0;color: #069;}"
            + RETOUR_LIGNE + "h3{text-align: left;text-decoration: underline;color: #069;font-style: italic;}"
            + RETOUR_LIGNE + "table{border-spacing: 0px;border-collapse: collapse;}"
            + RETOUR_LIGNE + ".init_head, table th {text-align: center;font-weight: bold;padding: 0.1em 0.5em;border-bottom: 2px solid #FFFFFF;background: #93C2C9;}"
            + RETOUR_LIGNE + "table tr.impair{text-align: center;border-bottom: 2px solid #FFFFFF;padding: 0.1em 0.5em;background: #EDEDED;}"
            + RETOUR_LIGNE + "table tr.pair{text-align: center;font-weight: normal;padding: 0.1em 0.5em;border-bottom: 2px solid #FFFFFF;background: #C7E6EB;}";

    
    /**
     * sert à extraire une sous chaîne d'une chaîne de caractère
     * @param chaine la chaîne de base
     * @param debut entier déterminant la position dans chaîne a partir de laquelle l'extraction aura lieu
     * @param separateurString caractère dont la position constituera la fin de la sous chaîne résultat
     * @return sous chaîne entre début et l'indice du caractère séparateur
     */
    public static String extraireSouschaine(String chaine, int debut, char separateurString) {
        return chaine.substring(debut, chaine.lastIndexOf(separateurString));
    }

    /**
     * génère le titre d'une page HTML ayant le nom de fichier json initial
     * @param nom le nom du fichier json avec son nom de chemin relatif
     * @return titreHTML le titre de la page HTML
     */
    public static String genererTitrePageHTML(String nom) {
        String titreHTML = "";
        char dernierChar = nom.charAt(nom.length() - 1);
        String nomSimple = extraireSouschaine(nom, nom.lastIndexOf(SEPARATEUR_OS) + 1, dernierChar);
        titreHTML = "Tableau d'amortissement pour " + nomSimple + dernierChar + ".json";

        return titreHTML;
    }

    /**
     * génère une table en HTML contenant les paramètres initiaux du prêt en cours
     * @param pret le prêt dont les parameteres initiaux seront lu 
     * @return la table (HTML table) des paramètres initiaux du prêt
     */
    public static table genererTabParamsInitiaux(ParametresPret pret) {

        table tableParamsInitiaux = new table();
        tr ligne;
        tr[] tabTr = new tr[7];

        ligne = new tr();
        ligne.addElement(new td("ID").setClass("init_head"));
        ligne.addElement(new td(pret.getId()));
        tabTr[0] = ligne;

        ligne = new tr();
        ligne.addElement(new td("Description").setClass("init_head"));
        ligne.addElement(new td(pret.getDescription()));
        tabTr[1] = ligne;

        ligne = new tr();
        ligne.addElement(new td("Montant").setClass("init_head"));
        ligne.addElement(new td(pret.getMontant().toString() + "$"));
        tabTr[2] = ligne;

        ligne = new tr();
        ligne.addElement(new td("Nombre d'année").setClass("init_head"));
        ligne.addElement(new td(Integer.toString(pret.getNombreAnnee())));
        tabTr[3] = ligne;

        ligne = new tr();
        ligne.addElement(new td("Fréquence de Remboursement").setClass("init_head"));
        ligne.addElement(new td(Integer.toString(pret.getFrequenceRemboursement())));
        tabTr[4] = ligne;

        ligne = new tr();
        ligne.addElement(new td("Taux d'interêt").setClass("init_head"));
        ligne.addElement(new td(Double.toString(pret.getTauxInteret()) + "%"));
        tabTr[5] = ligne;

        ligne = new tr();
        ligne.addElement(new td("Fréquence de Composition").setClass("init_head"));
        ligne.addElement(new td(Integer.toString(pret.getFrequenceComposition())));
        tabTr[6] = ligne;

        for (int i = 0; i < tabTr.length; ++i) {
            tabTr[i].setClass((i % 2 == 0 ? "pair" : "impair"));
            tableParamsInitiaux.addElement(tabTr[i]);
        }
        return tableParamsInitiaux;
    }

    /**
     * génère une table en HTML contenant les paramètres calculés du prêt en cours
     * @param pret le prêt à traiter
     * @return une table HTML contenant les paramètres du prêt
     */
    public static table genererTabAmortissements(ParametresPret pret) {
        table tabAmortHTML = new table();
        List<Amortissement> tabAmort = pret.getAmortissement();

        tr ligne = new tr();
        ligne.setClass("pair");
        th[] tabTH = new th[9];
        td[] tabTD = new td[9];
        tabTH[0] = new th("Periode");
        tabTH[1] = new th("Encours du capital");
        tabTH[2] = new th("Versement(capital et intérêt)");
        tabTH[3] = new th("Versement d'intérêt");
        tabTH[4] = new th("Versement sur capital");
        tabTH[5] = new th("Nouvel encours du capital");
        tabTH[6] = new th("Total des Versement");
        tabTH[7] = new th("Total des Versement d'intérêt");
        tabTH[8] = new th("Total des Versement sur capital");

        for (int j = 0; j < tabTH.length; ++j) {
            ligne.addElement(tabTH[j]);
        }
        tabAmortHTML.addElement(ligne);
        int i = 0;
        for (Amortissement amort : tabAmort) {
            ligne = new tr();
            ligne.setClass((i % 2 == 0 ? "pair" : "impair"));

            tabTD[0] = new td(Integer.toString(amort.getPeriode()));
            tabTD[1] = new td(amort.getCapitalDebut().toString() + "$");
            tabTD[2] = new td(amort.getVersementTotal().toString() + "$");
            tabTD[3] = new td(amort.getVersementInteret().toString() + "$");
            tabTD[4] = new td(amort.getVersementCapital().toString() + "$");
            tabTD[5] = new td(amort.getCapitalFin().toString() + "$");
            tabTD[6] = new td(amort.getVersementTotalCumulatif().toString() + "$");
            tabTD[7] = new td(amort.getVersementInteretCumulatif().toString() + "$");
            tabTD[8] = new td(amort.getVersementCapitalCumulatif().toString() + "$");

            for (int j = 0; j < tabTD.length; ++j) {
                ligne.addElement(tabTD[j]);
            }

            tabAmortHTML.addElement(ligne);

            i++;
        }

        return tabAmortHTML;
    }

    /**
     * génère la sortie HTML du prêt en cours, et écrit physiquement le fichier HTML sur le disque
     * @param nom le nom du fichier json intrant en traitement
     * @param pret le prêt relatif au fichier en intrant
     */
    public static void genererHTML(String nom, ParametresPret pret) {
        try {
            // Creation des nouveaux fichiers
            String nomFichier = nom + ".html";
            FileWriter fichierHTML = new FileWriter(nomFichier);
            PrintWriter writerHTML = new PrintWriter(fichierHTML);

            Document doc = new Document();
            Doctype doctype = new Doctype.XHtml10Strict();


            doc.setDoctype(doctype);
            param xmlns = new param();
            doc.getHtml().addAttribute("xmlns", "http://www.w3.org/1999/xhtml");

            String titre = genererTitrePageHTML(nom);
            doc.appendTitle(titre);
            meta metaUTF8 = new meta().setHttpEquiv("content-type").setContent("text/html; charset=utf-8");
            doc.appendHead(metaUTF8);
            link style = new link();
            style.setRel("stylesheet").setType("text/css").setHref("style.css");
            doc.appendHead(style);

            //titre de la page HTML            
            h2 h2Titre = new h2(titre);
            doc.appendBody(h2Titre);

            //paramteres initiaux du pret
            h3 h3Titre = new h3("Paramètres initiaux du prêt");
            doc.appendBody(h3Titre);

            table tableParamsInitiaux = genererTabParamsInitiaux(pret);
            doc.appendBody(tableParamsInitiaux);

            //tableau d'amortissement
            h3Titre = new h3("Tableau d'amortissement");
            doc.appendBody(h3Titre);
            table tableAmortissement = genererTabAmortissements(pret);
            doc.appendBody(tableAmortissement);

            //ecriture du document HTML dans le fichier
            writerHTML.println(doc);

            writerHTML.close();

        } catch (IOException io) {
            System.out.println("Erreur: " + io.getMessage());
        }
    }

    /**
     * génère la feuille de style en tant que fichier physique sur le disque pour 
     * l'affichage esthétique des pages HTML
     * @param nom le chemin dans lequel créer la feuille de style
     */
    public static void genererCSS(String nom) {
        try {
            // Creation du fichier CSS
            String nomFichier = nom + "style.css";
            FileWriter fichierCSS = new FileWriter(nomFichier);
            PrintWriter writerCSS = new PrintWriter(fichierCSS);
            writerCSS.println(STYLE_CSS);
            writerCSS.close();
        } catch (IOException io) {
            System.out.print("Erreur lors de la création de la feuille de style." );
        }
    }
}
