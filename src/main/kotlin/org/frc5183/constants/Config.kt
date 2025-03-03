package org.frc5183.constants

import edu.wpi.first.apriltag.AprilTagFieldLayout
import edu.wpi.first.apriltag.AprilTagFields
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Miscellaneous configuration constants.
 */
object Config {
    const val VISION_POSE_ESTIMATION = false
    val FIELD_LAYOUT: AprilTagFieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2025ReefscapeWelded) // https://firstfrc.blob.core.windows.net/frc2025/Manual/TeamUpdates/TeamUpdate12.pdf
    val BREAK_TIME_AFTER_DISABLE: Duration = 1.seconds
}
