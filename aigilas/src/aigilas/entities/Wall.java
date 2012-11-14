package aigilas.entities;

import aigilas.Common;
import sps.bridge.DrawDepths;
import sps.bridge.EntityTypes;
import sps.bridge.SpriteTypes;
import sps.core.Core;
import sps.core.Logger;
import sps.core.Point2;
import sps.entities.Entity;
import sps.entities.EntityManager;
import sps.graphics.SpriteEdge;

public class Wall extends Entity {
    public Wall(Point2 location) {
        initialize(location, SpriteTypes.get(Common.Wall), EntityTypes.get(Common.Wall), DrawDepths.get(Common.Wall));
        _isBlocking = true;
    }

    @Override
    public void update(){
        _graphic.setEdge(SpriteEdge.determine(_entityType, _location));
    }
}
