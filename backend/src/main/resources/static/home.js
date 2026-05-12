window.addEventListener("DOMContentLoaded", init);

async function init() {
    const user = localStorage.getItem("user");

    if (!user) {
        window.location.href = "index.html";
        return;
    }

    document.getElementById("welcome").innerText =
        t("welcome") + " " + user;

    const currencies = await fetch("/api/currency/currencies")
        .then(r => r.json());

    buildUI(currencies);

    applyLanguage();

    await loadSettings();

    await refreshUI();
}
function buildUI(currencies) {

    const currencyContainer = document.getElementById("currencyList");
    const timeframeContainer = document.getElementById("timeframeCurrencies");

    const baseSelect = document.getElementById("baseCurrency");
    const timeframeBase = document.getElementById("timeframeBase");

    currencyContainer.innerHTML = "";
    timeframeContainer.innerHTML = "";
    baseSelect.innerHTML = "";
    timeframeBase.innerHTML = "";

    for (const c of currencies) {

        baseSelect.add(new Option(c, c));
        timeframeBase.add(new Option(c, c));

        createCheckbox(currencyContainer, c, null);
        createCheckbox(timeframeContainer, c, null);
    }

    baseSelect.value = "EUR";
    timeframeBase.value = "EUR";
}

function createCheckbox(container, value, handler) {
    const label = document.createElement("label");
    const input = document.createElement("input");

    input.type = "checkbox";
    input.value = value;
    input.checked = false;

    if (handler) {
        input.addEventListener("change", handler);
    }

    label.appendChild(input);
    label.append(" " + value);

    container.appendChild(label);
}

async function loadData() {

    const base = document.getElementById("baseCurrency").value;

    const selected =
        [...document.querySelectorAll("#currencyList input:checked")]
            .map(el => el.value);

    if (!base || selected.length === 0) {
        await fetch(
            "/api/error-log?type=VALIDATION_ERROR&message=At least one currency must be selected",
            {
                method: "POST"
            }
        );

        alert("At least one currency must be selected");
        return;
    }

    const res = await fetch(
        `/api/currency/analyze?base=${base}&currencies=${selected.join(",")}`
    );

    if (!res.ok) {
        const errorMessage = await res.text();
        alert(errorMessage);
        return;
    }

    const data = await res.json();

    renderAnalyze(data);

    await saveCurrentAnalysis(data);
}

function renderAnalyze(data) {

    const container = document.getElementById("analyzeContainer");

    if (!container || !data) return;

    container.innerHTML = `
        <h3>${t("currentComparison")}</h3>

        <p><b>${t("strongest")}:</b> ${data.strongestCurrency}</p>
        <p>1 ${data.base} = ${data.strongestValue}</p>

        <p><b>${t("weakest")}:</b> ${data.weakestCurrency}</p>
        <p>1 ${data.base} = ${data.weakestValue}</p>
    `;

    let table = `
        <h4>${t("allConversions")}</h4>
        <table style="width:auto; border-collapse: collapse;">
            <tr>
                <th>${t("currency")}</th>
                <th>${t("rate")}</th>
            </tr>
    `;

    for (const currency in data.rates) {

        table += `
            <tr>
                <td style="padding:4px 12px 4px 0">${currency}</td>
                <td style="padding:4px 0">
                    1 ${data.base} = ${data.rates[currency]} ${currency}
                </td>
            </tr>
        `;
    }

    table += `</table>`;

    container.innerHTML += table;
}

async function loadTimeframe() {

    const start = document.getElementById("start").value;
    const end = document.getElementById("end").value;

    const base = document.getElementById("timeframeBase").value;

    const selected = [...document.querySelectorAll("#timeframeCurrencies input:checked")]
        .map(el => el.value);

    if (!start || !end || !base || selected.length === 0) {
        await fetch(
            "/api/error-log?type=VALIDATION_ERROR&message=Please fill all fields and select at least one currency",
            {
                method: "POST"
            }
        );

        alert("Please fill all fields and select at least one currency");
        return;
    }

    const res = await fetch(
        `/api/currency/timeframe?base=${base}&startDate=${start}&endDate=${end}&currencies=${selected.join(",")}`
    );

    if (!res.ok) {
        const errorMessage = await res.text();
        alert(errorMessage);
        return;
    }

    const data = await res.json();

    console.log(data);

    renderOnlyChart(data);
    await saveCalculation(data);
}
function renderOnlyChart(data) {

    const container =
        document.getElementById("timeframeContainer");

    if (!container || !data) return;

    container.innerHTML = `
        <h4>${t("chart")}</h4>
        <canvas id="currencyChart"></canvas>
    `;

    renderChart(data);
}
function renderTimeframe(data) {

    const container = document.getElementById("timeframeContainer");

    if (!container || !data) return;

    let html = `
        <h4>${t("averagePeriod")}</h4>

        <table style="width:auto; border-collapse: collapse; margin-bottom:20px;">
            <tr>
                <th>${t("currency")}</th>
                <th>${t("rate")}</th>
            </tr>
    `;

    for (const currency in data.averages) {

        html += `
            <tr>
                <td style="padding:4px 12px 4px 0">${currency}</td>
                <td>${data.averages[currency]}</td>
            </tr>
        `;
    }

    html += `</table>`;

    html += `<h4>${t("dailyValues")}</h4>`;

    for (const date in data.dailyRates) {

        html += `
            <h5 style="margin-bottom:5px;">${date}</h5>

            <table style="width:auto; border-collapse: collapse; margin-bottom:15px;">
                <tr>
                    <th>${t("currency")}</th>
                    <th>${t("rate")}</th>
                </tr>
        `;

        const day = data.dailyRates[date];

        for (const currency in day) {

            html += `
                <tr>
                    <td style="padding:4px 12px 4px 0">${currency}</td>
                    <td>${day[currency]}</td>
                </tr>
            `;
        }

        html += `</table>`;
    }

    html += `
        <h4>${t("chart")}</h4>
        <canvas id="currencyChart"></canvas>
    `;

    container.innerHTML = html;

    renderChart(data);
}

let chartInstance = null;

function renderChart(data) {

    const ctx = document.getElementById("currencyChart");

    if (!ctx) return;

    const dates = Object.keys(data.dailyRates);

    const currencies = new Set();

    for (const date of dates) {
        for (const c in data.dailyRates[date]) {
            currencies.add(c);
        }
    }

    const datasets = [];

    for (const currency of currencies) {

        const values = dates.map(date => {
            return data.dailyRates[date][currency] ?? null;
        });

        datasets.push({
            label: currency,
            data: values,
            borderWidth: 2,
            tension: 0.3
        });
    }

    if (chartInstance) {
        chartInstance.destroy();
    }

    chartInstance = new Chart(ctx, {
        type: "line",
        data: {
            labels: dates,
            datasets: datasets
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: "top"
                }
            }
        }
    });
}

async function saveSettings() {

    const settings = {

        baseCurrency:
            document.getElementById("baseCurrency").value,

        selectedCurrencies:
            [...document.querySelectorAll("#currencyList input:checked")]
                .map(el => el.value),

        timeframeBase:
            document.getElementById("timeframeBase").value,

        timeframeCurrencies:
            [...document.querySelectorAll("#timeframeCurrencies input:checked")]
                .map(el => el.value),

        startDate:
            document.getElementById("start").value,

        endDate:
            document.getElementById("end").value
    };

    await fetch("/api/settings", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(settings)
    });

    alert("Nastavení uloženo");
}

async function loadSettings() {

    const res = await fetch("/api/settings");
    const settings = await res.json();

    await new Promise(resolve => setTimeout(resolve, 100));

    document.getElementById("baseCurrency").value =
        settings.baseCurrency || "EUR";

    document.getElementById("timeframeBase").value =
        settings.timeframeBase || "EUR";

    document.getElementById("start").value =
        settings.startDate || "";

    document.getElementById("end").value =
        settings.endDate || "";

    setChecked(
        "#currencyList",
        settings.selectedCurrencies || []
    );

    setChecked(
        "#timeframeCurrencies",
        settings.timeframeCurrencies || []
    );

    await new Promise(resolve => setTimeout(resolve, 100));

    await loadData();
}

function setChecked(containerSelector, values) {

    const checkboxes =
        document.querySelectorAll(
            `${containerSelector} input`
        );

    checkboxes.forEach(cb => {
        cb.checked =
            Array.isArray(values) &&
            values.includes(cb.value);
    });
}

async function refreshUI() {
    applyLanguage();

    const checked =
        [...document.querySelectorAll("#currencyList input:checked")];

    if (checked.length > 0) {
        await loadData();
    }
}

async function saveCalculation(data) {

    const start = document.getElementById("start").value;
    const end = document.getElementById("end").value;
    const base = document.getElementById("timeframeBase").value;

    if (!start || !end || !base || !data?.dailyRates) {
        return;
    }

    const record = {
        base: base,
        startDate: start,
        endDate: end,
        dailyRates: data.dailyRates
    };

    await fetch("/api/history/save", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(record)
    });

    console.log("Analýza automaticky uložena");
}

async function saveCurrentAnalysis(data) {

    const base =
        document.getElementById("baseCurrency").value;

    const selectedCurrencies =
        [...document.querySelectorAll("#currencyList input:checked")]
            .map(el => el.value);

    if (!base || selectedCurrencies.length === 0 || !data) {
        return;
    }

    const calculations = {};

    for (const currency of selectedCurrencies) {

        const finalValue = data.rates[currency];

        const baseSourceValue =
            data.sourceRates[base];

        const targetSourceValue =
            data.sourceRates[currency];

        calculations[currency] = {
            sourceToBase:
                `USD → ${base} = ${baseSourceValue}`,

            sourceToTarget:
                `USD → ${currency} = ${targetSourceValue}`,

            finalCalculation:
                `${currency} / ${base} = ${finalValue}`
        };
    }

    const record = {
        source: "USD",
        base: base,
        selectedCurrencies: selectedCurrencies,

        calculations: calculations,

        strongestCurrency: data.strongestCurrency,
        strongestValue: data.strongestValue,

        weakestCurrency: data.weakestCurrency,
        weakestValue: data.weakestValue
    };

    await fetch("/api/current-analysis/save", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(record)
    });

    console.log("current-analysis.json saved");
}