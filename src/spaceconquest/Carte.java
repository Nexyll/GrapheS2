/*
 * Gestion de la carte
 */
package spaceconquest;

import java.util.ArrayList;
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
    
    public int coords(int i, int j){
        return (j-1)*taille +i;
    }
    
    public int coords(Couple c){
        return (c.getX()-1)*taille +c.getY();
    }

    public Graphe getGrapheGrille(){
        Graphe graphe = new Graphe(taille*taille*3);
        //Ligne
         for (int j = 1; j <= taille*3; j++){
            //Colonne
             for (int i = 1; i <= taille; i++){
                 if(j+1<= taille*3)
                     graphe.ajouterDeuxArc(coords(i, j), coords(i, j+1), 1);
                 if(j+2<= taille*3)
                     graphe.ajouterDeuxArc(coords(i, j), coords(i, j+2), 1);
                 
                 //Numéro de ligne pair
                 if(j%2 == 0){
                    if(i-1>0 && j+1 <= taille*3)
                        graphe.ajouterDeuxArc(coords(i, j), coords(i-1, j+1), 1);
                 }
                 //Numéro de ligne impair
                 else{
                     if(i+1<= taille && j+1<=taille*3)
                        graphe.ajouterDeuxArc(coords(i, j), coords(i+1, j+1), 1);
                 }
            }
        }
        return graphe;
    }

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

    public Graphe getGrapheLicorne(){
        Graphe graphe = new Graphe(taille*taille*3);
        graphe = getGrapheGrille();
        int sommetLicorne = 0;

        for (int l = 1; l <= graphe.getNbSommet() ; l++) {
            int x = ((l) % taille != 0) ? (l) % taille : taille;
            int y = ((l) - x) / taille + 1;
            Vaisseau vaisseauLic = getCase(y, x).getVaisseau();
            if (vaisseauLic!=null){
                if (vaisseauLic.getRace() == Race.Licorne)
                    sommetLicorne = l;
            }
        }

        for (int i = 1; i <= taille*3; i++) {
            for (int j = 1; j <= taille; j++) {
                ObjetCeleste obj = getCase(i, j).getObjetCeleste();
                if (obj != null){
                    if(obj.getType().equalsIgnoreCase("etoile"))
                        graphe.isolerSommet(coords(j, i));
                    if(obj.getType().equalsIgnoreCase("asteroide"))
                         graphe.ajouterContrainte(coords(j, i));
                }
                Vaisseau vaisseau = getCase(i, j).getVaisseau();
                if (vaisseau!=null){
                    if(vaisseau.getRace()==Race.Zombie)
                        graphe.isolerSommet(coords(j, i));

                    if(vaisseau.getRace()==Race.Shadok){
                        Dijkstra dijkstra = new Dijkstra(getGrapheZombie());
                        int[] distShadok = dijkstra.tableauDistance(coords(vaisseau.getPosition()));

                        for (int k = 1; k <= graphe.getNbSommet(); k++) {
                            if (distShadok[k-1] <= 2) {
                                if(k != sommetLicorne)
                                graphe.isolerSommet(k);
                            }
                        }
                    }
                }
            }
        }


        return graphe;
    }

    public Graphe getGrapheShadok(int sommetPlanete, int sommetVaisseau){
        Graphe graphe = getGrapheGrille();
        Dijkstra dijkstra = new Dijkstra(getGrapheZombie());
        int[] distPlanete = dijkstra.tableauDistance(sommetPlanete);
        int[] distVaisseau = dijkstra.tableauDistance(sommetVaisseau);

        graphe.isolerSommet(sommetPlanete);

        for (int i = 1; i <= graphe.getNbSommet(); i++) {
            if (distPlanete[i-1]  > 3) {
                graphe.isolerSommet(i);
            }
        }
        return graphe;
    }
    public void effacerColoration(){
        for(int i = 1;i <= 3*taille; i++) {
            for(int j = 1; j <= taille; j++) {
                Case cas = getCase(i ,j);
                cas.setCouleur(Couleur.Blanc);
                this.cases.put(new Couple(i, j), cas);
            }
        }
    }

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
        int[] dist = calculateur.tableauDistance(numSommet);
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

    public void colorationCase(Couple c, Couleur col){
        getCase(c).setCouleur(col);
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
        this.getCase(depart).getVaisseau().setPosition(arrivee);
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
                if(this.getCase(c).getCouleur() != Couleur.Blanc.getCouleur()) {

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
                        switch (SpaceConquest.getTour()){
                            case Zombie: colorationMouvements(c, getGrapheZombie()); break;
                            case Licorne: colorationMouvements(c, getGrapheLicorne()); break;
                            case Shadok: colorationMouvements(c, getGrapheZombie()); break;
                        }
                        //colorationMouvements(c, (SpaceConquest.getTour() == Race.Zombie) ? getGrapheZombie() : getGrapheLicorne());
                    }
                }
            }
        }
    }
}
