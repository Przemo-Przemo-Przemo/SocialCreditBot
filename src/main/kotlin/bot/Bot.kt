package bot

import bot.censorship.Intent
import bot.censorship.communistReply
import bot.commands.Command
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

fun main() {
    Bot().main()
}

@SpringBootApplication
class Bot : ListenerAdapter() {
    val prefix = "<3China"
    val nameOfCommandToCommand: HashMap<String, Command> = HashMap()

    lateinit var jda: JDA

    @Autowired
    lateinit var commands: List<Command>

    fun main() {
        runApplication<Bot>()
    }

    @Bean
    fun startup(config: BotConfiguration): JDA {
        val builder = JDABuilder.createLight(config.discordToken)
            .setActivity(Activity.playing("prefix - $prefix"))

        initializeCommands()

        builder.addEventListeners(this)

        jda = builder.build().awaitReady()

        return jda;
    }

    fun initializeCommands() {
        for(command in commands) {
            nameOfCommandToCommand[command.name] = command
        }
    }

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        GlobalScope.launch { onGuildMessageReceivedAsync(event) }
    }

    private suspend fun onGuildMessageReceivedAsync(event: GuildMessageReceivedEvent) {
        val message = event.message
        val author = message.author

        if(author.isBot) return

        if(message.contentRaw.startsWith(prefix)) {
            val prefixWithCommand = message.contentRaw.split(' ')[0]
            val commandName = prefixWithCommand.substringAfterLast(prefix)

            val command = nameOfCommandToCommand[commandName]

            if(command == null) {
                event.communistReply("Don't make me decrease your social credit for not knowing the command citizen.", Intent.BAD)
//                event.channel.sendMessage("Don't make me decrease your social credit for not knowing the command citizen.").queue()
            } else {
                command.run(event)
            }
        }
    }
}