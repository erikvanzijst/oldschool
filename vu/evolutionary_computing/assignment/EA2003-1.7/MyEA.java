
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
protected double lowVal = Double.MAX_VALUE;

protected Random rand = null;

protected int populationSize = 1;

protected boolean mutationEnabled = true;
protected double mutationParameter = 1;
protected double[] mutationParameters = null;
protected double mutationParameterExponent = 1;

protected double recombinationParameter = 0;
protected boolean recombinationEnabled = false;
protected boolean recombinationMatingPoolEnabled = false;

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

public void xover(double[][] pop)
{
	if (recombinationMatingPoolEnabled)
	{
		double sum = 0;
        	// use Fitness Proportional Selection with Windowing Down (page 59)
        	// to compute relative fitness
        	for(int nX = 0; nX < pop.length; nX++)
        	{
                	currentVal[nX] -= lowVal;
                	sum += currentVal[nX];
        	}
        	for(int nX = 0; nX < pop.length; nX++)
        	{
                	currentVal[nX] = currentVal[nX] / sum;
        	}
	
        	// create a new population based on fitness selection
        	double[][] pool = new double[pop.length][pop[0].length];
        	for(int nX = 0; nX < pop.length; nX++)
        	{
                	double value = currentVal[0];
                	double rnd = rand.nextDouble();
                	for(int mX = 0; mX < pop.length; mX++)
                	{
                        	if(rnd < value || (mX == pop.length - 1))
                        	{
                                	System.arraycopy(pop[mX], 0, pool[nX], 0, pop[0].length);
					//System.out.println("Selecting individual #" + mX + " for new pool.");
                                	break;
                        	}
                        	else
                        	{
                                	value += currentVal[mX + 1];
                        	}
                	}
        	}
        	pop = pool;      // new population is automatically shuffled
	}

        for(int nX = 0; nX < pop.length; nX += 2)
        {
                for(int mX = 0; mX < pop[nX].length; mX++)
                {
                        double child1 = recombinationParameter * pop[nX][mX] + (1.0 - recombinationParameter) * pop[nX+1][mX];
                        double child2 = recombinationParameter * pop[nX+1][mX] + (1.0 - recombinationParameter) * pop[nX][mX];
                        pop[nX][mX] = child1;
                        pop[nX+1][mX] = child2;
                }
        }
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
	populationSize = Integer.parseInt(cfg.getProperty("population.size", "2"));
	mutationParameters = new double[populationSize];
	evalsWithoutProgress = new long[populationSize];
	currentVal = new double[populationSize];

	recombinationEnabled = Boolean.valueOf(cfg.getProperty("recombination.enabled", "true")).booleanValue();
	recombinationMatingPoolEnabled = Boolean.valueOf(cfg.getProperty("recombination.matingpool.enabled", "false")).booleanValue();
	recombinationParameter = Double.parseDouble(cfg.getProperty("recombination.parameter", "0.7"));
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
                if (lowVal > currentVal[i]) lowVal = currentVal[i];
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
			//children = recombination(current, recombinationParameter);
			xover(children);
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
				//if (evalsWithoutProgress[i] % 10000 == 0) mutationParameters[i] = 1.01 * mutationParameters[i];
			}
			if( bestVal < currentVal[i] )
			{
				bestVal = currentVal[i];
				System.arraycopy(current[i],0,best,0,current[i].length);
				// printing progress info to stderr
				System.err.print("evals= "+evals+" bestval= "+nval+" mutationParameter= "+mutationParameters[i]+" progress= "+(currentVal[i]/bbp.upper())+"     ");
				System.err.print("\r");
			}
                        if ( lowVal > currentVal[i] ) lowVal = currentVal[i];
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
