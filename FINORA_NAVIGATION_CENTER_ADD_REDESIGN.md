# FINORA --- Floating Center Add Navigation Redesign

## 1. Objective

Redesign the existing Finora navigation so the primary mobile navigation
has a **center floating Add button**.

The center button is the main creation action:

-   Tap/click the center `+` button.
-   A compact action menu appears.
-   The user can choose:
    -   `Pemasukan` --- add income
    -   `Pengeluaran` --- add expense
-   The interaction must feel intentional, polished, and native to the
    existing Finora design.
-   Do not redesign unrelated navigation items.
-   Do not change application data, backend logic, routes, database
    structure, authentication, or existing transaction forms.

This is a **navigation/UI interaction redesign only**.

------------------------------------------------------------------------

## 2. Non-Negotiable Rules

### DO NOT change

-   Database schema
-   Entity/model classes
-   Repository classes
-   Service classes
-   Controller business logic
-   Authentication logic
-   Existing transaction fields
-   Existing Thymeleaf form bindings
-   Existing transaction routes
-   Existing page data
-   Existing category data
-   Existing report calculations
-   Existing dashboard calculations
-   Existing sidebar information architecture
-   Existing desktop navigation destinations
-   Existing navigation URLs unless absolutely required to connect the
    existing Add actions
-   Existing Finora colors/theme
-   Existing logo/assets
-   Existing content text outside the navigation interaction

### DO NOT

-   Invent new backend features.
-   Create duplicate transaction logic.
-   Create fake data.
-   Hard-code transaction values.
-   Replace existing forms with JavaScript forms.
-   Intercept or break existing form submissions.
-   Use AJAX for creating transactions.
-   Store user/account data in localStorage.
-   Introduce a new UI framework.
-   Add a new icon library if the project already has usable icons.
-   Replace the entire navigation architecture unnecessarily.
-   Make the navigation look like a generic SaaS dashboard.
-   Use purple gradients.
-   Use neon colors.
-   Use glassmorphism.
-   Use excessive shadows.
-   Use oversized typography.
-   Add unnecessary cards.
-   Make the interface look like AI-generated template UI.

The result should look like a deliberate part of Finora, not a separate
component.

------------------------------------------------------------------------

## 3. Existing Finora Visual Language

The navigation must inherit the existing Finora visual system.

Use the existing variables where available.

Core palette:

``` css
--cream: #FDF8F0;
--ivory: #FFFCF5;
--off-white: #F5EFE6;
--kraft: #E8DDD0;
--kraft-dark: #D9C5A5;
--warm-gray: #8A8680;
--charcoal: #2C2C2C;
--ink-navy: #1A2332;
--ink-navy-light: #243447;
--muted-gold: #C4A35A;
--sage: #E8EDE3;
--income-green: #52794A;
--terracotta: #A85D3F;
```

The center Add button should primarily use:

-   Finora navy for the main button.
-   Ivory/cream for the icon.
-   Muted gold as a subtle active/accent color.
-   Income green for the Pemasukan action.
-   Terracotta for the Pengeluaran action.

Do not introduce unrelated colors.

------------------------------------------------------------------------

## 4. Target Interaction

### Closed state

The navigation remains clean and compact.

Example concept:

``` text
[ Dashboard ] [ Pemasukan ] [    +    ] [ Pengeluaran ] [ Laporan ]
                                  ↑
                              center action
```

The exact existing navigation destinations must be preserved.

The center button should visually stand apart from the navigation bar
without looking oversized.

### Open state

When the user presses the center `+`:

``` text
                 [ Pemasukan ]
                      ↑
                    [ + ]
                      ↓
                [ Pengeluaran ]
```

Alternative placement is allowed if it works better with the existing
navigation layout.

The two actions should appear with a short, coordinated animation.

Each action should clearly communicate its purpose.

### Pemasukan action

-   Label: `Pemasukan`
-   Existing income creation route/form must be used.
-   Do not create a second income form.
-   Use the existing icon system.
-   Visual accent may use `--income-green`.

### Pengeluaran action

-   Label: `Pengeluaran`
-   Existing expense creation route/form must be used.
-   Do not create a second expense form.
-   Use the existing icon system.
-   Visual accent may use `--terracotta`.

------------------------------------------------------------------------

## 5. Center Button Behavior

The button must have multiple visual states:

### Default

-   Stable circular or softly rounded floating button.
-   Navy background.
-   Clear plus icon.
-   Subtle shadow.
-   Slight elevation above navigation.

### Hover

Desktop only:

-   Slight upward movement.
-   Slight shadow increase.
-   Very subtle scale increase.
-   No excessive bouncing.

### Press

-   Short scale-down feedback.
-   Return smoothly to normal size.

### Open

The `+` icon should transform into a close state, preferably:

``` text
+
```

to

``` text
×
```

using a smooth rotation/transform.

Do not swap the entire button abruptly.

### Closing

Reverse the same animation.

------------------------------------------------------------------------

## 6. Action Menu Animation

Use JavaScript for interaction state and CSS for visual animation.

When opened:

1.  Center button rotates/transforms.
2.  Pemasukan action appears.
3.  Pengeluaran action appears.
4.  Labels/icons fade and slide into position.
5.  Each item uses a small stagger delay.

Suggested timing:

-   Button transform: 220--300ms
-   Action items: 220--320ms
-   Stagger: 40--70ms
-   Easing: `cubic-bezier(0.16, 1, 0.3, 1)`

Keep animations fast and responsive.

Do not make the menu feel slow.

------------------------------------------------------------------------

## 7. JavaScript Requirements

Create or enhance the existing navigation JavaScript.

Recommended structure:

``` js
initializeAddMenu();
toggleAddMenu();
closeAddMenu();
```

The JavaScript must:

-   Toggle the menu.
-   Toggle the button's active state.
-   Toggle `aria-expanded`.
-   Close the menu when clicking outside.
-   Close the menu with `Escape`.
-   Close the menu after selecting an action.
-   Avoid duplicate event listeners.
-   Work after page navigation/reload.
-   Never interfere with normal anchor navigation.
-   Never intercept form submission.
-   Never modify transaction data.

Use event listeners instead of inline JavaScript where possible.

Example accessibility behavior:

``` html
<button
    type="button"
    class="nav-add-button"
    aria-label="Tambah transaksi"
    aria-expanded="false">
```

The menu should expose a meaningful label/state to assistive technology.

------------------------------------------------------------------------

## 8. Desktop Behavior

Do not blindly copy the mobile bottom navigation to desktop.

First inspect the existing navigation.

If desktop currently uses a sidebar:

-   Preserve the sidebar.
-   Keep its existing navigation destinations.
-   Add the new central Add interaction in a visually appropriate
    location.
-   Do not duplicate every navigation item unnecessarily.
-   The Add action may be positioned as a floating action button near
    the primary content/navigation boundary if that fits the existing
    layout.

Desktop must remain balanced at:

-   1024px
-   1280px
-   1440px
-   1920px

Do not allow the floating button to cover important content.

------------------------------------------------------------------------

## 9. Mobile Behavior

Mobile is the primary focus.

Target widths:

-   320px
-   360px
-   375px
-   390px
-   393px
-   414px

Requirements:

-   No horizontal scrolling.
-   Bottom navigation remains reachable.
-   Center Add button stays centered.
-   Button does not cover page content unnecessarily.
-   Menu remains inside the viewport.
-   Labels do not overflow.
-   Touch targets should be at least approximately 44px.
-   Safe-area support should be considered for modern phones.

Use:

``` css
padding-bottom: env(safe-area-inset-bottom);
```

where appropriate.

The center button may visually overlap the navigation bar slightly, but
the overlap must be intentional and consistent.

------------------------------------------------------------------------

## 10. Visual Style

The design should feel:

-   Elegant
-   Editorial
-   Financial
-   Warm
-   Minimal
-   Crafted
-   Responsive
-   Slightly tactile

It should NOT feel:

-   Like a generic admin template
-   Like Bootstrap defaults
-   Like Material UI defaults
-   Like an AI-generated dashboard
-   Over-animated
-   Childish
-   Neon
-   Cyberpunk
-   Glassmorphism-heavy

Use subtle details from Finora:

-   Fine borders
-   Warm cream surfaces
-   Navy controls
-   Muted gold accents
-   Existing paper/grid textures where appropriate
-   Small shadow layers
-   Existing icon language

------------------------------------------------------------------------

## 11. Suggested Component Structure

Use a structure similar to:

``` html
<nav class="mobile-navigation">

    <!-- existing navigation items -->

    <div class="nav-add-wrapper">
        <button
            type="button"
            class="nav-add-button"
            aria-label="Tambah transaksi"
            aria-expanded="false">

            <span class="nav-add-icon">
                <!-- existing/project icon -->
            </span>
        </button>

        <div class="nav-add-menu" aria-hidden="true">

            <a
                href="EXISTING_INCOME_CREATE_ROUTE"
                class="nav-add-action nav-add-income">

                <span class="nav-add-action-icon"></span>
                <span class="nav-add-action-label">Pemasukan</span>
            </a>

            <a
                href="EXISTING_EXPENSE_CREATE_ROUTE"
                class="nav-add-action nav-add-expense">

                <span class="nav-add-action-icon"></span>
                <span class="nav-add-action-label">Pengeluaran</span>
            </a>

        </div>
    </div>

    <!-- existing navigation items -->

</nav>
```

IMPORTANT:

Do not guess the existing routes.

Inspect the existing navigation and controllers/templates first and
reuse the exact existing create routes.

------------------------------------------------------------------------

## 12. CSS Architecture

Do not dump all CSS into one giant block.

Organize the new CSS approximately as:

``` css
/* Navigation base */

/* Center add button */

/* Add button states */

/* Add action menu */

/* Income action */

/* Expense action */

/* Open/closed states */

/* Desktop */

/* Mobile */

/* Small mobile */

/* Safe area */

/* Reduced motion */
```

Use existing Finora variables.

Use transitions instead of excessive keyframe animation.

Suggested easing:

``` css
cubic-bezier(0.16, 1, 0.3, 1)
```

Avoid:

``` css
linear
```

for the primary UI interaction unless appropriate.

------------------------------------------------------------------------

## 13. Accessibility

Required:

-   `aria-label`
-   `aria-expanded`
-   `aria-hidden` where appropriate
-   Keyboard support
-   Escape closes menu
-   Visible focus state
-   Minimum touch target
-   Reduced-motion support

Respect:

``` css
@media (prefers-reduced-motion: reduce)
```

When reduced motion is enabled:

-   Remove large movement.
-   Remove rotation.
-   Keep only short opacity/state transitions or no animation.
-   Functionality must remain identical.

------------------------------------------------------------------------

## 14. Do Not Break Existing Navigation

Before changing anything:

1.  Inspect the existing navigation HTML.
2.  Inspect navigation.js.
3.  Inspect responsive.css.
4.  Inspect relevant page templates.
5.  Identify the existing income creation route.
6.  Identify the existing expense creation route.
7.  Identify mobile navigation behavior.
8.  Identify desktop navigation behavior.
9.  Identify existing active-page state handling.

Only then implement the redesign.

Do not replace the navigation from scratch unless the existing structure
absolutely prevents the requested interaction.

Prefer incremental modification.

------------------------------------------------------------------------

## 15. QA Checklist

### Functional

-   [ ] Dashboard navigation works.
-   [ ] Pemasukan navigation works.
-   [ ] Pengeluaran navigation works.
-   [ ] Kategori navigation works.
-   [ ] Laporan navigation works.
-   [ ] Template navigation works.
-   [ ] Settings navigation works.
-   [ ] Logout still works.
-   [ ] Center Add button opens.
-   [ ] Center Add button closes.
-   [ ] Pemasukan action opens the existing income form.
-   [ ] Pengeluaran action opens the existing expense form.
-   [ ] Escape closes the menu.
-   [ ] Outside click closes the menu.
-   [ ] Clicking an action closes the menu naturally.
-   [ ] No duplicate navigation actions are created.

### Data integrity

-   [ ] No backend files modified.
-   [ ] No database files modified.
-   [ ] No entity/model changes.
-   [ ] No repository changes.
-   [ ] No service changes.
-   [ ] No transaction data changed.
-   [ ] No form binding changed.
-   [ ] No route changed unless required and already existing.

### Responsive

Test:

-   [ ] 320px
-   [ ] 360px
-   [ ] 375px
-   [ ] 390px
-   [ ] 393px
-   [ ] 414px
-   [ ] 768px
-   [ ] 1024px
-   [ ] 1280px
-   [ ] 1440px
-   [ ] 1920px

### Visual

-   [ ] Center button is actually centered.
-   [ ] Menu does not overflow viewport.
-   [ ] No content is unintentionally covered.
-   [ ] No horizontal scroll.
-   [ ] Existing Finora theme remains intact.
-   [ ] No purple gradient.
-   [ ] No neon.
-   [ ] No excessive glass effect.
-   [ ] No generic AI/SaaS appearance.
-   [ ] Animations feel smooth and intentional.

------------------------------------------------------------------------

## 16. Implementation Principle

The goal is NOT to add more UI.

The goal is to make the existing Finora navigation feel more intentional
by turning the central action into a clear transaction creation point.

Keep the surrounding navigation quiet.

Let the center `+` button become the visual focus.

The result should look like a feature that was designed specifically for
Finora.
