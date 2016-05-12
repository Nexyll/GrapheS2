/*
 * partie en cours
 */
package spaceconquest;

import spaceconquest.IHM.Fenetre;
import spaceconquest.Map.Couple;
import spaceconquest.ObjetCeleste.ObjetCeleste;
import spaceconquest.ObjetCeleste.PlaneteLicorne;
import spaceconquest.ObjetCeleste.PlaneteShadok;
import spaceconquest.Parties.Mode;
import spaceconquest.Race.Race;
import spaceconquest.Race.Vaisseau;

/**
 *
 * @author simonetma
 */
public class Partie {
    private Race tour;                                                          //A qui c'est de jouer
    private Mode mode;                                                          //Quel mode de jeu ?
    private final Carte carte;                                                  //Carte de la partie
    private Fenetre fenetre;                                                    //Fenetre de l'IHM
    
    private PlaneteLicorne LicoLand;                                            //monde d'origine des licornes
    private PlaneteShadok ShadoLand;                                            //Planete shadok
    private Vaisseau LicoShip;                                                  //Vaisseau Licorne
    private Vaisseau Zombificator;                                              //Vaisseau Zombie
    private Vaisseau Shadoko;                                                   //Vaisseau shadok
   
    private TimerPartie timer;                                                  //timer pour le mode automatique
    
    //Constructeur
    public Partie(int taille) {
        this.tour = Race.Licorne;
        this.mode = Mode.manuel;
        this.carte = new Carte(taille);
        
        this.LicoLand = null;
        this.ShadoLand = null;
        this.LicoShip = null;
        this.Shadoko = null;
        this.Zombificator = null;
    }
    
    //création de LicoLand 
    public void placerLicoLand(int i,int j) {
        LicoLand = new PlaneteLicorne();
        this.carte.addObjetCeleste(LicoLand, i, j);
    }

    //création de shadoLand
    public void placerShadoLand(int i, int j) {
        ShadoLand = new PlaneteShadok();
        this.carte.addObjetCeleste(ShadoLand, i, j);
    }
    
    //création du LicoShip 
    public void placerLicoShip(int i,int j) {
        this.LicoShip = new Vaisseau(Race.Licorne);
        this.carte.addVaisseau(LicoShip, i, j);
    }

    //création du shadoko
    public void placerShadoko(int i, int j){
        this.Shadoko = new Vaisseau(Race.Shadok);
        this.carte.addVaisseau(Shadoko, i, j);
    }

    //création du Zombificator 
    public void placerZombificator(int i,int j) {
        this.Zombificator = new Vaisseau(Race.Zombie);
        this.carte.addVaisseau(Zombificator, i, j);
    }
    
    //création d'objet céleste
    public void placerObjetCeleste(ObjetCeleste objet,int i,int j) {
        carte.addObjetCeleste(objet, i, j);
    }
    //renvoie le joueur dont c'est le tour
    public Race getTour() {
        return this.tour;
    }
    
    //renvoie le mode de jeu
    public Mode getMode() {
        return this.mode;
    }
    
    //renvoie si l'IHM est prete
    public boolean isIHMReady() {
        return (this.fenetre != null);
    }
    
    //fixe le mode de jeu
    public void setMode(Mode _mode) {
        this.mode = _mode;
    }
    
    //passe le tour (dans les deux modes de jeu)
    public void tourSuivant() {
        if(tour == Race.Zombie) {
            tour = Race.Licorne;
        }
        else {
            tour = Race.Zombie;
        }
        this.fenetre.refreshSide();
        
    }
    
    //renvoie la carte de la partie
    public Carte getCarte() {
        return this.carte;
    }

    public Couple getLicoShipPosition(){
        return LicoShip.getPosition();
    }

    public Couple getZombificatorPosition(){
        return Zombificator.getPosition();
    }

    //démarre la partie
    public void start() {
        //lance le timer
        timer = new TimerPartie(this);
        //lancement de l'IHM
        this.fenetre = new Fenetre(this.carte);
        
        Graphe licorne = carte.getGrapheLicorne();
    }
}
