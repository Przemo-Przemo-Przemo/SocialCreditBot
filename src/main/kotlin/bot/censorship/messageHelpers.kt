package bot.censorship

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

enum class Intent {
    BAD, GOOD, NEUTRAL
}

val neutralKeywords = listOf(
    "真相",
    "派对",
    "伟大的领袖",
    "最高领袖",
    "中国中国中国",
    "共产主义",
    "防空",
    "社会信用评分",
    "🙏🙏🙏🙏🙏",
    "🇨🇳\uD83C\uDDE8\uD83C\uDDF3\uD83C\uDDE8\uD83C\uDDF3"
)

val happyKeywords = listOf(
    "非常好",
    "政府批准",
    "不错不错不错",
    "这让疯子高兴",
    "✅✅"
)

val angryKeywords = listOf(
    "很坏",
    "政府批准",
    "你是假新闻",
    "停在那儿",
    "😡😡😡😡"
)

fun formatMessageAsPropaganda(message: String, intent: Intent): String {
    val pool = mutableListOf<String>()

    pool.add(
        when (intent) {
            Intent.BAD -> angryKeywords.random()
            Intent.GOOD -> happyKeywords.random()
            Intent.NEUTRAL -> neutralKeywords.random()
        }
    )
    pool.add(neutralKeywords.random())
    pool.add(neutralKeywords.random())

    pool.shuffle()

    val pieces = message.split(" ")
    pool.addAll(2, pieces.subList(pieces.size / 2, pieces.size))
    pool.addAll(1, pieces.subList(0, pieces.size / 2))

    return pool.joinToString(" ")
}

fun GuildMessageReceivedEvent.communistReply(str: String, intent: Intent) =
    this.channel.sendMessage(formatMessageAsPropaganda(str, intent)).queue()