/**
 * Finora — Ultimate Animation System
 * Advanced animations: particles, morphing, parallax, gestures, counters
 */

(function() {
    'use strict';

    const FinoraAnimations = {
        config: {
            prefersReducedMotion: false,
            particlesEnabled: true,
            parallaxEnabled: true,
            rippleEnabled: true
        },

        init() {
            this.detectReducedMotion();
            
            if (this.config.prefersReducedMotion) {
                return;
            }

            this.initParticleSystem();
            this.initRippleEffects();
            this.initNumberCounters();
            this.initParallaxElements();
            this.initMorphingShapes();
            this.initHoverMagnetic();
            this.initScrollProgress();
            this.initPageTransitions();
            this.initSkeletonLoaders();
        },

        detectReducedMotion() {
            const mediaQuery = window.matchMedia('(prefers-reduced-motion: reduce)');
            this.config.prefersReducedMotion = mediaQuery.matches;

            mediaQuery.addEventListener('change', (e) => {
                this.config.prefersReducedMotion = e.matches;
            });
        },

        // ==================== PARTICLE SYSTEM ====================
        initParticleSystem() {
            const containers = document.querySelectorAll('[data-particles]');
            
            containers.forEach(container => {
                const canvas = document.createElement('canvas');
                canvas.className = 'particle-canvas';
                canvas.style.cssText = 'position:absolute;top:0;left:0;width:100%;height:100%;pointer-events:none;z-index:1;';
                container.style.position = 'relative';
                container.insertBefore(canvas, container.firstChild);

                const ctx = canvas.getContext('2d');
                const particles = [];
                const particleCount = parseInt(container.dataset.particles) || 30;
                const color = container.dataset.particleColor || 'rgba(107, 140, 174, 0.15)';

                function resize() {
                    canvas.width = container.offsetWidth;
                    canvas.height = container.offsetHeight;
                }

                resize();
                window.addEventListener('resize', resize);

                class Particle {
                    constructor() {
                        this.reset();
                        this.y = Math.random() * canvas.height;
                        this.vx *= 0.5;
                    }

                    reset() {
                        this.x = Math.random() * canvas.width;
                        this.y = -10;
                        this.size = Math.random() * 3 + 1;
                        this.speedY = Math.random() * 0.5 + 0.2;
                        this.vx = (Math.random() - 0.5) * 0.3;
                        this.opacity = Math.random() * 0.5 + 0.2;
                        this.pulse = Math.random() * Math.PI * 2;
                        this.pulseSpeed = Math.random() * 0.02 + 0.01;
                    }

                    update() {
                        this.y += this.speedY;
                        this.x += this.vx;
                        this.pulse += this.pulseSpeed;

                        if (this.y > canvas.height + 10) {
                            this.reset();
                        }

                        if (this.x < 0 || this.x > canvas.width) {
                            this.vx *= -1;
                        }
                    }

                    draw() {
                        const pulseSize = this.size + Math.sin(this.pulse) * 0.5;
                        ctx.beginPath();
                        ctx.arc(this.x, this.y, pulseSize, 0, Math.PI * 2);
                        ctx.fillStyle = color.replace(/[\d.]+\)$/g, this.opacity + ')');
                        ctx.fill();
                    }
                }

                for (let i = 0; i < particleCount; i++) {
                    particles.push(new Particle());
                }

                function animate() {
                    ctx.clearRect(0, 0, canvas.width, canvas.height);
                    
                    particles.forEach(particle => {
                        particle.update();
                        particle.draw();
                    });

                    // Connect nearby particles
                    for (let i = 0; i < particles.length; i++) {
                        for (let j = i + 1; j < particles.length; j++) {
                            const dx = particles[i].x - particles[j].x;
                            const dy = particles[i].y - particles[j].y;
                            const distance = Math.sqrt(dx * dx + dy * dy);

                            if (distance < 100) {
                                ctx.beginPath();
                                ctx.strokeStyle = color.replace(/[\d.]+\)$/g, (0.1 * (1 - distance / 100)) + ')');
                                ctx.lineWidth = 0.5;
                                ctx.moveTo(particles[i].x, particles[i].y);
                                ctx.lineTo(particles[j].x, particles[j].y);
                                ctx.stroke();
                            }
                        }
                    }

                    requestAnimationFrame(animate);
                }

                animate();
            });
        },

        // ==================== RIPPLE EFFECTS ====================
        initRippleEffects() {
            const elements = document.querySelectorAll('[data-ripple], .btn, .action-card, .summary-card');
            
            elements.forEach(el => {
                el.style.position = 'relative';
                el.style.overflow = 'hidden';

                el.addEventListener('click', (e) => {
                    const ripple = document.createElement('span');
                    const rect = el.getBoundingClientRect();
                    const size = Math.max(rect.width, rect.height);
                    const x = e.clientX - rect.left - size / 2;
                    const y = e.clientY - rect.top - size / 2;

                    ripple.style.cssText = `
                        position: absolute;
                        width: ${size}px;
                        height: ${size}px;
                        border-radius: 50%;
                        background: radial-gradient(circle, rgba(255,255,255,0.6) 0%, rgba(255,255,255,0) 70%);
                        top: ${y}px;
                        left: ${x}px;
                        pointer-events: none;
                        transform: scale(0);
                        animation: ripple-animation 600ms ease-out;
                        z-index: 9999;
                    `;

                    el.appendChild(ripple);

                    setTimeout(() => ripple.remove(), 600);
                });
            });

            // Add ripple animation to document
            if (!document.getElementById('ripple-styles')) {
                const style = document.createElement('style');
                style.id = 'ripple-styles';
                style.textContent = `
                    @keyframes ripple-animation {
                        to {
                            transform: scale(4);
                            opacity: 0;
                        }
                    }
                `;
                document.head.appendChild(style);
            }
        },

        // ==================== NUMBER COUNTERS ====================
        initNumberCounters() {
            const counters = document.querySelectorAll('[data-counter]');
            
            if (counters.length === 0) return;

            const observer = new IntersectionObserver((entries) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting && !entry.target.dataset.counted) {
                        this.animateCounter(entry.target);
                        entry.target.dataset.counted = 'true';
                    }
                });
            }, { threshold: 0.5 });

            counters.forEach(counter => observer.observe(counter));
        },

        animateCounter(element) {
            const target = parseInt(element.textContent.replace(/[^\d]/g, '')) || 0;
            const duration = parseInt(element.dataset.counterDuration) || 2000;
            const prefix = element.dataset.counterPrefix || '';
            const suffix = element.dataset.counterSuffix || '';
            const start = 0;
            const increment = target / (duration / 16);
            let current = start;

            const updateCounter = () => {
                current += increment;
                if (current < target) {
                    element.textContent = prefix + Math.floor(current).toLocaleString('id-ID') + suffix;
                    requestAnimationFrame(updateCounter);
                } else {
                    element.textContent = prefix + target.toLocaleString('id-ID') + suffix;
                }
            };

            updateCounter();
        },

        // ==================== PARALLAX ELEMENTS ====================
        initParallaxElements() {
            const elements = document.querySelectorAll('[data-parallax]');
            
            if (elements.length === 0) return;

            let ticking = false;

            const updateParallax = () => {
                const scrollY = window.pageYOffset;

                elements.forEach(el => {
                    const speed = parseFloat(el.dataset.parallax) || 0.5;
                    const rect = el.getBoundingClientRect();
                    const elementY = rect.top + scrollY;
                    const offset = (scrollY - elementY) * speed;

                    el.style.transform = `translate3d(0, ${offset}px, 0)`;
                });

                ticking = false;
            };

            window.addEventListener('scroll', () => {
                if (!ticking) {
                    requestAnimationFrame(updateParallax);
                    ticking = true;
                }
            });

            updateParallax();
        },

        // ==================== MORPHING SHAPES ====================
        initMorphingShapes() {
            const shapes = document.querySelectorAll('[data-morph]');
            
            shapes.forEach(shape => {
                const svg = shape.querySelector('svg');
                if (!svg) return;

                let morphProgress = 0;
                const speed = parseFloat(shape.dataset.morphSpeed) || 0.01;

                const animate = () => {
                    morphProgress += speed;
                    
                    const paths = svg.querySelectorAll('path, circle, ellipse');
                    paths.forEach((path, index) => {
                        const offset = Math.sin(morphProgress + index) * 2;
                        path.style.transform = `translate(${offset}px, ${offset * 0.5}px)`;
                        path.style.opacity = 0.7 + Math.sin(morphProgress + index) * 0.3;
                    });

                    requestAnimationFrame(animate);
                };

                animate();
            });
        },

        // ==================== MAGNETIC HOVER ====================
        initHoverMagnetic() {
            const elements = document.querySelectorAll('[data-magnetic]');
            
            elements.forEach(el => {
                const strength = parseFloat(el.dataset.magnetic) || 0.3;

                el.addEventListener('mousemove', (e) => {
                    const rect = el.getBoundingClientRect();
                    const x = e.clientX - rect.left - rect.width / 2;
                    const y = e.clientY - rect.top - rect.height / 2;

                    el.style.transform = `translate(${x * strength}px, ${y * strength}px)`;
                });

                el.addEventListener('mouseleave', () => {
                    el.style.transform = 'translate(0, 0)';
                });
            });
        },

        // ==================== SCROLL PROGRESS ====================
        initScrollProgress() {
            const indicators = document.querySelectorAll('[data-scroll-progress]');
            
            indicators.forEach(indicator => {
                const updateProgress = () => {
                    const windowHeight = window.innerHeight;
                    const documentHeight = document.documentElement.scrollHeight;
                    const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
                    const progress = (scrollTop / (documentHeight - windowHeight)) * 100;

                    indicator.style.width = progress + '%';
                };

                window.addEventListener('scroll', updateProgress);
                updateProgress();
            });
        },

        // ==================== PAGE TRANSITIONS ====================
        initPageTransitions() {
            // Add smooth page transition overlay
            const links = document.querySelectorAll('a:not([target="_blank"]):not([href^="#"]):not(.no-transition)');
            
            links.forEach(link => {
                link.addEventListener('click', (e) => {
                    const href = link.getAttribute('href');
                    if (!href || href === '#' || href.startsWith('javascript:')) return;

                    e.preventDefault();

                    // Create transition overlay
                    const overlay = document.createElement('div');
                    overlay.style.cssText = `
                        position: fixed;
                        top: 0;
                        left: 0;
                        width: 100%;
                        height: 100%;
                        background: #FDF8F0;
                        z-index: 99999;
                        opacity: 0;
                        transition: opacity 300ms ease-in-out;
                        pointer-events: none;
                    `;

                    document.body.appendChild(overlay);

                    setTimeout(() => {
                        overlay.style.opacity = '1';
                        overlay.style.pointerEvents = 'auto';
                    }, 10);

                    setTimeout(() => {
                        window.location.href = href;
                    }, 350);
                });
            });

            // Fade in on page load
            document.body.style.opacity = '0';
            document.body.style.transition = 'opacity 300ms ease-in-out';
            
            window.addEventListener('load', () => {
                setTimeout(() => {
                    document.body.style.opacity = '1';
                }, 50);
            });
        },

        // ==================== SKELETON LOADERS ====================
        initSkeletonLoaders() {
            const skeletons = document.querySelectorAll('[data-skeleton]');
            
            skeletons.forEach(skeleton => {
                skeleton.classList.add('skeleton-loading');
                
                // Add shimmer effect
                const shimmer = document.createElement('div');
                shimmer.className = 'skeleton-shimmer';
                skeleton.appendChild(shimmer);
            });

            // Add skeleton styles
            if (!document.getElementById('skeleton-styles')) {
                const style = document.createElement('style');
                style.id = 'skeleton-styles';
                style.textContent = `
                    .skeleton-loading {
                        position: relative;
                        background: linear-gradient(90deg, #E8DDD0 0%, #F5EFE6 50%, #E8DDD0 100%);
                        background-size: 200% 100%;
                        animation: skeleton-pulse 1.5s ease-in-out infinite;
                        overflow: hidden;
                        border-radius: 8px;
                    }

                    .skeleton-shimmer {
                        position: absolute;
                        top: 0;
                        left: -100%;
                        width: 100%;
                        height: 100%;
                        background: linear-gradient(90deg, transparent, rgba(255,255,255,0.6), transparent);
                        animation: skeleton-shimmer 2s ease-in-out infinite;
                    }

                    @keyframes skeleton-pulse {
                        0%, 100% { opacity: 1; }
                        50% { opacity: 0.6; }
                    }

                    @keyframes skeleton-shimmer {
                        0% { left: -100%; }
                        100% { left: 100%; }
                    }
                `;
                document.head.appendChild(style);
            }
        },

        // ==================== UTILITY: ADD FLOATING ANIMATION ====================
        addFloatingAnimation(element, intensity = 10, duration = 3) {
            element.style.animation = `float-${intensity} ${duration}s ease-in-out infinite`;
            
            if (!document.getElementById(`float-${intensity}-keyframes`)) {
                const style = document.createElement('style');
                style.id = `float-${intensity}-keyframes`;
                style.textContent = `
                    @keyframes float-${intensity} {
                        0%, 100% { transform: translateY(0px); }
                        50% { transform: translateY(-${intensity}px); }
                    }
                `;
                document.head.appendChild(style);
            }
        },

        // ==================== UTILITY: SHAKE ANIMATION ====================
        shake(element) {
            element.style.animation = 'shake 0.5s ease-in-out';
            
            if (!document.getElementById('shake-keyframes')) {
                const style = document.createElement('style');
                style.id = 'shake-keyframes';
                style.textContent = `
                    @keyframes shake {
                        0%, 100% { transform: translateX(0); }
                        10%, 30%, 50%, 70%, 90% { transform: translateX(-5px); }
                        20%, 40%, 60%, 80% { transform: translateX(5px); }
                    }
                `;
                document.head.appendChild(style);
            }

            setTimeout(() => {
                element.style.animation = '';
            }, 500);
        },

        // ==================== UTILITY: BOUNCE IN ====================
        bounceIn(element) {
            element.style.animation = 'bounceIn 0.6s cubic-bezier(0.68, -0.55, 0.265, 1.55)';
            
            if (!document.getElementById('bounceIn-keyframes')) {
                const style = document.createElement('style');
                style.id = 'bounceIn-keyframes';
                style.textContent = `
                    @keyframes bounceIn {
                        0% { opacity: 0; transform: scale(0.3); }
                        50% { opacity: 1; transform: scale(1.05); }
                        70% { transform: scale(0.9); }
                        100% { opacity: 1; transform: scale(1); }
                    }
                `;
                document.head.appendChild(style);
            }
        }
    };

    // Auto-initialize
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', () => FinoraAnimations.init());
    } else {
        FinoraAnimations.init();
    }

    // Expose to window
    if (typeof window !== 'undefined') {
        window.FinoraAnimations = FinoraAnimations;
    }

})();
