const buyBtn = document.getElementById("buyBtn");
const sellBtn = document.getElementById("sellBtn");

const toastLiveExample = document.getElementById('liveToast')
const toastBody = document.getElementById("toast-body");

// Function to handle buy stock request
buyBtn.onclick = function (e) {

    e.preventDefault();
    let buyCsrf = document.getElementById("buyCsrf").value;
    let stockId = document.getElementById("stockBuyId").value;
    let quantity = document.getElementById("buyQuantity").value;
    let userId = document.getElementById("userId").value;

    const formData = new URLSearchParams();
    formData.append('userId', userId);
    formData.append('symbol', stockId);
    formData.append('quantity', quantity);
    formData.append("X-CSRF-Token", buyCsrf);

    fetch('/trade/buy', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-Token': buyCsrf
        },
        body: formData
    })
        .then(response => response.text())  // Assuming the response is a text message
        .then(response => {

            toastBody.innerText = response
            const toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastLiveExample)
            toastBootstrap.show();
        })
        .catch(() => {

            toastBody.innerText = `Failed to buy stocks.`
            const toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastLiveExample)
            toastBootstrap.show();
        });
}

// Function to handle sell stock request
sellBtn.onclick = function (e) {

    e.preventDefault();
    let stockId = document.getElementById("stockSellId").value;
    let sellCsrf = document.getElementById("sellCsrf").value;
    let quantity = document.getElementById("sellQuantity").value;
    let userId = document.getElementById("userId").value;

    const formData = new URLSearchParams();
    formData.append('userId', userId);
    formData.append('symbol', stockId);
    formData.append('quantity', quantity);
    formData.append("X-CSRF-Token", sellCsrf);

    fetch('/trade/sell', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-Token': sellCsrf
        },
        body: formData
    })
        .then(response => response.text())  // Assuming the response is a text message
        .then(response => {

            toastBody.innerText = response
            const toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastLiveExample)
            toastBootstrap.show();
        })
        .catch(() => {

            toastBody.innerText = `Failed to sell stocks.`
            const toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastLiveExample)
            toastBootstrap.show();
        });
}