/*
 * Gestion de la carte
 */
package spaceconquest;

import java.util.HashMap;   
import spaceconquest.Map.Case;
import spaceconquest.Map.Couleur;
import spaceconquest.Map.Couple;
import spaceconquest.ObjetCeleste.ObjetCeleste;
import spaceconquest.Race.Race;
import spaceconquest.Race.Vaisseau;

/**
 *
 * @author simonetma
 */


public class Carte {
    private int taille;                                                         //nombre de "colonne" de la map, (la map a 3 fois plus de lignes que de colonnes)
    private HashMap<Couple,Case> cases;                                         //listes des cases
    private Couple caseSelectionnee;                                            //case actuellement sélectionnée par le joueur
    
    //Constructeur
    public Carte(int _taille) {
        this.taille = _taille;
        this.cases = new HashMap<>(); 
        //initialisation de la map vide
        for(int i=1;i<= 3*_taille;i++) {
            for(int j=1;j<=_taille;j++) {
                this.cases.put(new Couple(i,j), new Case());
            }
        } 
        this.caseSelectionnee = null;
    }
    
    private int coords(int i, int j){
        return (j-1)*taille +i;
    }
    
    private int coords(Couple c){
        return (c.getX()-1)*taille +c.getY();
    }
    
    /**
     * Mouvement manquant, exemple de 1 à 7 methode à tester
     * Peut encore être optimisé (calcule de taille*3) test j+1 etc...
     * @return graphe correspondant à la grille
     */
    public Graphe getGrapheGrille(){
        Graphe graphe = new Graphe(taille*taille*3);
        //Ligne
         for (int j = 1; j <= taille*3; j++){
            //Colonne
             for (int i = 1; i <= taille; i++){
                 if(j+1<= taille*3)
                     graphe.ajouterArc(coords(i, j), coords(i, j+1), 1);
                 if(j+2<= taille*3)
                     graphe.ajouterArc(coords(i, j), coords(i, j+2), 1);
                 
                 //Numéro de ligne pair
                 if(j%2 == 0){
                    if(i-1>0 && j+1 <= taille*3)
                        graphe.ajouterArc(coords(i, j), coords(i-1, j+1), 1);
                 }
                 //Numéro de ligne impair
                 else{
                     if(i+1<= taille && j+1<=taille*3)
                        graphe.ajouterArc(coords(i, j), coords(i+1, j+1), 1);
                 }
            }
        }
        return graphe;
    }
    
    /**
     * 
     * @return graphe correspondant au déplacement des zombies
     */
    public Graphe getGrapheZombie(){
        Graphe graphe = new Graphe(taille*taille*3);
        graphe = getGrapheGrille();
        for (int i = 1; i <= taille*3; i++) {
            for (int j = 1; j <= taille; j++) {
                ObjetCeleste obj = getCase(i, j).getObjetCeleste();
                if (obj != null){
                    if(obj.getType().equalsIgnoreCase("etoile"))
                        graphe.isolerSommet(coords(j, i));
                }
            }
        }
        return graphe;
    }
    
    /**
     * 
     * @return graphe correspondant au déplacement des licornes.
     */
    public Graphe getGrapheLicorne(){
        Graphe graphe = new Graphe(taille*taille*3);
        graphe = getGrapheGrille();
        for (int i = 1; i <= taille*3; i++) {
            for (int j = 1; j <= taille; j++) {
                ObjetCeleste obj = getCase(i, j).getObjetCeleste();
                if (obj != null){
                    if(obj.getType().equalsIgnoreCase("etoile"))
                        graphe.isolerSommet(coords(j, i));
                    if(obj.getType().equalsIgnoreCase("asteroide"))
                         graphe.ajouterContrainte(coords(j, i), taille);
                }
            }
        }
        return graphe;
    }
    
    /**
     * Colore toute les cases de la carte en blanc
     */
    public void effacerColoration(){
        for(int i = 1;i <= 3*taille; i++) {
            for(int j = 1; j <= taille; j++) {
                Case cas = getCase(i ,j);
                cas.setCouleur(Couleur.Blanc);
                this.cases.put(new Couple(i, j), cas);
            }
        }
    }
    
    /**
     * 
     * @param c case départ ?
     * @param g graphe modélisant les contraintes de déplacements
     */
    public void colorationMouvements(Couple c, Graphe g){
        //Réinitialisation de la coloration
        this.effacerColoration();
        Dijkstra calculateur = new Dijkstra(g);
        
        //Coloration de la case selectionné en rouge
        Case cas = getCase(c);
        cas.setCouleur(Couleur.Rouge);
        this.cases.put(c, cas);
        
        //On à besoin des sommets en relation avec le sommet coords(c.getX, c.getY)
        int numSommet = coords(c);
        int[] dist = calculateur.calcul(numSommet);
        for(int i = 0 ; i < g.getNbSommet(); i++) {
            int x = ((i+1) % taille != 0) ? (i+1) % taille : taille;
            int y = ((i+1) - x) / taille + 1;
            if (dist[i] == 1) {
                cas = getCase(y, x);
                cas.setCouleur(Couleur.Vert);
                cases.put(new Couple(y, x), cas);
            } else if (dist[i] == 2) {
                cas = getCase(y, x);
                cas.setCouleur(Couleur.Jaune);
                cases.put(new Couple(y, x), cas);
            }
        }
    }
    
    //getteur de la taille de la map
    public int getTaille() {
        return this.taille;
    }
    
    //getteur de la case en position i,j
    public Case getCase(int i,int j) {
        return this.cases.get(new Couple(i,j));
    }
    
    //getteur de la case en position c (couple)
    public Case getCase(Couple c) {
        return this.cases.get(c);
    }
    
    //ajoute un objet celeste (étoile, astéroide...) à la position i,j (Passer par la classe partie !)
    public void addObjetCeleste(ObjetCeleste obj, int i,int j) {
        this.getCase(i, j).addObjetCeleste(obj);
        if(obj != null) {
            obj.setPosition(new Couple(i,j));
        }
    }
    
    //ajoute un vaisseau à la position i,j (Passer par la classe partie !)
    public void addVaisseau(Vaisseau v,int i,int j) {
        this.getCase(i,j).addVaisseau(v);
        if(v !=null) {
            v.setPosition(new Couple(i,j));
        }
    }
    
    //fait bouger le vaisseau présent en case départ à la case arrivée (détruisant tout vaisseau présent à cette case)
    public void BougerVaisseau(Couple depart, Couple arrivee) {
        if(this.getCase(depart).getVaisseau() == null) {
            System.err.println("ERREUR : Aucun vaisseau en case "+depart);
            System.exit(0);
        }
        if(this.getCase(arrivee).getVaisseau() != null) {
            System.out.println("Le "+this.getCase(arrivee).getVaisseau() + " a été détruit !");
            this.getCase(arrivee).getVaisseau().setPosition(null);
        }
        this.getCase(arrivee).addVaisseau(this.getCase(depart).getVaisseau());
        this.getCase(depart).addVaisseau(null);
    }
    
    //méthode gérant ce qu'il se passe quand on clique sur une case en mode manuel
    public void selectionCase(Couple c) {
        if(c.equals(this.caseSelectionnee)) {
            //deselection de la case
            this.effacerColoration();
            this.caseSelectionnee = null;
        }
        else {
            //si une case avait déja été sélectionnée
            if(this.caseSelectionnee != null) {
                //ajouter des conditions de déplacement
                //on fait bouger le vaisseau
                if(this.getCase(caseSelectionnee).getCouleur()!=Couleur.Blanc.getCouleur()) {
                    this.BougerVaisseau(this.caseSelectionnee, c);
                    //on déselectionne la case
                    this.effacerColoration();
                    this.caseSelectionnee = null;
                    //on passe le tour
                    SpaceConquest.tourSuivant();
                }
            }
            else {
                //si aucune case n'avait été selectionné
                //on vérifie que la case nouvellement sélectionné contient un vaisseau du joueur en cours
                if(this.getCase(c).getVaisseau() != null) {
                    if(this.getCase(c).getVaisseau().getRace() == SpaceConquest.getTour()) {
                        //on selectionne la case
                        this.getCase(c).setCouleur(Couleur.Rouge);
                        this.caseSelectionnee = c;
                        colorationMouvements(c, (SpaceConquest.getTour() == Race.Zombie) ? getGrapheZombie() : getGrapheLicorne());
                    }
                }
            }
        }
    }
}
