
/**
* This interface gives the functionality of a real valued black
* box problem. It defines a real valued function over the real domain
* <code>[0,1]^n()</code>.
*/
public interface RealBlackBoxProblem {

	/** This gives the dimensionality of the real search domain. */
	int n();

	/**
	* This gives the best known lower bound of the function.
	* Might return <code>Double.NEGATIVE_INFINITY</code>.
	* In case of stochastic functions this should be understood as the
	* lower bound of the expected value.
	*/
	double lower();
	
	/**
	* This gives the best known upper bound of the function.
	* Might return <code>Double.POSITIVE_INFINITY</code>.
	* In case of stochastic functions this should be understood as the
	* upper bound of the expected value.
	*/
	double upper();
	
	/**
	* This gives the function value for the given double vector.
	* The length of <code>solution</code> must be
	* <code>n()</code>,
	* and the i-th element of the vector must be in the interval
	* <code>[0,1]</code>.
	* @throws IllegalArgumentException If the vector is null or its length
	* is not <code>n()</code> or if one of the vector elements is outside
	* <code>[0,1]<code>.
	*/
	double eval( double[] solution );
}

