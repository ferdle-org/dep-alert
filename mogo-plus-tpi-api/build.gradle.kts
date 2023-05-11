import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    `maven-publish`
    id("org.openapi.generator") version "6.6.0"
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.spring") version "1.8.20"
}

group = "au.com.adatree.tpi.mogoplus"
version = "0.0.10"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("com.fasterxml.jackson:jackson-bom:2.14.2"))
    implementation("com.fasterxml.jackson.core:jackson-core:2.14.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-ion:2.14.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
    implementation("io.swagger:swagger-parser:1.0.65")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("javax.servlet:javax.servlet-api:3.0.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.0")
    implementation("org.openapitools:jackson-databind-nullable:0.2.0")
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.14")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
    dependsOn("generateMogoPlusTpiApi")
}

tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

task("generateMogoPlusTpiApi", org.openapitools.generator.gradle.plugin.tasks.GenerateTask::class){
    generatorName.set("kotlin-spring")
    inputSpec.set("$projectDir/src/main/resources/open-api/mogo-plus-tpi-api.yaml")
    outputDir.set("$projectDir/src/gen")
    apiPackage.set("au.com.adatree.tpi.mogoplus.api")
    modelPackage.set("au.com.adatree.tpi.mogoplus.api.model")
    apiFilesConstrainedTo.set(emptyList())
    modelNameSuffix.set("Dto")
    configOptions.value(mutableMapOf(
        "dateLibrary" to "java8",
        "delegatePattern" to "true",
        "useBeanValidation" to "true",
        "useTags" to "true",
        "library" to "spring-boot",
        "reactive" to "false",
        "enumPropertyNaming" to "UPPERCASE"
    ))
    globalProperties.set(mutableMapOf(
        "modelDocs" to "false",
        "models" to "",
        "apis" to "false"
    ))
}

sourceSets.main {
    java.srcDirs("$projectDir/src/gen/src/main/kotlin")
    resources.srcDir("$projectDir/src/main/resources")
}

publishing {
    publications {
        create<MavenPublication>("mogo-plus-tpi-api") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            url = if (version.toString().endsWith("SNAPSHOT")) {
                uri("https://adatree-704209381676.d.codeartifact.ap-southeast-2.amazonaws.com/maven/adatree-snapshots")
            } else {
                uri("https://adatree-704209381676.d.codeartifact.ap-southeast-2.amazonaws.com/maven/adatree-releases")
            }
            credentials {
                username = "aws"
                password = System.getenv("CODEARTIFACT_AUTH_TOKEN")
            }
        }
    }
}
