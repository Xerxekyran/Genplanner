package genplanner.ga.constraint;

import java.util.LinkedList;

import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.IGeneConstraintChecker;

/**
 * The GeneConstraintCheckerList is an list object that can be filled with
 * IGeneConstrainChecker. This way a Gene can have multiple ConstraintChecker.
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class GeneConstraintCheckerList implements IGeneConstraintChecker
{
	private static final long					serialVersionUID	= 1L;
	private LinkedList<IGeneConstraintChecker>	constraintChecker	= null;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * Default ctor
	 */
	public GeneConstraintCheckerList()
	{
		this.constraintChecker = new LinkedList<IGeneConstraintChecker>();
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------
	public void addConstraintChecker(IGeneConstraintChecker newChecker)
	{
		this.constraintChecker.add(newChecker);
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------

	@Override
	public boolean verify(Gene gene, Object alleleValue, IChromosome chrom, int position)
	{
		boolean ret = true;

		// ask every constraint checker in the list, if one returns false, the
		// return value is false
		for (IGeneConstraintChecker checker : constraintChecker)
		{
			if (!checker.verify(gene, alleleValue, chrom, position))
			{
				ret = false;
				break;
			}
		}

		return ret;
	}

}
