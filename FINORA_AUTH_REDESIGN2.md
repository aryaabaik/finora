FINORA LOGIN & REGISTER REDESIGN — INSPECTION COMPLETE
A. FILES TO MODIFY
File	Action
src/main/resources/static/css/auth.css	Modify — Enhance existing styles to match refined design spec
src/main/resources/templates/auth/login.html	Modify — Restructure visual layout, add decorative header, preserve all form bindings
src/main/resources/templates/auth/register.html	Modify — Same visual system as login, preserve all form bindings
src/main/resources/static/js/password-toggle.js	Preserve — Already works; may enhance visually only if needed
B. FILES THAT MUST REMAIN UNTOUCHED
File
src/main/java/com/finora/finora/Controller/AuthController.java
src/main/java/com/finora/finora/Service/AuthService.java
src/main/java/com/finora/finora/Controller/LandingController.java
src/main/resources/templates/layout/auth.html
src/main/resources/static/css/app.css
src/main/resources/static/css/responsive.css
src/main/resources/static/css/dashboard.css, pemasukan.css, pengeluaran.css, laporan.css, template.css, settings.css
src/main/resources/static/js/navigation.js
src/main/resources/templates/fragments/navigation.html
src/main/resources/templates/dashboard.html, landing.html, pemasukin/index.html, pengeluaran/index.html
src/main/resources/static/img/finora-logo.svg
src/main/resources/templates/auth/settings.html
C. EXISTING FORM FIELDS AND BINDINGS (Must Preserve Exactly)
LOGIN (login.html):
Form: th:action="@{/login}" method="post" class="auth-form"
- username: type="text", id="username", name="username", required, autofocus
- password: type="password", id="password", name="password", required
- btn-toggle-pwd: onclick="togglePassword(this)"
- btn-submit: type="submit"
- Switch: <a th:href="@{/register}">Register</a>
- Alerts: th:if="${error}", th:if="${message}", th:if="${success}"
REGISTER (register.html):
Form: th:action="@{/register}" th:object="${user}" method="post" class="auth-form"
- fullName: type="text", id="fullName", th:field="*{fullName}", required, autofocus
- username: type="text", id="username", th:field="*{username}", required
- email: type="email", id="email", th:field="*{email}", required
- password: type="password", id="password", th:field="*{passwordHash}", required, minlength="6"
- confirmPassword: type="password", id="confirmPassword", name="confirmPassword", required, minlength="6" (NO th:field!)
- btn-toggle-pwd (3 instances): password + confirm password
- btn-submit: type="submit"
- Switch: <a th:href="@{/login}">Login</a>
- Alerts: th:if="${error}"
CRITICAL: confirmPassword has name="confirmPassword" but NO th:field. Must preserve this exact binding.
D. DESIGN SYSTEM
Color System (from existing Finora dashboard CSS):
--cream: #FDF8F0          (background)
--ivory: #FFFCF5           (card surface)
--off-white: #F5EFE6       (secondary surface)
--kraft: #E8DDD0           (sidebar/border)
--kraft-dark: #D9C5A5      (warm beige border)
--warm-gray: #8A8680       (secondary text)
--charcoal: #2C2C2C        (body text)
--ink-navy: #1A2332        (primary text/header/button)
--ink-navy-light: #243447  (hover state)
--muted-gold: #C4A35A      (accent)
--sage: #E8EDE3            (income)
--income-green: #52794A    (income dark)
--terracotta: #A85D3F      (expense)
--border-subtle: rgba(217,197,165,0.45)
Typography:
- Display: Cormorant Garamond (Georgia serif fallback)
- Body: Inter (-apple-system, BlinkMacSystemFont, sans-serif fallback)
Spacing/Radius:
- --radius: 8px, --radius-lg: 12px
- Card padding: 28px desktop, 20px mobile, 16px small mobile
- Card max-width: 440px desktop, calc(100vw - 40px) mobile
Shadows:
- --shadow-card: 0 4px 24px rgba(26,35,50,0.08), 0 1px 4px rgba(26,35,50,0.04)
- --shadow-btn: 0 2px 8px rgba(26,35,50,0.15)
Existing auth.css already has this design system — the redesign enhances it, not replaces it.
E. VISUAL STRUCTURE
Login Page:
┌─────────────────────────────┐
│  [finora-logo.svg]          │  ← Decorative header (navy bg + grid pattern)
│  Finora                     │     min-height: 140px, navy #1A2332
│                             │     + subtle gold accent line at bottom
├─────────────────────────────┤
│                             │
│  Welcome back               │  ← Cormorant Garamond, 26px, navy
│  Sign in to manage your     │  ← 13px, warm gray, centered
│  finances.                  │
│                             │
│  [error/message/success     │  ← Alert boxes preserved
│   alert boxes]              │
│                             │
│  Username or Email          │  ← 11px uppercase label
│  [icon] [input]             │  ← 46px height, kraft border
│                             │
│  Password                   │
│  [icon] [input] [eye-toggle]│
│                             │
│  [    Sign In       ]       │  ← Navy bg, white text, 46px
│                             │
│  Don't have an account?     │
│  Register                   │  ← Sage green link
└─────────────────────────────┘
Register Page (same header, same card structure):
- Two form-section divisions: "Personal" and "Security" with section labels
- Back link in header (absolute positioned, top-left)
- Same form fields, same button, same switch prompt
F. RESPONSIVE STRATEGY
Breakpoints (from existing responsive.css):
- ≤ 540px: Card becomes full-viewport-width minus 40px, body aligns flex-start with padding
- ≤ 380px: Further compression, 32px side margins, smaller fonts
- ≥ 1440px: Card max-width increases to 460px, larger padding
- ≤ 960px: Card max-width 420px
Strategy:
- Mobile-first: card fills viewport width minus safe margins
- No horizontal scrolling at any breakpoint
- Inputs remain 44px+ touch targets
- Typography scales with clamp() where applicable
- No desktop sidebar/navigation on auth pages
- Paper texture background preserved
G. CSS STRATEGY
Approach: Enhance existing auth.css (already well-structured). Add:
1. CSS Custom Properties — Already defined in :root, keep and extend
2. Card animation — Already has cardSlideUp, refine to spec: 450-600ms, cubic-bezier(0.16, 1, 0.3, 1)
3. Staggered entrance — Add header (400ms), form (350-450ms) with 40-70ms stagger delays
4. Input focus states — Already has border-color: var(--finora-ink-navy), add subtle ring/shadow
5. Button hover/active — Already has translateY(-1px), refine shadow transition
6. Paper texture — Already exists via body::before
7. Reduced motion — Already has @media (prefers-reduced-motion: reduce) block
8. Error states — Already has .input-wrapper input.error styling
CSS Architecture (from spec section 18):
/* Variables */
/* Page shell */
/* Auth card */
/* Auth header */
/* Brand */
/* Form */
/* Inputs */
/* Buttons */
/* Links */
/* Validation/error states */
/* Animations */
/* Responsive */
/* Reduced motion */
H. JAVASCRIPT/ANIMATION STRATEGY
Existing JS: password-toggle.js works — preserve exactly.
Allowed additions:
1. Entrance animation — CSS-only via animation-delay on child elements:
- .auth-card: cardSlideUp 500ms cubic-bezier(0.16,1,0.3,1)
- .auth-card-header: fade-in at 400ms
- .auth-card-body: fade-in at 450ms
- Form elements: stagger 40-70ms delays
2. Loading state visuals — Button can show loading spinner on submit (must not prevent submission)
3. Reduced-motion detection — Already in CSS, can add JS check if needed
NOT allowed:
- AJAX authentication
- Form submission interception
- Route changes
- Credential storage
I. QA CHECKLIST
Login:
- Page loads without errors
- username field appears with correct name, id, required, autofocus
- password field appears with correct name, id, required
- th:action="@{/login}" preserved
- method="post" preserved
- Password toggle works (eye open/closed)
- Submit button still submits form
- Error/message/success alerts still render via Thymeleaf
- Register link navigates to /register
- No horizontal scroll at 320px, 375px, 414px, 768px, 1280px, 1920px
Register:
- All 5 fields appear: fullName, username, email, password, confirmPassword
- th:object="${user}" preserved
- th:field="*{fullName}", *{username}", *{email}", *{passwordHash}" preserved
- confirmPassword has name="confirmPassword" (NO th:field)
- th:action="@{/register}" preserved
- Password toggles work for both password fields
- Submit button still submits form
- Error alert still renders
- Login link navigates to /login
Visual/QA:
- No generic AI/SaaS appearance
- No purple gradients, neon, glassmorphism
- Finora navy/cream color system used
- Logo reused from /img/finora-logo.svg
- Animations smooth, restrained, under 600ms
- prefers-reduced-motion respected
- Focus states visible and accessible
- All labels visible, no floating labels
- Typography uses Inter + Cormorant Garamond only
- No unnecessary icons, decorations, or content