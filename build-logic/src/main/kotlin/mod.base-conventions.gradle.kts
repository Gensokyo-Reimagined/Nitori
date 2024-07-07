plugins {
  `java-library`

  id("io.github.goooler.shadow")
  id("io.papermc.paperweight.userdev")
  id("maven-publish")
}

// Expose version catalog
val libs = extensions.getByType(org.gradle.accessors.dm.LibrariesForLibs::class)

java {
  javaTarget(21)
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
