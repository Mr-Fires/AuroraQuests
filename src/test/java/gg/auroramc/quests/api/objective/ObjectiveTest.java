package gg.auroramc.quests.api.objective;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectiveTest {

    @Test
    void normalizeKeysLowercasesAndStripsMinecraftNamespace() {
        assertEquals(Set.of("taiga", "jungle", "librarian"),
                Objective.normalizeKeys(List.of("TAIGA", "minecraft:jungle", "Minecraft:Librarian")));
    }

    @Test
    void normalizeKeysEmptyListGivesEmptySet() {
        assertEquals(Set.of(), Objective.normalizeKeys(List.of()));
    }
}
