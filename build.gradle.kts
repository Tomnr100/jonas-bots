import com.runemate.gradle.RuneMatePlugin
import java.time.Duration
import java.net.URL

plugins {
    java
    id("com.runemate") version "latest.release"
    id("io.freefair.lombok") version "8.6"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

fun getLatestApiVersion(): String {
    val url = "https://gitlab.com/api/v4/projects/32972353/packages/maven/com/runemate/runemate-game-api/maven-metadata.xml"
    val content = URL(url).readText()
    return content.substringAfter("<latest>").substringBefore("</latest>")
}

fun getLatestClientVersion(): String {
    val url = "https://gitlab.com/api/v4/projects/10471880/packages/maven/com/runemate/runemate-client/maven-metadata.xml"
    val content = URL(url).readText()
    return content.substringAfter("<latest>").substringBefore("</latest>")
}

runemate {
    devMode = true
    autoLogin = true
    apiVersion = getLatestApiVersion()
    clientVersion = getLatestClientVersion()
    manifests {
        create("AutoRegear") {
            //This is the fully qualified name of your main class
            mainClass = "com.runemate.jonas.AutoRegear.AutoRegear"

            //A short description that is shown under the bot name on the bot store
            tagline = "Regears automatically"

            //Shown in the bot description in the client
            description = "Regears"

            //The unique internal ID of the bot
            internalId = "Regear-bot"

            //The version of the bot
            version = "1.0.0"

            //The store supports multiple categories, the first will be the "main" category
           // categories(Category.OTHER)

            //This is where you declare the price(s) of the bot
            variants {
                variant(name = "Variant name", price = 0.05)
            }

            //For premium bots, you can declare a "trial" which is a period for which a user can use the bot for free
            trial {
                window = Duration.ofDays(7)
                allowance = Duration.ofHours(3)
            }

            //Declare any resources used by the bot, relative to src/main/resources
            resources {
                include("fxml/example.fxml")
            }



        }
    }
}
