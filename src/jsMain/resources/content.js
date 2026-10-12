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

document.addEventListener("keydown", (event) => {
    if (!soundData) return;

    const matches =
        mode === "all" ||
        (triggerKey !== null && event.key.toLowerCase() === triggerKey.toLowerCase());

    if (matches) {
        new Audio(soundData).play();
    }
});