document.addEventListener("DOMContentLoaded", function () {
    const symbol = document.getElementById("symbol").innerText;

    const chartOptions = {
        layout: { textColor: 'black', background: { type: 'solid', color: 'white' } },
    };
    const chartContainer = document.getElementById("chart");
    const chart = LightweightCharts.createChart(chartContainer, chartOptions);

    // ✅ Create a candlestick series
    const candlestickSeries = chart.addCandlestickSeries({
        upColor: '#26a69a',
        downColor: '#ef5350',
        borderVisible: false,
        wickUpColor: '#26a69a',
        wickDownColor: '#ef5350',
        priceFormat: {
            type: 'price',
            precision: 4, // Number of decimal places
            minMove: 0.0001 // Minimum tick size
        }
    });

    chart.timeScale().fitContent();

    // ✅ Connect WebSocket for live stock updates using SockJS and STOMP
    let socket = new SockJS('/ws-stock');
    let stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {
        console.log("WebSocket connected!");
        stompClient.subscribe(`/topic/symbol/${symbol}`, function (message) {
            let data = JSON.parse(message.body);
            let stockData = {
                time: data.time,
                open: data.open,
                high: data.high,
                low: data.low,
                close: data.close
            };
            candlestickSeries.update(stockData);
        });
    }, function (error) {
        console.error("WebSocket Error:", error);
    });
});
