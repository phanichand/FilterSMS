# AI Action Plan for FilterSMS

## Project Goal
To create an Android application that filters incoming SMS messages based on sender and message content (using regex for messages, case-insensitive 'contains' for sender) and sends the filtered SMS content to a preconfigured email address.

## Current Progress

### Phase 1: Project Setup and Basic SMS Listening (Completed)

1.  **Android Project Structure:** Initial Android project structure has been set up.
    *   `AndroidManifest.xml`: Created with `RECEIVE_SMS`, `READ_SMS`, and `INTERNET` permissions, and declared `EmailSettingsActivity`.
    *   `MainActivity.java`: Basic activity created and enhanced for SMS filter configuration and launching `EmailSettingsActivity`.
    *   `SmsReceiver.java`: BroadcastReceiver created to listen for `SMS_RECEIVED` intent and filter messages.
    *   `EmailSettingsActivity.java`: New activity created for managing email settings.
    *   `activity_main.xml`, `activity_email_settings.xml`, `strings.xml`, `themes.xml`, `colors.xml`: Layout and resource files created and updated for separated configurations.
    *   `build.gradle` (project and app level), `settings.gradle`: Gradle build files configured, including JavaMail API dependency.
    *   `gradle.properties`: Added `android.useAndroidX=true` and `android.enableJetifier=true` for AndroidX compatibility.

### Phase 2: SMS Filtering Logic (Updated)

1.  **Configuration Storage:** `SharedPreferences` is *no longer* used for sender/message filters. Instead, a **Room Persistence Library database** is used to store multiple `SmsFilterRule` objects. Each `SmsFilterRule` contains a `sender` (case-insensitive 'contains') and a `messagePattern` (regex).
2.  **Filtering Mechanism:** `SmsReceiver` now retrieves all configured `SmsFilterRule` objects from the Room database and iterates through them. An SMS is filtered if it matches *any* of the stored rules.
3.  **Background Processing:** The `SmsReceiver` has been refactored to use `goAsync()` to prevent the system from terminating the process prematurely. This ensures that the SMS filtering and email sending operations have enough time to complete, even when the app is in the background.
4.  **Multi-part SMS Handling:** The `SmsReceiver` now combines multi-part SMS messages into a single message before processing.

### Phase 3: Email Sending (Updated)

1.  **Email Library Integration:** The JavaMail API has been added as a dependency to `app/build.gradle`.
2.  **SMTP Configuration:** SMTP server details (host, port, username, app-specific password) and the recipient email address are stored and retrieved from `SharedPreferences`.
3.  **Email Sending Function:** A utility class `EmailSender.java` has been created to send emails using the configured SMTP details.
4.  **Trigger Email:** The email sending function is called from `SmsReceiver` when an SMS matches the filtering criteria.
5.  **Asynchronous Email Sending:** The `EmailSender` has been refactored to use an `ExecutorService` for sending emails asynchronously, replacing the deprecated `AsyncTask`.
6.  **Email Subject:** The email subject is now the sender's name, without the "Filtered SMS from" prefix.

### Phase 4: User Interface for Configuration (Updated)

1.  **Email Settings UI:**
    *   `EmailSettingsActivity` now includes a **back button** in its `Toolbar` for easy navigation back to the main screen.
2.  **SMS Filter Rule Management UI:**
    *   `MainActivity` has been redesigned to display a list of configured SMS filter rules using a `RecyclerView`.
    *   A `FloatingActionButton (FAB)` has been added to `MainActivity` to allow users to easily add new filter rules.
    *   A new activity, `AddEditRuleActivity`, along with its layout `activity_add_edit_rule.xml`, has been created to facilitate adding new rules and editing existing ones.
    *   Each rule in the `RecyclerView` can be clicked to edit, and a "Delete" option is available for each rule.
    *   **Resolved Crash:** The app crashing on startup due to an `IllegalStateException` in `MainActivity` (related to `ActionBar` conflict) has been resolved by changing the application's base theme in `app/src/main/res/values/themes.xml` to `Theme.MaterialComponents.DayNight.NoActionBar`. This ensures that the system does not provide a default `ActionBar`, allowing the `Toolbar` to be set correctly.

### Phase 5: Code Cleanup and Refactoring (Completed)

1.  **Deprecated Code:** Replaced the deprecated `AsyncTask` in `EmailSender.java` with a more modern `ExecutorService`.
2.  **Build Warnings:** Addressed build warnings by removing the `package` attribute from `AndroidManifest.xml` and relying on the `namespace` in `build.gradle`.
3.  **Code Quality:** Removed unused and duplicate imports to improve code readability and maintainability.

## Usage Instructions

### Example: Filtering for Specific Phrases from a Sender

If you want to filter messages from a specific sender (e.g., your bank) but only when the message contains certain phrases like "Dear Customer" or "Dear User", you can create a highly specific rule.

1.  **Open the FilterSMS app.**
2.  Tap the **'+'** button to add a new rule.
3.  In the **Sender** field, enter the sender's name (e.g., `HDFCBK`).
4.  In the **Message Pattern** field, enter a regular expression to match the desired phrases. For "Dear Customer" or "Dear User" (case-insensitive), use:
    ```
    (?i)(Dear Customer|Dear User)
    ```
5.  **Save the rule.**

This ensures that only messages from that sender *and* containing one of those specific phrases are filtered and forwarded to your email.

1.  **Build and Install:** Build the Android project using Android Studio and install it on your device.
2.  **Grant Permissions:** Ensure the app has SMS permissions granted. You might need to grant them manually from your device's app settings.
3.  **Configure SMS Filter Rules:**
    *   Open the `FilterSMS` application.
    *   The main screen now displays a list of your SMS filter rules.
    *   To **add a new rule**, tap the `+` (plus) `FloatingActionButton` in the bottom right corner.
    *   In the "Add/Edit Rule" screen:
        *   **Sender:** Enter the sender's name or number (e.g., `+1234567890`, `BankName`). The matching is case-insensitive and checks if the sender *contains* this string.
        *   **Message Pattern:** Enter a regular expression for the message content (e.g., `.*OTP code is \d{6}.*`, `(?i)password reset`).
        *   Tap "Save Rule".
    *   To **edit an existing rule**, tap on the rule in the list on the main screen. Make your changes and tap "Save Rule".
    *   To **delete a rule**, tap the "Delete" text next to the rule in the list on the main screen.
4.  **Configure Email Settings:**
    *   From the `MainActivity`, tap the "Email Settings" button.
    *   **Recipient Email:** Enter the email address where you want to receive the filtered SMS messages.
    *   **SMTP Username:** Enter your Gmail address (the sender email).
    *   **SMTP Password:** **IMPORTANT:** For Gmail, you need to generate an App Password, as direct use of your regular Gmail password is not allowed for security reasons. Go to your Google Account Security settings, enable 2-Step Verification, and then generate an App Password. Use this generated password here.
    *   Tap "Save Email Settings". Use the back button in the toolbar to return to the main screen.
5.  **Test:** Send an SMS to your device that matches your configured filter rules. If successful, you should receive an email with the SMS content.

## Dependencies

*   JavaMail API (`com.sun.mail:android-mail:1.6.7`, `com.sun.mail:android-activation:1.6.7`)
*   Room Persistence Library (`androidx.room:room-runtime`, `androidx.room:room-compiler`)

## Permissions Required

*   `android.permission.RECEIVE_SMS`
*   `android.permission.READ_SMS`
*   `android.permission.INTERNET`
