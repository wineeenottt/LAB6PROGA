plugins {
    java
    application
}

group = "org.wineeenottt"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":COMMAND"))
    implementation("ch.qos.logback:logback-classic:1.5.13")
    implementation("ch.qos.logback:logback-core:1.5.13")
    implementation("org.slf4j:slf4j-api:1.7.32")
}

application {
    mainClass.set("org.wineeenottt.MainServer")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
