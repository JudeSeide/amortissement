Programme qui permet de calculer les modalités de remboursement de prêt.

L'intrant du logiciel est un dossier contenant des fichiers de type JSON.
Chaque fichier contient à son tour les paramètres du prêt à traiter.

Soient :

id;
description;
montant;
nombre annee;
frequence remboursement;
taux d'interet;
frequence composition;
versement periodique;
 
L'extrant du logiciel est un dossier contenant lui aussi des fichiers JSON, 
relatifs aux intrants et qui décrivent les modalités de remboursement du prêt
( table d'amortissement ), ainsi qu'un dossier contenant les mêmes calculs mais 
sous forme de documents HTML.
 
Le programme devrait être appelé par la commande suivante après le build: 
java -jar "nomFichier.jar" repertoireIntrant repertoireExtrantJSON repertoireExtrantHTML

Dependances pour builder :

org.codehaus.jackson.JsonGenerationException;
org.codehaus.jackson.map.JsonMappingException;
org.codehaus.jackson.map.ObjectMapper;

org.apache.ecs.*;
org.apache.ecs.xhtml.*;
