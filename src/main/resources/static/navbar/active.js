document.addEventListener("DOMContentLoaded", function() {
    const pathname = window.location.pathname;
    if (pathname === "/freeBoards") {
        document.querySelector(".freeBoards").classList.add("fw-bold");
    } else if (pathname === "/imageBoards") {
        document.querySelector(".imageBoards").classList.add("fw-bold");
    }
});