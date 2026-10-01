# Finora Login & Register Redesign --- OpenCode Specification

## 1. Objective

Redesign the existing Finora **Login** and **Register** pages so they
visually follow the provided mobile reference:

-   Compact centered authentication card
-   Distinct decorative top/header area
-   Rounded lower form area
-   Clean typography
-   Minimal, premium, editorial feel
-   Strong visual hierarchy
-   Subtle motion and interaction
-   Excellent desktop and mobile behavior

However, this is **not a copy of the reference color palette**.

The final design must use the existing Finora visual language from the
dashboard, especially the visual character of:

-   Pemasukan
-   Pengeluaran
-   Kategori
-   Laporan
-   Template
-   Sidebar/navigation

The result must feel like the same Finora product, not a separate
template.

------------------------------------------------------------------------

# 2. NON-NEGOTIABLE RULES

## 2.1 Do NOT change application data

The redesign is UI-only.

Do NOT modify:

-   Database structure
-   Entities/models
-   Repositories
-   Services
-   Controllers
-   Authentication logic
-   Registration logic
-   Password handling
-   Session handling
-   Validation rules
-   User data
-   Existing routes
-   POST/GET endpoints
-   Form submission behavior
-   Existing backend contracts
-   Existing field names
-   Existing `name` attributes
-   Existing `id` attributes
-   Existing input types
-   Existing Thymeleaf bindings
-   Existing `th:action`
-   Existing `th:object`
-   Existing `th:field`
-   Existing error handling
-   Existing success/error messages

Do not "clean up" backend code.

Do not rename variables just for style.

Do not introduce a new authentication system.

------------------------------------------------------------------------

# 3. FORM PRESERVATION RULE

Before changing any HTML/template:

1.  Inspect the current Login template.
2.  Inspect the current Register template.
3.  Identify every existing form field.
4.  Identify every `name`, `id`, `type`, `required`, `autocomplete`,
    Thymeleaf binding, action and method.
5.  Identify existing validation/error messages.
6.  Identify password visibility functionality if already present.
7.  Identify existing links between Login and Register.
8.  Preserve all of them exactly unless a purely visual wrapper is
    required.

You may rearrange the visual layout.

You may add wrappers/classes for styling.

You may add decorative elements.

You may add CSS classes.

You may add JavaScript for visual interaction.

But the functional form contract must remain intact.

------------------------------------------------------------------------

# 4. REFERENCE DESIGN DIRECTION

The provided reference image shows two compact authentication screens.

Use it as a **layout and interaction reference**, not as something to
copy literally.

### Important visual characteristics to preserve

-   Narrow authentication card
-   Large rounded corners
-   Decorative top section
-   Strong separation between header and form
-   Form area feels like a white paper/card surface
-   Compact inputs
-   Simple black/dark primary button in the reference
-   Small footer action/link
-   Minimal decoration
-   Large breathing room around the authentication card
-   Mobile-first composition

For Finora, adapt these ideas to the existing dashboard design language.

------------------------------------------------------------------------

# 5. FINORA COLOR DIRECTION

The authentication pages must visually belong to the existing Finora
dashboard.

Do NOT introduce a random new palette.

Use the existing project colors by inspecting the current dashboard and
transaction pages first.

The expected visual direction is approximately:

### Base

-   Warm off-white / cream background
-   Soft ivory surfaces
-   Deep navy / charcoal for primary text
-   Muted beige borders

### Pemasukan

Use the existing muted sage/green visual language for income-related
accents.

### Pengeluaran

Use the existing warm brown / terracotta visual language for
expense-related accents.

### Authentication

Use a neutral Finora combination rather than forcing income or expense
colors.

Recommended hierarchy:

-   Background: warm cream
-   Authentication header: deep Finora navy / charcoal
-   Primary button: deep navy
-   Accent: muted sage green
-   Secondary accent: warm beige / terracotta
-   Text: dark navy/charcoal
-   Secondary text: muted warm gray
-   Borders: soft beige

IMPORTANT:

Do not invent gradients just because they are common in AI-generated
designs.

Avoid neon colors.

Avoid purple SaaS palettes.

Avoid excessive gold.

Avoid glassmorphism.

Avoid excessive shadows.

------------------------------------------------------------------------

# 6. DESKTOP DESIGN

On desktop, create a centered authentication composition.

The page should NOT become a huge two-column marketing landing page.

The authentication card should remain the main focus.

Suggested structure:

``` text
                    FINORA AUTH PAGE

              ┌───────────────────────┐
              │                       │
              │     BRAND / LOGO      │
              │   decorative header   │
              │                       │
              ├───────────────────────┤
              │                       │
              │      Welcome back     │
              │                       │
              │  existing form fields │
              │                       │
              │    primary button     │
              │                       │
              │   Register / Login    │
              │                       │
              └───────────────────────┘
```

The card should have:

-   controlled width
-   comfortable but not excessive padding
-   rounded corners
-   subtle border
-   restrained shadow
-   clean alignment
-   balanced vertical rhythm

Do not let the card become unnecessarily tall.

Do not center every individual element if that makes the form harder to
scan.

------------------------------------------------------------------------

# 7. LOGIN PAGE

The Login page should communicate:

-   Finora branding
-   Welcome back
-   Existing login form
-   Existing username/email field
-   Existing password field
-   Existing password visibility control if present
-   Existing login button
-   Existing register navigation

Example visual hierarchy:

``` text
FINORA

Welcome back
Sign in to manage your finances.

USERNAME OR EMAIL
[ existing input ]

PASSWORD
[ existing password input ]

[ Sign In ]

Don't have an account? Register
```

Do not change the actual wording unless it is already part of the
existing visual content and the user explicitly requests copy changes.

The main focus should remain on the existing authentication form.

------------------------------------------------------------------------

# 8. REGISTER PAGE

Use the exact same design system as Login.

Do not create a completely different page.

Register should feel like Login's sibling.

Example structure:

``` text
FINORA

Create your account
[ existing registration fields ]

[ Register ]

Already have an account? Sign In
```

Again:

**DO NOT invent new registration fields.**

Use whatever fields already exist in the current project.

If the existing registration form has:

-   Full name
-   Username
-   Email
-   Password
-   Confirm password

keep those fields.

If it has a different structure, preserve the actual structure.

------------------------------------------------------------------------

# 9. CARD HEADER

Create a distinctive Finora header section inspired by the reference.

Possible elements:

-   Finora logo
-   Finora wordmark
-   subtle geometric pattern
-   tiny grid/paper texture
-   minimal decorative shapes

The decoration must be restrained.

Do not use:

-   random blobs
-   random circles
-   floating 3D objects
-   stock illustrations
-   fake financial graphics
-   excessive icons
-   random abstract AI art

The header should feel intentionally designed.

------------------------------------------------------------------------

# 10. FINORA LOGO

Reuse the project's existing logo/brand asset whenever available.

Do not replace it with an invented logo.

If the project already has a logo component or image:

-   reuse it
-   preserve its asset path
-   preserve its meaning
-   style it with CSS if necessary

Do not alter backend data to support the logo.

------------------------------------------------------------------------

# 11. TYPOGRAPHY

Inspect the existing Finora dashboard typography and reuse the same font
system whenever possible.

Do not introduce five different fonts.

Recommended hierarchy:

-   Small eyebrow label
-   Medium page title
-   Clear field labels
-   Comfortable body text
-   Small secondary navigation text

The typography should feel editorial and refined rather than like a
generic Bootstrap/SaaS page.

Avoid:

-   giant headings
-   excessive bold text
-   all-caps everywhere
-   huge letter spacing
-   childish typography
-   futuristic fonts

------------------------------------------------------------------------

# 12. INPUT DESIGN

Inputs should feel premium but simple.

Requirements:

-   consistent height
-   comfortable horizontal padding
-   clear focus state
-   subtle border
-   rounded but not excessively pill-shaped
-   clear labels
-   accessible contrast
-   no unnecessary floating labels unless the existing design already
    uses them

Focus state should be visually obvious.

Suggested interaction:

``` text
Default:
soft beige border

Hover:
slightly stronger border

Focus:
Finora navy/green accent border
subtle outer ring
```

Do not make the focus ring enormous.

------------------------------------------------------------------------

# 13. BUTTON DESIGN

The primary authentication button should use the Finora theme.

Use a strong dark navy/charcoal base.

Possible subtle interaction:

-   slight background shift
-   tiny upward movement
-   subtle shadow increase
-   smooth transition

Avoid:

-   huge gradients
-   glowing neon buttons
-   excessive bounce
-   scale animations that feel cheap

Button should feel stable and trustworthy.

------------------------------------------------------------------------

# 14. ANIMATION & MOTION

Add tasteful CSS/JS motion.

Motion must communicate polish, not decoration.

### Page entrance

Use a subtle sequence:

1.  authentication card fades in
2.  card moves upward a few pixels
3.  header/logo appears slightly after the card
4.  form content follows with a short stagger

Example timing:

-   card: 450--600ms
-   header: 400ms
-   form: 350--450ms
-   stagger: 40--70ms

Use easing such as:

``` css
cubic-bezier(0.22, 1, 0.36, 1)
```

Do not make the page slow.

### Input focus

On focus:

-   border transitions smoothly
-   subtle shadow/ring
-   icon can transition slightly if icons already exist

### Button

On hover:

-   tiny translateY(-1px)
-   subtle shadow change

On active:

-   return to original position

### Page transition

If Login → Register navigation is enhanced with JavaScript, keep it
lightweight.

Do not block navigation.

Do not introduce AJAX authentication.

Do not modify form submission.

------------------------------------------------------------------------

# 15. ACCESSIBILITY

Preserve and improve accessibility.

Requirements:

-   visible labels
-   keyboard navigation
-   focus states
-   sufficient contrast
-   buttons remain actual buttons
-   links remain actual links
-   inputs remain actual inputs
-   do not replace form controls with clickable `<div>`
-   respect `prefers-reduced-motion`

Add:

``` css
@media (prefers-reduced-motion: reduce) {
    *,
    *::before,
    *::after {
        animation-duration: 0.01ms !important;
        animation-iteration-count: 1 !important;
        transition-duration: 0.01ms !important;
        scroll-behavior: auto !important;
    }
}
```

------------------------------------------------------------------------

# 16. MOBILE DESIGN

Mobile is NOT an afterthought.

The reference image is primarily useful as a mobile composition
reference.

At widths around:

-   320px
-   360px
-   375px
-   390px
-   414px
-   430px

the page must remain clean.

Requirements:

-   no horizontal scrolling
-   no clipped content
-   no oversized whitespace
-   no tiny inputs
-   no text overflow
-   no overlapping elements
-   no fixed desktop widths
-   no desktop sidebar
-   no unnecessary decorative content

The card should occupy most of the available width while retaining safe
side margins.

Example:

``` text
┌─────────────────────────┐
│       FINORA            │
│     decorative          │
│       header            │
├─────────────────────────┤
│                         │
│     Welcome back        │
│                         │
│ Username / Email        │
│ [___________________]   │
│                         │
│ Password                │
│ [___________________]   │
│                         │
│ [      Sign In       ]  │
│                         │
│ Don't have account?     │
│ Register                │
│                         │
└─────────────────────────┘
```

Use responsive spacing instead of hardcoded desktop values.

------------------------------------------------------------------------

# 17. DESKTOP RESPONSIVENESS

The design must also work on:

-   1280px
-   1366px
-   1440px
-   1536px
-   1920px

Do not let the card become tiny on large monitors.

Do not let it become enormous either.

Use sensible `max-width`, fluid padding, and viewport-aware spacing.

------------------------------------------------------------------------

# 18. CSS ARCHITECTURE

Keep CSS organized.

Do not dump hundreds of unrelated rules into one giant block.

Suggested organization:

``` css
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
```

Use CSS custom properties for theme values.

Example:

``` css
:root {
    --finora-bg: ...;
    --finora-surface: ...;
    --finora-ink: ...;
    --finora-muted: ...;
    --finora-border: ...;
    --finora-primary: ...;
    --finora-income: ...;
    --finora-expense: ...;
}
```

BUT:

Before choosing exact values, inspect the existing dashboard CSS and
reuse the project's actual color values where possible.

Do not blindly invent colors.

------------------------------------------------------------------------

# 19. JAVASCRIPT

JavaScript may be added only for UI behavior.

Allowed:

-   entrance animation
-   password visibility if needed
-   subtle interaction
-   Login/Register page transition
-   mobile visual enhancements
-   reduced-motion detection
-   loading-state visuals that do not alter submission behavior

Not allowed:

-   changing form submission
-   intercepting authentication
-   changing request payloads
-   changing routes
-   changing passwords
-   changing validation
-   replacing server authentication
-   storing credentials
-   modifying database data

If an existing password visibility script already works, preserve its
functionality and improve only its visual behavior if necessary.

------------------------------------------------------------------------

# 20. ERROR STATES

Existing server-side errors must remain visible.

Do not remove error messages.

Style them consistently with Finora.

Example direction:

-   soft warm/red background
-   subtle border
-   clear readable text
-   compact spacing
-   no huge alert component

Validation errors must not destroy the layout.

------------------------------------------------------------------------

# 21. LOADING STATES

If the current application already has a loading state, preserve it.

If adding a visual loading state is necessary, it must:

-   not alter the request
-   not prevent submission
-   not create duplicate submissions
-   not replace server behavior

Keep it subtle.

------------------------------------------------------------------------

# 22. WHAT NOT TO DO

OpenCode must NOT produce:

-   generic AI SaaS UI
-   purple gradient UI
-   glassmorphism
-   neon effects
-   excessive rounded cards
-   giant hero section
-   two-column marketing landing page
-   random illustrations
-   stock photos
-   fake statistics
-   fake testimonials
-   unnecessary social login buttons
-   unnecessary "Remember me" fields
-   unnecessary Forgot Password functionality
-   new backend endpoints
-   new form fields
-   fake UI data
-   random icons everywhere
-   excessive shadows
-   excessive animations
-   bouncing buttons
-   spinning logos
-   huge typography
-   inconsistent colors
-   random fonts
-   mobile layout that is just a shrunk desktop

------------------------------------------------------------------------

# 23. REQUIRED INSPECTION BEFORE CODING

Before editing anything, inspect:

1.  Current Login template
2.  Current Register template
3.  Login/Register CSS
4.  Login/Register JavaScript
5.  Authentication controller
6.  Authentication service
7.  Dashboard page
8.  Pemasukan page
9.  Pengeluaran page
10. Shared layout/navigation if applicable
11. Existing logo/assets
12. Existing fonts
13. Existing theme variables

The purpose is to understand the existing Finora design system.

Do not modify anything during the inspection stage.

------------------------------------------------------------------------

# 24. PLAN PHASE

Before building, produce a clear implementation plan.

The plan must include:

### A. Files to modify

List every file.

### B. Files that will NOT be modified

Especially:

-   controllers
-   services
-   repositories
-   entities
-   authentication logic

### C. Current form contract

List every existing field and binding that will be preserved.

### D. Design system

Explain:

-   colors
-   typography
-   spacing
-   radius
-   shadows
-   button style
-   input style

### E. Responsive strategy

Explain desktop/tablet/mobile behavior.

### F. Animation strategy

Explain which elements animate and when.

### G. JavaScript strategy

Explain exactly what JS does and what it does NOT do.

Do not begin implementation until the plan is complete.

------------------------------------------------------------------------

# 25. BUILD PHASE

After the plan is approved, implement the redesign.

During implementation:

1.  Modify only presentation-layer files.
2.  Preserve existing form contracts.
3.  Reuse existing Finora assets.
4.  Reuse existing theme values where possible.
5.  Add clean CSS.
6.  Add lightweight JS only where useful.
7.  Do not touch authentication logic.
8.  Do not change database behavior.
9.  Do not create fake data.
10. Do not create unnecessary dependencies.

------------------------------------------------------------------------

# 26. VALIDATION AFTER BUILD

After implementation, inspect the result carefully.

Test:

### Login

-   page loads
-   existing fields appear
-   existing values/bindings work
-   existing password behavior works
-   submit still works
-   validation errors still work
-   Register link works

### Register

-   page loads
-   every existing field appears
-   existing bindings work
-   submit still works
-   validation errors still work
-   Login link works

### Responsive

Test:

-   320px
-   360px
-   375px
-   390px
-   414px
-   430px
-   768px
-   1024px
-   1280px
-   1440px
-   1920px

Check specifically:

-   overflow
-   clipping
-   spacing
-   card width
-   button width
-   input width
-   typography
-   header height
-   animation
-   link placement

------------------------------------------------------------------------

# 27. FINAL QUALITY BAR

The final result should look like a deliberately designed product
interface.

It should NOT look like:

> "AI generated a login page."

It should look like:

> "Finora already had this authentication design as part of its
> product."

Every spacing value should have a reason.

Every decoration should have a purpose.

Every animation should be subtle.

Every color should belong to the existing Finora visual system.

The Login and Register pages should clearly belong to the same product
as Pemasukan, Pengeluaran, Dashboard, Laporan, and Template.

------------------------------------------------------------------------

# 28. OPEN CODE WORKFLOW

Use this workflow exactly:

``` text
INSPECT
   ↓
ANALYZE EXISTING DESIGN
   ↓
CREATE PLAN
   ↓
WAIT FOR APPROVAL
   ↓
BUILD
   ↓
TEST
   ↓
VISUAL QA
   ↓
FIX
```

Do not skip inspection.

Do not skip planning.

Do not immediately rewrite the existing page.

------------------------------------------------------------------------

# 29. SUCCESS CRITERIA

The redesign is successful only if:

-   Login looks polished.
-   Register looks polished.
-   Both pages share one Finora design system.
-   The reference composition is clearly reflected.
-   Finora dashboard colors are respected.
-   Existing forms are unchanged functionally.
-   Existing backend behavior is unchanged.
-   Existing data is untouched.
-   Desktop is balanced.
-   Mobile is excellent.
-   Animations are smooth and restrained.
-   No horizontal overflow exists.
-   No unnecessary UI exists.
-   No generic AI/SaaS appearance exists.
-   No fake content exists.
-   No random colors exist.
-   No backend changes are required.
