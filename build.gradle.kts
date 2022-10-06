plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
    id("org.jetbrains.kotlin.kapt") version "1.6.21"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.micronaut.application") version "3.5.1"
}

group = "co.xorde.market_making_service"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    flatDir {
        dirs("libs")
    }
}

dependencies {
    kapt("io.micronaut:micronaut-http-validation")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.reactor:micronaut-reactor")
    implementation("io.micronaut.reactor:micronaut-reactor-http-client")
    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("io.micronaut:micronaut-validation")
    //LBank dependencies
    implementation("commons-codec:commons-codec:1.15")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.apache.commons:commons-compress:1.21")
    implementation("commons-codec:commons-codec:1.15")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation(":lbank-java-api-sdk-2.0.0-SNAPSHOT")
    //
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation("org.hamcrest:hamcrest-core:2.2")
    runtimeOnly("ch.qos.logback:logback-classic")
    compileOnly("org.graalvm.nativeimage:svm")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

}

application {
    mainClass.set("co.xorde.market_making_service.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.toVersion("17")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}

tasks.withType<JavaExec> {
    if (project.file(".env").exists()) {
        file(".env").readLines().forEach() {
            if (it.isNotEmpty() && !it.startsWith("#") && it.contains("=")) {
                val (key, value) = it.split("=")
                println("Variable: $key=$value")
                if (System.getenv(key) == null) {
                    environment(key, value)
                }
            }
        }
    }
}

micronaut {
    version("3.5.1")
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("co.xorde.*")
    }
}


