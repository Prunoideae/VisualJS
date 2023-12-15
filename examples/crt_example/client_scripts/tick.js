let applied = false

ClientEvents.tick(event => {
    if (!applied) {
        applied = true
        // Apply the effect
        VisualJS.applyEffect("crt", true)
    }

    let { level, player } = event

    // Modify uniform values, first get brightness value at player position
    let brightness = level.getRawBrightness(player.blockPosition(), 0)
    // Interpolate between 1.0 and 2.0 based on brightness (0 - 15)
    let brightnessFactor = 1.0 + (brightness / 15.0)
    VisualJS.setUniform(0, "Gray", [brightnessFactor, brightnessFactor, brightnessFactor])
    // Offset to green
    VisualJS.setUniform(0, "Offset", [0.0, 0.2, 0.0])
})

ClientEvents.loggedOut(event => {
    VisualJS.clearEffect()
    applied = false
})