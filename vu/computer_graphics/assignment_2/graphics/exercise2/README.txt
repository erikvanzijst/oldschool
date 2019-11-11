	Computer Graphics

	Exercise 2: solar system


This directory contains the submission for exercise 2 of the 2004 Computer Graphics
practical assignment. In this document the design choices, implementation and
features of the program are discussed.


	Models & Lighting
The program has the required F16, cube and pyramid. However, the scene also contains
two light bulbs that represent the actual location of the 2 stationary lights used
in the program. The bulbs are programmed to emit (white) light. Since this is kind
of a solar system, the cube is tilted 20 degrees. The F16 however orbits in the XY
plane. The pyramid makes full rotations around every axis.
When the object loader was tested, I fed it custom .sgf files with different
polygon types. One of these custom objects was a simple cube with normal vectors.
Because this cube.sgf file can replace the hardcoded cube object, I removed it from
the program and now always load the cube from file. The pyramid however is still
hardcoded in the program. Loading it dynamically through the loader is difficult as
the loader (currently) does not support texture mapping.


	Textures
The pyramid has the "Computer Graphics" texture mapped to all its five surfaces.
The texture layout on the ground surface can be configured using the right-button
context menu. Thw user can switch between (1 x 1), (1, 0.5) and (4 x 2). When
(1 x 0.5) is selected, only half the texture is visible. When closely examined, it
can be seen that when (1 x 0.5) is selected, the texture rotates, placing the text
up side down compared to the other tile layouts. This is due to the implementation.
When the texture is mapped to the surface, GL_REPEAT in both directions is used to
ensure the texture will be repeated when it doesn't cover the entire surface.
Because the implementation multiplies the arguments to glTexCoord2f() with the
number of selected tiles in both directions, (1 x 0.5) results in displaying only
the lower half of the texture ("Graphics"). However, because the exercise requires
the upper half, the glTexCoord2f() arguments are multiplied by negative values.
While this gives a clean and flexible implementation, it comes with the side-effect
of texture rotation.
When the texture is loaded, the program uses gluBuild2DMipmaps() to create texture
copies of different resolutions that are used when the texture mapped object is
small and wouldn't benefit from a high resolution texture. By pre-computing these
mipmaps, some performance is gained. How GL switches between mipmaps can be
witnessed by slowly zooming out while carefully watching one of the pyramid's
surfaces.


	Camera Movement & Mouse Tracking
To make the scene a bit more interesting (and not in the last place to easy
debugging), the program tracks movements of the mouse and moves the camera
accordingly. When the mouse is moved up or down, the camera zooms from 1 to 179
degrees. Up zooms in, down zooms out. Zooming is implemented using the fovy
argument of the gluPerspective() call. This changes the view angle and hence allows
for nice zooming. Mouse tracking is enabled by default, but can be switched off
through the right-button context menu "zooming/moving".


Erik van Zijst - erik@marketxs.com - 1351745
24.nov.2004
