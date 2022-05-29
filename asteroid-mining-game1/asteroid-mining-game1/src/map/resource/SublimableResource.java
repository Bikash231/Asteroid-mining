package map.resource;

import control.ActionFailedException;

/**
 * Class SublimableResource
 * Special resource, sublimates if drilled in perihelion
 */
public abstract class SublimableResource extends Resource {

    public SublimableResource() {
    }

    public void drilledInPerihelion() throws ActionFailedException {
        asteroid.extractResource(); // sublimate (removes the resource from the asteroid)
    }
}
