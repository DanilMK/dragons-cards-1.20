package net.smok;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CardCollection {
    private String name = "";
    private HashMap<String, Card> cards = new HashMap<>();

    public CardCollection(String name) {
        this.name = name;
    }

    public CardCollection(HashMap<String, Card> cards) {
        this.cards = cards;
    }

    public CardCollection(Card @NotNull ... cards) {
        this.cards = new HashMap<>();
        for (Card card : cards) {
            this.cards.put(card.getName().toLowerCase(), card);
        }
    }

    public CardCollection(@NotNull Collection<Card> cards) {
        this.cards = new HashMap<>();
        for (Card card : cards) {
            this.cards.put(card.getName(), card);
        }
    }

    @Nullable
    public Card find(@NotNull String cardName)  {
        if(cardName.isEmpty() || cards.isEmpty()) return null;
        return cards.get(cardName.toLowerCase());
    }
}
