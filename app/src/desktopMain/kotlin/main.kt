import com.electrolytej.main.App
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "spacecraft-kmp",
    ) {
        App()
    }
}