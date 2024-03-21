package net.smok.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.smok.cards.Card;
import net.smok.cards.CardGame;
import net.smok.cards.CardLibrary;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract boolean isOf(Item item);

    @Shadow public abstract boolean hasCustomName();

    @Shadow public abstract Text getName();

    @Shadow public abstract boolean hasNbt();

    @Shadow @Nullable public abstract NbtCompound getNbt();

    @Unique
    public Card getCard() {
        if (!isOf(Items.PAPER) || !hasNbt()) return null;
        String cardName = getNbt().getString("Card");
        return CardLibrary.findCardByName(cardName);
    }

    @Inject(at = @At("TAIL"), method = "getTooltip", cancellable = true)
    private void getTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
        Card card = getCard();
        if (card == null) return;

        CardGame cardGame = CardGame.PlayingGame;
        if (cardGame == null || cardGame.isCardOpen(card))
            cir.setReturnValue(card.getTooltip());
        else cir.setReturnValue(List.of(Text.empty().append("Card").formatted(Formatting.GRAY)));
    }
}
