import io.github.redstonneur1256.gaw.AccessWidenerExtension

plugins {
  `kotlin-dsl`
  id("io.github.redstonneur1256.gradle-access-widener") version "0.3.1"
}

dependencies {
  implementation(libs.build.paperweight)
  implementation(libs.build.shadow)
  implementation(libs.build.spotless)
}

dependencies {
  compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
  target {
    compilations.configureEach {
      kotlinOptions {
        jvmTarget = "21"
      }
    }
  }
}

configure<AccessWidenerExtension> {
  paths = files("../src/main/resources/nitori.accesswidener")
}
