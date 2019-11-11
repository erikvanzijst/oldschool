
import java.util.Properties;
import java.util.Random;

/**
* This algorithm is a simple example for using the class EA2002.
* Look out: this algorithm would fail on the exam!
*/
public class SampleEA implements Runnable {


// ==================== Private fields ==========================
// ==============================================================

/** constant for stop condition */
private final int maxEval; 

// ==================== Protected fields ========================
// ==============================================================

protected RealBlackBoxProblem bbp = null;

protected Properties cfg = null;

protected long evals = 0;

protected double[] current = null;

protected double currentVal = -1;

protected double[] best = null;

protected double bestVal = -1;

protected Random rand = null;

// ==================== Protected methods =======================
// ==============================================================


protected boolean stopCondition() {

	return  evals>=maxEval || currentVal>=bbp.upper();
}

// --------------------------------------------------------------

protected boolean replace( double bi ) {

	return bi >= currentVal; 
}

// --------------------------------------------------------------

/** random bool array of length l using randomness r */
protected static double[] generateInitial( int l, Random r ) {

	double[] c = new double[l];
	for(int i=0; i<l; ++i) c[i] = r.nextDouble();
	return c;
}

// --------------------------------------------------------------

public double[] mutate( double[] current ) {

	double[] result = new double[current.length];
	System.arraycopy(current,0,result,0,current.length);

	int i = rand.nextInt(result.length);
	result[i]+=(2*rand.nextDouble()-1)/10;
	if( result[i]<0 ) result[i]=0;
	if( result[i]>1 ) result[i]=1;
	return result;
}

// ==================== Public Constructors =====================
// ==============================================================


/**
* The following properties are used.
* <ul>
* <li>SampleEA.randomSeed: The default is the current time.
* If the parameter <code>random</code> is not null then this random seed
* is ignored and the given randomness is used.</li>
* <li>SampleEA.maxEval: The maximal number of evaluations to be performed.
* The algorithm may exit earlier eg if an optimal solution is found.
* The default is 1000000.</li>
* </ul>
*/
public SampleEA( Properties p, RealBlackBoxProblem bbp, Random random ){

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
		   "SampleEA.randomSeed",""+System.currentTimeMillis())));
	}
	
	maxEval = Integer.parseInt(
		cfg.getProperty("SampleEA.maxEval","1000000"));
	
	current = generateInitial( bbp.n(), rand );
	currentVal = bbp.eval(current);
}

// --------------------------------------------------------------

/**
* Uses default randomness initialised using the current time,
* and default properties
*/
public SampleEA( RealBlackBoxProblem bbp ) {
	
	this( null, bbp, null );
}

// --------------------------------------------------------------

/**
* Uses default randomness initialised using the current time,
* and default properties
*/
public SampleEA( RealBlackBoxProblem bbp, Random r ) {
	
	this( null, bbp, r );
}

// ==================== Public methods ==========================
// ==============================================================

public void run() {

	best = new double[current.length];
	bestVal = currentVal;
	System.arraycopy(current,0,best,0,current.length);
	
	while( !stopCondition() )
	{
		double[] n = mutate( current );
		double nval = bbp.eval(n);
		if( replace( nval ) )
		{
			current = n;
			currentVal = nval;
			
			// printing progress info to stderr
			System.err.print("evals= "+evals+" bestval= "+nval);
			System.err.print("\r");
		}
		++evals;
	}
	
	if( bestVal < currentVal )
	{
		bestVal = currentVal;
		System.arraycopy(current,0,best,0,current.length);
	}

	System.out.print("bestval= "+bestVal+" solution:\t");
	for(int j=0; j<current.length; ++j)System.out.print( best[j]+" ");
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
	SampleEA ea = new SampleEA( bbp );
	ea.run();
}
catch( Throwable th ) { th.printStackTrace(); }
}


}
