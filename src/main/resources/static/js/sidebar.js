const sidebarToggle = document.getElementById("sidebarToggle");
const dashboardLayout = document.querySelector(".dashboard-layout");

if (sidebarToggle && dashboardLayout) {
    sidebarToggle.addEventListener("click", () => {
        const isCollapsed =
            dashboardLayout.classList.toggle("sidebar-collapsed");

        sidebarToggle.setAttribute("aria-expanded", String(!isCollapsed));
        sidebarToggle.setAttribute(
            "aria-label",
            isCollapsed ? "Show sidebar" : "Hide sidebar"
        );
    });
}