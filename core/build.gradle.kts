plugins {
    id("java-library")
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    val kotlin_version by extra("1.5.10")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    implementation(Dependencies.coroutines)
    implementation(Dependencies.coroutinesCore)
}