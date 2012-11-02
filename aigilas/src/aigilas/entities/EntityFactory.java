package aigilas.entities;

import sps.bridge.EntityType;
import sps.core.Logger;
import sps.core.Point2;
import sps.entities.Entity;

public class EntityFactory {
    public static Entity create(EntityType type, Point2 location) {
        switch (type) {
            case Floor:
                return new Floor(location);
            case Wall:
                return new Wall(location);
            case Downstairs:
                return new Downstairs(location);
            case Upstairs:
                return new Upstairs(location);
            default:
                Logger.error("An undefined entityType was passed into the EntityFactory: " + type);
                return null;
        }
    }
}
