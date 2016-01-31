package util;

/**
 * Data class to hold to values as a pair
 * 
 * @author Lars George
 * @version 1.0
 * 
 * @param <T>
 *            First value
 * @param <S>
 *            Second value
 */
public class Pair<T, S>
{
	public T	firstValue	= null;
	public S	secondValue	= null;

	/**
	 * Constructor that inits the values
	 * 
	 * @param first
	 * @param second
	 */
	public Pair(T first, S second)
	{
		this.firstValue = first;
		this.secondValue = second;
	}
}
