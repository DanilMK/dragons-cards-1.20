package net.smok.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.smok.cards.Card;
import net.smok.cards.CardGame;
import net.smok.cards.CardLibrary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract boolean isOf(Item item);

    @Shadow public abstract boolean hasCustomName();

    @Shadow public abstract Text getName();

    @Inject(at = @At("TAIL"), method = "getTooltip", cancellable = true)
    private void getTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
        if (!isOf(Items.PAPER) || !hasCustomName()) return;

        Card card = CardLibrary.findCardByName(getName().getString());
        if (card == null) return;

        CardGame cardGame = CardGame.PlayingGame;
        if (cardGame == null || cardGame.isCardOpen(card))
            cir.setReturnValue(card.getTooltip());
        else cir.setReturnValue(List.of(Text.empty().append("Card").formatted(Formatting.GRAY)));
    }
}
