package de.randombyte.unity.commands

import de.randombyte.kosp.PlayerExecutedCommand
import de.randombyte.kosp.config.ConfigManager
import de.randombyte.kosp.extensions.toText
import de.randombyte.unity.Config
import de.randombyte.unity.Unity
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.entity.living.player.Player
import java.util.*

class DeclineRequestCommand(
        val configManager: ConfigManager<Config>,
        val getRequests: () -> Map<UUID, List<UUID>>,
        val removeRequest: (requester: UUID, requestee: UUID) -> Unit
) : PlayerExecutedCommand() {
    override fun executedByPlayer(player: Player, args: CommandContext): CommandResult {
        val requester = args.getOne<Player>(Unity.PLAYER_ARG).get()
        if (!requester.checkRequester(requestee = player, requests = getRequests())) {
            throw CommandException("You don't have a request from '${requester.name}'!".toText())
        }

        removeRequest(requester.uniqueId, player.uniqueId)

        val broadcastMessage = configManager.get().texts.declinedRequestBroadcast.apply(mapOf(
                "requester" to requester.name,
                "requestee" to player.name)).build()
        broadcast(broadcastMessage)

        return CommandResult.success()
    }
}