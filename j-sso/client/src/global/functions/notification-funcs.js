import { notify } from "@kyvg/vue3-notification";

export function showNotify(title, message, type) {
    notify({
        group: 'main',
        type: type,
        title: title,
        text: message
    })
}

export function showSNotify(title, message) {
    showNotify(title, message, "success");
}

export function showENotify(title, message) {
    showNotify(title, message, "error");
}

export function showINotify(title, message) {
    showNotify(title, message, "info");
}

export function showWNotify(title, message) {
    showNotify(title, message, "warning");
}