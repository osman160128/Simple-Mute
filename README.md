<h2>Simple Mute</h2>
This is an Android app built using Java that allows users to mute their phone during a specified time interval. For example, you can set the phone to mute from 1:00 AM to 2:00 AM. During this period, the app will mute notifications and sounds on the phone. The app utilizes various Android components, such as AlarmManager, BroadcastReceiver, and Service, to manage muting functionality and notifications.

<h4>Features</h4>
Time-based Mute: Set a specific time range to mute the phone (e.g., 1:00 AM to 2:00 AM).</br>
Notification Management: The app cancels and manages notifications during the mute period..</br>
Persistent Data Storage: The app uses a Room database to store mute schedule data for persistence across app launches..</br>
ViewModel Architecture: ViewModels are used to manage UI-related data in a lifecycle-conscious way..</br>
<h4>Architecture</h4>
Room Database: For storing mute schedule data (time range, mute status)..</br>
AlarmManager: To schedule mute and unmute operations at specified times..</br>
BroadcastReceiver: To receive system events and trigger muting operations when needed..</br>
Service: To handle background tasks like muting and unmuting the phone..</br>
ViewModel: To manage UI-related data in a lifecycle-aware manner and provide a clean architecture..</br>
<h4>Components Used</h4>
Java: The primary programming language used..</br>
XML: Used for creating layouts and UI components..</br>
Room Database: For persistent data storage..</br>
AlarmManager: For scheduling mute and unmute events..</br>
BroadcastReceiver: For receiving system broadcasts to trigger mute/unmute actions..</br>
Service: For background operations..</br>
