external interface StorageArea {
    fun set(items: dynamic)
    fun get(keys: dynamic, callback: (dynamic) -> Unit)
}

external interface ChromeStorage {
    val local: StorageArea
}

external interface Chrome {
    val storage: ChromeStorage
}

external val chrome: Chrome