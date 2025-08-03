# EventHopper

This project is an Android application developed in Android Studio. The app connects to a backend service that was build as a part of the "Server side engineering" course and requires proper configuration of your development environment and device to run successfully. 

---

## Prerequisites

Before running the project, ensure you have the following:

- **Android Studio** installed (recommended version 4.0 or higher)
- **Android SDK** installed and configured
- A **physical Android device** or emulator to run the app. (The android versions this was tested on was 13 and 15)
- USB cable (if using a physical device)

---

## 1. Configure `local.properties`

The project relies on some local configuration properties that are **not committed to version control** for security and flexibility reasons.

### Steps:

1. At the root of the project directory, locate the `local.properties` file.  
   If it does not exist, create a new file named `local.properties`.

2. Add the following two lines to the file:

    ```
    sdk.dir= "location to your sdk folder"
    ip_addr=192.168.X.XXX
    ```

3. Replace `192.168.X.XXX` with the **current local IP address** of your development machine.

---

### How to find your local IP address:

- Open **Command Prompt** on Windows.
- Run the command: ipconfig

- Locate the network adapter that you use to connect to the internet (typically your active Wi-Fi adapter).
- Find the **IPv4 Address** value (e.g., `192.168.72.52`).
- Use this IP address in the `ip_addr` property.

**Note:**  
Your local IP address may change frequently, especially if you are on a dynamic IP network.  
You will need to update `ip_addr` in `local.properties` accordingly before running the app each time your IP changes.

---

## 2. Running on a Physical Android Device

To run the app on a real Android device, follow these steps:

1. **Enable USB Debugging:**

 - Go back to **Settings**.
 - Open **Developer Options**.
 - Enable **USB Debugging**.

2. Connect your device to your computer using a USB cable.

3. When prompted on your device, approve the connection and allow USB debugging from your computer.

4. In Android Studio, select your connected device from the device dropdown menu.

---

## 3. Build and Run the Application

- In Android Studio, click the **Run** ▶️ button or use the keyboard shortcut:
- Android Studio will build the project, install the APK on your selected device, and launch the app.

---

## 4. Troubleshooting Connectivity Issues

If the app cannot connect to the backend service, verify the following:

- **Correct IP Address:** Ensure the `ip_addr` in `local.properties` matches your current local IP address.
- **Network Connectivity:** Confirm that your Android device and development machine are connected to the **same Wi-Fi network**.
- **Firewall and Antivirus:** Check that no firewall, antivirus, or VPN is blocking network traffic between your device and the development machine.
- **USB Debugging:** If using a physical device, confirm USB debugging is enabled and the device is authorized.
- **Backend Service:** Verify that the backend service is running and accessible at the configured IP and port.

---



