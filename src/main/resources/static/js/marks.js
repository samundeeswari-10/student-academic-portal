
const token = localStorage.getItem("token");
const role = localStorage.getItem("studentRole");

if (!token) {
    window.location.href = "login.html";
}

let editingMarkId = null;

const headers = {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${token}`
};

const markFields = [
    "cat1", "cat2", "cat3",
    "assignment1", "assignment2", "assignment3",
    "finalExam"
];

async function apiRequest(url, options = {}) {
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

function getNumber(id) {
    return Number(document.getElementById(id).value || 0);
}

function calculateMarks() {
    const cat1 = getNumber("cat1");
    const cat2 = getNumber("cat2");
    const cat3 = getNumber("cat3");

    const assignment1 = getNumber("assignment1");
    const assignment2 = getNumber("assignment2");
    const assignment3 = getNumber("assignment3");

    const finalExam = getNumber("finalExam");

    const catAverage = (cat1 + cat2 + cat3) / 3;
    const catScore = (catAverage * 30) / 50;

    const assignmentAverage =
        (assignment1 + assignment2 + assignment3) / 3;

    const internalTotal = catScore + assignmentAverage;
    const finalExamConverted = (finalExam * 60) / 100;

    const overallTotal = internalTotal + finalExamConverted;

    document.getElementById("catAverage").textContent =
        `${catAverage.toFixed(2)} / 50`;

    document.getElementById("catScore").textContent =
        `${catScore.toFixed(2)} / 30`;

    document.getElementById("assignmentAverage").textContent =
        `${assignmentAverage.toFixed(2)} / 10`;

    document.getElementById("internalTotal").textContent =
        `${internalTotal.toFixed(2)} / 40`;

    document.getElementById("finalExamConverted").textContent =
        `${finalExamConverted.toFixed(2)} / 60`;

    document.getElementById("overallTotal").textContent =
        `${overallTotal.toFixed(2)} / 100`;

    return {
        catAverage,
        catScore,
        assignmentAverage,
        internalTotal,
        finalExamConverted,
        overallTotal
    };
}

function resetCalculatedMarks() {
    calculateMarks();
}

markFields.forEach(id => {
    document.getElementById(id).addEventListener("input", calculateMarks);
});

async function loadStudents() {
    const students = await apiRequest("/api/students");
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
    const subjects = await apiRequest("/api/subjects");

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

function showMarkForm() {
    editingMarkId = null;

    const form = document.getElementById("markForm");
    form.reset();

    document.getElementById("formTitle").textContent = "Add Marks";
    document.getElementById("student").disabled = false;
    document.getElementById("subject").disabled = false;
    document.getElementById("markFormContainer").style.display = "block";
    document.getElementById("markMessage").textContent = "";

    resetCalculatedMarks();
}

function hideMarkForm() {
    document.getElementById("markFormContainer").style.display = "none";
    document.getElementById("markForm").reset();
    document.getElementById("student").disabled = false;
    document.getElementById("subject").disabled = false;

    editingMarkId = null;
    resetCalculatedMarks();
}

function formatMark(value) {
    return Number(value ?? 0).toFixed(2);
}

function getMarkCalculations(mark) {
    const cat1 = Number(mark.cat1 ?? 0);
    const cat2 = Number(mark.cat2 ?? 0);
    const cat3 = Number(mark.cat3 ?? 0);

    const assignment1 = Number(mark.assignment1 ?? 0);
    const assignment2 = Number(mark.assignment2 ?? 0);
    const assignment3 = Number(mark.assignment3 ?? 0);

    const finalExam = Number(mark.finalExam ?? 0);

    const catAverage = Number(
        mark.catAverage ?? ((cat1 + cat2 + cat3) / 3)
    );

    const catScore = (catAverage * 30) / 50;

    const assignmentAverage = Number(
        mark.assignmentAverage ??
        ((assignment1 + assignment2 + assignment3) / 3)
    );

    const internalTotal = Number(
        mark.internalTotal ?? (catScore + assignmentAverage)
    );

    const finalExamConverted = Number(
        mark.finalExamConverted ?? ((finalExam * 60) / 100)
    );

    const overallTotal = Number(
        mark.overallTotal ?? (internalTotal + finalExamConverted)
    );

    return {
        catAverage,
        assignmentAverage,
        internalTotal,
        finalExamConverted,
        overallTotal
    };
}

async function loadMarks() {
    const subjectId = document.getElementById("filterSubject").value;
    const tbody = document.getElementById("marksTableBody");

    if (!subjectId) {
        tbody.innerHTML =
            "<tr><td colspan='17'>Please select a subject.</td></tr>";
        return;
    }

    tbody.innerHTML = "<tr><td colspan='17'>Loading...</td></tr>";

    try {
        const marks = await apiRequest(`/api/marks/subject/${subjectId}`);
        tbody.innerHTML = "";

        if (!Array.isArray(marks) || marks.length === 0) {
            tbody.innerHTML =
                "<tr><td colspan='17'>No marks found for this subject.</td></tr>";
            return;
        }

        marks.forEach(mark => {
            const calculated = getMarkCalculations(mark);
            const row = document.createElement("tr");

            const values = [
                mark.id,
                mark.studentName,
                `${mark.subjectName} (${mark.subjectCode})`,
                formatMark(mark.cat1),
                formatMark(mark.cat2),
                formatMark(mark.cat3),
                formatMark(calculated.catAverage),
                formatMark(mark.assignment1),
                formatMark(mark.assignment2),
                formatMark(mark.assignment3),
                formatMark(calculated.assignmentAverage),
                formatMark(calculated.internalTotal),
                formatMark(mark.finalExam),
                formatMark(calculated.finalExamConverted),
                formatMark(calculated.overallTotal),
                `${calculated.overallTotal.toFixed(2)}%`
            ];

            values.forEach(value => {
                const cell = document.createElement("td");
                cell.textContent = value ?? "";
                row.appendChild(cell);
            });

            const actions = document.createElement("td");

            if (role === "ADMIN" || role === "FACULTY") {
                const editButton = document.createElement("button");
                editButton.type = "button";
                editButton.textContent = "Edit";
                editButton.onclick = () => editMark(mark);
                actions.appendChild(editButton);
            }

            if (role === "ADMIN") {
                const deleteButton = document.createElement("button");
                deleteButton.type = "button";
                deleteButton.textContent = "Delete";
                deleteButton.onclick = () => deleteMark(mark.id);
                actions.appendChild(deleteButton);
            }

            if (!actions.hasChildNodes()) {
                actions.textContent = "No actions";
            }

            row.appendChild(actions);
            tbody.appendChild(row);
        });
    } catch (error) {
        console.error(error);
        tbody.innerHTML =
            "<tr><td colspan='17'>Could not load marks.</td></tr>";
    }
}

function editMark(mark) {
    editingMarkId = mark.id;

    document.getElementById("markForm").reset();

    document.getElementById("formTitle").textContent = "Edit Marks";
    document.getElementById("student").value = mark.studentId;
    document.getElementById("subject").value = mark.subjectId;

    document.getElementById("cat1").value = mark.cat1 ?? 0;
    document.getElementById("cat2").value = mark.cat2 ?? 0;
    document.getElementById("cat3").value = mark.cat3 ?? 0;

    document.getElementById("assignment1").value = mark.assignment1 ?? 0;
    document.getElementById("assignment2").value = mark.assignment2 ?? 0;
    document.getElementById("assignment3").value = mark.assignment3 ?? 0;

    document.getElementById("finalExam").value = mark.finalExam ?? 0;

    document.getElementById("student").disabled = true;
    document.getElementById("subject").disabled = true;

    document.getElementById("markFormContainer").style.display = "block";
    document.getElementById("markMessage").textContent = "";

    calculateMarks();
}

document.getElementById("markForm").addEventListener(
    "submit",
    async function(event) {
        event.preventDefault();

        const studentId = document.getElementById("student").value;
        const subjectId = document.getElementById("subject").value;
        const message = document.getElementById("markMessage");

        const mark = {
            cat1: Number(document.getElementById("cat1").value),
            cat2: Number(document.getElementById("cat2").value),
            cat3: Number(document.getElementById("cat3").value),

            assignment1: Number(document.getElementById("assignment1").value),
            assignment2: Number(document.getElementById("assignment2").value),
            assignment3: Number(document.getElementById("assignment3").value),

            finalExam: Number(document.getElementById("finalExam").value)
        };

        const validMarks =
            mark.cat1 >= 0 && mark.cat1 <= 50 &&
            mark.cat2 >= 0 && mark.cat2 <= 50 &&
            mark.cat3 >= 0 && mark.cat3 <= 50 &&
            mark.assignment1 >= 0 && mark.assignment1 <= 10 &&
            mark.assignment2 >= 0 && mark.assignment2 <= 10 &&
            mark.assignment3 >= 0 && mark.assignment3 <= 10 &&
            mark.finalExam >= 0 && mark.finalExam <= 100;

        if (!validMarks) {
            message.textContent =
                "Please enter marks within the specified limits.";
            return;
        }

        if (!editingMarkId && (!studentId || !subjectId)) {
            message.textContent = "Please select a student and subject.";
            return;
        }

        const isEditing = editingMarkId !== null;

        const url = isEditing
            ? `/api/marks/${editingMarkId}`
            : `/api/marks?studentId=${encodeURIComponent(studentId)}&subjectId=${encodeURIComponent(subjectId)}`;

        try {
            await apiRequest(url, {
                method: isEditing ? "PUT" : "POST",
                body: JSON.stringify(mark)
            });

            const selectedSubject = isEditing
                ? document.getElementById("subject").value
                : subjectId;

            hideMarkForm();

            message.textContent = isEditing
                ? "Marks updated successfully!"
                : "Marks added successfully!";

            document.getElementById("filterSubject").value =
                selectedSubject;

            await loadMarks();
        } catch (error) {
            console.error(error);
            message.textContent = error.message;
        }
    }
);

async function deleteMark(id) {
    if (role !== "ADMIN") {
        alert("Only Admin can delete marks.");
        return;
    }

    if (!confirm("Are you sure you want to delete these marks?")) {
        return;
    }

    try {
        await apiRequest(`/api/marks/${id}`, {
            method: "DELETE"
        });

        alert("Marks deleted successfully!");
        await loadMarks();
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
        calculateMarks();
    } catch (error) {
        console.error(error);
        document.getElementById("markMessage").textContent =
            error.message;
    }
}

initializePage();
