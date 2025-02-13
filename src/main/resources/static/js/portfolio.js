const loggedInUser = document.getElementById("loggedInUser");

document.addEventListener("DOMContentLoaded", function () {
    const username = loggedInUser.innerText;  // Assuming username is stored in the hidden element
    const eventSource = new EventSource(`/portfolio-stream?username=${username}`);

    eventSource.addEventListener("portfolioUpdate", function (event) {

        const stocks = JSON.parse(event.data);
        updatePortfolioTable(stocks);
    });

    eventSource.onerror = function (event) {
        console.error("Error in SSE connection", event);
    };

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
            priceCell.style.color = portfolio.stock.price > portfolio.buyPrice
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
