import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import java.util.EnumSet
import java.util.Date

plugins {
    `java-library`
    `maven-publish`
    id("com.jfrog.bintray") version "1.8.4"
}

group = "net.serverpeon.testing.compile"
version = "1.0.0"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("com.google.testing.compile:compile-testing:0.15")

    // jUnit5 API
    api(platform("org.junit:junit-bom:5.3.2"))
    api("org.junit.jupiter:junit-jupiter-api")
    testRuntime("org.junit.jupiter:junit-jupiter-engine")
}

bintray {
    user = (project.findProperty("bintrayUser")
            ?: System.getenv("BINTRAY_USER"))?.toString()
    key = (project.findProperty("bintrayApiKey")
            ?: System.getenv("BINTRAY_API_KEY"))?.toString()

    setPublications("Bintray")

    dryRun = true

    pkg.apply {
        repo = "maven"
        name = "compile-testing-extension"
        setLicenses("Apache-2.0")
        websiteUrl = "https://github.com/Kiskae/compile-testing-extension"
        issueTrackerUrl = "https://github.com/Kiskae/compile-testing-extension/issues"
        vcsUrl = "https://github.com/Kiskae/compile-testing-extension.git"

        githubRepo = "Kiskae/compile-testing-extension"

        version.apply {
            name = "1.0.0"
            vcsTag = "1.0.0"
            released = Date().toString()
        }
    }
}

val sourcesJar by tasks.creating(Jar::class) {
    dependsOn("classes")

    classifier = "sources"
    from(sourceSets["main"].allSource)
}

tasks.withType<Javadoc>().configureEach {
    (options as StandardJavadocDocletOptions).apply {
        // External APIs
        links("https://docs.oracle.com/javase/8/docs/api/")
        linksOffline(
                "https://junit.org/junit5/docs/current/api/",
                file("src/javadoc/junit5").absolutePath
        )

        // No part of the library is deprecated, so this is entirely redundant
        noDeprecatedList(true)
    }
}

val javadocJar by tasks.creating(Jar::class) {
    dependsOn("javadoc")

    classifier = "javadoc"
    from(tasks["javadoc"])
}

operator fun <T> Property<T>.invoke(value: T) {
    this.set(value)
}

publishing {
    publications {
        register("Bintray", MavenPublication::class.java) {
            from(components["java"])
            artifact(sourcesJar)
            artifact(javadocJar)

            pom.licenses {
                license {
                    name("The Apache License, Version 2.0")
                    url("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }

            pom.scm {
                url("https://github.com/Kiskae/compile-testing-extension")
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}