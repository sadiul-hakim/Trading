const buyBtn = document.getElementById("buyBtn");
const sellBtn = document.getElementById("sellBtn");

const toastLiveExample = document.getElementById('liveToast')
const toastBody = document.getElementById("toast-body");

// Function to handle buy stock request
buyBtn.onclick = function (e) {

    e.preventDefault();
    let stockId = document.getElementById("stockBuyId").value;
    let quantity = document.getElementById("buyQuantity").value;
    let userId = document.getElementById("userId").value;

    const formData = new URLSearchParams();
    formData.append('userId', userId);
    formData.append('stockId', stockId);
    formData.append('quantity', quantity);

    fetch('/trade/buy', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: formData
    })
        .then(response => response.text())  // Assuming the response is a text message
        .then(response => {

            toastBody.innerText = `Successfully bought ${quantity} stocks.`
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
    let quantity = document.getElementById("sellQuantity").value;
    let userId = document.getElementById("userId").value;

    const formData = new URLSearchParams();
    formData.append('userId', userId);
    formData.append('stockId', stockId);
    formData.append('quantity', quantity);

    fetch('/trade/sell', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: formData
    })
        .then(response => response.text())  // Assuming the response is a text message
        .then(response => {

            toastBody.innerText = `Successfully sold ${quantity} stocks.`
            const toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastLiveExample)
            toastBootstrap.show();
        })
        .catch(() => {

            toastBody.innerText = `Failed to sell stocks.`
            const toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastLiveExample)
            toastBootstrap.show();
        });
}