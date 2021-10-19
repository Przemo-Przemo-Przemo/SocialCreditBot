package bot.commands

import net.dv8tion.jda.api.entities.TextChannel
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File

@Component
class CommandPong : Command() {
    override val name = "pong"
    private val log = LoggerFactory.getLogger(javaClass)

    override suspend fun run(channel: TextChannel, args: List<String>) {
        val resourcePath = "/xina.jpg"
        val uri = javaClass.getResource(resourcePath)?.toURI()

        if(uri == null) {
            log.error("Resource ${resourcePath} not found")
            return
        }

        val file = File(uri)
        channel.sendFile(file).queue()
    }
}