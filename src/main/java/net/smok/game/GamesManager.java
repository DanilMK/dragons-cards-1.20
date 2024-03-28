package net.smok.game;

import net.minecraft.entity.player.PlayerEntity;
import net.smok.server.ActionResult;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class GamesManager {
    public static final GamesManager INSTANCE = new GamesManager();


    private final HashMap<PlayerEntity, CardGame> allGames = new HashMap<>();

    public ActionResult createGame(PlayerEntity player, CardGame game) {
        if (allGames.containsKey(player)) return new ActionResult(false, "game.fail.already_playing");
        allGames.put(player, game);
        return new ActionResult(true, "game.created");
    }

    public boolean isPlaying(PlayerEntity player) {
        return allGames.containsKey(player);
    }

    public ActionResult invitePlayer(PlayerEntity inviter, PlayerEntity invited) {
        if (!allGames.containsKey(inviter)) return new ActionResult(false, "game.fail.not_in_game");
        CardGame game = allGames.get(inviter);

        if (game.getState() != CardGame.State.BEFORE_START) return new ActionResult(false, "game.fail.already_started");
        if (allGames.containsKey(invited)) return new ActionResult(false, "game.fail.already_playing");


        ActionResult result = game.addPlayer(invited);
        if (result.success()) allGames.put(invited, game);
        return result;
    }

    public ActionResult startGame(PlayerEntity player) {
        if (!allGames.containsKey(player)) return new ActionResult(false, "game.fail.not_in_game");
        CardGame game = allGames.get(player);

        return game.start();
    }

    public ActionResult endGame(PlayerEntity player) {
        if (!allGames.containsKey(player)) return new ActionResult(false, "game.fail.not_in_game");
        CardGame game = allGames.get(player);

        return game.end();
    }

    public ActionResult leave(PlayerEntity player) {
        if (!allGames.containsKey(player)) return new ActionResult(false, "game.fail.not_in_game");
        CardGame game = allGames.get(player);

        ActionResult result = game.leavePlayer(player);
        if (result.success()) allGames.remove(player);
        return result;

    }



    private void removeGame(@NotNull CardGame game) {
        for (PlayerEntity player : game.getPlayers()) allGames.remove(player);
    }



}
