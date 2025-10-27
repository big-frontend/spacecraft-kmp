import platform.android.*
import kotlinx.cinterop.*
import kotlin.experimental.ExperimentalNativeApi
import kotlin.text.toInt

@OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)
@CName("Java_com_electrolytej_KotlinA_a")
fun a():jstring?{
    return null
}

@OptIn(ExperimentalNativeApi::class)
@CName("Java_electrolytej_KotlinA_sayHello")
fun sayHello(){
    __android_log_print(ANDROID_LOG_INFO.toInt(), "Kn", "Hello %s", "Native")
}