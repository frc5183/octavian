package org.frc5183.math.auto.pathfinding

import com.pathplanner.lib.path.GoalEndState
import com.pathplanner.lib.path.PathConstraints
import com.pathplanner.lib.path.PathPlannerPath
import com.pathplanner.lib.pathfinding.Pathfinder
import edu.wpi.first.math.Pair
import edu.wpi.first.math.geometry.Translation2d

class DummyPathfinder(val pathFileName: String) : Pathfinder {
    override fun isNewPathAvailable(): Boolean = false

    override fun getCurrentPath(constraints: PathConstraints?, goalEndState: GoalEndState?): PathPlannerPath =
        PathPlannerPath.fromPathFile(pathFileName)

    override fun setStartPosition(startPosition: Translation2d?) {

    }

    override fun setGoalPosition(goalPosition: Translation2d?) {

    }

    override fun setDynamicObstacles(
        obs: MutableList<Pair<Translation2d, Translation2d>>?,
        currentRobotPos: Translation2d?
    ) {

    }
}