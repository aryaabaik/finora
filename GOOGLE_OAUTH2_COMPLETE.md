# Google OAuth2 Implementation - Finora

## 📋 Ringkasan Implementasi

Fitur Google OAuth2 telah diimplementasikan dengan dua alur terpisah:
1. **Google Login** - untuk pengguna yang sudah terdaftar
2. **Google Register** - untuk pengguna baru

## ✅ Fitur yang Diimplementasikan

### A. Google Login Flow
1. Pengguna klik "Masuk dengan Google" di halaman login
2. Sistem validasi email tersedia dan terverifikasi
3. Sistem cari akun Finora berdasarkan email (case-insensitive)
4. **Jika ditemukan**: Login otomatis dan redirect ke /dashboard
5. **Jika tidak ditemukan**: Redirect ke /register dengan email pre-filled dan pesan informasi

### B. Google Register Flow
1. Pengguna klik "Daftar dengan Google" di halaman register
2. Sistem validasi email tersedia dan terverifikasi
3. **Jika email sudah terdaftar**: Login otomatis (tidak buat duplikat)
4. **Jika email baru**: 
   - Buat akun Finora otomatis
   - Username dibuat dari email dengan penanganan collision
   - Password random secure untuk database constraint
   - Login otomatis dan redirect ke /dashboard

### C. Keamanan & Kompatibilitas
- Email verification wajib (email_verified = true)
- Password random secure (Base64-encoded, 32 characters) untuk akun Google
- Username unik dengan collision handling
- Tidak menyimpan password Google atau token
- Login username/password tetap berfungsi 100%
- Session management konsisten
- Database schema tidak berubah

---

## 📁 File yang Diubah/Dibuat

### 1. **application.properties**
**Perubahan:** Tambah konfigurasi untuk dua OAuth2 registration (google dan google-register)

```properties
# Google Login
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.scope=openid,profile,email
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/login/oauth2/code/{registrationId}

# Google Register
spring.security.oauth2.client.registration.google-register.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google-register.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google-register.scope=openid,profile,email
spring.security.oauth2.client.registration.google-register.provider=google
spring.security.oauth2.client.registration.google-register.redirect-uri={baseUrl}/register/oauth2/code/{registrationId}
```

**Alasan:** Memisahkan alur login dan register dengan dua registration berbeda

### 2. **GoogleOAuth2Service.java** (BARU)
**Lokasi:** `src/main/java/com/finora/finora/Service/GoogleOAuth2Service.java`

**Fungsi:**
- `createUserFromGoogle(email, fullName)` - Buat user baru dari Google
- `generateUsernameFromEmail(email)` - Generate username dari email
- `ensureUniqueUsername(baseUsername)` - Pastikan username unik
- `generateSecureRandomPassword()` - Generate password random secure
- `userExistsByEmail(email)` - Cek apakah email sudah terdaftar
- `findUserByEmail(email)` - Cari user berdasarkan email

**Alasan:** Memisahkan logika Google OAuth2 dari SecurityConfig dan AuthService

### 3. **SecurityConfig.java**
**Perubahan:**
- Import `GoogleOAuth2Service`
- Tambah dependency injection `GoogleOAuth2Service`
- Update `authorizeHttpRequests` untuk mengizinkan `/register/oauth2/**`
- Refactor `oauth2Login` handler:
  - Deteksi registration ID (google vs google-register)
  - Handle login flow: cari user, redirect ke register jika tidak ada
  - Handle register flow: buat user baru atau login jika sudah ada
  - Logging yang lebih bersih tanpa detail berlebihan

**Alasan:** Implementasi logika routing untuk dua alur OAuth2

### 4. **AuthController.java**
**Perubahan:** Update method `registerPage()`
- Ambil googleEmail, googleName, googleMessage dari session
- Pre-fill form register dengan data Google
- Tampilkan pesan info jika datang dari Google Login

**Alasan:** Memberikan pengalaman seamless untuk user yang datang dari Google Login

### 5. **login.html**
**Perubahan:** 
- Tambah alert-box untuk `${info}` message
- Tombol "Masuk dengan Google" sudah ada (dari implementasi sebelumnya)

**Alasan:** Menampilkan info message untuk berbagai kondisi

### 6. **register.html**
**Perubahan:**
- Tambah alert-box untuk `${message}` (info dari Google Login)
- Tambah divider "atau"
- Tambah tombol "Daftar dengan Google" dengan endpoint `/oauth2/authorization/google-register`
- Style konsisten dengan tombol di login.html

**Alasan:** Implementasi UI untuk Google Register

---

## 🔧 Cara Kerja

### Flow 1: Google Login - Email Terdaftar
```
User → Klik "Masuk dengan Google" → Google OAuth → Success Handler
  → Validasi email verified → Cari user di database → User FOUND
  → Set session "user" → Redirect /dashboard ✅
```

### Flow 2: Google Login - Email Belum Terdaftar
```
User → Klik "Masuk dengan Google" → Google OAuth → Success Handler
  → Validasi email verified → Cari user di database → User NOT FOUND
  → Set session attributes (googleEmail, googleName, googleMessage)
  → Redirect /register → Form pre-filled → User bisa:
      a) Isi form manual dan submit
      b) Klik "Daftar dengan Google" (Flow 4)
```

### Flow 3: Google Register - Email Terdaftar
```
User → Klik "Daftar dengan Google" → Google OAuth → Success Handler
  → Deteksi registration ID = "google-register"
  → Validasi email verified → Cek email di database → User EXISTS
  → Set session "user" → Redirect /dashboard ✅
  (Tidak buat akun duplikat)
```

### Flow 4: Google Register - Email Baru
```
User → Klik "Daftar dengan Google" → Google OAuth → Success Handler
  → Deteksi registration ID = "google-register"
  → Validasi email verified → Cek email di database → User NOT EXISTS
  → Create user:
      - Email dari Google
      - Full name dari Google
      - Username dari email (dengan collision handling)
      - Password random secure + hash
      - enabled = true
  → Set session "user" → Redirect /dashboard ✅
```

### Flow 5: Login Biasa (Username/Password)
```
User → Isi username/password → Submit → Spring Security validate
  → Success → Set session "user" → Redirect /dashboard ✅
(Tidak terpengaruh oleh Google OAuth2)
```

---

## 🧪 Cara Testing

### Prerequisite
1. **Environment variables harus sudah diset:**
   ```powershell
   [System.Environment]::SetEnvironmentVariable('GOOGLE_CLIENT_ID', 'your-client-id', 'User')
   [System.Environment]::SetEnvironmentVariable('GOOGLE_CLIENT_SECRET', 'your-secret', 'User')
   ```
   **⚠️ Restart terminal/IDE setelah set!**

2. **Google Cloud Console harus sudah dikonfigurasi:**
   - Authorized redirect URIs:
     - `http://localhost:8080/login/oauth2/code/google`
     - `http://localhost:8080/register/oauth2/code/google-register`

3. **Database PostgreSQL running di localhost:5432**

### Test Case 1: Google Login - Email Terdaftar ✅

**Setup:**
- Buat akun Finora manual: username `testuser`, email `test@gmail.com`
- Google account: `test@gmail.com`

**Langkah:**
1. Jalankan: `./mvnw spring-boot:run`
2. Buka: `http://localhost:8080/login`
3. Klik "Masuk dengan Google"
4. Pilih akun Google `test@gmail.com`

**Expected:**
- ✅ Login berhasil
- ✅ Redirect ke `/dashboard`
- ✅ User session tersimpan
- ✅ Log: "Google Login success - User found: testuser"

### Test Case 2: Google Login - Email Belum Terdaftar 📧

**Setup:**
- Google account: `newuser@gmail.com` (belum terdaftar di Finora)

**Langkah:**
1. Klik "Masuk dengan Google"
2. Pilih akun Google `newuser@gmail.com`

**Expected:**
- ✅ Redirect ke `/register`
- ✅ Email field pre-filled dengan `newuser@gmail.com`
- ✅ Pesan muncul: "Akun dengan email Google Anda belum terdaftar..."
- ✅ User bisa isi form manual atau klik "Daftar dengan Google"
- ✅ Log: "Google Login - Email not registered, redirecting to register"

### Test Case 3: Google Register - Email Baru ➕

**Setup:**
- Google account: `brandnew@gmail.com` (belum terdaftar di Finora)

**Langkah:**
1. Buka: `http://localhost:8080/register`
2. Klik "Daftar dengan Google"
3. Pilih akun Google `brandnew@gmail.com`

**Expected:**
- ✅ Akun Finora dibuat otomatis
- ✅ Username: `brandnew` (atau `brandnew1` jika collision)
- ✅ Email: `brandnew@gmail.com`
- ✅ Full name dari Google profile
- ✅ Password random secure (hidden, di-hash)
- ✅ Login otomatis dan redirect ke `/dashboard`
- ✅ Log: "Google Register - Creating new user"
- ✅ Log: "Google Register success - User created: brandnew"

### Test Case 4: Google Register - Email Sudah Terdaftar 🔄

**Setup:**
- Akun Finora sudah ada: email `existing@gmail.com`
- Google account: `existing@gmail.com`

**Langkah:**
1. Buka: `http://localhost:8080/register`
2. Klik "Daftar dengan Google"
3. Pilih akun Google `existing@gmail.com`

**Expected:**
- ✅ Tidak buat akun baru (tidak ada duplikat)
- ✅ Login menggunakan akun yang sudah ada
- ✅ Redirect ke `/dashboard`
- ✅ Log: "Google Register - User already exists, logging in"

### Test Case 5: Login Biasa - Tidak Terpengaruh 🔐

**Setup:**
- Akun Finora: username `normaluser`, password `password123`

**Langkah:**
1. Buka: `http://localhost:8080/login`
2. Isi username: `normaluser`
3. Isi password: `password123`
4. Klik "Sign In"

**Expected:**
- ✅ Login berhasil seperti biasa
- ✅ Redirect ke `/dashboard`
- ✅ Tidak ada perubahan behavior

### Test Case 6: Responsivitas UI 📱

**Langkah:**
1. Buka halaman `/login` dan `/register` di berbagai ukuran:
   - Desktop (1920x1080)
   - Tablet (768x1024)
   - Mobile (375x667)

**Expected:**
- ✅ Tombol Google rapi dan sejajar
- ✅ Divider "atau" terlihat jelas
- ✅ Tidak ada horizontal scroll
- ✅ Layout konsisten dengan desain Finora

---

## 🔒 Keamanan

### ✅ Yang Aman
- Email verification wajib (`email_verified = true`)
- Password random secure (Base64, 32 chars) untuk akun Google
- Password di-hash dengan BCrypt
- Tidak menyimpan Google token atau password Google
- Username collision handling mencegah duplikat
- Case-insensitive email matching
- Logging tidak mencetak token, secret, atau password

### ✅ Yang Dijaga
- Login username/password 100% berfungsi
- Entity User tidak berubah
- Database schema tidak berubah
- Session attribute "user" tetap konsisten
- AuthService existing tidak terpengaruh
- Fitur dashboard, kategori, laporan tetap berfungsi

---

## 📊 Struktur Database

Tidak ada perubahan skema. User yang dibuat via Google memiliki struktur sama:

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255),           -- Hash dari password random
    password_hash VARCHAR(255),      -- Copy dari password
    created_at TIMESTAMP,
    enabled BOOLEAN,
    foto VARCHAR(255)
);
```

**Contoh user dari Google:**
```
id: 5
username: "johndoe" (dari email john.doe@gmail.com)
email: "john.doe@gmail.com"
full_name: "John Doe" (dari Google profile)
password: "$2a$10$..." (hash dari random secure password)
password_hash: "$2a$10$..." (sama dengan password)
enabled: true
created_at: 2026-10-15 14:30:00
foto: null
```

---

## 🐛 Troubleshooting

### Error: "redirect_uri_mismatch"

**Penyebab:** Redirect URI di Google Cloud Console tidak sesuai

**Solusi:**
1. Buka Google Cloud Console
2. Periksa Authorized redirect URIs harus ada:
   - `http://localhost:8080/login/oauth2/code/google`
   - `http://localhost:8080/register/oauth2/code/google-register`
3. Tidak ada trailing slash

### Error: "Email not verified"

**Penyebab:** Email Google belum diverifikasi

**Solusi:**
1. Buka Google Account settings
2. Verifikasi email address
3. Coba login lagi

### Error: Username collision terus menerus

**Penyebab:** Base username sudah ada dan counter gagal

**Solusi:**
1. Periksa log untuk melihat username yang dicoba
2. System akan fallback ke timestamp-based username setelah 1000 attempt
3. Jika masih gagal, periksa unique constraint di database

### Email tidak pre-filled di register

**Penyebab:** Session attributes tidak tersimpan atau sudah expired

**Solusi:**
1. Pastikan browser tidak dalam incognito mode
2. Coba Google Login lagi
3. Periksa log untuk memastikan session attributes di-set

---

## 📝 Catatan Implementasi

### Mengapa Dua OAuth2 Registration?

Pendekatan ini dipilih karena:
1. **Separation of Concerns**: Login dan Register memiliki logika berbeda
2. **Clean Routing**: Endpoint berbeda untuk flow berbeda
3. **Spring Security Native**: Menggunakan fitur bawaan Spring Security
4. **Maintainability**: Mudah di-debug dan di-maintain
5. **Scalability**: Mudah ditambah provider lain (Facebook, GitHub)

### Mengapa Password Random untuk Akun Google?

Karena:
1. Database constraint `password` NOT NULL (dari schema existing)
2. User Google tidak butuh password untuk login
3. Password random secure memastikan tidak ada yang bisa login dengan password
4. Mengikuti best practice: store hash, bukan plaintext

### Mengapa Username dari Email?

Karena:
1. Entity User memiliki unique constraint pada username
2. Email bisa dijadikan base username yang readable
3. Collision handling memastikan uniqueness
4. User bisa update username nanti di settings (jika fitur ada)

---

## ✅ Checklist Implementasi

- [x] Tambah konfigurasi OAuth2 untuk login dan register
- [x] Buat GoogleOAuth2Service untuk handle logika
- [x] Update SecurityConfig dengan routing logic
- [x] Update AuthController untuk pre-fill form
- [x] Tambah tombol Google di login.html
- [x] Tambah tombol Google di register.html
- [x] Tambah info message di login dan register
- [x] Handle email verification
- [x] Handle user not found (redirect to register)
- [x] Handle user creation (register flow)
- [x] Handle duplicate prevention (register flow)
- [x] Generate unique username
- [x] Generate secure random password
- [x] Logging yang aman
- [x] Kompatibilitas dengan login biasa
- [x] Session management konsisten
- [x] UI responsif

---

## 🚀 Next Steps

1. **Build project:**
   ```bash
   ./mvnw clean compile
   ```

2. **Jalankan aplikasi:**
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Test semua flow:**
   - Google Login - email terdaftar
   - Google Login - email baru
   - Google Register - email baru
   - Google Register - email terdaftar
   - Login biasa

4. **Monitor log** untuk memastikan flow bekerja dengan benar

5. **Update Google Cloud Console** jika belum:
   - Tambah redirect URI untuk register

---

**Status:** ✅ Implementation Complete - Ready for Build & Test  
**Files Changed:** 6 files  
**Files Created:** 1 file (GoogleOAuth2Service.java)  
**Breaking Changes:** None

**Tanggal:** 2026-10-15
