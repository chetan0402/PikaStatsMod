package me.chetan.pikastats

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class ForceUpdateMapInfoCommand: CommandBase() {
    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }
    override fun getCommandName(): String {
        return "getmapinfo"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return "/getmapinfo"
    }

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        PikaStatsMod.mapInfo.update()
        TODO("a msg")
    }
}