plugins {
    `java-library`
    `maven-publish`
}

group = "net.serverpeon.testing.compile"
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

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                url.set("https://github.com/Kiskae/compile-testing-extension")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                issueManagement {
                    url.set("https://github.com/Kiskae/compile-testing-extension/issues")
                }
                
                scm {
                    connection.set("scm:git:git://github.com/Kiskae/compile-testing-extension.git")
                    url.set("https://github.com/Kiskae/compile-testing-extension")
                }
            }
        }
    }
}
