import discord4j.core.DiscordClient
import discord4j.core.event.domain.message.MessageCreateEvent
import kotlinx.coroutines.runBlocking
import skanderbeg.SkanderbegClient
import skanderbeg.SkanderbegException


suspend fun main(args: Array<String>) {
    val token = System.getenv("BOT_TOKEN")
    val skanderbegClient = SkanderbegClient()
    val formatter = Formatter()
    val client = DiscordClient.create(token)

    val gateway = client.login().block()


    gateway.on(MessageCreateEvent::class.java).subscribe { event: MessageCreateEvent ->
        val message = event.message
        if (message.content.startsWith(">dc ")) {
            val channel = message.channel.block()
            val url = message.content.replace(">dc https://skanderbeg.pm/browse.php?id=", "")
            runBlocking {
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
                    if (resultMessage.length <= 2000){
                        channel.createMessage(resultMessage).block()
                    } else {
                        messages.forEach {
                            channel.createMessage("```\n$it\n```").block()
                        }
                    }

                }

            }

        }
    }

    gateway.onDisconnect().block()



}

suspend fun localRun(){
    val skanderbegClient = SkanderbegClient()
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