package bot

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class BotConfiguration(
    // Dirty Secrets :>
    @Value("\${application.discord.botToken}") val discordToken: String,
)