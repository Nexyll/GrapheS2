/*
 * Timer pour le mode automatique
 */
package spaceconquest;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;
import java.util.concurrent.ThreadPoolExecutor;

import spaceconquest.Map.Couleur;
import spaceconquest.Map.Couple;
import spaceconquest.ObjetCeleste.ObjetCeleste;
import spaceconquest.ObjetCeleste.PlaneteLicorne;
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
            Graphe graphe = carte.getGrapheShadok(carte.coords(partie.getShadoLandPosition()), carte.coords(partie.getShadokoPosition()));
            Dijkstra dijkstra = new Dijkstra(graphe);
            ArrayList<Integer> sommetAccessible = dijkstra.sommetsAccessibles(carte.coords(partie.getShadokoPosition()), 2);
            int i = sommetAccessible.get((int)(Math.random()*(sommetAccessible.size()-1)));
            int x = ((i+1) % carte.getTaille() != 0) ? (i+1) % carte.getTaille() : carte.getTaille();
            int y = ((i+1) - x) / carte.getTaille() + 1;
            Couple couple = new Couple (y, x);
            carte.BougerVaisseau(partie.getShadokoPosition(), couple);
            carte.colorationCase(couple, Couleur.Rouge);
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
            ArrayList<PlaneteLicorne> listPlanetesLicornes = new ArrayList<>();

            for (int i = 1; i <= carte.getTaille()*3; i++) {
                for (int j = 1; j <= carte.getTaille(); j++) {
                    ObjetCeleste obj = carte.getCase(i, j).getObjetCeleste();
                    if (obj != null) {
                        if(obj.getType().equalsIgnoreCase("planete licorne"))
                            listPlanetesLicornes.add((PlaneteLicorne)obj);
                    }
                }
            }
            int distanceMin = 9999;
            Couple minCible = partie.getLicoLandPosition();
            for (PlaneteLicorne p : listPlanetesLicornes){
                Dijkstra d = new Dijkstra(carte.getGrapheLicorne());
                int i = carte.coords(p.getPosition());
                int[] dist = d.tableauDistance(i);
                if (dist[i] < distanceMin) {
                    distanceMin = dist[i];
                    minCible = p.getPosition();
                }
            }
            System.out.println("Tour des Licornes !");
            Dijkstra dijkstra = new Dijkstra(carte.getGrapheLicorne());
            int i = dijkstra.sommetIntermediaire(carte.coords(partie.getLicoShipPosition()), carte.coords(minCible), 2);
            int x = ((i+1) % carte.getTaille() != 0) ? (i+1) % carte.getTaille() : carte.getTaille();
            int y = ((i+1) - x) / carte.getTaille() + 1;
            Couple couple = new Couple (y, x);
            carte.BougerVaisseau(partie.getLicoShipPosition(),  couple);
            carte.colorationCase(couple, Couleur.Vert);
            for (PlaneteLicorne p : listPlanetesLicornes) {
                if (p.getPosition().equals(partie.getLicoShipPosition()))
                    stop();
            }
        }
    }    
}


