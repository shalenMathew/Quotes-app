# --- General Android & Line Numbers ---
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-keepattributes Exceptions

# --- GSON & Retrofit (Critical for Import/Export & API) ---
# Keep all Model classes used for JSON serialization
-keep class com.shalenmathew.quotesapp.domain.model.** { *; }
-keep class com.shalenmathew.quotesapp.data.remote.dto.** { *; }

# Keep GSON internal classes and required attributes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Retrofit rules
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-keepclasseswithmembers interface * {
    @retrofit2.http.* <methods>;
}

# --- Room Database ---
-keep class com.shalenmathew.quotesapp.data.local.** { *; }
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Dao interface *
-keep @androidx.room.Entity class *

# --- Hilt / Dagger ---
-keep class com.shalenmathew.quotesapp.di.** { *; }
-keep class * extends androidx.lifecycle.ViewModel
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }
-keep @dagger.hilt.EntryPoint interface * { *; }
-keep @dagger.hilt.InstallIn interface * { *; }

# --- Glance & Widgets (Critical for Widget functionality) ---
# Glance components are often accessed via reflection or specific system paths
-keep class com.shalenmathew.quotesapp.presentation.widget.** { *; }
-keep class androidx.glance.** { *; }

# --- WorkManager & Receivers ---
# Keeping these ensures background tasks and alarms work after obfuscation
-keep class com.shalenmathew.quotesapp.presentation.workmanager.** { *; }
-keep class com.shalenmathew.quotesapp.presentation.receivers.** { *; }
-keep class androidx.work.** { *; }

# --- Enums & Utils ---
# Keep enums used for storage/logic (like NotificationMode)
-keepclassmembers enum com.shalenmathew.quotesapp.util.** { *; }
-keep class com.shalenmathew.quotesapp.util.** { *; }

# --- Parcelable ---
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# --- AboutLibraries ---
-keep class com.mikepenz.aboutlibraries.** { *; }

# --- Haze / HypnoticCanvas / ColorPicker (UI Libraries) ---
-keep class dev.chrisbanes.haze.** { *; }
-keep class com.mikepenz.hypnoticcanvas.** { *; }
-keep class com.github.skydoves.colorpicker.** { *; }
-keep class io.github.aghajari.lazyswipecards.** { *; }
