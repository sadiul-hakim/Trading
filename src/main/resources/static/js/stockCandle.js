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

    // ✅ Use SSE instead of WebSockets
    let eventSource = new EventSource(`/stream/${symbol}`);

    eventSource.addEventListener("priceUpdate", function (event) {
        let data = JSON.parse(event.data);
        let stockData = {
            time: data.time,
            open: data.open,
            high: data.high,
            low: data.low,
            close: data.close
        };
        candlestickSeries.update(stockData);
    });

    eventSource.onerror = function (event) {
        console.error("SSE Error:", event);
        eventSource.close();
    };
});
