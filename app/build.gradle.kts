plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.daggerPlugin)
    id(BuildPlugins.navigationSafeArgs)
    id(BuildPlugins.googleServicesPlugin)
}

android {
    compileSdkVersion(AndroidSDK.compileSdk)
    buildToolsVersion("30.0.2")

    defaultConfig {
        applicationId = "com.ezzy.missingpersontracker"
        minSdkVersion(AndroidSDK.minSdk)

        targetSdkVersion(AndroidSDK.targetSdk)
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        vectorDrawables {
            useSupportLibrary = true
        }

        ndkVersion = "23.0.7599858"

        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    }

    buildTypes {
        getByName("release"){
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding=true
        mlModelBinding = true
    }
    configurations.all {
        exclude(group = "androidx.paging", module = "paging-runtime-ktx")
    }
}

dependencies {
    implementation(project(":core"))
    implementation(Dependencies.legacySupport)
    implementation(Dependencies.kotlinStdLib)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.materialComponents)
    implementation("androidx.preference:preference:1.1.1")
    implementation("androidx.test:monitor:1.3.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.1.0")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.1.0")
    implementation("org.tensorflow:tensorflow-lite-gpu:2.3.0")

    //dependency injection hilt
    implementation(Dependencies.daggerHilt)
    kapt(Dependencies.daggerKtxCompiler)

    implementation(Dependencies.navigationFragment)
    implementation(Dependencies.navigationUi)
    // Feature module Support
    implementation(Dependencies.navigationDynamic)
    // ViewModel
    implementation(Dependencies.lifecycleViewModel)
    // LiveData
    implementation(Dependencies.liveData)
    //COROUTINES
    implementation(Dependencies.coroutines)
    implementation(Dependencies.coroutinesCore)
    kapt(Dependencies.lifecyleCompiler)
    //ROOM DATABASE
    implementation(Dependencies.roomRuntime)
    kapt(Dependencies.roomCompiler)
    implementation(Dependencies.roomKtx)
    //timber
    implementation(Dependencies.timber)
    //glide
    implementation(Dependencies.glide)
    annotationProcessor(Dependencies.glideAnnotation)
    // Kotlin + coroutines
    implementation(Dependencies.workerKtx)
    implementation(Dependencies.circleImageView)
    implementation(Dependencies.multiDex)
    implementation("com.google.android.material:material:1.5.0-alpha02")

    implementation(Dependencies.sdpAndroid)
    implementation(Dependencies.sspAndroid)

    //work manager
    implementation(BuildPlugins.workManager)
    implementation(BuildPlugins.startUP)
    //hilt worker
    implementation(BuildPlugins.hiltWorker)

    //paging library
//    implementation(BuildPlugins.pagingLib)
    implementation(BuildPlugins.pagingLib)

    //Firebase
    implementation(platform(Dependencies.firebase))
    implementation(Dependencies.firebaseAnalytics)
    implementation(Dependencies.firebaseFireStore)
    implementation(Dependencies.firebaseStorage)
    implementation(Dependencies.firebaseAuth)
    implementation(Dependencies.firebaseCloudMessaging)
    implementation(Dependencies.firebaseUiAuth)
//    implementation(Dependencies.firebaseAuthFacebook)
    implementation(Dependencies.firebaseUiForStorage)
    implementation(Dependencies.firebaseUiForFirestore)
    implementation(Dependencies.coroutinesForFirebase)

    //sweet alert for android
//    implementation(Dependencies.sweetAlert2)
    implementation(Dependencies.sweetAlert)

    //data store
    implementation(Dependencies.dataStore)
    implementation(Dependencies.typedDataStore)
    implementation(Dependencies.spinKit)

    //CAMERAX

    val camerax_version = "1.0.1"
    // The following line is optional, as the core library is included indirectly by camera-camera2
    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    // If you want to additionally use the CameraX Lifecycle library
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    // If you want to additionally use the CameraX View class
    implementation("androidx.camera:camera-view:1.0.0-alpha28")
    // If you want to additionally use the CameraX Extensions library
    implementation("androidx.camera:camera-extensions:1.0.0-alpha28")

//    implementation(Dependencies.mpCharts)

//    implementation(Dependencies.skydoveProgress)

//    implementation(Dependencies.imagePicker)
//    implementation("io.github.ParkSangGwon:tedpermission:x.y.z")
//    implementation("gun0912.ted:tedbottompicker:x.y.z")

    testImplementation(TestLibraries.jUnit)
    testImplementation(TestLibraries.truth)
    testImplementation(TestLibraries.hiltTest)
    testImplementation(TestLibraries.roboelectric)
    kaptTest(Dependencies.daggerKtxCompiler)
    testImplementation(TestLibraries.truth)
    androidTestImplementation(TestLibraries.testRunner)
    androidTestImplementation(TestLibraries.espresso)


}