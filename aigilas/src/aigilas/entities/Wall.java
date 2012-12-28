package aigilas.entities;

import aigilas.Common;
import sps.bridge.DrawDepths;
import sps.bridge.EntityTypes;
import sps.bridge.SpriteTypes;
import sps.core.Point2;
import sps.entities.Entity;
import sps.graphics.SpriteEdge;

public class Wall extends Entity {
    public Wall(Point2 location) {
        initialize(location, SpriteTypes.get(Common.Entities.Wall), EntityTypes.get(Common.Entities.Wall), DrawDepths.get(Common.Entities.Wall));
        _isBlocking = true;
        _graphic.setAnimationEnabled(false);
    }

    private boolean firstRender = true;

    @Override
    public void update() {
        if (firstRender) {
            firstRender = false;
            _graphic.setEdge(SpriteEdge.determine(_entityType, _location));
        }
    }
}