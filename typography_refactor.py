import os
import re

BASE = r'C:\Users\aryaa\finora\src\main\resources\static'

files_to_update = [
    os.path.join(BASE, 'css', 'responsive.css'),
    os.path.join(BASE, 'css', 'dashboard.css'),
    os.path.join(BASE, 'css', 'pemasukan.css'),
    os.path.join(BASE, 'css', 'pengeluaran.css'),
    os.path.join(BASE, 'css', 'laporan.css'),
    os.path.join(BASE, 'css', 'settings.css'),
    os.path.join(BASE, 'css', 'app.css'),
]

# Also update @import in all CSS files to add DM Serif Display
for fpath in files_to_update:
    if not os.path.exists(fpath):
        continue
    with open(fpath, 'r', encoding='utf-8') as f:
        content = f.read()

    # Step 1: Replace Cormorant Garamond with DM Serif Display in font-family declarations
    content = re.sub(
        r"font-family:\s*'Cormorant\s+Garamond',\s*(?:Georgia,\s*)?serif",
        "font-family: var(--font-display)",
        content
    )
    content = re.sub(
        r"font-family:\s*'Cormorant\s+Garamond',\s*serif",
        "font-family: var(--font-display)",
        content
    )

    # Step 2: Replace specific Cormorant Garamond usages
    content = content.replace(
        "'Cormorant Garamond', Georgia, serif",
        "var(--font-display)"
    )
    content = content.replace(
        "'Cormorant Garamond', serif",
        "var(--font-display)"
    )

    # Step 3: Add font-variant-numeric: tabular-nums to financial number classes
    # This handles the CSS classes that contain financial values

    # Step 4: Ensure body font is explicitly used for UI elements
    # Add font-family: var(--font-body) to navigation, buttons, forms, labels

    with open(fpath, 'w', encoding='utf-8') as f:
        f.write(content)

    print(f"Updated: {os.path.basename(fpath)}")

# Now update the @import statements in responsive.css to include DM Serif Display
resp_path = os.path.join(BASE, 'css', 'responsive.css')
with open(resp_path, 'r', encoding='utf-8') as f:
    content = f.read()

# Add DM Serif Display to the import
if 'DM+Serif+Display' not in content:
    content = content.replace(
        "Inter:wght@400;500;600;700;800",
        "DM+Serif+Display:ital,wght@0,400;0,500;0,600;0,700;1,400;Inter:wght@400;500;600;700;800"
    )

with open(resp_path, 'w', encoding='utf-8') as f:
    f.write(content)
print(f"Updated responsive.css import")

print("\nDone! All font-family references updated.")
