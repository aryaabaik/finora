You are redesigning the FINORA authentication UI.

IMPORTANT:
This is a VISUAL + INTERACTION redesign only.

DO NOT change authentication logic, backend logic, database logic, routes, form bindings, field names, Thymeleaf bindings, validation behavior, session behavior, or controller/service code.

The final result must feel like a carefully designed real product interface, NOT an AI-generated SaaS template.

REFERENCE:
The provided reference image shows a compact authentication UI:
- compact centered card
- dark patterned top/header section
- rounded lower white form section
- strong curved transition between dark header and white body
- minimal typography
- clean inputs
- black/dark primary button
- subtle shadows
- compact spacing
- polished mobile-first composition
- visual interaction when fields are focused
- smooth transitions and micro-interactions

FINORA must keep its own visual identity:
- cream/off-white background
- navy
- warm beige
- muted gold
- sage green for income-related accents
- terracotta for expense-related accents
- Cormorant Garamond / Georgia for display typography
- Inter/system sans-serif for UI text

==================================================
PHASE 1 — PLAN
==================================================

Before editing anything, inspect the existing authentication implementation.

Inspect:

1. src/main/resources/templates/auth/login.html
2. src/main/resources/templates/auth/register.html
3. src/main/resources/static/css/auth.css
4. src/main/resources/static/js/password-toggle.js
5. src/main/resources/templates/layout/auth.html
6. Existing Finora dashboard CSS only for COLOR/TYPOGRAPHY/spacing references.

DO NOT modify files during inspection.

Determine:

- Current DOM structure
- Current Thymeleaf bindings
- Current form field names and IDs
- Current password toggle implementation
- Existing animation code
- Existing CSS variables
- Existing responsive behavior
- Existing alert/error rendering
- Existing authentication routes

Then create a concise implementation plan before making changes.

==================================================
ABSOLUTE BACKEND SAFETY RULE
==================================================

NEVER modify:

- AuthController.java
- AuthService.java
- LandingController.java
- repositories
- entities
- database structure
- authentication logic
- password handling
- session handling
- routes

Do not "clean up" backend code.

This task is ONLY:

HTML structure
CSS
JavaScript interaction

==================================================
FORM CONTRACT — MUST NOT CHANGE
==================================================

LOGIN:

<form th:action="@{/login}" method="post" class="auth-form">

Username:

type="text"
id="username"
name="username"
required
autofocus

Password:

type="password"
id="password"
name="password"
required

Password toggle:

onclick="togglePassword(this)"

Submit:

type="submit"

Register:

<a th:href="@{/register}">Register</a>

Alerts must continue supporting:

th:if="${error}"
th:if="${message}"
th:if="${success}"

REGISTER:

<form th:action="@{/register}" th:object="${user}" method="post" class="auth-form">

Fields:

fullName:
type="text"
id="fullName"
th:field="*{fullName}"
required
autofocus

username:
type="text"
id="username"
th:field="*{username}"
required

email:
type="email"
id="email"
th:field="*{email}"
required

password:
type="password"
id="password"
th:field="*{passwordHash}"
required
minlength="6"

confirmPassword:

type="password"
id="confirmPassword"
name="confirmPassword"
required
minlength="6"

CRITICAL:

confirmPassword MUST NOT receive th:field.

Preserve:

name="confirmPassword"

exactly.

Both password fields must retain their toggle behavior.

Login link:

<a th:href="@{/login}">Login</a>

==================================================
PHASE 2 — BUILD
==================================================

Now implement the redesign.

Files allowed to modify:

src/main/resources/templates/auth/login.html
src/main/resources/templates/auth/register.html
src/main/resources/static/css/auth.css
src/main/resources/static/js/password-toggle.js

Do not modify anything else unless absolutely necessary for the authentication UI.

==================================================
DESIGN DIRECTION
==================================================

The reference composition should be followed more closely.

DO NOT create a generic rectangular centered form.

The card must visually have TWO CONNECTED AREAS:

TOP:
Dark Finora navy patterned header.

BOTTOM:
Warm ivory/white form body.

The transition between the two must have a REAL CURVED SHAPE.

Use something like:

border-radius:
16px 16px 0 0

and a curved lower edge created through pseudo-elements or an equivalent CSS technique.

The white form body should visually overlap the dark header slightly.

The transition must feel intentional.

Do NOT simply use:

border-radius: 12px;

on the whole card.

The curved transition is an important part of the reference.

==================================================
AUTH CARD
==================================================

Desktop:

width:
approximately 400–440px

Do not make the card unnecessarily huge.

Mobile:

width:
calc(100% - 28px)

maximum width:
440px

The card should remain visually compact.

Use:

background: var(--ivory)

border:
1px solid subtle beige

box-shadow:
soft, restrained shadow

border-radius:
18px

No glassmorphism.

No excessive blur.

No giant shadow.

No gradient.

==================================================
TOP HEADER
==================================================

Use:

background:
#1A2332

height:
approximately 145–170px desktop

mobile:
approximately 120–145px

Create a subtle geometric grid/pattern.

Do NOT use an external image.

Use CSS pseudo-elements or CSS gradients only for the pattern.

Pattern must be subtle.

Inside:

Finora logo
Finora wordmark

Center them visually.

The logo must use:

/img/finora-logo.svg

Do not replace the logo.

Add a very subtle muted-gold accent.

==================================================
CURVED HEADER TRANSITION
==================================================

This is REQUIRED.

The bottom edge of the navy header must curve downward into the white section.

The white form body should overlap the header slightly.

Use a pseudo-element or layered element.

The visual target is approximately:

        NAVY HEADER
   ┌────────────────────┐
   │       FINORA       │
   │                    │
   │                    │
   └──────╮      ╭──────┘
          ╰────╯
        WHITE BODY

The curve should be smooth, not exaggerated.

Do NOT create a huge wave.

Do NOT use an SVG dependency.

CSS is preferred.

==================================================
LOGIN CONTENT
==================================================

Inside the white body:

Small eyebrow:

SIGN IN

Main heading:

Welcome back

Supporting text:

Sign in to manage your finances.

Keep the typography compact.

Heading:

Cormorant Garamond
approximately 27–30px

Body:

Inter
approximately 13px

Center alignment.

Do not add unnecessary marketing text.

Do not add social login.

Do not add "Forgot password" unless it already exists.

Do not invent features.

==================================================
REGISTER CONTENT
==================================================

Same visual system.

Header:

small back link

Sign Up

Finora branding

Body:

Create your account

Set up your Finora account and start keeping your finances organized.

Divide the fields visually into:

PERSONAL

and

SECURITY

Do not alter any field.

Do not add fields.

Do not remove fields.

==================================================
INPUT INTERACTION — JAVASCRIPT REQUIRED
==================================================

Do NOT rely exclusively on CSS.

Implement real JavaScript interaction.

Create/enhance:

src/main/resources/static/js/password-toggle.js

The JavaScript must provide:

1. Focus state management

When an input receives focus:

- add a class such as .is-focused to its wrapper
- animate icon
- slightly change border
- slightly increase visual elevation
- animate label/color
- smoothly transition the state

When blurred:

- remove the focused class if empty
- preserve filled state if value exists

2. Filled state

If an input contains text:

wrapper receives:

.is-filled

This must remain after blur.

3. Password visibility

Password toggle must:

- switch password → text
- switch text → password
- update icon state
- animate icon subtly
- remain accessible
- NOT interfere with form submission

4. Submit interaction

When form submits:

- do NOT preventDefault()
- do NOT use AJAX
- do NOT modify the action
- do NOT intercept authentication

Only add a visual loading state:

button.classList.add('is-loading')

The browser must continue normal form submission.

5. Button micro-interaction

On pointer interaction:

- subtle press animation
- slight scale down
- release smoothly

6. Optional input ripple/focus glow

Keep it subtle.

No flashy effects.

==================================================
JAVASCRIPT ARCHITECTURE
==================================================

Use vanilla JavaScript only.

No frameworks.

No external libraries.

Use:

DOMContentLoaded

querySelector/querySelectorAll

classList

addEventListener

dataset if useful

Do not write a huge JavaScript file.

Keep the code maintainable.

Example architecture:

document.addEventListener('DOMContentLoaded', () => {
    initializeAuthInputs();
    initializePasswordToggles();
    initializeSubmitStates();
    initializeCardInteractions();
});

Functions should be separated by responsibility.

==================================================
ENTRANCE ANIMATION
==================================================

The page should animate when loaded.

Sequence:

1. Auth card
   fade + translateY

2. Header
   fade in

3. Logo
   subtle scale/fade

4. Form body
   fade in

5. Form fields
   staggered entrance

Use approximately:

400–650ms total.

Do not make the animation slow.

Use:

cubic-bezier(0.16, 1, 0.3, 1)

Do NOT make every element bounce.

No excessive animation.

==================================================
FORM FOCUS ANIMATION
==================================================

This is especially important.

When clicking:

Username
Email
Password
Confirm Password

the field should feel alive.

Example:

normal:
border beige

focused:
border navy/gold

icon:
slightly changes position/opacity

wrapper:
small shadow

input:
very subtle background change

Transition:
180–220ms

The interaction should feel premium.

Not flashy.

==================================================
PASSWORD TOGGLE
==================================================

The eye button must look like part of the input.

Requirements:

- no ugly default button styling
- correct alignment
- 40px minimum clickable area
- keyboard accessible
- visible hover/focus state
- smooth icon transition

Do not use emoji as icons.

Use the existing icon system if available.

If there is no icon library, use inline SVG.

==================================================
BUTTON
==================================================

Primary button:

background:
#1A2332

text:
#FFFCF5

height:
46–48px

border-radius:
8px

Hover:
slightly lighter navy

Active:
translateY(1px)

Loading:
show small spinner inside button

Do NOT replace the button text permanently.

Do NOT prevent submission.

==================================================
COLOR SYSTEM
==================================================

Use Finora dashboard colors.

Primary:

#1A2332

Primary hover:

#243447

Background:

#FDF8F0

Surface:

#FFFCF5

Secondary:

#F5EFE6

Border:

#D9C5A5

Text:

#2C2C2C

Muted:

#8A8680

Accent:

#C4A35A

Income:

#52794A

Expense:

#A85D3F

Do not introduce:

purple
neon
bright blue
pink
cyan gradients

==================================================
MOBILE DESIGN
==================================================

Mobile is NOT a shrunken desktop version.

At:

320px
360px
375px
390px
414px

the design must remain intentional.

Card:

width:
calc(100% - 28px)

No horizontal scrolling.

Header becomes slightly shorter.

Form spacing becomes tighter.

Inputs remain at least 44px tall.

Buttons remain easy to tap.

Register page must be scrollable naturally.

Do NOT use fixed heights that cause clipping.

Do NOT hide fields.

Do NOT create oversized empty space.

The reference mobile composition is the primary inspiration.

==================================================
DESKTOP DESIGN
==================================================

At:

1024px
1280px
1440px
1920px

Keep the authentication card centered.

Do not stretch it.

Use generous background space but keep the card visually dominant.

The page should feel like a real Finora product screen.

==================================================
ANTI AI-SLOP RULES
==================================================

DO NOT:

- use purple gradients
- use glassmorphism
- use huge rounded cards
- use giant typography
- use excessive shadows
- use excessive badges
- use random decorative icons
- use fake statistics
- use marketing slogans everywhere
- use unnecessary sections
- use 5 different animation styles
- use rainbow colors
- use excessive blur
- use generic SaaS dashboard aesthetics
- create oversized whitespace
- use random gradients
- use emoji as UI icons
- add fake content

The interface should feel restrained, editorial, premium, and intentional.

==================================================
ACCESSIBILITY
==================================================

Maintain:

- visible focus states
- keyboard navigation
- proper labels
- button semantics
- form semantics
- sufficient contrast
- touch targets >= 44px
- prefers-reduced-motion

When reduced motion is enabled:

disable or significantly reduce:

- entrance animation
- transform animation
- decorative motion
- icon movement

==================================================
IMPORTANT: DO NOT BREAK THYMELEAF
==================================================

Before finishing, compare the resulting HTML against the original.

Verify every:

th:action
th:object
th:field
th:href
th:if

is still present and correct.

Verify:

confirmPassword

still uses:

name="confirmPassword"

and does NOT use:

th:field

==================================================
FINAL QA
==================================================

Test:

/login

/register

Desktop:

1280x720
1440x900
1920x1080

Mobile:

320x720
375x812
390x844
414x896

Check:

- no horizontal scroll
- no clipped form
- no overlapping text
- no broken logo
- no broken password toggle
- no broken form submission
- no changed routes
- no changed backend
- no changed form names
- no changed Thymeleaf bindings
- no console errors
- no JavaScript errors

Test login:

valid username/password submission must still work.

Test register:

all fields must still submit correctly.

Test:

password visibility toggle.

Test:

focus animation.

Test:

filled input state.

Test:

submit loading state.

Test:

Register → Login navigation.

Test:

Login → Register navigation.

==================================================
FINAL OUTPUT
==================================================

After implementation:

1. List files changed.
2. Explain the visual changes briefly.
3. Explain the JavaScript interactions added.
4. Confirm backend/authentication was NOT changed.
5. Confirm all Thymeleaf bindings were preserved.
6. Confirm responsive testing was performed.

Do not modify unrelated files.

Do not stop at a basic HTML/CSS implementation.

The result must visually follow the supplied reference while remaining unmistakably FINORA.