package nl.vu.pp.distributed;

import java.io.Serializable;

/**
 * An instance of this class describes a valid move that could be made on the
 * 15-puzzle board. The coordinates it holds locate the blank tile. The move is
 * made by sliding the tile currently at (getCol(), getRow()) to the blank
 * position. <P>
 * In addition to the new position of the blank tile, every Move object
 * contains a reference to its parent Move. Together with all parent moves, a
 * move describes all steps that lead to the current board configuration.
 * <P>
 *
 * @author	Erik van Zijst - erik@prutser.cx - 28.dec.2002
 */
public class Move implements Serializable
{
	private int blankCol, blankRow;
	private Move parent = null;

	/**
	 * Public constructor used by the Job class to create the starting
	 * position. <BR>
	 * The first Move instance locates the blank tile of the board
	 * configuration it is associated with. Sub-moves describe a non-blank,
	 * neighboring tile that needs to be slided into the empty position.
	 * <BR>
	 * Legal sub-moves are created by this class' <code>left()</code>,
	 * <code>right()</code>, <code>up()</code> and <code>down()</code>
	 * methods.
	 * <P>
	 *
	 * @param	blankCol the column in which the blank tile is located.
	 * @param	blankRow the row in which the blank tile is located.
	 */
	public Move(int blankCol, int blankRow)
	{
		this.blankCol = blankCol;
		this.blankRow = blankRow;
	}

	/**
	 * Private constructor used by this class to create successive moves
	 * following this move.
	 */
	private Move(int blankCol, int blankRow, Move parent)
	{
		this.blankCol = blankCol;
		this.blankRow = blankRow;
		this.parent = parent;
	}

	/**
	 * Returns the column of the tile that needs to be slided into the
	 * blank spot.
	 * <P>
	 *
	 * @return	the column of the tile that needs to be slided into
	 * 	the blank spot.
	 */
	public int getCol()
	{
		return blankCol;
	}

	/**
	 * Returns the row of the tile that needs to be slided into the
	 * blank spot.
	 * <P>
	 *
	 * @return	the row of the tile that needs to be slided into the
	 * 	blank spot.
	 */
	public int getRow()
	{
		return blankRow;
	}
	
	/**
	 * Returns this move's parent move or <code>null</code> if this is the
	 * very first (root) move.
	 *
	 * @return	this move's parent move.
	 */
	public Move getParent()
	{
		return parent;
	}
	
	/**
	 * Recursive function that counts adds one to the parent's depth to
	 * obtain the depth of all moves. The "root move" always returns a
	 * depth of 0 (zero).
	 * <P>
	 *
	 * @return	the depth of this move.
	 */
	public int getDepth()
	{
		return (parent == null ? 0 : parent.getDepth() + 1);
	}

	/**
	 * Returns a Move instance that moves the tile left to the blank spot
	 * to the right. <BR> If there is no tile left from the blank spot
	 * because it is located at the edge of the board, <code>null</code> is
	 * returned. Also, when the new move equals the parent move,
	 * effectively undo-ing the move altogether, <code>null</code> is
	 * returned as well.
	 * <P>
	 *
	 * @return	a Move instance that moves the tile left from the blank
	 * 	spot.
	 */
	public Move left()
	{
		Move nextStep = null;

		if(blankCol > 0 && !(nextStep = new Move(blankCol-1, blankRow, this)).equals(parent))
			return nextStep;

		return null;
	}

	/**
	 * Returns a Move instance that moves the tile right to the blank spot
	 * to the left. <BR> If there is no tile right from the blank spot
	 * because it is located at the edge of the board, <code>null</code> is
	 * returned. Also, when the new move equals the parent move,
	 * effectively undo-ing the move altogether, <code>null</code> is
	 * returned as well.
	 * <P>
	 *
	 * @return	a Move instance that moves the tile right from the blank
	 * 	spot.
	 */
	public Move right()
	{
		Move nextStep = null;

		if(blankCol < FifteenPuzzle.WIDTH-1 && !(nextStep = new Move(blankCol+1, blankRow, this)).equals(parent))
			return nextStep;

		return null;
	}

	/**
	 * Returns a Move instance that moves the tile above the blank spot
	 * down. <BR> If there is no tile above the blank spot
	 * because it is located at the edge of the board, <code>null</code> is
	 * returned. Also, when the new move equals the parent move,
	 * effectively undo-ing the move altogether, <code>null</code> is
	 * returned as well.
	 * <P>
	 *
	 * @return	a Move instance that moves the tile above the blank
	 * 	spot.
	 */
	public Move up()
	{
		Move nextStep = null;

		if(blankRow > 0 && !(nextStep = new Move(blankCol, blankRow-1, this)).equals(parent))
			return nextStep;

		return null;
	}

	/**
	 * Returns a Move instance that moves the tile below the blank spot
	 * up. <BR> If there is no tile below the blank spot
	 * because it is located at the edge of the board, <code>null</code> is
	 * returned. Also, when the new move equals the parent move,
	 * effectively undo-ing the move altogether, <code>null</code> is
	 * returned as well.
	 * <P>
	 *
	 * @return	a Move instance that moves the tile below the blank
	 * 	spot.
	 */
	public Move down()
	{
		Move nextStep = null;

		if(blankRow < FifteenPuzzle.HEIGHT-1 && !(nextStep = new Move(blankCol, blankRow+1, this)).equals(parent))
			return nextStep;

		return null;
	}

	/**
	 * Checks whether a given move equals this move. Returns true if the
	 * given move moves the same tile as this move, regardless of the
	 * parent moves. This method will return true even if both moves move
	 * the same tile, even though all parent moves are completely
	 * different.
	 *
	 * @param	move a move that might be equals to this move.
	 * @return	true if the given move moves the same tile as this move.
	 */
	public boolean equals(Move move)
	{
		return move != null && (move.blankCol == blankCol && move.blankRow == blankRow ? true : false);
	}

	/**
	 * Returns this move, including all parent moves recursively, leading to
	 * the complete path from the root to the current move. It is useful to
	 * call this method when a solution is found to obtain the complete path
	 * and possibly verify the validity of the path. <BR>
	 * A path string may look like this: (1, 1), (0, 1), (0, 2), (1, 2)
	 *
	 * @return	this move, including all parent moves recursively.
	 */
	public String toString()
	{
		return (parent == null ? "" : parent.toString() + "(" + blankCol + ", " + blankRow + ") ");
	}
}
