document.addEventListener("DOMContentLoaded", function () {
    console.log("In Stocks JS")

    const eventSource = new EventSource("/stocks-stream");

    eventSource.addEventListener("stocks", function (event) {

        const stocks = JSON.parse(event.data);
        updateStockTable(stocks);
    });

    eventSource.onerror = function (event) {
        console.error("Error in SSE connection", event);
        eventSource.close();
    };

    function updateStockTable(stocks) {

        let table = document.getElementById("stockTable");
        table.innerHTML = ""; // Clear existing rows

        stocks.forEach(stock => {
            let row = table.insertRow();
            row.insertCell(0).innerText = stock.name;
            row.insertCell(1).innerText = stock.symbol;
            row.insertCell(2).innerText = stock.price.toFixed(4);

            let changeCell = row.insertCell(3);
            changeCell.innerText = stock.change.toFixed(4) + "%";
            changeCell.style.color = stock.change < 0 ? "red" : "green";

            let linkCell = row.insertCell(4);
            let link = document.createElement("a");
            link.href = `/symbol/${stock.symbol}`;
            link.innerText = "View";
            linkCell.appendChild(link);
        });
    }
});
