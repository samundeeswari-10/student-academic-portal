
const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html";
}

let editingAssignmentId = null;

const headers = {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${token}`
};

async function loadSubjects() {
    const response = await fetch("/api/subjects", {
        headers: { Authorization: `Bearer ${token}` }
    });

    if (!response.ok) {
        throw new Error("Failed to load subjects");
    }

    const subjects = await response.json();
    const select = document.getElementById("subject");

    select.innerHTML = '<option value="">Select Subject</option>';

    subjects.forEach(subject => {
        const option = document.createElement("option");
        option.value = subject.id;
        option.textContent = `${subject.name} (${subject.code})`;
        select.appendChild(option);
    });
}

async function loadAssignments() {
    const tbody = document.getElementById("assignmentTableBody");
    tbody.innerHTML = "<tr><td colspan='6'>Loading...</td></tr>";

    try {
        const response = await fetch("/api/assignments", {
            headers: { Authorization: `Bearer ${token}` }
        });

        if (!response.ok) {
            throw new Error(`Failed to load assignments (${response.status})`);
        }

        const assignments = await response.json();
        tbody.innerHTML = "";

        if (assignments.length === 0) {
            tbody.innerHTML =
                "<tr><td colspan='6'>No assignments found.</td></tr>";
            return;
        }

        assignments.forEach(assignment => {
            const row = document.createElement("tr");

            const values = [
                assignment.id,
                assignment.title,
                assignment.description,
                assignment.subjectName || assignment.subject?.name || "—",
                assignment.dueDate
            ];

            values.forEach(value => {
                const cell = document.createElement("td");
                cell.textContent = value ?? "";
                row.appendChild(cell);
            });

            const actions = document.createElement("td");

            const editButton = document.createElement("button");
            editButton.textContent = "Edit";
            editButton.onclick = () => editAssignment(assignment.id);

            const deleteButton = document.createElement("button");
            deleteButton.textContent = "Delete";
            deleteButton.onclick = () => deleteAssignment(assignment.id);

            actions.append(editButton, deleteButton);
            row.appendChild(actions);
            tbody.appendChild(row);
        });
    } catch (error) {
        console.error(error);
        tbody.innerHTML =
            "<tr><td colspan='6'>Could not load assignments.</td></tr>";
    }
}

function showAssignmentForm() {
    editingAssignmentId = null;
    document.getElementById("assignmentForm").reset();
    document.getElementById("formTitle").textContent = "Add Assignment";
    document.getElementById("assignmentFormContainer").style.display = "block";
    document.getElementById("assignmentMessage").textContent = "";
}

function hideAssignmentForm() {
    document.getElementById("assignmentFormContainer").style.display = "none";
    document.getElementById("assignmentForm").reset();
    editingAssignmentId = null;
}

async function editAssignment(id) {
    try {
        const response = await fetch(`/api/assignments/${id}`, {
            headers: { Authorization: `Bearer ${token}` }
        });

        if (!response.ok) {
            throw new Error("Failed to load assignment");
        }

        const assignment = await response.json();

        editingAssignmentId = id;
        document.getElementById("title").value = assignment.title;
        document.getElementById("description").value =
            assignment.description || "";
        document.getElementById("dueDate").value = assignment.dueDate;
        document.getElementById("subject").value =
            assignment.subjectId ?? assignment.subject?.id ?? "";

        document.getElementById("formTitle").textContent = "Edit Assignment";
        document.getElementById("assignmentFormContainer").style.display =
            "block";
    } catch (error) {
        console.error(error);
        alert(error.message);
    }
}

document.getElementById("assignmentForm").addEventListener(
    "submit",
    async function(event) {
        event.preventDefault();

        const title = document.getElementById("title").value.trim();
        const description =
            document.getElementById("description").value.trim();
        const subjectId = document.getElementById("subject").value;
        const dueDate = document.getElementById("dueDate").value;

        const assignment = { title, description, dueDate };

        const url = editingAssignmentId
            ? `/api/assignments/${editingAssignmentId}?subjectId=${subjectId}`
            : `/api/assignments?subjectId=${subjectId}`;

        const method = editingAssignmentId ? "PUT" : "POST";

        try {
            const response = await fetch(url, {
                method,
                headers,
                body: JSON.stringify(assignment)
            });

            if (!response.ok) {
                const message = await response.text();
                throw new Error(message || `Request failed (${response.status})`);
            }

            document.getElementById("assignmentMessage").textContent =
                editingAssignmentId
                    ? "Assignment updated successfully!"
                    : "Assignment added successfully!";

            hideAssignmentForm();
            await loadAssignments();
        } catch (error) {
            console.error(error);
            document.getElementById("assignmentMessage").textContent =
                error.message;
        }
    }
);

async function deleteAssignment(id) {
    if (!confirm("Are you sure you want to delete this assignment?")) {
        return;
    }

    try {
        const response = await fetch(`/api/assignments/${id}`, {
            method: "DELETE",
            headers: { Authorization: `Bearer ${token}` }
        });

        if (!response.ok) {
            const message = await response.text();
            throw new Error(message || `Delete failed (${response.status})`);
        }

        alert("Assignment deleted successfully!");
        await loadAssignments();
    } catch (error) {
        console.error(error);
        alert(error.message);
    }
}

function logout() {
    localStorage.clear();
    window.location.href = "login.html";
}

async function initializePage() {
    try {
        await loadSubjects();
        await loadAssignments();
    } catch (error) {
        console.error(error);
        document.getElementById("assignmentMessage").textContent = error.message;
    }
}

initializePage();
