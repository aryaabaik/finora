# FINORA — LANDING PAGE UI/UX REDESIGN BRIEF

## STATUS
Approved redesign direction.

This document is the implementation instruction for OpenCode.
The goal is to completely redesign the Finora landing page UI/UX while preserving all existing application data, functionality, routes, and backend logic.

---

# 1. PRIMARY OBJECTIVE

Redesign the Finora landing page so it feels:

- Minimal
- Premium
- Modern
- Editorial
- Elegant
- Calm
- Professional
- Distinct from the current landing page

The redesign must NOT look like a minor CSS update.

The current visual identity must remain recognizable through the existing color family:

- Warm ivory / cream
- Deep navy
- Dusty blue
- Soft beige / gold accents

However, the layout, spacing, composition, hierarchy, card arrangement, section structure, and visual presentation must be substantially redesigned.

Use the approved visual direction as the design reference:
- Large editorial hero
- Large Finora dashboard mockup
- Mobile UI mockup beside/overlapping the dashboard
- Asymmetric composition
- Feature editorial grid
- "Why Finora" visual section
- Statistics row
- Testimonial section
- Dark navy final CTA
- Minimal footer

---

# 2. VERY IMPORTANT — DO NOT BREAK EXISTING DATA

This is a UI/UX redesign task.

DO NOT change:

- Database structure
- Database records
- Entity/model classes
- Repository logic
- Service logic
- Controller logic
- Authentication logic
- Existing routes
- Existing API endpoints
- Existing form submission logic
- Existing session logic
- Existing business logic
- Existing Java/Spring Boot functionality
- Existing user data
- Existing feature data

Do not rename variables, endpoints, model fields, or routes unless absolutely required for a frontend compatibility reason.

If existing dynamic data is already displayed on the landing page, keep using that same data.

The redesign should only change the presentation layer.

---

# 3. BEFORE EDITING

First inspect the existing project.

Identify:

1. Landing page/template
2. Existing CSS
3. Existing JavaScript
4. Existing image/assets
5. Existing routes used by the landing page
6. Existing reusable components/fragments
7. Existing responsive styles
8. Existing animation libraries, if any

Do NOT immediately rewrite files.

Understand the current implementation first.

Preserve reusable functionality where it is still useful.

---

# 4. DESIGN DIRECTION

## Overall Style

Use a refined editorial fintech aesthetic.

Think:

"premium personal finance journal meets modern financial dashboard"

Avoid:

- Generic SaaS template appearance
- Excessive gradients
- Excessive glassmorphism
- Neon colors
- Oversized rounded cards everywhere
- Excessive shadows
- Excessive animations
- Random decorative elements
- "AI-generated website" appearance
- Crowded sections
- Repeating identical cards

The page must feel intentionally designed.

---

# 5. NAVBAR

Redesign the navbar while keeping the existing functionality.

Keep the existing important actions such as:

- Finora logo
- Fitur
- Tentang
- Masuk
- Daftar Akun

Design direction:

- Clean horizontal navigation
- Generous whitespace
- Thin subtle bottom border
- Strong typography hierarchy
- Dark navy primary CTA
- Outline secondary action
- Sticky/floating behavior may be added if it improves UX

Do not make the navbar oversized.

On mobile:

- Use a clean mobile menu
- Keep CTA accessible
- Avoid cramped navigation

---

# 6. HERO SECTION

This must be the biggest visual change.

DO NOT reuse the old hero composition.

## Desktop composition

Create an asymmetric two-column hero.

LEFT:

Small eyebrow label:

"APLIKASI KEUANGAN PRIBADI"

Large editorial headline.

Suggested direction:

"Kelola Keuangan
Dengan Lebih Mudah,
Lebih Tenang."

Use serif typography for the main headline.

Highlight the final phrase with dusty blue/italic styling where appropriate.

Below headline:

Short supporting paragraph explaining Finora.

Then:

Primary CTA:
"Mulai Sekarang →"

Secondary CTA:
"Pelajari Fitur"

RIGHT:

Create a large Finora financial dashboard visual.

The visual should look like an actual application interface, not random decorative cards.

Include realistic representations of existing Finora concepts such as:

- Total saldo
- Pemasukan
- Pengeluaran
- Grafik keuangan
- Tabungan
- Target tabungan
- Recent transaction summary

Add a smaller mobile Finora interface overlapping or positioned beside the dashboard.

The dashboard should become the visual focal point.

Use subtle depth/shadow, but keep everything elegant.

---

# 7. HERO ANIMATIONS

Animations should be subtle and premium.

Suggested:

1. Hero text fade-up
2. Dashboard fade + slight upward movement
3. Dashboard floating animation
4. Chart line reveal
5. Small cards staggered entrance
6. CTA hover micro-interaction
7. Mobile mockup slight floating movement

Do NOT make everything bounce.

Avoid:

- Excessive scaling
- Constant movement
- Fast animations
- Distracting looping effects

Animation duration should generally feel around 300–900ms depending on the element.

Respect `prefers-reduced-motion`.

---

# 8. FEATURE SECTION

Replace the old simple three-card feature layout.

Use an editorial feature grid.

Heading:

"Semua yang kamu
butuhkan dalam satu aplikasi."

Supporting text:

Explain that Finora helps users manage income, expenses, savings, targets, budgets, categories, and reports.

Then create a structured feature grid.

Existing concepts that should remain represented:

- Pemasukan
- Pengeluaran
- Laporan
- Tabungan
- Target Tabungan
- Budget
- Kategori

Do not invent functionality that does not exist.

Each feature should have:

- Minimal line icon
- Small number/label
- Feature title
- Short existing/relevant description
- Subtle hover interaction

Avoid making every feature a huge boxed card.

Use borders, whitespace, columns, and typography to create hierarchy.

---

# 9. WHY FINORA SECTION

Create a strong visual section that feels completely different from the current page.

Layout:

LEFT:
A refined visual composition representing financial progress.

Possible elements:

- Circular savings progress
- Minimal finance card
- Decorative architectural/organic shape
- Soft beige background shape
- Small data visualization

RIGHT:

Eyebrow:

"KENAPA FINORA?"

Heading:

"Lebih dari sekadar
aplikasi keuangan."

Supporting text describing the purpose of Finora.

Below it, show existing/relevant trust/value metrics without inventing real claims.

If the existing project has no real user statistics, DO NOT fabricate statistics such as "10K pengguna", "4.9/5", or "100% aman".

Use truthful project-oriented values instead.

---

# 10. STATISTICS / VALUE ROW

Create a clean horizontal information row.

Use four balanced columns if appropriate.

Possible categories based only on information already supported by the project:

- Keuangan lebih teratur
- Transaksi lebih mudah dicatat
- Target lebih mudah dipantau
- Laporan lebih jelas

Do not create fake business statistics.

Use subtle vertical dividers.

---

# 11. TESTIMONIAL SECTION

Only include testimonials if real testimonial data already exists.

If there are no real testimonials:

DO NOT fabricate customer quotes.

Instead, create a lightweight "Finora philosophy" / product statement section.

The visual can still resemble a testimonial/editorial section without pretending someone said something.

Use:

- Large quotation-style typography
- Minimal decorative background
- Subtle navigation dots only if meaningful

---

# 12. FINAL CTA

Create a completely redesigned final CTA.

Use a deep navy background.

Headline:

"Kelola keuanganmu sekarang,
wujudkan tujuanmu nanti."

Use the existing signup action.

Primary button:

"Daftar Akun →"

Add a small supporting line.

Keep the section spacious.

Use very subtle line/arc decoration.

---

# 13. FOOTER

Minimal footer.

Keep:

- Finora branding
- Existing useful navigation
- Existing relevant links
- Copyright if already present

Do not add unnecessary links or fake company information.

---

# 14. COLOR SYSTEM

Keep the existing Finora color identity.

Suggested hierarchy:

PRIMARY:
Deep navy

BACKGROUND:
Warm ivory / cream

SECONDARY:
Dusty blue

ACCENT:
Soft beige / muted gold

TEXT:
Dark navy

SECONDARY TEXT:
Muted warm gray

Do not introduce bright colors unless required for semantic UI states.

Do not completely change the existing palette.

---

# 15. TYPOGRAPHY

Maintain the existing elegant serif + modern sans-serif character.

SERIF:
Use for:

- Hero headline
- Major section headings
- Editorial statements

SANS:
Use for:

- Navigation
- Body text
- Buttons
- Labels
- Metadata
- UI components

Do not use too many fonts.

Maintain strong typographic hierarchy.

---

# 16. SPACING

Use generous whitespace.

Avoid:

- Sections touching each other
- Extremely tight text
- Excessive padding inside cards
- Huge empty areas without purpose

Desktop should feel spacious.

Mobile should remain compact without becoming cramped.

---

# 17. RESPONSIVE DESIGN

The redesign must be properly responsive.

Desktop:
- Two-column hero
- Dashboard + mobile mockup
- Editorial feature grid
- Horizontal metrics

Tablet:
- Compress grid
- Preserve visual hierarchy

Mobile:
- Single-column hero
- Headline scales down naturally
- Dashboard mockup becomes responsive
- Mobile mockup may move below dashboard
- Feature grid becomes one column or two compact columns
- CTA buttons stack when needed
- Navbar becomes mobile menu
- No horizontal overflow

Test at approximately:

- 1440px
- 1280px
- 1024px
- 768px
- 480px
- 375px

---

# 18. MICRO INTERACTIONS

Use restrained interactions.

Examples:

Buttons:
- Slight translate
- Subtle shadow
- Arrow movement

Feature items:
- Icon slight movement
- Border/underline transition

Dashboard:
- Very subtle floating movement

Navigation:
- Clean underline or opacity transition

Do not animate every element.

---

# 19. PERFORMANCE

Keep the page lightweight.

Avoid:

- Huge unnecessary images
- Heavy animation libraries
- Multiple unnecessary dependencies
- Excessive JavaScript
- Continuous expensive animations

Prefer CSS transitions and existing project dependencies where possible.

---

# 20. ACCESSIBILITY

Maintain:

- Semantic HTML
- Keyboard accessibility
- Visible focus states
- Good contrast
- Button/link semantics
- Reduced-motion support
- Useful alt text for meaningful images

---

# 21. IMPORTANT ANTI-SLOP RULE

The result must NOT look like a generic AI-generated landing page.

Before finishing, check:

- Are there too many cards?
- Are all sections visually identical?
- Is every element rounded?
- Are there too many floating objects?
- Are there excessive gradients?
- Are there fake statistics?
- Are there unnecessary icons?
- Are animations excessive?
- Does the page look like a template?

If yes, simplify and refine.

The design should feel curated rather than generated.

---

# 22. DO NOT COPY THE OLD LAYOUT

The old page currently uses:

- Large centered headings
- Repeated vertical sections
- Three simple feature cards
- Large centered CTA
- Decorative finance cards in the hero

Do not simply rearrange those same components.

The new structure should instead use:

1. Editorial navbar
2. Asymmetric hero
3. Large dashboard visual
4. Feature editorial grid
5. Why Finora visual section
6. Value/statistics row
7. Philosophy/testimonial-style section
8. Dark CTA
9. Minimal footer

---

# 23. IMPLEMENTATION PROCESS

Follow this order:

### Step 1
Inspect the existing landing page and related files.

### Step 2
Identify all existing data bindings and routes.

### Step 3
Create the new page structure.

### Step 4
Implement desktop design.

### Step 5
Implement responsive mobile design.

### Step 6
Add subtle animations.

### Step 7
Check all existing links/buttons.

### Step 8
Check that no backend/data logic was modified.

### Step 9
Check visual consistency.

### Step 10
Fix overflow, spacing, typography, and mobile issues.

---

# 24. FINAL VALIDATION

Before finishing, verify:

- Landing page loads
- Existing routes still work
- Login button still works
- Register button still works
- Feature navigation still works
- Existing dynamic data remains intact
- No backend logic changed
- No database changes
- No broken links
- No console errors
- No horizontal overflow
- Mobile layout works
- Desktop layout works
- Animations work
- Reduced motion works

---

# 25. FINAL INSTRUCTION

Do not tell me that the redesign is complete just because the CSS changed.

The result should visibly look like a NEW Finora landing page.

The redesign must preserve the Finora identity while changing the composition, hierarchy, spacing, layout, visual storytelling, and interaction design substantially.

Prioritize:

1. Visual quality
2. Consistency
3. Usability
4. Responsiveness
5. Performance
6. Data/function preservation

DO NOT modify backend/data functionality just to make the UI easier to implement.
