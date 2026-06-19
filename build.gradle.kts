import com.diluv.schoomp.Webhook
import com.diluv.schoomp.message.Message
import com.diluv.schoomp.message.embed.Embed
import java.io.IOException

plugins {
    alias(libs.plugins.minotaur) apply false
    alias(libs.plugins.curseforgegradle) apply false

    // Required for NeoGradle
    alias(libs.plugins.ideaext)
}

subprojects {

    repositories {
        maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/") { name = "Fuzs  Maven" }
        maven("https://dl.cloudsmith.io/public/tslat/sbl/maven/") { name = "SmartBrainLib (SBL) Maven" }
        maven("https://maven.blamejared.com") { name = "BlameJared Maven" }
        maven("https://dvs1.progwml6.com/files/maven/") { name = "Progwml6 Maven" }
        maven("https://maven.architectury.dev/") { name = "Architectury Maven" }
        maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/") { name = "Geckolib Maven" }
        maven("https://maven.favouriteless.dev/releases") { name = "Favouriteless Maven" }
    }

}