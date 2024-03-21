package net.smok.resources;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryWrapper;
import net.smok.cards.Card;
import net.smok.cards.CardCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DragonCardsDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(CardCollectionDataProvider::new);

    }

    private static class CardCollectionDataProvider implements DataProvider {

        private final DataOutput.PathResolver pathResolver;

        public CardCollectionDataProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {

            this.pathResolver = output.getResolver(DataOutput.OutputType.DATA_PACK, "cards");
        }

        @Override
        public CompletableFuture<?> run(DataWriter writer) {

            final List<CompletableFuture<?>> futures = new ArrayList<>();


            CardCollection collection = CardCollection.createStandardFrenchCollection();
            for (Card frenchCard : collection.all()) {
                futures.add(DataProvider.writeToPath(writer, frenchCard.toJson(), pathResolver.resolveJson(frenchCard.getIdentifier())));
            }

            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        }


        @Override
        public String getName() {
            return "CardCollection";
        }
    }
}
