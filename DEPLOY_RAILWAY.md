# 🚀 Deploy SportsVista on Railway — Step-by-Step

## Prerequisites
- A **GitHub account** with your SportsVista code pushed to a repository
- A **Railway account** — sign up free at [railway.app](https://railway.app)

---

## Step 1: Push Code to GitHub

If you haven't already, create a GitHub repo and push your code:

```bash
cd SportsVista
git init
git add .
git commit -m "Initial commit - SportsVista"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/SportsVista.git
git push -u origin main
```

> **Important:** Make sure `Dockerfile`, `.dockerignore`, and `database.sql` are committed.

---

## Step 2: Create a Railway Project

1. Go to [railway.app](https://railway.app) and log in
2. Click **"New Project"**
3. Select **"Empty Project"**

---

## Step 3: Add a MySQL Database

1. Inside your project, click **"+ New"** → **"Database"** → **"MySQL"**
2. Railway creates a MySQL instance automatically
3. Click on the MySQL service → **"Variables"** tab
4. Note down these values (you'll need them later):
   - `MYSQLHOST`
   - `MYSQLPORT`
   - `MYSQLDATABASE`
   - `MYSQLUSER`
   - `MYSQLPASSWORD`
   - `MYSQL_URL`

---

## Step 4: Initialize the Database Schema

1. Click on your MySQL service → **"Data"** tab
2. Click **"Connect"** — this opens a query console
3. Copy the **entire contents** of your `database.sql` file
4. Paste it into the query console and click **"Run"**
5. Verify: You should see all tables created and 5 sports seeded

> If the console doesn't work, use **Railway CLI** or any MySQL client with the connection credentials from Step 3.

---

## Step 5: Deploy Your Web App

1. Inside the same project, click **"+ New"** → **"GitHub Repo"**
2. Select your **SportsVista** repository
3. Railway auto-detects the `Dockerfile` and starts building

---

## Step 6: Set Environment Variables

1. Click on your **web service** (not the database)
2. Go to the **"Variables"** tab
3. Click **"+ New Variable"** and add these:
4. First, link the MySQL variables by clicking **"Add Reference"** → select your MySQL service. This auto-populates:
   - `MYSQLHOST`
   - `MYSQLPORT`
   - `MYSQLDATABASE`
   - `MYSQLUSER`
   - `MYSQLPASSWORD`
   - `MYSQL_URL`

5. Then manually add your API keys:

   | Variable Name | Value |
   |--------------|-------|
   | `CRICAPI_KEY` | `db7e45fa-b899-491f-ae65-eb135564f626` |
   | `FOOTBALL_RAPIDAPI_KEY` | `d6167a7819msh9ffebab8c371583p1a0e7fjsn60c1647ad499` |
   | `NBA_BALLDONTLIE_KEY` | `29d54fa5-88d1-46f3-8908-6c8cf2286abc` |

6. Click **"Deploy"** to restart with the new variables

---

## Step 7: Generate a Public URL

1. Click on your web service
2. Go to **"Settings"** tab
3. Under **"Networking"** → click **"Generate Domain"**
4. Railway gives you a URL like: `https://sportsvista-production-xxxx.up.railway.app`

---

## Step 8: Verify

1. Open your Railway URL in a browser
2. You should see the SportsVista dashboard
3. Wait ~2 minutes for the schedulers to sync match data
4. Verify:
   - ✅ Dashboard loads with the teal theme
   - ✅ Cricket (IPL), Baseball, Football matches appear
   - ✅ Clicking a match shows the detail page
   - ✅ Login/signup works

---

## Troubleshooting

### "Application failed to start"
- Check **"Deploy Logs"** tab in Railway for errors
- Verify all MySQL env vars are set correctly

### "No matches showing up"
- Wait 2-3 minutes — schedulers need time for first sync
- Check logs for "Starting Global Match Sync..."

### Database connection errors
- Make sure Railway MySQL variables are linked to your web service
- Check if database schema was initialized (Step 4)

---

## Useful Commands

**View logs:**
```
Railway Dashboard → Your Service → "Deploy Logs" tab
```

**Restart the service:**
```
Railway Dashboard → Your Service → "Settings" → "Restart"
```

**Update your app:**
Just push to GitHub — Railway auto-deploys on every push:
```bash
git add .
git commit -m "Update feature"
git push
```

---

## Cost

Railway free tier includes:
- **$5 free credit/month** (renews monthly)
- **500 MB RAM** per service
- **1 GB disk** for MySQL
- **No cold starts** — your app stays running
- Auto-deploys from GitHub on every push
