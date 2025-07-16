# FilterSMS

FilterSMS is an Android application designed to filter incoming SMS messages based on user-defined rules and forward the filtered messages to a specified email address. This app helps in managing and organizing important SMS by automatically sending them to your inbox.

## Features

*   **SMS Filtering:** Define rules based on sender and message content to filter incoming SMS.
*   **Email Forwarding:** Automatically forward filtered SMS messages to a configured email address.
*   **Rule Management:** Add, edit, and delete SMS filtering rules.
*   **Application Logs:** View a log of processed SMS messages and email forwarding attempts.
*   **Email Configuration:** Configure SMTP settings for sending emails directly within the app.

## Prerequisites

Before you begin, ensure you have the following installed:

*   **Android Studio:** The official IDE for Android application development. Download from [https://developer.android.com/studio](https://developer.android.com/studio).
*   **Java Development Kit (JDK):** Android Studio typically bundles a suitable JDK, but ensure you have one configured.
*   **Android SDK:** Installed via Android Studio.

## Setup Instructions

Follow these steps to set up and run the project on your local machine:

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/your-username/FilterSMS.git
    cd FilterSMS
    ```
    *(Note: Replace `https://github.com/your-username/FilterSMS.git` with the actual repository URL if it's hosted elsewhere.)*

2.  **Open in Android Studio:**
    *   Launch Android Studio.
    *   Select `File > Open` and navigate to the cloned `FilterSMS` directory.
    *   Click `Open`. Android Studio will import the project and sync Gradle files. This might take some time.

3.  **Sync Gradle:**
    *   If Gradle doesn't sync automatically, click on the "Sync Project with Gradle Files" button (usually an elephant icon) in the toolbar.

4.  **Install Dependencies:**
    *   Android Studio will automatically download the necessary dependencies defined in `build.gradle` files. Ensure you have an active internet connection.

5.  **Run the Application:**
    *   Connect an Android device to your computer or set up an Android Virtual Device (AVD) using Android Studio's AVD Manager.
    *   Select your target device/AVD from the dropdown menu in the toolbar.
    *   Click the "Run 'app'" button (green play icon) in the toolbar.

## Permissions

The app requires the following permissions to function correctly:

*   `RECEIVE_SMS`: To receive incoming SMS messages.
*   `READ_SMS`: To read SMS messages (required for some Android versions and for filtering).

Upon first launch, the app will request these permissions. Please grant them for the app to work as intended.

## Usage

1.  **Add Filter Rules:**
    *   On the main screen, tap the floating action button (FAB) to add a new rule.
    *   Enter a sender (optional, can be partial match) and a message pattern (regex supported).
    *   Save the rule.

2.  **Configure Email Settings:**
    *   Tap the "Email Settings" button on the main screen.
    *   Enter your recipient email address, SMTP username, password, host, and port.
    *   Save the settings.

3.  **View Logs:**
    *   From the main screen, tap the menu icon (three dots) in the top right corner and select "App Logs" to view a history of processed SMS and email attempts.

## Contributing

If you'd like to contribute, please fork the repository and create a pull request.
