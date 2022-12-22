import discord4j.core.DiscordClient
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.`object`.entity.Message
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.runBlocking
import skanderbeg.SkanderbegClient
import skanderbeg.SkanderbegException


suspend fun main() {
    val token = System.getenv("BOT_TOKEN")
    val skanderbegClient = SkanderbegClient(System.getenv("SKANDERBEG_KEY"))
    val formatter = Formatter()

    val client = DiscordClient.create(token)

    client.withGateway {
        mono {
            it.on(MessageCreateEvent::class.java)
                .asFlow()
                .collect {
                    val message = it.message

                    processMessage(message, skanderbegClient, formatter)
                }
        }
    }
        .block()


}

private suspend fun processMessage(
    message: Message,
    skanderbegClient: SkanderbegClient,
    formatter: Formatter
) {
    if (!message.content.startsWith(">dc ")) {
        return
    }

    val channel = message.channel.block()!!
    val url = message.content.replace(">dc https://skanderbeg.pm/browse.php?id=", "")

    var resultMessage = "Sorry, looks like bot or skanderbeg are not feeling well"
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

suspend fun localRun() {
    val skanderbegClient = SkanderbegClient(System.getenv("SKANDERBEG_KEY"))
    val formatter = Formatter()
    val devClicks = skanderbegClient.getCountryDevClicks("b26ee5")
    val messages = formatter.formatDevClicks(devClicks)
    val resultMessage = messages.joinToString()
    if (resultMessage.length <= 2000) {
        println(resultMessage)
    } else {
        messages.forEach {
            println("```\n$it\n```")
        }
    }
}

data class CountryAndDevClicks(val name: String, val devClicks: Float)