/**
 * Finora Navigation Manager
 * Handles bottom navigation (mobile), desktop sidebar, and active states
 */

(function() {
    'use strict';

    const CONFIG = {
        mobileBreakpoint: 768,
        activeClass: 'active',
        dataActiveAttr: 'data-active',
        // Must match CSS .drawer.is-closing transition-duration (260ms) + item exit (80ms)
        drawerCloseDuration: 265
    };

    let state = {
        isMobile: window.innerWidth <= CONFIG.mobileBreakpoint,
        prefersReducedMotion: window.matchMedia('(prefers-reduced-motion: reduce)').matches,
        currentPage: null
    };

    let elements = {};

    // Guard against race conditions between open/close
    let isDrawerOpen = false;
    let _closeTimer = null;

    function init() {
        cacheElements();
        detectCurrentPage();
        bindEvents();
        setInitialActiveState();
        handleResize();
        initPageEntrance();
    }

    function cacheElements() {
        elements = {
            hamburger: document.getElementById('hamburger'),
            drawerOverlay: document.getElementById('drawer-overlay'),
            drawer: document.getElementById('drawer'),
            drawerClose: document.getElementById('drawer-close'),
            drawerNavItems: document.querySelectorAll('.drawer-nav-item'),
            bottomNavItems: document.querySelectorAll('.bottom-nav-item'),
            desktopMenuItems: document.querySelectorAll('.menu-item'),
            body: document.body,
            sidebar: document.querySelector('.sidebar'),
            mobileHeader: document.querySelector('.mobile-header')
        };

        state.isMobile = !elements.sidebar || window.innerWidth <= CONFIG.mobileBreakpoint;
    }

    function detectCurrentPage() {
        const path = window.location.pathname;
        let page = 'dashboard';

        if (path.includes('/pemasukan')) page = 'pemasukan';
        else if (path.includes('/pengeluaran')) page = 'pengeluaran';
        else if (path.includes('/kategori')) page = 'kategori';
        else if (path.includes('/template')) page = 'template';
        else if (path.includes('/laporan')) page = 'laporan';
        else if (path.includes('/auth/settings') || path.includes('/settings')) page = 'settings';

        state.currentPage = page;
    }

    function setInitialActiveState() {
        if (elements.sidebar) {
            elements.sidebar.setAttribute(CONFIG.dataActiveAttr, state.currentPage);
        }

        elements.desktopMenuItems.forEach(function(item) {
            var itemPage = item.getAttribute('data-active');
            item.classList.toggle(CONFIG.activeClass, itemPage === state.currentPage);
        });

        elements.drawerNavItems.forEach(function(item) {
            var itemPage = item.getAttribute('data-active');
            item.classList.toggle(CONFIG.activeClass, itemPage === state.currentPage);
        });

        elements.bottomNavItems.forEach(function(item) {
            var itemPage = item.getAttribute('data-active');
            item.classList.toggle(CONFIG.activeClass, itemPage === state.currentPage);
        });
    }

    function bindEvents() {
        if (elements.hamburger) {
            elements.hamburger.addEventListener('click', toggleDrawer);
        }

        if (elements.drawerClose) {
            elements.drawerClose.addEventListener('click', closeDrawer);
        }

        if (elements.drawerOverlay) {
            elements.drawerOverlay.addEventListener('click', closeDrawer);
        }

        elements.drawerNavItems.forEach(function(item) {
            item.addEventListener('click', handleDrawerNavClick);
        });

        elements.bottomNavItems.forEach(function(item) {
            item.addEventListener('click', handleBottomNavClick);
        });

        document.addEventListener('keydown', handleKeyDown);
        window.addEventListener('resize', debounce(handleResize, 150));
        document.addEventListener('click', handleNavLinkClick);
    }

    function handleBottomNavClick(event) {
        event.stopPropagation();
        var link = event.currentTarget;
        var page = link.getAttribute('data-active');

        if (state.isMobile) {
            closeDrawer();
        }

        updateActiveState(page);
    }

    function handleDrawerNavClick(event) {
        var link = event.currentTarget;
        var href = link.getAttribute('href');

        if (href === '#' || link.target === '_blank' || link.rel.includes('external')) {
            return;
        }

        if (state.isMobile) {
            closeDrawer();
        }

        updateActiveState(link.getAttribute('data-active'));
    }

    function handleNavLinkClick(event) {
        var link = event.target.closest('a');
        if (!link) return;
        if (link.closest('.bottom-nav')) return;

        var dataActive = link.getAttribute('data-active');
        if (dataActive) {
            updateActiveState(dataActive);
        }
    }

    function updateActiveState(page) {
        state.currentPage = page;

        if (elements.sidebar) {
            elements.sidebar.setAttribute(CONFIG.dataActiveAttr, page);
        }

        elements.desktopMenuItems.forEach(function(item) {
            var itemPage = item.getAttribute('data-active');
            item.classList.toggle(CONFIG.activeClass, itemPage === page);
        });

        elements.drawerNavItems.forEach(function(item) {
            var itemPage = item.getAttribute('data-active');
            item.classList.toggle(CONFIG.activeClass, itemPage === page);
        });

        elements.bottomNavItems.forEach(function(item) {
            var itemPage = item.getAttribute('data-active');
            item.classList.toggle(CONFIG.activeClass, itemPage === page);
        });
    }

    function toggleDrawer() {
        if (isDrawerOpen) {
            closeDrawer();
        } else {
            openDrawer();
        }
    }

    function openDrawer() {
        // If a close animation is in progress, cancel it and reopen cleanly
        if (_closeTimer !== null) {
            clearTimeout(_closeTimer);
            _closeTimer = null;
            _finishClose(false);
        }

        if (isDrawerOpen) return;
        isDrawerOpen = true;

        if (elements.hamburger) {
            elements.hamburger.setAttribute('aria-expanded', 'true');
        }

        // Show overlay via visibility+opacity (CSS handles fade, no display flash)
        if (elements.drawerOverlay) {
            elements.drawerOverlay.classList.remove('is-closing');
            elements.drawerOverlay.classList.add('is-open');
        }

        // Slide drawer in
        if (elements.drawer) {
            elements.drawer.classList.remove('is-closing');
            elements.drawer.classList.add('is-open');
        }

        elements.body.classList.add('drawer-open');

        // Stagger drawer nav items entrance
        if (!state.prefersReducedMotion && elements.drawerNavItems.length) {
            elements.drawerNavItems.forEach(function(item, i) {
                item.style.opacity = '0';
                item.style.transform = 'translateX(-8px)';
                // Start after drawer begins to slide in (~60ms offset)
                setTimeout(function() {
                    item.style.transition = 'opacity 230ms cubic-bezier(0.16,1,0.3,1), transform 230ms cubic-bezier(0.22,1,0.36,1)';
                    item.style.opacity = '1';
                    item.style.transform = 'translateX(0)';
                }, 60 + i * 38);
            });
        }
    }

    function closeDrawer() {
        if (!isDrawerOpen) return;

        // Prevent double-close race condition
        if (_closeTimer !== null) return;

        isDrawerOpen = false;

        if (elements.hamburger) {
            elements.hamburger.setAttribute('aria-expanded', 'false');
        }

        if (!state.prefersReducedMotion) {
            // Subtly fade nav items out before drawer slides away
            if (elements.drawerNavItems.length) {
                elements.drawerNavItems.forEach(function(item) {
                    item.style.transition = 'opacity 140ms ease, transform 140ms ease';
                    item.style.opacity = '0';
                    item.style.transform = 'translateX(-4px)';
                });
            }

            // After brief item exit, start drawer slide-out
            setTimeout(function() {
                if (elements.drawer) {
                    elements.drawer.classList.remove('is-open');
                    elements.drawer.classList.add('is-closing');
                }
                if (elements.drawerOverlay) {
                    elements.drawerOverlay.classList.remove('is-open');
                    elements.drawerOverlay.classList.add('is-closing');
                }
            }, 80);

            // Clean up after the full animation completes
            _closeTimer = setTimeout(function() {
                _finishClose(true);
            }, CONFIG.drawerCloseDuration + 80);

        } else {
            // Reduced motion: instant close, preserve full functionality
            _finishClose(true);
        }
    }

    /**
     * Final cleanup after drawer close animation finishes.
     * @param {boolean} resetItems - Whether to reset nav item inline styles
     */
    function _finishClose(resetItems) {
        _closeTimer = null;

        if (elements.drawer) {
            elements.drawer.classList.remove('is-open', 'is-closing');
        }
        if (elements.drawerOverlay) {
            elements.drawerOverlay.classList.remove('is-open', 'is-closing');
        }

        elements.body.classList.remove('drawer-open');

        if (resetItems && elements.drawerNavItems.length) {
            elements.drawerNavItems.forEach(function(item) {
                item.style.opacity = '';
                item.style.transform = '';
                item.style.transition = '';
            });
        }
    }

    function handleKeyDown(event) {
        if (event.key === 'Escape' && isDrawerOpen) {
            closeDrawer();
        }
    }

    function handleResize() {
        var wasMobile = state.isMobile;
        state.isMobile = window.innerWidth <= CONFIG.mobileBreakpoint;

        if (wasMobile && !state.isMobile && isDrawerOpen) {
            closeDrawer();
        }

        if (!wasMobile && state.isMobile && isDrawerOpen) {
            closeDrawer();
        }

        addBodyClass();

        if (elements.mobileHeader) {
            elements.mobileHeader.style.display = state.isMobile ? 'block' : 'none';
        }
    }

    function addBodyClass() {
        var hasBottomNav = !!document.querySelector('.bottom-nav');
        if (state.isMobile && hasBottomNav) {
            elements.body.classList.add('has-bottom-nav');
        } else {
            elements.body.classList.remove('has-bottom-nav');
        }
    }

    /**
     * Page entrance transition: subtle 240ms fade + 6px upward lift on page load.
     * Applied via CSS classes. Does NOT intercept links, forms, or browser nav.
     */
    function initPageEntrance() {
        if (state.prefersReducedMotion) return;

        var target = document.querySelector('.main, .main-content, [data-page-content]');
        if (!target) return;

        // Skip on back/forward navigation
        var navEntries = performance.getEntriesByType('navigation');
        if (navEntries.length && navEntries[0].type === 'back_forward') return;

        target.classList.add('page-enter');

        // Double rAF ensures initial paint with .page-enter before transition starts
        requestAnimationFrame(function() {
            requestAnimationFrame(function() {
                target.classList.add('page-enter-active');
                target.classList.remove('page-enter');
            });
        });

        // Clean up transition classes after animation ends
        target.addEventListener('transitionend', function cleanup(e) {
            if (e.propertyName === 'opacity') {
                target.classList.remove('page-enter-active');
                target.removeEventListener('transitionend', cleanup);
            }
        });
    }

    function debounce(func, wait) {
        var timeout;
        return function executedFunction() {
            var args = arguments;
            var later = function() {
                clearTimeout(timeout);
                func.apply(this, args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }

    var Navigation = {
        init: init,
        openDrawer: openDrawer,
        closeDrawer: closeDrawer,
        toggleDrawer: toggleDrawer,
        updateActiveState: function(page) { updateActiveState(page); },
        isDrawerOpen: function() { return isDrawerOpen; },
        isMobile: function() { return state.isMobile; },
        getCurrentPage: function() { return state.currentPage; }
    };

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

    // Safeguard: Ensure Finora Custom Select & Date Picker are active across all pages
    if (typeof window !== 'undefined') {
        if (!window.FinoraSelect && !document.querySelector('script[src*="custom-select.js"]')) {
            var csScript = document.createElement('script');
            csScript.src = '/js/custom-select.js';
            document.head.appendChild(csScript);
        }
        if (!window.FinoraDatePicker && !document.querySelector('script[src*="custom-datepicker.js"]')) {
            var dpScript = document.createElement('script');
            dpScript.src = '/js/custom-datepicker.js';
            document.head.appendChild(dpScript);
        }
    }

    if (typeof window !== 'undefined') {
        window.FinoraNavigation = Navigation;
    }

})();
