package pret;

import java.math.BigDecimal;

/**
 * Classe qui continent les paramètres d'un amortissement.
 * Elle permet de calculer les modalités du remboursement du prêt, et tient compte du
 * numéro de la période.
 */
public class Amortissement {

    private static int totalPeriodes = -1;
    
    private final int periode;   
    private BigDecimal capitalDebut;
    private BigDecimal versementTotal;
    private BigDecimal versementInteret;
    private BigDecimal versementCapital;
    private BigDecimal capitalFin;
    private BigDecimal versementTotalCumulatif;
    private BigDecimal versementInteretCumulatif;
    private BigDecimal versementCapitalCumulatif;

    public Amortissement() {
        ++totalPeriodes;
        periode = totalPeriodes;
        versementTotal = new BigDecimal("0");
        versementInteret = new BigDecimal("0");
        versementCapital = new BigDecimal("0");
        versementTotalCumulatif = new BigDecimal("0");
        versementInteretCumulatif = new BigDecimal("0");
        versementCapitalCumulatif = new BigDecimal("0");
    }

    public static void setTotPeriodes(int tp) {
        Amortissement.totalPeriodes = tp;
    }
    
    public void setVersementTotal(BigDecimal montant, int nombreVersement, double interet) {
        if(montant != null){
            double temp = 1 - Math.pow (1 + interet, - nombreVersement);
            temp = interet / temp;
            versementTotal = montant.multiply(new BigDecimal (temp)).setScale (2, BigDecimal.ROUND_HALF_UP);
        }        
    }

    public void setVersementInteret(BigDecimal resteAPayer, double interet) {
        if(resteAPayer != null){
              versementInteret = (resteAPayer.multiply(new BigDecimal(interet))).setScale(2, BigDecimal.ROUND_HALF_UP);
        }      
    }

    public void setVersementCapital(BigDecimal versementTotal, BigDecimal versementInteret) {
        if(versementTotal != null && versementInteret != null){
            versementCapital = (versementTotal.subtract(versementInteret)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }        
    }

    public void setCapitalFin(BigDecimal resteAPayer, BigDecimal versementCapital) {
        if(resteAPayer != null && versementCapital != null){
              capitalFin = (resteAPayer.subtract(versementCapital)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }      
    }

    public void setVersementTotalCumulatif(Amortissement amortissement, BigDecimal versementTotal) {
        if(amortissement != null && versementTotal != null){
             versementTotalCumulatif = (amortissement.versementTotalCumulatif.add(versementTotal)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }       
    }

    public void setVersementInteretCumulatif(Amortissement amortissement, BigDecimal versementInteret) {
        if(amortissement != null && versementInteret != null){
             versementInteretCumulatif = (amortissement.versementInteretCumulatif.add(versementInteret)).setScale(2, BigDecimal.ROUND_HALF_UP); 
        }        
    }

    public void setVersementCapitalCumulatif(Amortissement amortissement, BigDecimal versementCapital) {
        if(amortissement != null && versementCapital != null){
             versementCapitalCumulatif = (amortissement.versementCapitalCumulatif.add(versementCapital)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }       
    }

    public void setCapitalDebut(BigDecimal montant) {
        if(montant != null){
            capitalDebut = montant;
        }        
    }

    public int getPeriode() {
        return periode;
    }
    
    public BigDecimal getCapitalDebut() {
        return capitalDebut;
    }
    
    public BigDecimal getVersementTotal(){
        return versementTotal;
    }

    public BigDecimal getVersementInteret() {
        return versementInteret;
    }

    public BigDecimal getVersementCapital() {
        return versementCapital;
    }

    public BigDecimal getCapitalFin() {
        return capitalFin;
    }

    public BigDecimal getVersementTotalCumulatif() {
        return versementTotalCumulatif;
    }

    public BigDecimal getVersementInteretCumulatif() {
        return versementInteretCumulatif;
    }

    public BigDecimal getVersementCapitalCumulatif() {
        return versementCapitalCumulatif;
    }
}
