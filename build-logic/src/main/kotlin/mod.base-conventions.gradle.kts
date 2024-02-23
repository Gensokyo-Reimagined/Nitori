plugins {
  `java-library`

  id("com.github.johnrengelman.shadow")
  id("io.papermc.paperweight.userdev")
}

// Expose version catalog
val libs = extensions.getByType(org.gradle.accessors.dm.LibrariesForLibs::class)

java {
  javaTarget(17)
  withSourcesJar()
}

repositories {
  mavenCentral()
  maven("https://oss.sonatype.org/content/groups/public/")
  maven("https://repo.papermc.io/repository/maven-public/")
  maven("https://repo.spongepowered.org/maven/")
}

dependencies {
  compileOnlyApi(libs.jetbrains.annotations)
}

tasks {
  jar {
    archiveClassifier.set("dev")
  }

  reobfJar {
    remapperArgs.add("--mixin")
  }

  build {
    dependsOn(reobfJar)
  }
}
