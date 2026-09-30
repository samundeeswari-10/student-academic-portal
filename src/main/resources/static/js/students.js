const token = localStorage.getItem("token");

let editingStudentId = null;


// ===============================
// LOAD STUDENTS
// ===============================

async function loadStudents() {

    const tableBody =
        document.getElementById("studentTableBody");

    try {

        const response = await fetch("/api/students", {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error("Failed to load students");
        }

        const students = await response.json();

        tableBody.innerHTML = "";

        students.forEach(student => {

            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${student.id}</td>

                <td>${student.name}</td>

                <td>${student.email}</td>

                <td>${student.department}</td>

                <td>${student.year}</td>

                <td>
                    <button onclick="editStudent(${student.id})">
                        Edit
                    </button>

                    <button onclick="deleteStudent(${student.id})">
                        Delete
                    </button>
                </td>
            `;

            tableBody.appendChild(row);
        });

    } catch (error) {

        console.error(error);

        tableBody.innerHTML = `
            <tr>
                <td colspan="6">
                    Unable to load students.
                </td>
            </tr>
        `;
    }
}


// ===============================
// SHOW ADD FORM
// ===============================

function showAddStudent() {

    editingStudentId = null;

    document.getElementById(
        "studentFormTitle"
    ).textContent = "Add Student";

    document.getElementById(
        "saveStudentButton"
    ).textContent = "Save Student";

    document.getElementById(
        "studentForm"
    ).reset();

    document.getElementById(
        "studentFormContainer"
    ).style.display = "block";
}


// ===============================
// HIDE FORM
// ===============================

function hideStudentForm() {

    document.getElementById(
        "studentFormContainer"
    ).style.display = "none";

    editingStudentId = null;
}


// ===============================
// EDIT STUDENT
// ===============================

async function editStudent(id) {

    try {

        const response = await fetch(
            `/api/students/${id}`,
            {
                method: "GET",

                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        );

        if (!response.ok) {
            throw new Error("Failed to get student");
        }

        const student = await response.json();

        // Store the ID being edited
        editingStudentId = id;

        // Change form title
        document.getElementById(
            "studentFormTitle"
        ).textContent = "Edit Student";

        // Change button text
        document.getElementById(
            "saveStudentButton"
        ).textContent = "Update Student";

        // Fill existing values
        document.getElementById(
            "studentName"
        ).value = student.name;

        document.getElementById(
            "studentEmail"
        ).value = student.email;

        document.getElementById(
            "studentDepartment"
        ).value = student.department;

        document.getElementById(
            "studentYear"
        ).value = student.year;

        // Show form
        document.getElementById(
            "studentFormContainer"
        ).style.display = "block";

    } catch (error) {

        console.error(error);

        alert(
            "Failed to load student: " +
            error.message
        );
    }
}


// ===============================
// SAVE / UPDATE STUDENT
// ===============================

document.getElementById("studentForm")
    .addEventListener("submit", async function(event) {

        event.preventDefault();

        const student = {

            name: document.getElementById(
                "studentName"
            ).value,

            email: document.getElementById(
                "studentEmail"
            ).value,

            department: document.getElementById(
                "studentDepartment"
            ).value,

            year: Number(
                document.getElementById(
                    "studentYear"
                ).value
            )
        };


        try {

            let response;


            // ===========================
            // EDIT
            // ===========================

            if (editingStudentId !== null) {

                response = await fetch(
                    `/api/students/${editingStudentId}`,
                    {
                        method: "PUT",

                        headers: {
                            "Content-Type": "application/json",

                            Authorization:
                                `Bearer ${token}`
                        },

                        body: JSON.stringify(student)
                    }
                );

            }


                // ===========================
                // ADD
            // ===========================

            else {

                response = await fetch(
                    "/api/students",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type": "application/json",

                            Authorization:
                                `Bearer ${token}`
                        },

                        body: JSON.stringify(student)
                    }
                );
            }


            if (!response.ok) {

                const errorText =
                    await response.text();

                throw new Error(
                    errorText ||
                    "Failed to save student"
                );
            }


            if (editingStudentId !== null) {

                alert(
                    "Student updated successfully!"
                );

            } else {

                alert(
                    "Student added successfully!"
                );
            }


            document.getElementById(
                "studentForm"
            ).reset();

            hideStudentForm();

            loadStudents();


        } catch (error) {

            console.error(error);

            alert(
                "Failed to save student: " +
                error.message
            );
        }

    });


// ===============================
// DELETE STUDENT
// ===============================

async function deleteStudent(id) {

    const confirmed = confirm(
        "Are you sure you want to delete this student?"
    );

    if (!confirmed) {
        return;
    }

    try {

        const response = await fetch(
            `/api/students/${id}`,
            {
                method: "DELETE",
                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        );

        if (!response.ok) {
            const errorText = await response.text();

            throw new Error(
                errorText || "Failed to delete student"
            );
        }

        alert("Student deleted successfully!");

        loadStudents();

    } catch (error) {

        console.error(error);

        alert(
            "Failed to delete student: " +
            error.message
        );
    }
}

// ===============================
// LOGOUT
// ===============================

function logout() {

    localStorage.removeItem("token");

    localStorage.removeItem(
        "studentPortalToken"
    );

    localStorage.removeItem("studentId");

    localStorage.removeItem("studentName");

    localStorage.removeItem("studentEmail");

    localStorage.removeItem("studentRole");

    window.location.href = "login.html";
}


// ===============================
// INITIAL LOAD
// ===============================

loadStudents();