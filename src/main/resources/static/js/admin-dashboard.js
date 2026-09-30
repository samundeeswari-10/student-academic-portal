const token = localStorage.getItem("token");

async function loadAdminDashboard() {

    try {

        // STUDENTS
        const studentsResponse = await fetch("/api/students", {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        if (studentsResponse.ok) {
            const students = await studentsResponse.json();

            document.getElementById("studentCount").textContent =
                students.length;
        }

        // SUBJECTS
        const subjectsResponse = await fetch("/api/subjects", {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        if (subjectsResponse.ok) {
            const subjects = await subjectsResponse.json();

            document.getElementById("subjectCount").textContent =
                subjects.length;
        }

    } catch (error) {

        console.error(
            "Admin dashboard error:",
            error
        );
    }
}

function logout() {

    localStorage.removeItem("token");
    localStorage.removeItem("studentPortalToken");
    localStorage.removeItem("studentId");
    localStorage.removeItem("studentName");
    localStorage.removeItem("studentEmail");
    localStorage.removeItem("studentRole");

    window.location.href = "login.html";
}

function manageStudents() {
    alert("Student management coming next.");
}

function manageFaculty() {
    alert("Faculty management coming next.");
}

function manageSubjects() {
    alert("Subject management coming next.");
}

function manageAssignments() {
    alert("Assignment management coming next.");
}

loadAdminDashboard();