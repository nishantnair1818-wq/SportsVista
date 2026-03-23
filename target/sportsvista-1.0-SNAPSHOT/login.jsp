<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login & Sign Up | SportsVista</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800;900&display=swap" rel="stylesheet">
    <style>
        :root {
            --bg-page: #F7F8FA;
            --bg-card: #FFFFFF;
            --brand: #0D9488;
            --brand-hover: #0F766E;
            --brand-light: #F0FDFA;
            --text-1: #1E293B;
            --text-2: #475569;
            --text-3: #94A3B8;
            --border: #E2E8F0;
            --card-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.06), 0 2px 4px -1px rgba(0, 0, 0, 0.04);
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Inter', sans-serif;
        }

        @keyframes fadeInUp {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        body {
            background: linear-gradient(135deg, #F7F8FA 0%, #F0FDFA 50%, #F7F8FA 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            color: var(--text-1);
        }

        .container {
            width: 100%;
            max-width: 420px;
            padding: 20px;
            animation: fadeInUp 0.6s cubic-bezier(0.16, 1, 0.3, 1) both;
        }

        .auth-card {
            background: var(--bg-card);
            border: 1px solid var(--border);
            border-radius: 16px;
            padding: 36px;
            box-shadow: var(--card-shadow);
            transition: box-shadow 0.3s;
        }

        .auth-card:hover {
            box-shadow: 0 10px 25px -5px rgba(0,0,0,0.08);
        }

        .brand-logo {
            font-size: 1.8rem;
            font-weight: 900;
            text-align: center;
            margin-bottom: 28px;
            background: linear-gradient(135deg, #0F766E, #0D9488, #14B8A6);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            letter-spacing: -0.5px;
        }

        .tabs {
            display: flex;
            background: var(--bg-page);
            padding: 4px;
            border-radius: 10px;
            margin-bottom: 28px;
        }

        .tab-btn {
            flex: 1;
            padding: 9px;
            border: none;
            background: transparent;
            color: var(--text-3);
            font-weight: 600;
            font-size: 0.88rem;
            cursor: pointer;
            border-radius: 8px;
            transition: all 0.25s cubic-bezier(0.16, 1, 0.3, 1);
        }

        .tab-btn.active {
            background: white;
            color: var(--brand);
            box-shadow: 0 1px 3px rgba(0,0,0,0.08);
        }

        .tab-btn:hover:not(.active) {
            color: var(--text-2);
        }

        .form-group {
            margin-bottom: 18px;
        }

        .form-group label {
            display: block;
            margin-bottom: 6px;
            font-size: 0.82rem;
            font-weight: 600;
            color: var(--text-2);
        }

        .form-control {
            width: 100%;
            padding: 11px 14px;
            background: var(--bg-page);
            border: 1px solid var(--border);
            border-radius: 10px;
            color: var(--text-1);
            outline: none;
            transition: all 0.25s cubic-bezier(0.16, 1, 0.3, 1);
            font-size: 0.92rem;
        }

        .form-control:focus {
            border-color: var(--brand);
            background: white;
            box-shadow: 0 0 0 3px rgba(13, 148, 136, 0.1);
        }

        .btn-submit {
            width: 100%;
            padding: 12px;
            background: linear-gradient(135deg, #0F766E, #0D9488);
            border: none;
            border-radius: 10px;
            color: white;
            font-weight: 700;
            font-size: 0.95rem;
            cursor: pointer;
            transition: all 0.25s cubic-bezier(0.16, 1, 0.3, 1);
            margin-top: 8px;
        }

        .btn-submit:hover {
            background: linear-gradient(135deg, #0D9488, #14B8A6);
            transform: translateY(-1px);
            box-shadow: 0 4px 12px rgba(13, 148, 136, 0.25);
        }

        .error-msg {
            background: #FFF1F2;
            border: 1px solid #FDA4AF;
            color: #BE123C;
            padding: 10px 14px;
            border-radius: 10px;
            margin-bottom: 20px;
            font-size: 0.85rem;
            text-align: center;
            font-weight: 500;
        }

        #signupForm { display: none; }

        .back-link {
            text-align: center;
            margin-top: 24px;
            font-size: 0.85rem;
        }

        .back-link a {
            color: var(--text-3);
            text-decoration: none;
            font-weight: 500;
            transition: all 0.2s;
            padding: 4px 8px;
            border-radius: 6px;
        }

        .back-link a:hover {
            color: var(--brand);
            background: var(--brand-light);
        }
    </style>
</head>
<body>

<div class="container">
    <div class="brand-logo">SportsVista</div>

    <div class="auth-card">
        <div class="tabs">
            <button class="tab-btn active" onclick="switchTab('login')">Login</button>
            <button class="tab-btn" onclick="switchTab('signup')">Sign Up</button>
        </div>

        <c:if test="${not empty param.error}">
            <div class="error-msg">${param.error}</div>
        </c:if>

        <form id="loginForm" action="login" method="POST">
            <div class="form-group">
                <label>Username</label>
                <input type="text" name="username" class="form-control" placeholder="Your username" required>
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" class="form-control" placeholder="••••••••" required>
            </div>
            <button type="submit" class="btn-submit">Sign In</button>
        </form>

        <form id="signupForm" action="signup" method="POST">
            <div class="form-group">
                <label>Full Name</label>
                <input type="text" name="fullName" class="form-control" placeholder="e.g. John Doe" required>
            </div>
            <div class="form-group">
                <label>Username</label>
                <input type="text" name="username" class="form-control" placeholder="Choose username" required>
            </div>
            <div class="form-group">
                <label>Email Address</label>
                <input type="email" name="email" class="form-control" placeholder="name@email.com" required>
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" class="form-control" placeholder="Minimum 6 characters" required>
            </div>
            <button type="submit" class="btn-submit">Create Account</button>
        </form>
    </div>

    <div class="back-link">
        <a href="dashboard">← Back to Dashboard</a>
    </div>
</div>
<script>
    function switchTab(tab) {
        const loginForm = document.getElementById('loginForm');
        const signupForm = document.getElementById('signupForm');
        const loginBtn = document.querySelectorAll('.tab-btn')[0];
        const signupBtn = document.querySelectorAll('.tab-btn')[1];

        if (tab === 'login') {
            loginForm.style.display = 'block';
            signupForm.style.display = 'none';
            loginBtn.classList.add('active');
            signupBtn.classList.remove('active');
        } else {
            loginForm.style.display = 'none';
            signupForm.style.display = 'block';
            loginBtn.classList.remove('active');
            signupBtn.classList.add('active');
        }
    }

    // Auto-switch to signup if redirected back with error (optionally handle this)
</script>

</body>
</html>
