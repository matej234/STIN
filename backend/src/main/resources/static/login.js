async function login() {

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const res = await fetch("/api/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ username, password })
    });

    const data = await res.json();

    if (data.status === "OK") {
        localStorage.setItem("user", data.username);
        window.location.href = "home.html";
    } else {
        document.getElementById("result").innerText =
            "Špatné jméno nebo heslo";
    }
}

document.getElementById("loginBtn").addEventListener("click", login);