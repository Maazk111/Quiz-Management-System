# 📚 Quiz Management System – Java GUI-Based OOP Project

## 🚀 Overview
A GUI-based Java application that allows **teachers** to create quizzes and **students** to attempt them.  
It includes features like auto-marking, result analytics, and file-based persistence using Java File I/O.  
Built as part of the Object-Oriented Programming (OOP) course project at DHA Suffa University.

## 🛠️ Tech Stack
- Java
- Java Swing (for GUI)
- Java File I/O (for persistence)
- MVC Architecture
- OOP Concepts (Inheritance, Composition, Association)

---

## 👨‍🏫 Teacher Features
- Login with predefined credentials
- Create and manage **Question Banks**
- Generate **Quizzes** with date, time, and points
- Automatically **evaluate** quizzes after submission
- View student performance with **basic analytics**

## 👨‍🎓 Student Features
- Login and view available **Courses**
- View and attempt **active quizzes**
- Quizzes are **time-bound** with answer shuffling
- Instantly see marks after submission
- Secure validation of quiz timing (start and end time)

---

## 📁 Folder Structure

QuizManagementSystem/
├── src/
│ └── com/semester/
│ ├── common/
│ ├── login/
│ ├── student/
│ ├── teacher/
│ └── Main.java
├── .gitignore
├── README.md
└── assets/



---

## 🧪 How to Run

1. Open the project in **IntelliJ IDEA**.
2. Run `Main.java`.
3. Use any of the following credentials:

### 👨‍🏫 Teacher Login
- Username: `Sumaira`
- Password: `admin123`

### 👨‍🎓 Student Login
- Username: `Maaz` or `Rumaisa` or others
- Password: `stu123`

4. Navigate through GUI and explore features!

---

## 📸 Sample UI Screenshots

| Login Page | Teacher Panel                  | Student Quiz |
|------------|--------------------------------|----------------|
| ![Login](assets/login.png) | ![Teacher](assets/teacher.png) | ![Quiz](assets/student_attempt.png) |

---

## 📈 Analytics Example
After quizzes are submitted, the system provides:
- Percentage of students who got each question correct
- Highlight difficult questions
- Auto-generated attendance (absent/present)

---

## 🔐 Security & Validation
- Quiz timing validation to block late attempts
- Randomization of question order to avoid cheating
- User-type-based role access (Teacher vs Student)

---

## 📄 License
This project is created for educational purposes. You can modify and reuse it as needed.

---

## 👥 Credits
Developed by: **Maaz**  
Instructor: DHA Suffa University - OOP Project (Spring 2023)
