plugins {
    kotlin("multiplatform") version "1.7.0"
}

group = "io.yoroion"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }


    sourceSets {
        val commonMain by getting {
            dependencies {
                // Kotlinx Libraries
                implementation(KotlinX.serialization.json)
                implementation(KotlinX.serialization.protobuf)
                implementation(KotlinX.coroutines.core)
                implementation(KotlinX.datetime)
                implementation("org.jetbrains.kotlinx:atomicfu:_")
                // Ktor
                implementation(Ktor.client.core)
                implementation(Ktor.client.websockets)
                implementation(Ktor.client.encoding)
                // Encoding
                implementation("io.matthewnelson.kotlin-components:encoding-base64:_")
                implementation("com.soywiz.korlibs.krypto:krypto:_")
                // IO
                implementation(Square.okio)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(Ktor.client.cio)
            }
        }
        val jvmTest by getting
        val nativeMain by getting {
            dependencies {
                implementation(Ktor.client.curl)
            }
        }
        val nativeTest by getting
    }
}
