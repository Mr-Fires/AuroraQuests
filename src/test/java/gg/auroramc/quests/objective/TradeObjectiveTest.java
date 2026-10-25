package gg.auroramc.quests.objective;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TradeObjectiveTest {

    @Test
    void normalizeLowercasesAndStripsMinecraftNamespace() {
        assertEquals(Set.of("taiga", "jungle", "librarian"),
                TradeObjective.normalize(List.of("TAIGA", "minecraft:jungle", "Minecraft:Librarian")));
    }

    @Test
    void normalizeEmptyListMeansNoRestriction() {
        assertEquals(Set.of(), TradeObjective.normalize(List.of()));
    }
}
