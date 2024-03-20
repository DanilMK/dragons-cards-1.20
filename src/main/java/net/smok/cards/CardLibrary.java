package net.smok.cards;

import com.google.gson.JsonElement;
import net.minecraft.util.Identifier;
import net.smok.DragonsCards;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

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
    public static Card findCardByName(@NotNull String itemName) {
        if (!Identifier.isValid(itemName)) return null;
        return allCards.get(new Identifier(itemName));
    }

}
