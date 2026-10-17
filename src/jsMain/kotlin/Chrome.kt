external interface StorageArea {
    fun set(items: dynamic)
    fun set(items: dynamic, callback: () -> Unit)
    fun get(keys: dynamic, callback: (dynamic) -> Unit)
}

external interface ChromeStorage {
    val local: StorageArea
}

external interface ChromeWindows {
    fun create(options: dynamic)
}

external interface ChromeRuntime {
    fun getURL(path: String): String
}

external interface Chrome {
    val storage: ChromeStorage
    val windows: ChromeWindows
    val runtime: ChromeRuntime
}

external val chrome: Chrome