# ==============================================
# ProGuard / R8 Rules for CaballoApp
# ==============================================

# Preserve line numbers for debugging stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ==============================================
# DRAWABLE RESOURCES - loaded via reflection
# ==============================================

# Keep DrawableNameResolver which uses reflection to load drawables
-keep class com.villalobos.caballoapp.util.DrawableNameResolver { *; }

# Keep all R.drawable fields (loaded dynamically by name)
-keepclassmembers class com.villalobos.caballoapp.R$drawable {
    public static int *;
}

# Keep resources loaded via getIdentifier()
-keepclassmembers class **.R$* {
    public static <fields>;
}

# ==============================================
# DATA MODELS - used in navigation and quiz
# ==============================================

-keep class com.villalobos.caballoapp.data.model.** { *; }

# Keep region data source
-keep class com.villalobos.caballoapp.data.source.DatosMusculares { *; }

# ==============================================
# REGION ACTIVITIES - launched by reflection/intent
# ==============================================

-keep class com.villalobos.caballoapp.ui.region.** { *; }

# ==============================================
# ACCESSIBILITY - heavy runtime reflection
# ==============================================

-keep class com.villalobos.caballoapp.util.AccesibilityHelper { *; }
-keep class com.villalobos.caballoapp.AccesibilityHelper { *; }

# ==============================================
# MATERIAL 3 COMPONENTS
# ==============================================

-keep class com.google.android.material.** { *; }
-dontwarn com.google.android.material.**

# ==============================================
# HILT / DI
# ==============================================

-dontwarn dagger.hilt.**
-keep class dagger.hilt.** { *; }

# ==============================================
# ENUMS used in data model
# ==============================================

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
