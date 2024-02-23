plugins {
  id("mod.base-conventions")
}

dependencies {
  compileOnly(libs.ignite)
  compileOnly(libs.mixin)
  compileOnly(libs.mixinExtras)

  paperweight.paperDevBundle(libs.versions.paper)
}
