package net.smok.server;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.smok.cards.Card;
import net.smok.cards.CardLibrary;

import java.util.concurrent.CompletableFuture;

public class SetCardCommand implements CommandRegistrationCallback {

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager
                .literal("setcard")
                .requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.argument("card id", IdentifierArgumentType.identifier())
                        .suggests(SetCardCommand::setCardSuggest)
                        .executes(SetCardCommand::setCard)
                )
        );
    }

    private static CompletableFuture<Suggestions> setCardSuggest(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {

        for (Identifier identifier : CardLibrary.getAllIds()) {
            if (CommandSource.shouldSuggest(builder.getRemaining(), identifier.toString()))
                builder.suggest(identifier.toString());
            else if (CommandSource.shouldSuggest(builder.getRemaining(), identifier.getPath()))
                builder.suggest(identifier.toString());
        }
        return builder.buildFuture();
    }

    private static int setCard(CommandContext<ServerCommandSource> context) {
        Identifier value = IdentifierArgumentType.getIdentifier(context, "card id");
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) return 0;

        ItemStack cardItemStack = player.getStackInHand(Hand.MAIN_HAND);
        if (cardItemStack.isEmpty() || !cardItemStack.isOf(Items.PAPER)) {
            context.getSource().sendMessage(Text.of("No Paper in hand."));
            return 0;
        }
        Card card = CardLibrary.findCardById(value);
        if (card == null) {
            context.getSource().sendMessage(Text.of("This Card not existed."));
            return 0;
        }


        NbtCompound nbt;

        if (cardItemStack.hasNbt()) {
            nbt = cardItemStack.getNbt();
        }
        else nbt = new NbtCompound();
        cardItemStack.setNbt(card.addToNbt(nbt));

        context.getSource().sendMessage(Text.of("Card '"+value+"' apply to this paper."));

        return 1;
    }
}
