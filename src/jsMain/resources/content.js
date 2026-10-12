console.log("GoofyAhhSounds content script loaded");

let soundData = null;
let mode = "single";
let triggerKey = null;

chrome.storage.local.get(["soundData", "mode", "triggerKey"], (result) => {
    console.log("GoofyAhhSounds settings:", result);
    if (result.soundData) soundData = result.soundData;
    if (result.mode) mode = result.mode;
    if (result.triggerKey) triggerKey = result.triggerKey;
});

chrome.storage.onChanged.addListener((changes, areaName) => {
    if (areaName !== "local") return;

    if (changes.soundData) {
        soundData = changes.soundData.newValue || null;
    }
    if (changes.mode) {
        mode = changes.mode.newValue || "single";
    }
    if (changes.triggerKey) {
        triggerKey = changes.triggerKey.newValue || null;
    }
});

document.addEventListener("keydown", (event) => {
    if (!soundData) return;

    const matches =
        mode === "all" ||
        (triggerKey !== null && event.key.toLowerCase() === triggerKey.toLowerCase());

    if (matches) {
        new Audio(soundData).play();
    }
});