# Fix Test Execution and Project Structure

The error `No tests found for given includes: [FirebaseConnectionTest]` occurs because the test is located in the `src/androidTest` folder (Instrumented Tests), but the system is trying to run it using the `testDebugUnitTest` task (Unit Tests).

Additionally, the project currently has production logic (UseCases and Domain models) inside the `src/androidTest` folder, which prevents them from being used in the main application and causes confusion for the test runners.

## Proposed Changes

### 1. Move Production Logic to `src/main`
Move the core business logic and models from `src/androidTest` to `src/main` so the application can use them.

#### [MODIFY] [MainActivity.kt](file:///C:/Proyectos-Admin/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/MainActivity.kt)
- Remove the local `UserRole` enum to avoid conflicts with the global one in `AuthDomain.kt`.
- Update imports to use the shared `UserRole`.

#### [NEW] [AuthDomain.kt](file:///C:/Proyectos-Admin/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/AuthDomain.kt)
#### [NEW] [SignInUseCase.kt](file:///C:/Proyectos-Admin/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/SignInUseCase.kt)
#### [NEW] [RegisterMileageUseCase.kt](file:///C:/Proyectos-Admin/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/RegisterMileageUseCase.kt)
- Move these files from `src/androidTest/java/...` to `src/main/java/...`.

### 2. Move Unit Tests to `src/test`
Move tests that do not require an Android device (those using Fakes) to the `src/test` folder. This will allow them to run quickly as Unit Tests.

#### [NEW] [RegisterMileageUseCaseTest.kt](file:///C:/Proyectos-Admin/Proyecto01-Admin/app/src/test/java/com/example/proyecto01_administracion/RegisterMileageUseCaseTest.kt)
#### [NEW] [FakeAuthRepository.kt](file:///C:/Proyectos-Admin/Proyecto01-Admin/app/src/test/java/com/example/proyecto01_administracion/FakeAuthRepository.kt)
#### [NEW] [FakeMileageRepository.kt](file:///C:/Proyectos-Admin/Proyecto01-Admin/app/src/test/java/com/example/proyecto01_administracion/FakeMileageRepository.kt)
- Move these files from `src/androidTest/java/...` to `src/test/java/...`.

### 3. Keep Instrumented Tests in `src/androidTest`
Integration tests that require real Firebase connectivity must remain in `src/androidTest`.

#### [MODIFY] [FirebaseConnectionTest.kt](file:///C:/Proyectos-Admin/Proyecto01-Admin/app/src/androidTest/java/com/example/proyecto01_administracion/FirebaseConnectionTest.kt)
- Keep in current location.
- Ensure it uses the correct imports from the new `src/main` location.

## Verification Plan

### Automated Tests
- Run unit tests: `./gradlew :app:testDebugUnitTest`
- Run instrumented tests (requires emulator/device): `./gradlew :app:connectedDebugAndroidTest`

### Manual Verification
- Verify that the project builds successfully after moving the production code.
- Confirm that Android Studio no longer shows "No tests found" when running `RegisterMileageUseCaseTest`.
