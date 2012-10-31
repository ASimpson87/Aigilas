package aigilas.entities;

import aigilas.energygement.SpriteType;
import sps.bridge.DrawDepth;
import sps.bridge.EntityType;
import sps.core.Point2;
import sps.entities.Entity;

public class Floor extends Entity {
    public Floor(Point2 location) {
        initialize(location, SpriteType.Floor, EntityType.Floor, DrawDepth.Floor);
    }
}
