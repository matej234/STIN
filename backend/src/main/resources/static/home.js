window.addEventListener("DOMContentLoaded", init);

async function init() {
    const user = localStorage.getItem("user");
    if (!user) {
        window.location.href = "index.html";
        return;
    }

    document.getElementById("welcome").innerText = "Ahoj " + user;

    const currencies = await fetch("/api/currency/currencies")
        .then(r => r.json());

    buildUI(currencies);

    await loadData();
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

        createCheckbox(currencyContainer, c, loadData);
        createCheckbox(timeframeContainer, c, loadTimeframe);
    }

    baseSelect.value = "EUR";
    timeframeBase.value = "EUR";

    baseSelect.addEventListener("change", loadData);
    timeframeBase.addEventListener("change", loadTimeframe);
}

function createCheckbox(container, value, handler) {
    const label = document.createElement("label");
    const input = document.createElement("input");

    input.type = "checkbox";
    input.value = value;
    input.checked = true;

    input.addEventListener("change", handler);

    label.appendChild(input);
    label.append(" " + value);

    container.appendChild(label);
}

async function loadData() {

    const base = document.getElementById("baseCurrency").value;

    const selected = [...document.querySelectorAll("#currencyList input:checked")]
        .map(el => el.value);

    if (!base || selected.length === 0) return;

    const res = await fetch(
        `/api/currency/analyze?base=${base}&currencies=${selected.join(",")}`
    );

    const data = await res.json();

    renderAnalyze(data);
}

function renderAnalyze(data) {

    const container = document.getElementById("analyzeContainer");

    if (!container || !data) return;

    container.innerHTML = `
        <h4>Aktuální porovnání</h4>

        <p><b>Nejsilnější:</b> ${data.strongestCurrency}</p>
        <p>1 ${data.base} = ${data.strongestValue}</p>

        <p><b>Nejslabší:</b> ${data.weakestCurrency}</p>
        <p>1 ${data.base} = ${data.weakestValue}</p>
    `;

    let table = `
        <h4>Všechny přepočty</h4>
        <table style="width:auto; border-collapse: collapse;">
            <tr>
                <th>Měna</th>
                <th>Kurz</th>
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

    if (!start || !end || !base) return;

    const res = await fetch(
        `/api/currency/timeframe?base=${base}&start_date=${start}&end_date=${end}&currencies=${selected.join(",")}`
    );

    const data = await res.json();

    console.log(data);

    renderTimeframe(data);
}

function renderTimeframe(data) {

    const container = document.getElementById("timeframeContainer");

    if (!container || !data) return;

    let html = `
        <h4>Průměry za období</h4>

        <table style="width:auto; border-collapse: collapse; margin-bottom:20px;">
            <tr>
                <th>Měna</th>
                <th>Průměr</th>
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

    html += `<h4>Denní hodnoty</h4>`;

    for (const date in data.dailyRates) {

        html += `
            <h5 style="margin-bottom:5px;">${date}</h5>

            <table style="width:auto; border-collapse: collapse; margin-bottom:15px;">
                <tr>
                    <th>Měna</th>
                    <th>Kurz</th>
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
        <h4>Graf vývoje</h4>
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