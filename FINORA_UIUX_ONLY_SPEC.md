# FINORA — UI/UX LOGIN & REGISTER ONLY

## ATURAN MUTLAK

Tugas ini **HANYA** merombak UI/UX halaman Login dan Register menjadi desain FINORA berdasarkan gambar referensi.

### JANGAN MENGUBAH APA PUN SELAIN UI/UX

Dilarang mengubah:
- database
- data user
- data transaksi
- data keuangan
- data existing lainnya
- database schema
- migration
- seeder
- model
- controller
- service
- repository
- API
- endpoint
- route dan route name
- authentication
- authorization
- middleware
- session
- validation logic
- password hashing
- login/register logic
- redirect logic
- `.env`
- database credentials
- business logic

**Database dan data tidak boleh disentuh.**

Dilarang:
- INSERT/UPDATE/DELETE/TRUNCATE data
- membuat data dummy
- membuat user dummy
- membuat database/tabel baru
- menjalankan migration/seeder
- mengganti nama kolom database
- mengganti field form existing
- membuat authentication palsu
- membuat endpoint baru hanya demi desain

---

## YANG BOLEH DIUBAH

Hanya bagian visual:
- Blade/HTML markup yang diperlukan untuk tampilan
- CSS
- Tailwind classes jika project memang menggunakannya
- typography
- warna
- spacing
- layout
- icon
- button styling
- input styling
- decorative elements
- responsive design
- visual error/focus state
- JavaScript ringan untuk UI, misalnya show/hide password
- asset illustration khusus halaman auth

Jika tidak diperlukan, jangan menyentuh file lain.

---

## PRESERVE FORM EXISTING

Semua konfigurasi form harus tetap sama:
- `action`
- `method`
- `name`
- `id` jika dipakai logic
- `value`
- `old()`
- `autocomplete`
- `required`
- CSRF
- validation/error handling
- route

Jika existing:
```blade
<form method="POST" action="{{ route('login') }}">
    @csrf
```
tetap pertahankan.

Jika existing menggunakan:
```blade
<input name="email">
```
tetap gunakan `name="email"`. Jangan menggantinya menjadi username hanya karena referensi memakai Username.

Field Register juga harus mengikuti field existing. Jangan menambah atau menghapus field database.

---

# INSPECTION WAJIB

Sebelum mengedit:
1. Cari halaman Login existing.
2. Cari halaman Register existing.
3. Cari route Login/Register untuk memahami form.
4. Cari CSS/Tailwind/Bootstrap yang digunakan.
5. Cari JS yang digunakan.
6. Cari asset illustration yang sudah tersedia.
7. Identifikasi field form existing.
8. Setelah itu baru ubah UI.

Backend boleh dibaca untuk memahami struktur, tetapi **tidak boleh diubah**.

---

# DESAIN FINORA

Target visual:
- premium
- elegant
- soft
- clean
- feminine
- warm
- editorial
- luxury personal finance
- anime illustration lembut

Brand:
**FINORA**

Tagline:
**PERSONAL FINANCE, SIMPLIFIED**

Palette:
- ivory/cream
- white
- dark navy
- champagne gold
- warm gray

Jangan gunakan neon, cyberpunk, gradient berlebihan, atau tampilan admin dashboard generik.

---

# LOGIN DESKTOP

Gunakan split-screen sekitar 45–50% kiri dan 50–55% kanan.

### Panel kiri
- warm ivory/cream background
- circular anime illustration
- thin gold circle
- botanical gold ornament
- small stars/sparkles
- FINORA wordmark
- PERSONAL FINANCE, SIMPLIFIED

Target illustration:
- anime girl
- silver/white long hair
- soft blue eyes
- white blouse
- champagne-gold ribbon
- white coffee cup
- botanical decoration
- soft line art
- ivory/gold palette

Jika asset sudah ada, gunakan asset existing. Jika belum ada, buat container yang mudah diganti tanpa mengubah backend.

### Panel kanan
Heading:
**Welcome back,**

Subheading:
**Sign in to continue managing your finances.**

Gunakan serif elegant untuk heading dan sans-serif untuk body.

Form width sekitar 400–500px.

Input:
- tinggi 58–64px
- rounded 10–14px
- white/near-white
- thin warm-gray border
- subtle shadow
- line icon
- comfortable padding

Password:
- default `type="password"`
- eye toggle `type="button"`
- show/hide password hanya untuk UI
- jangan mengubah authentication

Primary button:
**Login**
- dark navy
- white text
- rounded
- tinggi sekitar 58–62px
- subtle hover/focus

Divider:
gold line + small decorative star/ornament.

Secondary button:
**Login with Bank**
- white
- warm border
- gold bank icon
- dark text

Jika fitur Login with Bank belum ada, **jangan membuat authentication/API palsu**. Pertahankan behavior existing.

Footer:
**Don't have an account? Register**

Gunakan route Register existing.

---

# REGISTER

Gunakan visual language yang sama.

Heading:
**Create your account**

Subheading:
**Start managing your finances with FINORA.**

Gunakan semua field yang memang dimiliki Register existing. Jangan mengubah struktur data.

Field umum jika memang existing:
- Name/Username
- Email
- Password
- Confirm Password

Setiap field:
- label
- icon
- placeholder
- focus state
- error state
- accessible label

Link:
**Already have an account? Login**

Gunakan route Login existing.

---

# VALIDATION

Validation Laravel existing wajib dipertahankan.

Jangan membuat validation backend baru.

Styling error boleh diubah:
- compact
- readable
- elegant
- tidak merusak layout

Pertahankan `@error`, old input, dan mekanisme error existing.

---

# RESPONSIVE

Desktop:
- split-screen

Tablet:
- illustration diperkecil
- spacing dikurangi

Mobile:
- jangan memaksa split 50/50
- illustration lebih kecil
- form full width
- margin sekitar 20–24px
- touch-friendly
- tidak boleh horizontal scrolling

Test minimal:
- 1440px
- 1280px
- 1024px
- 768px
- 480px
- 375px

---

# TYPOGRAPHY

Jika project sudah memakai Figtree, gunakan Figtree untuk body.

Heading/brand dapat menggunakan:
- Cormorant Garamond
- Playfair Display
- Libre Baskerville

Jangan menambah dependency yang tidak diperlukan.

---

# WARNA

```css
--finora-ivory: #F8F4EC;
--finora-cream: #FCFAF5;
--finora-white: #FFFFFF;
--finora-navy: #17345C;
--finora-navy-dark: #102A4A;
--finora-gold: #C9A45C;
--finora-gold-soft: #D8BC82;
--finora-gold-light: #E9D7AE;
--finora-text: #25303B;
--finora-muted: #77736C;
--finora-border: #DDD7CC;
--finora-error: #B45353;
```

---

# DECORATION & ANIMATION

Boleh:
- botanical branch
- thin gold circle
- stars
- tiny dots
- gold divider
- subtle fade/translate/hover/focus

Animasi sekitar 180–220ms dan hormati `prefers-reduced-motion`.

Jangan gunakan animasi berat atau dekorasi yang mengganggu form.

---

# ACCESSIBILITY

Pertahankan/tingkatkan:
- label
- keyboard navigation
- visible focus
- aria-label pada eye button
- semantic button/link
- contrast yang cukup
- touch target nyaman

---

# FILE YANG BOLEH DISENTUH

Utamakan:
```text
resources/views/
resources/css/
resources/js/
public/images/
public/assets/
```

Gunakan struktur project aktual.

Jangan mengubah backend hanya demi desain.

---

# DATABASE SAFETY

Jangan jalankan:
```bash
php artisan migrate
php artisan migrate:fresh
php artisan migrate:refresh
php artisan db:seed
php artisan migrate --seed
```

Jangan menjalankan query:
```sql
INSERT
UPDATE
DELETE
TRUNCATE
ALTER
DROP
CREATE
```

Database harus tetap persis seperti sebelum tugas dimulai.

Command read/build/test yang aman boleh digunakan jika relevan, misalnya:
```bash
php artisan route:list
php artisan view:clear
php artisan optimize:clear
npm run build
```

---

# TESTING

Login:
- halaman tampil
- field existing tetap ada
- action/method tetap
- CSRF tetap
- login valid tetap bekerja
- login invalid tetap menghasilkan error
- password toggle bekerja

Register:
- field existing tetap ada
- action/method tetap
- CSRF tetap
- validation tetap
- register valid tetap bekerja
- password confirmation tetap bekerja
- password toggle bekerja

Pastikan:
- DATA TIDAK BERUBAH
- DATABASE TIDAK BERUBAH
- BACKEND TIDAK BERUBAH
- AUTHENTICATION TIDAK BERUBAH

---

# PRIORITAS

Jika desain baru bertentangan dengan existing functionality/data:

**existing functionality dan data selalu menang.**

Cari solusi UI-only.

Urutan:
1. Preserve data
2. Preserve functionality
3. Preserve authentication
4. Preserve route/form structure
5. Redesign UI/UX
6. Match FINORA visual

---

# FINAL REPORT

Setelah selesai, laporkan:
```text
FILES CHANGED:
...

UI/UX CHANGES:
...

BACKEND:
Not modified.

DATABASE:
Not modified.

DATA:
Not modified.

AUTHENTICATION:
Preserved.

TESTS:
...
```

Jangan mengklaim sesuatu berhasil jika belum benar-benar dicek.

**Tugas ini 100% UI/UX Login dan Register.**
