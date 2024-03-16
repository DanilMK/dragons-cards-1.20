package net.smok;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Card {
    // todo make two textures of card
    private String name = "";
    private String description = "";
    private Rarity rarity = Rarity.COMMON;

    public Card(String name, String description, Rarity rarity) {
        this.name = name;
        this.description = description;
        this.rarity = rarity;
    }

    public Card(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Card(String name, Rarity rarity) {
        this.name = name;
        this.rarity = rarity;
    }

    public Card(String name) {
        this.name = name;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getDescription() {
        return description;
    }

    @NotNull
    public Rarity getRarity() {
        return rarity;
    }

    @NotNull
    public List<Text> getTooltip()  {
        ArrayList<Text> tooltip = new ArrayList<Text>();
        MutableText textName = Text.empty().append("+"+name).formatted(rarity.formatting);
        tooltip.add(textName);
        if (!description.isEmpty()) tooltip.add(Text.of(description));
        return tooltip;
    }

    @Override
    public String toString() {
        return name + " | " + rarity + " | " + description;
    }
}
