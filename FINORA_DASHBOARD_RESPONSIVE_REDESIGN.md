# FINORA — Responsive Dashboard Redesign Specification
## Desktop + Mobile / Anti-Slop Implementation Guide for OpenCode

> **Project:** Finora  
> **Scope:** Dashboard only  
> **Goal:** Make the dashboard feel intentionally designed for desktop AND mobile, while preserving Finora's existing visual identity, data, routes, and functionality.

---

# 1. IMPORTANT: READ THIS BEFORE CODING

This redesign is **not** a request to replace Finora with a generic finance dashboard.

The existing Finora theme is already recognizable and must remain the foundation.

### Existing visual identity to preserve

From the current dashboard:

- warm/off-white paper-like background
- dark plum / burgundy navigation areas
- dark charcoal/navy typography
- muted sage green for positive/income information
- muted warm amber/orange for expense information
- cream/white cards
- soft borders
- subtle shadows
- rounded corners, but NOT excessive pill-shaped UI
- calm, editorial, personal-finance feeling
- small elegant typography and generous whitespace
- Finora branding and current data/content

### What is allowed

You MAY:

- improve spacing
- improve hierarchy
- change card sizes and positions
- introduce a better desktop grid
- create a real desktop sidebar
- keep bottom navigation on mobile
- improve responsive behavior
- improve typography hierarchy
- improve hover/focus/active states
- add subtle transitions and micro-interactions
- improve charts/visual summaries if the existing data already supports them
- reorganize existing dashboard sections for usability
- improve avatar/profile presentation
- upgrade icons while preserving meaning
- introduce subtle visual details that fit the existing theme

### What is NOT allowed

Do NOT:

- change Finora's brand/theme into a completely different visual style
- replace the existing color identity with blue/purple fintech SaaS colors
- turn the dashboard into a generic AI-generated admin template
- add fake financial numbers
- invent new financial features
- invent transactions, charts, statistics, goals, or account data
- change database structure
- change entity/model fields
- change existing business logic
- change authentication logic
- change routes unless absolutely required for the responsive UI
- remove existing functionality
- rewrite the whole project unnecessarily
- replace working backend code just because the frontend is being redesigned
- introduce unnecessary libraries
- use huge gradients
- use excessive glassmorphism
- use excessive rounded cards
- use excessive floating elements
- use giant headings
- use random decorative blobs
- add meaningless animations
- make every element move
- create a visually busy dashboard

---

# 2. CURRENT PROBLEM

The current mobile dashboard is visually readable, but the desktop experience is effectively behaving like a stretched/mobile composition.

The redesign must solve this.

## Core requirement

**Desktop and mobile must be intentionally different compositions, not the same layout merely resized.**

Think:

```text
DESKTOP
Sidebar + top area + multi-column dashboard workspace

MOBILE
Compact header + stacked content + bottom navigation
```

The content/data can be shared, but the composition must be device-specific.

---

# 3. DESIGN DIRECTION

Use the concept:

## "Finora Editorial Finance"

The interface should feel like:

- a calm personal financial journal
- modern but not corporate
- elegant but not luxurious
- warm but not childish
- minimal but not empty
- functional but not dashboard-template-like

Avoid the typical:

```text
dark navy fintech SaaS
+ neon green
+ giant statistics
+ excessive charts
+ glassmorphism
+ gradients everywhere
+ huge rounded cards
```

Instead use:

```text
warm paper background
+ ink typography
+ sage positive states
+ muted amber expense states
+ structured grid
+ restrained borders
+ subtle depth
+ editorial spacing
```

---

# 4. REFERENCE DIRECTION

These references are for **layout and UX inspiration only**, not for copying.

### Reference A — Personal Finance Dashboard

A finance dashboard showing a clear desktop hierarchy, sidebar navigation, overview metrics, charts, and recent activity.

Reference:
https://www.uidux.com/personal-finance-manager-app-dashboard-for-figma-and-adobe-xd

Use the idea of:
- structured desktop workspace
- sidebar navigation
- overview first
- data grouped by importance

Do NOT copy its colors or exact component design.

### Reference B — Responsive Finance Dashboard

A mobile + desktop finance dashboard concept.

Reference:
https://dribbble.com/shots/25930907-Personal-Finance-Dashboard-Mobile-Desktop-UI

Use the idea of:
- different mobile/desktop compositions
- strong information hierarchy
- compact mobile navigation

Do NOT copy the blue palette.

### Reference C — Warm / Sage Finance Direction

A warm, restrained finance interface with cream and sage tones.

Reference:
https://sleek.design/templates/budgeting-app

Useful principles:
- warm cream background
- sage for positive financial states
- restrained accent colors
- clear money hierarchy
- less visual noise

Do not turn Finora into a copy of this design.

### Reference D — Sage Personal Finance Dashboard

A soft sage/neutral finance dashboard direction.

Reference:
https://dribbble.com/shots/27185487-FinanceUs-Personal-Finance-Dashboard-UI-Design

Useful principle:
- soft sage + neutral palette
- structured financial information
- calm visual hierarchy

Again: inspiration only.

---

# 5. TARGET DESKTOP LAYOUT

Target desktop width:

- 1280px
- 1366px
- 1440px
- larger screens should remain comfortable

Recommended maximum content width:

```css
max-width: 1440px;
```

Do not stretch content infinitely.

## Desktop structure

```text
┌─────────────────────────────────────────────────────────────────┐
│ SIDEBAR │                  MAIN WORKSPACE                       │
│         │                                                       │
│ Finora  │  Greeting / Date / Profile                           │
│         │                                                       │
│ Beranda │  ┌────────────┬────────────┬────────────┐            │
│ Pemasukan│ │ Pemasukan  │ Pengeluaran│   Saldo    │            │
│ Pengeluaran││            │            │            │            │
│ Tabungan │ └────────────┴────────────┴────────────┘            │
│ Target   │                                                       │
│ Laporan  │  ┌────────────────────────┐ ┌────────────────────┐  │
│ Budget   │  │ Financial overview     │ │ Quick summary      │  │
│ Kategori │  │ / existing chart       │ │ / existing data    │  │
│          │  │                        │ │                    │  │
│ Setting  │  └────────────────────────┘ └────────────────────┘  │
│          │                                                       │
│          │  ┌────────────────────────────────────────────────┐  │
│          │  │ Recent activity / existing dashboard content   │  │
│          │  └────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

This is a conceptual structure.

**Do not create sections that do not already have supporting data.**

If the current project does not have a chart or recent transaction dataset available to the dashboard, do not fabricate one. Instead, reorganize the existing content.

---

# 6. DESKTOP SIDEBAR

On desktop, replace the mobile bottom navigation with a vertical sidebar.

Recommended width:

```text
220px – 250px
```

Sidebar should feel like part of Finora, not an unrelated admin template.

### Sidebar contents

Use existing navigation items.

Expected conceptual order:

1. Finora branding
2. Beranda
3. Pemasukan
4. Pengeluaran
5. Tabungan
6. Target Tabungan
7. Laporan
8. Budget
9. Kategori
10. Pengaturan

Do not create fake navigation items.

### Sidebar behavior

Active page:

- subtle filled background
- small visual indicator
- readable label
- restrained accent

Do NOT:

- use giant glowing active states
- use neon colors
- animate the entire sidebar
- make every item look like a pill

---

# 7. DESKTOP HEADER

The desktop header should be visually lightweight.

Include existing available information such as:

- greeting
- current user/profile
- avatar
- relevant existing context

Avoid unnecessary:

- fake notifications
- fake search
- fake AI assistant
- fake financial score
- fake status indicators

If an element does not already exist in the application, do not invent data for it.

---

# 8. DESKTOP SUMMARY CARDS

The current mobile dashboard shows:

- Pemasukan
- Pengeluaran
- Saldo

Keep those concepts.

Desktop should present them horizontally in a clean grid.

Example:

```text
┌────────────────┐ ┌────────────────┐ ┌────────────────┐
│ PEMASUKAN      │ │ PENGELUARAN    │ │ SALDO          │
│ Rp 62.323.000  │ │ Rp 15.800.000  │ │ Rp 46.523.000  │
│ Total masuk    │ │ Total keluar   │ │ Saldo saat ini │
└────────────────┘ └────────────────┘ └────────────────┘
```

### Card design

Income:
- existing sage identity

Expense:
- existing amber identity

Balance:
- neutral cream / stronger ink hierarchy

Use color primarily to communicate meaning.

Do not make the entire interface colorful.

---

# 9. MOBILE LAYOUT

The screenshot shows an iPhone 16 width of approximately 393px.

Mobile should be designed around:

```text
360px – 430px
```

The mobile layout should NOT simply use the desktop grid.

## Mobile structure

```text
┌─────────────────────────────┐
│ Finora                 ○    │
├─────────────────────────────┤
│                             │
│ Good morning,               │
│ Atharazka Al Gifary         │
│                             │
│ ┌─────────────────────────┐ │
│ │ Pemasukan               │ │
│ │ Rp 62.323.000           │ │
│ └─────────────────────────┘ │
│                             │
│ ┌─────────────────────────┐ │
│ │ Pengeluaran             │ │
│ │ Rp 15.800.000           │ │
│ └─────────────────────────┘ │
│                             │
│ ┌─────────────────────────┐ │
│ │ Saldo                   │ │
│ │ Rp 46.523.000           │ │
│ └─────────────────────────┘ │
│                             │
│ existing dashboard content │
│                             │
├─────────────────────────────┤
│  Beranda ↑ ↓ Laporan ⚙     │
└─────────────────────────────┘
```

This is conceptual only.

---

# 10. MOBILE BOTTOM NAVIGATION

Keep the bottom navigation because it is appropriate for the mobile experience.

Important:

- fixed to viewport
- safe-area aware
- does not cover content
- comfortable touch targets
- active item clearly visible
- icons consistent
- labels remain readable
- avoid tiny icons

Minimum practical touch target:

```text
44px × 44px
```

Add bottom padding to page content so the navigation does not cover the last dashboard card.

---

# 11. MOBILE HEADER

Keep the current compact Finora header concept, but improve it.

Recommended:

```text
Finora                              Avatar
```

The header should:

- remain compact
- have good vertical alignment
- respect safe-area/top spacing
- not consume too much screen height
- keep avatar circular
- preserve existing avatar functionality

Do not add unnecessary hamburger navigation if bottom navigation already handles the primary navigation.

---

# 12. RESPONSIVE BREAKPOINT STRATEGY

Do not depend on only one breakpoint.

Recommended:

```text
< 600px
Mobile

600px – 899px
Tablet / compact layout

900px – 1199px
Desktop compact

>= 1200px
Full desktop
```

The exact breakpoints can be adapted to the project's existing CSS architecture.

Do not duplicate the entire dashboard HTML unnecessarily if CSS/layout restructuring can solve the problem.

However, if desktop and mobile truly require different semantic structures, a small controlled variation is acceptable.

---

# 13. TABLET BEHAVIOR

Tablet should not look broken.

At approximately 768px:

- desktop sidebar may become compact/collapsible
- cards may become 2 columns
- spacing reduces
- typography scales down slightly
- navigation remains usable

At approximately 1024px:

- desktop structure can become active
- sidebar returns to normal width
- cards can become 3 columns where appropriate

---

# 14. TYPOGRAPHY

Keep the current Finora typography identity.

If the project already imports a font, reuse it.

Do NOT add another font just for decoration.

Hierarchy should roughly be:

```text
Page title
    28–36px desktop
    24–28px mobile

Section title
    18–22px

Financial amount
    24–32px desktop
    20–26px mobile

Label
    11–14px

Supporting text
    12–14px
```

These are starting points, not mandatory exact values.

Use font-weight and spacing more than giant font sizes to create hierarchy.

---

# 15. SPACING SYSTEM

Use a consistent spacing scale.

Example:

```text
4px
8px
12px
16px
20px
24px
32px
40px
48px
```

Avoid random values such as:

```text
17px
23px
37px
43px
```

unless the existing design specifically requires them.

---

# 16. CARD SYSTEM

Cards should feel related.

Use:

- one border radius system
- one border system
- one shadow system
- consistent internal padding

Recommended visual language:

```text
border: subtle
shadow: low elevation
radius: moderate
background: warm cream/white
```

Avoid:

- every card having a different radius
- every card having a different shadow
- huge floating cards
- glassmorphism
- neon borders

---

# 17. COLOR RULE

Preserve the existing theme.

Conceptual palette:

```text
Background
warm off-white / paper

Primary ink
dark plum / charcoal

Positive
muted sage

Expense
muted amber / terracotta

Neutral
cream / white

Borders
soft warm gray
```

Important:

**Do not blindly replace existing CSS variables with guessed hex values.**

First inspect the current project and reuse its actual variables/colors.

If variables do not exist, extract the colors from the existing implementation before introducing new ones.

---

# 18. ANIMATION

Animations should communicate state, not decorate the screen.

Good:

- sidebar active transition
- card hover elevation
- subtle number appearance
- avatar interaction
- page section reveal
- bottom-nav active transition
- focus transitions

Bad:

- floating cards
- spinning icons
- bouncing dashboard cards
- continuous background animation
- excessive parallax
- large gradient blobs
- every element animating on load

Recommended duration:

```text
150ms – 250ms
```

For larger transitions:

```text
250ms – 400ms
```

Respect:

```css
prefers-reduced-motion
```

---

# 19. ACCESSIBILITY

The redesign must preserve or improve:

- keyboard navigation
- visible focus state
- readable contrast
- button semantics
- link semantics
- alt text for meaningful images
- sufficient touch target size
- reduced-motion support

Do not remove accessible labels just because icons look cleaner.

---

# 20. DATA SAFETY

This is a UI redesign.

Therefore:

### NEVER change

- entity fields
- database schema
- repositories
- services
- authentication
- session logic
- financial calculations
- existing business rules

unless an actual frontend integration bug requires a minimal change.

The UI must consume existing data.

Do not hardcode:

```text
Rp 62.323.000
Rp 15.800.000
Rp 46.523.000
```

Those values are only examples visible in the current screenshot.

Use the project's actual dynamic variables.

---

# 21. IMPLEMENTATION PROCESS

Before modifying anything:

1. Inspect project structure.
2. Locate the dashboard template.
3. Locate dashboard CSS.
4. Locate global CSS.
5. Locate responsive CSS.
6. Locate navigation component/template.
7. Locate avatar/profile component.
8. Locate controller/service supplying dashboard data.
9. Identify current CSS variables/colors.
10. Identify which dashboard values are dynamic.
11. Identify which elements are shared across other pages.

Then make the smallest architectural changes needed.

---

# 22. IMPORTANT ANTI-SLOP RULES

OpenCode must NOT immediately start rewriting the dashboard.

First inspect.

Do not create:

- `DashboardV2`
- `DashboardNew`
- `dashboard-final`
- duplicate CSS files
- random temporary components

unless the existing project architecture explicitly requires it.

Prefer editing the existing dashboard implementation.

Before creating a new CSS class, search for an existing class that already serves the same purpose.

Before creating a new component, check whether an existing component can be reused.

---

# 23. ANTI-SLOP VISUAL CHECKLIST

After implementation, check:

### Does it look like Finora?

If not, stop and correct it.

### Does desktop feel like desktop?

There should be a real desktop workspace, not a stretched phone.

### Does mobile feel like mobile?

The content should be stacked intentionally and bottom navigation should feel natural.

### Is the interface too colorful?

If yes, reduce decorative color.

### Are there too many cards?

If yes, consolidate.

### Are there too many rounded rectangles?

If yes, reduce radius or combine sections.

### Are there unnecessary gradients?

Remove them.

### Does it look like a generic AI finance dashboard?

If yes, redesign the composition using Finora's existing visual language.

---

# 24. QUALITY CHECKS

OpenCode must test at least:

```text
390 × 844
393 × 852
430 × 932
768 × 1024
1024 × 768
1280 × 720
1366 × 768
1440 × 900
1920 × 1080
```

Check:

- no horizontal scrolling
- no clipped cards
- no overlapping navigation
- no hidden content
- no broken avatar
- no broken icons
- no text overflow
- no buttons outside viewport
- no bottom-nav overlap
- no desktop sidebar appearing incorrectly on mobile
- no mobile bottom nav appearing incorrectly on desktop
- no console errors introduced by the redesign

---

# 25. ACCEPTANCE CRITERIA

The redesign is considered complete only when:

- [ ] Existing Finora theme is still recognizable.
- [ ] Desktop has a dedicated desktop composition.
- [ ] Mobile has a dedicated mobile composition.
- [ ] Tablet is usable.
- [ ] Existing data remains dynamic.
- [ ] No fake financial data was added.
- [ ] Existing routes still work.
- [ ] Existing navigation still works.
- [ ] Existing authentication still works.
- [ ] Existing profile/avatar behavior still works.
- [ ] No unnecessary backend changes were introduced.
- [ ] No unnecessary dependencies were added.
- [ ] No horizontal scrolling exists.
- [ ] Bottom navigation works correctly on mobile.
- [ ] Desktop sidebar works correctly on desktop.
- [ ] Visual hierarchy is clear.
- [ ] Animations are subtle.
- [ ] `prefers-reduced-motion` is respected.
- [ ] Console has no new errors.
- [ ] Existing pages were not visually broken by shared CSS changes.

---

# 26. OPENCODE PLAN PROMPT

Copy this prompt into OpenCode first.

```text
You are working on the existing Finora application.

TASK:
Plan a responsive redesign of the existing Dashboard so desktop and mobile have intentionally different compositions while preserving the existing Finora visual identity, data, backend behavior, routes, and functionality.

IMPORTANT:
Do NOT modify code yet.

First inspect the project.

Inspect:
1. project structure
2. dashboard template
3. dashboard CSS
4. global CSS
5. responsive CSS
6. navigation/sidebar/bottom-nav implementation
7. avatar/profile implementation
8. controller/service supplying dashboard data
9. existing CSS variables and theme colors
10. reusable components related to the dashboard

Then produce a concrete implementation plan.

The plan must identify:
- files that need modification
- files that should NOT be modified
- current responsive problems
- current desktop problems
- current mobile problems
- which existing classes/components can be reused
- where responsive breakpoints should be handled
- how desktop navigation should differ from mobile navigation
- how existing dashboard data will map into the redesigned layout
- risks of breaking shared pages
- exact verification steps

DESIGN DIRECTION:
Finora must remain warm, minimal, editorial, and personal-finance oriented.

Preserve:
- warm/off-white background
- dark plum/burgundy identity
- dark ink typography
- sage positive/income color
- muted amber expense color
- cream/white surfaces
- subtle borders
- restrained shadows
- moderate rounded corners

Do NOT turn it into:
- generic SaaS admin UI
- blue fintech dashboard
- neon finance dashboard
- glassmorphism UI
- gradient-heavy AI-generated design
- excessive bento cards
- excessive pills
- oversized typography

RESPONSIVE CONCEPT:

Desktop:
- dedicated sidebar
- lightweight desktop header
- 3-column financial summary where appropriate
- structured multi-column content area
- wider use of existing dashboard information
- max-width workspace

Mobile:
- compact Finora header
- vertically stacked financial summary
- existing mobile-friendly content flow
- fixed bottom navigation
- comfortable touch targets
- no horizontal overflow

IMPORTANT:
Do not invent new financial data.
Do not hardcode screenshot values.
Do not change database/entity/service/auth logic.
Do not add dependencies unless absolutely necessary.

ANTI-SLOP:
Do not immediately rewrite everything.
Do not create duplicate dashboard files unless architecture requires them.
Do not create random CSS files.
Do not change unrelated pages.
Do not use placeholder content.
Do not add decorative UI that has no functional purpose.

Output ONLY the implementation plan first.
Wait for approval before building.
```

---

# 27. OPENCODE BUILD PROMPT

Use this only after reviewing/approving the plan.

```text
Implement the approved Finora responsive Dashboard redesign now.

IMPORTANT:
Use the existing project architecture.
Do not rewrite the application.
Do not change backend/business logic unless a minimal frontend integration fix is required.

Before editing:
- re-check the files identified in the plan
- confirm existing data bindings
- confirm existing theme variables
- confirm navigation implementation

IMPLEMENTATION GOAL:

Desktop and mobile must be intentionally different layouts.

DESKTOP:
- desktop sidebar
- lightweight header
- 3-column financial summary where appropriate
- structured content grid
- comfortable max-width
- existing dashboard data only
- clean whitespace
- calm editorial finance aesthetic

MOBILE:
- compact header
- stacked cards/content
- fixed bottom navigation
- safe-area support
- touch-friendly controls
- no horizontal scrolling
- no desktop sidebar
- content must not hide behind bottom navigation

TABLET:
- adaptive grid
- no broken desktop sidebar
- no oversized mobile cards

THEME:
Preserve the existing Finora visual identity.

Use the project's existing:
- colors
- typography
- icons
- spacing conventions
- components
- variables

If improvement is necessary, refine rather than replace.

VISUAL QUALITY:
The final result should look deliberately designed by a human designer.

Avoid:
- random gradients
- giant text
- excessive rounded cards
- excessive shadows
- excessive animations
- floating decorative blobs
- fake statistics
- fake charts
- generic AI dashboard layouts
- unnecessary icons
- unnecessary labels
- excessive empty space

ANIMATION:
Use only subtle interaction animations.
Prefer 150–250ms transitions.
Respect prefers-reduced-motion.

DATA:
All financial values must remain dynamic.
Do not hardcode screenshot values.
Do not invent transactions, charts, goals, balances, or statistics.

SAFETY:
Do not modify:
- database schema
- entity/model fields
- financial calculations
- authentication
- session behavior
- unrelated services
- unrelated controllers

unless absolutely required and clearly justified.

RESPONSIVE TESTING:
Verify at:
390x844
393x852
430x932
768x1024
1024x768
1280x720
1366x768
1440x900
1920x1080

Check:
- horizontal overflow
- sidebar behavior
- bottom navigation behavior
- card wrapping
- typography
- avatar
- icons
- content clipping
- fixed navigation overlap
- console errors

IMPORTANT:
If a shared CSS change risks breaking another page, scope the dashboard styles instead of changing global styles blindly.

After implementation:
1. inspect the final diff
2. remove unused CSS/classes created during the redesign
3. remove temporary debug code
4. check console/runtime errors
5. verify all existing dashboard interactions
6. verify navigation
7. verify mobile and desktop separately

Do not stop at "looks good".
Actually verify the responsive behavior.

At the end, report:
- files changed
- what changed in each file
- responsive behavior implemented
- tests/checks performed
- any issue that could not be verified
- any backend/data logic intentionally left untouched
```

---

# 28. FINAL DESIGN PRINCIPLE

The most important rule:

> **Do not make the mobile dashboard smaller. Make the desktop dashboard different.**

Mobile is for:
- quick glance
- quick navigation
- compact information

Desktop is for:
- overview
- comparison
- broader financial context
- multiple sections visible at once

The same Finora identity should connect both experiences.

The user should feel:

**"This is the same Finora, but it was actually designed for the device I'm using."**

Not:

**"This is the mobile page stretched across a monitor."**
