import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.Audio
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.KeyboardEvent
import org.w3c.files.File
import org.w3c.files.FileReader

fun main() {
    val isFirefox = window.navigator.userAgent.contains("Firefox")
    val isPickerWindow = window.location.search.contains("picker=1")

    val fileInput = document.getElementById("fileInput") as HTMLInputElement
    val chooseBtn = document.getElementById("chooseButton") as HTMLButtonElement
    val status = document.getElementById("status") as HTMLElement
    val playBtn = document.getElementById("playButton") as HTMLButtonElement
    var selectedFile: File? = null
    val fReader: FileReader = FileReader()
    var savedData: String? = null
    val keyBox = document.getElementById("keyBox") as HTMLInputElement
    val modeSingle = document.getElementById("modeSingle") as HTMLInputElement
    val modeAll = document.getElementById("modeAll") as HTMLInputElement

    if (isFirefox && !isPickerWindow) {
        fileInput.hidden = true
        chooseBtn.hidden = false
    }

    fReader.onload = {
        val dataUrltext = fReader.result.toString()
        val data: dynamic = js("({})")
        data.soundName = selectedFile?.name
        data.soundData = dataUrltext
        chrome.storage.local.set(data)
        savedData = dataUrltext
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
        val file = savedData
        if (file != null) {
            Audio(file).play()
        }
    })

    keyBox.addEventListener("keydown", { event ->
        val keyEvent = event as KeyboardEvent
        keyBox.value = keyEvent.key
        keyEvent.preventDefault()

        val data: dynamic = js("({})")
        data.triggerKey = keyEvent.key
        chrome.storage.local.set(data)
    })

    modeSingle.addEventListener("change", {
        val data: dynamic = js("({})")
        data.mode = "single"
        chrome.storage.local.set(data)
        keyBox.disabled = false
    })

    modeAll.addEventListener("change", {
        val data: dynamic = js("({})")
        data.mode = "all"
        chrome.storage.local.set(data)
        keyBox.disabled = true
    })

    chrome.storage.local.get(arrayOf("soundName","soundData","triggerKey","mode"), { result ->
        if (result.soundName != null){
            status.textContent = result.soundName
            playBtn.disabled = false
            savedData = result.soundData.toString()
        }
        if (result.triggerKey != null){
            keyBox.value = result.triggerKey.toString()
        }
        if (result.mode == "all"){
            modeAll.checked = true
            keyBox.disabled = true
        }
    })
}
