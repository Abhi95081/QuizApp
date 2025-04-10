# 🧠 Quiz App

A simple and smart Quiz Application built with **Jetpack Compose** and powered by **Supabase** for authentication and data storage. This app allows users to log in using a UID, take quizzes, and have their scores stored in a real-time database.

---

## 📱 Features

- 🔐 Login with UID(Stores in the Supabase)
- ❓ Multiple Choice Quiz Interface
- 📊 Real-time Score Submission
- 🧾 View Previous Scores (Optional)
- ☁️ Supabase Integration for Auth & Database

---

## 🏗️ Tech Stack

- **Android** – Jetpack Compose
- **Backend** – [Supabase](https://supabase.com/)
- **Language** – Kotlin
- **Auth & DB** – Supabase Realtime DB and Auth

---

## 🧩 Database Schema (Supabase)

### Table: `quiz_scores`
| Column Name | Type      | Description               |
|-------------|-----------|---------------------------|
| `id`        | UUID      | Primary key (auto-gen)    |
| `uid`       | Text      | User's UID (from login)   |
| `score`     | Integer   | Quiz score                |
| `created_at`| Timestamp | Time of submission        |

> ✅ Ensure Row Level Security (RLS) is enabled with policies allowing read/write for authenticated users.

![Screenshot 2025-04-06 225025](https://github.com/user-attachments/assets/1f4d5f2a-240e-464c-82f5-110a09f3c3b1)
---

## 🖼️ UI Screens
![WhatsApp Image 2025-04-06 at 10 48 16 PM (1)](https://github.com/user-attachments/assets/457f2e2c-2929-41f4-802e-16d7d879c1d0)
![WhatsApp Image 2025-04-06 at 10 48 16 PM](https://github.com/user-attachments/assets/782ddea2-8602-4665-8bbc-99d87cb1d92b)
![WhatsApp Image 2025-04-06 at 10 48 16 PM (2)](https://github.com/user-attachments/assets/b7821042-70ec-455a-8fb5-f2e6c55ba867)
![WhatsApp Image 2025-04-06 at 10 48 16 PM (3)](https://github.com/user-attachments/assets/2de05c9e-fce0-4399-84b6-d549b2054962)
![WhatsApp Image 2025-04-06 at 10 48 16 PM (4)](https://github.com/user-attachments/assets/bd2de4da-817f-4fc7-b70e-c6d4244794c8)

---

## 🚀 Getting Started

### 1. Clone the Repo
```bash
git clone https://github.com/Abhi95081/QuizApp.git
cd your-repo-name

### 2. Set Up Supabase
Create a Supabase project

Go to SQL Editor and run schema:

create table quiz_scores (
  id uuid primary key default uuid_generate_v4(),
  uid text,
  score integer,
  created_at timestamp default now()
);
## Enable Row Level Security (RLS) and add policy:
-- Allow users to insert their own score
create policy "Allow insert for authenticated"
on quiz_scores for insert
using (auth.uid() = uid);

### 3. Configure App
In your Kotlin code, set up Supabase client using your Project URL and Anon Key:
val supabaseClient = createSupabaseClient(
    supabaseUrl = "https://xyzcompany.supabase.co",
    supabaseKey = "your-anon-key"
)
### 4. Run the App
Open the project in Android Studio and click Run.

### 📚 Future Improvements
Add Categories & Timed Quizzes

Add Google Sign-In

Leaderboard Feature

Export Score History

🧑‍💻 Author
Abhishek Roushan
📫 LinkedIn | GitHub

