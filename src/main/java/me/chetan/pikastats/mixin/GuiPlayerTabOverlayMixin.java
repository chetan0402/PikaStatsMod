package me.chetan.pikastats.mixin;

import me.chetan.pikastats.PikaAPI;
import me.chetan.pikastats.PikaStatsMod;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GuiPlayerTabOverlay.class)
public class GuiPlayerTabOverlayMixin{
    /**
     * @author Chetan0402
     * @reason to put fkdr
     */
    @Overwrite
    public String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
        String to_return=networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
        String username= PikaAPI.INSTANCE.getUsername(to_return);
        return to_return+ PikaStatsMod.userCache.getCache(username);
    }
}
