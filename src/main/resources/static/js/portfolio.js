const loggedInUser = document.getElementById("loggedInUser");

document.addEventListener("DOMContentLoaded", function () {
    let socket = new SockJS('/ws-stock'); // Assuming WebSocket is exposed at /ws
    let stompClient = Stomp.over(socket);
    stompClient.debug = null;

    let wsId = loggedInUser.innerText;
    stompClient.connect(
        {'ws-id': wsId}, // Use same authentication headers if needed
        function (frame) {
            console.log("Connected to WebSocket");

            // Subscribe to user-specific portfolio updates
            stompClient.subscribe("/user/topic/portfolio", function (message) {
                let portfolios = JSON.parse(message.body);
                updatePortfolioTable(portfolios);
            });
        },
        function (err) {
            console.error("WebSocket error:", err);
        }
    );

    function updatePortfolioTable(portfolios) {

        let tableBody = document.querySelector("#portfolio_tbody");
        tableBody.innerHTML = ""; // Clear existing rows

        portfolios.forEach(function (portfolio) {
            let row = document.createElement("tr");

            let stockCell = document.createElement("td");
            stockCell.textContent = portfolio.stock.name;

            let quantityCell = document.createElement("td");
            quantityCell.textContent = portfolio.quantity;

            let buyPriceCell = document.createElement("td");
            buyPriceCell.textContent = portfolio.buyPrice.toFixed(4);

            let priceCell = document.createElement("td");
            priceCell.textContent = portfolio.stock.price.toFixed(4);
            priceCell.style.color = portfolio.stock.price.toFixed(4) > portfolio.buyPrice.toFixed(4)
                ? "green" : "red";

            let valueCell = document.createElement("td");
            valueCell.textContent = (portfolio.quantity * portfolio.stock.price).toFixed(4);

            row.appendChild(stockCell);
            row.appendChild(quantityCell);
            row.appendChild(buyPriceCell);
            row.appendChild(priceCell);
            row.appendChild(valueCell);

            tableBody.appendChild(row);
        });
    }
});
