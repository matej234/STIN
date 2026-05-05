
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
    return await fetch("/api/currency/rates").then(r => r.json());
}
async function fetchTimeframe(start, end) {
    return await fetch(`/api/currency/timeframe?start_date=${start}&end_date=${end}`)
        .then(r => r.json());
}

function buildUI(sorted) {

    const baseSelect = document.getElementById("baseCurrency");
    const tfBase = document.getElementById("timeframeBase");
    const currencyList = document.getElementById("currencyList");
    const tfList = document.getElementById("timeframeCurrencies");

    if (!baseSelect || !tfBase || !currencyList || !tfList) return;

    const defaults = ["USD", "EUR", "GBP"];

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
    return rates[apiBase + currency] ?? null;
}

window.recalculate = async function () {

    const base = document.getElementById("baseCurrency")?.value;
    if (!base) return;

    const selected = [...document.querySelectorAll("#currencyList input:checked")]
        .map(el => el.value);

    if (!selected.length) return;

    const res = await fetch("/api/currency/compare", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            base,
            apiBase,
            selected,
            rates
        })
    });

    const data = await res.json();

    render(data);
};

function render(data) {

    let html = `
        <table style="width:100%; border-collapse:collapse;">
            <tr>
                <th style="text-align:left;">Měna</th>
                <th style="text-align:left;">Kurz</th>
            </tr>
    `;

    for (const r of data.rates) {
        html += `
            <tr>
                <td>${r.currency}</td>
                <td>1 ${data.base} = ${r.value.toFixed(4)} ${r.currency}</td>
            </tr>
        `;
    }

    html += `</table>`;

    const out = document.getElementById("statsContainer");

    if (!out) return;

    if (data.strongest && data.weakest) {
        out.innerHTML = `
            <h4>Aktuální porovnání</h4>

            <p><b>Nejsilnější:</b> ${data.strongest.currency}</p>
            <p>1 ${data.base} = ${data.strongest.value.toFixed(4)} ${data.strongest.currency}</p>

            <p><b>Nejslabší:</b> ${data.weakest.currency}</p>
            <p>1 ${data.base} = ${data.weakest.value.toFixed(4)} ${data.weakest.currency}</p>

            <hr>
            ${html}
        `;
    } else {
        out.innerHTML = "<p>Žádná data k výpočtu</p>";
    }
}

window.loadTimeframe = async function () {

    const start = document.getElementById("start")?.value;
    const end = document.getElementById("end")?.value;

    console.log("start/end:", start, end);

    if (!start || !end) {
        console.log("missing dates");
        return;
    }

    const data = await fetchTimeframe(start, end);

    console.log("response:", data);

    analyzeTimeframe(data?.rates || data);
};

function analyzeTimeframe(rates) {

    if (!rates) {
        console.log("no rates");
        return;
    }

    const dates = Object.keys(rates).sort();
    const firstDay = rates[dates[0]];

    if (!firstDay) return;

    const pairs = Object.keys(firstDay).filter(k => k.length <= 3);

    for (const pair of pairs) {

        const values = dates
            .map(d => rates[d]?.[pair])
            .filter(v => v != null);

        console.log(`${pair}:`, values);
    }
}