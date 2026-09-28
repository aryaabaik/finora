/**
 * Finora Navigation Manager
 * Handles bottom navigation (mobile), desktop sidebar, and active states
 */

(function() {
    'use strict';

    const CONFIG = {
        mobileBreakpoint: 768,
        activeClass: 'active',
        dataActiveAttr: 'data-active'
    };

    let state = {
        isMobile: window.innerWidth <= CONFIG.mobileBreakpoint,
        prefersReducedMotion: window.matchMedia('(prefers-reduced-motion: reduce)').matches,
        currentPage: null
    };

    let elements = {};

    function init() {
        cacheElements();
        detectCurrentPage();
        bindEvents();
        setInitialActiveState();
        handleResize();
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

        elements.desktopMenuItems.forEach(item => {
            const itemPage = item.getAttribute('data-active');
            item.classList.toggle(CONFIG.activeClass, itemPage === state.currentPage);
        });

        elements.drawerNavItems.forEach(item => {
            const itemPage = item.getAttribute('data-active');
            item.classList.toggle(CONFIG.activeClass, itemPage === state.currentPage);
        });

        elements.bottomNavItems.forEach(item => {
            const itemPage = item.getAttribute('data-active');
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

        elements.drawerNavItems.forEach(item => {
            item.addEventListener('click', handleDrawerNavClick);
        });

        elements.bottomNavItems.forEach(item => {
            item.addEventListener('click', handleBottomNavClick);
        });

        document.addEventListener('keydown', handleKeyDown);

        window.addEventListener('resize', debounce(handleResize, 150));

        document.addEventListener('click', handleNavLinkClick);
    }

    function handleBottomNavClick(event) {
        event.stopPropagation();
        const link = event.currentTarget;
        const page = link.getAttribute('data-active');

        if (state.isMobile) {
            closeDrawer();
        }

        updateActiveState(page);
    }

    function handleDrawerNavClick(event) {
        const link = event.currentTarget;
        const href = link.getAttribute('href');

        if (href === '#' || link.target === '_blank' || link.rel.includes('external')) {
            return;
        }

        if (state.isMobile) {
            closeDrawer();
        }

        updateActiveState(link.getAttribute('data-active'));
    }

    function handleNavLinkClick(event) {
        const link = event.target.closest('a');
        if (!link) return;
        if (link.closest('.bottom-nav')) return;

        const dataActive = link.getAttribute('data-active');
        if (dataActive) {
            updateActiveState(dataActive);
        }
    }

    function updateActiveState(page) {
        state.currentPage = page;

        if (elements.sidebar) {
            elements.sidebar.setAttribute(CONFIG.dataActiveAttr, page);
        }

        elements.desktopMenuItems.forEach(item => {
            const itemPage = item.getAttribute('data-active');
            item.classList.toggle(CONFIG.activeClass, itemPage === page);
        });

        elements.drawerNavItems.forEach(item => {
            const itemPage = item.getAttribute('data-active');
            item.classList.toggle(CONFIG.activeClass, itemPage === page);
        });

        elements.bottomNavItems.forEach(item => {
            const itemPage = item.getAttribute('data-active');
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

    let isDrawerOpen = false;

    function openDrawer() {
        if (isDrawerOpen) return;
        isDrawerOpen = true;

        if (elements.hamburger) {
            elements.hamburger.setAttribute('aria-expanded', 'true');
        }

        if (elements.drawer) elements.drawer.classList.add('is-open');
        if (elements.drawerOverlay) elements.drawerOverlay.classList.add('is-open');
        elements.body.classList.add('drawer-open');
    }

    function closeDrawer() {
        if (!isDrawerOpen) return;
        isDrawerOpen = false;

        if (elements.hamburger) {
            elements.hamburger.setAttribute('aria-expanded', 'false');
        }

        if (elements.drawer) elements.drawer.classList.remove('is-open');
        if (elements.drawerOverlay) elements.drawerOverlay.classList.remove('is-open');
        elements.body.classList.remove('drawer-open');
    }

    function handleKeyDown(event) {
        if (event.key === 'Escape' && isDrawerOpen) {
            closeDrawer();
        }
    }

    function handleResize() {
        const wasMobile = state.isMobile;
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
        const hasBottomNav = !!document.querySelector('.bottom-nav');
        if (state.isMobile && hasBottomNav) {
            elements.body.classList.add('has-bottom-nav');
        } else {
            elements.body.classList.remove('has-bottom-nav');
        }
    }

    function debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }

    const Navigation = {
        init,
        openDrawer,
        closeDrawer,
        toggleDrawer,
        updateActiveState: (page) => updateActiveState(page),
        isDrawerOpen: () => isDrawerOpen,
        isMobile: () => state.isMobile,
        getCurrentPage: () => state.currentPage
    };

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

    if (typeof window !== 'undefined') {
        window.FinoraNavigation = Navigation;
    }

})();
