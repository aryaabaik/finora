/**
 * Finora Custom Date Picker Component
 * Replaces native <input type="date"> elements with an elegant, responsive in-page calendar
 * that matches Finora's paper & kraft aesthetic and stays cleanly within mobile screens.
 */
(function() {
    'use strict';

    const MONTH_NAMES = [
        'Januari', 'Februari', 'Maret', 'April', 'Mei', 'Juni',
        'Juli', 'Agustus', 'September', 'Oktober', 'November', 'Desember'
    ];
    const MONTH_NAMES_SHORT = [
        'Jan', 'Feb', 'Mar', 'Apr', 'Mei', 'Jun',
        'Jul', 'Ags', 'Sep', 'Okt', 'Nov', 'Des'
    ];
    const DAY_NAMES = ['Min', 'Sen', 'Sel', 'Rab', 'Kam', 'Jum', 'Sab'];

    const CALENDAR_ICON_SVG = '<svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect><line x1="16" y1="2" x2="16" y2="6"></line><line x1="8" y1="2" x2="8" y2="6"></line><line x1="3" y1="10" x2="21" y2="10"></line></svg>';
    const CHEVRON_LEFT_SVG = '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="15 18 9 12 15 6"></polyline></svg>';
    const CHEVRON_RIGHT_SVG = '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="9 18 15 12 9 6"></polyline></svg>';

    function padZero(num) {
        return num < 10 ? '0' + num : String(num);
    }

    function formatDateToYMD(year, month, day) {
        return `${year}-${padZero(month + 1)}-${padZero(day)}`;
    }

    function parseYMD(str) {
        if (!str || typeof str !== 'string') return null;
        const parts = str.trim().split('-');
        if (parts.length !== 3) return null;
        const y = parseInt(parts[0], 10);
        const m = parseInt(parts[1], 10) - 1;
        const d = parseInt(parts[2], 10);
        if (isNaN(y) || isNaN(m) || isNaN(d)) return null;
        return { year: y, month: m, day: d };
    }

    function formatDisplayDate(ymd) {
        if (!ymd) return '';
        return `${ymd.day} ${MONTH_NAMES_SHORT[ymd.month]} ${ymd.year}`;
    }

    function initCustomDatePicker(input) {
        if (!input || input._finoraCustomDatePicker || input.hasAttribute('data-no-custom')) return;
        if (input.type !== 'date') return;

        input._finoraCustomDatePicker = true;

        // Container wrapper
        const wrapper = document.createElement('div');
        wrapper.className = 'finora-datepicker-container';

        // Keep parent layout class if any
        if (input.classList.contains('filter-date-input')) {
            wrapper.classList.add('is-filter-date');
        }

        // Insert wrapper before input and place input inside
        input.parentNode.insertBefore(wrapper, input);
        wrapper.appendChild(input);

        // Hide native date input visually while keeping it fully functional for forms & scripts
        input.classList.add('finora-native-date-hidden');
        input.setAttribute('tabindex', '-1');
        input.setAttribute('aria-hidden', 'true');

        // Trigger button
        const trigger = document.createElement('button');
        trigger.type = 'button';
        trigger.className = 'finora-datepicker-trigger';
        trigger.setAttribute('aria-haspopup', 'dialog');
        trigger.setAttribute('aria-expanded', 'false');

        const labelSpan = document.createElement('span');
        labelSpan.className = 'finora-datepicker-label';

        const iconSpan = document.createElement('span');
        iconSpan.className = 'finora-datepicker-icon';
        iconSpan.innerHTML = CALENDAR_ICON_SVG;

        trigger.appendChild(labelSpan);
        trigger.appendChild(iconSpan);
        wrapper.appendChild(trigger);

        // Dropdown calendar dialog
        const dropdown = document.createElement('div');
        dropdown.className = 'finora-datepicker-dropdown';
        dropdown.setAttribute('role', 'dialog');
        dropdown.setAttribute('aria-modal', 'true');

        // Header (Prev, Month Year Title, Next)
        const header = document.createElement('div');
        header.className = 'finora-dp-header';

        const prevBtn = document.createElement('button');
        prevBtn.type = 'button';
        prevBtn.className = 'finora-dp-nav-btn prev';
        prevBtn.innerHTML = CHEVRON_LEFT_SVG;
        prevBtn.setAttribute('aria-label', 'Bulan sebelumnya');

        const titleDiv = document.createElement('div');
        titleDiv.className = 'finora-dp-title';

        const nextBtn = document.createElement('button');
        nextBtn.type = 'button';
        nextBtn.className = 'finora-dp-nav-btn next';
        nextBtn.innerHTML = CHEVRON_RIGHT_SVG;
        nextBtn.setAttribute('aria-label', 'Bulan berikutnya');

        header.appendChild(prevBtn);
        header.appendChild(titleDiv);
        header.appendChild(nextBtn);
        dropdown.appendChild(header);

        // Weekday headers
        const weekdaysDiv = document.createElement('div');
        weekdaysDiv.className = 'finora-dp-weekdays';
        DAY_NAMES.forEach(day => {
            const dayEl = document.createElement('span');
            dayEl.textContent = day;
            weekdaysDiv.appendChild(dayEl);
        });
        dropdown.appendChild(weekdaysDiv);

        // Days Grid
        const gridDiv = document.createElement('div');
        gridDiv.className = 'finora-dp-grid';
        dropdown.appendChild(gridDiv);

        // Footer (Hari Ini, Hapus)
        const footer = document.createElement('div');
        footer.className = 'finora-dp-footer';

        const todayBtn = document.createElement('button');
        todayBtn.type = 'button';
        todayBtn.className = 'finora-dp-footer-btn today';
        todayBtn.textContent = 'Hari Ini';

        const clearBtn = document.createElement('button');
        clearBtn.type = 'button';
        clearBtn.className = 'finora-dp-footer-btn clear';
        clearBtn.textContent = 'Hapus';

        footer.appendChild(clearBtn);
        footer.appendChild(todayBtn);
        dropdown.appendChild(footer);

        // ---- PORTAL PATTERN: mount directly to <body> so width is never squished by parent ----
        document.body.appendChild(dropdown);
        wrapper._finoraDpDropdown = dropdown;

        // Internal State
        const todayDate = new Date();
        const todayYMD = {
            year: todayDate.getFullYear(),
            month: todayDate.getMonth(),
            day: todayDate.getDate()
        };

        let viewYear = todayYMD.year;
        let viewMonth = todayYMD.month;

        function updateFromInput() {
            const parsed = parseYMD(input.value);
            if (parsed) {
                labelSpan.textContent = formatDisplayDate(parsed);
                labelSpan.classList.remove('is-placeholder');
                viewYear = parsed.year;
                viewMonth = parsed.month;
            } else {
                labelSpan.textContent = input.placeholder || 'Pilih tanggal';
                labelSpan.classList.add('is-placeholder');
            }
            renderCalendar();
        }

        function setDateValue(year, month, day) {
            const ymdStr = formatDateToYMD(year, month, day);
            if (input.value !== ymdStr) {
                input.value = ymdStr;
                input.dispatchEvent(new Event('change', { bubbles: true }));
                input.dispatchEvent(new Event('input', { bubbles: true }));
            }
            updateFromInput();
            closeDropdown();
        }

        function clearDateValue() {
            if (input.value !== '') {
                input.value = '';
                input.dispatchEvent(new Event('change', { bubbles: true }));
                input.dispatchEvent(new Event('input', { bubbles: true }));
            }
            updateFromInput();
            closeDropdown();
        }

        function renderCalendar() {
            titleDiv.textContent = `${MONTH_NAMES[viewMonth]} ${viewYear}`;
            gridDiv.innerHTML = '';

            const selectedYMD = parseYMD(input.value);

            // First day of month index (0: Sunday, 1: Monday, ...)
            const firstDayIndex = new Date(viewYear, viewMonth, 1).getDay();
            // Number of days in current month
            const daysInMonth = new Date(viewYear, viewMonth + 1, 0).getDate();
            // Days in previous month
            const prevMonthDays = new Date(viewYear, viewMonth, 0).getDate();

            // Previous month padding days
            for (let i = firstDayIndex - 1; i >= 0; i--) {
                const dayNum = prevMonthDays - i;
                const cell = document.createElement('button');
                cell.type = 'button';
                cell.className = 'finora-dp-day is-other-month';
                cell.textContent = dayNum;
                cell.addEventListener('click', function(e) {
                    e.stopPropagation();
                    const prevM = viewMonth === 0 ? 11 : viewMonth - 1;
                    const prevY = viewMonth === 0 ? viewYear - 1 : viewYear;
                    setDateValue(prevY, prevM, dayNum);
                });
                gridDiv.appendChild(cell);
            }

            // Current month days
            for (let d = 1; d <= daysInMonth; d++) {
                const cell = document.createElement('button');
                cell.type = 'button';
                cell.className = 'finora-dp-day';
                cell.textContent = d;

                const isToday = (viewYear === todayYMD.year && viewMonth === todayYMD.month && d === todayYMD.day);
                if (isToday) {
                    cell.classList.add('is-today');
                }

                const isSelected = selectedYMD && (viewYear === selectedYMD.year && viewMonth === selectedYMD.month && d === selectedYMD.day);
                if (isSelected) {
                    cell.classList.add('is-selected');
                }

                cell.addEventListener('click', function(e) {
                    e.stopPropagation();
                    setDateValue(viewYear, viewMonth, d);
                });

                gridDiv.appendChild(cell);
            }

            // Next month padding days to complete 35 or 42 grid cells
            const totalRendered = firstDayIndex + daysInMonth;
            const remaining = (totalRendered % 7 === 0) ? 0 : 7 - (totalRendered % 7);
            for (let nextD = 1; nextD <= remaining; nextD++) {
                const cell = document.createElement('button');
                cell.type = 'button';
                cell.className = 'finora-dp-day is-other-month';
                cell.textContent = nextD;
                cell.addEventListener('click', function(e) {
                    e.stopPropagation();
                    const nextM = viewMonth === 11 ? 0 : viewMonth + 1;
                    const nextY = viewMonth === 11 ? viewYear + 1 : viewYear;
                    setDateValue(nextY, nextM, nextD);
                });
                gridDiv.appendChild(cell);
            }
        }

        prevBtn.addEventListener('click', function(e) {
            e.stopPropagation();
            if (viewMonth === 0) {
                viewMonth = 11;
                viewYear--;
            } else {
                viewMonth--;
            }
            renderCalendar();
        });

        nextBtn.addEventListener('click', function(e) {
            e.stopPropagation();
            if (viewMonth === 11) {
                viewMonth = 0;
                viewYear++;
            } else {
                viewMonth++;
            }
            renderCalendar();
        });

        todayBtn.addEventListener('click', function(e) {
            e.stopPropagation();
            setDateValue(todayYMD.year, todayYMD.month, todayYMD.day);
        });

        clearBtn.addEventListener('click', function(e) {
            e.stopPropagation();
            clearDateValue();
        });

        function toggleDropdown(e) {
            e.preventDefault();
            e.stopPropagation();
            if (wrapper.classList.contains('is-open')) {
                closeDropdown();
            } else {
                openDropdown();
            }
        }

        function positionDropdown() {
            const rect = trigger.getBoundingClientRect();
            const viewportH = window.innerHeight;
            const viewportW = window.innerWidth;
            const dpW = 286;
            const dpH = 320;

            const spaceBelow = viewportH - rect.bottom - 8;
            const spaceAbove = rect.top - 8;
            const isDropup = spaceBelow < dpH && spaceAbove > spaceBelow;

            let top = isDropup ? (rect.top - dpH - 6) : (rect.bottom + 6);
            let left = rect.left;

            // If opening near right screen boundary, align with right edge of trigger
            if (left + dpW > viewportW - 12) {
                left = rect.right - dpW;
            }
            // Ensure doesn't spill past left edge
            if (left < 12) left = 12;

            dropdown.style.position = 'fixed';
            dropdown.style.top = top + 'px';
            dropdown.style.left = left + 'px';
            dropdown.style.width = dpW + 'px';
            dropdown.style.zIndex = '999999';

            wrapper.classList.toggle('is-dropup', isDropup);
        }

        function openDropdown() {
            // Close all other open datepickers & selects
            document.querySelectorAll('.finora-datepicker-container.is-open').forEach(el => {
                if (el !== wrapper) {
                    el.classList.remove('is-open', 'is-dropup');
                    const tr = el.querySelector('.finora-datepicker-trigger');
                    if (tr) tr.setAttribute('aria-expanded', 'false');
                    if (el._finoraDpDropdown) el._finoraDpDropdown.classList.remove('is-open');
                }
            });
            document.querySelectorAll('.finora-select-container.is-open').forEach(el => {
                el.classList.remove('is-open', 'is-dropup');
                if (el._finoraDropdown) el._finoraDropdown.classList.remove('is-open');
            });

            // Sync view to current value before showing
            const parsed = parseYMD(input.value);
            if (parsed) {
                viewYear = parsed.year;
                viewMonth = parsed.month;
            } else {
                viewYear = todayYMD.year;
                viewMonth = todayYMD.month;
            }
            renderCalendar();

            positionDropdown();
            wrapper.classList.add('is-open');
            trigger.setAttribute('aria-expanded', 'true');
            dropdown.classList.add('is-open');
        }

        function closeDropdown() {
            wrapper.classList.remove('is-open', 'is-dropup');
            trigger.setAttribute('aria-expanded', 'false');
            dropdown.classList.remove('is-open');
        }

        trigger.addEventListener('click', toggleDropdown);

        // Keep dropdown aligned if user scrolls or resizes
        window.addEventListener('scroll', function() {
            if (wrapper.classList.contains('is-open')) {
                positionDropdown();
            }
        }, true);

        window.addEventListener('resize', function() {
            if (wrapper.classList.contains('is-open')) {
                positionDropdown();
            }
        });

        // Intercept native input .value setter so programmatic changes reflect immediately
        const descriptor = Object.getOwnPropertyDescriptor(HTMLInputElement.prototype, 'value');
        if (descriptor && descriptor.set) {
            const originalSet = descriptor.set;
            Object.defineProperty(input, 'value', {
                get: function() {
                    return descriptor.get.call(this);
                },
                set: function(val) {
                    originalSet.call(this, val);
                    updateFromInput();
                }
            });
        }

        input.addEventListener('change', updateFromInput);

        const parentForm = input.closest('form');
        if (parentForm) {
            parentForm.addEventListener('reset', function() {
                setTimeout(updateFromInput, 10);
            });
        }

        // Initial setup
        updateFromInput();
    }

    // Close on click outside (handles both container and portal dropdown)
    document.addEventListener('click', function(e) {
        document.querySelectorAll('.finora-datepicker-container.is-open').forEach(el => {
            const dp = el._finoraDpDropdown;
            if (!el.contains(e.target) && (!dp || !dp.contains(e.target))) {
                el.classList.remove('is-open', 'is-dropup');
                const tr = el.querySelector('.finora-datepicker-trigger');
                if (tr) tr.setAttribute('aria-expanded', 'false');
                if (dp) dp.classList.remove('is-open');
            }
        });
    });

    // Close on Escape
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            document.querySelectorAll('.finora-datepicker-container.is-open').forEach(el => {
                el.classList.remove('is-open', 'is-dropup');
                const tr = el.querySelector('.finora-datepicker-trigger');
                if (tr) tr.setAttribute('aria-expanded', 'false');
                if (el._finoraDpDropdown) el._finoraDpDropdown.classList.remove('is-open');
            });
        }
    });

    function initAll() {
        const dateInputs = document.querySelectorAll('input[type="date"]:not([data-no-custom])');
        dateInputs.forEach(initCustomDatePicker);
    }

    // Auto-init on load and observe dynamic insertions
    const bodyObserver = new MutationObserver(function(mutations) {
        for (const mut of mutations) {
            if (mut.addedNodes.length) {
                mut.addedNodes.forEach(node => {
                    if (node.nodeType === Node.ELEMENT_NODE) {
                        if (node.tagName === 'INPUT' && node.type === 'date') {
                            initCustomDatePicker(node);
                        } else if (node.querySelectorAll) {
                            node.querySelectorAll('input[type="date"]:not([data-no-custom])').forEach(initCustomDatePicker);
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

    window.FinoraDatePicker = {
        init: initCustomDatePicker,
        initAll: initAll
    };
})();
