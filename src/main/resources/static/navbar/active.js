document.addEventListener("DOMContentLoaded", function() {
    const pathname = window.location.pathname;
    if (pathname === "/freeBoard") {
        document.querySelector(".freeBoard").classList.add("fw-bold");
    } else if (pathname === "/imageBoard") {
        document.querySelector(".imageBoard").classList.add("fw-bold");
    }
});