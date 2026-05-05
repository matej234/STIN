const DEV_MODE = location.hostname === "localhost";

let rates = {};
let apiBase = "";

window.addEventListener("DOMContentLoaded", init);

async function init() {
    const user = localStorage.getItem("user");
    if (!user) {
        window.location.href = "index.html";
        return;
    }

    document.getElementById("welcome").innerText = "Ahoj " + user;

    const data = await fetchData();

    if (!data?.quotes) return;

    apiBase = data.source;
    rates = data.quotes;

    const currencies = Object.keys(rates).map(k => k.substring(3));
    currencies.push(apiBase);

    const sorted = [...new Set(currencies)].sort();

    buildUI(sorted);
    recalculate();
}

async function fetchData() {
    if (location.hostname === "localhost") {
        return await (await fetch("/data/rates.json")).json();
    }
    return await (await fetch("/api/currency/rates")).json();
}

function buildUI(sorted) {

    const baseSelect = document.getElementById("baseCurrency");
    const tfBase = document.getElementById("timeframeBase");
    const currencyList = document.getElementById("currencyList");
    const tfList = document.getElementById("timeframeCurrencies");

    if (!baseSelect || !tfBase || !currencyList || !tfList) return;

    const defaults = ["USD","EUR","GBP"];

    baseSelect.innerHTML = "";
    tfBase.innerHTML = "";
    currencyList.innerHTML = "";
    tfList.innerHTML = "";

    for (const c of sorted) {
        baseSelect.add(new Option(c, c));
        tfBase.add(new Option(c, c));
    }

    baseSelect.value = apiBase;
    tfBase.value = apiBase;

    for (const c of sorted) {

        const make = (container, bind) => {
            const label = document.createElement("label");
            const input = document.createElement("input");

            input.type = "checkbox";
            input.value = c;
            input.checked = defaults.includes(c);

            if (bind) {
                input.addEventListener("change", recalculate);
            }

            label.appendChild(input);
            label.append(" " + c);
            container.appendChild(label);
        };

        make(currencyList, true);
        make(tfList, false);
    }
}

function getRate(currency) {
    if (currency === apiBase) return 1;

    const key = apiBase + currency;
    const value = rates[key];

    return value ?? null;
}

window.recalculate = function () {

    const base = document.getElementById("baseCurrency")?.value;
    if (!base) return;

    const selected = [...document.querySelectorAll("#currencyList input:checked")]
        .map(el => el.value);

    if (!selected.length) return;

    const baseRate = (base === apiBase)
        ? 1
        : getRate(base);

    if (!baseRate) return;

    let strongest = null;
    let weakest = null;

    let html = `
    <div style="
        max-height: 300px;
        overflow-y: auto;
        border: 1px solid #ddd;
        border-radius: 8px;
    ">
    <table style="width:100%; border-collapse:collapse;">
    <tr>
      <th style="text-align:left;">Měna</th>
      <th style="text-align:left;">Kurz</th>
    </tr>
    `;

    for (const c of selected) {

        const rateC = rates[apiBase + c] ?? (c === apiBase ? 1 : null);
        const rateB = rates[apiBase + base] ?? (base === apiBase ? 1 : null);

        let value;

        if (!rateC || !rateB) {
            value = null;
        } else {
            value = rateC / rateB;
        }

        html += `
        <tr>
          <td>${c}</td>
          <td>
            ${value === null
                ? "—"
                : `1 ${base} = ${value.toFixed(4)} ${c}`
            }
          </td>
        </tr>
        `;

        if (value === null) continue;

        if (!strongest || value < strongest.value)
            strongest = { c, value };

        if (!weakest || value > weakest.value)
            weakest = { c, value };
    }
    html += `
    </table>
    </div>
    `;

    const out = document.getElementById("statsContainer");

    if (strongest && weakest) {
        out.innerHTML = `
            <h4>Aktuální porovnání</h4>

            <p><b>Nejsilnější:</b> ${strongest.c}</p>
            <p>1 ${base} = ${strongest.value.toFixed(4)} ${strongest.c}</p>

            <p><b>Nejslabší:</b> ${weakest.c}</p>
            <p>1 ${base} = ${weakest.value.toFixed(4)} ${weakest.c}</p>

            <hr>
            ${html}
        `;
    } else {
        out.innerHTML = "<p>Žádná data k výpočtu</p>";
    }
};

window.loadTimeframe = async function () {

    const start = document.getElementById("start")?.value;
    const end = document.getElementById("end")?.value;

    if (!start || !end) return;

    const data = await fetchTimeframe(start, end);

    analyzeTimeframe(data?.quotes);
};

async function fetchTimeframe(start, end) {

    if (location.hostname === "localhost") {
        return await (await fetch("/data/timeframe.json")).json();
    }

    return await (await fetch(
        `/api/currency/timeframe?start_date=${start}&end_date=${end}`
    )).json();
}

function analyzeTimeframe(quotes) {

    const selected = [...document.querySelectorAll("#timeframeCurrencies input:checked")]
        .map(el => el.value);

    const base = document.getElementById("timeframeBase")?.value;

    if (!quotes || !selected.length || !base) return;

    const series = {};

    for (const date in quotes) {
        const day = quotes[date];

        for (const c of selected) {

            const rate = (c === apiBase)
                ? 1
                : day[apiBase + c];

            const baseRate = (base === apiBase)
                ? 1
                : day[apiBase + base];

            if (!rate || !baseRate) continue;

            const value = rate / baseRate;

            if (!series[c]) series[c] = [];
            series[c].push(value);
        }
    }

    let html = `
        <h4>Průměry za období</h4>
        <table style="width:100%; border-collapse:collapse;">
        <tr><th>Měna</th><th>Průměr</th></tr>
    `;

    for (const c in series) {
        const arr = series[c];
        const avg = arr.reduce((a,b) => a + b, 0) / arr.length;

        html += `
            <tr>
              <td>${c}</td>
              <td>${avg.toFixed(4)}</td>
            </tr>
        `;
    }

    html += `</table>`;

    document.getElementById("statsContainer").innerHTML = html;
}