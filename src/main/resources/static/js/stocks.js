function connect() {
    let socket = new SockJS('/ws-stock');
    let stompClient = Stomp.over(socket);
    stompClient.debug = null;

    stompClient.connect({}, function () {
        stompClient.subscribe('/topic/stocks', function (message) {
            let stocks = JSON.parse(message.body);
            updateStockTable(stocks);
        });
    });
}

function updateStockTable(stocks) {
    let table = document.getElementById("stockTable");
    table.innerHTML = "";
    stocks.forEach(stock => {
        let row = table.insertRow();
        row.insertCell(0).innerText = stock.name;
        row.insertCell(1).innerText = stock.symbol;
        row.insertCell(2).innerText = stock.price.toFixed(4);
        // row.insertCell(4).innerText = stock.change.toFixed(2) + "%";

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

window.onload = connect;