/*
 * Classe de gestion des graphes
 */
package spaceconquest;

import java.util.HashMap;
import spaceconquest.Map.Couple;

/**
 *
 * @author simonetma
 */

public class Graphe {
    private int nbSommet;
    private HashMap<Couple,Integer> matrice;
    private Boolean orienté;
    
    //constructeur
    public Graphe(int n) {
        this.nbSommet = n;
        this.matrice = new HashMap<>();
        this.orienté = false;
    }
    
    //renvoie le nombre de sommet du graphe    
    public int getNbSommet() {
        return this.nbSommet;
    }
    
    //*************** gestion de la matrice d'adjacence ***********************
    //Modifie la valeur (i,j) de la matrice d'adjacence du graphe
    public void modifierMatrice(int i,int j,int valeur) {
        if(i<=0 || j<=0) {
            System.err.println("Erreur ! La matrice d'adjacence ne possède pas de coefficient ("+i+","+j+") !");
        }
        else if(i>this.nbSommet || j>this.nbSommet) {
            System.err.println("Erreur ! La matrice d'adjacence ne possède pas de coefficient ("+i+","+j+") !");
        }
        else
        {
            Couple c = new Couple(i,j);
            this.matrice.put(c, valeur);
        }
    }
    
    public void ajouterArc(int deb, int fin, int l) {
        if (this.orienté){
            this.modifierMatrice(deb, fin, l);
        }
        else {
            this.modifierMatrice(deb, fin, l);
            this.modifierMatrice(fin, deb, l);
        }
    }
    /**
     * Détruit tout les liens entre le sommet i et les autres sommets.
     * @param i numéro du sommet
     */
    public void isolerSommet(int i){
        for (int j = 1; j <= this.nbSommet; j++) {
            this.ajouterArc(i, j, 0);
        }
    }
    
    /**
     * Méthode servant à traduire la contrainte de déplacement des licornes.
     * @param i numéro du sommet
     * @param contrainte le nombre de la contrainte (2 pour les licornes)
     */
    public void ajouterContrainte(int i, int contrainte){
        for (int j = 1; j <= this.nbSommet; j++) {
            this.ajouterArc(i, j, contrainte);
        }
    }
    
    //renvoie la valeur du coefficient (i,j) de la matrice d'adjacence (0 par défaut)
    public int getMatrice(int i, int j) {
        if(i<=0 || j<=0) {
            System.err.println("Erreur ! La matrice d'adjacence ne possède pas de coefficient ("+i+","+j+") !");
        }
        else if(i>this.nbSommet || j>this.nbSommet) {
            System.err.println("Erreur ! La matrice d'adjacence ne possède pas de coefficient ("+i+","+j+") !");
        }
        else {
            Couple c = new Couple(i,j);
            if(this.matrice.containsKey(c)) {
                return this.matrice.get(c);
            }
        }
        return 0;
    }
    
    //renvoie l'orientation
    public boolean getOrientation() {
        return this.orienté;
    }
    
    //affiche la matrice d'adjaceance
    @Override
    public String toString() {
        String ret = "<html><center>Matrice du graphe :<br><br>";
        for(int i=1;i<=this.nbSommet;i++) {
            for(int j=1;j<=this.nbSommet;j++) {
                Couple c = new Couple(i,j);
                if(this.matrice.containsKey(c)) {
                    ret += this.matrice.get(c);
                }
                else {
                    ret += "0";
                }
                if(j<this.nbSommet) {
                    ret+= " ";
                }
            }
            if(i<this.nbSommet) {
                ret+="<br>";
            }
        }
        ret += "</center></html>";
        return ret;
    }
    
    
    //
}
