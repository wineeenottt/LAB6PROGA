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
    implementation(project(":COMMAND"))  // Подмодуль COMMAND, если он нужен клиенту
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("org.codehaus.janino:janino:3.1.12")  // Добавляем Janino
}
application {
    mainClass.set("org.wineeenottt.MainClient")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
