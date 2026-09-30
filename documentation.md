# Quiz Project Documentation (KMP)

## 1. Project Overview

A cross-platform quiz application built with **Kotlin Multiplatform (KMP)**. The app supports Android and iOS, sharing business logic (the `shared` module) and using platform-specific UI layers (`androidApp`, `iosApp` modules).

### Key Technologies

- **Kotlin Multiplatform** — shared code for Android and iOS.
- **Jetpack Compose** — UI for Android.
- **Compose Multiplatform / SwiftUI** — UI for iOS.
- **Koin** — Dependency Injection (DI).
- **Room KMP** — local SQLite database.
- **Ktor** — HTTP network client.
- **Multiplatform Settings** — key-value storage.
- **AndroidX Paging 3** — pagination for leaderboards and user search.
- **Orbit MVI** — presentation architectural pattern.

---

## 2. Architecture

The project follows **Clean Architecture** principles, split into three layers:

| Layer | Purpose | Location |
| --- | --- | --- |
| **Data** | Data sources (DB, network), mappers, repository implementations | `feature/*/data` |
| **Domain** | Business logic (UseCases), repository interfaces, domain models | `feature/*/domain` |
| **Presentation** | UI logic, ViewModels, MVI components, UI models | `feature/*/presentation` |

### Module Structure

- **`androidApp/`** — Android app (Jetpack Compose UI).
- **`iosApp/`** — iOS app (SwiftUI UI).
- **`shared/`** — Shared KMP module.
  - **`commonMain/`** — Common code for all platforms.
  - **`androidMain/`** — Android-specific implementations (actual).
  - **`iosMain/`** — iOS-specific implementations (actual).

---

## 3. `shared` Module: Common Code

The `shared` module contains all business logic shared between Android and iOS.

### 3.1. Core

The `ru.kpfu.itis.quiz.core` package contains shared components.

#### Config

- `Configuration` — global app configuration.
- `PlatformConfiguration` (expect) — platform-specific configuration (e.g., `androidContext` on Android).

#### Database

- `AppDatabase` — main database class (Room).
- `AppDatabaseBuilderFactory` (expect) — factory for creating a `RoomDatabase.Builder`.
- `DatabaseModule` — Koin module for DI.
- `Migrations` — database schema migrations.
- **DAO**: `ResultDao`, `UserDao`.
- **Entity**: `ResultEntity`, `UserEntity`.
- **Relation**: `ResultAndUser`, `UserWithResults`.

#### Network

- `HttpEngineFactory` (expect) — network engine factory.
- `NetworkModule` — Koin module for DI.

#### Settings

- `SettingsFactory` (expect) — settings storage factory.
- `SettingsModule` — Koin module for DI.

#### Util

- `Extensions.kt` — common Kotlin extensions.
- `HtmlDecoder` (expect) — HTML decoding.
- `KMMTimer` (expect) — cross-platform timer.
- `Locale.kt` — locale utilities.
- `Log` (expect) — logging.
- `PasswordValidation.kt` — password validation.
- `UsernameValidation.kt` — username validation.

#### DI

- `Platform.kt` (expect) — platform abstraction.
- `PlatformModule.kt` — Koin module for platform dependencies.

---

### 3.2. Feature Modules

Each feature follows a uniform template:

- **`data/`**
  - Repository implementations (`*RepositoryImpl.kt`).
  - Mappers (`*Mapper.kt`).
  - Data sources (DAO, API services).
- **`domain/`**
  - Repository interfaces (`*Repository.kt`).
  - UseCases (`*UseCase.kt` / `*UseCaseImpl.kt`).
  - Domain models.
- **`presentation/`**
  - ViewModel (`*ViewModel.kt`).
  - MVI components (`*Intent.kt`, `*State.kt`, `*SideEffect.kt`).
  - UI models (`*Ui.kt`).
  - Mappers (`*UiMapper.kt`).
- **`di/`**
  - Koin module (`*Module.kt`).

#### Feature List

| Feature | Purpose | Key Classes |
| --- | --- | --- |
| `authentication` | Sign-in and registration | `SignInViewModel`, `RegisterViewModel`, `AuthenticationRepository` |
| `leaderboard` | Leaderboard | `LeaderboardViewModel`, `ResultPagingSource` |
| `profile` | User profile | `ProfileViewModel`, `OtherUserProfileViewModel`, `UserRepository` |
| `questions` | Quiz gameplay | `QuestionsViewModel`, `QuestionSettingsViewModel` |
| `users` | User search | `SearchUsersViewModel`, `UserPagingSource` |

---

## 4. Database Layer

### 4.1. Overview

Local persistence is implemented with **Room KMP** on top of SQLite.

- **Driver:** `BundledSQLiteDriver` (version-independent).
- **Execution context:** `Dispatchers.IO`.
- **Schema version:** 2.
- **Schema export:** enabled (`exportSchema = true`).
- **Migrations:** `MIGRATION_1_2`.

### 4.2. Entities

#### `UserEntity` (table `users`)

| Field | Type | Description |
| --- | --- | --- |
| `id` | `Long` | Primary key (auto-generated). |
| `username` | `String` | Login. |
| `password` | `String` | Password (encrypted). |
| `profile_picture_uri` | `String` | Avatar URI (default `""`). |
| `info` | `String` | Additional info (default `""`). |
| `date_registered` | `Int` | Registration timestamp. |
| `signed_in` | `Boolean` | Active session flag (default `false`). |

#### `ResultEntity` (table `results`)

| Field | Type | Description |
| --- | --- | --- |
| `id` | `Long` | Primary key (auto-generated). |
| `time` | `Int` | Time spent, seconds. |
| `correct` | `Int` | Correct answers count. |
| `total` | `Int` | Total questions count. |
| `difficulty` | `String` | Difficulty. |
| `category` | `String` | Category. |
| `game_mode` | `String` | Game mode. |
| `score` | `Double` | Final score. |
| `user_id` | `Long` | FK → `users.id` (cascade on delete). |

### 4.3. Relations

- **`ResultAndUser`** — `ResultEntity` + `UserEntity` (used in leaderboard).
- **`UserWithResults`** — `UserEntity` + `List<ResultEntity>` (used in profile).

### 4.4. DAO

#### `UserDao`

- **Write:** `save`, `update`, `updatePassword`, `logout`.
- **Read:** `getByUsernameAndPassword`, `getById`, `getSignedInUser`, `getSignedInUserWithResults`, `getByIdWithResults`, `getByQueryNameWithPaging`, `getAllWithResults`.

#### `ResultDao`

- **Write:** `save`.
- **Read (all `@Transaction`):** `getAll`, `getByGameModeWithPaging`, `getByGameModeAndDifficultyWithPaging`, `getByGameModeAndCategoryWithPaging`, `getByGameModeDifficultyAndCategoryWithPaging`.

> All pagination methods accept `limit` and `offset`.

### 4.5. KMP Initialization

- **`AppDatabase`** — abstract class extending `RoomDatabase`, annotated with `@ConstructedBy(AppDatabaseConstructor::class)`.
- **Expect:** `AppDatabaseConstructor`, `AppDatabaseBuilderFactory`.
- **Android actual:** `Context.getDatabasePath("quiz.db")`.
- **iOS actual:** `NSFileManager` → `NSDocumentDirectory` → `/quiz.db`.

### 4.6. Dependency Injection

- `AppDatabaseBuilderFactory` and `AppDatabase` are **singletons**.
- Configured with `BundledSQLiteDriver` and `Dispatchers.IO`.
- `MIGRATION_1_2` is applied automatically.

---

## 5. Network Layer

### 5.1. Overview

The network layer is built on **Ktor Client** and provides a shared HTTP client for all features.

**External API:** `opentdb.com` (Open Trivia Database).

### 5.2. Components

- **`HttpEngineFactory`** (expect) — factory for creating a platform-specific Ktor engine.
  - Method: `createEngine(config: Configuration): HttpClientEngineFactory<HttpClientEngineConfig>`.
  - Android: `OkHttp` engine with `retryOnConnectionFailure = true`.
  - iOS: `Darwin` engine.
- **`NetworkModule`** (Koin) — provides `HttpClient`, `Json`, and `HttpEngineFactory` as singletons.

### 5.3. HTTP Client Configuration

- **Base URL:** `https://opentdb.com`.
- **JSON:** `isLenient = true`, `ignoreUnknownKeys = true`.
- **Logging:** `LogLevel.BODY`.
- **Timeouts:** connect `5000 ms`, request `10000 ms`, socket `10000 ms`.

### 5.4. External API Endpoints

| Endpoint | Purpose |
| --- | --- |
| `GET /api_category.php` | List of available categories. |
| `GET /api.php` | Fetch questions by `amount`, `difficulty`, `category`. |

---

## 6. Settings Layer

### 6.1. Overview

For storing user settings, the app uses the **Multiplatform Settings** library (key-value storage).

### 6.2. Components

- **`SettingsFactory`** (expect) — factory for creating a `Settings` instance.
  - Method: `create(name: String, conf: PlatformConfiguration): Settings`.
  - Android: `SharedPreferences`.
  - iOS: `NSUserDefaults`.
- **`SettingsModule`** (Koin) — provides `Settings` as a singleton with the name `"quiz_settings"`.

---

## 7. Utility Layer

### 7.1. Components

- **`KMMTimer`** (expect) — cross-platform timer with fixed-rate callback.
  - API: `start()`, `cancel()`, `isRunning()`.
  - Constructor: `name`, `interval`, `delay`, `action`.
  - Android: `kotlin.concurrent.fixedRateTimer`.
  - iOS: `NSTimer` (intervals in seconds, added to `NSRunLoopCommonModes`).
- **`HtmlDecoder`** (expect) — decodes HTML entities into plain text.
  - Signature: `decodeHtml(html: String): String`.
  - Android: `android.text.Html`.
  - iOS: `NSAttributedString`.
- **Other utilities:** `Extensions.kt`, `Locale.kt`, `Log` (expect), `PasswordValidation.kt`, `UsernameValidation.kt`.

---

## 8. Platform Abstractions (expect/actual)

| Component | expect (commonMain) | actual (androidMain) | actual (iosMain) |
| --- | --- | --- | --- |
| Config | `PlatformConfiguration` | `PlatformConfiguration.android.kt` | `PlatformConfiguration.ios.kt` |
| Database | `AppDatabaseBuilderFactory` | `AppDatabaseBuilderFactory.android.kt` | `AppDatabaseBuilderFactory.ios.kt` |
| Network | `HttpEngineFactory` | `HttpEngineFactory.android.kt` (`OkHttp`) | `HttpEngineFactory.ios.kt` (`Darwin`) |
| Settings | `SettingsFactory` | `SettingsFactory.android.kt` (`SharedPreferences`) | `SettingsFactory.ios.kt` (`NSUserDefaults`) |
| Util | `KMMTimer` | `KMMTimer.android.kt` (`Timer`) | `KMMTimer.ios.kt` (`NSTimer`) |
| Util | `HtmlDecoder` | `HtmlDecoder.android.kt` (`Html`) | `HtmlDecoder.ios.kt` (`NSAttributedString`) |
| Util | `Log` | `Log.android.kt` | — |

---

## 9. Domain Layer

### 9.1. Overview

The Domain layer contains pure business logic, independent of any platform or framework. It defines:

- **Repository interfaces** — contracts for data access.
- **UseCases** — single-responsibility business operations.
- **Domain models** — pure data classes used across layers.

### 9.2. Repository Interfaces

| Feature | Repository | Responsibility |
| --- | --- | --- |
| `authentication` | `AuthenticationRepository` | Register, sign-in, get signed-in user. |
| `leaderboard` | `ResultRepository`, `QuestionSettingsRepository` | Fetch results with filters and pagination, read difficulty/game mode. |
| `profile` | `UserRepository` | Get signed-in user, get any user, update profile, logout. |
| `questions` | `QuestionRepository`, `ResultRepository`, `QuestionSettingsRepository`, `UserRepository` | Quiz flow, results, settings. |
| `users` | `UserRepository` | Search users with pagination. |

**`UserRepository` API:**

- `suspend getSignedInUser(): UserWithResults?` — current user + their results.
- `suspend getUser(id: Long): UserWithResults?` — any user + their results.
- `suspend updateUser(updated: User)` — updates username, info, profile picture.
- `suspend updatePassword(userId: Long, password: String)` — updates the password (encrypted).
- `suspend logout()` — resets the `signedIn` flag for all users.
- `suspend findByUsernameQueryWithPaging(username: String, limit: Int, offset: Int): List<User>` — search users by username substring with pagination.

### 9.3. UseCases

UseCases follow a consistent pattern:

- Defined as an interface (`*UseCase`) with an `operator fun invoke(...)`.
- Implemented in the `data/usecase/` package (`*UseCaseImpl`).
- Always wrapped in `withContext(Dispatchers.IO)` for suspending operations.
- Delegate to a repository — **no business logic duplication**.

**`questions` feature UseCases:**

| UseCase | Signature | Purpose |
| --- | --- | --- |
| `GetQuestionsUseCase` | `suspend operator fun invoke(): List<QuestionData>` | Fetches questions for the current game session. |
| `GetQuestionSettingsUseCase` | `suspend operator fun invoke(): QuestionSettings` | Reads difficulty, category, game mode, locale. |
| `SaveQuestionSettingsUseCase` | `suspend operator fun invoke(difficulty?, category?, gameMode?, locale?)` | Saves provided settings (nullable = skip). |
| `GetMaxScoreUseCase` | `suspend operator fun invoke(): Double` | Returns the maximum possible score. |
| `SaveResultsUseCase` | `suspend operator fun invoke(difficulty, category, gameMode, time, correct, total): Double` | Saves a result and returns the computed score. |

**`authentication` feature UseCases:**

| UseCase | Signature | Purpose |
| --- | --- | --- |
| `RegisterUserUseCase` | `suspend operator fun invoke(username, password, confirmPassword): Boolean` | Registers a new user. |
| `SignInUserUseCase` | `suspend operator fun invoke(username, password): Boolean` | Authenticates a user. |
| `GetSignedInUserUseCase` | `suspend operator fun invoke(): User?` | Returns the currently authenticated user. |

**`leaderboard` feature UseCases:**

| UseCase | Signature | Purpose |
| --- | --- | --- |
| `GetLeaderboardUseCase` | `suspend operator fun invoke(gameMode, difficulty?, category?, limit): Pager<Int, Result>` | Builds a `Pager` that streams paginated results from `ResultPagingSource`. |
| `GetDifficultyUseCase` | `suspend operator fun invoke(): Difficulty` | Reads the current difficulty from settings. |
| `GetGameModeUseCase` | `suspend operator fun invoke(): GameMode` | Reads the current game mode from settings. |

**`profile` feature UseCases:**

| UseCase | Signature | Purpose |
| --- | --- | --- |
| `GetCurrentUserUseCase` | `suspend operator fun invoke(): UserWithResults?` | Returns the signed-in user with their results. |
| `GetUserUseCase` | `suspend operator fun invoke(id: Long): UserWithResults?` | Returns any user by ID with their results. |
| `UpdateCredentialsUseCase` | `suspend operator fun invoke(password: String)` | Updates the signed-in user's password. |
| `UpdateProfilePictureUseCase` | `suspend operator fun invoke(uri: String)` | Updates the signed-in user's avatar URI. |
| `UpdateUserInfoUseCase` | `suspend operator fun invoke(info: String?, username: String?)` | Updates the signed-in user's info and/or username (nulls are skipped). |
| `LogoutUseCase` | `suspend operator fun invoke()` | Resets the `signedIn` flag for all users. |

**`users` feature UseCases:**

| UseCase | Signature | Purpose |
| --- | --- | --- |
| `SearchUsersUseCase` | `suspend operator fun invoke(username: String, limit: Int): Pager<Int, User>` | Builds a `Pager` that streams paginated users from `UserPagingSource`. |

### 9.4. Domain Models

- **`User`** — domain representation of a user.
- **`Result`** — domain representation of a quiz result.
- **`QuestionSettings`** — quiz configuration (`difficulty`, `category`, `gameMode`, `locale`).
- **`QuestionData`** — a single quiz question (text, answers, correct answer).
- **`UserWithResults`** — user + their results.

### 9.5. Business Rules

- **Game session length** is determined by `GameMode`:
  - `BLITZ` → 10 questions.
  - `CHALLENGE` → 15 questions.
  - `EXPERT` → 25 questions.
- **Score calculation** (in `ResultRepositoryImpl`) is based on:
  - **Correct ratio:** `e^(correct/total)`.
  - **Game mode factor:** Blitz (1/50), Challenge (1/120), Expert (1/250).
  - **Time penalty:** `e^(-(time * factor)^4)`.
  - **Max score:** 10 points.
  - Formula: `ratioValue * (timeValue + 1) / 2 / e * MAX_SCORE`.
- **Leaderboard filtering rules** (see `ResultRepositoryImpl.getResults`):
  - If `difficulty` and `category` are provided → `getByGameModeDifficultyAndCategoryWithPaging`.
  - If only `difficulty` → `getByGameModeAndDifficultyWithPaging`.
  - If only `category` → `getByGameModeAndCategoryWithPaging`.
  - If none → `getByGameModeWithPaging`.
- **Profile updates:**
  - All update operations first fetch the signed-in user.
  - If no signed-in user exists, a `RuntimeException(Res.string.user_not_present)` is thrown.
  - `UpdateUserInfoUseCase` skips `null` fields (keeps existing values).
  - Passwords are always stored encrypted (`password.encrypt()`).
- **User search:**
  - Search is case-insensitive and matches by substring.
  - The repository appends `%` to the query before passing it to the DAO (`getByQueryNameWithPaging`).
  - Results are mapped to domain `User` with a formatted `dateRegistered`.

### 9.6. Pagination

#### `ResultPagingSource`

- Extends `androidx.paging.PagingSource<Int, Result>`.
- **Parameters (mutable):** `gameMode`, `difficulty`, `category`.
- **Key type:** `Int` (page index, starting from 0).
- **Behavior:**
  - `load(params)` computes `offset = page * params.loadSize`.
  - Calls `ResultRepository.getResults(...)` with the current filters.
  - Returns `LoadResult.Page` with `prevKey`/`nextKey`, or `LoadResult.Error` on failure.
  - `nextKey = null` when the last page is empty (end of list).
- **`getRefreshKey`** — returns a key based on `anchorPosition` for restoring scroll state.

#### `UserPagingSource`

- Extends `androidx.paging.PagingSource<Int, User>`.
- **Parameters (mutable):** `query` (username substring).
- **Key type:** `Int` (page index, starting from 0).
- **Behavior:**
  - `load(params)` computes `offset = page * params.loadSize`.
  - Calls `UserRepository.findByUsernameQueryWithPaging(query, limit, offset)`.
  - Returns `LoadResult.Page` with `prevKey`/`nextKey`, or `LoadResult.Error` on failure.
  - `nextKey = null` when the last page is empty.
- **`getRefreshKey`** — same as `ResultPagingSource`.

---

## 10. Data Layer

### 10.1. Overview

The Data layer implements the contracts defined in Domain. It is responsible for:

- Communicating with **Room** (local DB), **Settings** (KMP settings), and **Ktor** (network).
- Mapping **entities** and **DTOs** into **domain models**.
- Handling errors and returning domain-friendly results.

### 10.2. Repository Implementations

#### `AuthenticationRepositoryImpl`

- **Constructor:** `AuthenticationRepositoryImpl(appDatabase: AppDatabase)`.
- **Methods:**
  - `register(username, password, confirmPassword): Boolean` — creates a `UserEntity`, encrypts the password, sets `dateRegistered` from `Clock.System`, marks the user as `signedIn`.
  - `signIn(username, password): Boolean` — looks up the user by credentials, updates `signedIn` flag on success.
  - `getSignedInUser(): User?` — fetches the currently signed-in user and maps it to the domain `User`.

#### `QuestionRepositoryImpl`

- **Constructor:** `QuestionRepositoryImpl(client: HttpClient)`.
- **Methods:**
  - `getCategoryCode(category: Category): Int` — calls `/api_category.php`, matches the domain `Category` with the API category name, returns its numeric code. Throws `UnknownParameterException` if not found.
  - `getQuestions(amount, difficulty, category): Questions` — calls `/api.php` with `amount`, `difficulty`, `category` parameters, maps the response to domain models.
- **Category name mapping:** a `when` expression with constants (e.g., `"General Knowledge"` → `Category.GENERAL`).

#### `QuestionSettingsRepositoryImpl`

- **Constructor:** `QuestionSettingsRepositoryImpl(settings: Settings)`.
- **Backed by:** Multiplatform `Settings` (key-value).
- **Keys (questions):** `"difficulty"`, `"category"`, `"game_mode"`, `"locale"`.
- **Keys (leaderboard):** `"difficulty"`, `"game_mode"` (read-only subset).
- **Methods:**
  - `getDifficulty() / saveDifficulty()`.
  - `getCategory() / saveCategory()`.
  - `getGameMode() / saveGameMode()`.
  - `getLocale() / saveLocale()`.
- **Defaults:** `Difficulty.MEDIUM`, `Category.GENERAL`, `GameMode.BLITZ`, `AppLocale.SYSTEM`.

#### `ResultRepositoryImpl`

- **Constructor:** `ResultRepositoryImpl(appDatabase: AppDatabase)`.
- **Methods:**
  - `getMaxScore(): Double` — returns `MAX_SCORE = 10.0`.
  - `save(result: Result, userId: Long): Double` — computes the score via `calculateScore`, saves a `ResultEntity`, and returns the score.
  - `getResults(gameMode, difficulty?, category?, limit, offset): List<Result>` — selects the appropriate DAO method based on non-null filters, returns mapped `List<Result>`.
- **Score calculation:** see Business Rules in section 9.5.

#### `UserRepositoryImpl`

- **Constructor:** `UserRepositoryImpl(appDatabase: AppDatabase)`.
- **Methods:**
  - `getSignedInUser(): UserWithResults?` — fetches the signed-in user with all results, mapped to `UserWithResults`.
  - `getUser(id: Long): UserWithResults?` — fetches any user by ID with their results.
  - `updateUser(updated: User)` — loads the `UserEntity` by ID and updates `username`, `info`, `profilePictureUri`.
  - `updatePassword(userId: Long, password: String)` — encrypts and updates the password.
  - `logout()` — resets `signedIn` for all users.
  - `findByUsernameQueryWithPaging(username: String, limit: Int, offset: Int): List<User>` — appends `%` to the query and calls `getByQueryNameWithPaging`, maps entities to domain `User` with formatted `dateRegistered`.
- **Error handling:** if the entity is not found, `updateUser` is a no-op.

### 10.3. Mappers

- **Entity → Domain:** `nullableEntityToUser`, `entityToUser`, `UserEntityMapper`, `mapResultAndUserEntity`, `mapUserEntityWithResults`.
- **DTO → Domain:** `QuestionResponseMapper`.
- **Domain → UI:** `QuestionDataUiMapper`, `QuestionSettingsMapper`, `ResultMapper`, `UserMapper`.

> Mappers are stateless top-level functions or extensions, easy to test.

---

## 11. Presentation Layer (MVI)

### 11.1. Overview

Every screen uses the **MVI** pattern, implemented with the **Orbit MVI** library (`ContainerHost`, `container`, `reduce`, `intent`, `postSideEffect`).

### 11.2. MVI Contracts

Each screen defines three contracts:

- **`<Feature>ScreenIntent`** — user intentions (UI events).
- **`<Feature>ScreenState`** — screen state (data to display).
- **`<Feature>ScreenSideEffect`** — one-shot side effects (navigation, errors).

**Example — `RegisterScreen`:**

- **Intent variants:** `RegisterUser(username, password, confirmPassword)`, `GetSignedInUser`, `ValidatePassword(password)`, `ValidateConfirmPassword(password)`, `ValidateUsername(username)`.
- **SideEffect variants:** `NavigateToMainMenu`, `ShowError(title, message)`.
- **State fields:** `username: String`, `isLoading: Boolean`, `passwordError: String?`, `confirmPasswordError: String?`, `usernameError: String?`. State is `@Serializable` for `SavedStateHandle` persistence.

**Example — `LeaderboardScreen`:**

- **Intent variants:** `LoadResults`, `ChangeFilters(difficulty?, category?, gameMode?)`, `Refresh`, `Retry`.
- **SideEffect variants:** `ShowError(title, message)`, `NavigateToProfile(userId)`.
- **State fields:** `results: LazyPagingItems<Result>`, `filters`, `isLoading: Boolean`, `error: String?`.

**Example — `ProfileScreen`:**

- **Intent variants:** `LoadProfile`, `UpdateUserInfo(info?, username?)`, `UpdateProfilePicture(uri)`, `UpdatePassword(password)`, `Logout`.
- **SideEffect variants:** `ShowError(title, message)`, `NavigateToLogin`, `ProfileUpdated`.
- **State fields:** `user: UserWithResults?`, `isLoading: Boolean`, `error: String?`.

**Example — `OtherUserProfileScreen`:**

- **Intent variants:** `LoadUser(id: Long)`.
- **SideEffect variants:** `ShowError(title, message)`.
- **State fields:** `user: UserWithResults?`, `isLoading: Boolean`, `error: String?`.

**Example — `SearchUsersScreen`:**

- **Intent variants:** `Search(query: String)`, `LoadMore`, `ClearQuery`.
- **SideEffect variants:** `ShowError(title, message)`.
- **State fields:** `users: LazyPagingItems<User>`, `query: String`, `isLoading: Boolean`, `error: String?`.

### 11.3. ViewModel

ViewModels inherit from `ContainerHost<State, SideEffect>` and use the Orbit DSL:

- **`container { ... }`** — initializes state with `SavedStateHandle`.
- **`onIntent(intent)`** — dispatches incoming intents via `when`.
- **`reduce { state.copy(...) }`** — updates state.
- **`postSideEffect(...)`** — emits one-shot effects.
- **`intent { ... }`** — scopes operations for a given intent.

**Example — `RegisterViewModel`:**

- **Constructor:** `RegisterViewModel(savedStateHandle, registerUserUseCase, getSignedInUserUseCase)`.
- **Container:** initialized with `RegisterScreenState()` and a serializer.
- **Intent handlers:** `registerUser`, `getSignedInUser`, `validatePassword` / `validateConfirmPassword` / `validateUsername`.
- **Error handling:** `try/catch (Throwable)` → `ShowError`. Strings from `Res.string.*`.

**Example — `LeaderboardViewModel`:**

- **Constructor:** `LeaderboardViewModel(savedStateHandle, getLeaderboardUseCase, getDifficultyUseCase, getGameModeUseCase)`.
- **Container:** initialized with `LeaderboardScreenState()` and a serializer.
- **Intent handlers:**
  - `LoadResults` — reads current filters and calls `GetLeaderboardUseCase`, stores the `Pager` in state.
  - `ChangeFilters` — updates filters and reloads the pager.
  - `Retry` / `Refresh` — re-invokes the pager.
- **Error handling:** `try/catch (Throwable)` → `ShowError`.

**Example — `ProfileViewModel`:**

- **Constructor:** `ProfileViewModel(savedStateHandle, getCurrentUserUseCase, updateUserInfoUseCase, updateProfilePictureUseCase, updateCredentialsUseCase, logoutUseCase)`.
- **Container:** initialized with `ProfileScreenState()` and a serializer.
- **Intent handlers:**
  - `LoadProfile` — calls `GetCurrentUserUseCase`, stores the result in state.
  - `UpdateUserInfo` / `UpdateProfilePicture` / `UpdatePassword` — call the corresponding UseCase and refresh the profile.
  - `Logout` — calls `LogoutUseCase`, then emits `NavigateToLogin`.
- **Error handling:** `try/catch (Throwable)` → `ShowError`.

**Example — `OtherUserProfileViewModel`:**

- **Constructor:** `OtherUserProfileViewModel(savedStateHandle, getUserUseCase)`.
- **Container:** initialized with `OtherUserProfileScreenState()` and a serializer.
- **Intent handlers:** `LoadUser(id)` — calls `GetUserUseCase`, stores the result in state.

**Example — `SearchUsersViewModel`:**

- **Constructor:** `SearchUsersViewModel(savedStateHandle, searchUsersUseCase)`.
- **Container:** initialized with `SearchUsersScreenState()` and a serializer.
- **Intent handlers:**
  - `Search(query)` — calls `SearchUsersUseCase(query, limit)`, stores the resulting `Pager` in state.
  - `LoadMore` — handled by the pager itself.
  - `ClearQuery` — resets the pager and query.
- **Error handling:** `try/catch (Throwable)` → `ShowError`.

### 11.4. Feature Structure (uniform template)

Every feature's presentation layer follows the same structure:

- **`mvi/`** — `<Feature>ScreenIntent.kt`, `<Feature>ScreenState.kt`, `<Feature>ScreenSideEffect.kt`.
- **`viewmodel/`** — `<Feature>ViewModel.kt`.
- **`model/`** — UI-specific models (`*Ui.kt`).
- **`mapper/`** — Domain → UI mappers (`*UiMapper.kt`).

> **Note:** The `signin` package mirrors `register` exactly. Same pattern for `profile`, `leaderboard`, `questions`, `users`.

---

## 12. Navigation (Android)

Navigation is implemented in the `androidApp` module via Jetpack Compose Navigation.

- `Routes.kt` — the app's route map.
- Entry point: `MainActivity.kt` → `App.kt` → `MainMenuScreen.kt`.

### Feature Screens

| Feature | Screen | File |
| --- | --- | --- |
| Auth | Sign In | `SignInScreen.kt` |
| Auth | Registration | `RegisterScreen.kt` |
| Leaderboard | Leaderboards | `LeaderboardsScreen.kt` |
| Profile | Profile | `ProfileScreen.kt` |
| Profile | Other User Profile | `OtherUserProfileScreen.kt` |
| Questions | Questions | `QuestionsScreen.kt` |
| Questions | Result | `ResultScreen.kt` |
| Questions | Question Settings | `QuestionSettingsScreen.kt` |
| Users | Search Users | `SearchUsersScreen.kt` |
| Main | Main Menu | `MainMenuScreen.kt` |

---

## 13. Design System (core.designsystem)

Shared UI components and theming live in `core.designsystem`.

### Components

- `Components.kt` — base reusable UI components.
- `DropdownMenu.kt` — dropdown menu.
- `EmptyResults.kt` — empty state placeholder.
- `ErrorDialog.kt` — error dialog.
- `Graph.kt` — chart/graph component.
- `InputSections.kt` — form input sections.
- `Stopwatch.kt` — timer/stopwatch UI component.

### Theme

- `Color.kt` — color palette.
- `Font.kt` — font definitions.
- `Theme.kt` — theme composition.
- `Type.kt` — typography.

---

## 14. Documentation Status

| Section | Status |
| --- | --- |
| Overview | ✅ Done |
| Architecture | ✅ Done |
| Core Layer | ✅ Done |
| Database Layer | ✅ Done |
| Network Layer | ✅ Done |
| Settings Layer | ✅ Done |
| Utility Layer | ✅ Done |
| Platform Abstractions | ✅ Done |
| Domain Layer | ✅ Done |
| Data Layer | ✅ Done |
| Presentation Layer (MVI) | ✅ Done |
| Navigation (structure) | ✅ Done |
| Design System (structure) | ✅ Done |

All core sections of the documentation are complete. All five features (`authentication`, `leaderboard`, `profile`, `questions`, `users`) are documented with their repositories, UseCases, pagination sources, and MVI contracts. Source code can be inspected directly in the IDE for implementation details.

---

*End of document.*