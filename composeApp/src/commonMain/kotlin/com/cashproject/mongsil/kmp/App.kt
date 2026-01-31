package com.cashproject.mongsil.kmp

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.screen.main.MainScreen
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.poppins_medium
import org.jetbrains.compose.resources.Font
import org.koin.core.KoinApplication
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module


@Composable
fun App(
    fontFamily: FontFamily = FontFamily(Font(resource = Res.font.poppins_medium)),
    onDarkThemeChange: ((Boolean) -> Unit)? = null,
) {

    // 다크모드 상태 collect 코드 추가

    MongsilTheme(
        darkTheme = false, // TODO 추가,
        fontFamily = fontFamily
    ) {
        MainScreen()
    }

}

internal val appModule = module {
//    // Firebase expect 클래스들을 등록
//    single { FirebaseFirestore() }
//    single { FirebaseAuth() }
//
//    // Repository들 등록
//    single<CheckInRepository> { CheckInRepositoryImpl(get(), get()) }
//    single<AuthRepository> { AuthRepositoryImpl(get()) }
//
//    // ViewModel들 등록
//    viewModelOf(::CheckInViewModel)
//    viewModelOf(::HistoryViewModel)
//    viewModelOf(::AppViewModel)
//    viewModelOf(::AuthViewModel)
}

internal fun mongsilAppDeclaration(
    additionalDeclaration: KoinApplication.() -> Unit = {},
): KoinAppDeclaration = {
    modules(appModule)
    additionalDeclaration()
}


@Composable
expect fun getPlatformName(): String
