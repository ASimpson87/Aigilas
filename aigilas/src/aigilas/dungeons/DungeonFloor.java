package aigilas.dungeons;

import aigilas.Aigilas;
import aigilas.Config;
import aigilas.creatures.impl.CreatureFactory;
import aigilas.entities.*;
import aigilas.gods.GodId;
import aigilas.items.ItemFactory;
import aigilas.net.Client;
import sps.bridge.ActorTypes;
import sps.bridge.EntityTypes;
import sps.bridge.Sps;
import sps.core.Logger;
import sps.core.Point2;
import sps.core.RNG;
import sps.core.Settings;
import sps.entities.Entity;
import sps.entities.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class DungeonFloor {
    private static int enemyCapModifier = 0;
    private static int enemyBaseModifier = 0;

    private List<Entity> _contents = new ArrayList<Entity>();
    private final Entity[][] dungeon = new Entity[Settings.get().tileMapWidth][Settings.get().tileMapHeight];
    private final Point2 _upSpawnLocation = new Point2(0, 0);
    private final Point2 _downSpawnLocation = new Point2(0, 0);

    public static void reset() {
        enemyBaseModifier = 0;
        enemyCapModifier = 0;
    }

    public DungeonFloor() {
        generateRooms(true);
        placeAltars();
        placeStairs();
        transferDungeonState();
    }

    public DungeonFloor(Point2 upstairsSpawn) {
        enemyCapModifier++;
        enemyBaseModifier = Config.get().enemyBase + enemyCapModifier / 5;
        EntityManager.get().clear();
        generateRooms(false);
        _upSpawnLocation.copy(upstairsSpawn);
        placeStairs();
        int enemiesToPlace = RNG.next(Config.get().enemyBase + enemyBaseModifier, Config.get().enemyCap + enemyCapModifier);
        if (enemiesToPlace <= 0) {
            enemiesToPlace = 1;
        }
        placeCreatures(enemiesToPlace);
        placeItems(RNG.next(Config.get().itemBase, Config.get().itemCap));
        transferDungeonState();
    }

    public void loadTiles(boolean goingUp) {
        EntityManager.get().clear();
        placeFloor();
        Point2 spawn = goingUp ? _downSpawnLocation : _upSpawnLocation;
        List<Point2> neighbors = spawn.getNeighbors();
        for (Entity player : EntityCache.get().flushCache()) {
            player.setLocation(getRandomNeighbor(neighbors));
            _contents.add(player);
        }
        for (Entity item : _contents) {
            EntityManager.get().addEntity(item);
        }

        //Force an update so that tiles with dynamic sprites render correctly without any flicker
        EntityManager.get().update();
    }

    public void preserveFloor() {
        List<Entity> players = EntityManager.get().getPlayers();
        Logger.info("Detected " + players.size() + " players");
        for (int ii = 0; ii < players.size(); ii++) {
            EntityCache.get().addToCache(players.get(ii));
            EntityManager.get().removeEntity(players.get(ii));
        }
        _contents = new ArrayList<Entity>(EntityCache.get().getEntitiesToCache());
    }

    public Point2 getDownstairsLocation() {
        return _downSpawnLocation;
    }

    public void transferDungeonState() {
        for (Entity[] row : dungeon) {
            for (Entity tile : row) {
                if (tile != null) {
                    if (!EntityCache.get().isReusable(tile.getEntityType())) {
                        _contents.add(tile);
                    }
                    EntityManager.get().addEntity(tile);
                }
            }
        }

        List<Entity> cache = EntityCache.get().flushCache();
        List<Point2> neighbors = _upSpawnLocation.getNeighbors();

        int playerCount = Client.get().getPlayerCount();
        if (cache.size() == 0) {
            if (Config.get().debugFourPlayers) {
                playerCount = 4;
            }
            for (int ii = 0; ii < playerCount; ii++) {
                _contents.add(CreatureFactory.create(ActorTypes.get(Sps.Actors.Player), getRandomNeighbor(neighbors)));
            }
        }
        else {
            for (Entity player : cache) {
                player.setLocation(getRandomNeighbor(neighbors));
            }
            EntityManager.get().addEntities(cache);
            _contents.addAll(cache);
        }
    }

    private Point2 findRandomFreeTile() {
        return findRandomFreeTile(1);
    }

    private Point2 findRandomFreeTile(int buffer) {
        while (true) {
            int x = RNG.next(buffer, Settings.get().tileMapWidth - buffer);
            int y = RNG.next(buffer, Settings.get().tileMapHeight - buffer);
            if (dungeon[x][y].getEntityType() == EntityTypes.get(Sps.Entities.Floor)) {
                return new Point2(x, y);
            }
        }
    }

    Point2 neighborTemp = new Point2(0, 0);

    private Point2 getRandomNeighbor(List<Point2> neighbors) {
        while (neighbors.size() > 0) {
            int neighborIndex = RNG.next(0, neighbors.size());
            neighborTemp = neighbors.get(neighborIndex);
            neighbors.remove(neighborIndex);
            if (!dungeon[neighborTemp.GridX][neighborTemp.GridY].isBlocking()) {
                return neighborTemp;
            }
        }
        return neighborTemp;
    }

    private void generateRooms(boolean altarRoom) {
        for (Tile tile : new FloorPlan(altarRoom).getTiles()) {
            dungeon[tile.X][tile.Y] = EntityFactory.create(tile.EntityType, tile.Position);
        }
    }

    private void placeAltars() {
        int startY = Settings.get().tileMapHeight / 2;
        int startX = Settings.get().tileMapWidth / 3 - 1;
        for (GodId god : GodId.values()) {
            dungeon[startX][startY] = new Altar(new Point2(startX, startY), god);
            startX += 2;
        }

        CreatureFactory.create(ActorTypes.get(Aigilas.Actors.Dummy), new Point2(Settings.get().tileMapWidth / 2, startY - 2));
    }

    private void placeFloor() {
        for (Entity e : EntityCache.get().getReusable(EntityTypes.get(Aigilas.Entities.Darkness))) {
            ((Darkness) e).changeOpacity();
        }
        EntityManager.get().addEntities(EntityCache.get().getReusable(EntityTypes.get(Sps.Entities.Floor)));
        EntityManager.get().addEntities(EntityCache.get().getReusable(EntityTypes.get(Aigilas.Entities.Darkness)));
    }

    private void placeBossesForTesting(int amountOfCreatures) {
        Point2 random = new Point2(findRandomFreeTile());
        dungeon[random.GridX][random.GridY] = CreatureFactory.create(ActorTypes.get(Aigilas.Actors.Sloth), random);
        while (CreatureFactory.bossesRemaining() > 0) {
            CreatureFactory.createNextBoss(Point2.Zero);
        }
    }

    private void placeCreatures(int amountOfCreatures) {
        if (Config.get().bossLevelMod <= 1 || Dungeon.getFloorCount() % Config.get().bossLevelMod == 1) {
            Point2 randomPoint = new Point2(findRandomFreeTile());
            dungeon[randomPoint.GridX][randomPoint.GridY] = CreatureFactory.createNextBoss(randomPoint);
        }
        else {
            while (amountOfCreatures > 0) {
                amountOfCreatures--;
                Point2 randomPoint = new Point2(findRandomFreeTile());
                dungeon[randomPoint.GridX][randomPoint.GridY] = CreatureFactory.createRandom(randomPoint);
            }
        }
    }

    private void placeStairs() {
        if (_upSpawnLocation.isZero()) {
            _upSpawnLocation.copy(findRandomFreeTile(2));
        }
        dungeon[_upSpawnLocation.GridX][_upSpawnLocation.GridY] = new Upstairs(_upSpawnLocation);
        _downSpawnLocation.copy(findRandomFreeTile(2));
        dungeon[_downSpawnLocation.GridX][_downSpawnLocation.GridY] = new Downstairs(_downSpawnLocation);
    }

    private void placeItems(int amountToPlace) {
        while (amountToPlace > 0) {
            amountToPlace--;
            Point2 randomPoint = findRandomFreeTile();
            dungeon[randomPoint.GridX][randomPoint.GridY] = ItemFactory.createRandomPlain(randomPoint);
        }
    }
}
