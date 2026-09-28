# Finora Mobile Navigation --- Design Specification & OpenCode Prompt

## Tujuan

Membuat navigasi **mobile-friendly** untuk Finora berdasarkan referensi
UI yang diberikan.

Referensi memiliki konsep: - Bottom navigation / floating navigation di
bagian bawah layar. - Container putih dengan sudut rounded besar. -
Active navigation berbentuk pill gelap. - Icon outline sederhana. -
Label hanya muncul pada item yang sedang aktif. - Tampilan minimal,
clean, dan modern.

Untuk Finora, konsep tersebut harus disesuaikan dengan identitas website
yang sudah ada, bukan disalin mentah dari referensi.

------------------------------------------------------------------------

## Arah Visual Finora

Finora menggunakan karakter visual:

-   Calm
-   Minimal
-   Premium
-   Personal finance
-   Editorial
-   Warm neutral
-   Tidak terlalu ramai
-   Tidak terlihat seperti template admin dashboard

Gunakan warna dan typography yang **sudah ada di project** sebagai
sumber utama.

Jangan membuat palette baru jika tidak diperlukan.

### Karakter navigasi

Navigasi mobile harus terasa seperti bagian asli dari Finora:

-   Background navigation: warm white / off-white sesuai UI existing.
-   Active item: warna gelap yang sudah menjadi warna utama Finora.
-   Text active: putih atau warna kontras yang sudah digunakan Finora.
-   Icon: outline, clean, dan konsisten.
-   Border/shadow: sangat subtle.
-   Rounded corner: cukup besar dan modern.
-   Hindari gradient mencolok.
-   Hindari warna biru/ungu generik.
-   Hindari glassmorphism berlebihan.
-   Hindari efek neon.
-   Hindari desain yang terlalu "AI generated".

------------------------------------------------------------------------

## Struktur Navigation Mobile

Gunakan navigation utama yang memang sudah tersedia di project.

Contoh konsep:

  Posisi   Fungsi
  -------- --------------------
  1        Dashboard / Home
  2        Pemasukan
  3        Pengeluaran
  4        Laporan
  5        Profile / Settings

**PENTING:** jangan mengarang route baru.

Sebelum implementasi, OpenCode harus membaca project dan menemukan
route/link navigation yang memang sudah digunakan.

Jika nama menu atau route berbeda, ikuti project yang sudah ada.

------------------------------------------------------------------------

## Perilaku Active State

Konsep active state mengikuti referensi:

``` text
[ icon + label ]   icon   icon   icon   icon
```

Contoh:

``` text
[ 🏠 Home ]   ↓   ↓   ↓   ↓
```

Saat halaman Pemasukan aktif:

``` text
   icon   [ icon + Pemasukan ]   icon   icon   icon
```

Jadi:

-   Item aktif mendapatkan pill background.
-   Label aktif ditampilkan.
-   Item tidak aktif hanya menampilkan icon.
-   Transisi active state halus.
-   Jangan membuat animasi berlebihan.

------------------------------------------------------------------------

## Posisi

Pada mobile:

-   Navigation berada fixed di bagian bawah viewport.
-   Tidak menutupi konten.
-   Berikan bottom spacing pada content jika diperlukan.
-   Perhatikan `env(safe-area-inset-bottom)` untuk perangkat dengan
    gesture navigation.
-   Navigation tidak boleh menyebabkan horizontal scrolling.

Pada desktop:

-   Jangan mengubah layout desktop yang sudah ada kecuali memang
    diperlukan.
-   Navigation mobile hanya muncul pada breakpoint mobile.

------------------------------------------------------------------------

## Responsive

Target:

-   Mobile kecil
-   Mobile normal
-   Mobile besar
-   Tablet portrait jika breakpoint project mendukung

Navigation harus tetap nyaman digunakan pada layar sempit.

Jangan membuat setiap item terlalu kecil.

Touch target setiap item minimal nyaman disentuh.

------------------------------------------------------------------------

## Animasi

Gunakan animasi ringan:

-   Active pill: `transform` / width transition yang halus.
-   Label: fade + slight slide/scale.
-   Hover hanya jika device mendukung hover.
-   Tap/click tidak boleh terasa lambat.

Hindari: - bouncing - excessive scaling - glowing - parallax - animasi
terus-menerus

------------------------------------------------------------------------

## Hal yang Tidak Boleh Rusak

Ini bagian paling penting.

Jangan mengubah:

-   Database
-   Entity / Model
-   Repository
-   Service
-   Controller
-   API endpoint
-   Session
-   Authentication
-   Login
-   Register
-   Data pemasukan
-   Data pengeluaran
-   Kategori
-   Laporan
-   Chart
-   Form submission
-   Fetch API
-   Thymeleaf variable
-   URL / route yang sudah ada

Jangan mengganti data hanya untuk menyesuaikan tampilan.

Jangan membuat dummy data.

Jangan menghapus fungsi existing.

Jangan membuat controller baru hanya untuk navigation.

------------------------------------------------------------------------

## Prinsip Anti-Slop AI

Implementasi harus terlihat seperti UI yang sengaja dirancang, bukan
hasil template AI.

Hindari:

-   terlalu banyak shadow
-   terlalu banyak border
-   gradient random
-   glassmorphism
-   neon
-   floating blobs
-   dekorasi tidak berguna
-   icon berbeda-beda style
-   terlalu banyak animasi
-   font baru tanpa alasan
-   warna random
-   CSS duplicate
-   class yang tidak digunakan
-   JavaScript yang sebenarnya tidak diperlukan

Prioritaskan:

**spacing + typography + hierarchy + consistency + usability.**

------------------------------------------------------------------------

# Prompt untuk OpenCode

Saya ingin kamu melakukan redesign **mobile navigation** pada aplikasi
Finora.

Saya sudah memberikan sebuah gambar referensi UI navigation mobile.

Gunakan gambar tersebut hanya sebagai **referensi konsep**, bukan untuk
menyalin desain secara mentah.

Konsep yang saya inginkan:

-   bottom navigation mobile
-   container putih/off-white
-   rounded corners
-   active menu berbentuk pill
-   active menu menampilkan icon + label
-   inactive menu hanya menampilkan icon
-   icon menggunakan outline style
-   animasi active state yang halus
-   clean dan minimal

Namun hasil akhirnya WAJIB disesuaikan dengan tema Finora yang sudah
ada.

Finora adalah aplikasi personal finance dengan karakter: - calm -
clean - minimal - premium - warm neutral - elegant - tidak ramai

## WORKFLOW WAJIB

Sebelum mengubah kode, lakukan inspection terlebih dahulu.

1.  Cari semua file yang mengatur navigation/header/sidebar.
2.  Cari semua route yang digunakan navigation saat ini.
3.  Cari CSS global dan CSS halaman yang berkaitan.
4.  Cari struktur Thymeleaf yang digunakan.
5.  Identifikasi apakah navigation dipakai melalui fragment/component.
6.  Identifikasi breakpoint responsive yang sudah ada.
7.  Identifikasi warna, typography, spacing, border radius, dan shadow
    yang sudah digunakan Finora.
8.  Jangan langsung membuat CSS baru sebelum memahami style existing.

Setelah memahami project, baru lakukan implementasi.

## ATURAN UTAMA

Saya hanya ingin perubahan pada **tampilan mobile navigation**.

Desktop harus tetap seperti sekarang.

Jangan mengubah: - backend - controller - service - repository -
model/entity - database - API - authentication - session - data -
route - form - business logic

Jangan membuat dummy data.

Jangan membuat controller baru.

Jangan mengubah nama route.

Jangan menghapus fungsi yang sudah berjalan.

Jika navigation saat ini memiliki link yang benar, pertahankan link
tersebut.

## MOBILE NAVIGATION

Buat navigation fixed di bagian bawah layar mobile.

Konsep visual:

``` text
┌────────────────────────────────────────────┐
│                                            │
│       [  HOME  ]   Search   Bell   User   │
│                                            │
└────────────────────────────────────────────┘
```

Item aktif:

``` text
[ icon + label ]
```

Item tidak aktif:

``` text
icon
```

Saat user berpindah halaman, active state harus otomatis mengikuti
halaman/route yang sedang dibuka.

Jangan hardcode satu menu sebagai aktif untuk semua halaman.

## VISUAL

Gunakan warna existing Finora.

Jika project sudah memiliki: - CSS variables - color tokens - typography
variables - spacing variables

gunakan kembali.

Jangan membuat palette baru tanpa alasan.

Visual yang diinginkan:

-   warm white / cream
-   dark primary color Finora
-   muted brown bila memang sudah ada
-   subtle shadow
-   clean outline icons
-   rounded container
-   elegant spacing

Jangan gunakan: - gradient mencolok - biru/ungu generik - neon -
glassmorphism berlebihan - emoji - icon campuran dari berbagai style

Jika project sudah menggunakan icon library, gunakan library tersebut.
Jangan menambahkan dependency baru hanya untuk icon jika tidak
diperlukan.

## RESPONSIVE

Navigation hanya tampil pada mobile breakpoint.

Desktop navigation/layout existing jangan diubah.

Pastikan:

-   tidak menutupi tombol/form
-   tidak menyebabkan horizontal scroll
-   content memiliki ruang bawah yang cukup
-   mendukung safe area iPhone/gesture navigation
-   nyaman digunakan dengan satu tangan
-   touch target cukup besar

Gunakan:

``` css
padding-bottom: env(safe-area-inset-bottom);
```

jika sesuai dengan struktur existing.

## ANIMATION

Gunakan animasi yang subtle.

Active item:

-   smooth transition
-   slight scale/fade jika diperlukan
-   label muncul dengan natural
-   tidak bouncing

Gunakan `transform` dan `opacity` jika memungkinkan.

Jangan menggunakan animasi berlebihan.

## ANTI-SLOP

Saya sangat tidak ingin hasilnya terlihat seperti "AI generated UI".

Jangan: - membuat 20 class baru tanpa kebutuhan - menambahkan CSS
duplicate - menambahkan shadow di semua elemen - menggunakan gradient
hanya agar terlihat modern - membuat blur/glass effect berlebihan -
menambahkan dekorasi yang tidak berfungsi - mengganti font existing
secara sembarangan - mengubah layout desktop - membuat komponen baru
jika existing component bisa digunakan - menambahkan dependency hanya
untuk efek visual sederhana

Prioritaskan kualitas detail kecil:

-   alignment
-   spacing
-   icon size
-   text size
-   active state
-   border radius
-   shadow
-   safe area
-   responsive behavior

## IMPLEMENTATION

Gunakan struktur project yang sudah ada.

Jika navigation berada di Thymeleaf fragment, modifikasi fragment
tersebut.

Jika CSS navigation sudah ada, upgrade CSS tersebut daripada membuat CSS
duplicate.

Jika JavaScript existing sudah menangani navigation, pertahankan dan
modifikasi seminimal mungkin.

Setelah implementasi:

1.  Periksa semua route navigation.
2.  Pastikan setiap tombol tetap menuju halaman yang benar.
3.  Pastikan active state berubah sesuai halaman.
4.  Pastikan desktop tidak berubah.
5.  Pastikan mobile tidak horizontal overflow.
6.  Pastikan content tidak tertutup navigation.
7.  Pastikan tidak ada JavaScript error.
8.  Pastikan tidak ada Thymeleaf error.
9.  Pastikan tidak ada perubahan pada data atau backend.

## HASIL YANG DIHARAPKAN

Hasil akhir harus terasa seperti:

"Finora mobile app"

bukan:

"template dashboard AI"

Gunakan referensi gambar sebagai inspirasi struktur navigation, tetapi
visual akhirnya harus benar-benar mengikuti design language Finora yang
sudah ada.

Jangan melakukan redesign halaman lain.

Fokus hanya pada mobile navigation dan responsive behavior.
