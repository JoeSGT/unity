package de.randombyte.unity.commands

import de.randombyte.kosp.PlayerExecutedCommand
import de.randombyte.kosp.extensions.toText
import de.randombyte.unity.Config
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.entity.living.player.Player

abstract class UnityCommand(
        val getConfig: () -> Config
) : PlayerExecutedCommand() {
    override fun executedByPlayer(player: Player, args: CommandContext): CommandResult {
        val config = getConfig()
        val unities = config.unities
        val unity = unities.getUnity(player.uniqueId)
                ?: throw CommandException("You must be married to someone to execute this command!".toText()) // TODO: change to 'unity'
        return executedByUnityMember(player, args, unity, config)
    }

    abstract fun executedByUnityMember(player: Player, args: CommandContext, thisUnity: Config.Unity, config: Config): CommandResult
}