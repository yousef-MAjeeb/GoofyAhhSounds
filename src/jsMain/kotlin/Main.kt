import kotlinx.browser.document
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement

fun main() {
    val fileInput = document.getElementById("fileInput") as HTMLInputElement
    val status = document.getElementById("status") as HTMLElement
    val playBtn = document.getElementById("playButton") as HTMLButtonElement

    fileInput.addEventListener("change", {
        val file = fileInput.files?.item(0)
        if (file != null) {
            status.textContent = file.name
        } else {
            status.textContent = "No sound selected"
        }
    })
}
