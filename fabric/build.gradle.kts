import net.darkhax.curseforgegradle.TaskPublishCurseForge
import net.fabricmc.loom.task.RemapJarTask

plugins {
    id("enchanted-convention")

    alias(libs.plugins.minotaur)
    alias(libs.plugins.curseforgegradle)
    alias(libs.plugins.loom)
}

val mod_id: String by project
version = libs.versions.enchanted.get()
val minecraft_version = libs.versions.minecraft.asProvider().get()
val parchment_minecraft_version = libs.versions.parchment.minecraft.get()
val parchment_version = libs.versions.parchment.asProvider().get()
val fabric_version = libs.versions.fabric.asProvider().get()
val fapi_version = libs.versions.fabric.api.get()

java {
    sourceCompatibility =  JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

base {
    archivesName = "${mod_id}-fabric-${minecraft_version}"
}

repositories {
    maven {
        name = "ParchmentMC"
        url = uri("https://maven.parchmentmc.org")
        content {
            includeGroupAndSubgroups("org.parchmentmc")
        }
    }
}


dependencies {
    compileOnly( project(":common") )
    minecraft( libs.minecraft )
    implementation( libs.jsr305 )
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${parchment_minecraft_version}:${parchment_version}@zip")
    })
    modImplementation( libs.fabric )
    modImplementation( libs.fabric.api )

    // Required
    modImplementation( libs.geckolib.fabric )
    modImplementation( libs.patchouli.fabric )
    modImplementation( libs.sbl.fabric )
    modImplementation( libs.stateobserver.fabric )
    modApi( libs.forgeconfigapi.fabric )

    // Optional
    modImplementation( libs.jei.fabric )
}

loom {
	accessWidenerPath = project(":common").file("src/main/resources/${mod_id}.accesswidener")
    runs {
        named("client") {
            configName = "Fabric Client"

            client()
            ideConfigGenerated(true)
            runDir("runs/" + name)
            programArg("--username=Favouriteless")
            programArg("--uuid=9410df73-6be3-41d5-a620-51b2e9be667b")
        }

        named("server") {
            configName = "Fabric Server"

            server()
            ideConfigGenerated(true)
            runDir("runs/" + name)
        }
    }

    mixin {
        defaultRefmapName.convention("${mod_id}.refmap.json")
    }
}

tasks.withType<JavaCompile>().configureEach {
    source(project(":common").sourceSets.getByName("main").allSource)
}

tasks.named<Jar>("sourcesJar").configure {
    from(project(":common").sourceSets.getByName("main").allSource)
}

tasks.withType<Javadoc>().configureEach {
    source(project(":common").sourceSets.getByName("main").allJava)
}

tasks.withType<ProcessResources>().configureEach {
   from(project(":common").sourceSets.getByName("main").resources)
}

modrinth {
    token = System.getenv("MODRINTH_TOKEN") ?: "Invalid/No API Token Found"

    projectId = "HsbpdVo9"
    versionName = "Fabric ${minecraft_version}"
    versionNumber.set(project.version.toString())

    uploadFile.set(tasks.named<RemapJarTask>("remapJar"))
    changelog.set(rootProject.file("changelog.txt").readText(Charsets.UTF_8))

    loaders.set(listOf("fabric"))
    gameVersions.set(listOf(minecraft_version))

    dependencies {
        required.project("fabric-api")
        required.project("geckolib")
        required.project("patchouli")
        required.project("smartbrainlib")
        required.project("stateobserver")
        required.project("forge-config-api-port")
    }
    //debugMode = true
    //https://github.com/modrinth/minotaur#available-properties
}

tasks.register<TaskPublishCurseForge>("publishToCurseForge") {
    group = "publishing"
    apiToken = System.getenv("CURSEFORGE_TOKEN") ?: "Invalid/No API Token Found"

    val mainFile = upload(560363, tasks.remapJar)
    mainFile.releaseType = "release"
    mainFile.addModLoader("Fabric")
    mainFile.addGameVersion(minecraft_version)
    mainFile.addJavaVersion("Java 17")
    mainFile.changelog = rootProject.file("changelog.txt").readText(Charsets.UTF_8)

    mainFile.addRequirement(
        "fabric-api",
        "geckolib",
        "patchouli-fabric",
        "smartbrainlib",
        "stateobserver",
        "forge-config-api-port"
    )

    //debugMode = true
    //https://github.com/Darkhax/CurseForgeGradle#available-properties
}

publishing {
    publications {
        create<MavenPublication>(mod_id) {
            from(components["java"])
            artifactId = base.archivesName.get()
        }
    }
}

tasks.named<DefaultTask>("publish").configure {
    finalizedBy("modrinth")
    finalizedBy("publishToCurseForge")
}

