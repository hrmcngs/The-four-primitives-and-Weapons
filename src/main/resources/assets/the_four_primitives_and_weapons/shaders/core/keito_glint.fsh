#version 150
#moj_import <fog.glsl>

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform float GlintAlpha;
uniform vec3 GlintColor;
in float vertexDistance;
in vec2 texCoord0;
out vec4 fragColor;

void main() {
    vec4 sampleColor = texture(Sampler0, texCoord0);
    if (sampleColor.a * ColorModulator.a < 0.1) discard;
    // Keep the full moving pattern at moderate intensity; do not clip its dim bands.
    float peak = max(sampleColor.r, max(sampleColor.g, sampleColor.b));
    float brightness = peak * 0.45;
    float fade = linear_fog_fade(vertexDistance, FogStart, FogEnd) * GlintAlpha;
    fragColor = vec4(GlintColor * brightness * ColorModulator.rgb * fade,
                     sampleColor.a * ColorModulator.a);
}
