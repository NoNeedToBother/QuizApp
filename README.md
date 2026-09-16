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

<img width="300" alt="Screenshot_20260916_155846_ru kpfu itis quiz android" src="https://github.com/user-attachments/assets/d442dca4-1344-4469-add5-01c0277acdd3" />
<img width="300" alt="Screenshot_20260916_155850_ru kpfu itis quiz android" src="https://github.com/user-attachments/assets/e6bb4fd3-c6e7-4da7-bb89-5504390322c1" />


### Main menu

Main menu features two buttons: `Begin` and `Question settings`. The latter opens screen where user can choose
question settings:
- Category: defines theme of questions
- Difficulty: defines how difficult questions are
- Game mode: defines how many questions there will be and how fast should user answer them

<img width="300" alt="Screenshot_20260916_155938_ru kpfu itis quiz android" src="https://github.com/user-attachments/assets/3dcb400d-6e1b-4fb3-9c88-a5fb0cdf066f" />
<img width="300" alt="Screenshot_20260916_155933_ru kpfu itis quiz android" src="https://github.com/user-attachments/assets/2ffdd93a-472e-42a9-8b09-92af59b96417" />

### Questions screen

Gets questions from remote [API](https://opentdb.com/api_config.php) and lets user answer them.
Each question can be swiped to the side to go to the next or previous question and features question text and possible answers.
In the end of quiz resulting score is calculated and saved.

<img width="300" alt="Screenshot_20260916_163251_ru kpfu itis quiz android" src="https://github.com/user-attachments/assets/37f96219-a417-42df-ae7c-73f87edcfa99" />
<img width="300" alt="Screenshot_20260916_163238_ru kpfu itis quiz android" src="https://github.com/user-attachments/assets/71c32d9c-62a5-4a74-85df-11626141b79d" />

### User Profile

Displays information about signed-in user and lets edit user info by clicking an icon at top right corner.

<img width="300" alt="Screenshot_20260916_163028_ru kpfu itis quiz android" src="https://github.com/user-attachments/assets/ef6b44d1-d61b-4f1a-acea-e33252ab6023" />

### Leaderboards

Displays all quiz results sorted by their score from highest to lowest. Allows filtering results by question settings by opening bottom sheet.

<img width="300" alt="Screenshot_20260916_162957_ru kpfu itis quiz android" src="https://github.com/user-attachments/assets/3becc92c-5f12-47c1-8904-c03a9f9959d2" />

### User search

Enables searching users by their username and displays all found profiles.

<img width="300" alt="Screenshot_20260916_162953_ru kpfu itis quiz android" src="https://github.com/user-attachments/assets/1cf3f7ff-8b9c-413b-a8ea-f0d5012fa622" />

