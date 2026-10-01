/**
 * Finora — Global Motion System
 * Handles page entrance, scroll reveals, stagger animations, and micro-interactions
 */

(function() {
    'use strict';

    const FinoraMotion = {
        config: {
            prefersReducedMotion: false,
            pageEntranceDelay: 60,
            staggerDelay: 35,
            scrollThreshold: 0.14,
            scrollRootMargin: '-60px 0px',
            maxStaggerItems: 10
        },

        observers: {
            scroll: null
        },

        init() {
            this.detectReducedMotion();
            
            if (this.config.prefersReducedMotion) {
                this.disableAllMotion();
                return;
            }

            this.initPageEntrance();
            this.initScrollReveals();
            this.initListStaggers();
            this.initButtonInteractions();
            this.initInputEnhancements();
        },

        detectReducedMotion() {
            const mediaQuery = window.matchMedia('(prefers-reduced-motion: reduce)');
            this.config.prefersReducedMotion = mediaQuery.matches;

            mediaQuery.addEventListener('change', (e) => {
                this.config.prefersReducedMotion = e.matches;
                if (e.matches) {
                    this.disableAllMotion();
                }
            });
        },

        disableAllMotion() {
            document.querySelectorAll('[data-motion]').forEach(el => {
                el.classList.add('is-visible', 'in-view');
                el.style.opacity = '1';
                el.style.transform = 'none';
                el.style.transition = 'none';
            });

            document.querySelectorAll('[data-motion-stagger]').forEach(container => {
                Array.from(container.children).forEach(child => {
                    child.classList.add('is-visible');
                    child.style.opacity = '1';
                    child.style.transform = 'none';
                    child.style.transition = 'none';
                });
            });
        },

        initPageEntrance() {
            const elements = document.querySelectorAll('[data-motion="enter"]');
            
            elements.forEach(el => {
                const delay = parseInt(el.dataset.motionDelay || 0, 10);
                setTimeout(() => {
                    el.classList.add('is-visible');
                }, delay);
            });
        },

        initScrollReveals() {
            const elements = document.querySelectorAll('[data-motion="reveal"], [data-motion="reveal-scale"]');
            
            if (elements.length === 0) return;

            if (!('IntersectionObserver' in window)) {
                elements.forEach(el => el.classList.add('in-view'));
                return;
            }

            this.observers.scroll = new IntersectionObserver(
                (entries) => {
                    entries.forEach(entry => {
                        if (entry.isIntersecting) {
                            entry.target.classList.add('in-view');
                            this.observers.scroll.unobserve(entry.target);
                        }
                    });
                },
                {
                    threshold: this.config.scrollThreshold,
                    rootMargin: this.config.scrollRootMargin
                }
            );

            elements.forEach(el => this.observers.scroll.observe(el));
        },

        initListStaggers() {
            const containers = document.querySelectorAll('[data-motion-stagger]');
            
            containers.forEach(container => {
                this.animateStaggerContainer(container);
            });
        },

        animateStaggerContainer(container) {
            // Use MutationObserver to detect when content is added dynamically
            const observer = new MutationObserver(() => {
                const children = Array.from(container.children);
                
                // If container is emptied (e.g. before re-fetching), reset stagger-animated
                if (children.length === 0) {
                    container.classList.remove('stagger-animated');
                    return;
                }

                // If all children are already visible, nothing to do
                const unrevealed = children.filter(child => !child.classList.contains('is-visible'));
                if (unrevealed.length === 0) return;

                const delay = parseInt(container.dataset.motionStagger || this.config.staggerDelay, 10);
                const maxItems = parseInt(container.dataset.motionStaggerMax || this.config.maxStaggerItems, 10);

                if (!container.classList.contains('stagger-animated')) {
                    // Initial animation on first load
                    container.classList.add('stagger-animated');
                    container.classList.add('motion-ready');

                    setTimeout(() => {
                        children.slice(0, maxItems).forEach((child, i) => {
                            setTimeout(() => {
                                child.classList.add('is-visible');
                            }, i * delay);
                        });

                        if (children.length > maxItems) {
                            children.slice(maxItems).forEach(child => {
                                child.classList.add('is-visible');
                                child.style.opacity = '1';
                                child.style.transform = 'none';
                            });
                        }
                    }, 50);
                } else {
                    // For subsequent dynamic updates (filter applied, item added, pagination),
                    // reveal all new/unrevealed items immediately!
                    unrevealed.forEach(child => {
                        child.classList.add('is-visible');
                        child.style.opacity = '1';
                        child.style.transform = 'none';
                    });
                }
            });

            // Start observing
            observer.observe(container, { childList: true });

            // Also check immediately in case content is already there
            setTimeout(() => {
                const children = Array.from(container.children);
                if (children.length > 0 && !container.classList.contains('stagger-animated')) {
                    const delay = parseInt(container.dataset.motionStagger || this.config.staggerDelay, 10);
                    const maxItems = parseInt(container.dataset.motionStaggerMax || this.config.maxStaggerItems, 10);
                    
                    container.classList.add('stagger-animated');
                    container.classList.add('motion-ready');

                    setTimeout(() => {
                        children.slice(0, maxItems).forEach((child, i) => {
                            setTimeout(() => {
                                child.classList.add('is-visible');
                            }, i * delay);
                        });

                        if (children.length > maxItems) {
                            children.slice(maxItems).forEach(child => {
                                child.classList.add('is-visible');
                                child.style.opacity = '1';
                                child.style.transform = 'none';
                            });
                        }
                    }, 50);
                }
            }, 100);
        },

        initButtonInteractions() {
            const buttons = document.querySelectorAll('.btn:not(.btn-submit), .action-card');
            
            buttons.forEach(btn => {
                btn.addEventListener('pointerdown', (e) => {
                    if (!btn.disabled && !btn.classList.contains('is-loading')) {
                        btn.classList.add('is-pressed');
                    }
                });

                btn.addEventListener('pointerup', () => {
                    btn.classList.remove('is-pressed');
                });

                btn.addEventListener('pointerleave', () => {
                    btn.classList.remove('is-pressed');
                });
            });
        },

        initInputEnhancements() {
            const inputs = document.querySelectorAll('input:not(.auth-field input), select:not(.auth-field select), textarea:not(.auth-field textarea)');
            
            inputs.forEach(input => {
                input.addEventListener('focus', () => {
                    const wrapper = input.closest('.form-group, .filter-group, .select-group, .input-wrapper');
                    if (wrapper) wrapper.classList.add('is-focused');
                });

                input.addEventListener('blur', () => {
                    const wrapper = input.closest('.form-group, .filter-group, .select-group, .input-wrapper');
                    if (wrapper) wrapper.classList.remove('is-focused');
                });
            });
        },

        refresh() {
            this.initScrollReveals();
            this.initListStaggers();
        }
    };

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', () => FinoraMotion.init());
    } else {
        FinoraMotion.init();
    }

    if (typeof window !== 'undefined') {
        window.FinoraMotion = FinoraMotion;
    }

})();
