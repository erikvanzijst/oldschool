/*
 * This files contains the collision detection routines. It is used in every
 * cycle of the program. The implementation was inspired by the article at:
 * http://www.cs.montana.edu/~charon/thesis/tutorials/collision.php
 *
 * Erik van Zijst, erik@prutser.cx, 01.jan.2005
 */

#include "globals.h"
#include "util.h"

/*
 * Returns TRUE if the given point lies inside the given polygon, FALSE
 * otherwise. All vertices of the polygon must be co-planar and not be on a
 * single line. The polygon must have at least 3 vertices and be convex.
 */
BOOL vertexInPolygon(Vertex *point, Vertex *vertices, int num_vertices) {

	#define EPSILON 0.01
	double angle = 0.0;
	int i;
	Vertex first, previous;

	if(num_vertices < 3) {
		// this is not a proper polygon
		return FALSE;
	} else {

		GLfloat dp;
		for(i = 0; i < num_vertices; i++) {

			Vertex vector = {0.0, 0.0, 0.0, 1.0};
			Vertex normalized_vector = {0.0, 0.0, 0.0, 1.0};

			subtractVertices((Vertex *)vertices[i], point, &vector);
			normalize(&vector, &normalized_vector);

			if(i == 0) {
				// save the first vector for use at the end
				memcpy(&first, &normalized_vector, sizeof(Vertex));
			} else {
				dp = DotProduct(&previous, &normalized_vector);
				// calculate the angle between this vector and the previous
				if(dp > 1.0 || dp < -1.0) {
					return FALSE;
				} else {
					angle += acos(dp);
				}
			}

			// save this vector for use in the next round
			memcpy(&previous, &normalized_vector, sizeof(Vertex));
		}
		dp = DotProduct(&previous, &first);
		if(dp > 1.0 || dp < -1.0) {
			return FALSE;
		} else {
			angle += acos(dp);
		}


		if(fabs(fabs(angle) - to_radians(360.0)) < EPSILON) {
			return TRUE;
		} else {
			return FALSE;
		}
	}
}

/*
 *
 */
BOOL detectSceneryCollision(Vertex *from, Vertex *to, LinkedList *scenery) {

	LinkedList *entry;

	for(entry = scenery; entry != NULL; entry = entry->next) {

		Object *object = (Object *)entry->data;
		Surface *polygon;
		int i;

		for(i = 0; i < object->num_polygons; i++) {
			polygon = &object->polygons[i];

			if(polygon->num_vertices >= 3) {
	
				// for each polygon, test to see if the vertices are on different sides
				GLfloat dist1, dist2;
				dist1 = DotProduct(&polygon->plane_normal, to) - polygon->distance;
				dist2 = DotProduct(&polygon->plane_normal, from) - polygon->distance;

				if((dist1 > 0.0f && dist2 < 0.0f) || (dist1 < 0.0f && dist2 > 0.0f) || dist2 == 0.0f) {

					// we crossed the plane, calculate the intersection point
					Vertex ray, intersect;
					GLfloat t, denominator;

					subtractVertices(to, from, &ray);
					denominator = DotProduct(&polygon->plane_normal, &ray);

					// check for orthogonality between the plane's normal and our ray
					if(denominator != 0.0f) {	

						t = -(DotProduct(&polygon->plane_normal, from) - polygon->distance ) / denominator;
						vectorScalarMultiply(&ray, t, &ray);
						addVertices(from, &ray, &intersect);

						// now see if the intersection point lies inside the polygon
						if(vertexInPolygon(&intersect, (Vertex *)polygon->absolute_vertices, polygon->num_vertices)) {
							return TRUE;
						}
					}
				}
			}
		}
	}

	return FALSE;
}

/*
 * Check to see if the given monster has collided with the specified player
 * vehicle. This is done by checking if the monster's center-of-mass lies
 * inside the collision bounding box of the player's vehicle.
 */
BOOL detectMonsterCollision(Monster *m, Player *p) {

	Vertex m_pos;

	if(sizeof(p->model_absolute_corners) / sizeof(Vertex) < 3) {
		return FALSE;
	} else {
		memcpy(&m_pos, &m->position, sizeof(Vertex));
		m_pos[1] = p->model_absolute_corners[0][1];
		return vertexInPolygon(&m_pos, (Vertex *)&p->model_absolute_corners, sizeof(p->model_absolute_corners) / sizeof(Vertex));
	}
}
