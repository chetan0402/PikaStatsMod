package me.chetan.pikastats.mixin;

import me.chetan.pikastats.map.ChatListener;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiNewChat.class)
public class MixinGuiNewChat {
    @Inject(method="printChatMessageWithOptionalDeletion",at=@At(value="INVOKE",target = "Lnet/minecraft/util/IChatComponent;getUnformattedText()Ljava/lang/String;"))
    public void getChatMessage(IChatComponent chatComponent, int chatLineId, CallbackInfo ci){
        ChatListener.Companion.setCurrentChatMsg(chatComponent);
    }
}
