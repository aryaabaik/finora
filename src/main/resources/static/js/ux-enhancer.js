/**
 * Finora — Global UX Enhancer
 * Adds consistent delightful animations across existing pages
 */
(function() {
    'use strict';

    function ready(fn) {
        if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', fn);
        } else {
            fn();
        }
    }

    ready(function() {
        addUniversalAttributes();
        animateDynamicLists();
        enhanceForms();
        enhanceModals();
        enhanceDeleteButtons();
        enhanceNumbers();
        addAmbientOrbs();
    });

    function addUniversalAttributes() {
        document.querySelectorAll('.btn, button:not(.hamburger):not(.drawer-close):not(.modal-close), .menu-item, .drawer-nav-item, .bottom-nav-item, .nav-add-button, .nav-add-action, .btn-google').forEach(function(el) {
            if (!el.hasAttribute('data-ripple')) el.setAttribute('data-ripple', '');
        });

        document.querySelectorAll('.summary-card, .chart-card, .filter-bar, .income-list-container, .expense-list-container, .kategori-card, .template-modal-card, .tx-card, .insight-card').forEach(function(el) {
            el.classList.add('hover-lift');
        });

        document.querySelectorAll('.page-header:not([data-motion]), .filter-section:not([data-motion]), .summary-row:not([data-motion])').forEach(function(el, index) {
            el.setAttribute('data-motion', 'enter');
            el.setAttribute('data-motion-delay', String(index * 80));
            setTimeout(function() {
                el.classList.add('is-visible');
            }, (index * 80) + 50);
        });

        document.querySelectorAll('.report-section:not([data-motion]), .closing-section:not([data-motion])').forEach(function(el) {
            el.setAttribute('data-motion', 'reveal');
            if (window.FinoraMotion && typeof FinoraMotion.refresh === 'function') {
                FinoraMotion.refresh();
            } else {
                el.classList.add('in-view');
            }
        });

        // Auth page animations
        setTimeout(function() {
            document.querySelectorAll('.auth-divider, .btn-google').forEach(function(el) {
                el.classList.add('is-visible');
            });
        }, 200);
    }

    function animateDynamicLists() {
        var observer = new MutationObserver(function(mutations) {
            mutations.forEach(function(mutation) {
                mutation.addedNodes.forEach(function(node, index) {
                    if (!(node instanceof HTMLElement)) return;
                    if (node.matches('.income-row, .expense-row, .kategori-card, .template-modal-card, .tx-card, .chip')) {
                        node.style.opacity = '0';
                        node.style.transform = 'translateY(12px) scale(0.98)';
                        node.style.transition = 'opacity 360ms cubic-bezier(0.16,1,0.3,1), transform 360ms cubic-bezier(0.16,1,0.3,1)';
                        setTimeout(function() {
                            node.style.opacity = '1';
                            node.style.transform = 'translateY(0) scale(1)';
                        }, 30 + index * 35);
                    }
                });
            });
        });

        document.querySelectorAll('#incomeList, #expenseList, #incomeKategoriList, #expenseKategoriList, #templateModalBody, #activeFilters').forEach(function(container) {
            observer.observe(container, { childList: true, subtree: true });
        });
    }

    function enhanceForms() {
        document.querySelectorAll('form').forEach(function(form) {
            form.addEventListener('submit', function() {
                var submit = form.querySelector('button[type="submit"], .btn-submit');
                if (submit && window.FinoraUI && FinoraUI.inlineLoader && !submit.classList.contains('btn-submit')) {
                    FinoraUI.inlineLoader.addToButton(submit);
                }
            });
        });

        document.querySelectorAll('input, select, textarea').forEach(function(input) {
            input.addEventListener('focus', function() {
                var group = input.closest('.form-group, .filter-group, .select-group, .auth-field, .input-wrapper');
                if (group) group.classList.add('is-focused');
            });
            input.addEventListener('blur', function() {
                var group = input.closest('.form-group, .filter-group, .select-group, .auth-field, .input-wrapper');
                if (group) group.classList.remove('is-focused');
            });
        });
    }

    function enhanceModals() {
        document.querySelectorAll('.modal-overlay, .profile-modal').forEach(function(modal) {
            var observer = new MutationObserver(function() {
                if (modal.classList.contains('is-visible') || modal.classList.contains('active')) {
                    var content = modal.querySelector('.modal-container, .profile-modal-content');
                    if (content && window.FinoraAnimations) {
                        FinoraAnimations.bounceIn(content);
                    }
                }
            });
            observer.observe(modal, { attributes: true, attributeFilter: ['class'] });
        });
    }

    function enhanceDeleteButtons() {
        document.addEventListener('click', function(e) {
            var btn = e.target.closest('.delete-btn');
            if (!btn) return;
            if (window.FinoraAnimations) {
                FinoraAnimations.shake(btn);
            }
        }, true);
    }

    function enhanceNumbers() {
        document.querySelectorAll('.summary-value span, #totalIncomeValue, #totalExpenseValue, .balance-value span').forEach(function(el) {
            var text = el.textContent || '';
            if (/^[\d\.]+$/.test(text.trim()) && !el.hasAttribute('data-counter')) {
                el.setAttribute('data-counter', '');
                el.setAttribute('data-counter-duration', '1200');
            }
        });

        if (window.FinoraAnimations && typeof FinoraAnimations.initNumberCounters === 'function') {
            FinoraAnimations.initNumberCounters();
        }
    }

    function addAmbientOrbs() {
        if (document.querySelector('.finora-ambient-orbs')) return;
        if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) return;

        var orbs = document.createElement('div');
        orbs.className = 'finora-ambient-orbs';
        orbs.innerHTML = '<span></span><span></span><span></span>';
        document.body.appendChild(orbs);
    }
})();
