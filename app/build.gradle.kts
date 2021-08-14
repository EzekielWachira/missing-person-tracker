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
    testImplementation(TestLibraries.jUnit)
    androidTestImplementation(TestLibraries.testRunner)
    androidTestImplementation(TestLibraries.espresso)

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
    implementation(Dependencies.firebaseUiAuth)
    implementation(Dependencies.firebaseUiForFirestore)
    implementation(Dependencies.coroutinesForFirebase)

    //sweet alert for android
//    implementation(Dependencies.sweetAlert2)
    implementation(Dependencies.sweetAlert2)

    //data store
    implementation(Dependencies.dataStore)
    implementation(Dependencies.typedDataStore)

    implementation(Dependencies.mpCharts)

    implementation(Dependencies.skydoveProgress)

    implementation(Dependencies.imagePicker)
//    implementation("io.github.ParkSangGwon:tedpermission:x.y.z")
//    implementation("gun0912.ted:tedbottompicker:x.y.z")

}