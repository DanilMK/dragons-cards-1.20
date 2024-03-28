package net.smok.server;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.smok.game.CardGame;
import net.smok.game.GamesManager;

public class GameCommand implements CommandRegistrationCallback {
    private static final GamesManager MANAGER = GamesManager.INSTANCE;


    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager
                .literal("cardgame")
                .then(CommandManager.literal("create").executes(this::create))
                .then(CommandManager.literal("start").executes(this::start))
                .then(CommandManager.literal("end").executes(this::end))
                .then(CommandManager.literal("invite").then(CommandManager.argument("player", EntityArgumentType.player()).executes(this::invite)))
                .then(CommandManager.literal("leave").executes(this::leave))
        );
    }

    private int commandResult(PlayerEntity player, ActionResult result) {
        player.sendMessage(result.message(), false);
        return result.success() ? 1 : 0;
    }

    private int leave(CommandContext<ServerCommandSource> context) {
        PlayerEntity player = context.getSource().getPlayer();
        if (player == null) return -1;
        return commandResult(player, MANAGER.leave(player));
    }

    private int invite(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        PlayerEntity player = context.getSource().getPlayer();
        ServerPlayerEntity other = EntityArgumentType.getPlayer(context, "player");
        if (player == null || other == null) return -1;
        return commandResult(player, MANAGER.invitePlayer(player, other));
    }

    private int end(CommandContext<ServerCommandSource> context) {
        PlayerEntity player = context.getSource().getPlayer();
        if (player == null) return -1;
        return commandResult(player, MANAGER.endGame(player));
    }

    private int start(CommandContext<ServerCommandSource> context) {
        PlayerEntity player = context.getSource().getPlayer();
        if (player == null) return -1;
        return commandResult(player, MANAGER.startGame(player));
    }

    private int create(CommandContext<ServerCommandSource> context) {
        PlayerEntity player = context.getSource().getPlayer();
        if (player == null) return -1;
        return commandResult(player, MANAGER.createGame(player, new CardGame()));
    }
}
