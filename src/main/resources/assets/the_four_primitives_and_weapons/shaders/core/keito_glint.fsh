#version 150
#moj_import <fog.glsl>

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform float GlintAlpha;
in float vertexDistance;
in vec2 texCoord0;
out vec4 fragColor;

void main() {
    vec4 sampleColor = texture(Sampler0, texCoord0);
    if (sampleColor.a * ColorModulator.a < 0.1) discard;
    // Keep the full moving pattern at moderate intensity; do not clip its dim bands.
    float peak = max(sampleColor.r, max(sampleColor.g, sampleColor.b));
    float brightness = peak * 0.45;
    vec3 blueWhite = vec3(0.65, 0.86, 1.0);
    float fade = linear_fog_fade(vertexDistance, FogStart, FogEnd) * GlintAlpha;
    fragColor = vec4(blueWhite * brightness * ColorModulator.rgb * fade,
                     sampleColor.a * ColorModulator.a);
}
