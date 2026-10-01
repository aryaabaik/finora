/**
 * Finora Custom Select Component
 * Replaces native <select> elements with an elegant custom dropdown.
 *
 * PORTAL PATTERN: dropdown is appended directly to <body> and positioned
 * with position:fixed, so it NEVER gets clipped by any parent container,
 * overflow:hidden, or stacking context.
 */
(function() {
    'use strict';

    const CHECKMARK_SVG = '<svg class="finora-select-check" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"></polyline></svg>';
    const CHEVRON_SVG = '<svg class="finora-select-chevron" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="6 9 12 15 18 9"></polyline></svg>';

    function initCustomSelect(select) {
        if (!select || select._finoraCustomSelect || select.hasAttribute('data-no-custom')) return;
        if (select.offsetParent === null && select.style.display === 'none') return;
        if (select.id === 'kategori-options-template') return;

        select._finoraCustomSelect = true;

        // Create container wrapper
        const wrapper = document.createElement('div');
        wrapper.className = 'finora-select-container';
        if (select.classList.contains('form-select')) wrapper.classList.add('is-form-select');

        // Insert wrapper before select and move select into it
        select.parentNode.insertBefore(wrapper, select);
        wrapper.appendChild(select);

        // Hide native select visually while keeping it accessible and form-submittable
        select.classList.add('finora-native-select-hidden');
        select.setAttribute('tabindex', '-1');
        select.setAttribute('aria-hidden', 'true');

        // Trigger button
        const trigger = document.createElement('button');
        trigger.type = 'button';
        trigger.className = 'finora-select-trigger';
        trigger.setAttribute('aria-haspopup', 'listbox');
        trigger.setAttribute('aria-expanded', 'false');

        const valueSpan = document.createElement('span');
        valueSpan.className = 'finora-select-label';

        const arrowSpan = document.createElement('span');
        arrowSpan.className = 'finora-select-arrow';
        arrowSpan.innerHTML = CHEVRON_SVG;

        trigger.appendChild(valueSpan);
        trigger.appendChild(arrowSpan);
        wrapper.appendChild(trigger);

        // ---- PORTAL: append dropdown directly to <body> ----
        const dropdown = document.createElement('div');
        dropdown.className = 'finora-select-dropdown finora-select-portal-dropdown';
        dropdown.setAttribute('role', 'listbox');

        const optionsList = document.createElement('div');
        optionsList.className = 'finora-select-options-list';
        dropdown.appendChild(optionsList);

        // Append to BODY, not to wrapper, so z-index is always on top
        document.body.appendChild(dropdown);

        // Store reference for cross-close
        wrapper._finoraDropdown = dropdown;

        function renderOptions() {
            optionsList.innerHTML = '';
            const options = Array.from(select.options);
            const selectedIndex = select.selectedIndex >= 0 ? select.selectedIndex : 0;
            const currentSelectedOption = options[selectedIndex];

            if (options.length === 0) {
                valueSpan.textContent = 'Pilih...';
                valueSpan.classList.add('is-placeholder');
                const emptyItem = document.createElement('div');
                emptyItem.className = 'finora-select-option is-empty';
                emptyItem.textContent = 'Tidak ada pilihan';
                emptyItem.style.color = 'var(--warm-gray)';
                emptyItem.style.cursor = 'default';
                optionsList.appendChild(emptyItem);
                return;
            }

            if (currentSelectedOption) {
                valueSpan.textContent = currentSelectedOption.text;
                if (!currentSelectedOption.value && currentSelectedOption.text.toLowerCase().includes('pilih')) {
                    valueSpan.classList.add('is-placeholder');
                } else {
                    valueSpan.classList.remove('is-placeholder');
                }
            } else {
                valueSpan.textContent = '';
            }

            options.forEach((opt, idx) => {
                const optItem = document.createElement('div');
                optItem.className = 'finora-select-option';
                optItem.setAttribute('role', 'option');
                optItem.setAttribute('data-value', opt.value);
                optItem.setAttribute('data-index', idx);

                const isSelected = idx === selectedIndex;
                if (isSelected) {
                    optItem.classList.add('is-selected');
                    optItem.setAttribute('aria-selected', 'true');
                }

                const optText = document.createElement('span');
                optText.className = 'finora-select-option-text';
                optText.textContent = opt.text;
                optItem.appendChild(optText);

                if (isSelected) {
                    const checkSpan = document.createElement('span');
                    checkSpan.className = 'finora-select-option-check';
                    checkSpan.innerHTML = CHECKMARK_SVG;
                    optItem.appendChild(checkSpan);
                }

                // Use mousedown so it fires before blur closes dropdown
                optItem.addEventListener('mousedown', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    selectOption(idx);
                    closeDropdown();
                });

                optionsList.appendChild(optItem);
            });
        }

        function selectOption(index) {
            if (select.selectedIndex !== index) {
                select.selectedIndex = index;
                select.dispatchEvent(new Event('change', { bubbles: true }));
                select.dispatchEvent(new Event('input', { bubbles: true }));
            }
            renderOptions();
        }

        function positionDropdown() {
            const rect = trigger.getBoundingClientRect();
            const viewportH = window.innerHeight;
            const viewportW = window.innerWidth;

            // Temporarily show (hidden) to measure dimensions
            dropdown.style.visibility = 'hidden';
            dropdown.style.opacity = '0';
            dropdown.style.display = 'block';
            dropdown.style.position = 'fixed';
            dropdown.style.top = '-9999px';
            dropdown.style.left = '-9999px';
            dropdown.style.width = 'max-content';
            dropdown.style.minWidth = rect.width + 'px';
            dropdown.style.maxWidth = Math.min(360, viewportW - 16) + 'px';

            const ddW = dropdown.offsetWidth;
            const ddH = dropdown.offsetHeight;

            dropdown.style.display = '';
            dropdown.style.visibility = '';
            dropdown.style.opacity = '';

            // Decide drop direction
            const spaceBelow = viewportH - rect.bottom - 8;
            const spaceAbove = rect.top - 8;
            const isDropup = spaceBelow < ddH && spaceAbove > spaceBelow;

            let top = isDropup ? (rect.top - ddH - 4) : (rect.bottom + 4);
            let left = rect.left;

            // Clamp to viewport edges
            if (left + ddW > viewportW - 8) left = viewportW - ddW - 8;
            if (left < 8) left = 8;

            dropdown.style.position = 'fixed';
            dropdown.style.top = top + 'px';
            dropdown.style.left = left + 'px';
            dropdown.style.width = ddW + 'px';
            dropdown.style.minWidth = '';
            dropdown.style.maxWidth = '';

            wrapper.classList.toggle('is-dropup', isDropup);
        }

        function toggleDropdown(e) {
            e.preventDefault();
            e.stopPropagation();
            if (wrapper.classList.contains('is-open')) {
                closeDropdown();
            } else {
                openDropdown();
            }
        }

        function openDropdown() {
            // Close all other open finora selects first
            document.querySelectorAll('.finora-select-container.is-open').forEach(el => {
                if (el !== wrapper) {
                    el.classList.remove('is-open', 'is-dropup');
                    const tr = el.querySelector('.finora-select-trigger');
                    if (tr) tr.setAttribute('aria-expanded', 'false');
                    if (el._finoraDropdown) el._finoraDropdown.classList.remove('is-open');
                }
            });

            positionDropdown();

            wrapper.classList.add('is-open');
            trigger.setAttribute('aria-expanded', 'true');
            dropdown.classList.add('is-open');

            // Scroll selected option into view
            const selectedOpt = optionsList.querySelector('.finora-select-option.is-selected');
            if (selectedOpt) {
                selectedOpt.scrollIntoView({ block: 'nearest' });
            }

            // Reposition on scroll or resize while open
            window.addEventListener('scroll', positionDropdown, { passive: true, capture: true });
            window.addEventListener('resize', positionDropdown, { passive: true });
        }

        function closeDropdown() {
            wrapper.classList.remove('is-open', 'is-dropup');
            trigger.setAttribute('aria-expanded', 'false');
            dropdown.classList.remove('is-open');
            window.removeEventListener('scroll', positionDropdown, { capture: true });
            window.removeEventListener('resize', positionDropdown);
        }

        trigger.addEventListener('click', toggleDropdown);

        // Keyboard navigation
        trigger.addEventListener('keydown', function(e) {
            if (e.key === 'Enter' || e.key === ' ' || e.key === 'ArrowDown') {
                e.preventDefault();
                if (!wrapper.classList.contains('is-open')) {
                    openDropdown();
                } else if (e.key === 'ArrowDown') {
                    navigateOption(1);
                }
            } else if (e.key === 'ArrowUp' && wrapper.classList.contains('is-open')) {
                e.preventDefault();
                navigateOption(-1);
            } else if (e.key === 'Escape') {
                closeDropdown();
                trigger.focus();
            }
        });

        function navigateOption(direction) {
            const nextIdx = Math.max(0, Math.min(select.options.length - 1, select.selectedIndex + direction));
            selectOption(nextIdx);
        }

        // Intercept native select value setter so programmatic changes update trigger
        const descriptor = Object.getOwnPropertyDescriptor(HTMLSelectElement.prototype, 'value');
        if (descriptor && descriptor.set) {
            const originalSet = descriptor.set;
            Object.defineProperty(select, 'value', {
                get: function() {
                    return descriptor.get.call(this);
                },
                set: function(val) {
                    originalSet.call(this, val);
                    renderOptions();
                }
            });
        }

        // Observe DOM changes on <select> (options added/removed)
        const observer = new MutationObserver(function() {
            renderOptions();
        });
        observer.observe(select, { childList: true, subtree: true, attributes: true });

        // Listen for change events from external code
        select.addEventListener('change', renderOptions);

        // Listen for parent form reset
        const parentForm = select.closest('form');
        if (parentForm) {
            parentForm.addEventListener('reset', function() {
                setTimeout(renderOptions, 10);
            });
        }

        // Initial render
        renderOptions();
    }

    // Close on mousedown outside (mousedown fires before blur)
    document.addEventListener('mousedown', function(e) {
        const inWrapper  = e.target.closest('.finora-select-container');
        const inDropdown = e.target.closest('.finora-select-portal-dropdown');
        if (!inWrapper && !inDropdown) {
            document.querySelectorAll('.finora-select-container.is-open').forEach(el => {
                el.classList.remove('is-open', 'is-dropup');
                const tr = el.querySelector('.finora-select-trigger');
                if (tr) tr.setAttribute('aria-expanded', 'false');
                if (el._finoraDropdown) el._finoraDropdown.classList.remove('is-open');
            });
        }
    });

    // Close on Escape
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            document.querySelectorAll('.finora-select-container.is-open').forEach(el => {
                el.classList.remove('is-open', 'is-dropup');
                const tr = el.querySelector('.finora-select-trigger');
                if (tr) tr.setAttribute('aria-expanded', 'false');
                if (el._finoraDropdown) el._finoraDropdown.classList.remove('is-open');
            });
        }
    });

    // Auto-init all eligible selects on page
    function initAll() {
        const selects = document.querySelectorAll('select:not([data-no-custom])');
        selects.forEach(initCustomSelect);
    }

    // Also watch for dynamically added selects
    const bodyObserver = new MutationObserver(function(mutations) {
        for (const mut of mutations) {
            if (mut.addedNodes.length) {
                mut.addedNodes.forEach(node => {
                    if (node.nodeType === Node.ELEMENT_NODE) {
                        if (node.tagName === 'SELECT') {
                            initCustomSelect(node);
                        } else {
                            node.querySelectorAll && node.querySelectorAll('select:not([data-no-custom])').forEach(initCustomSelect);
                        }
                    }
                });
            }
        }
    });

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', function() {
            initAll();
            bodyObserver.observe(document.body, { childList: true, subtree: true });
        });
    } else {
        initAll();
        bodyObserver.observe(document.body, { childList: true, subtree: true });
    }

    // Expose for manual calls if needed
    window.FinoraSelect = {
        init: initCustomSelect,
        initAll: initAll
    };
})();
