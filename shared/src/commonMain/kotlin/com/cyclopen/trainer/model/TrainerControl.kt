package com.cyclopen.trainer.model

/**
 * Control commands for smart trainers.
 */
sealed class TrainerControl {
    data class SetTargetPower(val watts: Int) : TrainerControl()
    data class SetResistance(val level: Int) : TrainerControl()
    data class SetSlope(val grade: Float) : TrainerControl()
    data object Stop : TrainerControl()
}
