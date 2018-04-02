package net.ximias.fileSearch;

/**
 * Defines the properties of a search.
 */
public class SearchProperties {
	public final String goal;
	public final String[] exclusions;
	public final String[] priorities;
	
	/**
	 * @param goal The file to locate, including extention.
	 * @param priorities The directory names with precedence.
	 * @param exclusions The directory names to be ignored in search.
	 */
	public SearchProperties(String goal, String[] priorities, String[] exclusions) {
		this.goal = goal;
		this.exclusions = exclusions;
		this.priorities = priorities;
	}
}
