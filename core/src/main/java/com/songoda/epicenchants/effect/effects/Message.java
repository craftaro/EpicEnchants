package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import static com.songoda.epicenchants.effect.EffectExecutor.Who.PLAYER;

public class Message extends EffectExecutor {
    public Message(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(Player wearer, Player opponent, int level) {
        String message = getSection().getString("message").replace("{level}", "" + level);

        who().ifPresent(who -> {
            if (who == PLAYER) wearer.sendMessage(message);
            else opponent.sendMessage(message);
        });
    }
}
