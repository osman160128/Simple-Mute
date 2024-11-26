<h2>Android Mute Scheduler App</h2>
This is an Android app built using Java that allows users to mute their phone during a specified time interval. For example, you can set the phone to mute from 1:00 AM to 2:00 AM. During this period, the app will mute notifications and sounds on the phone. The app utilizes various Android components, such as AlarmManager, BroadcastReceiver, and Service, to manage muting functionality and notifications.

Features
Time-based Mute: Set a specific time range to mute the phone (e.g., 1:00 AM to 2:00 AM).
Notification Management: The app cancels and manages notifications during the mute period.
Persistent Data Storage: The app uses a Room database to store mute schedule data for persistence across app launches.
ViewModel Architecture: ViewModels are used to manage UI-related data in a lifecycle-conscious way.
Architecture
Room Database: For storing mute schedule data (time range, mute status).
AlarmManager: To schedule mute and unmute operations at specified times.
BroadcastReceiver: To receive system events and trigger muting operations when needed.
Service: To handle background tasks like muting and unmuting the phone.
ViewModel: To manage UI-related data in a lifecycle-aware manner and provide a clean architecture.
Components Used
Java: The primary programming language used.
XML: Used for creating layouts and UI components.
Room Database: For persistent data storage.
AlarmManager: For scheduling mute and unmute events.
BroadcastReceiver: For receiving system broadcasts to trigger mute/unmute actions.
Service: For background operations.
