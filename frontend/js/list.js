const token = localStorage.getItem("accessToken");
if (!token) {
  window.location.href = "login.html";
}

function authHeaders() {
  return {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
  };
}

function logout() {
  localStorage.removeItem("accessToken");
  window.location.href = "login.html";
}

function currentYearMonth() {
  const now = new Date();
  const month = String(now.getMonth() + 1).padStart(2, "0");
  return `${now.getFullYear()}-${month}`;
}

async function loadCategories() {
  const res = await fetch(`${API_BASE}/api/categories`, { headers: authHeaders() });
  const result = await res.json();
  const select = document.getElementById("new-category");
  select.innerHTML = "";
  result.data.forEach((category) => {
    const option = document.createElement("option");
    option.value = category.id;
    option.textContent = `${category.name} (${category.type === "INCOME" ? "수입" : "지출"})`;
    select.appendChild(option);
  });
}

async function loadSummary() {
  const yearMonth = currentYearMonth();
  const res = await fetch(`${API_BASE}/api/statistics/monthly?yearMonth=${yearMonth}`, {
    headers: authHeaders(),
  });
  const result = await res.json();
  if (result.success) {
    document.getElementById("summary-income").textContent = result.data.income.toLocaleString();
    document.getElementById("summary-expense").textContent = result.data.expense.toLocaleString();
    document.getElementById("summary-balance").textContent = result.data.balance.toLocaleString();
  }
}

async function loadTransactions() {
  const yearMonth = currentYearMonth();
  const res = await fetch(`${API_BASE}/api/transactions?yearMonth=${yearMonth}&page=0&size=50`, {
    headers: authHeaders(),
  });
  const result = await res.json();
  const tbody = document.getElementById("transaction-list");
  tbody.innerHTML = "";

  result.data.transactions.forEach((tx) => {
    const row = document.createElement("tr");
    const typeLabel = tx.type === "INCOME" ? "수입" : "지출";
    const typeClass = tx.type === "INCOME" ? "income" : "expense";
    row.innerHTML = `
      <td>${tx.transactionDate}</td>
      <td class="${typeClass}">${typeLabel}</td>
      <td>${tx.categoryName}</td>
      <td class="${typeClass}">${tx.amount.toLocaleString()}원</td>
      <td>${tx.description ?? ""}</td>
      <td><button class="delete-btn" onclick="deleteTransaction(${tx.id})">삭제</button></td>
    `;
    tbody.appendChild(row);
  });
}

async function createTransaction() {
  const type = document.getElementById("new-type").value;
  const categoryId = document.getElementById("new-category").value;
  const amount = document.getElementById("new-amount").value;
  const transactionDate = document.getElementById("new-date").value;
  const description = document.getElementById("new-description").value;
  const errorBox = document.getElementById("create-error");
  errorBox.textContent = "";

  const res = await fetch(`${API_BASE}/api/transactions`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify({
      type,
      categoryId: Number(categoryId),
      amount: Number(amount),
      transactionDate,
      description,
    }),
  });
  const result = await res.json();

  if (result.success) {
    document.getElementById("new-amount").value = "";
    document.getElementById("new-description").value = "";
    await loadTransactions();
    await loadSummary();
  } else {
    errorBox.textContent = result.message;
  }
}

async function deleteTransaction(id) {
  if (!confirm("이 거래내역을 삭제할까요?")) {
    return;
  }
  await fetch(`${API_BASE}/api/transactions/${id}`, {
    method: "DELETE",
    headers: authHeaders(),
  });
  await loadTransactions();
  await loadSummary();
}

document.getElementById("new-date").value = new Date().toISOString().slice(0, 10);
loadCategories();
loadTransactions();
loadSummary();
