import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.protobuf.gradle.*

plugins {
    application
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.google.protobuf") version "0.8.19"
}

// These address this error:
// 'compileJava' task (current target is 17) and 'compileKotlin' task (current target is 1.8) jvm target compatibility
// should be set to the same Java version.
val javaVersion = JavaVersion.VERSION_17
java { sourceCompatibility = javaVersion; targetCompatibility = javaVersion }
tasks.withType<KotlinCompile> { kotlinOptions { jvmTarget = javaVersion.toString() } }

group = "com.test"
version = "0.1"

repositories { mavenCentral() }

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
    implementation("io.grpc:grpc-protobuf:1.48.1")
    implementation("io.grpc:grpc-kotlin-stub:1.3.0")

    // The RPC fails only when I do :runShadow. :run works. The stacktrace is all in netty, but when I compile without
    // netty there is no netty in the jar, making me think there is no transitive dependency on netty. It fails with
    // both netty and netty-shaded, but with different stack traces.
    // With netty-shaded, it says:
    // Caused by: io.grpc.netty.shaded.io.netty.channel.AbstractChannel$AnnotatedConnectException: connect(..) failed: Address family not supported by protocol: /localhost:8980
    // With netty, it just says:
    // Caused by: java.nio.channels.UnsupportedAddressTypeException
    implementation("io.grpc:grpc-netty:1.48.1")
    implementation("com.google.protobuf:protobuf-kotlin:3.21.4")
}

// for gRPC
protobuf {
    protoc { artifact = "com.google.protobuf:protoc:3.21.4" }
    plugins {
        // We only need this for Kotlin, but it doesn't fully link without the Java sources.
        id("grpc") { artifact = "io.grpc:protoc-gen-grpc-java:1.48.1" }
        id("grpckt") { artifact = "io.grpc:protoc-gen-grpc-kotlin:1.3.0:jdk8@jar" }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins { id("grpc"); id("grpckt") }
            // The plugins above (I think) generate the Java proto code, but not the extra Kotlin stuff like DSLs.
            // Including this adds a /kotlin folder to the generated code which contains all the extra stuff.
            it.builtins { id("kotlin") }
        }
    }
}

sourceSets {
    main {
        java {
            // These are the default generated source dirs, but they have to be added manually currently:
            // https://github.com/google/protobuf-gradle-plugin/issues/109
            // The plugin generates both a grpc dir with Java files, and grpckt with Kotlin files. I believe the grpckt
            // contents includes all the functionality of the grpc (java) contents, but I'm not positive. The kotlin
            // files might just be stubs for the java version? so maybe this compiles but doesn't run?
            srcDirs("build/generated/source/proto/main/grpc")
            srcDirs("build/generated/source/proto/main/grpckt")
            // This contains the Java classes like the request and response messages.
            srcDirs("build/generated/source/proto/main/java")
            // This contains the Kotlin extensions to those java classes, like DSLs.
            srcDirs("build/generated/source/proto/main/kotlin")
        }
    }
}

tasks.jar { manifest.attributes["Main-Class"] = "com.test.MainKt" }
project.setProperty("mainClassName", "com.test.MainKt") // for shadowJar
