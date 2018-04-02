package net.ximias.fileSearch;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Searcher {
	private SearchProperties properties;
	
	public Searcher(SearchProperties properties) {
		this.properties = properties;
	}
	
	public File performSearch(){
		File[] roots = File.listRoots();
		Node[] rootNodes = new Node[roots.length];
		ArrayList<Node> bredth = new ArrayList<>(200);
		for (int i = 0; i < roots.length; i++) {
			File root = roots[i];
			rootNodes[i] = new Node(root, properties);
		}
		
		bredth.addAll(Arrays.asList(rootNodes));
		while (!bredth.isEmpty()){
			for (Node rootNode : bredth) {
				if (rootNode.hasPriority()){
					if (rootNode.expandPriority()){
						return rootNode.getGoalFile();
					}
				}
			}
			
			for (Node rootNode : bredth) {
				if (rootNode.expand()){
					return rootNode.getGoalFile();
				}
			}
			ArrayList<Node> next = new ArrayList<>(200);
			for (Node node : bredth) {
				next.addAll(node.getAllChildren());
			}
			System.out.println("No result in prioritized directories. Expanded search to: "+next.size()+" directories.");
			bredth = next;
		}
		System.out.println("Search found no results");
		return null;
	}
}
