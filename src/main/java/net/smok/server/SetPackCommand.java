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
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.smok.cards.Card;
import net.smok.cards.CardCollection;
import net.smok.cards.CardLibrary;
import net.smok.cards.Pack;

import java.util.concurrent.CompletableFuture;

public class SetPackCommand implements CommandRegistrationCallback {

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager
                .literal("setpack")
                .requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.argument("collection id", IdentifierArgumentType.identifier())
                        .suggests(SetPackCommand::setPackSuggest)
                        .executes(SetPackCommand::setPack)
                )
        );
    }

    private static CompletableFuture<Suggestions> setPackSuggest(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {

        for (Identifier identifier : CardLibrary.getAllCollectionIds()) {
            if (CommandSource.shouldSuggest(builder.getRemaining(), identifier.toString()))
                builder.suggest(identifier.toString());
            else if (CommandSource.shouldSuggest(builder.getRemaining(), identifier.getPath()))
                builder.suggest(identifier.toString());
        }
        return builder.buildFuture();
    }

    private static int setPack(CommandContext<ServerCommandSource> context) {
        Identifier value = IdentifierArgumentType.getIdentifier(context, "collection id");
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) return 0;

        ItemStack itemStack = player.getStackInHand(Hand.MAIN_HAND);
        if (itemStack.isEmpty() || !itemStack.isOf(Items.PAPER)) {
            context.getSource().sendMessage(Text.of("No Paper in hand."));
            return 0;
        }
        CardCollection collection = CardLibrary.findCollectionById(value);
        if (collection == null) {
            context.getSource().sendMessage(Text.of("This collection not existed."));
            return 0;
        }

        Pack pack = new Pack(collection);
        pack.sort();
        NbtCompound nbt;

        if (itemStack.hasNbt()) {
            nbt = itemStack.getNbt();
        }
        else nbt = new NbtCompound();

        NbtList list = new NbtList();
        for (Card card : pack.getAll()) list.add(NbtString.of(card.getIdentifier().toString()));

        nbt.putInt("PackSize", pack.getMaxSize());
        nbt.put("Pack", list);
        itemStack.setNbt(nbt);

        context.getSource().sendMessage(Text.of("Pack with collection '"+value+"' apply to this paper."));

        return 1;
    }
}
