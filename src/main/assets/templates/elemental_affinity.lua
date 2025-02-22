math.randomseed(os.time())  -- Ensure randomness on each run

local elements = {
    "🔥 Fire - Passionate, unpredictable, and a little destructive.",
    "💧 Water - Calm, adaptable, but can be overwhelming.",
    "🌿 Earth - Grounded, patient, and dependable.",
    "🌪 Air - Free-spirited, ever-changing, and untamed.",
    "⚡ Lightning - Fast, intense, and never stays still.",
    "❄ Ice - Cool, distant, but sharp when needed.",
    "☀ Light - Radiant, uplifting, and full of energy.",
    "🌑 Shadow - Mysterious, unseen, but always present."
}

local choice = elements[math.random(#elements)]

print("Your Elemental Affinity: " .. choice)
