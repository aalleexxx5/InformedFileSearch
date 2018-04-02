package net.ximias;

import net.ximias.fileSearch.SearchProperties;
import net.ximias.fileSearch.Searcher;

import java.io.File;
import java.util.Scanner;

/**
 * We write the priorities in decreasing order, thus we guesstimate the path in reverse order
 * with decreasing confidence. Said in another way:
 * We are less and less confident, that the folder name is part of the path to the goal.
 */
public class BetterApproach {
	public static void main(String[] args) {
		String goal = "javac.exe";
		String[] priorities = {"bin", "jdk*" , "java*", "program*"};
		String[] exclusions = {"windows*", "driver*", "game*"};
		SearchProperties properties = new SearchProperties(goal,priorities, exclusions);
		Searcher searcher = new Searcher(properties);
		
		File result = searcher.performSearch();
		if (result == null) {
			System.out.println("Too bad");
		}else{
			System.out.println("Search result: "+result.getAbsolutePath());
		}
	}
}
