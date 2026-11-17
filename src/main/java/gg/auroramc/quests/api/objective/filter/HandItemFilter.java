package gg.auroramc.quests.api.objective.filter;

import gg.auroramc.aurora.api.AuroraAPI;
import gg.auroramc.aurora.api.item.TypeId;
import gg.auroramc.quests.api.objective.ObjectiveMeta;
import lombok.AllArgsConstructor;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.entity.Trident;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

@AllArgsConstructor
public class HandItemFilter implements ObjectiveFilter {
    private final Set<TypeId> items;

    @Override
    public boolean filter(ObjectiveMeta meta) {
        var item = meta.getVariable("hand", ItemStack.class).orElseGet(() -> meta.getPlayer().getInventory().getItemInMainHand());
        var itemId = AuroraAPI.getItemManager().resolveId(item);
        return items.contains(itemId);
    }

    public static ObjectiveMeta with(ObjectiveMeta meta, DamageSource source) {
        // Kill credit can outlast the killing blow, so ignore projectiles fired by someone else
        if (source.getCausingEntity() != meta.getPlayer()) return meta;
        var weapon = projectileWeapon(source.getDirectEntity());
        if (weapon != null) meta.setVariable("hand", weapon);
        return meta;
    }

    private static ItemStack projectileWeapon(Entity directEntity) {
        // A trident's item is the weapon itself, an arrow's item is just the arrow
        if (directEntity instanceof Trident trident) return trident.getItem();
        if (directEntity instanceof AbstractArrow arrow) return arrow.getWeapon();
        if (directEntity instanceof ThrowableProjectile thrown) return thrown.getItem();
        return null;
    }
}
