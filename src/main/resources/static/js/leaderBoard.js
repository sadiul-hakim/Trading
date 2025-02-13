document.addEventListener("DOMContentLoaded", function () {
    // Establish a connection to the SSE stream
    const eventSource = new EventSource("/leader_board/top-traders-stream");

    eventSource.addEventListener("topTraders", function (event) {

        const stocks = JSON.parse(event.data);
        updateTopTradersTable(stocks);
    });

    eventSource.onerror = function (event) {
        console.error("Error in SSE connection", event);
    };

    // Function to update the table with the new top traders data
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
});
