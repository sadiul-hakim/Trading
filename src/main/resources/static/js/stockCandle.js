// document.addEventListener("DOMContentLoaded", function () {
//     const symbol = document.getElementById("symbol");
//
//     const chartOptions = {
//         layout: {textColor: 'black', background: {type: 'solid', color: 'white'}},
//     };
//     const chartContainer = document.getElementById("chart");
//     const chart = LightweightCharts.createChart(chartContainer, chartOptions);
//
//     // ✅ Create a candlestick series
//     const candlestickSeries = chart.addCandlestickSeries({
//         upColor: '#26a69a', downColor: '#ef5350', borderVisible: false,
//         wickUpColor: '#26a69a', wickDownColor: '#ef5350',
//     });
//
//     chart.timeScale().fitContent();
//
//     // ✅ Connect WebSocket for live stock updates
//     let socket = new WebSocket("ws://localhost:9095/stock");
//
//     socket.onmessage = function (event) {
//         let data = JSON.parse(event.data);
//         let stockData = {
//             time: data.time,
//             open: data.open,
//             high: data.high,
//             low: data.low,
//             close: data.close
//         };
//         candlestickSeries.update(stockData);
//     };
//
//     socket.onopen = function () {
//         console.log("WebSocket connected!");
//     };
//
//     socket.onerror = function (error) {
//         console.error("WebSocket Error:", error);
//     };
// });


document.addEventListener("DOMContentLoaded", function () {
    const symbol = document.getElementById("symbol").innerText;

    const chartOptions = {
        layout: { textColor: 'black', background: { type: 'solid', color: 'white' } },
    };
    const chartContainer = document.getElementById("chart");
    const chart = LightweightCharts.createChart(chartContainer, chartOptions);

    // ✅ Create a candlestick series
    const candlestickSeries = chart.addCandlestickSeries({
        upColor: '#26a69a', downColor: '#ef5350', borderVisible: false,
        wickUpColor: '#26a69a', wickDownColor: '#ef5350',
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
