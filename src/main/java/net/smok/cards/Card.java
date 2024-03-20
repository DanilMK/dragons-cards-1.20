package net.smok.cards;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.smok.DragonsCards;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Card {
    // todo make two textures of card
    private final Identifier identifier;
    private String displayName;
    private String description = "";
    private int value = 0;
    private Rarity rarity = Rarity.COMMON;

    public Card(String cardId, CardCollection collection) {
        identifier = new Identifier(DragonsCards.MOD_ID, toValidIdentifier(collection.getName()) + "/" + toValidIdentifier(cardId));
        displayName = cardId;
    }

    public Card(String cardId, CardCollection collection, JsonElement json) {
        identifier = new Identifier(DragonsCards.MOD_ID, toValidIdentifier(collection.getName()) + "/" + cardId);
        JsonObject jsonObject = json.getAsJsonObject();

        if (jsonObject.has("description")) description = jsonObject.get("description").getAsString();
        if (jsonObject.has("displayName")) displayName = jsonObject.get("displayName").getAsString();
        else displayName = identifier.getPath();
        if (jsonObject.has("rarity")) rarity = Rarity.valueOf(jsonObject.get("rarity").getAsString());
        if (jsonObject.has("value")) value = jsonObject.get("value").getAsInt();
    }

    public Card(String cardId, CardCollection collection, String displayName) {
        this(cardId, collection);
        this.displayName = displayName;
    }

    public Card(String cardId, CardCollection collection, String displayName, String description) {
        this(cardId, collection);
        this.displayName = displayName;
        this.description = description;
    }
    public Card(String cardId, CardCollection collection, String displayName, String description, Rarity rarity) {
        this(cardId, collection);
        this.displayName = displayName;
        this.description = description;
        this.rarity = rarity;
    }
    public Card(String cardId, CardCollection collection, String displayName, Rarity rarity) {
        this(cardId, collection);
        this.displayName = displayName;
        this.rarity = rarity;
    }

    @NotNull
    private static String toValidIdentifier(@NotNull String name) {
        return name.toLowerCase().replace(" ", "-");
    }



    @NotNull
    public String getDisplayName() {
        return displayName;
    }

    @NotNull
    public String getDescription() {
        return description;
    }

    @NotNull
    public Rarity getRarity() {
        return rarity;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    @NotNull
    public List<Text> getTooltip()  {
        ArrayList<Text> tooltip = new ArrayList<Text>();
        MutableText textName = Text.empty().append(displayName).formatted(rarity.formatting);
        tooltip.add(textName);
        if (!description.isEmpty()) tooltip.add(Text.of(description));
        return tooltip;
    }

    @Override
    public String toString() {
        return displayName + " | " + rarity + " | " + description;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("displayName", displayName);
        if (!description.isEmpty()) json.addProperty("description", description);
        json.addProperty("rarity", rarity.name());
        json.addProperty("value", value);
        return json;
    }


    // todo add builder with packets
}