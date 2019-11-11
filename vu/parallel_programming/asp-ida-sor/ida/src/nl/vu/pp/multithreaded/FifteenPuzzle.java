package nl.vu.pp.multithreaded;

import java.io.Reader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

/**
 * This is a sequential implementation of the famous 15-puzzle by Sam Loyd, a
 * recreational mathematician in the 19th century.
 * <P>
 * It contains a single-threaded implementation of the Iterative Deepening
 * A-Star, Depth-First algorithm (IDA*) [Korf, 1985] that looks for the optimal
 * solutions for a given board. <BR>
 * The program is run from this class and takes a plain text file that contains
 * a board layout as argument and optionally a specific goal configuration.
 * <BR>
 * The program will first estimate the minimal number of required moves and use
 * the heuristic as the initial bound for the IDA* algorithm. If no solution is
 * found, the heuristic is incremented by 2 (property of Manhattan distances,
 * the applied heuristic) and the algorithm is restarted.
 * <P>
 * Be careful when supplying randomized board configurations as not every
 * configuration can be solved. <BR>
 * If we consider the empty slot as a tile, there are 16 choices for the top
 * left square, 15 for the second, 14 for the 3rd, etc.  i.e. 16! arrangements
 * in total. Without picking up tiles (i.e. sliding only), only half of these
 * positions are achievable from any starting configuration - still a very
 * large number indeed.
 * <P>
 *
 * @author	Erik van Zijst - erik@prutser.cx - 28.dec.2002
 */
public class FifteenPuzzle
{
	public static final int HEIGHT = 4;
	public static final int WIDTH = 4;
	private static int solutions = 0;
	private static JobQueue jobQueue = new JobQueue();

	/**
	 * This method runs the program. <BR> Start the application like this:
	 * <P>
	 * <PRE>
	 *   $ java nl.vu.pp.FifteenPuzzle -board board.txt [-solution solution.txt]
	 * </PRE>
	 * </P>
	 * If a destination layout is not provided explicitly through the
	 * -solution parameter, the program uses this default goal layout:
	 * <P>
	 * <PRE>
	 *       1  2  3
	 *    4  5  6  7
	 *    8  9 10 11
	 *   12 13 14 15
	 * </PRE>
	 * </P>
	 *
	 * @exception	Exception all exceptions that are raised in the
	 * 	application are uncatched and as a consequence will cause the
	 * 	<code>main()</code> method to exit with a stacktrace.
	 */
	public static void main(String[] argv) throws Exception
	{
		new FifteenPuzzle()._main(argv);
	}

	/**
	 * Usage: $ java nl.vu.pp.FifteenPuzzle -board board.txt [-workers 4] [-solution solution.txt]
	 *
	 */
	private void _main(String[] argv) throws Exception
	{
		int nX, bound, workers = 2, prebranch = 6;
		int[][] board = null;
		int[][] solution = {{0, 4, 8, 12}, {1, 5, 9, 13}, {2, 6, 10, 14}, {3, 7, 11, 15}};

		if(argv.length < 2)
		{
			System.out.println("Usage: $ java nl.vu.pp.FifteenPuzzle -board board.txt [-workers 2] [-prebranch 6] [-solution goal.txt]");
			System.exit(1);
		}

		for(nX = 0; nX < argv.length; nX++)
		{
			if(argv[nX].equals("-board"))
				board = readBoard(new InputStreamReader(new FileInputStream(argv[++nX])));

			if(argv[nX].equals("-workers"))
				workers = Integer.parseInt(argv[++nX]);

			if(argv[nX].equals("-prebranch"))
				prebranch = Integer.parseInt(argv[++nX]);

			if(argv[nX].equals("-solution"))
				solution = readBoard(new InputStreamReader(new FileInputStream(argv[++nX])));
		}

		bound = getTotalDistance(board, solution);
		for(nX = 0; nX < workers; nX++)
			new Worker("Worker-" + nX, jobQueue);

		System.out.println("Solving 15-puzzle multithreaded.\n");
		System.out.println(boardToString(board, "Initial board:") + "\n");
		System.out.println(boardToString(solution, "Goal:") + "\n");
		long start = System.currentTimeMillis();

		for(;;)
		{
			System.out.println("Trying bound " + bound);
			Job job = new Job(board, solution, bound);
			job.setMaxJobDepth(prebranch);
			job.solve();
			jobQueue.join(workers);
			
			if(solutions == 0)
				bound += 2;
			else
				break;
		}
		double time = (double)(System.currentTimeMillis() - start) / 1000.0;
		System.out.println(solutions + " solutions found after " + time + " seconds and " + Move.getNumberOfMoves() + " tested moves.");
	}

	/**
	 * When the IDA* algorithm as implemented in the Job class finds a
	 * solution, this method is invoked together with the move that solved
	 * the game (note that every Move includes its parent move). <BR>
	 * Currently, this method just prints the solution to stdout and
	 * increments the number of solutions.
	 * <P>
	 *
	 * @param	move the Move that solved the game.
	 */
	public synchronized static void addSolution(Move move)
	{
		System.out.println("Solution found after " + move.getDepth() + " moves: " + move.toString());
		solutions++;
	}
	
	public static JobQueue getJobQueue()
	{
		return jobQueue;
	}

	/**
	 * This method takes a two-dimensional board array and formats it to a
	 * human-readable string that could be printed to the console or a
	 * file. The board will be formatted like this:
	 * <P>
	 * <PRE>
	 *   title-string
	 *   10      13              12
	 *   14      5       9       4
	 *   6       1       8       2
	 *   15      11      7       3
	 *</PRE>
	 * </P>
	 *
	 * @param	_board a two-dimensional board array.
	 * @param	title this string will be the board's caption.
	 * @return	the human-readable formatted board.
	 */
	public static String boardToString(int[][] _board, String title)
	{
		StringBuffer sb = (new StringBuffer(title)).append("\n");

		for(int row = 0; row < _board.length; row++)
		{
			for(int col = 0; col < _board[row].length; col++)
				sb.append((_board[col][row] == 0 ? "" : Integer.toString(_board[col][row])) + "\t");
			if(row < _board.length-1)
				sb.append("\n");
		}

		return sb.toString();
	}

	/**
	 * Takes both a shuffled board and its goal position and calculates the
	 * minimum amount of moves that are required to solve the puzzle.
	 * <BR>
	 * Note that this "minimum cost" is highly optimistic as it is derived
	 * from the manhattan distances of all individual tiles.
	 * <P>
	 *
	 * @param	board the shuffled board.
	 * @param	solution the board's goal position.
	 * @return	the minimum amount of moves that are required to bring
	 * 	the given board to its goal configuration.
	 */
	public static int getTotalDistance(int[][] board, int[][] solution)
	{
		int row, col, distance = 0;

		// calculate the total distance to the solution
		for(row = 0; row < board.length; row++)
			for(col = 0; col < board[row].length; col++)
				if(board[col][row] != 0)
					distance += getManhattanDistance(board[col][row], col, row, solution);

		return distance;
	}

	/**
	 * Returns the manhattan distance for a tile at a given position.
	 * <BR>
	 * The method will return the distance between the tile's given
	 * position (x, y) and the tile's destination. <BR>
	 * The manhattan distance is a heuristic based on the shortest path
	 * between two tiles (<code>dx + dy</code>).
	 *
	 * @param	value the value of a given tile.
	 * @param	x the current column of a certain tile.
	 * @param	y the current row of a certain tile.
	 * @param	solution the board's goal position.
	 * @return	the manhattan distance (shortest path) for the current
	 * 	tile.
	 */
	public static int getManhattanDistance(int value, int x, int y, int[][] solution)
	{
		int col,row;

		// get the destination position for this tile
		for(row = 0; row < solution.length; row++)
			for(col = 0; col < solution[row].length; col++)
				if(solution[col][row] == value)
					return Math.abs(col - x) + Math.abs(row - y);

		return 0;
	}

	/**
	 * Reads a board configuration from a Reader. A Reader could be
	 * connected to a file. The file should look like this:
	 * <P>
	 * <PRE>
	 *   4
	 *   4
	 *   1 5 2 3
	 *   9 6 10 7
	 *   4 8 0 11
	 *   12 13 14 15
	 * </PRE>
	 * <P>
	 * Where the first line represents the puzzle's width, the second its
	 * height and the rest of the lines represent the actual board
	 * configuration. The blank (open) tile is represented by the value 0
	 * (zero). Note that the white spaces MUST be one or more spaces. Tabs
	 * are not allowed.
	 * <P>
	 *
	 * @param	in a Reader connected to a plain text file or other
	 * 	resource.
	 * @return	the board configuration as it was read from the Reader.
	 * @exception	IOException thrown whenever something went wrong
	 * 	parsing the board. Note that the parser is extremely picky and
	 * 	does often not give very helpful messages.
	 */
	protected int[][] readBoard(Reader in) throws IOException
	{
		BufferedReader br = new BufferedReader(in);
		String line;

		try
		{
			int row = 0;
			int[][] board = new int[WIDTH][HEIGHT];

			while((line = br.readLine()) != null && row < HEIGHT)
				if(line.length() > 0)
				{
					StringTokenizer strtok = new StringTokenizer(line, " ");
					for(int col = 0; col < WIDTH; col++)
						board[col][row] = Integer.parseInt(strtok.nextToken());
					row++;
				}

			return board;
		}
		catch(NumberFormatException e)
		{
			throw new IOException("Error reading board: " + e.getMessage());
		}
		catch(NoSuchElementException e)
		{
			throw new IOException("Error reading board: " + e.getMessage());
		}
	}
}
