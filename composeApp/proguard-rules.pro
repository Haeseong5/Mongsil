# ============================================================
# Mongsil composeApp ProGuard / R8 Rules
# ============================================================

# ── 공통 ─────────────────────────────────────────────────────
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

# ── kotlinx.serialization ────────────────────────────────────
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}
-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <2>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}
-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

# ── Ktor ─────────────────────────────────────────────────────
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# ── Koin ─────────────────────────────────────────────────────
-keep class org.koin.** { *; }
-dontwarn org.koin.**
-keepclassmembers class * {
    @org.koin.core.annotation.* <methods>;
}

# ── Room ─────────────────────────────────────────────────────
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }
-dontwarn androidx.room.**

# ── SQLDelight ───────────────────────────────────────────────
-keep class app.cash.sqldelight.** { *; }
-dontwarn app.cash.sqldelight.**

# ── Firebase ─────────────────────────────────────────────────
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**
-keep class dev.gitlive.firebase.** { *; }
-dontwarn dev.gitlive.firebase.**

# ── Google Play Services / Billing / Auth / Drive ────────────
-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**
-keep class com.android.vending.billing.** { *; }
-keep class com.google.api.** { *; }
-dontwarn com.google.api.**

# ── Coil ─────────────────────────────────────────────────────
-keep class coil3.** { *; }
-dontwarn coil3.**

# ── AndroidX Glance (App Widget) ─────────────────────────────
-keep class androidx.glance.** { *; }
-dontwarn androidx.glance.**

# ── Compose ──────────────────────────────────────────────────
-dontwarn androidx.compose.**

# ── App 모델 클래스 (리플렉션 보호) ──────────────────────────
-keep class com.cashproject.mongsil.kmp.model.** { *; }
-keep class com.cashproject.mongsil.kmp.core.data.model.** { *; }