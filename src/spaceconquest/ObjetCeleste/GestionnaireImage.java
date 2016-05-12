/*
 * gestionnaire d'images
 */
package spaceconquest.ObjetCeleste;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import spaceconquest.Race.Race;
import spaceconquest.SpaceConquest;

/**
 *
 * @author simonetma
 */
public class GestionnaireImage {
    
    private static GestionnaireImage instance;                                  //instance de singleton
    
    private BufferedImage fond = null;                                          //image de fond
    private BufferedImage planeteLicorne = null;                                //image de la planete des licornes
    private BufferedImage planeteShadok = null;                                 //Image de la plante des shadoks
    private BufferedImage etoile = null;                                        //image d'une étoile
    private BufferedImage asteroide = null;                                     //image d'un astéroide
    private HashMap<Race,BufferedImage> vaisseaux;                              //images des vaisseaux
    private HashMap<Race,BufferedImage> joueurs;                                //images des joueurs
    
    //constructeur de singleton
    private GestionnaireImage() {
        try {
            //chargement des images en mémoire
             this.fond = ImageIO.read(SpaceConquest.class.getResource("/fond.jpg"));
             this.planeteLicorne = ImageIO.read(SpaceConquest.class.getResource("/planeteLicorne.png"));
             this.planeteShadok = ImageIO.read(SpaceConquest.class.getResource("/planeteShadok.png"));
             this.etoile = ImageIO.read(SpaceConquest.class.getResource("/etoile.png"));
             this.asteroide = ImageIO.read(SpaceConquest.class.getResource("/asteroide.png"));
             this.vaisseaux = new HashMap<>();
             this.vaisseaux.put(Race.Zombie, ImageIO.read(SpaceConquest.class.getResource("/vaisseauZombie.png")));
             this.vaisseaux.put(Race.Licorne, ImageIO.read(SpaceConquest.class.getResource("/vaisseauLicorne.png")));
             this.vaisseaux.put(Race.Shadok, ImageIO.read(SpaceConquest.class.getResource("/vaisseauShadok.png")));
             this.joueurs = new HashMap<>();
             this.joueurs.put(Race.Zombie, ImageIO.read(SpaceConquest.class.getResource("/zombie.png")));
             this.joueurs.put(Race.Licorne, ImageIO.read(SpaceConquest.class.getResource("/licorne.png")));
             this.joueurs.put(Race.Shadok, ImageIO.read(SpaceConquest.class.getResource("/shadok.png")));
        }
        catch (IOException e) {
            //gestion d'erreur
            System.err.println("ERREUR : Impossible de charger les images.");
            System.exit(0);
        }
    }
    
    //instanceur de singleton
    public static GestionnaireImage getInstance() {
        if(instance == null) {
            instance = new GestionnaireImage();
        }
        return instance;
    }
    
    //getteur de l'image de fond
    public BufferedImage getImageFond() {
        return this.fond;
    }
    //getteur de l'image de la planete licorne
    public BufferedImage getImagePlaneteLicorne() {
        return this.planeteLicorne;
    }
    //getteur de l'image de la planete shadok
    public BufferedImage getImagePlaneteShadok() { return  this.planeteShadok; }
    //getteur de l'image d'un astéroide
    public BufferedImage getImageAsteroide() {
        return this.asteroide;
    }
    //getteur de l'image d'une étoile
    public BufferedImage getImageEtoile() {
        return this.etoile;
    }
    
    //getteur de l'image d'un vaisseau
    public BufferedImage getImageVaisseau(Race r) {
        return this.vaisseaux.get(r);
    }
    //getteur de l'image d'un joueur
    public BufferedImage getImageJoueur(Race r) {
        return this.joueurs.get(r);
    }
}
