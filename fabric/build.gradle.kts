import net.darkhax.curseforgegradle.TaskPublishCurseForge
import net.fabricmc.loom.task.RemapJarTask

plugins {
    id("enchanted-convention")

    alias(libs.plugins.minotaur)
    alias(libs.plugins.curseforgegradle)
    alias(libs.plugins.loom)
}

version = libs.versions.enchanted.get()
val minecraftVersion = libs.versions.minecraft.asProvider().get();

java {
    sourceCompatibility =  JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

base {
    archivesName = "enchanted-fabric-${minecraftVersion}"
}

repositories {
    maven {
        name = "Parchment"
        url = uri("https://maven.parchmentmc.org")
        content {
            includeGroupAndSubgroups("org.parchmentmc")
        }
    }
}

dependencies {
    compileOnly( project(":common") )
    minecraft( libs.minecraft )
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${libs.versions.parchment.minecraft.get()}:${libs.versions.parchment.asProvider().get()}@zip")
    })

    modImplementation( libs.fabric )
    modImplementation( libs.fabric.api )

    modImplementation( libs.stateobserver.fabric )
    modImplementation( libs.modopedia.fabric )
    modImplementation( libs.geckolib.fabric )
    modImplementation( libs.forgeconfigapi.fabric )

    modCompileOnly( libs.iris.fabric )
    modCompileOnly( libs.jei.fabric.api )

    modRuntimeOnly( libs.jei.fabric )
}

loom {
    accessWidenerPath = file("src/main/resources/enchanted.accesswidener")

    runs {
        named("client") {
            configName = "Fabric Client"

            client()
            ideConfigGenerated(true)
            runDir("runs/$name")
            programArgs("--username=Favouriteless", "--uuid=9410df73-6be3-41d5-a620-51b2e9be667b")
        }

        named("server") {
            configName = "Fabric Server"

            server()
            ideConfigGenerated(true)
            runDir("runs/$name")
        }
    }

    mixin {
        defaultRefmapName.convention("enchanted.refmap.json")
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

    projectId.set("HsbpdVo9")

    versionName.set("Fabric $minecraftVersion")
    versionNumber.set(project.version.toString())
    versionType.set(if(project.version.toString().endsWith("beta")) "beta" else "release")
    uploadFile.set(tasks.named<RemapJarTask>("remapJar"))
    changelog.set(rootProject.file("changelog.txt").readText(Charsets.UTF_8))

    loaders.set(listOf("fabric"))
    gameVersions.set(listOf(minecraftVersion))

    dependencies {
        required.project("fabric-api")
        required.project("stateobserver")
        required.project("modopedia")
        required.project("geckolib")
        required.project("forge-config-api-port")
    }
    //debugMode = true
    //https://github.com/modrinth/minotaur#available-properties
}

tasks.register<TaskPublishCurseForge>("publishToCurseForge") {
    group = "publishing"
    apiToken = System.getenv("CURSEFORGE_TOKEN") ?: "Invalid/No API Token Found"

    val mainFile = upload(560363, tasks.named<RemapJarTask>("remapJar"))
    mainFile.releaseType = if(project.version.toString().endsWith("beta")) "beta" else "release"
    mainFile.changelog = rootProject.file("changelog.txt").readText(Charsets.UTF_8)

    mainFile.addModLoader("Fabric")
    mainFile.addGameVersion(minecraftVersion)
    mainFile.addJavaVersion("Java 21")

    mainFile.addRequirement(
        "fabric-api",
        "stateobserver",
        "geckolib",
        "forge-config-api-port-fabric",
        "modopedia"
    )

    //debugMode = true
    //https://github.com/Darkhax/CurseForgeGradle#available-properties
}

publishing {
    publications {
        create<MavenPublication>("enchanted") {
            from(components["java"])
            artifactId = base.archivesName.get()
        }
    }
}

tasks.named<DefaultTask>("publish").configure {
    finalizedBy("modrinth")
    finalizedBy("publishToCurseForge")
}

