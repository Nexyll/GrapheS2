/*
 * Une planete
 */
package spaceconquest.ObjetCeleste;

/**
 *
 * @author simonetma
 */
public class PlaneteLicorne extends ObjetCeleste {
    
    //constructeur
    public PlaneteLicorne() {
        super(GestionnaireImage.getInstance().getImagePlaneteLicorne(),0.75);
    }

    @Override
    public String getType() {
        return "planete licorne";
    }
}
