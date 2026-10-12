console.log("GoofyAhhSounds content script loaded");

let soundData = null;
let soundUrl = null;
let mode = "single";
let triggerKey = null;

function dataUrlToBlob(dataUrl) {
    const parts = dataUrl.split(",");
    const mime = parts[0].match(/:(.*?);/)[1];
    const binary = atob(parts[1]);
    const bytes = new Uint8Array(binary.length);
    for (let i = 0; i < binary.length; i++) {
        bytes[i] = binary.charCodeAt(i);
    }
    return new Blob([bytes], { type: mime });
}

function updateSoundUrl(dataUrl) {
    if (soundUrl) {
        URL.revokeObjectURL(soundUrl);
        soundUrl = null;
    }
    if (!dataUrl) return;
    soundUrl = URL.createObjectURL(dataUrlToBlob(dataUrl));
}

chrome.storage.local.get(["soundData", "mode", "triggerKey"], (result) => {
    console.log("GoofyAhhSounds settings:", result);
    if (result.soundData) {
        soundData = result.soundData;
        updateSoundUrl(soundData);
    }
    if (result.mode) mode = result.mode;
    if (result.triggerKey) triggerKey = result.triggerKey;
});

chrome.storage.onChanged.addListener((changes, areaName) => {
    if (areaName !== "local") return;

    if (changes.soundData) {
        soundData = changes.soundData.newValue || null;
        updateSoundUrl(soundData);
    }
    if (changes.mode) {
        mode = changes.mode.newValue || "single";
    }
    if (changes.triggerKey) {
        triggerKey = changes.triggerKey.newValue || null;
    }
});

document.addEventListener("keydown", (event) => {
    if (!soundUrl) return;

    const matches =
        mode === "all" ||
        (triggerKey !== null && event.key.toLowerCase() === triggerKey.toLowerCase());

    if (matches) {
        new Audio(soundUrl)
            .play()
            .catch((err) => console.error("GoofyAhh play failed:", err));
    }
});