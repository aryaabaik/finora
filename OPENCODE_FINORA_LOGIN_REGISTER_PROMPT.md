You are working on the Finora Spring Boot + Thymeleaf application.

Your task is to redesign ONLY the Login and Register UI.

IMPORTANT: Do not touch authentication logic, backend behavior, database
structure, user data, routes, form contracts, validation logic, or
request payloads.

A detailed specification is provided in:

FINORA_LOGIN_REGISTER_REDESIGN.md

Follow that specification strictly.

## WORKFLOW --- DO NOT SKIP STEPS

### PHASE 1 --- INSPECT

Before changing any file, inspect:

-   current Login template
-   current Register template
-   related CSS
-   related JavaScript
-   authentication controller
-   authentication service
-   dashboard UI
-   Pemasukan UI
-   Pengeluaran UI
-   shared layout/navigation
-   existing logo/assets
-   existing fonts
-   existing theme variables

Do not edit files during this phase.

Your goal is to understand the existing Finora design system and the
exact existing form contracts.

### PHASE 2 --- CREATE PLAN

Create a concise implementation plan.

The plan must explicitly show:

1.  Files that will be modified.
2.  Files that will NOT be modified.
3.  Every existing Login/Register field that will be preserved.
4.  Existing form actions/methods/bindings that will remain unchanged.
5.  The new visual structure.
6.  The Finora color strategy.
7.  Desktop responsive strategy.
8.  Mobile responsive strategy.
9.  CSS architecture.
10. JavaScript/animation strategy.
11. Accessibility strategy.
12. QA/testing checklist.

DO NOT BUILD YET.

The plan must make it obvious that this is a presentation-only redesign.

### PHASE 3 --- BUILD

After the plan is approved, implement the redesign.

Design direction:

-   Use the supplied reference image as the structural inspiration.
-   Use a compact centered authentication card.
-   Use a distinctive decorative header.
-   Use a clean rounded form surface.
-   Keep the visual language minimal and premium.
-   Adapt the colors to the existing Finora dashboard instead of copying
    the reference's black/white palette literally.

Finora should feel like one coherent product.

Use the actual existing dashboard colors whenever possible.

Do not introduce purple gradients, neon colors, glassmorphism, random
blobs, stock illustrations, fake content, or generic SaaS decoration.

### FORM SAFETY

Preserve exactly:

-   field names
-   field IDs
-   input types
-   Thymeleaf bindings
-   form action
-   form method
-   validation
-   error rendering
-   authentication behavior
-   registration behavior
-   password behavior

You may add CSS classes and visual wrappers.

You may rearrange the visual presentation.

You may NOT change the functional contract.

### CSS

Create clean, maintainable CSS.

Organize it into:

-   variables
-   page shell
-   authentication card
-   header
-   brand
-   form
-   fields
-   buttons
-   links
-   error states
-   animations
-   responsive rules
-   reduced motion

Use CSS variables for Finora colors.

Do not hardcode the same color repeatedly.

### ANIMATION

Add polished but restrained motion:

-   card entrance
-   header/logo entrance
-   form stagger
-   input focus transition
-   button hover/active transition
-   optional Login/Register page transition

Use smooth easing.

Avoid bounce, excessive scaling, spinning, glowing, or slow animations.

Respect:

`prefers-reduced-motion: reduce`

### JAVASCRIPT

JavaScript is allowed ONLY for presentation and interaction.

Allowed:

-   visual entrance animation
-   subtle page transition
-   password visibility UI if required
-   interaction states
-   reduced-motion handling
-   non-invasive loading visuals

Forbidden:

-   changing authentication
-   changing submitted data
-   changing form endpoints
-   intercepting credentials
-   changing validation
-   database changes
-   new authentication APIs

### MOBILE

Treat mobile as a first-class layout.

Test at:

320px, 360px, 375px, 390px, 414px, 430px.

There must be:

-   no horizontal scrolling
-   no clipped text
-   no overlapping content
-   no oversized whitespace
-   no tiny form controls
-   no fixed desktop width
-   no broken animations

The card should fit naturally within the viewport with safe side
margins.

### DESKTOP

Test at:

1280px, 1366px, 1440px, 1536px, 1920px.

The card should remain visually balanced.

Do not turn the page into a giant two-column marketing landing page.

### VISUAL QA

After implementation, inspect the final result carefully.

Look specifically for:

-   spacing inconsistency
-   excessive whitespace
-   inconsistent border radius
-   poor alignment
-   weak contrast
-   oversized typography
-   button sizing
-   input sizing
-   mobile overflow
-   broken form layout
-   animation glitches
-   inconsistent colors
-   elements that look AI-generated or generic

If anything looks wrong, fix it before declaring completion.

## FINAL REQUIREMENT

Do not say "done" immediately after editing.

Perform a final visual and functional QA pass first.

The final Login and Register pages must feel like they were designed
specifically for Finora, not copied from a generic AI login template.
