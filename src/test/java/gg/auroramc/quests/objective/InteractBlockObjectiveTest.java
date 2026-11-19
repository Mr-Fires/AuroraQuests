package gg.auroramc.quests.objective;

import org.bukkit.event.block.Action;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InteractBlockObjectiveTest {

    @Test
    void emptyListDefaultsToRightClickBlock() {
        assertEquals(EnumSet.of(Action.RIGHT_CLICK_BLOCK), InteractBlockObjective.parseActions(List.of(), name -> {
            throw new AssertionError(name);
        }));
    }

    @Test
    void parsesCaseInsensitiveAndSkipsUnknown() {
        var invalid = new ArrayList<String>();
        var actions = InteractBlockObjective.parseActions(List.of("physical", "LEFT_CLICK_BLOCK", "typo"), invalid::add);

        assertEquals(EnumSet.of(Action.PHYSICAL, Action.LEFT_CLICK_BLOCK), actions);
        assertEquals(List.of("typo"), invalid);
    }
}
