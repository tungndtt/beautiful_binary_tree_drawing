package main;

import processing.Draw;
import processing.DrawMode;
import processing.DrawFactory;
import processing.TreeNode;

public class Main {
	
	private static final String filePath = "beautiful_binary_tree.txt";
	
	private static DrawMode mode = DrawMode.DIRECTORY;
	
	public static void main(String[] args) {
		
		// create tree node
		TreeNode<String> root = Main.createTreeFromFile();
		
		// draw tree
		DrawFactory drawFactory = new DrawFactory(1,2);
		Draw drawer = drawFactory.getDrawer(mode);
		
		boolean success = drawer.drawToFile(filePath, root);
		
		if(success) System.out.print("Open your file to see the masterpiece!");
		else System.out.print("Oh! Somethings went wrong!");
		
	}
	
	@SuppressWarnings("unused")
	private static TreeNode<Integer> manuallyCreateTree() {
		TreeNode<Integer> lrll = new TreeNode<>(null, null, 4568);
		TreeNode<Integer> lrl = new TreeNode<>(lrll, null, 123);
		TreeNode<Integer> lrrr = new TreeNode<>(null, null, 24242);
		TreeNode<Integer> lrr = new TreeNode<>(null, lrrr, 12345);
		TreeNode<Integer> lr = new TreeNode<>(lrl, lrr, 4567);
		
		TreeNode<Integer> llrr = new TreeNode<>(null, null, 46275);
		TreeNode<Integer> llr = new TreeNode<>(null, llrr, 2427542);
		TreeNode<Integer> ll = new TreeNode<>(null, llr, 10789);
		TreeNode<Integer> l = new TreeNode<>(ll, lr, 3512);
		
		TreeNode<Integer> rlll = new TreeNode<>(null, null, 561270);
		TreeNode<Integer> rll = new TreeNode<>(rlll, null, 72356);
		TreeNode<Integer> rlr = new TreeNode<>(null, null, 34123123);
		TreeNode<Integer> rl = new TreeNode<>(rll, rlr, 2241998);
		
		TreeNode<Integer> rrl = new TreeNode<>(null, null, 3069);
		TreeNode<Integer> rr = new TreeNode<>(rrl, null, 34);
		
		TreeNode<Integer> r = new TreeNode<>(rl, rr, 59078);
		
		TreeNode<Integer> root = new TreeNode<>(l, r, 42298);
		
		return root;
	}
	
	@SuppressWarnings("unused")
	private static TreeNode<String> createTreeFromFile() {
		
		final String my_tree_file = "my_tree.txt"; 
		
		TreeNode<String> root = TreeNode.parseTreeFromFile(my_tree_file);
		return root;
	}
}
