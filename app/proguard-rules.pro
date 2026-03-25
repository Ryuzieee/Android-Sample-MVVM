# ========================================
# Retrofit / OkHttp
# ========================================
# Retrofit は内部リフレクションでサービスインターフェースを使用する
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# OkHttp
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# ========================================
# Kotlinx Serialization
# ========================================
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.yamamuto.android_sample_mvvm.**$$serializer { *; }
-keepclassmembers class com.yamamuto.android_sample_mvvm.** {
    *** Companion;
}
-keepclasseswithmembers class com.yamamuto.android_sample_mvvm.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ========================================
# Navigation Compose (Type-safe routes)
# ========================================
-keep class com.yamamuto.android_sample_mvvm.ui.navigation.** { *; }

# ========================================
# Hilt
# ========================================
-dontwarn dagger.hilt.internal.aggregatedroot.codegen.**

# ========================================
# Debugging
# ========================================
# リリースビルドでもスタックトレースの行番号を保持
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
