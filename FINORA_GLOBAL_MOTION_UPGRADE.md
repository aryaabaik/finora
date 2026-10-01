# FINORA — GLOBAL MOTION & INTERACTION UPGRADE

## Goal

Upgrade the motion and interaction quality of the entire Finora frontend.

This is a FRONTEND-ONLY upgrade.

The application should feel noticeably more alive, polished, responsive, and intentional while keeping the existing Finora visual identity and all existing functionality.

The result must NOT look like generic AI/SaaS UI.

---

## NON-NEGOTIABLE RULES

DO NOT change:

- Java controllers
- Java services
- repositories
- entities/models
- database schema
- database data
- calculations
- authentication logic
- routes
- Thymeleaf form bindings
- form actions
- existing chart data
- existing business logic

Do not replace working functionality just to create animations.

Preserve the current Finora design language:

- cream / ivory
- warm beige / kraft
- ink navy
- muted gold
- sage / income green
- terracotta / expense red
- Cormorant Garamond for display typography
- Inter for UI
- subtle paper/grid texture
- restrained shadows and borders

DO NOT introduce:

- purple gradients
- neon colors
- glassmorphism
- excessive blur
- giant floating cards
- random blobs
- excessive pills
- excessive shadows
- rainbow gradients
- fake loading screens
- fake content
- unnecessary decorative illustrations
- particle effects
- generic SaaS animations

---

# PAGES TO COVER

Inspect the actual project first, then cover all relevant existing pages:

1. Landing / Welcome
2. Login
3. Register
4. Dashboard
5. Pemasukan
6. Pengeluaran
7. Kategori
8. Laporan
9. Template
10. Template Create
11. Template Edit
12. Pengaturan / Settings
13. Existing transaction forms
14. Desktop navigation
15. Mobile navigation
16. Existing modals/dialogs

Do not assume filenames. Inspect the repository.

---

# MOTION SYSTEM

Create one coherent motion system instead of many unrelated animations.

Use three levels.

### 1. Ambient motion

Very subtle:

- paper/grid movement where appropriate
- tiny decorative line movement
- chart drawing
- subtle accent movement

### 2. Interface motion

For:

- buttons
- navigation
- filters
- inputs
- dropdowns
- tabs
- modals
- template selection
- password toggles

### 3. Content entrance

For:

- page titles
- descriptions
- action buttons
- summary cards
- charts
- tables
- lists
- report sections

Motion must support hierarchy.

---

# PAGE ENTER ANIMATION

Implement reusable page-entry motion.

Recommended order:

- page shell
- title
- description
- actions
- summary
- filters
- main content

Suggested delays:

0ms
60ms
100ms
140ms
180ms
220ms
260–320ms

Use:

`cubic-bezier(0.16, 1, 0.3, 1)`

Preferred movement:

- opacity 0 → 1
- translateY(10–14px) → 0

Avoid dramatic zoom effects.

---

# SCROLL REVEAL

Implement an IntersectionObserver-based system.

Suggested classes:

- `.motion-reveal`
- `.motion-reveal-left`
- `.motion-reveal-right`
- `.motion-scale`
- `.motion-stagger`

Requirements:

- trigger once
- efficient on mobile
- no continuous scroll animation
- support dynamically inserted content where needed
- respect reduced motion

Suggested threshold:

0.10–0.18

Suggested duration:

500–700ms

Do not animate every DOM node individually.

---

# STAGGERED FINANCIAL LISTS

Apply to:

- income rows
- expense rows
- category lists
- template items
- report sections

Use approximately:

0ms
35ms
70ms
105ms

Cap the delay around 220ms.

For very long lists, animate only the first visible group.

---

# BUTTON INTERACTIONS

Buttons should feel physical.

Hover:

- translateY(-1px)
- subtle shadow increase

Press:

- translateY(1px)
- shadow reduction
- 100–140ms transition

Focus:

- clear accessible focus ring

Loading:

- small spinner
- preserve button dimensions
- do not prevent normal form submission
- do not use AJAX

Avoid bouncing buttons.

---

# NAVIGATION

Desktop sidebar:

- smooth active-state transition
- subtle icon movement
- restrained hover feedback

Mobile bottom navigation:

- active item slight lift
- subtle icon movement
- label fade/slide
- no excessive bounce

---

# INPUTS AND FORMS

For login, register, settings, income, expense, category and other forms:

On focus:

- smooth border transition
- subtle focus ring/shadow
- subtle icon transition
- maximum 1px movement

Filled:

- stable visual state

Error:

- short one-time shake
- keep error message readable

Password toggle:

- smooth eye/icon state transition
- preserve current password functionality

Do not change form bindings.

---

# MODALS

For template selection and existing dialogs:

Opening:

1. overlay fades in
2. modal moves upward slightly
3. opacity increases
4. content enters with short stagger

Closing:

- reverse quickly

Duration:

220–360ms

Do not use giant zoom animations.

---

# FILTERS / DROPDOWNS

For category, date, sort and template controls:

Use:

- opacity
- translateY(4–8px)
- scale 0.98 → 1

Keep it fast and subtle.

---

# FINANCIAL DATA

## Dashboard

Summary cards may use subtle number entrance/count-up ONLY if the existing values are already present.

Do not change calculations.

Charts should use the chart library's native animation where possible.

Do not rebuild charts unnecessarily.

## Pemasukan / Pengeluaran

Filtering must remain unchanged.

Use short content transitions after filtering.

Rows get subtle entrance and hover feedback.

## Laporan

Reveal summary, charts, analysis and breakdown sections progressively.

Do not animate every number separately.

## Template

Template cards:

- subtle hover lift
- item-list reveal
- action transition
- modal selection animation

Using a template must remain fast.

---

# AUTH PAGES

Login/Register already have their own motion system.

Preserve it.

Enhance only where useful:

- header entrance
- logo entrance
- curved header transition
- form stagger
- input focus animation
- password visibility animation
- button press/loading
- optional safe page transition between login/register

Never intercept authentication.

Never store credentials.

Never modify auth routes.

---

# PAGE TRANSITIONS

If safe with the existing Thymeleaf architecture:

- outgoing page fades slightly
- incoming page enters smoothly

Do NOT delay navigation unnecessarily.

Do NOT intercept normal form submissions.

Do NOT break Back/Forward navigation.

If unsafe, use page-entry animations only.

---

# JAVASCRIPT ARCHITECTURE

Prefer a centralized file:

`static/js/motion.js`

Responsibilities:

- page entrance
- IntersectionObserver reveals
- stagger handling
- button interaction
- navigation interaction
- modal hooks
- reduced-motion detection
- safe initialization

Avoid duplicating the same animation logic in every HTML file.

Use data attributes only where useful:

```html
data-motion="reveal"
data-motion-delay="80"
data-motion-group="summary"
```

Do not add attributes everywhere.

Extend existing JavaScript instead of replacing working interaction code.

---

# PERFORMANCE

Prefer:

- transform
- opacity
- IntersectionObserver
- CSS transitions
- requestAnimationFrame only when genuinely necessary

Avoid:

- continuous JavaScript loops
- constant layout calculations
- heavy animation libraries unless already installed
- expensive blur/filter animation
- permanent scroll listeners

Mobile scrolling must remain smooth.

---

# NO AI SLOP

The final result must NOT have:

- every card flying from a different direction
- random rotations
- excessive bounce
- huge scale effects
- glowing borders
- floating blobs
- animated gradients
- fake skeleton loaders
- unnecessary particles
- 1–2 second animations everywhere
- animation on every DOM element

The user should notice that Finora feels alive, not that everything is moving.

---

# RESPONSIVE TARGETS

Test:

320px
360px
375px
393px
414px
430px
768px
1024px
1280px
1440px
1920px

Mobile requirements:

- no horizontal overflow
- no clipped animation
- no layout jumps
- bottom navigation remains usable
- charts remain readable
- forms remain usable
- touch targets remain practical

Desktop requirements:

- preserve existing layout
- do not unnecessarily rearrange content

---

# FILE SAFETY

Before editing:

1. inspect the repository
2. identify all CSS files
3. identify all JavaScript files
4. identify existing animation systems
5. identify navigation scripts
6. identify modal implementations
7. identify chart libraries
8. identify existing page templates

Do not blindly overwrite files.

---

# QA

Verify:

- Login works
- Register works
- Password toggles work
- Navigation works
- Pemasukan works
- Pengeluaran works
- Kategori works
- Laporan works
- Template works
- Settings works
- Existing forms submit normally
- Existing filters work
- Existing charts use the same data
- No console errors
- No broken Thymeleaf expressions
- No duplicate event listeners
- No backend changes
- No database changes
- No route changes
- No horizontal overflow
- Reduced-motion works

---

# DEFINITION OF DONE

Every major page has intentional entrance motion.

Interactive controls have polished micro-interactions.

Lists and dashboard sections reveal smoothly.

Charts animate naturally.

Modals open and close smoothly.

Mobile navigation feels responsive.

Forms have clear focus, filled and submit states.

Login/Register remain functional.

Reduced-motion is respected.

No backend, data, route or form behavior has changed.

The result should feel like:

"Finora feels much more alive and polished."

Not:

"Everything is animated."
