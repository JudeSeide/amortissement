package pret;

import pret.Amortissement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui contient les paramètres d'un prêt à rembourser. 
 */
public class ParametresPret {

    private String id;
    private String description;
    private BigDecimal montant = new BigDecimal("0");
    private int nombreAnnee;
    private int frequenceRemboursement;
    private double tauxInteret;
    private int frequenceComposition;
    private BigDecimal versementPeriodique = new BigDecimal("0");
    private List<Amortissement> amortissement = new ArrayList<Amortissement>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if(id != null){
             this.id = id.trim();
        }       
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if(description != null){
             this.description = description.trim();
        }       
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        if(montant != null){
            this.montant = montant;
        }        
    }

    public int getNombreAnnee() {
        return nombreAnnee;
    }

    public void setNombreAnnee(int nombreAnnee) {        
        this.nombreAnnee = nombreAnnee;
    }

    public int getFrequenceRemboursement() {
        return frequenceRemboursement;
    }

    public void setFrequenceRemboursement(int frequenceRemboursement) {
        this.frequenceRemboursement = frequenceRemboursement;
    }

    public double getTauxInteret() {
        return tauxInteret;
    }

    public void setTauxInteret(double tauxInteret) {
        this.tauxInteret = tauxInteret;
    }

    public int getFrequenceComposition() {
        return frequenceComposition;
    }

    public void setFrequenceComposition(int frequenceComposition) {
        this.frequenceComposition = frequenceComposition;
    }

    public BigDecimal getVersementPeriodique() {
        return versementPeriodique;
    }

    public void setVersementPeriodique(BigDecimal versementPeriodique) {
        if(versementPeriodique != null){
               this.versementPeriodique = versementPeriodique;
        }     
    }

    public List<Amortissement> getAmortissement() {
        return amortissement;
    }

    public void setAmortissement(List<Amortissement> amortissement) {
        this.amortissement = amortissement;
    }

    public double interet() {
        double taux = (tauxInteret / 100) / frequenceComposition;
    	double exp = 1.0 / (12 / frequenceComposition);
        return Math.pow( 1 + taux, exp ) - 1;
    }
    
    public int nbVersements (){
        return frequenceRemboursement * nombreAnnee;
    }

}
