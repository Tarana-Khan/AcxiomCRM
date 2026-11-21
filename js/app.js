
document.addEventListener("DOMContentLoaded", function () {

    // Mobile sidebar toggle
    const menuToggle = document.querySelector(".menu-toggle");
    const sidebar = document.querySelector(".sidebar");

    if (menuToggle && sidebar) {
        menuToggle.addEventListener("click", function () {
            sidebar.classList.toggle("open");
        });

        document.addEventListener("click", function (event) {
            const clickedInsideSidebar = sidebar.contains(event.target);
            const clickedToggle = menuToggle.contains(event.target);

            if (!clickedInsideSidebar && !clickedToggle) {
                sidebar.classList.remove("open");
            }
        });
    }

    // Confirmation before delete or deactivate actions
    document.querySelectorAll(".delete-confirm").forEach(function (element) {
        element.addEventListener("click", function (event) {
            const confirmed = confirm(
                "Are you sure you want to delete this record?"
            );

            if (!confirmed) {
                event.preventDefault();
            }
        });
    });

    document.querySelectorAll(".deactivate-confirm").forEach(function (element) {
        element.addEventListener("click", function (event) {
            const confirmed = confirm(
                "Are you sure you want to deactivate this user?"
            );

            if (!confirmed) {
                event.preventDefault();
            }
        });
    });

    // Dismissible alerts
    document.querySelectorAll(".alert").forEach(function (alert) {
        const closeButton = alert.querySelector(".alert-close");

        if (closeButton) {
            closeButton.addEventListener("click", function () {
                alert.remove();
            });
        }
    });

    // Password visibility toggle
    document.querySelectorAll("[data-toggle-password]").forEach(function (button) {
        button.addEventListener("click", function () {
            const input = document.querySelector(
                button.getAttribute("data-toggle-password")
            );

            if (input) {
                input.type = input.type === "password" ? "text" : "password";
                button.textContent =
                    input.type === "password" ? "Show" : "Hide";
            }
        });
    });

    // Automatically hide success alerts after a delay
    document.querySelectorAll(".alert-success").forEach(function (alert) {
        setTimeout(function () {
            alert.style.opacity = "0";
            alert.style.transition = "opacity 0.4s ease";

            setTimeout(function () {
                alert.remove();
            }, 400);
        }, 4000);
    });

});
