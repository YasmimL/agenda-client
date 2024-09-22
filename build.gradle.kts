plugins {
    id("java")
    application
}

application {
    mainClass.set("br.com.ifce.Main")
}

tasks.jar {
    manifest {
        attributes(mapOf("Main-Class" to application.mainClass))
    }
}

group = "br.com.ifce"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}