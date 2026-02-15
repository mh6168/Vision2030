# Vision2030 (New Republic) — Java Booking System

## 📌 Overview

**Vision2030 (New Republic)** is a multilingual Java desktop application designed to simulate a unified smart booking platform for transportation and tourism services.
The system allows users to create accounts, securely log in, verify identity using QR codes, and book services such as **air taxis, monorail rides, and tourist attractions** within a single platform.

This project was developed as a **team-based academic project**, where I led a group of 4 members and oversaw system design, architecture decisions, and integration.

---

## 🚀 Features

* 🌍 **Multilingual Interface**
  Supports **Arabic, English, French, and Spanish**

* 👤 **User Authentication**

  * Secure login & signup system
  * Input validation and session handling

* 🪪 **QR-Code Identity System**

  * Generates unique QR identity for each user
  * Can be used for verification/check-in simulation

* 🎫 **Integrated Booking Modules**

  * Air Taxi booking
  * Monorail transportation booking
  * Tourist site reservations

* ☁️ **Cloud Database Integration**

  * Connected to **Oracle LiveSQL**
  * Stores user data, bookings, and system records

* 🧱 **Object-Oriented Design**

  * Modular architecture
  * Separation of responsibilities
  * Reusable classes and structured logic

* 👥 **Team Collaboration**

  * Led a 4-member development team
  * Coordinated feature distribution and integration

---

## 🛠️ Tech Stack

* **Language:** Java
* **Paradigm:** Object-Oriented Programming (OOP)
* **Database:** Oracle LiveSQL (cloud-based SQL database)
* **UI:** Java Desktop GUI (Swing)
* **QR Generation:** Java QR libraries (update with exact library if known)

---

## 📂 Project Structure (example)

```
Vision2030/
│
├── DigitalID/                # QR-based digital identity system
│
├── Egypt_2030_Text/          # Multilingual welcome screens (AR / EN / ES / FR) + assets
│
├── Home/                     # Main dashboard and navigation UI
│
├── Language_Settings/        # Language selection and localization logic
│
├── Login/                    # Authentication system (login, signup, Oracle DB connection)
│   └── Login_Application.java  # **Application entry point**
│
├── My_Tickets/               # Ticket display and management for all services
│
├── Tourist_Site_Booking/     # Tourist attraction booking module (e.g., GEM)
│
└── Transportation/           # Air Taxi & Monorail reservation modules
```



---

## ⚙️ Setup & Installation

1. Clone the repository:

```
git clone https://github.com/yourusername/vision2030-java.git
```

2. Open the project in your Java IDE (IntelliJ, Eclipse, or NetBeans)

3. Configure database connection:

   * Update database credentials in the configuration file
   * Ensure Oracle LiveSQL tables are created

4. Run the main class:

```
java Login.Login_Application
```

---
## 💾 Database Structure (Outline)

The Vision2030 booking system uses an Oracle LiveSQL database. The main tables and their purpose are:

### 1. Users
Stores user accounts and personal information.  
**Key columns:**  
`user_id`, `full_name`, `email`, `password`, `phone`, `nationality`, `national_id`, `city`, `dob`, `pref_lang`, `created_at`

### 2. GEM_Bookings
Stores tourist site bookings (e.g., Grand Egyptian Museum).  
**Key columns:**  
`booking_id`, `user_id`, `egyptian_adults`, `egyptian_children`, `egyptian_students`, `egyptian_seniors`, `foreign_adults`, `foreign_children`, `foreign_students`, `total_price`, `visit_time`, `created_at`

### 3. Transportation_Bookings
Stores Air Taxi and Monorail bookings.  
**Key columns:**  
`booking_id`, `user_id`, `origin`, `destination`, `departure_time`, `passenger_count`, `payment_method`, `total_price`, `booking_status`, `created_at`

---

## 🔐 Database Note

For security reasons:

* Database credentials in this repository are **placeholders**
* Replace them with your own Oracle LiveSQL credentials before running

Example:

```java
String DB_PASSWORD = "YOUR_PASSWORD_HERE";
```

---

## 🎓 Academic Context

This project was created as part of a university coursework assignment (Object-Oriented Programming) to demonstrate:

* Object-oriented system design
* Team-based software development
* Database-driven applications
* Multilingual UI implementation

---

## 📸 Demo

Watch a short demo of the Vision2030 booking system [here](https://youtu.be/a8uIC67_aJw).

- Shows login/signup flow  
- Multilingual interface switching  
- Booking for Air Taxi, Monorail, and tourist sites  
- QR-code identity system


---

## 📜 License / Usage Notice

This project is **for educational and portfolio purposes only**.  
All code and assets are **owned by the author (Mohamed Hazem Ahmed)** and are **not licensed for copying, reuse, or redistribution**.  

You may **view the project** on GitHub or use it for assessment purposes (e.g., recruiters), but **any duplication or commercial use is strictly prohibited**.

---

## ✍️ Author

**Mohamed Hazem Ahmed**
Computer Science Student at AAST

Linkedin: https://www.linkedin.com/in/mohamed-hazem-ahmed/
