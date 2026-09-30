const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html";
}
let editingSubjectId = null;


// ===============================
// LOAD SUBJECTS
// ===============================

async function loadSubjects() {

    try {

        const response = await fetch("/api/subjects", {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error("Failed to load subjects");
        }

        const subjects = await response.json();

        const tableBody =
            document.getElementById("subjectTableBody");

        tableBody.innerHTML = "";

        subjects.forEach(subject => {

            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${subject.id}</td>

                <td>${subject.name}</td>

                <td>${subject.code}</td>

                <td>${subject.credits}</td>

                <td>
                    ${subject.departmentName}
                    (${subject.departmentCode})
                </td>

                <td>
                    ${subject.facultyName || "Not Assigned"}
                </td>

                <td>

                    <button
                        onclick="editSubject(${subject.id})">
                        Edit
                    </button>

                    <button
                        onclick="deleteSubject(${subject.id})">
                        Delete
                    </button>

                </td>
            `;

            tableBody.appendChild(row);
        });

    } catch (error) {

        console.error("Subject loading error:", error);

        alert("Unable to load subjects.");
    }
}


// ===============================
// LOAD DEPARTMENTS
// ===============================

async function loadDepartments() {

    try {

        const response = await fetch("/api/departments", {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error("Failed to load departments");
        }

        const departments = await response.json();

        const departmentSelect =
            document.getElementById("departmentId");

        departmentSelect.innerHTML =
            `<option value="">Select Department</option>`;

        departments.forEach(department => {

            const option = document.createElement("option");

            option.value = department.id;

            option.textContent =
                `${department.name} (${department.code})`;

            departmentSelect.appendChild(option);
        });

    } catch (error) {

        console.error("Department loading error:", error);

        alert("Unable to load departments.");
    }
}


// ===============================
// LOAD FACULTY
// ===============================

async function loadFaculty() {

    try {

        const response = await fetch("/api/users/faculty", {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error("Failed to load faculty");
        }

        const facultyUsers = await response.json();

        const facultySelect =
            document.getElementById("facultyId");

        facultySelect.innerHTML =
            `<option value="">Select Faculty</option>`;

        facultyUsers.forEach(faculty => {

            const option = document.createElement("option");

            option.value = faculty.id;

            option.textContent =
                `${faculty.name} - ${faculty.email}`;

            facultySelect.appendChild(option);
        });

    } catch (error) {

        console.error("Faculty loading error:", error);

        alert("Unable to load faculty.");
    }
}


// ===============================
// SHOW ADD FORM
// ===============================

function showAddSubject() {

    editingSubjectId = null;

    document.getElementById("subjectFormTitle").textContent =
        "Add Subject";

    document.getElementById("saveSubjectButton").textContent =
        "Save Subject";

    document.getElementById("subjectForm").reset();

    document.getElementById("subjectFormContainer").style.display =
        "block";
}


// ===============================
// HIDE FORM
// ===============================

function hideSubjectForm() {

    editingSubjectId = null;

    document.getElementById("subjectFormContainer").style.display =
        "none";

    document.getElementById("subjectForm").reset();
}


// ===============================
// EDIT SUBJECT
// ===============================

async function editSubject(id) {

    try {

        const response = await fetch(`/api/subjects/${id}`, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error("Failed to get subject");
        }

        const subject = await response.json();

        editingSubjectId = id;

        document.getElementById("subjectName").value =
            subject.name;

        document.getElementById("subjectCode").value =
            subject.code;

        document.getElementById("subjectCredits").value =
            subject.credits;

        document.getElementById("departmentId").value =
            subject.departmentId;

        document.getElementById("facultyId").value =
            subject.facultyId;

        document.getElementById("subjectFormTitle").textContent =
            "Edit Subject";

        document.getElementById("saveSubjectButton").textContent =
            "Update Subject";

        document.getElementById("subjectFormContainer").style.display =
            "block";

    } catch (error) {

        console.error("Edit subject error:", error);

        alert("Unable to load subject.");
    }
}


// ===============================
// SAVE / UPDATE SUBJECT
// ===============================

document
    .getElementById("subjectForm")
    .addEventListener("submit", async function(event) {

        event.preventDefault();

        const name =
            document.getElementById("subjectName").value.trim();

        const code =
            document.getElementById("subjectCode").value.trim();

        const credits =
            Number(document.getElementById("subjectCredits").value);

        const departmentId =
            document.getElementById("departmentId").value;

        const facultyId =
            document.getElementById("facultyId").value;


        const subject = {
            name: name,
            code: code,
            credits: credits
        };


        try {

            let response;

            if (editingSubjectId !== null) {

                response = await fetch(
                    `/api/subjects/${editingSubjectId}?departmentId=${departmentId}&facultyId=${facultyId}`,
                    {
                        method: "PUT",

                        headers: {
                            "Content-Type": "application/json",
                            Authorization: `Bearer ${token}`
                        },

                        body: JSON.stringify(subject)
                    }
                );

            } else {

                response = await fetch(
                    `/api/subjects?departmentId=${departmentId}&facultyId=${facultyId}`,
                    {
                        method: "POST",

                        headers: {
                            "Content-Type": "application/json",
                            Authorization: `Bearer ${token}`
                        },

                        body: JSON.stringify(subject)
                    }
                );
            }


            if (!response.ok) {

                const errorText = await response.text();

                throw new Error(
                    errorText || "Failed to save subject"
                );
            }


            alert(
                editingSubjectId !== null
                    ? "Subject updated successfully!"
                    : "Subject added successfully!"
            );

            hideSubjectForm();

            loadSubjects();

        } catch (error) {

            console.error("Save subject error:", error);

            alert(error.message || "Unable to save subject.");
        }

    });


// ===============================
// DELETE SUBJECT
// ===============================

async function deleteSubject(id) {

    const confirmed =
        confirm("Are you sure you want to delete this subject?");

    if (!confirmed) {
        return;
    }

    try {

        const response = await fetch(
            `/api/subjects/${id}`,
            {
                method: "DELETE",
                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        );

        const responseText = await response.text();

        console.log("DELETE STATUS:", response.status);
        console.log("DELETE RESPONSE:", responseText);

        if (!response.ok) {
            throw new Error(
                `Delete failed (${response.status}): ${responseText}`
            );
        }

        alert("Subject deleted successfully!");

        loadSubjects();

    } catch (error) {

        console.error("Delete subject error:", error);

        alert(error.message);
    }
}


// ===============================
// LOGOUT
// ===============================

function logout() {

    localStorage.removeItem("token");
    localStorage.removeItem("studentPortalToken");
    localStorage.removeItem("studentId");
    localStorage.removeItem("studentName");
    localStorage.removeItem("studentEmail");
    localStorage.removeItem("studentRole");

    window.location.href = "login.html";
}


// ===============================
// INITIAL LOAD
// ===============================

async function initializePage() {

    await loadDepartments();

    await loadFaculty();

    await loadSubjects();
}

initializePage();