package net.ximias;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Reasoning about the naive approach:
 *
 * This works, but it can be optimized by adding a bit more breadth-iness to it in the following way:
 *  If a subdirectory has no more priority files, it should be continued later when no other directory has any priority.
 *  This will me more efficient, as long as the inner directories are on the priority list.
 *  It will however be much slower, if a single directory is left out.
 * Example:
 *  Say we're looking for java/jdk/bin/javac.exe
 *  The priority list must contain in order: bin, jdk, java.
 *  If the bin directory is left out, the search will skip out of the directory and search elsewhere.
 *  As a consequence, the search will search all parents in the search tree before returning to the bin directory
 * Tradeoff:
 *  If search is well-informed, will be much faster, as it will have different priorities.
 *  If search is missing a bit of information, it will match all other priorities before resuming the search.
 * Reasoning why the tradeoff is worth it.
 *  Within installed programs, the file structure is extremely predictable;
 *  We know that there will be a bin folder in the jdk directory, and the javac.exe file is in there.
 *  What we are less sure about is the file structure outside the jdk directory, as this is influenced by the user.
 *  Users will often change the install directory to other locations, but the tail end of the path will remain the same.
 *  Thus, we are increasingly certain of the tail-end of the final path, and the more breadth-y search will be well worth it.
 */
public class NaiveApproach {
	private final String goal = "javac.exe";
	private final String[] exclutions = {"windows*", "driver*", "document*", "game*"};
	private final String[] priorities = {"bin", "jdk*", "java*", "program*"};
	private File goalFile = null;
	
    public static void main(String[] args){
	    NaiveApproach searcher = new NaiveApproach();
	    for (File file : File.listRoots()) {
		    System.out.println(file.getAbsolutePath());
	    }
    	for (File file : File.listRoots()) {
		    if (searcher.isGoalInSubDirectory(file)) break;
	    }
	    System.out.println("Found goal: "+searcher.getGoalFile());
    }
    
    public File getGoalFile(){
    	if (goalFile == null) throw new Error("Search found nothing");
    	return goalFile;
    }
    
    private boolean isGoalInSubDirectory(File file){
	    TreeMap<Integer, ArrayList<File>> sortedMap = new TreeMap<Integer, ArrayList<File>>();
	    System.out.println(file.getAbsolutePath());
	    
	    if (file.isDirectory()){
    		File[] subFiles = file.listFiles();
    		if (subFiles == null) return false;
		
		    for (File subFile : subFiles) {
			    if (subFile.isHidden()) continue;
			    
		    	if (subFile.isDirectory()){
				    addDirectoryByPriority(sortedMap, subFile);
				    addDirectoryIfNotExcluded(sortedMap, subFile);
			    }else{
				    if (isFileGoal(subFile)) return true;
			    }
		    }
		    for (ArrayList<File> files : sortedMap.values()) {
			    for (File it : files) {
				    if (isGoalInSubDirectory(it)) return true;
			    }
		    }
	    }
	    return false;
    }
	
	private boolean isFileGoal(File subFile) {
		if (subFile.getName().equals(goal)) {
			goalFile = subFile;
			return true;
		}
		return false;
	}
	
	private void addDirectoryIfNotExcluded(TreeMap<Integer, ArrayList<File>> sortedMap, File subFile) {
		for (String exclution : exclutions) {
			if (!applyRegex(exclution, subFile.getName())){
				addToMap(sortedMap, subFile, priorities.length+1);
			}
		}
	}
	
	private void addDirectoryByPriority(TreeMap<Integer, ArrayList<File>> sortedMap, File subFile) {
		for (int i = 0; i < priorities.length; i++) {
			String priority = priorities[i];
			if (applyRegex(priority, subFile.getName())){
				addToMap(sortedMap, subFile, i);
			}
		}
	}
	
	private boolean applyRegex(String regex, String applicant){
    	if (regex.endsWith("*")){
    		return applicant.toLowerCase().contains(regex.substring(0,regex.length()-1).toLowerCase());
	    }else return applicant.equalsIgnoreCase(regex);
    	
    }
	
	private void addToMap(TreeMap<Integer, ArrayList<File>> sortedMap, File subFile, int i) {
		sortedMap.computeIfAbsent(i, k -> new ArrayList<File>());
		if (sortedMap.get(i).contains(subFile)) return;
		sortedMap.get(i).add(subFile);
	}
}
