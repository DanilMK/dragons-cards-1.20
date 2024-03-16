package net.smok;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CardLibrary {
    private static final List<CardCollection> allCollections = new ArrayList<>();
    static {
        allCollections.add(new CardCollection(new Card("ace"), new Card("One"), new Card("Red Two")));
    }

    @Nullable
    public static Card findCardByName(@NotNull String cardName) {
        for (CardCollection collection : allCollections) {
            Card card = collection.find(cardName);
            if (card != null) return card;
        }
        return null;
    }

}
