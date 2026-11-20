# GCE OL Biology - Questions & Answers (GCEOLMCQS)

An Android application designed to help Ordinary Level (O-Level) students prepare for GCE Biology examinations through interactive past papers, multiple-choice questions (Paper 1), structured questions and solutions (Paper 2), comprehensive syllabus lesson notes, a biological dictionary, performance analytics, and subscription management.

---

## 📱 Features

- **Paper 1 (MCQs)**
  - Exam Mode: Timed past paper practice simulating official examination conditions.
  - Exercise Mode: Instant feedback on question options with detailed explanations.
  - Interactive diagram-based questions with dedicated image resource management.
  - Review & Correction screen highlighting correct answers vs. user selections.

- **Paper 2 (Structured & Essay Questions)**
  - Categorized past year Paper 2 questions with step-by-step solutions and model answers.

- **Syllabus & Study Notes**
  - Structured chapter-by-chapter lesson notes covering the entire GCE Ordinary Level Biology curriculum.

- **Biological Dictionary**
  - Searchable terms and definitions glossary for quick revision.

- **Performance Analytics & Tracking**
  - Real-time score recording using local Room DB (`ExamScoreDao`).
  - Score history statistics dialog summarizing score percentages, attempts, and progress over time.

- **Subscriptions & Mobile Payment**
  - Integrated with **CamPay SDK (Mobile Money / MoMo Pay)** for package subscriptions.
  - Expiry date calculation and subscription countdown timers.

- **Reminders & Foreground Service**
  - `AppReminderService` notifications to encourage consistent study habits.

---

## 🏗 Architecture & Tech Stack

The app follows the **MVVM (Model-View-ViewModel)** architectural pattern and repository pattern for separation of concerns and maintainability.

### Core Technologies
- **Language**: Kotlin 1.9+
- **Min SDK**: 24 (Android 7.0 Nougat) | **Target SDK**: 34 | **Compile SDK**: 35
- **UI & Layouts**: Material Components for Android, ViewBinding, ViewPager2, Fragments, RecyclerViews, Custom Dialogs.
- **Database / Persistence**:
  - **Room Database**: Local storage for exam scores (`ExamScoreEntity`) and app data cache (`AppDataDao`).
  - **SharedPreferences**: Storing user session data and preferences via `LocalUserDataRepository`.
  - **Asset JSON Data**: Local bundled past papers and question banks read via `AssertReader`.
- **Asynchronous Execution**: Kotlin Coroutines & RxJava 3.
- **Networking**:
  - OkHttp3 & Gson for remote REST API calls (`RestRepository`, `RemoteDatabaseManager`).
  - CamPay Android SDK for processing Mobile Money payments.

---

## 📂 Project Structure

```
app/src/main/java/com/example/gceolmcqs/
├── App.kt                                # Application entry class
├── GCEFirstActivity.kt                   # Splash / Launcher screen
├── MainActivity.kt                       # Main Navigation Host
├── PaperActivity.kt                      # Paper 1 quiz & exam execution activity
├── Paper2Activity.kt                     # Paper 2 questions & solutions viewer activity
├── SyllabusActivity.kt                   # Syllabus & topic lesson viewer activity
├── SubscriptionActivity.kt               # Subscription packages and payment screen
├── DictionaryActivity.kt                 # Biological terms lookup activity
├── NotesActivity.kt                      # Topic notes viewer activity
│
├── adapters/                             # RecyclerView & ViewPager adapters
│   ├── SectionQuestionsRecyclerAdapter.kt
│   ├── SectionRecyclerAdapter.kt
│   ├── HomeRecyclerViewAdapter.kt
│   ├── SubjectContentTableViewPagerAdapter.kt
│   └── ...
│
├── datamodels/                           # Data domain models & entities
│   ├── Paper1Data.kt
│   ├── Paper2Data.kt
│   ├── QuestionData.kt
│   ├── ExamScoreEntity.kt
│   ├── UserData.kt
│   └── SubjectPackageData.kt
│
├── fragments/                            # View fragments & dialogs
│   ├── HomeFragment.kt
│   ├── ExerciseFragment.kt
│   ├── SectionFragment.kt
│   ├── SectionResultFragment.kt
│   ├── CorrectionFragment.kt
│   ├── Paper2QuestionsSolutionFragment.kt
│   └── ...
│
├── repository/                           # Repository layer handling data sources
│   ├── Paper1DataRepository.kt
│   ├── Paper2DataRepository.kt
│   ├── ExamScoreDataRepository.kt
│   ├── LocalUserDataRepository.kt
│   ├── RemoteDatabaseManager.kt
│   └── RestRepository.kt
│
├── roomDB/                               # Room persistence definitions
│   ├── ExamScoreDao.kt
│   ├── AppDataDoa.kt
│   └── ...
│
└── services / helpers                    # Utilities & Background services
    ├── MomoPayService.kt
    ├── AppReminderService.kt
    ├── ImageResourceManager.kt
    ├── UserScoresStatsTracker.kt
    └── AssertReader.kt
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio**: Jellyfish / Ladybug or newer recommended.
- **JDK**: Version 17 (or 1.8 compatibility configured).
- **Android SDK**: API Level 34+ installed.

### Build & Run
1. Clone or open the project folder in Android Studio.
2. Allow Gradle sync to complete and fetch dependencies.
3. Select the `app` module run configuration.
4. Run on an Android Emulator or connected physical device (API level 24+).

To assemble a debug APK via CLI:
```bash
./gradlew :app:assembleDebug
```

---

## 📄 License
Privately managed application for GCE Ordinary Level Biology prep.
