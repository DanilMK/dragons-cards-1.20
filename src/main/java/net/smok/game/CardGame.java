package net.smok.game;

import net.minecraft.entity.player.PlayerEntity;
import net.smok.server.ActionResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class CardGame {

    private final HashMap<PlayerEntity, PlayerStats>  players = new HashMap<>();

    private State state = State.BEFORE_START;

    public ActionResult start() {
        state = State.PLAYING;
        return new ActionResult(true, "game.start");
    }

    public ActionResult end() {
        state = State.AFTER_END;
        return new ActionResult(true, "game.end");
    }

    public ActionResult addPlayer(PlayerEntity player) {
        players.put(player, new PlayerStats());
        return new ActionResult(true, "game.invited");
    }

    public ActionResult leavePlayer(PlayerEntity player) {
        players.remove(player);
        return new ActionResult(true, "game.leave", player);
    }

    public Collection<PlayerEntity> getPlayers() {
        return players.keySet();
    }

    public State getState() {
        return state;
    }

    public enum State {
        BEFORE_START, AFTER_END, PLAYING
    }

    public record PlayerStats() {


    }
}
