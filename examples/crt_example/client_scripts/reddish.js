let applied = false

ClientEvents.tick(event => {
    if (!applied) {
        applied = true
        // Apply the effect
        VisualJS.applyEffect("reddish", true)
        // Modify uniform values
        VisualJS.setUniform(0, "redShift", [0.0, 0.2, 0.0])
    }
})

ClientEvents.loggedOut(event => {
    VisualJS.clearEffect()
    applied = false
})