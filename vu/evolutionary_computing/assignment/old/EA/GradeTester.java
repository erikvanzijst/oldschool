import java.util.Random;
import java.lang.reflect.*;

public class GradeTester {

/**
* Runs a students class once until it exits and prints stats about the
* first 1000000 evaluations.
* The first parameter is the random seed, the second is a class name
* which should conform to the specs given to students, that is it has
* to have a constructor with two pars: RealBlackBoxProblem and Random.
* The output lines all start with "MMWWMM " to allow filtering it out
* from the eventual other student output. The three numbers output
* by the algorithm are the evaluation number used by the algorithm,
* the max value, and the max value truncated to integer.
*/
public static void main( String[] pars ) {
try
{
	int[] results = new int[10];
	Class c = Class.forName(pars[1]);
	long randSeed = Long.parseLong(pars[0]);
	Random rand = new Random();
	Class cpars[] = {Class.forName("RealBlackBoxProblem"),rand.getClass()};
	
	// --- calculating the 10 grades
	for(int i=0; i<10; ++i)
	{
		StatCollectorWrapper bbp = new StatCollectorWrapper(
			new EA2002(), 1000000 );
		rand.setSeed( randSeed );
		Constructor cons = c.getConstructor( cpars );
		Object objpars[] = { bbp, rand };
		Runnable alg = (Runnable)cons.newInstance( objpars );
		alg.run();
		System.out.println(
			"\nMMWWMM run: "+i+" evals: "+bbp.getEvals()+
			" max: "+bbp.getMax()+" grade: "+
			(results[i]=(int)Math.floor(bbp.getMax())) );
		randSeed += 10000;
	}

	// --- throwing away the worst and calculating average
	int sum = 0;
	int min = 20;
	for(int i=0; i<10; ++i )
	{
		sum += results[i];
		if(min>results[i]) min = results[i];
	}
	
	System.out.println("\nMMWWMM Worst run: "+min);
	System.out.println("\nMMWWMM **** the grade you get: "+
		Math.round((sum-min)/9) );
	
}
catch( Throwable e )
{
	e.printStackTrace();
}
}

}
