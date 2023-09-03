plugins {
    `java-library`
    `maven-publish`
    `signing`
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
}

group = "io.github.kiskae"
version = "1.0.2-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.testing.compile:compile-testing:0.18")

    // jUnit5 API
    api(platform("org.junit:junit-bom:5.3.2"))
    api("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    withJavadocJar()
    withSourcesJar()

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    withType<Test> {
        useJUnitPlatform()

        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    withType<JavaCompile> {
        options.release.set(8)
    }

    withType<Javadoc> {
        (options as StandardJavadocDocletOptions).apply {
            // External APIs
            links("https://docs.oracle.com/en/java/javase/20/docs/api/")
            linksOffline(
                    "https://junit.org/junit5/docs/current/api/org.junit.jupiter.api/",
                    file("src/javadoc/junit5").absolutePath
            )

            // No part of the library is deprecated, so this is entirely redundant
            noDeprecatedList(true)

            if (JavaVersion.current().isJava9Compatible) {
                addBooleanOption("html5", true)
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            val projectUrl = "github.com/kiskae/compile-testing-extension"

            pom {
                name.set(project.name)
                description.set("JUnit5 extension based on Google's \"compile-testing\" library")
                url.set("https://$projectUrl")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                issueManagement {
                    url.set("https://$projectUrl/issues")
                }

                developers {
                    developer {
                        id.set("kiskae")
                        name.set("Jeroen van Leusen")
                        email.set("jvleusen@gmail.com")
                        url.set("https://github.com/kiskae")
                    }
                }
                
                scm {
                    connection.set("scm:git:git://$projectUrl.git")
                    developerConnection.set("scm:git:git://$projectUrl.git")
                    url.set("https://$projectUrl")
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["mavenJava"])
}
