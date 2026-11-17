# Google Login Fix - Diagnosis dan Perbaikan

## 🔍 Diagnosis Masalah

### Gejala
- OAuth berhasil sampai pengguna memilih akun Google
- Setelah memilih akun, aplikasi redirect ke `/login?error=google`
- Pesan error: "Akun Google tidak terdaftar atau email tidak terverifikasi"
- Padahal email sudah terdaftar di database

### Akar Masalah yang Teridentifikasi

**Masalah utama ada di logika pencarian user di SecurityConfig.java:**

```java
// KODE LAMA (BERMASALAH):
User user = userRepository
    .findByUsernameOrEmailIgnoreCase(email)  // ← Masalah di sini
    .filter(u -> email.equalsIgnoreCase(u.getEmail()))
    .orElse(null);
```

**Analisis Masalah:**

1. **Method `findByUsernameOrEmailIgnoreCase(email)`** mencari user berdasarkan:
   - `username = email` **ATAU** 
   - `email = email`

2. **Skenario gagal:**
   - Jika ada user dengan `username = "test@gmail.com"` dan `email = "otheremail@gmail.com"`
   - Query menemukan user karena **username** cocok dengan email Google
   - Filter kemudian membuang user karena **email field** tidak cocok dengan email Google
   - Hasil: `user = null` → redirect ke error

3. **Kesimpulan:** 
   - Logika lama bisa salah mencocokkan username dengan email Google
   - Filter tambahan mencoba memperbaiki tapi terlambat - user sudah salah diambil
   - Perlu method pencarian yang **hanya memeriksa email field**

### Mengapa Ini Terjadi?

Method `findByUsernameOrEmailIgnoreCase()` dirancang untuk **login form** di mana user bisa memasukkan username ATAU email untuk login. Ini bagus untuk form login tapi **tidak tepat untuk OAuth2** karena:

- OAuth2 hanya memberikan **email** dari Google
- Kita harus mencari user **hanya berdasarkan email field**
- Tidak boleh mencampur pencarian username dan email

## ✅ Solusi yang Diimplementasikan

### 1. Tambah Method Baru di UserRepository.java

```java
@Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
Optional<User> findByEmailIgnoreCase(@Param("email") String email);
```

**Keunggulan:**
- Hanya mencari berdasarkan **email field** (bukan username)
- Case-insensitive untuk menangani variasi huruf besar/kecil
- Tidak ambigu - fokus pada email saja

### 2. Update SecurityConfig.java

**Perubahan:**
```java
// KODE BARU (PERBAIKAN):
User user = userRepository.findByEmailIgnoreCase(email).orElse(null);
```

**Perbaikan:**
- Menggunakan method pencarian email yang spesifik
- Tidak ada lagi pencampuran username dan email
- Logika lebih jelas dan mudah dipahami

### 3. Tambah Logging Diagnostik

Menambahkan logging yang aman (tanpa mencetak data sensitif):

```java
logger.info("Google OAuth2 login attempt - Attributes available: {}", googleUser.getAttributes().keySet());
logger.info("Google OAuth2 - Email extracted: {}, Email verified: {}", 
    email != null ? "present" : "null", emailVerified);
logger.info("Searching for user with email: {}", email);
logger.warn("Google OAuth2 failed - No user found with email: {}", email);
logger.info("Google OAuth2 success - User found: {}, Email: {}", 
    user.getUsername(), user.getEmail());
```

**Tujuan:**
- Membantu debugging di masa depan
- Tidak mencetak token, secret, atau password
- Hanya mencetak informasi non-sensitif (email, status)

## 📋 File yang Diubah

### 1. UserRepository.java
**Perubahan:** Menambahkan method `findByEmailIgnoreCase()`

**Alasan:** Perlu method pencarian yang hanya fokus pada email field

### 2. SecurityConfig.java
**Perubahan:** 
- Import Logger dan LoggerFactory
- Tambah logging diagnostik
- Ganti `findByUsernameOrEmailIgnoreCase()` dengan `findByEmailIgnoreCase()`

**Alasan:** Memperbaiki logika pencarian dan menambahkan observability

## 🧪 Cara Testing Perbaikan

### Prerequisite
1. Pastikan environment variables sudah diset:
   ```powershell
   echo $env:GOOGLE_CLIENT_ID
   echo $env:GOOGLE_CLIENT_SECRET
   ```

2. Pastikan ada user di database dengan email yang sama dengan Google account Anda

### Test Case 1: Email Sudah Terdaftar (Seharusnya Berhasil)

**Setup:**
- Akun Finora: `username = "testuser"`, `email = "test@gmail.com"`
- Google account: `test@gmail.com`

**Langkah:**
1. Jalankan aplikasi: `./mvnw spring-boot:run`
2. Buka `http://localhost:8080/login`
3. Klik "Masuk dengan Google"
4. Pilih akun Google dengan email `test@gmail.com`

**Expected Result:**
- ✅ Login berhasil
- ✅ Redirect ke `/dashboard`
- ✅ User session tersimpan
- ✅ Log menampilkan: "Google OAuth2 success - User found: testuser, Email: test@gmail.com"

### Test Case 2: Email Tidak Terdaftar (Seharusnya Gagal)

**Setup:**
- Google account: `notregistered@gmail.com` (tidak ada di database Finora)

**Langkah:**
1. Klik "Masuk dengan Google"
2. Pilih akun Google dengan email yang tidak terdaftar

**Expected Result:**
- ❌ Login gagal
- ❌ Redirect ke `/login?error=google`
- ❌ Pesan: "Akun Google tidak terdaftar atau email tidak terverifikasi."
- ❌ Log menampilkan: "Google OAuth2 failed - No user found with email: notregistered@gmail.com"

### Test Case 3: Edge Case - Username = Email (Tidak Boleh Bingung)

**Setup:**
- User A: `username = "john@example.com"`, `email = "john.real@gmail.com"`
- User B: `username = "testuser"`, `email = "test@gmail.com"`
- Google account: `test@gmail.com`

**Langkah:**
1. Klik "Masuk dengan Google"
2. Login dengan `test@gmail.com`

**Expected Result:**
- ✅ Login berhasil sebagai User B (bukan User A)
- ✅ User yang dipilih berdasarkan **email field**, bukan username
- ✅ Tidak ada kebingungan antara username dan email

## 🔍 Monitoring & Debugging

### Melihat Log Aplikasi

Saat menjalankan aplikasi, perhatikan log untuk melihat flow OAuth2:

```
INFO  c.f.f.Config.SecurityConfig - Google OAuth2 login attempt - Attributes available: [sub, email, email_verified, name, picture, ...]
INFO  c.f.f.Config.SecurityConfig - Google OAuth2 - Email extracted: present, Email verified: true
INFO  c.f.f.Config.SecurityConfig - Searching for user with email: test@gmail.com
INFO  c.f.f.Config.SecurityConfig - Google OAuth2 success - User found: testuser, Email: test@gmail.com
```

Jika gagal:
```
WARN  c.f.f.Config.SecurityConfig - Google OAuth2 failed - No user found with email: notregistered@gmail.com
```

### Troubleshooting Berdasarkan Log

**Log: "Email extracted: null"**
- **Masalah:** Google tidak mengirim email
- **Solusi:** Periksa scope OAuth2 di application.properties (harus ada `email`)

**Log: "Email verified: false"**
- **Masalah:** Email Google belum diverifikasi
- **Solusi:** User harus memverifikasi email di Google account settings

**Log: "No user found with email: xxx"**
- **Masalah:** Email tidak terdaftar di database Finora
- **Solusi:** User harus register terlebih dahulu melalui `/register` dengan email yang sama

## 🔒 Keamanan

### Apa yang Aman?
- ✅ Tidak mencetak token, secret, atau password ke log
- ✅ Email verification wajib (`email_verified = true`)
- ✅ Tidak auto-create akun untuk email baru
- ✅ Case-insensitive matching untuk menghindari bypass
- ✅ Pencarian email yang spesifik (tidak bisa confused dengan username)

### Apa yang Tetap Dijaga?
- ✅ Login username/password tidak terpengaruh
- ✅ Session management konsisten
- ✅ Database schema tidak berubah
- ✅ Fitur existing (dashboard, kategori, laporan) tetap berfungsi

## 📊 Perbandingan Before & After

### Before (Bermasalah)
```java
// Bisa salah mencocokkan username dengan email Google
User user = userRepository
    .findByUsernameOrEmailIgnoreCase(email)
    .filter(u -> email.equalsIgnoreCase(u.getEmail()))
    .orElse(null);
```

**Masalah:**
- Mencampur pencarian username dan email
- Filter terlambat - user sudah salah diambil
- Tidak ada logging untuk debugging

### After (Perbaikan)
```java
// Hanya mencari berdasarkan email field
User user = userRepository.findByEmailIgnoreCase(email).orElse(null);
```

**Keunggulan:**
- Pencarian spesifik hanya pada email
- Tidak ambigu
- Ada logging untuk monitoring

## 📈 Dampak Perubahan

### Yang Berubah
- ✅ Logika pencarian user untuk OAuth2 lebih spesifik
- ✅ Tambah method repository baru: `findByEmailIgnoreCase()`
- ✅ Tambah logging untuk observability

### Yang TIDAK Berubah
- ✅ Login username/password tetap menggunakan `findByUsernameOrEmailIgnoreCase()`
- ✅ Database schema tetap sama
- ✅ UI/UX tidak berubah
- ✅ Session management tidak berubah
- ✅ Endpoint OAuth2 tidak berubah

## ✅ Kesimpulan

**Masalah teridentifikasi:**
- Logika pencarian user menggunakan method yang dirancang untuk form login (username OR email)
- Tidak tepat untuk OAuth2 yang hanya memberikan email

**Perbaikan dilakukan:**
1. Tambah method repository khusus untuk pencarian email
2. Update OAuth2 success handler untuk menggunakan method yang tepat
3. Tambah logging diagnostik yang aman

**Status:**
- ✅ Build Success
- ✅ Logika perbaikan sudah diimplementasikan
- ⏳ Perlu testing dengan akun Google nyata
- ⏳ Monitor log saat testing untuk memastikan flow bekerja

**Next Steps:**
1. Jalankan aplikasi: `./mvnw spring-boot:run`
2. Test login dengan Google menggunakan email yang sudah terdaftar
3. Periksa log untuk memastikan flow benar
4. Jika masih ada masalah, log akan memberikan informasi yang jelas

---

**Tanggal Perbaikan:** 2026-10-15  
**Status:** ✅ Build Success - Ready for Testing  
**File Diubah:** 2 files (SecurityConfig.java, UserRepository.java)
