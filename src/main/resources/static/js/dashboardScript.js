const commonChartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
        legend: { display: false },
        tooltip: { mode: 'index', intersect: false }
    }
};



function performanceChart(label,data){
const performanceData = {
    labels: label,
    datasets: [{
        label: 'Performance',
        data: data,
        borderColor: '#7C3AED',
        backgroundColor: 'rgba(124, 58, 237, 0.1)',
        fill: true,
        tension: 0.3,
        borderWidth: 2,
        pointBackgroundColor: '#7C3AED',
        pointBorderColor: '#fff',
        pointBorderWidth: 2,
        pointRadius: 4,
        pointHoverRadius: 6
    }]
}
new Chart(document.getElementById('performanceChart'), {
    type: 'line',
    data: performanceData,
    options: {
        ...commonChartOptions,
        scales: {
            y: {
                beginAtZero: true,
                grid: { display: false }
            },
            x: {
                grid: { display: false }
            }
        }
    }
});

}

function timeDistributionChart(timeTaken){
    const timeData = {
        labels: [...document.getElementsByClassName("quiz-title")].map(element => element.innerText),
        datasets: [{
            label: 'Time Spent',
            data: timeTaken,
            backgroundColor: '#7C3AED',
            borderRadius: 8
        }]
    };
    new Chart(document.getElementById('timeChart'), {
        type: 'bar',
        data: timeData,
        options: {
            ...commonChartOptions,
            scales: {
                y: {
                    beginAtZero: true,
                    grid: { display: false }
                },
                x: {
                    grid: { display: false }
                }
            },
            borderRadius: 8,
            maintainAspectRatio: false
        }
    });
    
}




// Topic Breakdown Chart
function createTopicBreakDownChart(label,data){
    const topicsData = {
        labels: label,
        datasets: [{
            label: 'Questions',
            data: data,
            backgroundColor: ['#F59E0B', '#EF4444', '#10B981', '#7C3AED', '#EC4899'],
            borderWidth: 1,
            borderColor: ['#F59E0B', '#EF4444', '#10B981', '#7C3AED', '#EC4899'],
            datalabels: {
                display: true,
                color: '#fff',
                font: {
                    weight: 'bold'
                },
                formatter: (value, ctx) => {
                    return `${ctx.chart.data.labels[ctx.dataIndex]}: ${value}%`;
                }
            }
        }]
    };
    
    new Chart(document.getElementById('topicsChart'), {
        type: 'doughnut',
        data: topicsData,
        options: {
            ...commonChartOptions,
            cutout: '70%',
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: true,
                    position: 'right'
                }
            }
        }
    });
    
}


// Accuracy Rate Chart
function createAccuracy(accuracyData){
    new Chart(document.getElementById('accuracyChart'), {
        type: 'line',
        data: accuracyData,
        options: {
            ...commonChartOptions,
            scales: {
                y: {
                    beginAtZero: true,
                    grid: { display: false }
                },
                x: {
                    grid: { display: false }
                }
            }
        }
    });
}

function openTargetsModal() {
    document.getElementById('targetsModal').style.display = 'block';
    document.body.style.overflow = 'hidden';
}

function closeTargetsModal() {
    document.getElementById('targetsModal').style.display = 'none';
    document.body.style.overflow = 'auto';
}

function openQuestionModal() {
    document.getElementById('questionModal').style.display = 'flex';
    document.body.style.overflow = 'hidden';
}

function closeQuestionModal() {
    document.getElementById('questionModal').style.display = 'none';
    document.body.style.overflow = 'auto';
}

function openNewQuizModal() {
    const modal = document.getElementById('newQuizModal');
    modal.style.display = 'flex';
    document.body.style.overflow = 'hidden';
}

function closeNewQuizModal() {
    const modal = document.getElementById('newQuizModal');
    modal.style.display = 'none';
    document.body.style.overflow = 'auto';
}

function startQuiz(type) {
    //console.log(`Starting ${type}`);
    localStorage.setItem("quiz_type",type)
    if(type === "custom-mix"){
        window.location.href = "custom_mix.html"
    }
    if(type === "exam-mode"){
        window.location.href = "exam_mode.html"
    }
    if(type === "performance-based"){
        window.location.href = "final_quiz_interface.html"
    }
    if(type === "random-mix"){
        window.location.href = "final_quiz_interface.html" 
    }
    if(type === "custom-topic"){
        window.location.href = "chapter_list.html"
    }
    
    closeNewQuizModal();
}


// Close modal when clicking outside
window.onclick = function(event) {
    const modals = ['targetsModal', 'questionModal', 'newQuizModal'];
    modals.forEach(modalId => {
        const modal = document.getElementById(modalId);
        if (event.target === modal) {
            modal.style.display = 'none';
            document.body.style.overflow = 'auto';
        }
    });
}

// Add escape key handler for all modals
document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
        const modals = ['targetsModal', 'questionModal', 'newQuizModal'];
        modals.forEach(modalId => {
            const modal = document.getElementById(modalId);
            if (modal.style.display === 'flex' || modal.style.display === 'block') {
                modal.style.display = 'none';
                document.body.style.overflow = 'auto';
            }
        });
    }
});

// Add click effect for all buttons
document.querySelectorAll('.start-quiz-btn, .start-btn').forEach(button => {
    button.addEventListener('click', function(e) {
        // Create ripple effect
        const ripple = document.createElement('div');
        ripple.classList.add('ripple');
        this.appendChild(ripple);

        // Position the ripple
        const rect = this.getBoundingClientRect();
        const size = Math.max(rect.width, rect.height);
        ripple.style.width = ripple.style.height = `${size}px`;
        ripple.style.left = `${e.clientX - rect.left - size/2}px`;
        ripple.style.top = `${e.clientY - rect.top - size/2}px`;

        // Remove ripple after animation
        ripple.addEventListener('animationend', () => {
            ripple.remove();
        });
    });
});

// Add this CSS for the ripple effect
const style = document.createElement('style');
style.textContent = `
    .ripple {
        position: absolute;
        border-radius: 50%;
        background: rgba(255, 255, 255, 0.3);
        transform: scale(0);
        animation: ripple 0.6s linear;
        pointer-events: none;
    }

    @keyframes ripple {
        to {
            transform: scale(4);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);

const student_name = document.getElementById("student_name")
const student_id = sessionStorage.getItem("student_id")
//console.log(student_id)

async function getStudentAnalyticys() {
    const api_endpoint = `/api/v1/student-analytics?student_id=${student_id}&interval=1`;
    try {
        // Send a GET request to the API endpoint
        const response = await fetch(api_endpoint, {
            method: 'GET', // Define the request method
            headers: {
                'Accept': 'application/json', // Set the expected response format
                'x-api-key': 'neet_QdrUprUid6I7h7HTvdfOJHib0aSNOy7iUyLYYdFaPcc', // Include the API key in the header (if required)
            }
        });

        // Check if the response was successful (status code 200-299)
        if (response.ok) {
            const data = await response.json(); 
            console.log("Student Analytics:", data);
            student_name.innerHTML = `${sessionStorage.getItem("name")}<span class="waving-hand" style="display: inline-block; animation: wave 2s infinite; transform-origin: 70% 70%;">👋</span>`
            const student_analytics = formatStudentAnalytics(data)

            console.log(student_analytics)
            // recentQuizzes(student_analytics)
             const accuracyData = {
                labels: student_analytics.chapter_ids,
                datasets: [{
                    label: 'Accuracy Rate',
                    data: student_analytics.accuracy_scores,
                    borderColor: '#10B981',
                    backgroundColor: 'rgba(16, 185, 129, 0.2)',
                    fill: true,
                    tension: 0.4
                }]
            };
            createAccuracy(accuracyData)
            createTopicBreakDownChart(student_analytics.chapter_names,student_analytics.total_questions)
        } else {
            console.error("Failed to fetch student analytics:", response.statusText);
        }
    } catch (error) {
        console.error("Error during the request:", error);
        document.getElementById("analytics").innerText = "Error during request.";
    }
}

getStudentAnalyticys();

function formatStudentAnalytics(data) {
    // Ensure that the data is an array
    if (Array.isArray(data)) {
        // Create separate arrays for each field, including chapter names (ch_name)
        const chapter_ids = data.map(item => item.chapter_id);
        const chapter_names = data.map(item => item.chapter_name); // Extract chapter names
        const total_questions = data.map(item => item.total_questions);
        const total_score = data.map(item => item.total_score);
        const accuracy_scores = data.map(item => item.avg_accuracy);

        // Return the formatted data
        return {
            chapter_ids,
            chapter_names, // Add the chapter names
            total_questions,
            total_score,
            accuracy_scores
        };
    } else {
        console.error('Invalid data format. Expected an array.');
        return null;
    }
}


function recentQuizzes(student_analytics){
    // console.log(student_analytics)
    const recent_quizzes = document.getElementsByClassName("recent-quizzes")[0]
    let color 
            student_analytics.forEach((item) => {
                // console.log(item)
                if(item["score"]<=75){
                    color = "badge-secondary"
                }else{
                    color = "badge"
                }
                //console.log(`Chapter Name: ${student_analytics.chapter_names[index]}`);
                recent_quizzes.innerHTML += `<a> <div class="quiz-item" onclick="openQuizResults('${item["quiz_id"]}')">
                <div ">
                    <h4 class="quiz-title">Quiz #${item["quiz_id"].substring(0,3)} ${item["quiz_type"]}</h4>
                    <p>${formatDateTime(item["created_at"])}</p>
                   
                </div>
                <span class=" ${color}">${item["score"]}%</span>
            </div> </a>`

            })
}

function getStudent_quiz_results(){
    const url = `/api/v1/recent-quizzes?student_id=${student_id}`
    fetch(url, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "x-api-key": "neet_QdrUprUid6I7h7HTvdfOJHib0aSNOy7iUyLYYdFaPcc"
        }
      })
        .then(response => {
          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }
          return response.json();
        })
        .then(data => {
          console.log("Recent quizzes:", data);
        //   formatStudentQuizResults(data)
          recentQuizzes(data)
          const time_taken = createTimeList(data)
          timeDistributionChart(time_taken)
          // Handle the data as needed
        })
        .catch(error => {
          console.error("Error fetching recent quizzes:", error);
        });
}


function formatDateTime(dateString) {
    const date = new Date(dateString);
  
    // Check if the date is valid
    if (isNaN(date.getTime())) {
      return "Invalid date";
    }
  
    // Format the date
    const options = {
      weekday: 'long', // "Monday"
      year: 'numeric', // "2024"
      month: 'long', // "November"
      day: 'numeric', // "26"
      hour: '2-digit', // "10"
      minute: '2-digit', // "08"
      second: '2-digit', // "36"
      hour12: true, // 24-hour format
    };
  
    return date.toLocaleString('en-US', options);
  }


  function openQuizResults(quiz_id){
    // console.log(quiz_id)
    localStorage.setItem("quiz_id",quiz_id)
    window.location.href = "result.html"
  }

  function createTimeList(data){
    // console.log(data)
    const timeTaken = []
    data.forEach(item =>{
        timeTaken.push(item["time_taken"])
    })
    // console.log(timeTaken)
    return timeTaken
  }

  function formatPerformanceTrendResponse(data){
    const date = []
    const average_score = []
    data.forEach(item => {
        date.push(item["quiz_date"])
        average_score.push(item["average_score"])
    })
    // console.log(date)
    return [date,average_score]
  }

  function getPerformanceMatrix(){
    const url = `/api/v1/performance-matrix?student_id=${student_id}&interval=7`
    fetch(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "x-api-key": "neet_QdrUprUid6I7h7HTvdfOJHib0aSNOy7iUyLYYdFaPcc"
        }
      })
        .then(response => {
          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }
          return response.json();
        })
        .then(data => {
            console.log(data)
            const formattedResponse = formatPerformanceTrendResponse(data)
            // console.log(formattedResponse)
            performanceChart(formattedResponse[0],formattedResponse[1])
          // Handle the data as needed
        })
        .catch(error => {
          console.error("Error fetching recent quizzes:", error);
        });
  }


  function calcStatsGrid(){
    fetch(`/api/v1/student-stats?student_id=${student_id}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "x-api-key": "neet_QdrUprUid6I7h7HTvdfOJHib0aSNOy7iUyLYYdFaPcc"
        }
      })
        .then(response => {
          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }
          return response.json();
        })
        .then(data => {
            console.log(data)
            const stats = Array.from(document.getElementsByClassName("stat-value"))
            console.log(stats)
            stats[0].innerText = isNaN(parseInt(data["averageScore"])) ? "0" : parseInt(data["averageScore"]) + "%";
            stats[1].innerText = data["totalQuizzes"]
            stats[2].innerText = isNaN(parseInt(data["averageTimeTaken"])) ? "0 sec" : parseInt(data["averageTimeTaken"]) + " sec";
            stats[3].innerText = data["totalQuestions"]
        })
        .catch(error => {
          console.error("Error fetching recent quizzes:", error);
        });
  }


  function getPerformancebasedQuiz(){
    fetch(`/api/v1/performance-based-mock?student_id=${student_id}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "x-api-key": "neet_QdrUprUid6I7h7HTvdfOJHib0aSNOy7iUyLYYdFaPcc"
        }
      })
        .then(response => {
          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }
          return response.json();
        })
        .then(data => {
            console.log(data)
            const stats = Array.from(document.getElementsByClassName("stat-value"))
            console.log(stats)
            stats[0].innerText = parseInt(data["averageScore"]) + "%"
            stats[1].innerText = data["totalQuizzes"]
            stats[2].innerText = parseInt(data["averageTimeTaken"]) + " sec"
            stats[3].innerText = data["totalQuestions"]
        })
        .catch(error => {
          console.error("Error fetching recent quizzes:", error);
        });
  }

getStudent_quiz_results()
calcStatsGrid()
getPerformanceMatrix()

// Feedback Modal Functions
function openFeedbackModal() {
    document.getElementById('feedbackModal').style.display = 'flex';
}

function closeFeedbackModal() {
    document.getElementById('feedbackModal').style.display = 'none';
}

// Handle feedback form submission
document.getElementById('feedbackForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const feedbackType = document.getElementById("feedbackType").value
    const feedbackMessage = document.getElementById("feedbackMessage").value
    const feedbackEmail = document.getElementById("feedbackEmail").value

    const data = {
        student_id:sessionStorage.getItem("student_id"),
        feedback_type:feedbackType,
        feedback_text:feedbackMessage,
        email:feedbackEmail ? feedbackEmail : null
    }

    console.log(data)
    postFeedback(data)
    // Show success message
    alert('Thank you for your feedback!');
    
    // Close modal and reset form
    closeFeedbackModal();
    e.target.reset();
});

localStorage.clear()

function postFeedback(data) {
    fetch('/api/v1/feedback', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'x-api-key': 'neet_QdrUprUid6I7h7HTvdfOJHib0aSNOy7iUyLYYdFaPcc'
        },
        body: JSON.stringify(data),
    })
    .then(response => response.json())
    .then(console.log)
    .catch(console.error);
}


function icon_click(icon){
    
    if(icon === "home"){
        window.location.href = "home.html"
    }else if(icon === "logout"){
        sessionStorage.clear()
        window.location.href = "home.html"
    }
}