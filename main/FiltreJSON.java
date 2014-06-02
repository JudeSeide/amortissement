package main;

import java.io.FilenameFilter;
import java.io.File;

/**
 * Classe pour filtrer les fichiers JSON dans un dossier et ignorer les autres s'il y a lieu.
 */
public class FiltreJSON implements FilenameFilter{
    @Override
    public boolean accept(File f, String nom){
        return (nom.toLowerCase()).endsWith(".json");
    }
}
