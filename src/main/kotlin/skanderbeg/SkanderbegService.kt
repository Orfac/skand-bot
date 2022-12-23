package skanderbeg

import formatting.Formatter
import discord4j.core.`object`.entity.Message
import kotlinx.coroutines.reactor.awaitSingle

private const val SORRY_MESSAGE = "Sorry, looks like bot or skanderbeg are not feeling well"

class SkanderbegService(
    private val skanderbegClient: SkanderbegClient,
    private val formatter: Formatter
) {
    suspend fun processMessage(message: Message) {
        if (!message.content.startsWith(">dc ")) {
            return
        }
        val channel = message.channel.block()!!
        val url = message.content.replace(">dc https://skanderbeg.pm/browse.php?id=", "")
        var resultMessage = SORRY_MESSAGE
        var messages = emptyList<String>()
        try {
            val devClicks = skanderbegClient.getCountryDevClicks(url)
            messages = formatter.formatDevClicks(devClicks)
            resultMessage = messages.joinToString()
        } catch (ex: SkanderbegException) {
            println(ex)
            resultMessage = ex.message.toString()
        } catch (ex: Exception) {
            println(ex)
        } finally {
            if (resultMessage.length <= 2000) {
                channel.createMessage(resultMessage).awaitSingle()
            } else {
                messages.forEach {
                    channel.createMessage("```\n$it\n```").awaitSingle()
                }
            }
        }
    }
}