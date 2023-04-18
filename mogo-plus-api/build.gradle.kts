import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    `maven-publish`
    id("org.openapi.generator") version "6.5.0"
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.spring") version "1.8.20"
}

group = "au.com.adatree.tpi.mogoplus"
version = "0.0.2"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("com.fasterxml.jackson:jackson-bom:2.14.2"))
    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-ion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.20")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.20")
    implementation("org.openapitools:jackson-databind-nullable:0.2.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
    dependsOn("generateMogoPlusApi")
}

tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

task("generateMogoPlusApi", org.openapitools.generator.gradle.plugin.tasks.GenerateTask::class){
    generatorName.set("kotlin")
    inputSpec.set("$projectDir/src/main/resources/open-api/cdr-banking-openapi.yaml")
    outputDir.set("$projectDir/src/gen")
    modelPackage.set("au.com.adatree.tpi.mogoplus.external.model")
    modelNameSuffix.set("Dto")
    modelNamePrefix.set("MogoPlus")
    apiFilesConstrainedTo.set(emptyList())
    configOptions.value(mutableMapOf(
        "dateLibrary" to "java8",
        "useTags" to "true",
        "enumPropertyNaming" to "UPPERCASE",
        "artifactId" to "MogoPlusApi",
        "packageName" to "au.com.adatree.tpi.mogoplus.external",
        "serializationLibrary" to "jackson"
    ))
    globalProperties.value(
        mutableMapOf(
            "apis" to "false",
            "models" to ""
        )
    )
    templateDir.set("$projectDir/src/main/resources/templates")
}

sourceSets.main {
    java.srcDirs("$projectDir/src/gen/src/main/kotlin")
    resources.srcDir("$projectDir/src/main/resources")
}
