function showSignup() {
  document.getElementById("login-box").style.display = "none";
  document.getElementById("signup-box").style.display = "block";
}

function showLogin() {
  document.getElementById("signup-box").style.display = "none";
  document.getElementById("login-box").style.display = "block";
}

async function login() {
  const email = document.getElementById("login-email").value;
  const password = document.getElementById("login-password").value;
  const errorBox = document.getElementById("login-error");
  errorBox.textContent = "";

  const res = await fetch(`${API_BASE}/api/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
  });
  const result = await res.json();

  if (result.success) {
    localStorage.setItem("accessToken", result.data.accessToken);
    window.location.href = "list.html";
  } else {
    errorBox.textContent = result.message;
  }
}

async function signup() {
  const email = document.getElementById("signup-email").value;
  const password = document.getElementById("signup-password").value;
  const nickname = document.getElementById("signup-nickname").value;
  const errorBox = document.getElementById("signup-error");
  errorBox.textContent = "";

  const res = await fetch(`${API_BASE}/api/auth/signup`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password, nickname }),
  });
  const result = await res.json();

  if (result.success) {
    alert("회원가입이 완료되었습니다. 로그인해주세요.");
    showLogin();
  } else {
    errorBox.textContent = result.message;
  }
}
