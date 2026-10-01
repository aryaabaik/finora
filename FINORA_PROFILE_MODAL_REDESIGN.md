# FINORA PROFILE MODAL REDESIGN

## 1. OBJECTIVE

Redesign the existing profile/avatar modal on the Finora dashboard.

The current modal looks too generic and contains unnecessary UI such as:

- "Personal Account" badge
- Green status dot
- Generic account/status styling
- Excessive empty space
- Basic static modal appearance

The new modal must feel like a carefully designed part of the Finora product.

The result should be:

- Elegant
- Minimal
- Premium
- Calm
- Editorial
- Consistent with the existing Finora dashboard
- Responsive
- Smoothly animated
- Clearly different from the current modal
- Not generic AI/SaaS UI
- Not over-designed
- Not childish
- Not flashy

IMPORTANT:

This is a VISUAL/UI upgrade only.

Do NOT modify backend functionality.

Do NOT modify authentication.

Do NOT modify database structure.

Do NOT modify user data.

Do NOT change existing routes.

Do NOT change how the user profile image is loaded.

Do NOT change the existing user name source.

Do NOT create fake profile data.

Do NOT add "Personal Account".

Do NOT add a green online/status dot.

Do NOT add unnecessary badges.

Do NOT add statistics that do not already exist.

---

# 2. FIRST STEP — INSPECT THE EXISTING IMPLEMENTATION

Before modifying anything:

1. Locate the current profile/avatar trigger.
2. Locate the current profile modal HTML.
3. Locate the CSS controlling the modal.
4. Locate the JavaScript controlling:
   - opening the modal
   - closing the modal
   - avatar interaction
   - overlay interaction
5. Identify where the following existing data comes from:
   - profile image
   - user's displayed name
6. Check whether the modal is shared across desktop and mobile.
7. Check existing Finora CSS variables.
8. Check existing animation utilities.
9. Check existing responsive rules.

Do NOT start rewriting files before understanding the current implementation.

Preserve the existing architecture whenever possible.

Prefer modifying the existing implementation instead of creating duplicate modal systems.

---

# 3. DESIGN DIRECTION

The new profile modal should feel like a small Finora identity card.

Visual reference:

- Warm ivory background
- Finora navy
- Muted gold
- Warm gray
- Charcoal
- Very subtle kraft/beige borders
- Editorial typography
- Clean spacing
- Strong visual hierarchy

Use the existing Finora design tokens instead of inventing a new color palette.

Expected visual character:

"quiet premium financial application"

NOT:

- SaaS dashboard template
- Glassmorphism
- Neon UI
- Purple gradient
- Cyberpunk
- Excessive shadows
- Excessive rounded cards
- Floating colorful badges
- AI-generated landing page style

---

# 4. NEW MODAL STRUCTURE

Use the existing profile image and existing user name.

Suggested structure:

------------------------------------------------

[ close button ]

        circular profile image

        thin decorative ring

        subtle gold accent

        USER NAME
        displayed using the existing user name value

------------------------------------------------

Do NOT show:

- Personal Account
- Online
- Active
- green dot
- status indicator
- fake role
- fake statistics
- fake account metadata

The modal should be intentionally simple.

The user's name should be the primary information.

---

# 5. MODAL VISUAL DESIGN

## Overlay

The page behind the modal should become visually secondary.

Use:

- rgba dark overlay
- subtle backdrop blur
- smooth opacity animation

Do not make the overlay completely black.

Recommended:

background:
rgba(26, 35, 50, 0.52)

backdrop-filter:
blur(5px)

The dashboard behind the modal should remain recognizable.

---

# 6. MODAL CONTAINER

Use an elegant compact container.

Desktop target:

width:
280px - 320px

Do not make it unnecessarily large.

Recommended:

border-radius:
22px

background:
var(--finora-ivory)

border:
1px solid rgba(217,197,165,0.55)

shadow:
0 24px 70px rgba(26,35,50,0.20)

The modal must not feel like a standard rectangular Bootstrap dialog.

---

# 7. PROFILE IMAGE

The existing user profile image MUST be preserved.

Do not replace it.

Do not crop the actual image content incorrectly.

Create a visually interesting frame around it.

Suggested structure:

outer decorative ring
→ thin muted gold
→ subtle navy accent
→ image

The image should remain circular.

Desktop:

width:
128px - 145px

height:
128px - 145px

The image should have:

border-radius: 50%;
object-fit: cover;

Add a very subtle shadow.

Avoid:

- green status dot
- excessive glow
- neon border
- rainbow border

---

# 8. PROFILE NAME

Use the existing user name.

Typography:

Cormorant Garamond / existing Finora display font

Font size:
24px - 28px

Font weight:
600

Color:
var(--finora-ink-navy)

Text alignment:
center

Do not replace the name with placeholder content.

Do not hardcode a user's name.

---

# 9. SMALL IDENTITY LABEL

Do NOT use "Personal Account".

If a small label is required for visual hierarchy, use a neutral Finora-style label such as:

PROFILE

or

ACCOUNT

However, prefer removing the label completely if the modal already looks balanced without it.

The user's name should be enough.

No status badge.

No green dot.

---

# 10. CLOSE BUTTON

The close button should become part of the visual design.

Position:

top-right

Shape:

circular

Size:
36px - 40px

Use a subtle ivory/kraft surface.

Default:

background:
rgba(232,221,208,0.55)

Hover:

background:
var(--finora-kraft)

The close icon should rotate slightly during hover.

Example interaction:

hover:
transform: rotate(90deg);

Transition:
250ms - 350ms

Do not make the animation excessive.

---

# 11. ANIMATION SYSTEM

The modal must NOT simply appear instantly.

The animation should be clearly visible but professional.

## Opening sequence

Sequence:

1. Overlay fades in
2. Modal moves upward and scales into place
3. Profile image scales slightly
4. Decorative ring rotates subtly
5. User name fades upward
6. Close button fades in

Suggested:

Overlay:

opacity:
0 → 1

duration:
280ms

Modal:

opacity:
0 → 1

transform:
translateY(18px) scale(0.94)

to:

translateY(0) scale(1)

duration:
420ms

easing:

cubic-bezier(0.16, 1, 0.3, 1)

Avatar:

opacity:
0 → 1

transform:
scale(0.82)

to:

scale(1)

duration:
500ms

Use a slight delay.

Name:

opacity:
0 → 1

transform:
translateY(8px)

to:

translateY(0)

duration:
350ms

Do NOT make the modal bounce excessively.

---

# 12. PROFILE IMAGE INTERACTION

When the modal is open:

The profile image can have a subtle interaction.

On hover:

- image scale: 1.025
- ring slightly rotates
- shadow becomes slightly stronger

Do not use:

- glowing neon
- huge zoom
- spinning avatar
- infinite animation

The interaction should feel premium.

---

# 13. JAVASCRIPT

Use JavaScript for the modal interaction.

Do not rely only on CSS.

Existing functionality must continue to work.

JavaScript should handle:

- open modal
- close modal
- close when clicking overlay
- close when pressing Escape
- prevent accidental interaction behind modal while open
- animation state classes
- focus handling where appropriate

IMPORTANT:

Do NOT intercept unrelated dashboard functionality.

Do NOT change navigation behavior.

Do NOT modify authentication.

Do NOT modify transaction logic.

Do NOT modify API calls.

Do NOT modify user data.

Do NOT reload the page just to open/close the modal.

---

# 14. ANIMATION STATE

Use state classes such as:

.is-opening
.is-open
.is-closing

or another clean architecture if the existing project already has one.

Avoid scattered inline styles.

Prefer CSS classes controlled by JavaScript.

---

# 15. ACCESSIBILITY

The modal must:

- have appropriate aria attributes
- have an accessible close button
- support Escape
- maintain visible focus
- not trap keyboard focus incorrectly
- respect prefers-reduced-motion

When:

@media (prefers-reduced-motion: reduce)

Disable or significantly reduce:

- modal movement
- avatar animation
- rotation
- staggered animation

The modal should still function normally.

---

# 16. MOBILE DESIGN

The mobile version is extremely important.

The modal must look intentional on:

- 320px
- 360px
- 375px
- 393px
- 414px
- 430px
- tablet
- desktop

Do NOT simply scale the desktop modal down.

Mobile target:

width:
calc(100vw - 40px)

max-width:
320px

Padding:
24px

Avatar:
110px - 120px

Name:
22px - 25px

Close button:
36px

The modal should remain vertically centered.

No horizontal overflow.

No clipping.

No content touching the viewport edges.

---

# 17. RESPONSIVE BEHAVIOR

Desktop:

- compact centered profile card
- larger avatar
- stronger visual spacing

Tablet:

- slightly smaller modal
- maintain proportions

Mobile:

- compact but comfortable
- touch-friendly close button
- no excessive empty space
- avatar remains the visual focal point

Do not introduce a bottom sheet unless the existing Finora design system strongly supports it.

Keep the modal centered.

---

# 18. FINORA DESIGN CONSISTENCY

Use existing Finora variables.

Expected colors:

--cream
--ivory
--off-white
--kraft
--kraft-dark
--warm-gray
--charcoal
--ink-navy
--ink-navy-light
--muted-gold

Do not introduce an unrelated palette.

The modal should look like it belongs to:

Dashboard
Pemasukan
Pengeluaran
Laporan
Template
Pengaturan

It must feel like one product.

---

# 19. DO NOT CHANGE

The following must remain unchanged unless absolutely required for the modal itself:

- User entity
- User repository
- Authentication controller
- Authentication service
- Dashboard controller
- Transaction controllers
- Transaction services
- Database schema
- User profile data
- Profile image path/source
- Existing dashboard data
- Existing navigation routes
- Existing transaction routes
- Existing forms
- Existing Thymeleaf data bindings

This task is a FRONTEND MODAL REDESIGN.

---

# 20. FILE STRATEGY

First identify the actual files.

Likely files may include:

- dashboard.html
- navigation.html
- dashboard.css
- responsive.css
- navigation.js

But DO NOT assume these are the correct files.

Inspect the repository first.

Only modify files that are actually responsible for the profile modal.

If a dedicated profile modal CSS/JS file already exists, prefer using it.

Do not create duplicate implementations.

---

# 21. CODE QUALITY

Requirements:

- semantic HTML
- clean CSS
- organized JavaScript
- no duplicated event listeners
- no duplicated CSS rules
- no inline JavaScript
- no unnecessary dependencies
- no unnecessary libraries
- no console errors
- no broken event handlers
- no memory leaks
- no animation loops running while modal is closed

Use existing project conventions.

---

# 22. QA

Before finishing, verify:

### Desktop

- modal opens
- modal closes
- overlay closes modal
- Escape closes modal
- close button works
- avatar displays correctly
- existing user name displays correctly
- no Personal Account badge
- no green status dot
- no fake information
- animation works
- no layout shift

### Mobile

Test:

320px
360px
375px
393px
414px
430px

Verify:

- no horizontal scroll
- modal stays centered
- avatar does not overflow
- name does not overflow
- close button remains reachable
- animation remains smooth

### Regression

Verify:

- dashboard still loads
- navigation still works
- profile image still works
- user data still appears correctly
- no backend files changed
- no routes changed
- no form bindings changed
- no console errors

---

# 23. FINAL DESIGN PRINCIPLE

The final result should communicate:

"Finora profile"

not:

"Generic SaaS account modal"

Keep it:

quiet,
clean,
intentional,
premium,
warm,
and consistent.

Every visual element must have a purpose.

If an element does not improve hierarchy, interaction, or aesthetics, remove it.