# 🚀 Deploy SportsVista on Render — Step-by-Step

## Prerequisites
- A **GitHub account** with your SportsVista code pushed to a repo
- A **Render account** — sign up free at [render.com](https://render.com)

---

## Step 1: Push Code to GitHub

```bash
cd SportsVista
git init
git add .
git commit -m "Initial commit - SportsVista"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/SportsVista.git
git push -u origin main
```

> Make sure `Dockerfile`, `database_pg.sql`, and `.dockerignore` are committed.

---

## Step 2: Create a PostgreSQL Database on Render

1. Go to [dashboard.render.com](https://dashboard.render.com)
2. Click **"New +"** → **"PostgreSQL"**
3. Fill in:
   - **Name:** `sportsvista-db`
   - **Database:** `sportsvista`
   - **User:** `sportsvista_user`
   - **Region:** Choose nearest to you
   - **Plan:** Free
4. Click **"Create Database"**
5. Wait for it to be ready (1-2 minutes)

---

## Step 3: Initialize the Database Schema

1. On the database page, scroll down to **"Connections"**
2. Copy the **"External Database URL"** (starts with `postgres://...`)
3. You need to run `database_pg.sql` against this database. Two options:

**Option A: Using psql (if installed)**
```bash
psql "YOUR_EXTERNAL_DATABASE_URL" -f database_pg.sql
```

**Option B: Using an online tool**
1. Go to [pg.new](https://www.pg.new/) or use DBeaver/pgAdmin
2. Connect using the External Database URL from Render
3. Paste the entire contents of `database_pg.sql` and execute

---

## Step 4: Create the Web Service

1. On Render dashboard, click **"New +"** → **"Web Service"**
2. Connect your **GitHub repository** (SportsVista)
3. Configure:
   - **Name:** `sportsvista`
   - **Region:** Same as your database
   - **Runtime:** **Docker**
   - **Plan:** Free
4. Click **"Create Web Service"**

---

## Step 5: Set Environment Variables

1. Click on your web service → **"Environment"** tab
2. Add the following variables:

| Key | Value |
|-----|-------|
| `DATABASE_URL` | *(Copy "Internal Database URL" from your PostgreSQL service — starts with `postgres://`)* |
| `CRICAPI_KEY` | `db7e45fa-b899-491f-ae65-eb135564f626` |
| `FOOTBALL_RAPIDAPI_KEY` | `d6167a7819msh9ffebab8c371583p1a0e7fjsn60c1647ad499` |
| `NBA_BALLDONTLIE_KEY` | `29d54fa5-88d1-46f3-8908-6c8cf2286abc` |

> **IMPORTANT:** Use the **Internal Database URL** (not External) — it's faster and free within Render's network.

3. Click **"Save Changes"** — the service will auto-redeploy

---

## Step 6: Wait for Deployment

1. Go to the **"Logs"** tab to watch the build progress
2. The Docker build takes **3-5 minutes** on first deploy
3. You'll see logs like:
   ```
   DBConnection: Using Render PostgreSQL.
   ConfigLoader: Loaded config.properties...
   Starting Global Match Sync...
   ```
4. Once deployed, you'll see **"Your service is live 🎉"**

---

## Step 7: Access Your Website

1. Click on the URL shown at the top of your service page
   - Format: `https://sportsvista-XXXX.onrender.com`
2. You should see the SportsVista dashboard!
3. Wait ~2 minutes for match data to appear (schedulers syncing)

---

## Verify Everything Works

- ✅ Dashboard loads with teal theme
- ✅ Cricket, Baseball, Football, NFL matches appear (after 2 min)
- ✅ Click a match → detail page with Summary/Lineups/Info tabs
- ✅ Login page works

---

## Updating Your App

Just push to GitHub — Render auto-deploys:
```bash
git add .
git commit -m "Update feature"
git push
```

---

## Important Notes

| Topic | Detail |
|-------|--------|
| **Cold starts** | Free tier spins down after 15 min inactivity. First request takes ~30s |
| **Free DB** | PostgreSQL free tier expires in 90 days. Renew before expiry |
| **Custom domain** | Settings → Custom Domains → add your domain |
| **Logs** | Dashboard → Your Service → Logs tab |
| **Restart** | Dashboard → Your Service → Manual Deploy → "Deploy latest commit" |
