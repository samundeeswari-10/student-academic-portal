
const token = localStorage.getItem("studentPortalToken");
const studentId = localStorage.getItem("studentId");
const studentName = localStorage.getItem("studentName");

let allAttendanceRecords = [];
let currentFilter = "ALL";

if (!token || !studentId) {
    window.location.href = "login.html";
} else {
    document.getElementById("studentName").textContent =
        studentName || "Student";

    // Render attendance records based on the selected filter
    function renderAttendanceTable() {
        const tbody = document.getElementById("attendanceTable");
        tbody.innerHTML = "";

        const filteredRecords = allAttendanceRecords.filter(record => {
            if (currentFilter === "PRESENT") return record.present;
            if (currentFilter === "ABSENT") return !record.present;
            return true;
        });

        if (filteredRecords.length === 0) {
            tbody.innerHTML =
                "<tr><td colspan='3'>No matching attendance records.</td></tr>";
            return;
        }

        filteredRecords
            .slice()
            .sort((a, b) =>
                b.attendanceDate.localeCompare(a.attendanceDate)
            )
            .forEach(record => {
                const row = document.createElement("tr");

                const subjectCell = document.createElement("td");
                subjectCell.textContent = record.subjectName;

                const dateCell = document.createElement("td");
                dateCell.textContent = record.attendanceDate;

                const statusCell = document.createElement("td");
                statusCell.textContent = record.present ? "Present" : "Absent";
                statusCell.className = record.present ? "present" : "absent";

                row.append(subjectCell, dateCell, statusCell);
                tbody.appendChild(row);
            });
    }

    // Load attendance records from the backend
    async function loadAttendance() {
        const tbody = document.getElementById("attendanceTable");
        const message = document.getElementById("message");

        tbody.innerHTML =
            "<tr><td colspan='3'>Loading attendance...</td></tr>";

        try {
            const response = await fetch(
                `/api/attendance/student/${studentId}`,
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${token}`,
                        Accept: "application/json"
                    }
                }
            );

            if (!response.ok) {
                throw new Error(
                    `Failed to load attendance (${response.status})`
                );
            }

            const records = await response.json();
            allAttendanceRecords = records;

            // Calculate overall attendance
            const classesAttended =
                records.filter(record => record.present).length;
            const totalClasses = records.length;

            const percentage = totalClasses === 0
                ? 0
                : (classesAttended / totalClasses) * 100;

            document.getElementById("overallPercentage").textContent =
                `${percentage.toFixed(2)}%`;

            document.getElementById("classesAttended").textContent =
                classesAttended;

            document.getElementById("totalClasses").textContent =
                totalClasses;

            // Calculate subject-wise attendance
            const subjectSummary =
                document.getElementById("subjectSummary");

            subjectSummary.innerHTML = "";

            const subjects = {};

            records.forEach(record => {
                if (!subjects[record.subjectId]) {
                    subjects[record.subjectId] = {
                        name: record.subjectName,
                        attended: 0,
                        total: 0
                    };
                }

                subjects[record.subjectId].total++;

                if (record.present) {
                    subjects[record.subjectId].attended++;
                }
            });

            Object.values(subjects).forEach(subject => {
                const subjectPercentage =
                    (subject.attended / subject.total) * 100;

                const card = document.createElement("div");
                card.className = "subject-card";

                const heading = document.createElement("h4");
                heading.textContent = subject.name;

                const details = document.createElement("p");
                details.textContent =
                    `${subjectPercentage.toFixed(2)}% · ` +
                    `${subject.attended} of ${subject.total} classes attended`;

                const progress = document.createElement("div");
                progress.className = "subject-progress";

                const fill = document.createElement("div");
                fill.className = "subject-progress-fill";
                fill.style.width = `${subjectPercentage}%`;

                progress.appendChild(fill);
                card.append(heading, details, progress);
                subjectSummary.appendChild(card);
            });

            // Handle no attendance records
            if (records.length === 0) {
                subjectSummary.textContent =
                    "No subject attendance records found.";
            }

            // Render table using the currently selected filter
            renderAttendanceTable();

            message.textContent =
                `${records.length} attendance record(s) found.`;

        } catch (error) {
            console.error(error);

            tbody.innerHTML =
                "<tr><td colspan='3'>Unable to load attendance.</td></tr>";

            message.textContent =
                "Please try again later.";

            document.getElementById("subjectSummary").textContent =
                "Unable to load subject attendance.";

            document.getElementById("overallPercentage").textContent = "--%";
            document.getElementById("classesAttended").textContent = "--";
            document.getElementById("totalClasses").textContent = "--";
        }
    }

    // Set up All, Present and Absent filter buttons
    document.querySelectorAll(".filter-btn").forEach(button => {
        button.addEventListener("click", () => {
            currentFilter = button.dataset.filter;

            document.querySelectorAll(".filter-btn").forEach(btn => {
                btn.classList.toggle("active", btn === button);
            });

            renderAttendanceTable();
        });
    });

    // Initial page load
    loadAttendance();
}
