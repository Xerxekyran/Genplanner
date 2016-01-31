/**
 * Copyright (c) 2005-2008 NetAllied Systems GmbH, Tettnang
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package genplanner.gui.view;

import genplanner.domain.Position;
import genplanner.domain.solution.CustomerEntry;
import genplanner.domain.solution.ISolution;
import genplanner.domain.solution.LogisticPlan;
import genplanner.gui.GUICache;
import genplanner.gui.control.MapView3DVehicleListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.ogre4j.ColourValue;
import org.ogre4j.HardwareBuffer;
import org.ogre4j.IEntity;
import org.ogre4j.ILight;
import org.ogre4j.IMeshManager;
import org.ogre4j.IMeshPtr;
import org.ogre4j.INode;
import org.ogre4j.ISceneNode;
import org.ogre4j.MeshManager;
import org.ogre4j.MeshPtr;
import org.ogre4j.Plane;
import org.ogre4j.Quaternion;
import org.ogre4j.ResourceGroupManager;
import org.ogre4j.Vector3;
import org.ogre4j.demos.swt.exampleapp.ExampleApplication;
import org.xbig.base.WithoutNativeObject;

import util.Pair;

/**
 * This class represents a window displaying the 3D-Map based on the given data.
 * It is based on the ExampleApplication of the ogre4j Team:
 * http://ogre4j.sourceforge.net/
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class MapView3D extends ExampleApplication implements Runnable
{
	private static String		CUSTOMER_MESH			= "house1.mesh";
	private static String		DEPOT_MESH				= "depot1.mesh";
	private static String		VEHICLE_MESH			= "vehicle1.mesh";
	private static String		SELECTED_VEHICLE_MESH	= "selectedVehicle.mesh";
	private static float		XSCALE					= 5.0f;
	private static float		YSCALE					= 5.0f;

	private static final long	serialVersionUID		= 1L;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * 
	 */
	public MapView3D()
	{
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------
	/**
	 * Tells the object to close itself
	 */
	public void closeMapView3D()
	{
		stopApplication();
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------
	/**
	 * Creates a plane.
	 */
	private void createEnvironment(float width, float length)
	{
		Plane plane = new Plane(Vector3.getUNIT_Y(), 0);
		IMeshPtr meshPtr = new MeshPtr(WithoutNativeObject.I_WILL_DELETE_THIS_OBJECT);
		IMeshManager meshMgr = MeshManager.getSingleton();
		meshMgr.createPlane(meshPtr, "ground", ResourceGroupManager.getDEFAULT_RESOURCE_GROUP_NAME(), plane, width, length, 20, 20, true, 1, 5, 5, Vector3.getUNIT_Z(), HardwareBuffer.Usage.HBU_STATIC_WRITE_ONLY, HardwareBuffer.Usage.HBU_STATIC_WRITE_ONLY, true, true);
		plane.delete();
		meshPtr.delete();

		IEntity entity = mSceneMgr.createEntity("GroundEntity", "ground");
		entity.setMaterialName("Examples/GrassFloor");

		ISceneNode node = mSceneMgr.getRootSceneNode().createChildSceneNode("GroundEntityNode", Vector3.getZERO(), Quaternion.getIDENTITY());
		node.setPosition((width - 40) / 2, 0, (length - 40) / 2);
		node.attachObject(entity);

		// Set ambient light
		ColourValue color = new ColourValue(0.2f, 0.2f, 0.2f, 1f);
		mSceneMgr.setAmbientLight(color);
		color.delete();

		// // Create a skydome
		mSceneMgr.setSkyDome(true, "Examples/CloudySky", 5, 8, 4000, true, Quaternion.getIDENTITY(), 16, 16, -1, ResourceGroupManager.getDEFAULT_RESOURCE_GROUP_NAME());

		// Create a light
		ILight l = mSceneMgr.createLight("MainLight");
		// Accept default settings: point light, white diffuse, just set
		// position
		// NB I could attach the light to a SceneNode if I wanted it to move
		// automatically with
		// other objects, but I don't
		l.setPosition(0, 50, 0);
		ILight l2 = mSceneMgr.createLight("MainLight2");
		l2.setPosition(50, 50, 50);
		ILight l3 = mSceneMgr.createLight("MainLight3");
		l3.setPosition(50, 50, -50);
		ILight l4 = mSceneMgr.createLight("MainLight4");
		l4.setPosition(-50, 50, 50);
	}

	/**
	 * Shuts down the app.
	 */
	private void cleanup()
	{
		// clean up
		swtDisplay.dispose();
		mRoot.delete();
	}

	@Override
	public void createScene()
	{
		ISolution allSolution = GUICache.getSolution();
		HashMap<String, Boolean> createdDepots = new HashMap<String, Boolean>();
		HashMap<INode, Queue<Pair<Position, Double>>> vehicleTasks = new HashMap<INode, Queue<Pair<Position, Double>>>();
		float width = 0;
		float length = 0;
		boolean camIsSet = false;
		String currentVehicleMesh = VEHICLE_MESH;

		// is there something to do?
		if (GUICache.hasLogisticTask() && allSolution != null)
		{
			ISceneNode rootSceneNode = mSceneMgr.getRootSceneNode();

			mSceneMgr.destroyAllEntities();

			LinkedList<LogisticPlan> selectedSolution = allSolution.getPlans().get(GUICache.getSelectedSolution());

			// for every plan in the selected solution
			for (int tourIndex = 0; tourIndex < selectedSolution.size(); tourIndex++)
			{
				LogisticPlan plan = selectedSolution.get(tourIndex);
				// ----------------------------------
				// draw the depots
				// ----------------------------------
				if (createdDepots.get(plan.getDepot().getName()) == null)
				{
					createdDepots.put(plan.getDepot().getName(), true);

					// create entity
					IEntity entity = mSceneMgr.createEntity("Depot_" + plan.getDepot().getName(), DEPOT_MESH);
					ISceneNode depotNode = rootSceneNode.createChildSceneNode(Vector3.getZERO(), Quaternion.getIDENTITY());
					depotNode.attachObject(entity);
					depotNode.setPosition(plan.getDepot().getPosition().getX() * XSCALE, 0, plan.getDepot().getPosition().getY() * YSCALE);

					// remember the highest values, so we can draw the ground
					// under all buildings
					if (plan.getDepot().getPosition().getX() * XSCALE > width)
						width = plan.getDepot().getPosition().getX() * XSCALE;
					if (plan.getDepot().getPosition().getY() * YSCALE > length)
						length = plan.getDepot().getPosition().getY() * YSCALE;

					if (!camIsSet)
					{
						camIsSet = true;
						Vector3 camPos = new Vector3(depotNode.getPosition().getx(), depotNode.getPosition().gety() + 10, depotNode.getPosition().getz());
						mCamera.setPosition(camPos);
					}
				}

				Queue<Pair<Position, Double>> customerTasks = new LinkedList<Pair<Position, Double>>();

				for (CustomerEntry customerEntr : plan.getCustomer())
				{
					// ----------------------------------
					// draw every customer
					// ----------------------------------
					IEntity entity = mSceneMgr.createEntity("Customer_" + customerEntr.getCustomer().getName(), CUSTOMER_MESH);
					ISceneNode customerNode = rootSceneNode.createChildSceneNode(Vector3.getZERO(), Quaternion.getIDENTITY());
					customerNode.attachObject(entity);
					customerNode.setPosition(customerEntr.getCustomer().getPosition().getX() * XSCALE, 0, customerEntr.getCustomer().getPosition().getY() * YSCALE);

					customerTasks.add(new Pair<Position, Double>(new Position((int) (customerEntr.getCustomer().getPosition().getX() * XSCALE), (int) (customerEntr.getCustomer().getPosition().getY() * YSCALE)), customerEntr.getCustomer().getServiceTime() + 0.0));

					// remember the highest values, so we can draw the ground
					// under all buildings
					if (customerEntr.getCustomer().getPosition().getX() * XSCALE > width)
						width = customerEntr.getCustomer().getPosition().getX() * XSCALE;
					if (customerEntr.getCustomer().getPosition().getY() * YSCALE > length)
						length = customerEntr.getCustomer().getPosition().getY() * YSCALE;
				}

				customerTasks.add(new Pair<Position, Double>(new Position((int) (plan.getDepot().getPosition().getX() * XSCALE), (int) (plan.getDepot().getPosition().getY() * YSCALE)), 0.0));

				// ----------------------------------
				// create the vehicle
				// ----------------------------------
				// create entity
				if(tourIndex == GUICache.getSelectedTour())
				{
					currentVehicleMesh = SELECTED_VEHICLE_MESH;
				}
				else
				{
					currentVehicleMesh = VEHICLE_MESH;
				}
				IEntity entity = mSceneMgr.createEntity("Vehicle_" + plan.getVehicle().getName(), currentVehicleMesh);
				entity.setMaterialName("Examples/RustySteel");
				ISceneNode vehicleNode = rootSceneNode.createChildSceneNode("VehicleNode_" + plan.getVehicle().getName(), Vector3.getZERO(), Quaternion.getIDENTITY());
				vehicleNode.attachObject(entity);
				vehicleNode.setInheritOrientation(false);
				vehicleNode.setPosition(plan.getDepot().getPosition().getX() * XSCALE, 0, plan.getDepot().getPosition().getY() * YSCALE);

				// add the tasks to the vehicle
				vehicleTasks.put(vehicleNode, customerTasks);
			}
		}

		// create a plane as ground
		createEnvironment(width + 35, length + 35);

		MapView3DVehicleListener vehicleListener = new MapView3DVehicleListener(vehicleTasks);
		addFrameListener(vehicleListener);
	}

	@Override
	public void run()
	{
		try
		{
			this.go();

			cleanup();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------
}
