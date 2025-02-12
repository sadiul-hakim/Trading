document.addEventListener("DOMContentLoaded", function () {
    let socket = new SockJS('/ws-stock');
    let stompClient = Stomp.over(socket);
    stompClient.debug = null;

    stompClient.connect({}, function (frame) {

        stompClient.subscribe('/topic/top_traders', function (message) {
            let topTraders = JSON.parse(message.body);
            updateTopTradersTable(topTraders);
        });
    });

    function updateTopTradersTable(traders) {
        let tbody = document.getElementById("topTrader_tbody");
        tbody.innerHTML = ""; // Clear existing rows

        traders.forEach(trader => {
            let row = document.createElement("tr");
            row.innerHTML = `
            <td>${trader.name}</td>
            <td>${trader.balance.toFixed(4)}</td>
            <td class="text-warning">${trader.netWorth.toFixed(4)}</td>
        `;
            tbody.appendChild(row);
        });
    }
})