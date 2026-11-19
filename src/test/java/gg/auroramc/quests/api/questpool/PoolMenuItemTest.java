package gg.auroramc.quests.api.questpool;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PoolMenuItemTest {

    private static PoolConfig.PoolMenuItem item(Integer page, List<Integer> pages) {
        var item = new PoolConfig.PoolMenuItem();
        item.setPage(page);
        item.setPages(pages);
        return item;
    }

    @Test
    void pageOnly() {
        var item = item(2, null);
        assertTrue(item.onPage(2));
        assertFalse(item.onPage(1));
        assertEquals(2, item.lastPage());
    }

    @Test
    void pagesOnly() {
        var item = item(null, List.of(2, 3));
        assertTrue(item.onPage(2));
        assertTrue(item.onPage(3));
        assertFalse(item.onPage(1));
        assertEquals(3, item.lastPage());
    }

    @Test
    void pageAndPages() {
        var item = item(5, List.of(2, 3));
        assertTrue(item.onPage(5));
        assertTrue(item.onPage(3));
        assertFalse(item.onPage(4));
        assertEquals(5, item.lastPage());
    }

    @Test
    void nullPageAndEmptyPagesDefaultsToFirst() {
        var item = item(null, List.of());
        assertFalse(item.onPage(1));
        assertEquals(1, item.lastPage());
    }

    @Test
    void nullPageAndNullPagesDefaultsToFirst() {
        var item = item(null, null);
        assertFalse(item.onPage(1));
        assertEquals(1, item.lastPage());
    }
}
