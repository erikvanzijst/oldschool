package nl.vu.pp.sequential;

import java.util.Arrays;

/**
 * The Job class implements the Iterative Deepening A-Star (IDA*) algorithm
 * [Korf, 1985] to solve the 15-puzzle. After an initial Job instance has been
 * created with a given, shuffled board, the board's goal and a maximum depth
 * (bound), the <code>solve()</code> method will search the game tree up to and
 * including the given bound and return. Everytime a solution is found, the
 * implementation will invoke <code>FifteenPuzzle.addSolution()</code>.
 * <P>
 *
 * @author	Erik van Zijst - erik@prutser.cx - 28.dec.2002
 */
public class Job
{
	private static Job[] preAllocatedJobs = null;
	private int[][] board;
	private int[][] solution;
	private int distance = 0;
	private int depth = 0;
	private int bound;
	private boolean prune = false;
	private Move move = null;

	/**
	 * Private constructor that is only used by this class internally to
	 * create a pool of pre-allocated jobs.
	 */
	private Job()
	{
		board = new int[FifteenPuzzle.WIDTH][FifteenPuzzle.HEIGHT];
	}

	/**
	 * Used to create the initial (root) job from which the IDA* algorithm
	 * is run and the game tree is unfolded. <BR>
	 * Invoke <code>solve()</code> to start the algorithm.
	 * <P>
	 *
	 * @param	board the shuffled board that needs to be solved.
	 * @param	solution the board's goal layout.
	 * @param	bound the maximum depth for the IDA* algorithm.
	 */
	public Job(int[][] board, int[][] solution, int bound)
	{
		this.board = board;
		this.solution = solution;
		this.bound = bound;

		preAllocatedJobs = new Job[bound+2];
		for(int nX = 0; nX < bound+2; nX++)
			preAllocatedJobs[nX] = new Job();

		distance = FifteenPuzzle.getTotalDistance(board, solution);

		// create initial move; this move has the coordinates of the
		// blank tile
		int row, col;
		for(row = 0; row < board.length; row++)
			for(col = 0; col < board[row].length; col++)
				if(board[col][row] == 0)
				{
					move = new Move(col, row);
					return;
				}
	}

	/**
	 * Factory method that handles job creation. Since every step needs a
	 * new Job instance, this factory maintains a pool of pre-allocated
	 * jobs that can be re-used. Since every level in the game tree needs
	 * only one job instance at a time, the depth is used to determine
	 * which instance can be re-used as the application does not explicitly
	 * need to give instances back to te pool.
	 * <P>
	 *
	 * @param	depth the current depth of the game tree.
	 * @return	a Job instance that can be initialized and used.
	 */
	private static Job instance(int depth)
	{
		return preAllocatedJobs[depth];
	}

	/**
	 * Since instances of this class are re-used multiple times, an
	 * instance needs to be explicitly initialized everytime it is obtained
	 * through the factory method <code>instance()</code>. <BR>
	 * Initializing a job requires specifying a parent job and the next
	 * move (subtree) that the job needs to test.
	 * <P>
	 *
	 * @param	parent the parent job that this job was spawned by.
	 * @param	move the next move that his job needs to evaluate.
	 */
	private Job init(Job parent, Move move)
	{
		prune = false;
		distance = parent.distance;
		depth = parent.depth;
		bound = parent.bound;
		solution = parent.solution;
		this.move = move;
		
		if(distance == 0)
		{
			FifteenPuzzle.addSolution(move.getParent());
			prune = true;
			return this;
		}

		// deep-copy board
		for(int nX = 0; nX < board.length; nX++)
			System.arraycopy(parent.board[nX], 0, board[nX], 0, board[nX].length);

		// is it worth making the move?
		if(depth + distance > bound)
		{
			// this move would eventually exceed the maximum depth
			prune = true;
			return this;
		}
		else
		{
			// yes it is; make the new move
			int value = board[move.getCol()][move.getRow()];
			board[move.getParent().getCol()][move.getParent().getRow()] = value;
			board[move.getCol()][move.getRow()] = 0;
			// we've made the move, increase depth
			depth++;
			// total distance after the move is
			// distance = distance - tile's old distance + tile's new distance
			distance = distance - FifteenPuzzle.getManhattanDistance(value, move.getCol(), move.getRow(), solution) + FifteenPuzzle.getManhattanDistance(value, move.getParent().getCol(), move.getParent().getRow(), solution);
		}

		return this;
	}

	/**
	 * Returns the depth in the game tree of this job. The "root job" will
	 * always return 0 (zero).
	 * <P>
	 *
	 * @return	the depth in the game tree of this job.
	 */
	public int getDepth()
	{
		return depth;
	}

	/**
	 * Returns the algorithm's bound. The bound determines how deep the
	 * program will descent into the game tree before pruning its path. It
	 * is a property of many depth-first algorithms.
	 * <P>
	 *
	 * @return	the algorithm's bound.
	 */
	public int getBound()
	{
		return bound;
	}

	/**
	 * Invoke this method to solve the job. <BR>
	 * It will create and solve up to (bound - depth) new jobs recursively
	 * and search for all solutions in its game tree.
	 * <P>
	 */
	public void solve()
	{
		if(prune)
			return;

		// try all branches, invalid steps (out-of-bound and parent move)
		// are skipped
		Move step;

		step = move.left();
		if(step != null)
			instance(depth).init(this, step).solve();

		step = move.right();
		if(step != null)
			instance(depth).init(this, step).solve();

		step = move.up();
		if(step != null)
			instance(depth).init(this, step).solve();

		step = move.down();
		if(step != null)
			instance(depth).init(this, step).solve();
	}
}
