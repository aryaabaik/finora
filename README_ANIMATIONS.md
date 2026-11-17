# 🎨 Finora - Sistem Animasi Maksimal

## 🚀 Overview

Sistem animasi lengkap dan maksimal untuk aplikasi Finora yang membuat user experience jadi ga ngebosenin! Dengan 20+ jenis animasi, particle system, interactive effects, dan micro-interactions yang smooth.

---

## ✨ Fitur Utama

### 1. **Particle Background System**
- ✅ 30+ floating particles dengan gerakan organik
- ✅ Line connections antar partikel yang dekat
- ✅ Pulse effect pada setiap partikel
- ✅ Canvas-based untuk performance optimal

### 2. **Ripple Effects**
- ✅ Material design ripple pada klik
- ✅ Responsive ke posisi mouse
- ✅ Smooth fade out
- ✅ Auto-applied pada buttons & cards

### 3. **Number Counter Animations**
- ✅ Smooth counting animation
- ✅ Support prefix & suffix (Rp, %, dll)
- ✅ Trigger saat element visible
- ✅ Localized number formatting

### 4. **Parallax Effects**
- ✅ Depth illusion dengan parallax scroll
- ✅ Customizable speed
- ✅ Hardware accelerated
- ✅ Performance optimized

### 5. **Morphing Shapes**
- ✅ SVG shapes bergerak organik
- ✅ Continuous smooth animation
- ✅ Opacity variation

### 6. **Magnetic Hover**
- ✅ Element mengikuti mouse
- ✅ Smooth magnetic pull
- ✅ Customizable strength
- ✅ Auto-return to center

### 7. **Scroll Progress Indicator**
- ✅ Visual progress bar
- ✅ Auto-calculate percentage
- ✅ Gradient colors
- ✅ Smooth transitions

### 8. **Page Transitions**
- ✅ Smooth fade overlay
- ✅ Fade in on load
- ✅ Prevent jarring changes
- ✅ Auto-applied

### 9. **Toast Notifications**
- ✅ 4 types: success, error, warning, info
- ✅ Auto dismiss dengan timer
- ✅ Click to dismiss
- ✅ Stacked notifications
- ✅ Beautiful icons & colors

### 10. **Loading States**
- ✅ Fullscreen loading overlay
- ✅ Progress bar indicator
- ✅ Inline button loaders
- ✅ Skeleton loaders

### 11. **Confirmation Dialogs**
- ✅ Beautiful modal design
- ✅ Promise-based API
- ✅ Danger mode support
- ✅ Backdrop blur

### 12. **Micro-Interactions**
- ✅ Button press feedback
- ✅ Input focus rings
- ✅ Card shimmer on load
- ✅ Hover lift effects
- ✅ Form validation animations

### 13. **30+ CSS Animations**
- ✅ Fade (in, out, up, down)
- ✅ Slide (left, right)
- ✅ Scale & Zoom
- ✅ Bounce & Float
- ✅ Shake & Wiggle
- ✅ Glow & Shine
- ✅ Flip & Rotate
- ✅ Text shimmer
- ✅ Gradient animations

---

## 📦 File Structure

```
finora/
├── src/main/resources/
│   ├── static/
│   │   ├── css/
│   │   │   ├── animations.css           # Core animation utilities
│   │   │   └── micro-interactions.css   # Micro-interactions & smooth transitions
│   │   └── js/
│   │       ├── motion.js                # Enhanced motion system
│   │       ├── animations.js            # Advanced animations (particles, parallax, etc)
│   │       └── ui-system.js             # Toast, loading, dialogs
│   └── templates/
│       ├── dashboard.html               # Dashboard dengan semua animasi
│       ├── landing.html                 # Landing page enhanced
│       └── animation-demo.html          # Demo page untuk test animasi
└── ANIMATION_GUIDE.md                   # Dokumentasi lengkap
```

---

## 🎯 Quick Start

### 1. Include Files di HTML

```html
<!-- CSS -->
<link rel="stylesheet" href="/css/animations.css">
<link rel="stylesheet" href="/css/micro-interactions.css">

<!-- JavaScript -->
<script src="/js/motion.js"></script>
<script src="/js/animations.js"></script>
<script src="/js/ui-system.js"></script>
```

### 2. Basic Usage

#### Particle Background
```html
<div data-particles="30" data-particle-color="rgba(107, 140, 174, 0.1)">
    <!-- Your content -->
</div>
```

#### Ripple Effect
```html
<button data-ripple>Click Me</button>
```

#### Number Counter
```html
<span data-counter data-counter-prefix="Rp ">5000000</span>
```

#### Motion Entrance
```html
<div data-motion="enter" data-motion-delay="100">
    Appears on page load
</div>
```

#### Scroll Reveal
```html
<div data-motion="reveal">
    Appears when scrolled into view
</div>
```

#### Stagger Animation
```html
<ul data-motion-stagger>
    <li>Item 1</li>
    <li>Item 2</li>
    <li>Item 3</li>
</ul>
```

---

## 🎨 JavaScript API

### Toast Notifications

```javascript
// Success
FinoraUI.toast.success('Berhasil disimpan!', 3000);

// Error
FinoraUI.toast.error('Terjadi kesalahan!', 3000);

// Warning
FinoraUI.toast.warning('Peringatan!', 3000);

// Info
FinoraUI.toast.info('Informasi penting', 3000);
```

### Loading Overlay

```javascript
// Show
FinoraUI.loading.show('Loading...');

// Hide
FinoraUI.loading.hide();
```

### Progress Bar

```javascript
// Start
FinoraUI.progress.start();

// Update
FinoraUI.progress.update(50); // 50%

// Complete
FinoraUI.progress.complete();
```

### Confirmation Dialog

```javascript
const confirmed = await FinoraUI.confirm.show(
    'Yakin ingin menghapus?',
    {
        title: 'Konfirmasi',
        confirmText: 'Ya, Hapus',
        cancelText: 'Batal',
        type: 'danger' // or omit for normal
    }
);

if (confirmed) {
    // Do something
}
```

### Utility Functions

```javascript
// Confetti celebration
FinoraUI.confetti();

// Highlight element
FinoraUI.highlight(element);

// Shake animation
FinoraAnimations.shake(element);

// Bounce in animation
FinoraAnimations.bounceIn(element);
```

---

## 🎭 CSS Classes

### Pre-built Animations

```html
<!-- Fade -->
<div class="fade-in">Fade In</div>
<div class="fade-in-up">Fade In Up</div>
<div class="fade-in-down">Fade In Down</div>

<!-- Slide -->
<div class="slide-in-right">Slide from right</div>
<div class="slide-in-left">Slide from left</div>

<!-- Scale -->
<div class="scale-in">Scale In</div>
<div class="zoom-in">Zoom In</div>

<!-- Bounce -->
<div class="bounce">Bounce</div>
<div class="bounce-in">Bounce In</div>

<!-- Others -->
<div class="float">Float</div>
<div class="pulse">Pulse</div>
<div class="shake">Shake</div>
<div class="wiggle">Wiggle</div>
<div class="glow">Glow</div>
<div class="heartbeat">Heartbeat</div>
<div class="text-shimmer">Text Shimmer</div>
```

### Hover Effects

```html
<div class="hover-lift">Lifts on hover</div>
<div class="hover-scale">Scales on hover</div>
<div class="hover-glow">Glows on hover</div>
<div class="card-hover-effect">Combined effects</div>
```

---

## 🎬 Demo Page

Buka demo page untuk test semua animasi:

```
http://localhost:8080/animation-demo
```

Demo page menampilkan:
- ✅ Live particle background
- ✅ All toast notification types
- ✅ Loading states & progress
- ✅ Confirmation dialogs
- ✅ Ripple effects
- ✅ Number counters
- ✅ Magnetic hovers
- ✅ 30+ CSS animations
- ✅ Hover effects showcase
- ✅ Confetti & highlight utilities

---

## ⚙️ Configuration

### Customize Animation Speed

```javascript
// Motion system config
FinoraMotion.config.staggerDelay = 50; // ms between stagger items
FinoraMotion.config.scrollThreshold = 0.2; // reveal trigger point
```

### Customize Colors

Edit CSS variables:
```css
:root {
    --primary: #1A2332;
    --accent: #6B8CAE;
    --success: #6B7F59;
    --error: #A85D3F;
    --warning: #C4A35A;
}
```

---

## 🚀 Performance

### Optimizations Implemented:
- ✅ `will-change` untuk transform & opacity
- ✅ Hardware acceleration dengan `translate3d`
- ✅ `requestAnimationFrame` untuk smooth 60fps
- ✅ Debounced scroll & resize handlers
- ✅ Lazy IntersectionObserver
- ✅ Canvas-based particles untuk efficiency
- ✅ CSS `contain` untuk layout optimization

### Best Practices:
- ✅ Remove `will-change` after animation
- ✅ Use transform instead of position
- ✅ Limit simultaneous animations
- ✅ Reduce motion for accessibility

---

## ♿ Accessibility

- ✅ `prefers-reduced-motion` support
- ✅ Keyboard navigation
- ✅ ARIA labels
- ✅ Focus indicators
- ✅ Screen reader friendly

```css
@media (prefers-reduced-motion: reduce) {
    /* All animations disabled */
}
```

---

## 📱 Responsive

Semua animasi responsive dan mobile-friendly:
- ✅ Touch feedback
- ✅ Adaptive timing
- ✅ Reduced particles on mobile
- ✅ Optimized for smaller screens

---

## 🎯 Usage Examples

### Dashboard dengan Animasi Penuh

```html
<!-- Scroll progress -->
<div data-scroll-progress></div>

<!-- Particle background -->
<div data-particles="25"></div>

<!-- Hero dengan parallax -->
<section data-motion="enter" data-morph>
    <div data-parallax="0.3">
        <h1 class="text-shimmer">Welcome</h1>
    </div>
</section>

<!-- Cards dengan hover & ripple -->
<div class="card hover-lift" data-ripple data-magnetic="0.2">
    <h3>Balance</h3>
    <p data-counter data-counter-prefix="Rp ">5000000</p>
</div>

<!-- Action buttons -->
<button class="btn" data-ripple data-magnetic="0.15" 
        onclick="FinoraUI.toast.success('Saved!')">
    Save
</button>
```

---

## 🐛 Troubleshooting

### Animasi tidak muncul?
1. Pastikan semua file JS & CSS sudah di-include
2. Cek console untuk error
3. Pastikan element ada di DOM saat init

### Performance lambat?
1. Kurangi jumlah particles
2. Disable parallax pada mobile
3. Limit simultaneous animations

### Reduced motion tidak work?
1. Cek browser support untuk `prefers-reduced-motion`
2. Test dengan OS settings

---

## 🎉 Summary

### Total Features:
- ✅ **20+ Animation Systems**
- ✅ **30+ CSS Animations**
- ✅ **Particle Background**
- ✅ **Toast Notifications**
- ✅ **Loading States**
- ✅ **Micro-interactions**
- ✅ **Performance Optimized**
- ✅ **Accessibility Ready**
- ✅ **Mobile Responsive**
- ✅ **Easy to Use**

---

## 📄 License

Finora Animation System - Created with ❤️ for Finora

---

## 🤝 Support

Untuk pertanyaan atau masalah:
1. Cek `ANIMATION_GUIDE.md` untuk dokumentasi lengkap
2. Test di `/animation-demo` page
3. Inspect console untuk error messages

---

**Selamat menggunakan Finora Animation System! 🎨✨**

*Bikin aplikasimu jadi lebih hidup dan ga ngebosenin!*
