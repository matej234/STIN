document.addEventListener("DOMContentLoaded", initTheme);

function initTheme() {
    const btn = document.querySelector(".theme-toggle");
    if (!btn) return;

    const savedTheme = localStorage.getItem("theme");

    if (savedTheme === "dark") {
        document.body.classList.add("dark");
        btn.innerText = "☀️";
    } else {
        btn.innerText = "🌙";
    }

    btn.addEventListener("click", toggleDark);
}

function toggleDark() {
    const btn = document.querySelector(".theme-toggle");
    if (!btn) return;

    document.body.classList.toggle("dark");

    const isDark = document.body.classList.contains("dark");

    if (isDark) {
        btn.innerText = "☀️";
        localStorage.setItem("theme", "dark");
    } else {
        btn.innerText = "🌙";
        localStorage.setItem("theme", "light");
    }
}