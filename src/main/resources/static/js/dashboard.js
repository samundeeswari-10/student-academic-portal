const token = localStorage.getItem("studentPortalToken");
const studentId = localStorage.getItem("studentId");
const studentName = localStorage.getItem("studentName");
const studentRole = localStorage.getItem("studentRole");

if (!token || !studentId) {
    window.location.href = "login.html";
}

document.getElementById("studentName").textContent =
    studentName || "Student";

document.getElementById("headerStudentName").textContent =
    studentName || "Student";

document.getElementById("headerStudentRole").textContent =
    studentRole || "STUDENT";


async function loadAnalytics() {

    try {

        const response = await fetch(
            `/api/analytics/student/${studentId}`,
            {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

        if (!response.ok) {
            throw new Error("Failed to load analytics");
        }

        const data = await response.json();

        document.getElementById("marksAverage")
            .textContent =
            `${data.overallMarksAverage.toFixed(2)}%`;

        document.getElementById("attendancePercentage")
            .textContent =
            `${data.overallAttendancePercentage.toFixed(2)}%`;

        document.getElementById("academicRisk")
            .textContent =
            data.academicRisk;

        document.getElementById("subjectCount")
            .textContent =
            data.subjectPerformance.length;

        const subjectContainer =
            document.getElementById("subjectPerformance");

        subjectContainer.innerHTML = "";

        data.subjectPerformance.forEach(subject => {

            subjectContainer.innerHTML += `
                <div class="subject-row">

                    <div class="subject-info">

                        <div>
                            <div class="subject-name">
                                ${subject.subjectName}
                            </div>

                            <div class="subject-code">
                                ${subject.subjectCode}
                            </div>
                        </div>

                        <strong>
                            ${subject.marksPercentage.toFixed(2)}%
                        </strong>

                    </div>

                    <div class="progress-bar">

                        <div
                            class="progress-fill"
                            style="width: ${subject.marksPercentage}%">
                        </div>

                    </div>

                    <div class="subject-percentage">
                        Attendance:
                        ${subject.attendancePercentage.toFixed(2)}%
                    </div>

                </div>
            `;
        });

    } catch (error) {

        console.error(error);

        document.getElementById("subjectPerformance")
            .innerHTML =
            `<p class="loading">
                Unable to load performance.
             </p>`;
    }
}


async function loadNotifications() {

    try {

        const response = await fetch(
            `/api/notifications/student/${studentId}`,
            {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

        if (!response.ok) {
            throw new Error("Failed to load notifications");
        }

        const notifications = await response.json();

        const container =
            document.getElementById("notifications");

        container.innerHTML = "";

        if (notifications.length === 0) {

            container.innerHTML =
                `<p class="loading">
                    No notifications.
                 </p>`;

            return;
        }

        notifications.slice(0, 5).forEach(notification => {

            container.innerHTML += `
                <div class="notification-item">

                    <h4>
                        ${notification.title}
                    </h4>

                    <p>
                        ${notification.message}
                    </p>

                </div>
            `;
        });

    } catch (error) {

        console.error(error);

        document.getElementById("notifications")
            .innerHTML =
            `<p class="loading">
                Unable to load notifications.
             </p>`;
    }
}


function logout() {

    localStorage.removeItem("studentPortalToken");
    localStorage.removeItem("studentId");
    localStorage.removeItem("studentName");
    localStorage.removeItem("studentEmail");
    localStorage.removeItem("studentRole");

    window.location.href = "login.html";
}


loadAnalytics();
loadNotifications();