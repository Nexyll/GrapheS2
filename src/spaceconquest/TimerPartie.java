/*
 * Timer pour le mode automatique
 */
package spaceconquest;

import java.util.TimerTask;
import java.util.Timer;

import spaceconquest.Map.Couleur;
import spaceconquest.Map.Couple;
import spaceconquest.Parties.Mode;

/**
 *
 * @author simonetma
 */
public class TimerPartie extends Timer {

        private Partie partie;                                                  //partie en cours
        private Carte carte;

            
    //constructuer    
    public TimerPartie(Partie partie) {
        super();
        this.partie = partie;
        this.carte = partie.getCarte();
        if(this.partie.getMode() == Mode.automatique) {
            this.scheduleAtFixedRate(new TimerTaskPartie(this.partie), 0, 1000);
        }
    }
    
    //arret du timer si besoin
    public void stop() {
        this.cancel();
    }
    
    //sous classe privée
    private class TimerTaskPartie extends TimerTask {
        
        private Partie partie;
        
        //constructeur
        public TimerTaskPartie(Partie partie) {
            this.partie = partie;
        }
        
        //fonction appellée à chaque tic du timer
        @Override
        public void run() {
            if(this.partie.isIHMReady()) {
                switch(this.partie.getTour()) {
                    case Licorne : this.tourDesLicornes(); break;
                    case Zombie : this.tourDesZombies(); break;
                    case Shadok : this.tourDesShadok(); break;
                }
                    this.partie.tourSuivant();
            }
        }

        private void tourDesShadok() {
            System.out.println("Tour des Shadok !");
            //Dijkstra dijkstra = new Dijkstra();
            carte.colorationMouvements(partie.getShadokoPosition(), carte.getGrapheShadok(carte.coords(partie.getShadoLandPosition()), carte.coords(partie.getShadokoPosition())));
            stop();
        }

        //ce qu'il se passe lors du tour des zombies
        private void tourDesZombies() {
            System.out.println("Tour des Zombies !");
            Dijkstra dijkstra = new Dijkstra(carte.getGrapheZombie());
            int i = dijkstra.sommetIntermediaire(carte.coords(partie.getZombificatorPosition()), carte.coords(partie.getLicoShipPosition()), 2);
            int x = ((i+1) % carte.getTaille() != 0) ? (i+1) % carte.getTaille() : carte.getTaille();
            int y = ((i+1) - x) / carte.getTaille() + 1;
            Couple couple = new Couple (y, x);
            carte.BougerVaisseau(partie.getZombificatorPosition(),  couple);
            carte.colorationCase(couple, Couleur.Jaune);
        }
            
        //ce qu'il se passe lors du tour des licornes
        private void tourDesLicornes() {
            System.out.println("Tour des Licornes !");
            Dijkstra dijkstra = new Dijkstra(carte.getGrapheLicorne());
            int i = dijkstra.sommetIntermediaire(carte.coords(partie.getLicoShipPosition()), carte.coords(partie.getLicoLandPosition()), 2);
            int x = ((i+1) % carte.getTaille() != 0) ? (i+1) % carte.getTaille() : carte.getTaille();
            int y = ((i+1) - x) / carte.getTaille() + 1;
            Couple couple = new Couple (y, x);
            carte.BougerVaisseau(partie.getLicoShipPosition(),  couple);
            carte.colorationCase(couple, Couleur.Vert);
            if (partie.getLicoLandPosition().equals(partie.getLicoShipPosition()))
                stop();
        }
    }    
}


