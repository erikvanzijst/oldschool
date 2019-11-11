package nl.vu.pp.multithreaded;

import java.util.HashMap;
import java.util.Arrays;

/**
 * @author	Erik van Zijst - erik@prutser.cx - 03.jan.2003
 */
public class Job
{
	private static HashMap jobPool;
	private int[][] board;
	private int[][] solution;
	private int distance = 0;
	private int depth = 0;
	private int maxJobDepth = -1;
	private int bound;
	private boolean prune = false;
	private Move move = null;

	static
	{
		jobPool = new HashMap();
	}

	private Job()
	{
		board = new int[FifteenPuzzle.WIDTH][FifteenPuzzle.HEIGHT];
	}

	public Job(int[][] board, int[][] solution, int bound)
	{
		this.board = board;
		this.solution = solution;
		this.bound = bound;
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
	 * only one job instance per thread at a time, the depth, together with
	 * the thread id is used to determine
	 * which instance can be re-used as the application does not explicitly
	 * need to give instances back to the pool.
	 * <P>
	 * N.B. <BR>
	 * The implementation of this method uses a static HashMap to map
	 * arrays of pre-allocated job instances to individual threads. For the
	 * typical 15-puzzle (i.e. 4 x 4 grid) this factory mechanism is over 3
	 * times faster than creating a new job instance.
	 * <P>
	 *
	 * @param	depth the current depth of the game tree.
	 * @return	a Job instance that can be initialized and used.
	 */
	private Job instance(int depth)
	{
		Job[] jobs = (Job[])jobPool.get(Thread.currentThread());

		try
		{
			return jobs[depth];
		}
		catch(Exception e)
		{
			jobs = new Job[bound+2];
			for(int nX = 0; nX < bound+2; nX++)
				jobs[nX] = new Job();
			jobPool.put(Thread.currentThread(), jobs);

			return jobs[depth];
		}
	}
	
	/**
	 * This method specifies how far a job should be expanded by a single
	 * thread. If during execution a job's depth exceeds this value, the
	 * execution is suspended and the job instance is put (back) into the
	 * central job queue. <BR>
	 * A value of -1 disables the maximum depth and causes a thread to keep
	 * expanding a job until either the job's bound is reached or the
	 * puzzle is solved. Normally, a worker thread that has popped a
	 * pending job from the execution queue will first set this value to -1
	 * and then invoke <code>solve()</code>.
	 * <P>
	 */
	public void setMaxJobDepth(int maxDepth)
	{
		maxJobDepth = maxDepth;
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
		maxJobDepth = parent.maxJobDepth;
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
	 * It will create and solve up to (bound - depth) new job levels
	 * recursively and search for all solutions in its game tree.
	 * <P>
	 */
	public void solve()
	{
		if(prune)
			return;
			
		// this job has executed long enough, put it (back) on the
		// execution queue
		if(depth == maxJobDepth)
		{
			FifteenPuzzle.getJobQueue().push(deepcopy());
			return;
		}

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
	
	/**
	 * Creates a deep-copy of the job object. <BR>
	 * Since job instances are pre-allocated and are associated to a
	 * specific thread, they cannot be passed to other threads. This means
	 * one must first make a deep-copy of a specific instance that is not
	 * related to any job pool or thread, before passing it to a different
	 * thread or job pool.
	 * <P>
	 *
	 * @return	a deep-copy of this job object that is not associated
	 * 	to any job pool or thread.
	 */
	public Job deepcopy()
	{
		Job job = new Job();
		job.prune = prune;
		job.distance = distance;
		job.depth = depth;
		job.bound = bound;
		job.maxJobDepth = maxJobDepth;
		job.solution = solution;
		job.move = move;

		// deep-copy board
		for(int nX = 0; nX < board.length; nX++)
			System.arraycopy(board[nX], 0, job.board[nX], 0, job.board[nX].length);

		return job;
	}
}
