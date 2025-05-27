plugins {
	alias(libs.plugins.kotlin.jvm)            apply false
	alias(libs.plugins.kotlin.plugin.spring)  apply false
	alias(libs.plugins.spring.boot)           apply false
	alias(libs.plugins.spring.dependency.management) apply false
}

allprojects {
	group = "com.feedflow"
	version = "0.0.1-SNAPSHOT"

	repositories {
		mavenCentral()
	}
}