package net.smok.cards;

import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Pack {

    private List<Card> cards = new ArrayList<>();
    private int maxSize = 1000;

    public Pack() {}

    public Pack(int maxSize) {
        this.maxSize = maxSize;
    }

    public Pack(CardCollection collection) {
        cards = new ArrayList<>(collection.all());
        maxSize = cards.size();
    }

    public boolean contains(Card card) {
        return cards.contains(card);
    }

    public boolean add(Card card) {
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

    public List<Card> getAll() {
        return cards;
    }

    public int getMaxSize() {
        return maxSize;
    }

    @NotNull
    public List<Text> getTooltip()  {
        List<Text> textList = new ArrayList<>();
        for (Card card : cards) textList.add(Text.of(card.getDisplayName()));
        return textList;
    }

    public void sort() {
        cards.sort(Comparator.naturalOrder());
    }
}
