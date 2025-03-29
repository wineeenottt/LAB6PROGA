plugins {
    id("java")
}

group = "org.wineeenottt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

}

tasks.test {
    useJUnitPlatform()
}