# Quiz

This app features creating local user profiles, answering quiz questions based on different settings, such as category and difficulty, saving results in leaderboard and watching other user results in leaderboards.

## Installation

#### 1. Copy this repository in Android Studio

```bash
git clone https://github.com/NoNeedToBother/QuizApp.git
```

#### 2. Open in your IDE

Open the cloned root directory in IntelliJ IDEA or Android Studio and let Gradle sync finish before running anything.

#### 3. Run the app

Use the run configurations at the top of the IDE, or run from the terminal:
- **Android**: select the `androidApp` run configuration or build from terminal:
```bash
./gradlew :androidApp:assembleDebug
```
- **iOS**: open the Xcode project in the `iosApp` directory and run it on a simulator

## Using

### Authorization screen

Lets user register a new profile or login into existing one. To register, user should provide username and password. All profiles are saved locally on user's device.

### Main menu

Main menu features two buttons: `Begin` and `Question settings`. The latter opens screen where user can choose
question settings:
- Category: defines theme of questions
- Difficulty: defines how difficult questions are
- Game mode: defines how many questions there will be and how fast should user answer them

### Questions screen

Gets questions from remote [API](https://opentdb.com/api_config.php) and lets user answer them.
Each question can be swiped to the side to go to the next or previous question and features question text and possible answers.
In the end of quiz resulting score is calculated and saved.

### User Profile

Displays information about signed-in user and lets edit user info by clicking an icon at top right corner.

### Leaderboards

Displays all quiz results sorted by their score from highest to lowest. Allows filtering results by question settings by opening bottom sheet.

### User search

Enables searching users by their username and displays all found profiles.
