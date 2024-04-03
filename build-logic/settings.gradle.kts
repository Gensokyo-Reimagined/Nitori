rootProject.name = "ignite-template-build-logic"

dependencyResolutionManagement {
  repositories {
    gradlePluginPortal()
    maven("https://repo.papermc.io/repository/maven-public/")
    val citizensJenkins = ivy {
      name = "citizensRepo"
      url = uri("https://ci.citizensnpcs.co/job")
      patternLayout {
          artifact("/[organisation]/[classifier]/artifact/dist/target/[module]-[revision]-b[classifier].[ext]")
      }
      metadataSources {
        artifact()
      }
    }
    exclusiveContent {
      forRepositories(citizensJenkins)
      filter {
        includeGroup("Citizens2")
      }
    }
  }

  versionCatalogs {
    register("libs") {
      from(files("../gradle/libs.versions.toml")) // include from parent project
    }
  }
}
