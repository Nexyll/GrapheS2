/*
 * Classe principale du projet
 */
package spaceconquest;

import spaceconquest.ObjetCeleste.*;
import spaceconquest.Parties.Mode;
import spaceconquest.Partie;
import spaceconquest.Race.Race;

/**
 *
 * @author simonetma
 */
public class SpaceConquest {
    
    private static Partie partie;
    
    public static Race getTour() {
        return partie.getTour();
    }
    
    public static Mode getMode() {
        return partie.getMode();
    }
    

    
    public static void tourSuivant() {
        partie.tourSuivant();
    }
    
    public static void main(String[] args) {
        //on cree la partie
        partie = new Partie(5);
        
        //ajout des éléments clé de la partie
        partie.placerLicoLand(5, 2);
        partie.placerLicoLand(4,3);
        partie.placerShadoLand(14, 2);
        partie.placerLicoShip(5, 5);
        partie.placerShadoko(14, 1);
        partie.placerZombificator(15, 5);
        
        //placement des objets célestes
        partie.placerObjetCeleste(new Etoile(), 3, 3);
        partie.placerObjetCeleste(new Asteroide(), 4, 4);
        partie.placerObjetCeleste(new Asteroide(), 5, 4);
        partie.placerObjetCeleste(new Asteroide(), 5, 3);
                
        //on definit le mode de jeu
        partie.setMode(Mode.automatique);
        //on lance l'IHM

        partie.start();
    }
    
}
