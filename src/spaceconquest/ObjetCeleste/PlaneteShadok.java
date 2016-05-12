package spaceconquest.ObjetCeleste;

/**
 * Created by MrMan on 12/05/2016.
 */
public class PlaneteShadok extends ObjetCeleste{

    public PlaneteShadok() {
        super(GestionnaireImage.getInstance().getImagePlaneteShadok(),0.75);
    }

    @Override
    public String getType() {
        return "planete shadok";
    }
}
