
import java.util.Properties;
import java.util.Random;

/**
* This algorithm is a simple example for using the class EA2002.
* Look out: this algorithm would fail on the exam!
*/
public class MyEA implements Runnable {


// ==================== Private fields ==========================
// ==============================================================

/** constant for stop condition */
private final int maxEval; 

// ==================== Protected fields ========================
// ==============================================================

protected RealBlackBoxProblem bbp = null;

protected Properties cfg = null;

protected long evals = 0;

protected double[][] current = null;

protected double[] currentVal = null;

protected double[] best = null;

protected double bestVal = -1;

protected Random rand = null;

protected int populationSize = 1;

protected boolean mutationEnabled = true;
protected double mutationParameter = 1;
protected double[] mutationParameters = null;
protected double mutationParameterExponent = 1;

protected double recombinationParameter = 0;
protected boolean recombinationEnabled = false;

protected long[] evalsWithoutProgress = null;

// ==================== Protected methods =======================
// ==============================================================


protected boolean stopCondition() {

	return  evals>=maxEval || bestVal>=bbp.upper();
}

// --------------------------------------------------------------

protected boolean replace( double bi , double cv) {

	return bi >= cv; 
}

// --------------------------------------------------------------

/** random bool array of length l using randomness r */
protected static double[][] generateInitial( int k, int l, Random r ) {

	double[][] c = new double[k][l];
	for (int i = 0; i < k; i++)
	{
		for(int j = 0; j < l; j++) 
		{
			c[i][j] = r.nextDouble();
		}
	}
	return c;
}

// --------------------------------------------------------------

public double[] mutate( double[] current , double p)
{
	double[] result = new double[current.length];
	System.arraycopy(current,0,result,0,current.length);

	// Nonuniform mutation based on a Gaussian distribution with SD = 1.0 * p
	for (int i = 0; i < result.length; i++)
	{
        	result[i] += (p * rand.nextGaussian());
        	if( result[i]<0 ) result[i]=0;
        	if( result[i]>1 ) result[i]=1;
	}

	return result;
}

// --------------------------------------------------------------

public double[][] recombination ( double[][] current, double p)
{
	double [][] result = new double[current.length][current[0].length];

	for (int i = 0; i < current.length; i++)
	{
		for (int j = 0; j < current[0].length; j++)
		{
			if (rand.nextDouble() > p)
			{
				result[i][j] 				= current[(i+1) % current.length][j];
				result[(i+1) % result.length][j] 	= current[i][j];
			}
			else
			{
				result[i][j] = current[i][j];
			}
		}
	}

	return result;
}


// ==================== Public Constructors =====================
// ==============================================================


/**
* The following properties are used.
* <ul>
* <li>MyEA.randomSeed: The default is the current time.
* If the parameter <code>random</code> is not null then this random seed
* is ignored and the given randomness is used.</li>
* <li>MyEA.maxEval: The maximal number of evaluations to be performed.
* The algorithm may exit earlier eg if an optimal solution is found.
* The default is 1000000.</li>
* </ul>
*/
public MyEA( Properties p, RealBlackBoxProblem bbp, Random random ){

	if( p != null ) cfg = p;
	else cfg = new Properties();
	if( bbp == null ) throw new IllegalArgumentException(
		"null Problem instance");
	else this.bbp = bbp;
	
	if( random != null ) rand = random;
	else
	{
		rand = new Random();
		rand.setSeed( Long.parseLong( cfg.getProperty(
		   "MyEA.randomSeed",""+System.currentTimeMillis())));
	}
	
	maxEval = Integer.parseInt(
		cfg.getProperty("MyEA.maxEval","1000000"));
	
	best = new double[bbp.n()];
	populationSize = Integer.parseInt(cfg.getProperty("population.size", "1"));
	mutationParameters = new double[populationSize];
	evalsWithoutProgress = new long[populationSize];
	currentVal = new double[populationSize];

	recombinationEnabled = Boolean.valueOf(cfg.getProperty("recombination.enabled", "false")).booleanValue();
	recombinationParameter = Double.parseDouble(cfg.getProperty("recombination.parameter", "0.5"));
	System.out.println("Recombination " + (recombinationEnabled ? "enabled" : "disabled"));
	if (recombinationEnabled) System.out.println("Recombination parameter= "+recombinationParameter);

	mutationEnabled = Boolean.valueOf(cfg.getProperty("mutation.enabled", "true")).booleanValue();
	mutationParameter = Double.parseDouble(cfg.getProperty("mutation.parameter", "0.525"));
	mutationParameterExponent = Double.parseDouble(cfg.getProperty("mutation.parameter.exponent", "2.25"));
	System.out.println("Mutation " + (mutationEnabled ? "enabled" : "disabled"));
	if (mutationEnabled) System.out.println("Mutation parameter= "+mutationParameter+" exponent= "+mutationParameterExponent);

	current = generateInitial( populationSize, bbp.n(), rand );
	for (int i = 0; i < populationSize; i++)
	{
		currentVal[i] = bbp.eval(current[i]);
		mutationParameters[i] = mutationParameter;
		evalsWithoutProgress[i] = 0;
		if (replace ( currentVal[i], bestVal)) 
		{
			bestVal = currentVal[i];
			System.arraycopy(current[i],0,best,0,current[i].length);
		}
	}
}

// --------------------------------------------------------------

/**
* Uses default randomness initialised using the current time,
* and default properties
*/
public MyEA( RealBlackBoxProblem bbp ) {
	
	this( null, bbp, null );
}

// --------------------------------------------------------------

/**
* Uses default randomness initialised using the current time,
* and default properties
*/
public MyEA( RealBlackBoxProblem bbp, Random r ) {
	
	this( null, bbp, r );
}

// ==================== Public methods ==========================
// ==============================================================

public void run() {

	
	while( !stopCondition() )
	{
		double[][] children = current;
		if (recombinationEnabled) 
		{
			children = recombination(current, recombinationParameter);
		}
		for (int i = 0; i < populationSize; i++)
		{
			double[] n = current[i];
			if (mutationEnabled) n = mutate( children[i], mutationParameters[i] );
			double nval = bbp.eval(n);
			evals++;
			if( replace( nval , currentVal[i]) )
			{
				current[i] = n;
				currentVal[i] = nval;
				evalsWithoutProgress[i] = 0;
				double progress = currentVal[i] / bbp.upper();
				mutationParameters[i] = Math.pow( 1 - progress, mutationParameterExponent) * mutationParameter;
			}
			else
			{
				evalsWithoutProgress[i]++;
				if (evalsWithoutProgress[i] % 10000 == 0) mutationParameters[i] = 1.01 * mutationParameters[i];
			}
			if( bestVal < currentVal[i] )
			{
				bestVal = currentVal[i];
				System.arraycopy(current[i],0,best,0,current[i].length);
				// printing progress info to stderr
				System.err.print("evals= "+evals+" bestval= "+nval+" mutationParameter= "+mutationParameters[i]+" progress= "+(currentVal[i]/bbp.upper())+"     ");
				System.err.print("\r");
			}
		}
	}
	
	System.out.print("bestval= "+bestVal+" solution:\t");
	for(int j=0; j<best.length; ++j)System.out.print( best[j]+" ");
	System.out.println();
}

// --------------------------------------------------------------

/**
* Solves an EA2002 instance, loading a properties file if given in the
* command line.
*/
public static void main(String args[]) {
try {	
	Properties p = null;
	
	if( args.length > 0 )
	{
		java.io.FileInputStream fis = 
			new java.io.FileInputStream(args[0]);
		p = new Properties();
		p.load(fis);
		fis.close();
	}
	EA2002 bbp = new EA2002();
	MyEA ea = new MyEA(p, bbp, null );
	ea.run();
}
catch( Throwable th ) { th.printStackTrace(); }
}


}
