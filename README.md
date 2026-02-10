# FUNSPLASH 📸

Simple Unsplash Photo Browser App - v1.0

FUNSPLASH adalah aplikasi Android berbasis Kotlin yang memungkinkan pengguna menjelajahi, melihat detail, menyimpan (bookmark), dan mengunduh foto dari layanan **Unsplash API**.
Aplikasi ini dirancang dengan pendekatan modern Android Development menggunakan arsitektur MVVM, serta berfokus pada tampilan yang clean, konsisten, dan mudah digunakan.

<p align="center">
    <img alt="Kotlin" src="https://img.shields.io/badge/-Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white"/>
    <img alt="Android" src="https://img.shields.io/badge/-Android-3DDC84?style=flat&logo=android&logoColor=white"/>
    <img alt="MVVM" src="https://img.shields.io/badge/-MVVM-4285F4?style=flat"/>
    <img alt="Room" src="https://img.shields.io/badge/-Room%20Database-6DB33F?style=flat"/>
</p>

<p align="center">
  <img src="./screenshots/home.png" width="250px" alt="Home"/>
  <img src="./screenshots/detail.png" width="250px" alt="Detail"/>
  <img src="./screenshots/bookmark.png" width="250px" alt="Bookmark"/>
</p>

---

## Table of Contents

* [Features](#features)
* [App Pages](#app-pages)
* [Technical Stack](#technical-stack)
* [Architecture](#architecture)
* [Data Storage](#data-storage)
* [Installation](#installation)
* [Creator](#creator)
* [License](#license)

---

## ✨ Features

* Browse photos from Unsplash API
* View photo details (author, likes, downloads, description)
* Bookmark / unbookmark photos
* Persistent bookmarks using Room Database
* Download photos to local storage
* Edit profile photo from device gallery
* Dark Mode support
* Clean UI with Material Design + Collapsing Toolbar

---

## 📱 App Pages

1. **Authentication**

   * Sign In
   * Sign Up

2. **Home**

   * List of photos from Unsplash API
   * Infinite scroll

3. **Detail Photo**

   * Hero image (collapsing effect)
   * Author, likes, downloads, description
   * Bookmark & Download button

4. **Bookmark**

   * List of saved photos
   * Click item → open detail photo

5. **Profile**

   * Display name & email
   * Change profile photo
   * Logout

---

## 🛠️ Technical Stack

* 💙 Kotlin (Native Android)
* 💚 Retrofit (REST API Client)
* 💛 Glide (Image Loader)
* 💜 Room Database (Local Storage)
* 🧡 ViewModel + LiveData
* 💙 RecyclerView
* 💜 Material Design Components

---

## 🧱 Architecture

FUNSPLASH menggunakan pola **MVVM (Model - View - ViewModel)**:

* **Model**
  Data classes, Room Entity, DAO, API response

* **View**
  Activity & Fragment

* **ViewModel**
  Mengelola state UI dan komunikasi ke Repository

* **Repository**
  Menghubungkan ViewModel dengan API & Database

Keuntungan MVVM:

* Kode lebih terstruktur
* Mudah dipelihara
* Memisahkan logika bisnis dari UI

---

## 💾 Data Storage

Aplikasi ini **tidak menggunakan server-side database**.

1. **SharedPreferences**

   * Menyimpan session login
   * Nama, email, dan foto profil

2. **Room Database**

   * Menyimpan data foto yang di-bookmark
   * Data tersimpan lokal di device

3. **API (Unsplash)**

   * Mengambil data foto secara online

---

## 🚀 Installation

1. Clone repository:

```bash
git clone https://github.com/Luficerr/funsplash.git
```

2. Buka project di Android Studio

3. Tambahkan API Key Unsplash di:

```kotlin
local.properties
UNSPLASH_ACCESS_KEY=your_api_key_here
```

4. Run aplikasi di emulator atau device

---

## 🎓 Purpose

Project ini dikembangkan sebagai:

* Media pembelajaran Android Native Kotlin
* Latihan implementasi MVVM & Room Database
* Pemenuhan tugas akhir mata kuliah
  **Bahasa Pemrograman 3 (BP3)**

---

## Creator

Lutfi Faiz Ziyadatullah ([Luficerr](https://github.com/Luficerr))

<p align="left">
  <a href="mailto:lutfifaizziyadatullah@gmail.com"> 
    <img alt="Connect via Email" src="https://img.shields.io/badge/Gmail-c14438?style=flat&logo=Gmail&logoColor=white" />
  </a>
</p>

If you like this project, please give it a ⭐

---

## License

Copyright © 2025-2026
Lutfi Faiz Ziyadatullah. All rights reserved.
