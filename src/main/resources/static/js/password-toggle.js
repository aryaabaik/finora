function togglePassword(btn) {
    const input = btn.previousElementSibling;
    if (!input || input.tagName !== 'INPUT') return;
    const isPassword = input.type === 'password';
    input.type = isPassword ? 'text' : 'password';
    btn.setAttribute('aria-pressed', String(isPassword));
}