# Fix Redirect URI Mismatch - Google OAuth2 Register

## 🔍 Identifikasi Masalah

### Error yang Terjadi
```
Error 400: redirect_uri_mismatch
```

### Google Cloud Console Configuration
**Authorized redirect URIs yang sudah ada:**
- ✅ `http://localhost:8080/login/oauth2/code/google`

### Implementasi Aktual (SALAH)

**application.properties (SEBELUM):**
```properties
# Google Login - OK
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/login/oauth2/code/{registrationId}
→ Menghasilkan: http://localhost:8080/login/oauth2/code/google ✅

# Google Register - SALAH
spring.security.oauth2.client.registration.google-register.redirect-uri={baseUrl}/register/oauth2/code/{registrationId}
→ Menghasilkan: http://localhost:8080/register/oauth2/code/google-register ❌
```

**SecurityConfig.java:**
```java
.requestMatchers(
    "/oauth2/**", 
    "/login/oauth2/**", 
    "/register/oauth2/**"  // ← Path ini tidak perlu
).permitAll()
```

### Akar Masalah

1. **Redirect URI berbeda untuk register:**
   - Login: `http://localhost:8080/login/oauth2/code/google`
   - Register: `http://localhost:8080/register/oauth2/code/google-register`

2. **Google Cloud Console hanya punya:**
   - `http://localhost:8080/login/oauth2/code/google`

3. **Saat user klik "Daftar dengan Google":**
   - Spring mengirim redirect URI: `http://localhost:8080/register/oauth2/code/google-register`
   - Google menolak karena URI tidak ada di authorized list
   - Error: `redirect_uri_mismatch`

---

## ✅ Solusi yang Diterapkan

### Gunakan Callback Standar Spring Security

**Prinsip:**
- Semua OAuth2 callback menggunakan path standar: `/login/oauth2/code/{registrationId}`
- Spring Security membedakan flow berdasarkan `registrationId` di success handler
- Tidak perlu multiple callback URIs

**URI yang Dihasilkan (SETELAH):**
```
Login dengan Google:
  Button: /oauth2/authorization/google
  Callback: http://localhost:8080/login/oauth2/code/google
  ✅ Sudah ada di Google Cloud Console

Daftar dengan Google:
  Button: /oauth2/authorization/google-register
  Callback: http://localhost:8080/login/oauth2/code/google-register
  ✅ Menggunakan path standar /login/oauth2/code/
```

---

## 📁 File yang Diubah

### 1. application.properties

**Perubahan:** Hapus custom redirect-uri untuk kedua registration

```properties
# SEBELUM (SALAH):
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/login/oauth2/code/{registrationId}
spring.security.oauth2.client.registration.google-register.redirect-uri={baseUrl}/register/oauth2/code/{registrationId}

# SESUDAH (BENAR):
# Tidak ada explicit redirect-uri
# Spring Security menggunakan default: {baseUrl}/login/oauth2/code/{registrationId}
```

**Konfigurasi Lengkap (SESUDAH):**
```properties
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.scope=openid,profile,email

spring.security.oauth2.client.registration.google-register.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google-register.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google-register.scope=openid,profile,email
spring.security.oauth2.client.registration.google-register.provider=google
```

**Alasan:**
- Tanpa explicit `redirect-uri`, Spring Security menggunakan default
- Default: `{baseUrl}/login/oauth2/code/{registrationId}`
- Untuk `google`: `http://localhost:8080/login/oauth2/code/google`
- Untuk `google-register`: `http://localhost:8080/login/oauth2/code/google-register`

### 2. SecurityConfig.java

**Perubahan:** Hapus `/register/oauth2/**` dari permitAll (tidak digunakan)

```java
// SEBELUM:
.requestMatchers(
    "/", "/login", "/register",
    "/css/**", "/js/**", "/img/**",
    "/uploads/**", "/*.css", "/favicon.ico",
    "/oauth2/**", "/login/oauth2/**", "/register/oauth2/**"  // ← Tidak perlu
).permitAll()

// SESUDAH:
.requestMatchers(
    "/", "/login", "/register",
    "/css/**", "/js/**", "/img/**",
    "/uploads/**", "/*.css", "/favicon.ico",
    "/oauth2/**", "/login/oauth2/**"
).permitAll()
```

**Alasan:**
- Semua OAuth2 callback sekarang menggunakan `/login/oauth2/code/...`
- Path `/register/oauth2/**` tidak digunakan
- `/oauth2/**` mengizinkan authorization initiation (`/oauth2/authorization/...`)
- `/login/oauth2/**` mengizinkan callback (`/login/oauth2/code/...`)

---

## 🔧 Google Cloud Console Configuration

### URI yang Perlu Ditambahkan

**PENTING:** Tambahkan URI kedua di Google Cloud Console:

```
Authorized redirect URIs:
1. http://localhost:8080/login/oauth2/code/google           ✅ (sudah ada)
2. http://localhost:8080/login/oauth2/code/google-register  ⚠️ (TAMBAHKAN INI)
```

**Langkah:**
1. Buka [Google Cloud Console](https://console.cloud.google.com/)
2. Pilih project Finora
3. APIs & Services → Credentials
4. Klik OAuth 2.0 Client ID yang digunakan
5. Di "Authorized redirect URIs", klik **ADD URI**
6. Tambahkan: `http://localhost:8080/login/oauth2/code/google-register`
7. Klik **SAVE**

**Catatan:** Kedua URI menggunakan path yang sama (`/login/oauth2/code/`), hanya `{registrationId}` yang berbeda.

---

## 🔄 Flow OAuth2 Setelah Fix

### Flow 1: Login dengan Google

```
User → Klik "Masuk dengan Google"
  ↓
Browser redirect ke: /oauth2/authorization/google
  ↓
Spring Security redirect ke Google dengan:
  - client_id
  - redirect_uri=http://localhost:8080/login/oauth2/code/google
  ↓
Google menampilkan consent screen
  ↓
User pilih akun → Google callback ke:
  http://localhost:8080/login/oauth2/code/google?code=xxx&state=xxx
  ✅ URI cocok dengan Google Cloud Console
  ↓
Spring Security terima callback
  ↓
SecurityConfig oauth2Login handler:
  - Deteksi registrationId = "google"
  - Cari user berdasarkan email
  - Jika ada: login
  - Jika tidak: redirect /register dengan pre-fill
```

### Flow 2: Daftar dengan Google

```
User → Klik "Daftar dengan Google"
  ↓
Browser redirect ke: /oauth2/authorization/google-register
  ↓
Spring Security redirect ke Google dengan:
  - client_id (sama)
  - redirect_uri=http://localhost:8080/login/oauth2/code/google-register
  ↓
Google menampilkan consent screen
  ↓
User pilih akun → Google callback ke:
  http://localhost:8080/login/oauth2/code/google-register?code=xxx&state=xxx
  ✅ URI cocok dengan Google Cloud Console (setelah ditambahkan)
  ↓
Spring Security terima callback
  ↓
SecurityConfig oauth2Login handler:
  - Deteksi registrationId = "google-register"
  - Cari user berdasarkan email
  - Jika tidak ada: buat akun baru
  - Jika ada: login (tidak duplikat)
  - Redirect /dashboard
```

---

## 🧪 Testing

### Prerequisite

1. **Environment variables sudah diset:**
   ```powershell
   echo $env:GOOGLE_CLIENT_ID
   echo $env:GOOGLE_CLIENT_SECRET
   ```

2. **Google Cloud Console sudah update:**
   - ✅ `http://localhost:8080/login/oauth2/code/google`
   - ✅ `http://localhost:8080/login/oauth2/code/google-register`

### Test Case 1: Login dengan Google ✅

1. Buka `http://localhost:8080/login`
2. Klik "Masuk dengan Google"
3. **Redirect URL yang dikirim:**
   ```
   https://accounts.google.com/o/oauth2/v2/auth?
   response_type=code&
   client_id=xxx&
   redirect_uri=http://localhost:8080/login/oauth2/code/google&
   ...
   ```
4. Pilih akun Google
5. **Callback URL:**
   ```
   http://localhost:8080/login/oauth2/code/google?code=xxx&state=xxx
   ```
6. **Expected:** Login berhasil ATAU redirect ke register

### Test Case 2: Daftar dengan Google ✅

1. Buka `http://localhost:8080/register`
2. Klik "Daftar dengan Google"
3. **Redirect URL yang dikirim:**
   ```
   https://accounts.google.com/o/oauth2/v2/auth?
   response_type=code&
   client_id=xxx&
   redirect_uri=http://localhost:8080/login/oauth2/code/google-register&
   ...
   ```
4. Pilih akun Google
5. **Callback URL:**
   ```
   http://localhost:8080/login/oauth2/code/google-register?code=xxx&state=xxx
   ```
6. **Expected:** Akun dibuat ATAU login jika sudah ada

### Verifikasi di Browser Console

**Network Tab:**
1. Buka Developer Tools (F12)
2. Tab Network
3. Klik tombol Google
4. Lihat request ke `accounts.google.com`
5. Periksa query parameter `redirect_uri`

**Expected untuk Login:**
```
redirect_uri=http://localhost:8080/login/oauth2/code/google
```

**Expected untuk Register:**
```
redirect_uri=http://localhost:8080/login/oauth2/code/google-register
```

---

## 🐛 Troubleshooting

### Masih Error 400: redirect_uri_mismatch

**Check:**
1. ✅ URI di Google Cloud Console sudah ditambahkan?
2. ✅ URI exact match (tidak ada trailing slash)?
3. ✅ Protokol benar (http untuk localhost, https untuk production)?
4. ✅ Port benar (8080)?
5. ✅ Application sudah di-restart setelah perubahan config?

**Debug:**
```bash
# Cek URL yang dikirim di browser console Network tab
# Atau check di Spring Security debug log
```

### Error: No registrationId found

**Penyebab:** Spring tidak bisa detect registration dari callback

**Solusi:**
1. Pastikan `provider=google` ada di `google-register` configuration
2. Restart application

### Google menolak scope

**Penyebab:** Scope tidak sesuai dengan yang di-approve

**Solusi:**
1. Pastikan scope sama: `openid,profile,email`
2. Re-verify di Google Cloud Console

---

## 📊 Summary

### Masalah
- Register dengan Google menggunakan redirect URI berbeda: `/register/oauth2/code/google-register`
- Google Cloud Console hanya punya: `/login/oauth2/code/google`
- Mismatch → Error 400

### Solusi
- Gunakan callback standar Spring Security: `/login/oauth2/code/{registrationId}`
- Hapus custom `redirect-uri` dari application.properties
- Tambahkan URI kedua di Google Cloud Console

### File Changed
1. **application.properties** - Hapus custom redirect-uri
2. **SecurityConfig.java** - Hapus `/register/oauth2/**` dari permitAll

### Google Cloud Console Action Required
⚠️ **TAMBAHKAN URI:** `http://localhost:8080/login/oauth2/code/google-register`

### Result
- ✅ Login dengan Google: `http://localhost:8080/login/oauth2/code/google`
- ✅ Daftar dengan Google: `http://localhost:8080/login/oauth2/code/google-register`
- ✅ Kedua URI menggunakan path standar Spring Security
- ✅ Tidak ada lagi redirect_uri_mismatch

---

## ✅ Checklist

- [x] Hapus custom redirect-uri dari application.properties
- [x] Update SecurityConfig (hapus `/register/oauth2/**`)
- [ ] Build project (pending)
- [ ] Tambahkan URI di Google Cloud Console
- [ ] Test Login dengan Google
- [ ] Test Daftar dengan Google
- [ ] Verifikasi tidak ada redirect_uri_mismatch

---

**Status:** ✅ Code Fixed - Menunggu Google Cloud Console Update  
**Action Required:** Tambahkan `http://localhost:8080/login/oauth2/code/google-register` di Google Cloud Console  
**Tanggal:** 2026-10-15
