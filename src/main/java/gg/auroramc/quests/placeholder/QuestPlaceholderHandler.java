package gg.auroramc.quests.placeholder;

import gg.auroramc.aurora.api.AuroraAPI;
import gg.auroramc.aurora.api.placeholder.PlaceholderHandler;
import gg.auroramc.quests.AuroraQuests;
import gg.auroramc.quests.api.quest.Quest;
import gg.auroramc.quests.api.questpool.QuestPool;
import gg.auroramc.quests.util.DurationFormatter;
import gg.auroramc.quests.util.RomanNumber;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class QuestPlaceholderHandler implements PlaceholderHandler {
    @Override
    public String getIdentifier() {
        return "quests";
    }

    @Override
    public String onPlaceholderRequest(Player player, String[] args) {
        if (args.length == 1 && (args[0].equals("tracked") || args[0].equals("tracking"))) {
            var profile = AuroraQuests.getInstance().getProfileManager().getProfile(player);
            if (profile == null) return "";
            boolean on = !profile.getData().isTrackingDisabled();
            if (args[0].equals("tracking")) return String.valueOf(on);
            if (!on) return "";
            var objective = profile.getTracker().getTrackedObjective();
            return objective != null ? objective.display() : "";
        }
        if (args.length < 2) return null;
        var full = String.join("_", args);
        var questTotal = resolveQuestTotal(full);
        if (questTotal != null) return questTotal;
        var profile = AuroraQuests.getInstance().getProfileManager().getProfile(player);
        if(profile == null) return "";

        if (full.endsWith("total_completed_raw")) {
            var sum = profile.getQuestPools().stream().mapToLong(QuestPool::getCompletedQuestCount).sum();
            return String.valueOf(sum);
        } else if (full.endsWith("total_completed")) {
            var sum = profile.getQuestPools().stream().mapToLong(QuestPool::getCompletedQuestCount).sum();
            return AuroraAPI.formatNumber(sum);
        } else if (full.endsWith("level_roman")) {
            var pool = profile.getQuestPool(full.substring(0, full.length() - 12));
            if (pool == null) return null;
            return RomanNumber.toRoman(pool.getLevel());
        } else if (full.endsWith("level_raw")) {
            var pool = profile.getQuestPool(full.substring(0, full.length() - 10));
            if (pool == null) return null;
            return String.valueOf(pool.getLevel());
        } else if (full.endsWith("level")) {
            var pool = profile.getQuestPool(full.substring(0, full.length() - 6));
            if (pool == null) return null;
            return AuroraAPI.formatNumber(pool.getLevel());
        } else if (full.endsWith("current_count")) {
            var pool = profile.getQuestPool(full.substring(0, full.length() - 14));
            if (pool == null) return null;
            return AuroraAPI.formatNumber(pool.getActiveQuests().size());
        } else if (full.endsWith("current_completed")) {
            var pool = profile.getQuestPool(full.substring(0, full.length() - 18));
            if (pool == null) return null;
            return AuroraAPI.formatNumber(pool.getActiveQuests().stream().filter(Quest::isCompleted).count());
        } else if (full.endsWith("count_raw")) {
            var pool = profile.getQuestPool(full.substring(0, full.length() - 10));
            if (pool == null) return null;
            return String.valueOf(pool.getCompletedQuestCount());
        } else if (full.endsWith("count")) {
            var pool = profile.getQuestPool(full.substring(0, full.length() - 6));
            if (pool == null) return null;
            return AuroraAPI.formatNumber(pool.getCompletedQuestCount());
        } else if (full.endsWith("countdown_long")) {
            var pool = profile.getQuestPool(full.substring(0, full.length() - 15));
            if (pool == null) return null;
            if (pool.isGlobal()) return null;
            return DurationFormatter.format(player, pool.getDurationUntilNextRoll(), DurationFormatter.Type.LONG);
        } else if (full.endsWith("countdown")) {
            var pool = profile.getQuestPool(full.substring(0, full.length() - 10));
            if (pool == null) return null;
            if (pool.isGlobal()) return null;
            return DurationFormatter.format(player, pool.getDurationUntilNextRoll(), DurationFormatter.Type.SHORT);
        }

        return null;
    }

    private String resolveQuestTotal(String placeholder) {
        for (var pool : AuroraQuests.getInstance().getPoolManager().getPools()) {
            var definition = pool.getDefinition();
            var prefix = definition.getId() + "_quest_total";
            if (placeholder.equals(prefix)) {
                return AuroraAPI.formatNumber(definition.getQuests().size());
            }
            if (placeholder.startsWith(prefix + "_")) {
                var difficultyId = placeholder.substring(prefix.length() + 1);
                if (!AuroraQuests.getInstance().getConfigManager().getConfig().getDifficulties().containsKey(difficultyId)) return null;
                return AuroraAPI.formatNumber(definition.countQuestsWithDifficulty(difficultyId));
            }
        }
        return null;
    }

    @Override
    public List<String> getPatterns() {
        var manager = AuroraQuests.getInstance().getPoolManager();
        var difficultyIds = AuroraQuests.getInstance().getConfigManager().getConfig().getDifficulties().keySet();

        var list = new ArrayList<String>(manager.getPoolIds().size() * (10 + difficultyIds.size()) + 4);

        list.add("tracked");
        list.add("tracking");
        list.add("total_completed_raw");
        list.add("total_completed");

        for (var pool : manager.getPoolIds()) {
            list.add(pool + "_level");
            list.add(pool + "_level_roman%");
            list.add(pool + "_level_raw");
            list.add(pool + "_count");
            list.add(pool + "_count_raw");
            list.add(pool + "_current_count");
            list.add(pool + "_current_completed");
            list.add(pool + "_countdown");
            list.add(pool + "_countdown_long");
            list.add(pool + "_quest_total");
            for (var difficultyId : difficultyIds) {
                list.add(pool + "_quest_total_" + difficultyId);
            }
        }

        return list;
    }
}
