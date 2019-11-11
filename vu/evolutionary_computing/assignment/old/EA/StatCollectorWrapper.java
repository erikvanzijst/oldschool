
public class StatCollectorWrapper implements RealBlackBoxProblem {

// ================== private fields =============================
// ===============================================================

private final RealBlackBoxProblem p;

private final long maxeval;

private long evals = 0;

private double min;

private double max;

// ================== constructor ================================
// ===============================================================

public StatCollectorWrapper( RealBlackBoxProblem p, long maxeval ) {
	
	this.p = p;
	this.maxeval = maxeval;
}

// ================== public RealBlackBox functions ==============
// ===============================================================

public int n() { return p.n(); }

public double lower() { return p.lower(); }

public double upper() { return p.upper(); }
	
/**
* Collects stats (min, max, and number of evaluations). After the number of
* evals exceeds the given limit, does not modify max and min anymore.
*/
public double eval( double[] solution ) {

	if( ++evals > maxeval ) return 0.0;

	double y = p.eval(solution);

	if( evals == 1 )
	{
		max = min = y;
	}
	else
	{
		if( max < y ) max = y;
		if( min > y ) min = y;
	}

	return y;
}

// ==================== public functions ===========================
// =================================================================


public long getEvals() { return evals; }

public double getMax() { return max; }

public double getMin() { return min; }


}

