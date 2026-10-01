document.addEventListener('DOMContentLoaded', () => {
    initializeEntranceAnimation();
    initializeAuthInputs();
    initializePasswordToggles();
    initializeButtonInteractions();
    initializeAlertAnimations();
    initializePageTransitions();
    initializeSectionTransitions();
});

function initializeEntranceAnimation() {
    const card = document.querySelector('.auth-card');
    if (!card) return;

    requestAnimationFrame(() => {
        card.classList.add('is-visible');

        const header = card.querySelector('.auth-card-header');
        const logo = card.querySelector('.header-logo');
        const brand = card.querySelector('.header-brand-name');
        const body = card.querySelector('.auth-card-body');
        const eyebrow = body ? body.querySelector('.eyebrow') : null;
        const title = body ? body.querySelector('.form-title') : null;
        const subtitle = body ? body.querySelector('.form-subtitle') : null;
        const alerts = body ? body.querySelectorAll('.alert-box') : [];
        const fields = card.querySelectorAll('.auth-field');
        const button = card.querySelector('.btn-submit');
        const switchPrompt = card.querySelector('.form-switch-prompt');

        setTimeout(() => header.classList.add('is-visible'), 100);
        setTimeout(() => {
            logo.classList.add('is-visible');
            brand.classList.add('is-visible');
        }, 180);
        setTimeout(() => body.classList.add('is-visible'), 300);

        if (eyebrow) setTimeout(() => eyebrow.classList.add('is-visible'), 360);
        if (title) setTimeout(() => title.classList.add('is-visible'), 420);
        if (subtitle) setTimeout(() => subtitle.classList.add('is-visible'), 480);

        alerts.forEach((alert, i) => {
            setTimeout(() => alert.classList.add('is-visible'), 520 + i * 100);
        });

        const fieldBase = 580;
        fields.forEach((field, i) => {
            setTimeout(() => field.classList.add('is-visible'), fieldBase + i * 60);
        });

        if (button) {
            setTimeout(() => button.classList.add('is-visible'), fieldBase + fields.length * 60 + 60);
        }
        if (switchPrompt) {
            setTimeout(() => switchPrompt.classList.add('is-visible'), fieldBase + fields.length * 60 + 120);
        }
    });
}

function initializeAuthInputs() {
    const wrappers = document.querySelectorAll('.auth-field');
    wrappers.forEach(wrapper => {
        const input = wrapper.querySelector('input');
        if (!input) return;

        input.addEventListener('focus', () => {
            wrapper.classList.add('is-focused');
            wrapper.classList.toggle('is-filled', input.value.length > 0);
        });

        input.addEventListener('blur', () => {
            wrapper.classList.remove('is-focused');
            wrapper.classList.toggle('is-filled', input.value.length > 0);
        });

        input.addEventListener('input', () => {
            wrapper.classList.toggle('is-filled', input.value.length > 0);
        });
    });
}

function initializePasswordToggles() {
    const toggles = document.querySelectorAll('.btn-toggle-pwd');
    toggles.forEach(btn => {
        btn.removeAttribute('onclick');
        btn.addEventListener('click', () => {
            const input = btn.previousElementSibling;
            if (!input || input.tagName !== 'INPUT') return;

            const isPassword = input.type === 'password';
            input.type = isPassword ? 'text' : 'password';
            btn.setAttribute('aria-pressed', String(isPassword));
            btn.classList.toggle('is-visible', isPassword);

            btn.classList.remove('is-toggling');
            void btn.offsetWidth;
            btn.classList.add('is-toggling');
            setTimeout(() => btn.classList.remove('is-toggling'), 220);
        });
    });
}

function initializeButtonInteractions() {
    const forms = document.querySelectorAll('.auth-form');
    forms.forEach(form => {
        const button = form.querySelector('.btn-submit');
        if (!button) return;

        form.addEventListener('submit', () => {
            button.classList.add('is-loading');
        });

        button.addEventListener('pointerdown', () => {
            if (!button.classList.contains('is-loading')) {
                button.classList.add('is-pressed');
            }
        });

        button.addEventListener('pointerup', () => {
            button.classList.remove('is-pressed');
        });

        button.addEventListener('pointerleave', () => {
            button.classList.remove('is-pressed');
        });
    });
}

function initializeAlertAnimations() {
    const observer = new MutationObserver((mutations) => {
        mutations.forEach((mutation) => {
            if (mutation.type === 'attributes' && mutation.attributeName === 'class') {
                const target = mutation.target;
                if (target.classList.contains('is-visible') && target.classList.contains('alert-error') && !target.classList.contains('is-shake')) {
                    target.classList.add('is-shake');
                    setTimeout(() => target.classList.remove('is-shake'), 400);
                }
            }
        });
    });

    document.querySelectorAll('.alert-box.alert-error').forEach(alert => {
        observer.observe(alert, { attributes: true, attributeFilter: ['class'] });
    });
}

function initializePageTransitions() {
    const switchLinks = document.querySelectorAll('.form-switch-prompt a');
    switchLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            const card = document.querySelector('.auth-card');
            if (card && !card.classList.contains('page-exit')) {
                e.preventDefault();
                card.classList.add('page-exit');
                setTimeout(() => {
                    window.location.href = link.href;
                }, 190);
            }
        });
    });

    const backLink = document.querySelector('.back-link');
    if (backLink) {
        backLink.addEventListener('click', (e) => {
            const card = document.querySelector('.auth-card');
            if (card && !card.classList.contains('page-exit')) {
                e.preventDefault();
                card.classList.add('page-exit');
                setTimeout(() => {
                    window.location.href = backLink.href;
                }, 190);
            }
        });
    }
}

function initializeSectionTransitions() {
    const sections = document.querySelectorAll('.form-section');
    sections.forEach((section, i) => {
        const label = section.querySelector('.section-label');
        const fields = section.querySelectorAll('.auth-field');

        setTimeout(() => {
            if (label) label.classList.add('is-visible');
            section.classList.add('is-visible');

            if (i > 0) {
                const divider = document.createElement('div');
                divider.className = 'section-divider';
                section.parentNode.insertBefore(divider, section);
                setTimeout(() => divider.classList.add('is-visible'), 50);
            }
        }, 600 + i * 200);
    });
}
