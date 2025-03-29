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
        implementation ("org.slf4j:slf4j-api:2.0.16")
        implementation ("ch.qos.logback:logback-classic:1.5.18")
}
application {
    mainClass.set("org.wineeenottt.MainClient")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
