import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("io.spring.dependency-management") version "1.1.0"
    id("org.springframework.boot") version "2.6.0"
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.spring") version "1.8.20"
}

java.sourceCompatibility = JavaVersion.VERSION_17

"au.com.adatree.tpi.mogoplus".also { group = it }
version = "0.0.1"

repositories {
    maven {
        url = uri("https://adatree-704209381676.d.codeartifact.ap-southeast-2.amazonaws.com/maven/adatree-releases/")
        credentials {
            username = "aws"
            password = System.getenv("CODEARTIFACT_AUTH_TOKEN")
        }
    }
    maven {
        url = uri("https://adatree-704209381676.d.codeartifact.ap-southeast-2.amazonaws.com/maven/adatree-snapshots/")
        credentials {
            username = "aws"
            password = System.getenv("CODEARTIFACT_AUTH_TOKEN")
        }
    }
    mavenCentral()
}

dependencies {
    implementation(project(":mogo-plus-api"))
    implementation(project(":mogo-plus-tpi-api"))
    implementation(platform("io.awspring.cloud:spring-cloud-aws-dependencies:2.4.4"))
    implementation(platform("software.amazon.awssdk:bom:2.20.53"))
    implementation("au.com.adatree:adatree-auth:1.15.3") {
        exclude("org.apache.logging.log4j", "log4j-api")
        exclude("org.apache.logging.log4j", "log4j-to-slf4j")
    }

    implementation("com.auth0:java-jwt:4.3.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.16.0")
    implementation("io.awspring.cloud:spring-cloud-starter-aws-messaging:2.4.4")
    implementation("net.logstash.logback:logstash-logback-encoder:7.1.1")
    implementation("org.apache.httpcomponents:httpclient:4.5.14")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("software.amazon.awssdk:secretsmanager")
    implementation("software.amazon.awssdk:sqs")
    implementation("software.amazon.awssdk:dynamodb-enhanced")
    testImplementation("com.ninja-squad:springmockk:3.1.1")
    testImplementation("com.tyro.oss:random-data:1.0.0")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.4.31")
    testImplementation ("org.mockito:mockito-inline:2.13.0")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
    enabled = false
}
