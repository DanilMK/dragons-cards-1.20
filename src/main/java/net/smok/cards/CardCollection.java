package net.smok.cards;

import net.minecraft.util.Identifier;
import net.smok.DragonsCards;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;

public class CardCollection {
    private String name = "";
    private HashMap<Identifier, Card> cards = new HashMap<>();

    public CardCollection(String name) {
        this.name = name;
    }

    public CardCollection(HashMap<Identifier, Card> cards) {
        this.cards = cards;
    }

    public CardCollection(Card @NotNull ... cards) {
        this.cards = new HashMap<>();
        for (Card card : cards) {
            this.cards.put(card.getIdentifier(), card);
        }
    }

    public CardCollection(@NotNull Collection<Card> cards) {
        this.cards = new HashMap<>();
        for (Card card : cards) {
            this.cards.put(card.getIdentifier(), card);
        }
    }

    public String getName() {
        return name;
    }

    public void put(Card card) {
        cards.put(card.getIdentifier(), card);
    }

    @Nullable
    public Card find(@NotNull String cardName)  {
        if(cardName.isEmpty() || cards.isEmpty()) return null;
        return cards.get(Identifier.of(DragonsCards.MOD_ID, cardName.toLowerCase()));
    }

    public static CardCollection createStandardFrenchCollection() {
        CardCollection collection = new CardCollection("f52");

        for (int j = 0; j < suits.length; j++) {
            String suit = suits[j];
            String suitDisplay = suitsDisplay[j];
            for (int i = 1; i < 14; i++) {
                Card card = createFrenchCard(suit, i, collection, suitDisplay);
                collection.cards.put(card.getIdentifier(), card);
            }
        }
        return collection;
    }

    private static final String[] suits = new String[] {"Clubs", "Diamonds", "Hearts", "Spades"};
    private static final String[] suitsDisplay = new String[] {"§7♣", "§c♦", "§c♥", "§7♠"};

    private static Card createFrenchCard(String suit, int num, CardCollection collection, String suitDisplay) {
        String strNum = switch (num) {
            case 1 -> "Ace";
            case 11 -> "Jack";
            case 12 -> "Queen";
            case 13 -> "King";
            default -> String.valueOf(num);
        };
        return new Card(suit + " " + strNum, collection, suitDisplay + " " + strNum);
    }

    public Collection<Card> all() {
        return cards.values();
    }
}
