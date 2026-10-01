# FINORA — CENTER ACTION NAVIGATION REDESIGN

## Goal
Redesign the existing Finora navigation so the primary action is a centered `+` button.

When pressed, the button opens two quick actions:
- Tambah Pemasukan
- Tambah Pengeluaran

Use the existing Finora visual language. This is a frontend-only redesign.

## Non-Negotiable Safety Rules
DO NOT modify:
- database or data
- entities/models
- repositories
- services
- controllers
- authentication
- transaction logic
- categories
- reports
- templates
- user data
- existing form bindings
- existing routes

Do not create new backend routes.

The quick actions MUST use the existing routes for creating income and expense transactions. Inspect the repository and use the actual existing routes; never guess them.

Do not remove existing navigation functionality.

## 1. INSPECT FIRST
Before editing:
1. Find the actual navigation fragment/component.
2. Find its CSS.
3. Find its JavaScript.
4. Find the existing active-state logic.
5. Find the existing create-income route/link.
6. Find the existing create-expense route/link.
7. Inspect responsive/mobile navigation.
8. Inspect existing Finora CSS variables and icon system.

Do not implement until this inspection is complete.

## 2. UX CONCEPT

Mobile concept:

```text
┌──────────────────────────────────────┐
│                                      │
├──────────────────────────────────────┤
│  Home   Income    (+)   Expense  More│
└──────────────────────────────────────┘
```

When `+` is pressed:

```text
             Tambah Pengeluaran
                    ↑
              Tambah Pemasukan
                    ↑
                   (+)
```

Keep the menu compact. It must not become a full-screen modal or giant dropdown.

On desktop, preserve the existing navigation structure where appropriate. Integrate the quick-action control naturally instead of blindly converting the desktop sidebar into mobile navigation.

## 3. CENTER PLUS BUTTON
Make `+` the visual focal point.

Suggested:
- 52–60px desktop
- 52–62px mobile
- minimum 44px touch target
- circular/near-circular
- Finora navy
- subtle muted-gold detail
- ivory/light icon
- restrained shadow

States:
- default: `+`
- hover: subtle lift and scale
- press: `scale(.94)` for about 100–150ms
- open: smoothly transform `+` into `×`

No neon, purple gradient, glassmorphism, oversized FAB, or excessive glow.

## 4. QUICK ACTIONS
Two actions only:

### Tambah Pemasukan
Use the existing income visual language:
- subtle sage/green accent
- existing income icon if available
- existing create-income destination

### Tambah Pengeluaran
Use the existing expense visual language:
- subtle terracotta accent
- existing expense icon if available
- existing create-expense destination

Do not invent transaction types or fake data.

## 5. ANIMATION
The interaction MUST use JavaScript.

Opening sequence:
1. plus changes toward close state
2. income action rises/fades in
3. expense action rises/fades in
4. use a small stagger

Use:
`cubic-bezier(0.16, 1, 0.3, 1)`

Suggested total interaction duration: 200–350ms.

No bouncing, infinite animation, or distracting effects.

Closing should reverse smoothly.

## 6. JAVASCRIPT
JavaScript must handle:
- open/close toggle
- plus/close state
- outside click
- Escape key
- animation state classes
- keyboard accessibility

Do NOT:
- intercept transaction forms
- use AJAX
- reload the page just to toggle the menu
- add unnecessary libraries
- duplicate event listeners

Prefer the existing `navigation.js` architecture if applicable.

## 7. FINORA DESIGN
Use existing Finora colors/tokens such as:
- cream
- ivory
- kraft
- kraft-dark
- warm-gray
- charcoal
- ink-navy
- ink-navy-light
- muted-gold
- income green
- expense terracotta

Avoid:
- generic SaaS styling
- purple/neon gradients
- glassmorphism
- excessive pills
- excessive shadows
- random icon styles
- unnecessary decorations

The navigation must look like it belongs to Dashboard, Pemasukan, Pengeluaran, Laporan, Template, and Pengaturan.

## 8. MOBILE
Test:
- 320px
- 360px
- 375px
- 393px
- 414px
- 430px

Requirements:
- no horizontal scrolling
- no clipping
- no overlap
- comfortable touch targets
- labels remain readable
- plus remains centered
- quick actions stay inside viewport
- page content is not unnecessarily covered

## 9. ACCESSIBILITY
Use:
- real button semantics
- accessible label
- `aria-expanded`
- visible focus state
- keyboard access
- Escape to close

Respect `prefers-reduced-motion`.

When reduced motion is enabled, minimize transforms/stagger while keeping functionality intact.

## 10. FILE STRATEGY
Likely files may include:
- `templates/fragments/navigation.html`
- `static/js/navigation.js`
- `static/css/responsive.css`
- navigation-related CSS

Do not assume these are correct. Inspect first.

Modify the smallest necessary set of files.

Do not create duplicate navigation components.

## 11. QA
Verify:
- all existing navigation links still work
- active states still work
- logout/settings still work
- plus opens/closes
- outside click closes
- Escape closes
- income action uses existing create-income route
- expense action uses existing create-expense route
- no console errors
- no duplicate listeners
- mobile works at all listed widths
- desktop remains correct
- dashboard/pemasukan/pengeluaran/laporan/template/settings still work
- no data changed
- no backend files changed

## 12. FINAL PRINCIPLE
The center `+` should feel like a natural Finora "quick add" control, not a copied generic FAB.

Keep it:
- elegant
- practical
- premium
- calm
- intentional
- restrained

Do not redesign unrelated pages.
Do not invent functionality.
Do not change application data.
