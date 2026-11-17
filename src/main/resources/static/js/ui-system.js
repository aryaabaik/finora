/**
 * Finora — Advanced Loading & Notification System
 * Beautiful loading states, toast notifications, and feedback animations
 */

(function() {
    'use strict';

    const FinoraUI = {
        // ==================== TOAST NOTIFICATIONS ====================
        toast: {
            container: null,

            init() {
                if (!this.container) {
                    this.container = document.createElement('div');
                    this.container.className = 'toast-container';
                    this.container.style.cssText = `
                        position: fixed;
                        top: 20px;
                        right: 20px;
                        z-index: 99999;
                        display: flex;
                        flex-direction: column;
                        gap: 12px;
                        pointer-events: none;
                    `;
                    document.body.appendChild(this.container);
                }
            },

            show(message, type = 'info', duration = 3000) {
                this.init();

                const toast = document.createElement('div');
                toast.className = `toast toast-${type}`;
                
                const icons = {
                    success: '✓',
                    error: '✕',
                    warning: '⚠',
                    info: 'ℹ'
                };

                const colors = {
                    success: '#6B7F59',
                    error: '#A85D3F',
                    warning: '#C4A35A',
                    info: '#6B8CAE'
                };

                toast.style.cssText = `
                    background: #FFFCF5;
                    color: #1A2332;
                    padding: 16px 20px;
                    border-radius: 12px;
                    box-shadow: 0 8px 24px rgba(26, 35, 50, 0.15);
                    border-left: 4px solid ${colors[type]};
                    display: flex;
                    align-items: center;
                    gap: 12px;
                    min-width: 280px;
                    max-width: 400px;
                    pointer-events: auto;
                    cursor: pointer;
                    font-family: 'Inter', sans-serif;
                    font-size: 14px;
                    font-weight: 500;
                    transform: translateX(100%);
                    transition: transform 300ms cubic-bezier(0.34, 1.56, 0.64, 1);
                `;

                toast.innerHTML = `
                    <span style="
                        width: 24px;
                        height: 24px;
                        border-radius: 50%;
                        background: ${colors[type]};
                        color: white;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        font-weight: 700;
                        flex-shrink: 0;
                    ">${icons[type]}</span>
                    <span style="flex: 1;">${message}</span>
                    <button style="
                        background: none;
                        border: none;
                        color: #8A8680;
                        cursor: pointer;
                        padding: 4px;
                        display: flex;
                        align-items: center;
                        font-size: 16px;
                        transition: color 150ms ease;
                    " onclick="this.parentElement.remove()">✕</button>
                `;

                this.container.appendChild(toast);

                // Trigger entrance animation
                setTimeout(() => {
                    toast.style.transform = 'translateX(0)';
                }, 10);

                // Auto remove
                if (duration > 0) {
                    setTimeout(() => {
                        toast.style.transform = 'translateX(120%)';
                        setTimeout(() => toast.remove(), 300);
                    }, duration);
                }

                // Click to dismiss
                toast.addEventListener('click', (e) => {
                    if (e.target.tagName !== 'BUTTON') {
                        toast.style.transform = 'translateX(120%)';
                        setTimeout(() => toast.remove(), 300);
                    }
                });

                return toast;
            },

            success(message, duration) {
                return this.show(message, 'success', duration);
            },

            error(message, duration) {
                return this.show(message, 'error', duration);
            },

            warning(message, duration) {
                return this.show(message, 'warning', duration);
            },

            info(message, duration) {
                return this.show(message, 'info', duration);
            }
        },

        // ==================== LOADING OVERLAY ====================
        loading: {
            overlay: null,

            show(message = 'Loading...') {
                if (this.overlay) return;

                this.overlay = document.createElement('div');
                this.overlay.className = 'loading-overlay';
                this.overlay.style.cssText = `
                    position: fixed;
                    inset: 0;
                    background: rgba(253, 248, 240, 0.95);
                    backdrop-filter: blur(8px);
                    z-index: 99999;
                    display: flex;
                    flex-direction: column;
                    align-items: center;
                    justify-content: center;
                    gap: 20px;
                    opacity: 0;
                    transition: opacity 300ms ease;
                `;

                this.overlay.innerHTML = `
                    <div style="
                        width: 60px;
                        height: 60px;
                        border: 3px solid rgba(107, 140, 174, 0.2);
                        border-top-color: #6B8CAE;
                        border-radius: 50%;
                        animation: spin 800ms linear infinite;
                    "></div>
                    <p style="
                        margin: 0;
                        color: #1A2332;
                        font-family: 'Inter', sans-serif;
                        font-size: 15px;
                        font-weight: 600;
                    ">${message}</p>
                `;

                document.body.appendChild(this.overlay);
                document.body.style.overflow = 'hidden';

                setTimeout(() => {
                    if (this.overlay) this.overlay.style.opacity = '1';
                }, 10);
            },

            hide() {
                if (!this.overlay) return;

                this.overlay.style.opacity = '0';
                setTimeout(() => {
                    if (this.overlay) {
                        this.overlay.remove();
                        this.overlay = null;
                        document.body.style.overflow = '';
                    }
                }, 300);
            }
        },

        // ==================== INLINE LOADER ====================
        inlineLoader: {
            create(size = 'medium') {
                const sizes = {
                    small: 16,
                    medium: 24,
                    large: 40
                };

                const dimension = sizes[size] || sizes.medium;

                const loader = document.createElement('div');
                loader.className = `inline-loader inline-loader-${size}`;
                loader.style.cssText = `
                    display: inline-block;
                    width: ${dimension}px;
                    height: ${dimension}px;
                    border: 2px solid rgba(107, 140, 174, 0.2);
                    border-top-color: #6B8CAE;
                    border-radius: 50%;
                    animation: spin 600ms linear infinite;
                    vertical-align: middle;
                `;

                return loader;
            },

            addToButton(button) {
                if (button.querySelector('.inline-loader')) return;
                
                button.classList.add('is-loading');
                button.disabled = true;
                
                const originalContent = button.innerHTML;
                button.dataset.originalContent = originalContent;
                
                const loader = this.create('small');
                loader.style.marginRight = '8px';
                
                button.innerHTML = '';
                button.appendChild(loader);
                button.appendChild(document.createTextNode(button.dataset.loadingText || 'Loading...'));
            },

            removeFromButton(button) {
                button.classList.remove('is-loading');
                button.disabled = false;
                
                if (button.dataset.originalContent) {
                    button.innerHTML = button.dataset.originalContent;
                    delete button.dataset.originalContent;
                }
            }
        },

        // ==================== PROGRESS BAR ====================
        progress: {
            bar: null,

            init() {
                if (!this.bar) {
                    this.bar = document.createElement('div');
                    this.bar.className = 'global-progress-bar';
                    this.bar.style.cssText = `
                        position: fixed;
                        top: 0;
                        left: 0;
                        width: 0%;
                        height: 3px;
                        background: linear-gradient(90deg, #6B8CAE, #C4A35A);
                        z-index: 99999;
                        transition: width 300ms ease, opacity 300ms ease;
                        opacity: 0;
                    `;
                    document.body.appendChild(this.bar);
                }
            },

            start() {
                this.init();
                this.bar.style.opacity = '1';
                this.bar.style.width = '30%';
            },

            update(percent) {
                this.init();
                this.bar.style.opacity = '1';
                this.bar.style.width = Math.min(percent, 100) + '%';
            },

            complete() {
                this.init();
                this.bar.style.width = '100%';
                
                setTimeout(() => {
                    this.bar.style.opacity = '0';
                    setTimeout(() => {
                        this.bar.style.width = '0%';
                    }, 300);
                }, 300);
            }
        },

        // ==================== CONFIRMATION DIALOG ====================
        confirm: {
            show(message, options = {}) {
                return new Promise((resolve) => {
                    const dialog = document.createElement('div');
                    dialog.className = 'confirm-dialog-overlay';
                    dialog.style.cssText = `
                        position: fixed;
                        inset: 0;
                        background: rgba(26, 35, 50, 0.6);
                        backdrop-filter: blur(4px);
                        z-index: 99999;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        opacity: 0;
                        transition: opacity 300ms ease;
                        padding: 20px;
                    `;

                    const modal = document.createElement('div');
                    modal.className = 'confirm-dialog';
                    modal.style.cssText = `
                        background: #FFFCF5;
                        border-radius: 20px;
                        padding: 32px;
                        max-width: 440px;
                        width: 100%;
                        box-shadow: 0 20px 60px rgba(26, 35, 50, 0.3);
                        transform: scale(0.9) translateY(20px);
                        transition: transform 300ms cubic-bezier(0.34, 1.56, 0.64, 1);
                    `;

                    modal.innerHTML = `
                        <h3 style="
                            margin: 0 0 16px;
                            font-family: 'Cormorant Garamond', serif;
                            font-size: 28px;
                            font-weight: 600;
                            color: #1A2332;
                        ">${options.title || 'Konfirmasi'}</h3>
                        <p style="
                            margin: 0 0 28px;
                            font-family: 'Inter', sans-serif;
                            font-size: 15px;
                            color: #4A4A48;
                            line-height: 1.6;
                        ">${message}</p>
                        <div style="
                            display: flex;
                            gap: 12px;
                            justify-content: flex-end;
                        ">
                            <button class="confirm-cancel" style="
                                padding: 12px 24px;
                                border: 1px solid rgba(217, 197, 165, 0.5);
                                background: transparent;
                                color: #1A2332;
                                border-radius: 999px;
                                font-family: 'Inter', sans-serif;
                                font-size: 14px;
                                font-weight: 600;
                                cursor: pointer;
                                transition: all 200ms ease;
                            ">${options.cancelText || 'Batal'}</button>
                            <button class="confirm-ok" style="
                                padding: 12px 24px;
                                border: none;
                                background: ${options.type === 'danger' ? '#A85D3F' : '#1A2332'};
                                color: #FFFCF5;
                                border-radius: 999px;
                                font-family: 'Inter', sans-serif;
                                font-size: 14px;
                                font-weight: 700;
                                cursor: pointer;
                                transition: all 200ms ease;
                            ">${options.confirmText || 'OK'}</button>
                        </div>
                    `;

                    dialog.appendChild(modal);
                    document.body.appendChild(dialog);
                    document.body.style.overflow = 'hidden';

                    // Trigger entrance
                    setTimeout(() => {
                        dialog.style.opacity = '1';
                        modal.style.transform = 'scale(1) translateY(0)';
                    }, 10);

                    const close = (result) => {
                        dialog.style.opacity = '0';
                        modal.style.transform = 'scale(0.9) translateY(20px)';
                        
                        setTimeout(() => {
                            dialog.remove();
                            document.body.style.overflow = '';
                            resolve(result);
                        }, 300);
                    };

                    modal.querySelector('.confirm-ok').addEventListener('click', () => close(true));
                    modal.querySelector('.confirm-cancel').addEventListener('click', () => close(false));
                    dialog.addEventListener('click', (e) => {
                        if (e.target === dialog) close(false);
                    });
                });
            }
        },

        // ==================== UTILITY: HIGHLIGHT ELEMENT ====================
        highlight(element) {
            element.style.animation = 'highlight-flash 1s ease-out';
            setTimeout(() => {
                element.style.animation = '';
            }, 1000);
        },

        // ==================== UTILITY: CONFETTI ====================
        confetti() {
            const colors = ['#6B8CAE', '#6B7F59', '#C4A35A', '#A87862'];
            const confettiCount = 50;
            
            for (let i = 0; i < confettiCount; i++) {
                const confetti = document.createElement('div');
                confetti.style.cssText = `
                    position: fixed;
                    width: 10px;
                    height: 10px;
                    background: ${colors[Math.floor(Math.random() * colors.length)]};
                    top: -10px;
                    left: ${Math.random() * 100}%;
                    opacity: ${Math.random() * 0.7 + 0.3};
                    transform: rotate(${Math.random() * 360}deg);
                    z-index: 99999;
                    pointer-events: none;
                    border-radius: ${Math.random() > 0.5 ? '50%' : '0'};
                `;

                document.body.appendChild(confetti);

                const animation = confetti.animate([
                    { 
                        transform: `translate(0, 0) rotate(0deg)`,
                        opacity: confetti.style.opacity
                    },
                    { 
                        transform: `translate(${(Math.random() - 0.5) * 200}px, 100vh) rotate(${Math.random() * 720}deg)`,
                        opacity: 0
                    }
                ], {
                    duration: 2000 + Math.random() * 1000,
                    easing: 'cubic-bezier(0.25, 0.46, 0.45, 0.94)'
                });

                animation.onfinish = () => confetti.remove();
            }
        }
    };

    // Auto-initialize
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', () => {
            // Add necessary styles
            const style = document.createElement('style');
            style.textContent = `
                @keyframes spin {
                    to { transform: rotate(360deg); }
                }
                
                @keyframes highlight-flash {
                    0%, 100% { background: transparent; }
                    10%, 30% { background: rgba(107, 127, 89, 0.15); }
                }
                
                .confirm-cancel:hover {
                    background: rgba(232, 221, 208, 0.3) !important;
                    transform: translateY(-1px);
                }
                
                .confirm-ok:hover {
                    transform: translateY(-2px);
                    box-shadow: 0 8px 20px rgba(26, 35, 50, 0.2);
                }
            `;
            document.head.appendChild(style);
        });
    }

    // Expose to window
    if (typeof window !== 'undefined') {
        window.FinoraUI = FinoraUI;
    }

})();
