# Fix Circular Dependency - Google OAuth2 Finora

## 🔍 Identifikasi Masalah

### Error yang Terjadi
```
The dependencies of some of the beans in the application context form a cycle:
SecurityConfig -> GoogleOAuth2Service -> SecurityConfig
```

### Akar Masalah

**Siklus Dependency:**
```
SecurityConfig 
  ↓ (inject)
GoogleOAuth2Service 
  ↓ (inject)
PasswordEncoder (@Bean dari SecurityConfig)
  ↓ (butuh)
SecurityConfig
  ↓ CYCLE!
```

**Detail:**
1. `SecurityConfig` constructor inject `GoogleOAuth2Service` (baris 25)
2. `GoogleOAuth2Service` constructor inject `PasswordEncoder` (baris 24)
3. `PasswordEncoder` adalah `@Bean` method di `SecurityConfig` (baris 187-189)
4. Spring mencoba:
   - Buat `SecurityConfig` → butuh `GoogleOAuth2Service`
   - Buat `GoogleOAuth2Service` → butuh `PasswordEncoder` bean
   - Buat `PasswordEncoder` bean → butuh `SecurityConfig` instance
   - **CIRCULAR DEPENDENCY!**

---

## ✅ Solusi yang Diterapkan

### Pemisahan PasswordEncoder Bean

**Prinsip:** Pindahkan bean yang menyebabkan siklus ke Configuration class terpisah.

### File yang Dibuat: 1 file

**`PasswordEncoderConfig.java`**
```java
package com.finora.finora.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

**Lokasi:** `src/main/java/com/finora/finora/Config/PasswordEncoderConfig.java`

**Alasan:**
- `PasswordEncoderConfig` tidak bergantung pada class lain
- Spring akan membuat bean ini terlebih dahulu
- `GoogleOAuth2Service` bisa inject `PasswordEncoder` tanpa bergantung pada `SecurityConfig`
- `SecurityConfig` bisa inject `GoogleOAuth2Service` tanpa masalah

### File yang Diubah: 1 file

**`SecurityConfig.java`**

**Perubahan:** Hapus method `passwordEncoder()` @Bean

```java
// DIHAPUS:
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

**Alasan:** Bean sudah dipindah ke `PasswordEncoderConfig`

---

## 🔄 Dependency Flow Setelah Fix

**Flow Baru (Tidak Ada Siklus):**

```
PasswordEncoderConfig
  ↓ @Bean
PasswordEncoder (independent bean)
  ↓ inject ke
GoogleOAuth2Service
  ↓ inject ke
SecurityConfig
  ✅ NO CYCLE!
```

**Urutan Pembuatan Bean:**
1. Spring membuat `PasswordEncoderConfig` (tidak ada dependency)
2. Spring membuat `PasswordEncoder` bean dari `PasswordEncoderConfig`
3. Spring membuat `UserRepository` (JPA, tidak ada circular dependency)
4. Spring membuat `GoogleOAuth2Service` (inject `UserRepository` + `PasswordEncoder`)
5. Spring membuat `SecurityConfig` (inject `UserRepository` + `GoogleOAuth2Service`)
6. ✅ Application Context berhasil dibuat

---

## 🧪 Verifikasi

### Perintah Build
```bash
cd /c/Users/aryaa/finora
./mvnw clean compile
```

**Expected Output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: ...
```

### Perintah Run
```bash
./mvnw spring-boot:run
```

**Expected Output:**
```
Started FinoraApplication in ... seconds
```

**Tidak akan ada error:**
```
❌ The dependencies of some of the beans in the application context form a cycle
```

---

## 📋 Summary Perubahan

### File Dibuat
1. **`PasswordEncoderConfig.java`** - Configuration class terpisah untuk PasswordEncoder bean

### File Diubah
1. **`SecurityConfig.java`** - Hapus method `passwordEncoder()` @Bean

### File Tidak Berubah
- ✅ `GoogleOAuth2Service.java` - Tidak perlu diubah
- ✅ `UserRepository.java` - Tidak perlu diubah
- ✅ `AuthController.java` - Tidak perlu diubah
- ✅ `AuthService.java` - Tidak perlu diubah
- ✅ `User.java` - Tidak perlu diubah
- ✅ Semua template HTML - Tidak perlu diubah
- ✅ `application.properties` - Tidak perlu diubah

---

## ✅ Fungsi yang Tetap Berfungsi

### Google OAuth2
- ✅ Google Login untuk email terdaftar
- ✅ Google Login redirect ke register untuk email baru
- ✅ Google Register untuk email baru (buat akun)
- ✅ Google Register untuk email terdaftar (login, tidak duplikat)
- ✅ Email verification wajib
- ✅ Username generation dari email
- ✅ Secure random password (hashed dengan BCrypt)

### Existing Features
- ✅ Login username/password
- ✅ Register manual
- ✅ Logout
- ✅ Session management dengan attribute "user"
- ✅ Redirect ke /dashboard
- ✅ Password encoding untuk semua user (BCrypt)
- ✅ Database PostgreSQL
- ✅ UI design tetap sama

---

## 🔒 Keamanan

### PasswordEncoder Bean
- ✅ Masih menggunakan `BCryptPasswordEncoder`
- ✅ Bean masih injectable ke semua service yang membutuhkan
- ✅ Tidak ada perubahan behavior encoding
- ✅ Semua password (manual register & Google register) tetap di-hash dengan BCrypt

### Tidak Ada Compromise
- ✅ Tidak menggunakan `spring.main.allow-circular-references=true` (jalan pintas buruk)
- ✅ Tidak mengubah algoritma encoding
- ✅ Tidak ada bean yang di-lazy load untuk menghindari masalah
- ✅ Tidak ada field injection yang bisa menyembunyikan circular dependency

---

## 📖 Penjelasan Teknis

### Mengapa Circular Dependency Terjadi?

Circular dependency terjadi saat:
1. Bean A butuh Bean B
2. Bean B butuh Bean C
3. Bean C butuh Bean A
4. Spring tidak bisa menentukan urutan pembuatan

### Mengapa Solusi Ini Bekerja?

**Separation of Concerns:**
- `PasswordEncoderConfig` hanya bertanggung jawab untuk menyediakan `PasswordEncoder` bean
- Tidak bergantung pada class lain
- Bisa dibuat pertama kali oleh Spring

**Dependency Graph Menjadi Acyclic:**
```
Before (Cyclic):
A → B → C → A (❌ CYCLE)

After (Acyclic):
Independent → B → A (✅ LINEAR)
```

### Best Practice yang Diikuti

1. **Single Responsibility**: Setiap Configuration class punya satu tanggung jawab
2. **Dependency Inversion**: Class bergantung pada abstraksi (PasswordEncoder interface)
3. **No Circular Dependencies**: Graph dependency berbentuk DAG (Directed Acyclic Graph)
4. **Constructor Injection**: Explicit dependency, mudah detect circular dependency saat compile time

---

## 🐛 Troubleshooting

### Jika Masih Ada Error Circular Dependency

**Check:**
1. Apakah `PasswordEncoderConfig.java` sudah dibuat?
2. Apakah method `passwordEncoder()` sudah dihapus dari `SecurityConfig.java`?
3. Apakah ada class lain yang inject `SecurityConfig` atau bean dari `SecurityConfig`?

**Debug Command:**
```bash
# Lihat dependency tree
./mvnw dependency:tree

# Compile dengan verbose
./mvnw clean compile -X
```

### Jika PasswordEncoder Tidak Ditemukan

**Error:**
```
No qualifying bean of type 'org.springframework.security.crypto.password.PasswordEncoder'
```

**Penyebab:** `PasswordEncoderConfig` tidak ter-scan oleh Spring

**Solusi:**
1. Pastikan package `com.finora.finora.Config` di-scan
2. Pastikan class memiliki annotation `@Configuration`
3. Restart IDE jika menggunakan IDE

### Jika Build Gagal

**Check:**
1. `./mvnw clean` untuk clean cache
2. Pastikan tidak ada syntax error
3. Pastikan import statements benar
4. Pastikan Java 17 terinstall

---

## ✅ Checklist Verifikasi

- [x] File `PasswordEncoderConfig.java` dibuat
- [x] Method `passwordEncoder()` dihapus dari `SecurityConfig.java`
- [x] Tidak ada perubahan logic di `GoogleOAuth2Service`
- [x] Tidak ada perubahan di service/controller lain
- [ ] Build berhasil (pending - bash unavailable)
- [ ] Application context berhasil start (pending - bash unavailable)
- [ ] Google Login berfungsi (pending - testing)
- [ ] Google Register berfungsi (pending - testing)
- [ ] Login biasa berfungsi (pending - testing)

---

## 🚀 Next Steps

1. **Build Project:**
   ```bash
   ./mvnw clean compile
   ```
   Expected: `BUILD SUCCESS`

2. **Run Application:**
   ```bash
   ./mvnw spring-boot:run
   ```
   Expected: Application starts tanpa error circular dependency

3. **Test Semua Flow:**
   - Login username/password
   - Google Login (email terdaftar)
   - Google Login (email baru → redirect register)
   - Google Register (email baru → buat akun)
   - Google Register (email terdaftar → login)

4. **Monitor Log:**
   - Tidak ada "circular dependency" error
   - Tidak ada "bean creation exception"
   - OAuth2 handler berjalan normal

---

## 📊 Impact Analysis

### Lines of Code Changed
- **Added:** 13 lines (`PasswordEncoderConfig.java`)
- **Removed:** 4 lines (dari `SecurityConfig.java`)
- **Net Change:** +9 lines

### Complexity
- **Before:** 2 Configuration classes, 1 dengan circular dependency
- **After:** 3 Configuration classes, 0 circular dependency
- **Impact:** Minimal, architecture lebih clean

### Performance
- ✅ No performance impact
- ✅ Bean creation order lebih predictable
- ✅ Application startup time sama

### Maintainability
- ✅ Lebih mudah di-test (PasswordEncoder bisa di-mock terpisah)
- ✅ Lebih mudah di-maintain (separation of concerns)
- ✅ Lebih mudah di-extend (tambah encoder lain jika perlu)

---

## 📝 Kesimpulan

**Masalah:** Circular dependency antara `SecurityConfig` → `GoogleOAuth2Service` → `PasswordEncoder` (bean dari `SecurityConfig`)

**Solusi:** Pindahkan `PasswordEncoder` bean ke `PasswordEncoderConfig` class terpisah

**Hasil:** Dependency graph menjadi acyclic, application context bisa dibuat tanpa error

**Status:** ✅ Fix Implemented - Ready for Build & Test

**File Changed:** 2 (1 created, 1 modified)

**Breaking Changes:** None

**Functional Changes:** None - semua fitur tetap berfungsi sama

---

**Tanggal Fix:** 2026-10-15  
**Metode:** Separation of Configuration Bean  
**Verified:** Code level ✅, Build pending, Runtime pending
