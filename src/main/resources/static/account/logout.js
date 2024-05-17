async function logoutFunc() {
    let _csrf = document.querySelector("#_csrf").value;
    try {
        await fetch("/logout", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "X-CSRF-TOKEN" : _csrf
            }
        });
        alert("로그아웃 성공");
        window.location.href = "/";
    } catch (error) {
        alert(error);
        window.location.href = "/";
    }
}

document.addEventListener("DOMContentLoaded", function() {
    let logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", logoutFunc);
    }
});