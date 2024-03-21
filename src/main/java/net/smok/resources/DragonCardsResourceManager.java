package net.smok.resources;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.smok.DragonsCards;
import net.smok.cards.CardLibrary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DragonCardsResourceManager implements SimpleSynchronousResourceReloadListener {
    @Override
    public Identifier getFabricId() {
        return new Identifier(DragonsCards.MOD_ID, "dragons-cards");
    }

    @Override
    public void reload(ResourceManager manager) {
        Map<Identifier, Resource> resources = manager.findResources("cards", path -> path.getPath().endsWith(".json"));
        resources.forEach(this::resourceReload);
    }

    private void resourceReload(Identifier identifier, Resource resource) {
        try {
            JsonElement element = JsonParser.parseReader(resource.getReader());
            String[] pathSplit = identifier.getPath().split("/");
            if (pathSplit.length < 3) {
                DragonsCards.LOGGER.warn("The file '"+identifier.getPath()+"' has incorrect path. The path should be like 'cards/my-collection/file.json'");
                return;
            }

            String collectionId;
            if (pathSplit.length == 3) collectionId = pathSplit[1];
            else {
                List<String> collectionPath = new ArrayList<>(List.of(pathSplit));
                collectionPath.remove(0);
                collectionPath.remove(collectionPath.size() - 1);
                collectionId = String.join("/", collectionPath);
            }
            int last = pathSplit.length - 1;
            String cardId = pathSplit[last].substring(0, pathSplit[last].indexOf("."));
            CardLibrary.registryCard(identifier.getNamespace(), collectionId, cardId, element);

        } catch (IOException e) {
            DragonsCards.LOGGER.error("Error occurred while loading resource json "+identifier.toString(), e);
        }
    }
}
