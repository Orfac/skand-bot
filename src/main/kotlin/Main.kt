import discord4j.core.DiscordClient
import discord4j.core.event.domain.message.MessageCreateEvent
import formatting.AsciiLibraryFormatter
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.mono
import skanderbeg.SkanderbegClient
import skanderbeg.SkanderbegService


suspend fun main() {
    val token = System.getenv("BOT_TOKEN")
    val skanderbegService =
        SkanderbegService(SkanderbegClient(System.getenv("SKANDERBEG_KEY")), AsciiLibraryFormatter())

    val client = DiscordClient.create(token)

    client.withGateway {
        mono {
            it.on(MessageCreateEvent::class.java)
                .asFlow()
                .collect {
                    skanderbegService.processMessage(it.message)
                }
        }
    }.block()
}

suspend fun localRun() {
    val skanderbegClient = SkanderbegClient(System.getenv("SKANDERBEG_KEY"))
    val formatter = AsciiLibraryFormatter()
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