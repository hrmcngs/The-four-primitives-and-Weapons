"""Synthesize original weather ambience loops. Requires Python 3 and ffmpeg; no sampled audio."""
from pathlib import Path
import math, random, struct, subprocess, tempfile, wave

ROOT = Path(__file__).resolve().parents[2]
OUT = ROOT / 'src/main/resources/assets/the_four_primitives_and_weapons/sounds/weather'
RATE = 22050

def generate(name, seed):
    rng = random.Random(seed)
    samples = []
    low = mid = impact = 0.0
    for i in range(RATE * 9):
        t = i / RATE
        noise = rng.uniform(-1, 1)
        low += .012 * (noise - low)
        mid += .16 * (noise - mid)
        gust = .65 + .2 * math.sin(t * 1.4) + .15 * math.sin(t * 3.1)
        if name == 'wind':
            value = (low * 3 + mid * .35) * gust
        elif name == 'sand':
            value = (low * 2 + mid * .65 + noise * .08) * gust
        else:
            if rng.random() < 28 / RATE:
                impact = rng.uniform(.15, .5)
            impact *= .975
            value = low * .8 + impact * (noise * .6 + math.sin(i * 1.7) * .4)
        samples.append(value)
    # Crossfade the tail into the head; the resulting 8-second loop has no hard seam.
    n = RATE * 8
    blend = RATE
    loop = samples[:n]
    for i in range(blend):
        f = i / blend
        loop[i] = samples[n + i] * (1 - f) + samples[i] * f
    peak = max(abs(x) for x in loop)
    pcm = b''.join(struct.pack('<h', round(x / peak * 18000)) for x in loop)
    with tempfile.TemporaryDirectory() as folder:
        wav = Path(folder) / 'loop.wav'
        with wave.open(str(wav), 'wb') as w:
            w.setnchannels(1); w.setsampwidth(2); w.setframerate(RATE); w.writeframes(pcm)
        subprocess.run(['ffmpeg', '-v', 'error', '-y', '-i', str(wav), '-ac', '2', '-c:a', 'vorbis', '-strict', '-2', '-q:a', '4', str(OUT / (name + '.ogg'))], check=True)

if __name__ == '__main__':
    OUT.mkdir(parents=True, exist_ok=True)
    for index, name in enumerate(('wind', 'sand', 'hail')):
        generate(name, 8240 + index)
        print(OUT / (name + '.ogg'))
