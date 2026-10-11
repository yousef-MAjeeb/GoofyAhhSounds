import kotlinx.browser.document
import org.w3c.dom.Audio
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.url.URL
import org.w3c.files.File
import org.w3c.files.FileReader

fun main() {
    val fileInput = document.getElementById("fileInput") as HTMLInputElement
    val status = document.getElementById("status") as HTMLElement
    val playBtn = document.getElementById("playButton") as HTMLButtonElement
    var selectedFile: File? = null
    val fReader: FileReader = FileReader()

    fReader.onload = {
        val dataUrltext = fReader.result.toString()
        val data: dynamic = js("({})")
        data.soundName = selectedFile?.name
        data.soundData = dataUrltext
        chrome.storage.local.set(data)
        status.textContent = "${selectedFile?.name} (${dataUrltext.length} chars)"
    }

    // update the status (selected or not)
    fileInput.addEventListener("change", {
        val file = fileInput.files?.item(0)
        if (file != null) {
            status.textContent = file.name
            playBtn.disabled = false
            selectedFile = file
            fReader.readAsDataURL(file)
        } else {
            status.textContent = "No sound selected"
            playBtn.disabled = true
            selectedFile = null
        }
    })

    playBtn.addEventListener("click", {
        val file = selectedFile
        if (file != null) {
            val url = URL.createObjectURL(file)
            Audio(url).play()
        }
    })
}
