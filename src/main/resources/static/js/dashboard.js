
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


// LOAD STUDENT ANALYTICS

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

        document.getElementById("marksAverage").textContent =
            `${data.overallMarksAverage.toFixed(2)}%`;

        document.getElementById("attendancePercentage").textContent =
            `${data.overallAttendancePercentage.toFixed(2)}%`;

        document.getElementById("academicRisk").textContent =
            data.academicRisk;

        document.getElementById("subjectCount").textContent =
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
                            style="width: ${Math.min(
                100,
                Math.max(0, subject.marksPercentage)
            )}%">
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

        document.getElementById("subjectPerformance").innerHTML =
            `<p class="loading">
                Unable to load performance.
            </p>`;
    }
}


// TARGET TRACKING

async function loadTargetTracking() {
    const targetInput =
        document.getElementById("targetPercentage");

    const message =
        document.getElementById("targetMessage");

    const currentAverage =
        document.getElementById("targetCurrentAverage");

    const targetGap =
        document.getElementById("targetGap");

    const targetStatus =
        document.getElementById("targetStatus");

    const target = Number(targetInput.value);

    if (
        targetInput.value.trim() === "" ||
        !Number.isFinite(target) ||
        target < 0 ||
        target > 100
    ) {
        message.textContent =
            "Please enter a target between 0 and 100.";

        currentAverage.textContent = "--";
        targetGap.textContent = "--";
        targetStatus.textContent = "--";
        return;
    }

    message.textContent = "Loading target progress...";

    try {
        const response = await fetch(
            `/api/analytics/student/${studentId}/target?targetPercentage=${target}`,
            {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Accept": "application/json"
                }
            }
        );

        if (!response.ok) {
            throw new Error(
                `Failed to load target tracking (${response.status})`
            );
        }

        const data = await response.json();

        currentAverage.textContent =
            `${data.currentAverage.toFixed(2)}%`;

        const gap = data.percentageGap;

        if (gap > 0) {
            targetGap.textContent =
                `${gap.toFixed(2)}% below`;
        } else if (gap < 0) {
            targetGap.textContent =
                `${Math.abs(gap).toFixed(2)}% above`;
        } else {
            targetGap.textContent = "0.00%";
        }

        const statusLabels = {
            BELOW_TARGET: "Below Target",
            TARGET_MET: "Target Met",
            TARGET_EXCEEDED: "Target Exceeded"
        };

        targetStatus.textContent =
            statusLabels[data.targetStatus] || data.targetStatus;
        // Update target result box colors
        const resultBoxes = document.querySelectorAll(
            ".target-results > div"
        );

        const gapBox = resultBoxes[1];
        const statusBox = resultBoxes[2];

// Clear previous colors
        gapBox.classList.remove("target-positive", "target-below");
        statusBox.classList.remove("target-success", "target-below");

// Apply colors based on result
        if (data.targetStatus === "BELOW_TARGET") {
            gapBox.classList.add("target-below");
            statusBox.classList.add("target-below");
        } else {
            gapBox.classList.add("target-positive");
            statusBox.classList.add("target-success");
        }

        message.textContent =
            data.targetStatus === "BELOW_TARGET"
                ? "Keep working towards your target."
                : data.targetStatus === "TARGET_MET"
                    ? "You have reached your target."
                    : "Your current average is above your target.";

    } catch (error) {
        console.error(error);

        message.textContent =
            "Unable to load target progress. Please try again.";

        currentAverage.textContent = "--";
        targetGap.textContent = "--";
        targetStatus.textContent = "--";
    }
}


// LOAD NOTIFICATIONS

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
                    <h4>${notification.title}</h4>
                    <p>${notification.message}</p>
                </div>
            `;
        });

    } catch (error) {
        console.error(error);

        document.getElementById("notifications").innerHTML =
            `<p class="loading">
                Unable to load notifications.
            </p>`;
    }
}


// LOGOUT

function logout() {
    localStorage.removeItem("studentPortalToken");
    localStorage.removeItem("studentId");
    localStorage.removeItem("studentName");
    localStorage.removeItem("studentEmail");
    localStorage.removeItem("studentRole");

    window.location.href = "login.html";
}


// BUTTON EVENT

document.getElementById("checkTargetButton")
    .addEventListener("click", loadTargetTracking);


// INITIAL LOAD

loadAnalytics();
loadNotifications();
loadTargetTracking();

