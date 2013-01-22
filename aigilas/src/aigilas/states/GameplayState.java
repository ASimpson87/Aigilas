package aigilas.states;

import aigilas.Aigilas;
import aigilas.classes.PlayerClassRegistry;
import aigilas.creatures.StatsRegistry;
import aigilas.creatures.impl.CreatureFactory;
import aigilas.dungeons.Dungeon;
import aigilas.dungeons.DungeonFloor;
import aigilas.hud.HudRenderer;
import aigilas.net.Client;
import aigilas.reactions.ReactionRegistry;
import aigilas.skills.SkillRegistry;
import aigilas.statuses.StatusRegistry;
import sps.bridge.EntityTypes;
import sps.bridge.Sps;
import sps.core.Logger;
import sps.entities.Entity;
import sps.entities.EntityManager;
import sps.particles.ParticleEngine;
import sps.states.State;
import sps.states.StateManager;

import java.util.ArrayList;
import java.util.List;

public class GameplayState implements State {
    public GameplayState() {
        Logger.info("Generating the dungeon...");
        SkillRegistry.get();
        StatusRegistry.get();
        ReactionRegistry.get();
        PlayerClassRegistry.get();
        StatsRegistry.get();
        EntityManager.reset();
        EntityManager.get().addCacheType(EntityTypes.get(Aigilas.Entities.Darkness));
        EntityManager.get().addCacheType(EntityTypes.get(Sps.Entities.Floor));
        CreatureFactory.reset();
        Dungeon.start();
        DungeonFloor.reset();
        Client.get().dungeonHasLoaded();
    }

    List<Entity> players = new ArrayList<Entity>();

    @Override
    public void update() {
        players = EntityManager.get().getPlayers();
        boolean allDead = true;
        for (Entity player : players) {
            if (player.isActive()) {
                allDead = false;
            }
        }
        if (allDead) {
            StateManager.loadState(new GameLoseState());
        }
        EntityManager.get().update();
    }

    @Override
    public void load() {
        EntityManager.get().loadContent();
    }

    @Override
    public void unload() {
        EntityManager.get().clear();
        HudRenderer.reset();
        ParticleEngine.reset();
    }

    @Override
    public void draw() {
        EntityManager.get().draw();
    }
}
