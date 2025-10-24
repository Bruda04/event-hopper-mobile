# 📱 EventHopper Android App

EventHopper is an Android application developed in **Java** that connects directly to the local Spring Boot backend for real-time event management and participation.

---

## 🚀 Overview

This app supports:

- Registration and login for **three user types**:
  - 👤 **User** – browse, follow, and attend events
  - 🎤 **Organizer** – create and manage personal events
  - 🛠️ **Service Provider** – offer products and services related to events
- Creating, scheduling, and editing events
- Sending **invitations** and **notifications** to participants
- Searching and filtering events by category, date, or location
- **Purchasing products** and **booking services** associated with events
- Viewing and tracking upcoming and past events
- Full integration between **mobile**, **web**, and **backend API** clients

🔗 **Related Repositories**

- **Backend:** [EventHopper Backend](https://github.com/Bruda04/event-hopper-backend)
- **Frontend:** [EventHopper Frontend](https://github.com/Bruda04/event-hopper-frontend)

---

## 🧰 Tech Stack

| Component          | Technology               |
| ------------------ | ------------------------ |
| Language           | **Java**                 |
| IDE                | **Android Studio**       |
| Minimum SDK        | **Android 8.0 (API 26)** |
| Target SDK         | **Android 15 (API 35)**  |
| Backend Connection | **HTTP (REST API)**      |

---

## ⚙️ Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/Bruda04/event-hopper-mobile.git
cd event-hopper-mobile
```

### 2. Configure `local.properties`

Create or update the `local.properties` file with your local SDK and backend IP address:

```properties
sdk.dir=/path/to/your/sdk
ip_addr=192.168.X.XXX
```

Replace the IP with your computer’s **local IP address**.

---

### 3. Run the Application

- Open the project in Android Studio.
- Connect a physical device or start an emulator.
- Click **Run ▶️** to build and deploy the app.

---

## 👥 Authors

- [Marija Parežanin](https://github.com/marijaparezanin)
- [Vanja Kostić](https://github.com/vanjakostic03)
- [Luka Bradić](https://github.com/Bruda04)
