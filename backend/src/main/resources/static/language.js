const words = {
    cs: {
        welcome: "Ahoj",
        analysis: "Analýza měn",
        baseCurrency: "Základní měna",
        currencySelection: "Výběr měn",
        timeframeSelection: "Výběr měn (období)",
        save: "Uložit",
        strongest: "Nejsilnější",
        load: "Načíst",
        langToggle: "CZ",
        currency: "Měna",
        rate: "Kurz",
        currentComparison: "Aktuální porovnání",
        calculateCurrent: "Spočítat aktuální porovnání",
        allConversions: "Všechny přepočty",
        averagePeriod: "Průměry za období",
        dailyValues: "Denní hodnoty",
        chart: "Graf vývoje",
        weakest: "Nejslabší"

    },
    en: {
        welcome: "Hello",
        analysis: "Currency analysis",
        baseCurrency: "Base currency",
        currencySelection: "Currency selection",
        timeframeSelection: "Timeframe selection",
        save: "Save",
        strongest: "Strongest",
        load: "Load",
        langToggle: "EN",
        currency: "Currency",
        rate: "Rate",
        currentComparison: "Current comparison",
        calculateCurrent: "Calculate current comparison",
        allConversions: "All conversions",
        averagePeriod: "Average period",
        dailyValues: "Daily values",
        chart: "Chart",
        weakest: "Weakest"
    }
};

let lang = localStorage.getItem("lang") || "cs";

function t(key) {
    return words[lang][key] || key;
}

function toggleLang() {
    lang = (lang === "cs") ? "en" : "cs";
    localStorage.setItem("lang", lang);

    refreshUI();
}

function applyLanguage() {

    document.getElementById("welcome").innerText =
        t("welcome") + " " + (localStorage.getItem("user") || "");

    document.querySelectorAll("[data-key]").forEach(el => {
        const key = el.getAttribute("data-key");
        el.innerText = t(key);
    });

    const baseLabel = document.getElementById("baseLabel");
    if (baseLabel) baseLabel.innerText = t("baseCurrency");

    const saveBtn = document.getElementById("saveBtn");
    if (saveBtn) saveBtn.innerText = t("save");
}