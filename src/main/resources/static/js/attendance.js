
const token = localStorage.getItem("token");
const role = localStorage.getItem("studentRole");

if (!token) {
    window.location.href = "login.html";
}

let editingAttendanceId = null;

const headers = {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${token}`
};

async function fetchJson(url, options = {}) {
    const response = await fetch(url, {
        ...options,
        headers: { ...headers, ...(options.headers || {}) }
    });

    if (!response.ok) {
        const message = await response.text();
        throw new Error(message || `Request failed (${response.status})`);
    }

    if (response.status === 204) return null;
    return response.json();
}

async function loadStudents() {
    const students = await fetchJson("/api/students");
    const select = document.getElementById("student");

    select.innerHTML = '<option value="">Select Student</option>';

    students.forEach(student => {
        const option = document.createElement("option");
        option.value = student.id;
        option.textContent = `${student.name} (ID: ${student.id})`;
        select.appendChild(option);
    });
}

async function loadSubjects() {
    const subjects = await fetchJson("/api/subjects");

    ["subject", "filterSubject"].forEach(selectId => {
        const select = document.getElementById(selectId);
        select.innerHTML = '<option value="">Select Subject</option>';

        subjects.forEach(subject => {
            const option = document.createElement("option");
            option.value = subject.id;
            option.textContent = `${subject.name} (${subject.code})`;
            select.appendChild(option);
        });
    });
}

function showAttendanceForm() {
    editingAttendanceId = null;
    document.getElementById("attendanceForm").reset();
    document.getElementById("formTitle").textContent = "Mark Attendance";

    document.getElementById("student").disabled = false;
    document.getElementById("subject").disabled = false;

    document.getElementById("attendanceDate").value =
        new Date().toLocaleDateString("en-CA");

    document.getElementById("attendanceFormContainer").style.display = "block";
    document.getElementById("attendanceMessage").textContent = "";
}

function hideAttendanceForm() {
    document.getElementById("attendanceFormContainer").style.display = "none";
    document.getElementById("attendanceForm").reset();
    document.getElementById("student").disabled = false;
    document.getElementById("subject").disabled = false;
    editingAttendanceId = null;
}

async function loadAttendance() {
    const subjectId = document.getElementById("filterSubject").value;
    const tbody = document.getElementById("attendanceTableBody");
    const percentageContainer =
        document.getElementById("percentageContainer");

    percentageContainer.textContent = "";

    if (!subjectId) {
        tbody.innerHTML =
            "<tr><td colspan='6'>Please select a subject.</td></tr>";
        return;
    }

    tbody.innerHTML = "<tr><td colspan='6'>Loading...</td></tr>";

    try {
        const records = await fetchJson(
            `/api/attendance/subject/${subjectId}`
        );

        tbody.innerHTML = "";

        if (records.length === 0) {
            tbody.innerHTML =
                "<tr><td colspan='6'>No attendance records found.</td></tr>";
            return;
        }

        records.forEach(record => {
            const row = document.createElement("tr");

            const values = [
                record.id,
                record.studentName,
                `${record.subjectName} (${record.subjectCode})`,
                record.attendanceDate,
                record.present ? "Present" : "Absent"
            ];

            values.forEach(value => {
                const cell = document.createElement("td");
                cell.textContent = value ?? "";
                row.appendChild(cell);
            });

            const actions = document.createElement("td");

            const editButton = document.createElement("button");
            editButton.textContent = "Edit";
            editButton.onclick = () => editAttendance(record);

            actions.appendChild(editButton);

            const percentageButton = document.createElement("button");
            percentageButton.textContent = "Percentage";
            percentageButton.onclick = () =>
                showPercentage(record.studentId, record.subjectId);

            actions.appendChild(percentageButton);

            if (role === "ADMIN") {
                const deleteButton = document.createElement("button");
                deleteButton.textContent = "Delete";
                deleteButton.onclick = () => deleteAttendance(record.id);
                actions.appendChild(deleteButton);
            }

            row.appendChild(actions);
            tbody.appendChild(row);
        });
    } catch (error) {
        console.error(error);
        tbody.innerHTML =
            "<tr><td colspan='6'>Could not load attendance.</td></tr>";
    }
}

async function showPercentage(studentId, subjectId) {
    try {
        const percentage = await fetchJson(
            `/api/attendance/student/${studentId}/subject/${subjectId}/percentage`
        );

        alert(`Attendance Percentage: ${Number(percentage).toFixed(2)}%`);
    } catch (error) {
        console.error(error);
        alert(error.message);
    }
}

function editAttendance(record) {
    editingAttendanceId = record.id;

    document.getElementById("formTitle").textContent = "Edit Attendance";
    document.getElementById("student").value = record.studentId;
    document.getElementById("subject").value = record.subjectId;
    document.getElementById("attendanceDate").value = record.attendanceDate;
    document.getElementById("present").value = String(record.present);

    // The existing backend update endpoint changes the record by ID.
    // Student and subject are kept unchanged during editing.
    document.getElementById("student").disabled = true;
    document.getElementById("subject").disabled = true;

    document.getElementById("attendanceFormContainer").style.display = "block";
    document.getElementById("attendanceMessage").textContent = "";
}

document.getElementById("attendanceForm").addEventListener(
    "submit",
    async function(event) {
        event.preventDefault();

        const studentId = document.getElementById("student").value;
        const subjectId = document.getElementById("subject").value;
        const attendanceDate =
            document.getElementById("attendanceDate").value;
        const present =
            document.getElementById("present").value === "true";

        const attendance = { attendanceDate, present };

        try {
            let url;
            let method;

            if (editingAttendanceId !== null) {
                url = `/api/attendance/${editingAttendanceId}`;
                method = "PUT";
            } else {
                url = `/api/attendance?studentId=${studentId}&subjectId=${subjectId}`;
                method = "POST";
            }

            await fetchJson(url, {
                method,
                body: JSON.stringify(attendance)
            });

            document.getElementById("attendanceMessage").textContent =
                editingAttendanceId !== null
                    ? "Attendance updated successfully!"
                    : "Attendance marked successfully!";

            const selectedSubject =
                editingAttendanceId !== null
                    ? document.getElementById("subject").value
                    : subjectId;

            hideAttendanceForm();

            document.getElementById("filterSubject").value =
                selectedSubject;

            await loadAttendance();
        } catch (error) {
            console.error(error);
            document.getElementById("attendanceMessage").textContent =
                error.message;
        }
    }
);

async function deleteAttendance(id) {
    if (role !== "ADMIN") {
        alert("Only Admin can delete attendance records.");
        return;
    }

    if (!confirm("Are you sure you want to delete this attendance record?")) {
        return;
    }

    try {
        await fetchJson(`/api/attendance/${id}`, {
            method: "DELETE"
        });

        alert("Attendance deleted successfully!");
        await loadAttendance();
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
        await Promise.all([loadStudents(), loadSubjects()]);
    } catch (error) {
        console.error(error);
        document.getElementById("attendanceMessage").textContent =
            error.message;
    }
}

initializePage();
