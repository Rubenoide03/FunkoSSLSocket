plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.28")
    implementation("org.junit.jupiter:junit-jupiter:5.8.1")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("io.projectreactor:reactor-core:3.5.10")
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("io.r2dbc:r2dbc-h2:1.0.0.RELEASE")
    implementation("io.r2dbc:r2dbc-pool:1.0.0.RELEASE")
    annotationProcessor("io.r2dbc:r2dbc-pool:1.0.0.RELEASE")
    testImplementation("io.r2dbc:r2dbc-pool")
    implementation ("com.h2database:h2")
    testImplementation("org.mockito:mockito-core:5.6.0")
    implementation("org.mockito:mockito-core:5.6.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.6.0")




}

tasks.test {
    useJUnitPlatform()
}