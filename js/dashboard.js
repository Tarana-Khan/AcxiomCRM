
document.addEventListener("DOMContentLoaded", function () {

    // Animate dashboard numbers
    const counters = document.querySelectorAll("[data-counter]");

    counters.forEach(function (counter) {
        const target = Number(counter.getAttribute("data-counter"));

        if (!Number.isFinite(target)) {
            return;
        }

        const duration = 900;
        const startTime = performance.now();

        function updateCounter(currentTime) {
            const progress = Math.min(
                (currentTime - startTime) / duration,
                1
            );

            const currentValue = Math.floor(target * progress);
            counter.textContent = currentValue.toLocaleString();

            if (progress < 1) {
                requestAnimationFrame(updateCounter);
            }
        }

        requestAnimationFrame(updateCounter);
    });

    // Draw a simple chart if the dashboard contains a canvas
    const canvas = document.getElementById("crmOverviewChart");

    if (!canvas) {
        return;
    }

    const context = canvas.getContext("2d");

    if (!context) {
        return;
    }

    const labelsElement = document.getElementById("chartLabels");
    const valuesElement = document.getElementById("chartValues");

    // Read optional comma-separated values from the dashboard.
    const labels = labelsElement
        ? labelsElement.textContent.split(",").map(value => value.trim())
        : ["Customers", "Leads", "Opportunities"];

    const values = valuesElement
        ? valuesElement.textContent.split(",").map(Number)
        : [0, 0, 0];

    if (
        values.length === 0 ||
        values.some(value => !Number.isFinite(value) || value < 0)
    ) {
        return;
    }

    function drawChart() {
        const width = canvas.clientWidth || 500;
        const height = 240;
        const pixelRatio = window.devicePixelRatio || 1;

        canvas.width = width * pixelRatio;
        canvas.height = height * pixelRatio;
        canvas.style.height = height + "px";

        context.setTransform(pixelRatio, 0, 0, pixelRatio, 0, 0);
        context.clearRect(0, 0, width, height);

        const padding = 35;
        const chartHeight = height - 75;
        const chartWidth = width - padding * 2;
        const maxValue = Math.max(...values, 1);
        const barGap = 18;
        const barWidth = Math.max(
            12,
            (chartWidth - barGap * (values.length - 1)) / values.length
        );

        values.forEach(function (value, index) {
            const barHeight = (value / maxValue) * chartHeight;
            const x = padding + index * (barWidth + barGap);
            const y = chartHeight - barHeight + 12;

            context.fillStyle = "#0a3c28";
            context.beginPath();
            context.roundRect(x, y, barWidth, barHeight, 5);
            context.fill();

            context.fillStyle = "#26352e";
            context.font = "12px Arial";
            context.textAlign = "center";
            context.fillText(
                value.toLocaleString(),
                x + barWidth / 2,
                Math.max(y - 7, 12)
            );

            context.fillStyle = "#78857e";
            context.font = "11px Arial";

            const label = labels[index] || "Item";
            context.fillText(
                label.substring(0, 12),
                x + barWidth / 2,
                height - 15
            );
        });
    }

    drawChart();

    window.addEventListener("resize", drawChart);
});
