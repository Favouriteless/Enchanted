plugins {
    `java`
    `maven-publish`
    `idea`
    `eclipse`
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)

    withSourcesJar()
    withJavadocJar()
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

val libs = project.versionCatalogs.find("libs")
val version = libs.get().findVersion("enchanted").get()

val minecraftVersion = libs.get().findVersion("minecraft").get()
val minecraftVersionRange = libs.get().findVersion("minecraft.range").get()
val neoVersion = libs.get().findVersion("neoforge").get()
val neoVersionRange = libs.get().findVersion("neoforge.range").get()
val neoLoaderRange = libs.get().findVersion("neoforge.loader.range").get()
val fapiVersion = libs.get().findVersion("fabric.api").get()
val fabricVersion = libs.get().findVersion("fabric").get()

val stateobserverVersion = libs.get().findVersion("stateobserver").get()
val geckoVersion = libs.get().findVersion("geckolib").get()
val modopediaVersion = libs.get().findVersion("modopedia").get()
val fconfapiVersion = libs.get().findVersion("forgeconfigapi").get()

tasks.withType<Jar>().configureEach {
    from(rootProject.file("LICENSE")) {
        rename { "${it}_Enchanted" }
    }

    manifest {
        attributes(mapOf(
            "Specification-Title"     to "Enchanted",
            "Specification-Vendor"    to "Favouriteless",
            "Specification-Version"   to version,
            "Implementation-Title"    to "Enchanted",
            "Implementation-Version"  to version,
            "Implementation-Vendor"   to "Favouriteless",
            "Built-On-Minecraft"      to minecraftVersion
        ))
    }
}

tasks.withType<JavaCompile>().configureEach {
    this.options.encoding = "UTF-8"
    this.options.getRelease().set(21)
}

tasks.withType<ProcessResources>().configureEach {
    val expandProps = mapOf(
        "version" to version,
        "minecraft_version" to minecraftVersion,
        "neoforge_version" to neoVersion,
        "neoforge_version_range" to neoVersionRange,
        "neoforge_loader_range" to neoLoaderRange,
        "minecraft_version_range" to minecraftVersionRange,
        "fabric_api_version" to fapiVersion,
        "fabric_loader_version" to fabricVersion,
        "stateobserver_version" to stateobserverVersion,
        "geckolib_version" to geckoVersion,
        "forgeconfigapi_version" to fconfapiVersion,
        "modopedia_version" to modopediaVersion
    )

    filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "META-INF/neoforge.mods.toml", "*.mixins.json")) {
        expand(expandProps)
    }

    inputs.properties(expandProps)
}

tasks.withType<GenerateModuleMetadata>().configureEach {
    enabled = false
}

publishing {
    repositories {
        if (System.getenv("FAVOURITELESS_MAVEN_USER") == null && System.getenv("FAVOURITELESS_MAVEN_PASS") == null) {
            mavenLocal()
        }
        else maven {
            name = "FavouritelessReleases"
            url = uri("https://maven.favouriteless.net/releases")

            credentials {
                username = System.getenv("FAVOURITELESS_MAVEN_USER")
                password = System.getenv("FAVOURITELESS_MAVEN_PASS")
            }
        }
    }
}