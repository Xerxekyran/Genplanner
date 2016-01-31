package genplanner.ga.constraint;

import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.IGeneConstraintChecker;

/**
 * A IGeneConstraintChecker implementation that checks for fixed route
 * manipulations !! NOT YET IMPLEMENTED !!
 * 
 * @author Lars George
 * @version 0.1
 * 
 */
public class FixedRouteChecker implements IGeneConstraintChecker
{
	private static final long	serialVersionUID	= 1L;

	@Override
	public boolean verify(Gene gene, Object allele, IChromosome chrom, int position)
	{
		// System.out.println("FixedRouteChecker, Old ["+
		// gene.getAllele().toString() +"] New ["+ allele +"]");
		return true;
	}

}
