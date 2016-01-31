package genplanner.gui.control;

import genplanner.domain.Position;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;

import org.ogre4j.INode;
import org.ogre4j.IVector3;
import org.ogre4j.Matrix3;
import org.ogre4j.Quaternion;
import org.ogre4j.Vector3;
import org.ogre4j.demos.swt.exampleapp.FrameEvent;
import org.ogre4j.demos.swt.exampleapp.IFrameListener;

import util.Pair;

/**
 * This class manages the movement of the vehicles in the 3D - Map
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class MapView3DVehicleListener implements IFrameListener
{
	@SuppressWarnings("unused")
	private static final String								MODULE_NAME		= "MapView3DVehicleListener";
	long													lastUpdate		= System.currentTimeMillis();
	long													now				= System.currentTimeMillis();

	/**
	 * VehicleNode -> List of TargetPositions and wait time at target
	 */
	private HashMap<INode, Queue<Pair<Position, Double>>>	vehicleTasks	= null;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------

	/**
	 * Constructor
	 * 
	 * @param vehicleTasks
	 *            A hashmap with keys representing the nodes of the vehicles and
	 *            a list of tasks to be done (position to move to and time to
	 *            wait at target position)
	 */
	public MapView3DVehicleListener(HashMap<INode, Queue<Pair<Position, Double>>> vehicleTasks)
	{
		this.vehicleTasks = vehicleTasks;
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------
	@Override
	public boolean frameEnded(FrameEvent evt)
	{
		return true;
	}

	@Override
	public boolean frameStarted(FrameEvent evt)
	{
		now = System.currentTimeMillis();
		processOneStep((now - lastUpdate));
		lastUpdate = System.currentTimeMillis();

		return true;
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------
	/**
	 * 
	 * @param timeElapsed
	 *            the time elapsed since the last call of this method
	 */
	private void processOneStep(long timeElapsed)
	{
		Iterator<INode> it = vehicleTasks.keySet().iterator();
		while (it.hasNext())
		{
			// the speed of the vehicles in scaled km per millisecond (8h = 1
			// minute)
			float vehicleSpeed = 1.0f * 60.0f * (8.0f / 24.0f);
			INode currentVehicleNode = it.next();
			Queue<Pair<Position, Double>> taskQueue = vehicleTasks.get(currentVehicleNode);

			Pair<Position, Double> currentTask = taskQueue.peek();
			if (currentTask == null)
			{
				continue;
			}

			// move vehicle
			IVector3 currentPosition = currentVehicleNode.getPosition();
			IVector3 toPosition = new Vector3(currentTask.firstValue.getX(), 0, currentTask.firstValue.getY());
			float oldDistance = currentPosition.distance(toPosition);

			IVector3 direction = new Vector3(toPosition.getx() - currentPosition.getx(), toPosition.gety() - currentPosition.gety(), toPosition.getz() - currentPosition.getz());
			direction.normalise();

			currentVehicleNode.setOrientation(GenerateRotationFromDirectionVector(direction));

			float moveDistance = (float) vehicleSpeed * timeElapsed / 1000.0f;

			direction.setx(direction.getx() * moveDistance);
			direction.sety(direction.gety() * moveDistance);
			direction.setz(direction.getz() * moveDistance);

			currentPosition.setx(currentPosition.getx() + direction.getx());
			currentPosition.sety(currentPosition.gety() + direction.gety());
			currentPosition.setz(currentPosition.getz() + direction.getz());

			currentVehicleNode.setPosition(currentPosition);

			// if we arrived at the goal, start waiting
			float newDistance = currentPosition.distance(toPosition);
			if (newDistance > oldDistance || newDistance <= 1.0f)
			{
				currentVehicleNode.setPosition(toPosition);

				// refresh the waiting time
				currentTask.secondValue = currentTask.secondValue - (timeElapsed * 60.0d / 1000.0d);

				// if waiting time is over
				if (currentTask.secondValue <= 0)
				{
					taskQueue.poll();
				}
			}
		}
	}

	private Quaternion GenerateRotationFromDirectionVector(IVector3 vDirection)
	{
		// Step 1. Setup basis vectors describing the rotation given the input
		// vector and assuming an initial up direction of (0, 1, 0)
		Vector3 vUp = new Vector3(0.0f, 1.0f, 0.0f);
		Vector3 vRight = new Vector3();
		vUp.crossProduct(vRight, vDirection);
		vRight.normalise();

		Matrix3 mBasis = new Matrix3();
		mBasis.FromAxes(vRight, vUp, vDirection);
		Quaternion qrot = new Quaternion(mBasis);
		return qrot;
	}
	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------
}
