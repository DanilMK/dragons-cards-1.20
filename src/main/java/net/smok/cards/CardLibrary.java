package net.smok.cards;

import com.google.gson.JsonElement;
import net.minecraft.util.Identifier;
import net.smok.DragonsCards;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Set;

public class CardLibrary {
    private static final HashMap<Identifier, CardCollection> collections = new HashMap<>();
    private static final HashMap<Identifier, Card> allCards = new HashMap<>();


    public static void registryCard(String namespace, String collectionId, String cardId, JsonElement json) {
        //DragonsCards.LOGGER.info("Register card "+namespace + ":"+collectionId+"/"+cardId);
        Identifier collectionIdentifier = new Identifier(namespace, collectionId);
        CardCollection collection;
        if (collections.containsKey(collectionIdentifier)) collection = collections.get(collectionIdentifier);
        else {
            collection = new CardCollection(collectionId);
            collections.put(collectionIdentifier, collection);
        }
        Card card = new Card(cardId, collection, json);
        if (allCards.containsKey(card.getIdentifier())) {
            DragonsCards.LOGGER.warn("Card with the same ID already exist");
            return;
        }
        allCards.put(card.getIdentifier(), card);
        collection.put(card);
        //DragonsCards.LOGGER.info("Successful registered card! '"+card.getIdentifier()+"'");

    }
    @Nullable
    public static Card findCardById(@Nullable Identifier id) {
        return allCards.get(id);
    }

    @Nullable
    public static Card findCardByName(@NotNull String itemName) {
        if (!Identifier.isValid(itemName)) return null;
        String[] nameSplit = itemName.split(":");
        if (nameSplit.length == 0 || nameSplit.length == 1) return allCards.get(new Identifier(DragonsCards.MOD_ID, itemName));
        if (nameSplit.length == 2) return allCards.get(new Identifier(nameSplit[0], nameSplit[1]));
        return null;
    }

    public static Set<Identifier> getAllIds() {
        return allCards.keySet();
    }
}
