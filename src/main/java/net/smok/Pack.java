package net.smok;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Pack {

    private String name = "";
    private List<Card> cards = new ArrayList<>();
    private int maxSize = 1000;

    public boolean contains(Card card) {
        return cards.contains(card);
    }

    public boolean add(Card card){
        if (cards.size() >= maxSize) return false;
        cards.add(card);
        return true;
    }

    public boolean take(Card card) {
        return cards.remove(card);
    }

    public List<Card> getInOrder(int amount) {
        ArrayList<Card> result = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            if (i >= maxSize) break;
            result.add(cards.get(i));
        }
        return result;
    }

    List<Card> getInRandom(int amount, Random rng)  {
        ArrayList<Card> result = new ArrayList<>(amount);
        ArrayList<Card> from = new ArrayList<>(cards);
        for (int i = 0; i < amount; i++) {
            if (from.size() <= 0) break;
            Card c = from.get(rng.nextInt(from.size()));

            result.add(c);
            from.remove(c);
        }
        return result;
    }

    public void move(List<Card> moveCards, Pack from) {
        for (Card card : moveCards) if (add(card)) from.take(card);
    }
}
