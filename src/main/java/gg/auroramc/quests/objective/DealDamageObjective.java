package gg.auroramc.quests.objective;

import gg.auroramc.aurora.api.AuroraAPI;
import gg.auroramc.quests.api.objective.ObjectiveDefinition;
import gg.auroramc.quests.api.objective.TypedObjective;
import gg.auroramc.quests.api.objective.filter.HandItemFilter;
import gg.auroramc.quests.api.profile.Profile;
import gg.auroramc.quests.api.quest.Quest;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DealDamageObjective extends TypedObjective {
    private final boolean countPlayerDamage;

    public DealDamageObjective(Quest quest, ObjectiveDefinition definition, Profile.TaskDataWrapper data) {
        super(quest, definition, data);
        this.countPlayerDamage = definition.getArgs().getBoolean("count-player-damage", false);
    }

    @Override
    protected void activate() {
        onEvent(EntityDamageByEntityEvent.class, this::handle, EventPriority.MONITOR);
    }

    public void handle(EntityDamageByEntityEvent event) {
        var attacker = event.getDamager() instanceof Projectile projectile ? projectile.getShooter() : event.getDamager();
        if (attacker != data.profile().getPlayer() || event.getEntity() == attacker) return;

        if (event.getEntity() instanceof Player && !countPlayerDamage) {
            return;
        }

        var id = AuroraAPI.getEntityManager().resolveId(event.getEntity());
        progress(event.getFinalDamage(), HandItemFilter.with(meta(id), event.getDamageSource()));
    }
}
