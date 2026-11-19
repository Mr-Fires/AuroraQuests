package gg.auroramc.quests.api.objective.filter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlaceholderFilterTest {
    private static final String LIST = "Speed II, Fire Resistance I";

    @Test
    void matchesWholeEntriesOnly() {
        assertTrue(PlaceholderFilter.containsEntry(LIST, ", ", "Speed II"));
        assertTrue(PlaceholderFilter.containsEntry(LIST, ", ", "Fire Resistance I"));
        assertFalse(PlaceholderFilter.containsEntry(LIST, ", ", "Speed I"));
    }

    @Test
    void singleEntryEmptyAndCustomSeparator() {
        assertTrue(PlaceholderFilter.containsEntry("Speed II", ", ", "Speed II"));
        assertFalse(PlaceholderFilter.containsEntry("", ", ", "Speed II"));
        assertTrue(PlaceholderFilter.containsEntry("Speed II | Haste I", " | ", "Haste I"));
        assertTrue(PlaceholderFilter.containsEntry("Speed II,Haste I ", ",", "Haste I"));
        assertFalse(PlaceholderFilter.containsEntry("Speed II, Haste I", ", ", "Haste"));
    }
}
