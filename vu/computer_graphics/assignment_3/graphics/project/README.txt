	Computer Graphics

	Final Project: A Split-Screen Multi-Player Car Simulator


This directory contains the submission for the final project of the 2004
Computer Graphics assignment. This document discusses some of the design
choices, implementation and features of the program.


	Extensible Scenery Map Format

When the game starts up, it loads the city's scenery. This can be a complex
environment with an arbitrary number of objects. To make the game flexible
towards the scenery, the program uses a loader routine that parses a
text-based scenery map file. By default it loads the file
..\models\maps\standard.map. The format of the map file is described in the
file map-format.txt that can be found in the project's root directory.


	Collision Detection

To make the game a little bit difficult and more realistic, the program uses
dynamic collision detection to prevent players from driving off the world or
through buildings. When a collision is detected, the player's car is
immediately brought to a halt. Collision detection is dynamic in the way
that it analyses the objects from the map file and in each frame checks to
see if any of the players runs through the scenery's polygons.
A summary of the algorithm is as follows:

In each frame, the program calculates the absolute position of each vertex
of each scenery object. From that it calculates the plane for each polygon.
This information is stored in memory by means of the "plane equation" and
the normal vector for each (co-planar, convex) polygon is dynamically
computed using the normalized cross-product of two of the polygon's vectors.
Then the program creates a bounding box around each vehicle (this can be
visualized through the right-button context menu) and for each of the box's
corners it checks whether that vertex will hit or cross a polygon of the
scenery. This is done by computing the absolute coordinates of the bounding
box's corner for both the current vehicle location and the new location
after the elapsed time slice. It then uses the dot-product to derive the
absolute distance between the 2 points and a polygon from the scene. This
distance can be positive or negative, depending on whether the point is "in
front" or "behind" the polygon's plane. If the distances differ in sign it
means that the new location of the bound box is on the other side of a
polygon's plane. This could be a collision if indeed the intersection point
of the trajectory vector and the polygon's infinite plane lies inside the
actual polygon. To calculate this, we use compute the intersection point and
run a second algrithm to see if it is inside the convex polygon or not. This
is done by computing a vector between the intersection point and each of the
polygon's vertices. Then we calculate the angles between all these vectors
and check if they sum up to exactly 360 degrees. If they do, the
intersection point lies inside the convex polygon, otherwise it does not.
This technique was adopted from articles on the Internet (in particular:
http://www.cs.montana.edu/~charon/thesis/tutorials/collision.php), as the
book does not cover the subject of collision detection.


	Godzilla's Trajectory

As described in the map-format.txt file, Godzilla's walk through the city is
a pre-defined trajectory of landmarks. These landmarks are specified inside
the scenery's map file and loaded on startup. Godzilla chooses a random
landmark to start from and then simply visits the landmarks in their
original order. When Godzilla is run over by a car, it dies and a new
Godzilla is spawned on a random landmark somewhere in the city.


	Car Dynamics

The cars use simple high-school physics to model acceleration, braking and
realistic steering behaviour. When the forward key is pressed, the vehicle
will start to accelerate until it reaches its top speed. When the forward
key is released, the car will slowly decelerate again as a result of
friction and gently come to a halt again. Pressing reverse will make the car
brake faster. Steering is modeled as if the car makes a circle around a
point perpendicular to the car's direction. Obviously since a car cannot
turn while standing still, the player must press either forward or reverse
to steer the car.
Currently the car features no handbrake-turns, power-sliding or burn-outs :)



Erik van Zijst - erik@marketxs.com - 1351745
24.dec.2004
