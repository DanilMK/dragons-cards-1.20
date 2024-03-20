package net.smok;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.smok.resources.DragonCardsResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DragonsCards implements ModInitializer {
	public static final String MOD_ID = "dragons-cards";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new DragonCardsResourceManager());
	}
}