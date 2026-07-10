(function(){
  // Same-origin: this file is served by Spring Boot itself, so no host/port needed.
  const API_BASE = '/api/interest';

  let activeTab = 'day';

  const $ = (id) => document.getElementById(id);
  const statusEl = $('apiStatus');
  const calcBtn = $('calcBtn');
  const clearAllBtn = $('clearAllBtn');
  const refreshBtn = $('refreshBtn');

  const tabs = document.querySelectorAll('.tab');
  const dayForm = $('dayForm');
  const monthForm = $('monthForm');
  const rdForm = $('rdForm');
  const rdTableWrap = $('rdTableWrap');

  tabs.forEach(t => t.addEventListener('click', () => {
    tabs.forEach(x => x.classList.remove('active'));
    t.classList.add('active');
    activeTab = t.dataset.tab;
    dayForm.classList.toggle('active', activeTab === 'day');
    monthForm.classList.toggle('active', activeTab === 'month');
    rdForm.classList.toggle('active', activeTab === 'rd');
    rdTableWrap.classList.remove('show');
    $('resultStub').classList.remove('show');
    $('resultEmpty').classList.remove('hidden');
    clearStatus();
    loadHistory();
  }));

  function fmtINR(num){
    const n = Number(num);
    const [intPart, decPart] = n.toFixed(2).split('.');
    let sign = '';
    let digits = intPart;
    if(digits.startsWith('-')){ sign = '-'; digits = digits.slice(1); }
    let last3 = digits.slice(-3);
    let rest = digits.slice(0, -3);
    if(rest !== ''){
      rest = rest.replace(/\B(?=(\d{2})+(?!\d))/g, ',');
      last3 = ',' + last3;
    }
    return '₹' + sign + rest + last3 + '.' + decPart;
  }

  function setError(fieldId, show){
    const field = $(fieldId + '-field');
    if(field) field.classList.toggle('error', show);
  }

  function clearAllErrors(prefix){
    ['amount', 'deposit', 'rate', 'start', 'end', 'months'].forEach(f => setError(prefix + '-' + f, false));
  }

  function setStatus(message, ok){
    statusEl.textContent = message || '';
    statusEl.classList.toggle('ok', !!ok);
  }
  function clearStatus(){ setStatus('', false); }

  // Parse a "YYYY-MM-DD" string (the guaranteed format of a <input type="date">
  // value) into a UTC-midnight timestamp. Using Date.UTC directly on the parts
  // avoids relying on the JS engine's string parser, which is inconsistent
  // across browsers/webviews for date-only strings.
  function parseIsoDateAsUtc(value){
    if(!value) return null;
    const m = /^(\d{4})-(\d{2})-(\d{2})$/.exec(value);
    if(!m) return null;
    const year = Number(m[1]), month = Number(m[2]), day = Number(m[3]);
    const ts = Date.UTC(year, month - 1, day);
    const check = new Date(ts);
    if(check.getUTCFullYear() !== year || check.getUTCMonth() !== month - 1 || check.getUTCDate() !== day){
      return null;
    }
    return ts;
  }

  function calcActualDays(){
    const s = parseIsoDateAsUtc($('d-start').value);
    const e = parseIsoDateAsUtc($('d-end').value);
    if(s !== null && e !== null){
      const days = Math.round((e - s) / 86400000);
      $('d-days').value = days > 0 ? days + ' day' + (days === 1 ? '' : 's') : '';
      return days;
    }
    $('d-days').value = '';
    return null;
  }
  ['change', 'input', 'blur'].forEach(evt => {
    $('d-start').addEventListener(evt, calcActualDays);
    $('d-end').addEventListener(evt, calcActualDays);
  });
  // Also run once on load, in case the browser autofilled the date fields
  calcActualDays();

  // Add `months` whole calendar months to an ISO "YYYY-MM-DD" start date,
  // clamping the day if the target month is shorter (e.g. Jan 31 + 1mo -> Feb 28/29).
  function addMonthsIso(isoDate, months){
    const m = /^(\d{4})-(\d{2})-(\d{2})$/.exec(isoDate);
    if(!m) return null;
    const year = Number(m[1]), month = Number(m[2]) - 1, day = Number(m[3]);
    const totalMonths = year * 12 + month + months;
    const targetYear = Math.floor(totalMonths / 12);
    const targetMonth = totalMonths % 12;
    // Day 0 of the *next* month = last day of the target month
    const lastDayOfTargetMonth = new Date(Date.UTC(targetYear, targetMonth + 1, 0)).getUTCDate();
    const targetDay = Math.min(day, lastDayOfTargetMonth);
    const result = new Date(Date.UTC(targetYear, targetMonth, targetDay));
    return result.toISOString().slice(0, 10);
  }

  function fmtDateDisplay(isoDate){
    const m = /^(\d{4})-(\d{2})-(\d{2})$/.exec(isoDate);
    if(!m) return '';
    return `${m[3]}/${m[2]}/${m[1]}`;
  }

  function calcRdMaturity(){
    const start = $('rd-start').value;
    const months = parseInt($('rd-months').value, 10);
    if(start && months > 0){
      const maturity = addMonthsIso(start, months);
      $('rd-maturity').value = maturity ? fmtDateDisplay(maturity) : '';
      return maturity;
    }
    $('rd-maturity').value = '';
    return null;
  }
  ['change', 'input', 'blur'].forEach(evt => {
    $('rd-start').addEventListener(evt, calcRdMaturity);
    $('rd-months').addEventListener(evt, calcRdMaturity);
  });
  calcRdMaturity();

  function validateDayClientSide(){
    let ok = true;
    const amount = parseFloat($('d-amount').value);
    const rate = parseFloat($('d-rate').value);
    const start = $('d-start').value;
    const end = $('d-end').value;
    const days = calcActualDays();

    clearAllErrors('d');

    if(!(amount > 0)){ setError('d-amount', true); ok = false; }
    if(!(rate > 0)){ setError('d-rate', true); ok = false; }
    if(!start){ setError('d-start', true); ok = false; }
    if(!end || (start && days !== null && days <= 0)){ setError('d-end', true); ok = false; }

    return ok ? { amount, interestRate: rate, startDate: start, endDate: end } : null;
  }

  function validateMonthClientSide(){
    let ok = true;
    const amount = parseFloat($('m-amount').value);
    const rate = parseFloat($('m-rate').value);
    const months = parseInt($('m-months').value, 10);

    clearAllErrors('m');

    if(!(amount > 0)){ setError('m-amount', true); ok = false; }
    if(!(rate > 0)){ setError('m-rate', true); ok = false; }
    if(!(months > 0) || $('m-months').value.includes('.')){ setError('m-months', true); ok = false; }

    return ok ? { amount, interestRate: rate, months } : null;
  }

  function validateRdClientSide(){
    let ok = true;
    const monthlyDeposit = parseFloat($('rd-deposit').value);
    const rate = parseFloat($('rd-rate').value);
    const months = parseInt($('rd-months').value, 10);
    const start = $('rd-start').value;

    clearAllErrors('rd');

    if(!(monthlyDeposit > 0)){ setError('rd-deposit', true); ok = false; }
    if(!(rate > 0)){ setError('rd-rate', true); ok = false; }
    if(!(months > 0) || $('rd-months').value.includes('.')){ setError('rd-months', true); ok = false; }
    if(!start){ setError('rd-start', true); ok = false; }

    return ok ? { monthlyDeposit, interestRate: rate, months, startDate: start } : null;
  }

  function renderResult(data, type){
    const stub = $('resultStub');
    if(type === 'rd'){
      $('r-principal').textContent = fmtINR(data.principalAmount);
      $('r-rate').textContent = Number(data.interestRate).toFixed(2) + '%';
      $('r-period-label').textContent = 'Maturity Date';
      $('r-period').textContent = data.maturityDate ? fmtDateDisplay(data.maturityDate) : '—';
      $('r-interest').textContent = fmtINR(data.totalInterest);
      $('r-total').textContent = fmtINR(data.maturityAmount);
      renderRdTable(data.rows);
    } else {
      $('r-principal').textContent = fmtINR(data.amount);
      $('r-rate').textContent = Number(data.interestRate).toFixed(2) + '%';
      if(type === 'day'){
        $('r-period-label').textContent = 'Actual Days';
        $('r-period').textContent = data.actualDays + ' days';
      } else {
        $('r-period-label').textContent = 'Months';
        $('r-period').textContent = data.months + ' mo';
      }
      $('r-interest').textContent = fmtINR(data.interestAmount);
      $('r-total').textContent = fmtINR(data.totalAmount);
      rdTableWrap.classList.remove('show');
    }

    $('resultEmpty').classList.add('hidden');
    stub.classList.remove('show');
    void stub.offsetWidth; // restart reveal animation
    stub.classList.add('show');
  }

  function renderRdTable(rows){
    if(!rows || rows.length === 0){
      rdTableWrap.classList.remove('show');
      return;
    }
    $('rdTableBody').innerHTML = rows.map(r => `
      <tr>
        <td>${r.month}</td>
        <td>${fmtINR(r.deposit)}</td>
        <td>${fmtINR(r.runningBalance)}</td>
        <td>${fmtINR(r.interest)}</td>
      </tr>
    `).join('');
    rdTableWrap.classList.add('show');
  }

  function renderHistory(records, type){
    const list = $('historyList');
    if(!records || records.length === 0){
      list.innerHTML = '<div class="hist-empty">No calculations yet — they\'ll be listed here.</div>';
      clearAllBtn.disabled = true;
      return;
    }
    clearAllBtn.disabled = false;
    // Oldest first, so the numbers read top-to-bottom as 1, 2, 3...
    const sorted = records.map((r, i) => ({ ...r, __no: i + 1 }));
    list.innerHTML = sorted.map((r) => {
      let desc, amt;
      if(type === 'day'){
        desc = `Day-wise · ${r.actualDays}d @ ${Number(r.interestRate).toFixed(2)}%`;
        amt = r.totalAmount;
      } else if(type === 'month'){
        desc = `Month-wise · ${r.months}mo @ ${Number(r.interestRate).toFixed(2)}%`;
        amt = r.totalAmount;
      } else {
        desc = `RD · ${r.months}mo @ ${Number(r.interestRate).toFixed(2)}% · matures ${fmtDateDisplay(r.maturityDate)}`;
        amt = r.maturityAmount;
      }
      return `
        <div class="hist-item" data-record-id="${r.id}" tabindex="0" role="button" title="View this calculation">
          <span class="sr-no">${r.__no}</span>
          <span class="desc">${desc}</span>
          <span>
            <span class="amt">${fmtINR(amt)}</span>
            <button data-id="${r.id}" title="Remove">✕</button>
          </span>
        </div>
      `;
    }).join('');

    // Keep the full record around so clicking a row can redraw its breakdown
    list.querySelectorAll('.hist-item').forEach(row => {
      const record = records.find(r => String(r.id) === row.dataset.recordId);
      const openRecord = () => { if(record) renderResult(record, type); };
      row.addEventListener('click', openRecord);
      row.addEventListener('keydown', (e) => {
        if(e.key === 'Enter' || e.key === ' '){ e.preventDefault(); openRecord(); }
      });
    });

    list.querySelectorAll('button[data-id]').forEach(btn => {
      btn.addEventListener('click', (e) => {
        e.stopPropagation(); // don't also trigger the row click
        deleteRecord(btn.dataset.id, type);
      });
    });
  }

  function typeEndpoint(type){
    return type === 'rd' ? 'rd' : `${type}wise`;
  }

  async function loadHistory(){
    try{
      const res = await fetch(`${API_BASE}/${typeEndpoint(activeTab)}`, { cache: 'no-store' });
      if(!res.ok) throw new Error('Failed to load history');
      const data = await res.json();
      renderHistory(data, activeTab);
    }catch(err){
      $('historyList').innerHTML = '<div class="hist-empty">Could not load history — is the backend running?</div>';
    }
  }

  async function deleteRecord(id, type){
    if(!confirm('Delete this entry? This can\'t be undone.')) return;
    try{
      const res = await fetch(`${API_BASE}/${typeEndpoint(type)}/${id}`, { method: 'DELETE', cache: 'no-store' });
      if(!res.ok) throw new Error('Delete failed');
      await loadHistory();
      setStatus('Entry deleted.', true);
    }catch(err){
      setStatus('Could not delete that record.', false);
    }
  }

  async function clearAllHistory(){
    if(clearAllBtn.disabled) return;
    const label = activeTab === 'day' ? 'day-wise' : (activeTab === 'month' ? 'month-wise' : 'RD');
    if(!confirm(`Delete all ${label} history? This can't be undone.`)) return;
    try{
      const res = await fetch(`${API_BASE}/${typeEndpoint(activeTab)}`, { method: 'DELETE', cache: 'no-store' });
      if(!res.ok) throw new Error('Clear failed');
      await loadHistory();
      setStatus('History cleared.', true);
    }catch(err){
      setStatus('Could not clear history.', false);
    }
  }
  clearAllBtn.addEventListener('click', clearAllHistory);

  refreshBtn.addEventListener('click', () => {
    window.location.reload();
  });

  async function submitCalculation(){
    clearStatus();
    calcBtn.disabled = true;
    calcBtn.textContent = 'Calculating…';

    try{
      let payload, endpoint;
      if(activeTab === 'day'){
        payload = validateDayClientSide();
        endpoint = `${API_BASE}/daywise`;
      } else if(activeTab === 'month'){
        payload = validateMonthClientSide();
        endpoint = `${API_BASE}/monthwise`;
      } else {
        payload = validateRdClientSide();
        endpoint = `${API_BASE}/rd`;
      }
      if(!payload) return; // client-side validation already flagged the fields

      const res = await fetch(endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if(!res.ok){
        let rawText = '';
        let errBody = {};
        try{
          rawText = await res.text();
          errBody = rawText ? JSON.parse(rawText) : {};
        }catch(parseErr){
          errBody = {}; // response wasn't JSON — e.g. a 404/500 HTML page
        }

        if(errBody.fieldErrors){
          // Map backend field names to our prefix, e.g. "interestRate" -> "d-rate"
          const map = activeTab === 'day'
            ? { amount: 'd-amount', interestRate: 'd-rate', startDate: 'd-start', endDate: 'd-end' }
            : activeTab === 'month'
              ? { amount: 'm-amount', interestRate: 'm-rate', months: 'm-months' }
              : { monthlyDeposit: 'rd-deposit', interestRate: 'rd-rate', months: 'rd-months', startDate: 'rd-start' };
          Object.keys(errBody.fieldErrors).forEach(f => {
            if(map[f]) setError(map[f], true);
          });
        }

        let message;
        if(errBody.message){
          message = errBody.message;
        } else if(res.status === 404){
          message = `Endpoint not found (HTTP 404) at ${endpoint}. Make sure you're browsing the app via the Spring Boot server (e.g. http://localhost:8080), not a separate static file server.`;
        } else if(res.status >= 500){
          message = `Server error (HTTP ${res.status}). Check the backend console/log for a stack trace.`;
        } else {
          message = `Unexpected response (HTTP ${res.status})${rawText ? ': ' + rawText.slice(0, 140) : ''}`;
        }
        setStatus(message, false);
        return;
      }

      const data = await res.json();
      renderResult(data, activeTab);
      setStatus('Saved to the ledger.', true);
      loadHistory();

    }catch(err){
      setStatus('Could not reach the backend. Is it running on this same server?', false);
    }finally{
      calcBtn.disabled = false;
      calcBtn.textContent = 'Calculate Interest';
    }
  }

  calcBtn.addEventListener('click', submitCalculation);

  // Load history for the default tab on page load
  loadHistory();
})();