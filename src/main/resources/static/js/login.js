
const loginForm = document.getElementById("loginForm");
const loginMessage = document.getElementById("loginMessage");

loginForm.addEventListener("submit", async function (event) {

    event.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    loginMessage.textContent = "Signing in...";
    loginMessage.style.color = "#6b7280";

    try {

        const response = await fetch("/api/login", {
            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                email: email,
                password: password
            })
        });

        const result = await response.json();

        if (!response.ok) {
            throw new Error(
                result.message || "Invalid email or password"
            );
        }

        // Save JWT token
        localStorage.setItem(
            "studentPortalToken",
            result.token
        );

        // Also save using the common token key
        localStorage.setItem(
            "token",
            result.token
        );

        // Save user information
        localStorage.setItem(
            "studentId",
            result.studentId || ""
        );

        localStorage.setItem(
            "studentName",
            result.name || ""
        );

        localStorage.setItem(
            "studentEmail",
            result.email || ""
        );

        localStorage.setItem(
            "studentRole",
            result.role || ""
        );

        loginMessage.textContent = "Login successful!";
        loginMessage.style.color = "green";

        // Redirect based on role
        setTimeout(() => {

            if (result.role === "ADMIN") {

                window.location.href = "admin-dashboard.html";

            } else if (result.role === "FACULTY") {

                window.location.href = "faculty-dashboard.html";

            } else {

                window.location.href = "dashboard.html";
            }

        }, 500);

    } catch (error) {

        loginMessage.textContent =
            error.message || "Invalid email or password";

        loginMessage.style.color = "red";
    }
});
