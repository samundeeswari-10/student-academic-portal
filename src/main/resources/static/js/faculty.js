const token = localStorage.getItem("token");

let editingFacultyId = null;


// ===============================
// LOAD FACULTY
// ===============================

async function loadFaculty() {

    const tableBody =
        document.getElementById("facultyTableBody");

    try {

        const response = await fetch(
            "/api/faculty",
            {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        );

        if (!response.ok) {
            throw new Error("Failed to load faculty");
        }

        const facultyList =
            await response.json();

        tableBody.innerHTML = "";

        facultyList.forEach(faculty => {

            const row =
                document.createElement("tr");

            row.innerHTML = `
                <td>${faculty.id}</td>

                <td>${faculty.name}</td>

                <td>${faculty.email}</td>

                <td>${faculty.department}</td>

                <td>${faculty.designation}</td>

                <td>

                    <button
                        onclick="editFaculty(${faculty.id})">

                        Edit

                    </button>

                    <button
                        onclick="deleteFaculty(${faculty.id})">

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
                    Unable to load faculty.
                </td>
            </tr>
        `;
    }
}


// ===============================
// SHOW ADD FORM
// ===============================

function showAddFaculty() {

    editingFacultyId = null;

    document.getElementById(
        "facultyFormTitle"
    ).textContent = "Add Faculty";

    document.getElementById(
        "saveFacultyButton"
    ).textContent = "Save Faculty";

    document.getElementById(
        "facultyForm"
    ).reset();

    document.getElementById(
        "facultyFormContainer"
    ).style.display = "block";
}


// ===============================
// HIDE FORM
// ===============================

function hideFacultyForm() {

    document.getElementById(
        "facultyFormContainer"
    ).style.display = "none";

    editingFacultyId = null;
}


// ===============================
// EDIT FACULTY
// ===============================

async function editFaculty(id) {

    try {

        const response = await fetch(
            `/api/faculty/${id}`,
            {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        );

        if (!response.ok) {
            throw new Error("Failed to get faculty");
        }

        const faculty =
            await response.json();

        editingFacultyId = id;

        document.getElementById(
            "facultyFormTitle"
        ).textContent = "Edit Faculty";

        document.getElementById(
            "saveFacultyButton"
        ).textContent = "Update Faculty";

        document.getElementById(
            "facultyName"
        ).value = faculty.name;

        document.getElementById(
            "facultyEmail"
        ).value = faculty.email;

        document.getElementById(
            "facultyDepartment"
        ).value = faculty.department;

        document.getElementById(
            "facultyDesignation"
        ).value = faculty.designation;

        document.getElementById(
            "facultyFormContainer"
        ).style.display = "block";

    } catch (error) {

        console.error(error);

        alert(
            "Failed to load faculty: " +
            error.message
        );
    }
}


// ===============================
// SAVE / UPDATE FACULTY
// ===============================

document.getElementById("facultyForm")
    .addEventListener(
        "submit",
        async function(event) {

            event.preventDefault();

            const faculty = {

                name:
                document.getElementById(
                    "facultyName"
                ).value,

                email:
                document.getElementById(
                    "facultyEmail"
                ).value,

                department:
                document.getElementById(
                    "facultyDepartment"
                ).value,

                designation:
                document.getElementById(
                    "facultyDesignation"
                ).value
            };


            try {

                let response;


                if (editingFacultyId !== null) {

                    response = await fetch(
                        `/api/faculty/${editingFacultyId}`,
                        {
                            method: "PUT",

                            headers: {
                                "Content-Type":
                                    "application/json",

                                Authorization:
                                    `Bearer ${token}`
                            },

                            body:
                                JSON.stringify(faculty)
                        }
                    );

                } else {

                    response = await fetch(
                        "/api/faculty",
                        {
                            method: "POST",

                            headers: {
                                "Content-Type":
                                    "application/json",

                                Authorization:
                                    `Bearer ${token}`
                            },

                            body:
                                JSON.stringify(faculty)
                        }
                    );
                }


                if (!response.ok) {

                    const errorText =
                        await response.text();

                    throw new Error(
                        errorText ||
                        "Failed to save faculty"
                    );
                }


                if (editingFacultyId !== null) {

                    alert(
                        "Faculty updated successfully!"
                    );

                } else {

                    alert(
                        "Faculty added successfully!"
                    );
                }


                hideFacultyForm();

                loadFaculty();

            } catch (error) {

                console.error(error);

                alert(
                    "Failed to save faculty: " +
                    error.message
                );
            }

        }
    );


// ===============================
// DELETE FACULTY
// ===============================

async function deleteFaculty(id) {

    const confirmed = confirm(
        "Are you sure you want to delete this faculty?"
    );

    if (!confirmed) {
        return;
    }

    try {

        const response = await fetch(
            `/api/faculty/${id}`,
            {
                method: "DELETE",

                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        );

        if (!response.ok) {

            const errorText =
                await response.text();

            throw new Error(
                errorText ||
                "Failed to delete faculty"
            );
        }

        alert(
            "Faculty deleted successfully!"
        );

        loadFaculty();

    } catch (error) {

        console.error(error);

        alert(
            "Failed to delete faculty: " +
            error.message
        );
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

loadFaculty();