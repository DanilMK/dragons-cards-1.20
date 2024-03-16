package net.smok;

import net.minecraft.entity.player.PlayerInventory;
import org.jetbrains.annotations.Nullable;

public class CardGame {
    public static CardGame PlayingGame;

    @Nullable
    private PlayerInventory playerInventory;


    private Pack hand = new Pack();
    private Pack takePack = new Pack();
    private Pack offPack = new Pack();

/*
    public Card findCardByName(String cardName) {
        return rules.defaultCollection.find(cardName);
    }*/

    public Boolean isCardOpen(Card card) {
        return hand.contains(card);
    }
}
