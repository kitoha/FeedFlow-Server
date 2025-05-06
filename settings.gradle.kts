plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "feedflow"

include("domain")
include("application")
include("infrastructure")
include("api")
