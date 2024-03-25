package net.smok.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.smok.cards.Card;
import net.smok.cards.CardGame;
import net.smok.cards.CardLibrary;
import net.smok.cards.Pack;
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

    @Shadow public abstract void setNbt(@Nullable NbtCompound nbt);

    @Unique
    public Card getCard() {
        if (!isOf(Items.PAPER) || !hasNbt()) return null;
        String cardName = getNbt().getString("Card");
        return CardLibrary.findCardByName(cardName);
    }

    @Unique
    public void setCard(Card card) {
        NbtCompound nbt;

        if (hasNbt()) nbt = getNbt();
        else nbt = new NbtCompound();

        nbt.putString("Card", card.getIdentifier().toString());
        setNbt(nbt);
    }

    @Unique
    public Pack getPack() {
        if (!isOf(Items.PAPER) || !hasNbt()) return null;

        NbtList packList = (NbtList) getNbt().get("Pack");
        if (packList == null) return null;

        Pack pack = new Pack(getNbt().getInt("PackSize"));
        for (NbtElement nbt : packList) pack.add(CardLibrary.findCardByName(nbt.asString()));

        return pack;
    }

    @Unique
    public void setPack(Pack pack) {
        NbtCompound nbt;

        if (hasNbt()) nbt = getNbt();
        else nbt = new NbtCompound();

        NbtList list = new NbtList();
        for (Card card : pack.getAll()) list.add(NbtString.of(card.getIdentifier().toString()));

        nbt.putInt("PackSize", pack.getMaxSize());
        nbt.put("Pack", list);
        setNbt(nbt);
    }

    @Inject(at = @At("TAIL"), method = "getTooltip", cancellable = true)
    private void getTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {



        Card card = getCard();
        if (card != null) {
            CardGame cardGame = CardGame.PlayingGame;
            if (cardGame == null || cardGame.isCardOpen(card))
                cir.setReturnValue(card.getTooltip());
            else cir.setReturnValue(List.of(Text.empty().append("Card").formatted(Formatting.GRAY)));
            return;
        }
        Pack pack = getPack();
        if (pack != null) {
            cir.setReturnValue(pack.getTooltip());
        }

    }
}
