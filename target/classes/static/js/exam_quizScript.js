let questions = [];
let currentQuestionIndex = 0;
let startTime
let endTime
const userAnswers = new Array(questions.length).fill(null);
const markedQuestions = new Set();
const studentId = sessionStorage.getItem('student_id');
showSpinner()
// Update the getData function
async function getData() {
    const url = `/api/v1/exam-mode`;

    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'X-API-KEY': 'neet_QdrUprUid6I7h7HTvdfOJHib0aSNOy7iUyLYYdFaPcc',
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`Response status: ${response.status}`);
        }

        const json = await response.json();
        questions = json["data"];
        // Update userAnswers array length after getting questions
        userAnswers.length = questions.length;
        userAnswers.fill(null);
        startTime = Date.now()
        updateQuestion(questions);
        updateQuestionPalette();
        hideSpinner()
    } catch (error) {
        console.error('Error fetching questions:', error);
    }
}

// Update the updateQuestion function to accept questions parameter
function updateQuestion(questions) {
    if (!questions || questions.length === 0) {
        console.error('No questions available');
        return;
    }

    const question = questions[currentQuestionIndex];
    const questionArea = document.getElementById('question');
    const optionsArea = document.getElementById('options');

    questionArea.innerHTML = `<h2>Q${currentQuestionIndex + 1}. ${question.question}</h2>`;
    optionsArea.innerHTML = [1, 2, 3, 4].map((num, index) => `
        <div class="option ${userAnswers[currentQuestionIndex] === index ? 'selected' : ''}" data-index="${index}">
            ${question[`option${num}`]}
        </div>
    `).join('');

    // Update navigation buttons
    document.getElementById('prevBtn').disabled = currentQuestionIndex === 0;
    document.getElementById('nextBtn').disabled = currentQuestionIndex === questions.length - 1;

    // Add click handlers for options
    document.querySelectorAll('.option').forEach(option => {
        option.addEventListener('click', () => {
            document.querySelectorAll('.option').forEach(opt => opt.classList.remove('selected'));
            option.classList.add('selected');
            userAnswers[currentQuestionIndex] = parseInt(option.getAttribute('data-index'));
            updateQuestionPalette();
        });
    });
}

function updateQuestionPalette() {
    const paletteGrid = document.getElementById('paletteGrid');
    paletteGrid.innerHTML = '';

    for (let i = 0; i < questions.length; i++) {
        const div = document.createElement('div');
        div.className = 'question-number';
        if (userAnswers[i] !== null) div.classList.add('answered');
        if (markedQuestions.has(i)) div.classList.add('marked');
        div.textContent = i + 1;
        div.addEventListener('click', () => {
            currentQuestionIndex = i;
            updateQuestion(questions);
        });
        paletteGrid.appendChild(div);
    }
}

// Update the navigation handlers to pass questions
document.getElementById('prevBtn').addEventListener('click', () => {
    if (currentQuestionIndex > 0) {
        currentQuestionIndex--;
        updateQuestion(questions);
    }
});

document.getElementById('nextBtn').addEventListener('click', () => {
    if (currentQuestionIndex < questions.length - 1) {
        currentQuestionIndex++;
        updateQuestion(questions);
    }
});

// Mark for Review handler
document.getElementById('markBtn').addEventListener('click', () => {
    if (markedQuestions.has(currentQuestionIndex)) {
        markedQuestions.delete(currentQuestionIndex);
    } else {
        markedQuestions.add(currentQuestionIndex);
    }
    updateQuestionPalette();
});

function generateSubmissionJSON() {
    
    
    // Only include questions that have been answered
    const submissionData = questions
        .map((question, index) => {
            if (userAnswers[index] !== null) {
                return {
                    student_id: studentId,
                    ch_id: question.chapter_id ? question.chapter_id.toLowerCase() : '', // Add null check
                    question_id: question.id || '', // Add fallback for missing id
                    selected_ans: userAnswers[index] + 1 // Converting 0-based index to 1-based option number
                };
            }
            return null;
        })
        .filter(item => item !== null); // Remove null entries (unanswered questions)

    return submissionData;
}

function areAllQuestionsAnswered() {
    return userAnswers.every(answer => answer !== null);
}

// Update the submit handler
document.getElementById('submitBtn').addEventListener('click', () => {
    if (!areAllQuestionsAnswered()) {
        alert('Please answer all questions before submitting the test.');
        return;
    }

    if (confirm('Are you sure you want to submit the test?')) {
        const submissionData = generateSubmissionJSON();
        console.log('Submission Data:', submissionData);
        
        // Example of how to send to server (uncomment and modify as needed)
        
        fetch('/api/v1/validate-answer', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-API-KEY': 'neet_QdrUprUid6I7h7HTvdfOJHib0aSNOy7iUyLYYdFaPcc'
            },
            body: JSON.stringify(submissionData)
        })
        .then(response => response.json())
        .then(data => {
            endTime = Date.now()
            const time_taken = ((endTime - startTime) / 1000)
            alert('Test submitted successfully!');
            console.log(data);
            const formatted_json = transformQuizData(data,time_taken)
            
            
            storeResult(formatted_json)
            
        })
        .catch(error => {
            console.error('Error:', error);
            // alert('Error submitting test');
        });
        
    }
});

// Timer functionality
let timeLeft = 90 * 60; // 90 minutes in seconds
const timerElement = document.getElementById('timer');

function updateTimer() {
    const minutes = Math.floor(timeLeft / 60);
    const seconds = timeLeft % 60;
    timerElement.textContent = `${minutes}:${seconds.toString().padStart(2, '0')}`;
    
    if (timeLeft > 0) {
        timeLeft--;
        setTimeout(updateTimer, 1000);
    } else {
        alert('Time is up!');
        // Add submission logic here
    }
}

updateTimer();

getData();

function storeResult(submissionData){
    fetch('/api/v1/quiz-details', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-API-KEY': 'neet_QdrUprUid6I7h7HTvdfOJHib0aSNOy7iUyLYYdFaPcc'
        },
        body: JSON.stringify(submissionData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        // alert('Test submitted successfully!');
        localStorage.setItem("quiz_id",data["quiz_id"]); 
       
        // localStorage.setItem("timetaken",time_taken)
        window.location.href = "result.html"
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error submitting test');
    });
}


function transformQuizData(inputData,time_taken) {
    // Extract necessary values from inputData
    const student_id = inputData.student_id;
    const score = inputData["score(%)"];
    const total_marks = inputData.total_marks;
    const total_questions = inputData.total_questions;
    const chapterwise_total_question = inputData.chapterwise_total_question;

    // Convert the chapterwise total question keys to a comma-separated string
    const chapter_ids = Object.keys(chapterwise_total_question).join(',');

    // Construct the output object
    const outputData = {
        student_id: student_id,
        score: score,
        total_marks: total_marks,
        total_questions: total_questions,
        quiz_type: localStorage.getItem("quiz_type"),
        time_taken: time_taken,  
        chapter_ids: chapter_ids
    };

    return outputData;
}


