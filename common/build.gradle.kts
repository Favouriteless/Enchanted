import com.diluv.schoomp.Webhook
import com.diluv.schoomp.message.Message
import com.diluv.schoomp.message.embed.Embed
import java.io.IOException

plugins {
    id("enchanted-convention")
    alias(libs.plugins.moddevgradle)
}

version = libs.versions.enchanted.get()
val mcVersion = libs.versions.minecraft.asProvider().get()

base {
    archivesName = "enchanted-common-${mcVersion}"
}

neoForge {
    neoFormVersion = libs.versions.neoform.get()

    accessTransformers.files.setFrom("src/main/resources/META-INF/accesstransformer.cfg")

    parchment {
        minecraftVersion = libs.versions.parchment.minecraft.get()
        mappingsVersion = libs.versions.parchment.asProvider().get()
    }
}

dependencies {
    compileOnly( libs.mixin )
    compileOnly( libs.mixinextras.common )
    compileOnly( libs.stateobserver.common )
    compileOnly( libs.modopedia.common )
    compileOnly( libs.geckolib.common )
    compileOnly( libs.forgeconfigapi.common )
    compileOnly( libs.iris.common )
    compileOnly( libs.jei.common.api )
}

publishing {
    publications {
        create<MavenPublication>("enchanted") {
            from(components["java"])
            artifactId = base.archivesName.get()
        }
    }
}

sourceSets.main.get().resources.srcDir(project(":common").file("src/generated/resources"))

tasks.create("postDiscord") {
    group = "publishing"
    doLast {
        try {
            if(System.getenv("ENCHANTED_RELEASE_WEBHOOK") != null) {
                val webhook = Webhook(System.getenv("ENCHANTED_RELEASE_WEBHOOK"), "Enchanted Gradle Upload")

                val message = Message()
                message.username = "Elaina"
                message.avatarUrl = "https://i.imgur.com/I455GSr.jpeg"
                message.content = "<@&1246846443944149145> Enchanted $version for Minecraft $mcVersion has been published!"

                val changeString = rootProject.file("changelog.txt").readText(Charsets.UTF_8).replace("\r", "")

                val embed = Embed()
                embed.title = "Changelog"
                embed.description = if (changeString.length < 4096) changeString else "The changelog was too long to embed, you can view it on the " +
                        "[GitHub Release](https://github.com/Favouriteless/Enchanted/releases/tag/v$version-release), " +
                        "[CurseForge](https://www.curseforge.com/minecraft/mc-mods/enchanted-witchcraft/files/all) or " +
                        "[Modrinth](https://modrinth.com/mod/enchanted-witchcraft/versions)."
                embed.color = 0x9C58B8

                message.addEmbed(embed)
                webhook.sendMessage(message)
            }
        } catch (e: IOException) {
            project.logger.error("Failed to push CF Discord webhook: " + e.message)
        }
    }
}

tasks.named<DefaultTask>("publish").configure {
    finalizedBy("postDiscord")
}