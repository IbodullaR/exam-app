// GLOBAL STATE
let subjects = [];
let allSubjectQuestions = [];
let currentSubjectId = null, currentSubjectName = '';
let tickets = []; // [[q,q,...], [q,q,...], ...]
let currentTicketIdx = 0;
let practiceQuestions = [];
let practiceIdx = 0, practiceCorrect = 0, practiceWrong = 0, practiceAnswered = false;
let examQuestions = [];
let examIdx = 0, examCorrect = 0, examWrong = 0, examAnswered = false;
let examAnswers = {};
const LETTERS = ['A','B','C','D'];
const TICKET_SIZE = 10;

// INIT
loadSubjects();

async function loadSubjects() {
  try {
    const res = await fetch('/api/subjects');
    subjects = await res.json();
    renderSubjects();
  } catch (e) { console.error('Fanlar yuklanmadi:', e); }
}

function renderSubjects() {
  const grid = document.getElementById('subjects-grid');
  if (subjects.length === 0) {
    grid.innerHTML = '<div class="empty-state"><div class="icon">📚</div><p>Fanlar topilmadi. Swagger orqali fan qo\'shing.</p></div>';
    return;
  }
  grid.innerHTML = subjects.map(s => `
    <div class="subject-card" onclick="openTickets(${s.id}, '${s.name}', ${s.questionCount})">
      <div class="icon">📖</div>
      <div class="name">${s.name}</div>
      <div class="count">${s.questionCount} ta savol</div>
    </div>
  `).join('');
}

function showPage(id) {
  document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
  document.getElementById(id).classList.add('active');
}

function goHome() { loadSubjects(); showPage('page-home'); }
function goTickets() { showPage('page-tickets'); }

// ========== TICKETS ==========
async function openTickets(subjectId, subjectName, count) {
  if (count === 0) { alert('Bu fanda savollar yo\'q'); return; }
  currentSubjectId = subjectId;
  currentSubjectName = subjectName;
  try {
    const res = await fetch(`/api/questions/by-subject/${subjectId}`);
    allSubjectQuestions = await res.json();
    tickets = makeTickets(allSubjectQuestions, TICKET_SIZE);
    document.getElementById('tickets-subject-name').textContent = subjectName;
    document.getElementById('tickets-subject-info').textContent =
      allSubjectQuestions.length + ' ta savol • ' + tickets.length + ' ta bilet';
    renderTickets();
    showPage('page-tickets');
  } catch (e) { console.error('Savollar yuklanmadi:', e); }
}

function makeTickets(questions, size) {
  const result = [];
  for (let i = 0; i < questions.length; i += size) {
    result.push(questions.slice(i, i + size));
  }
  return result;
}

function renderTickets() {
  const grid = document.getElementById('tickets-grid');
  grid.innerHTML = tickets.map((t, i) => `
    <div class="ticket-card" onclick="startTicket(${i})">
      <div class="t-icon">🎫</div>
      <div class="t-num">Bilet №${i + 1}</div>
      <div class="t-count">${t.length} ta savol</div>
    </div>
  `).join('');
}

function startTicket(ticketIdx) {
  currentTicketIdx = ticketIdx;
  practiceQuestions = tickets[ticketIdx];
  practiceIdx = 0; practiceCorrect = 0; practiceWrong = 0; practiceAnswered = false;
  document.getElementById('practice-subject-name').textContent =
    currentSubjectName + ' — Bilet №' + (ticketIdx + 1);
  document.getElementById('practice-subject-count').textContent =
    practiceQuestions.length + ' ta savol';
  document.getElementById('p-total').textContent = practiceQuestions.length;
  document.getElementById('p-correct').textContent = 0;
  document.getElementById('p-wrong').textContent = 0;
  document.getElementById('practice-card').style.display = '';
  document.getElementById('practice-result').classList.remove('show');
  updatePracticeProgress();
  showPage('page-practice');
  renderPracticeQuestion();
}

// ========== PRACTICE ==========
function renderPracticeQuestion() {
  if (practiceIdx >= practiceQuestions.length) { showPracticeResult(); return; }
  practiceAnswered = false;
  const q = practiceQuestions[practiceIdx];
  document.getElementById('p-q-num').textContent = 'No' + (practiceIdx + 1);
  document.getElementById('p-q-text').textContent = q.questionText;

  // Qiyinlik darajasi
  const diffLabels = {1: '🟢 Oson', 2: "🟡 O'rta", 3: '🔴 Qiyin'};
  const diffColors = {1: '#22c55e', 2: '#f59e0b', 3: '#ef4444'};
  let diffBadge = document.getElementById('p-diff-badge');
  if (!diffBadge) {
    diffBadge = document.createElement('span');
    diffBadge.id = 'p-diff-badge';
    diffBadge.style.cssText = 'font-size:11px;padding:3px 8px;border-radius:5px;font-weight:700;margin-left:8px;flex-shrink:0;';
    document.getElementById('p-q-num').parentNode.appendChild(diffBadge);
  }
  diffBadge.textContent = diffLabels[q.difficulty] || '';
  diffBadge.style.color = diffColors[q.difficulty] || '#94a3b8';
  diffBadge.style.background = 'rgba(0,0,0,0.2)';
  diffBadge.style.border = '1px solid ' + (diffColors[q.difficulty] || '#94a3b8');
  const opts = [q.optionA, q.optionB, q.optionC, q.optionD].filter(Boolean);
  const optionsEl = document.getElementById('p-options');
  optionsEl.innerHTML = '';
  opts.forEach((opt, i) => {
    const btn = document.createElement('button');
    btn.className = 'option-btn';
    btn.innerHTML = '<span class="option-letter">' + LETTERS[i] + '</span>' + opt;
    btn.onclick = () => practiceSelectAnswer(opt, q.correctAnswer, opts);
    optionsEl.appendChild(btn);
  });
  document.getElementById('p-feedback').className = 'feedback';
  document.getElementById('p-btn-next').style.display = 'none';
}

function practiceSelectAnswer(chosen, correct, opts) {
  if (practiceAnswered) return;
  practiceAnswered = true;
  const btns = document.querySelectorAll('#p-options .option-btn');
  const isCorrect = chosen === correct;
  if (isCorrect) practiceCorrect++; else practiceWrong++;
  btns.forEach((btn, i) => {
    btn.disabled = true;
    if (opts[i] === correct) btn.classList.add(isCorrect && opts[i] === chosen ? 'correct' : 'reveal-correct');
    else if (opts[i] === chosen && !isCorrect) btn.classList.add('wrong');
  });
  const fb = document.getElementById('p-feedback');
  fb.className = 'feedback show ' + (isCorrect ? 'correct-fb' : 'wrong-fb');
  document.getElementById('p-fb-icon').textContent = isCorrect ? '✅' : '❌';
  document.getElementById('p-fb-text').textContent = isCorrect ? "To'g'ri!" : "Noto'g'ri. To'g'ri javob: " + correct;
  document.getElementById('p-btn-next').style.display = 'block';
  document.getElementById('p-correct').textContent = practiceCorrect;
  document.getElementById('p-wrong').textContent = practiceWrong;
  updatePracticeProgress();
}

function practiceSkip() {
  if (practiceAnswered) { practiceNext(); return; }
  practiceAnswered = true;
  const q = practiceQuestions[practiceIdx];
  const opts = [q.optionA, q.optionB, q.optionC, q.optionD].filter(Boolean);
  const btns = document.querySelectorAll('#p-options .option-btn');
  btns.forEach((btn, i) => { btn.disabled = true; if (opts[i] === q.correctAnswer) btn.classList.add('reveal-correct'); });
  const fb = document.getElementById('p-feedback');
  fb.className = 'feedback show wrong-fb';
  document.getElementById('p-fb-icon').textContent = '⏭️';
  document.getElementById('p-fb-text').textContent = "O'tkazildi. To'g'ri javob: " + q.correctAnswer;
  document.getElementById('p-btn-next').style.display = 'block';
  updatePracticeProgress();
}

function practiceNext() { practiceIdx++; renderPracticeQuestion(); }

function updatePracticeProgress() {
  const done = practiceCorrect + practiceWrong;
  const total = practiceQuestions.length;
  const pct = total ? Math.round((done / total) * 100) : 0;
  document.getElementById('p-progress-text').textContent = done + ' / ' + total;
  document.getElementById('p-progress-pct').textContent = pct + '%';
  document.getElementById('p-progress-fill').style.width = pct + '%';
}

function showPracticeResult() {
  document.getElementById('practice-card').style.display = 'none';
  document.getElementById('practice-result').classList.add('show');
  const total = practiceQuestions.length;
  const pct = total ? Math.round((practiceCorrect / total) * 100) : 0;
  document.getElementById('pr-score').textContent = pct + '%';
  document.getElementById('pr-correct').textContent = practiceCorrect;
  document.getElementById('pr-wrong').textContent = practiceWrong;
  document.getElementById('pr-total').textContent = total;
  document.getElementById('pr-emoji').textContent = pct >= 75 ? '🎉' : pct >= 55 ? '👍' : '📚';
  document.getElementById('pr-sub').textContent = pct >= 75 ? 'Ajoyib!' : pct >= 55 ? 'Yaxshi, yana mashq qiling' : "Ko'proq o'rganing";
  document.getElementById('p-progress-text').textContent = total + ' / ' + total;
  document.getElementById('p-progress-pct').textContent = '100%';
  document.getElementById('p-progress-fill').style.width = '100%';
}

function restartPractice() {
  practiceIdx = 0; practiceCorrect = 0; practiceWrong = 0; practiceAnswered = false;
  document.getElementById('p-correct').textContent = 0;
  document.getElementById('p-wrong').textContent = 0;
  document.getElementById('practice-card').style.display = '';
  document.getElementById('practice-result').classList.remove('show');
  updatePracticeProgress();
  renderPracticeQuestion();
}

// ========== EXAM ==========
async function startExam() {
  try {
    const res = await fetch('/api/exam/start');
    examQuestions = await res.json();
    if (examQuestions.length === 0) { alert('Savollar topilmadi. Avval savollar qo\'shing.'); return; }
    examIdx = 0; examCorrect = 0; examWrong = 0; examAnswered = false; examAnswers = {};
    document.getElementById('e-total').textContent = examQuestions.length;
    document.getElementById('e-correct').textContent = 0;
    document.getElementById('e-wrong').textContent = 0;
    document.getElementById('exam-card').style.display = '';
    document.getElementById('exam-result').classList.remove('show');
    showPage('page-exam');
    renderExamQuestion();
  } catch (e) { console.error('Imtihon boshlanmadi:', e); }
}

function renderExamQuestion() {
  if (examIdx >= examQuestions.length) { submitExam(); return; }
  examAnswered = false;
  const q = examQuestions[examIdx];
  document.getElementById('e-q-num').textContent = 'No' + (examIdx + 1);
  document.getElementById('e-q-text').textContent = q.questionText;
  document.getElementById('e-q-subject').textContent = q.subjectName;

  // Qiyinlik darajasi
  const diffLabels = {1: '🟢 Oson', 2: "🟡 O'rta", 3: '🔴 Qiyin'};
  const diffColors = {1: '#22c55e', 2: '#f59e0b', 3: '#ef4444'};
  let diffBadge = document.getElementById('e-diff-badge');
  if (!diffBadge) {
    diffBadge = document.createElement('span');
    diffBadge.id = 'e-diff-badge';
    diffBadge.style.cssText = 'font-size:11px;padding:3px 8px;border-radius:5px;font-weight:700;flex-shrink:0;';
    document.getElementById('e-q-subject').insertAdjacentElement('afterend', diffBadge);
  }
  diffBadge.textContent = diffLabels[q.difficulty] || '';
  diffBadge.style.color = diffColors[q.difficulty] || '#94a3b8';
  diffBadge.style.background = 'rgba(0,0,0,0.2)';
  diffBadge.style.border = '1px solid ' + (diffColors[q.difficulty] || '#94a3b8');
  const opts = [q.optionA, q.optionB, q.optionC, q.optionD].filter(Boolean);
  const optionsEl = document.getElementById('e-options');
  optionsEl.innerHTML = '';
  opts.forEach((opt, i) => {
    const btn = document.createElement('button');
    btn.className = 'option-btn';
    btn.innerHTML = '<span class="option-letter">' + LETTERS[i] + '</span>' + opt;
    btn.onclick = () => examSelectAnswer(q.id, opt, opts);
    optionsEl.appendChild(btn);
  });
  document.getElementById('e-feedback').className = 'feedback';
  document.getElementById('e-btn-next').style.display = 'none';
  updateExamProgress();
}

function examSelectAnswer(qId, chosen, opts) {
  if (examAnswered) return;
  examAnswered = true;
  examAnswers[qId] = chosen;
  const btns = document.querySelectorAll('#e-options .option-btn');
  // Faqat tanlangan variantni highlight qilamiz (yashil emas, oddiy)
  btns.forEach((btn, i) => {
    btn.disabled = true;
    if (opts[i] === chosen) {
      btn.style.borderColor = '#60a5fa';
      btn.style.background = 'rgba(59,130,246,0.15)';
    }
  });
  document.getElementById('e-btn-next').style.display = 'block';
  updateExamProgress();
}

function examNext() { examIdx++; renderExamQuestion(); }

function updateExamProgress() {
  const done = Object.keys(examAnswers).length;
  const total = examQuestions.length;
  const pct = total ? Math.round((done / total) * 100) : 0;
  document.getElementById('e-progress-text').textContent = done + ' / ' + total;
  document.getElementById('e-progress-pct').textContent = pct + '%';
  document.getElementById('e-progress-fill').style.width = pct + '%';
}

async function submitExam() {
  try {
    const res = await fetch('/api/exam/submit', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({answers: examAnswers})
    });
    const result = await res.json();
    showExamResult(result);
  } catch (e) { console.error('Natija yuklanmadi:', e); }
}

function showExamResult(result) {
  document.getElementById('exam-card').style.display = 'none';
  document.getElementById('exam-result').classList.add('show');
  const scoreEl = document.getElementById('er-score');
  scoreEl.textContent = result.percentage + '%';
  scoreEl.className = 'result-score ' + (result.passed ? 'pass' : 'fail');
  document.getElementById('er-grade').textContent = result.grade + ' (' + result.gradeText + ')';
  document.getElementById('er-grade').style.color = result.passed ? '#22c55e' : '#ef4444';
  document.getElementById('er-correct').textContent = result.correctAnswers;
  document.getElementById('er-wrong').textContent = result.wrongAnswers;
  document.getElementById('er-total').textContent = result.totalQuestions;
  document.getElementById('er-emoji').textContent = result.passed ? '🎉' : '😔';
  document.getElementById('er-sub').textContent = result.passed
    ? 'Tabriklaymiz! Imtihondan o\'tdingiz!'
    : 'Afsuski, imtihondan o\'ta olmadingiz. Qayta urinib ko\'ring.';
  document.getElementById('e-progress-text').textContent = result.totalQuestions + ' / ' + result.totalQuestions;
  document.getElementById('e-progress-pct').textContent = '100%';
  document.getElementById('e-progress-fill').style.width = '100%';
}
