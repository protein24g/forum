document.addEventListener("DOMContentLoaded", function() {
    document.querySelector("#sidebar-toggle").addEventListener("click", toggleSidebar);
    document.querySelector("#overlay").addEventListener("click", toggleSidebar);
});
function toggleSidebar() {
    document.getElementById("sidebar").classList.toggle("sidebar-active");
    document.getElementById("overlay").classList.toggle("overlay-active");
}