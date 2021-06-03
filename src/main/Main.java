package main;

import processing.Coordination;
import processing.DrawBTree;
import processing.TreeNode;

public class Main {
	public static void main(String[] args) {
		DrawBTree drawer = new DrawBTree(2, 3);
		TreeNode<Integer> lrl = new TreeNode<>(null, null, 123);
		TreeNode<Integer> lrrr = new TreeNode<>(null, null, 24242);
		TreeNode<Integer> lrr = new TreeNode<>(null, lrrr, 12345);
		TreeNode<Integer> lr = new TreeNode<>(lrl, lrr, 4567);
		
		TreeNode<Integer> ll = new TreeNode<>(null, null, 10789);
		TreeNode<Integer> l = new TreeNode<>(ll, lr, 3512);
		
		TreeNode<Integer> rlll = new TreeNode<>(null, null, 561270);
		TreeNode<Integer> rll = new TreeNode<>(rlll, null, 72356);
		TreeNode<Integer> rlr = new TreeNode<>(null, null, 34123123);
		TreeNode<Integer> rl = new TreeNode<>(rll, rlr, 2241998);
		
		TreeNode<Integer> rrl = new TreeNode<>(null, null, 3069);
		TreeNode<Integer> rr = new TreeNode<>(rrl, null, 34);
		
		TreeNode<Integer> r = new TreeNode<>(rl, rr, 59078);
		
		TreeNode<Integer> root = new TreeNode<>(l, r, 42298);
		
		/*
		Coordination coord = drawer.clone(root, 0, 0);

		System.out.println(coord.getId() + " " + coord.getValue().length());
		System.out.println(coord.getLeft().getId() + " " + coord.getLeft().getValue());
		System.out.println(coord.getLeft().getLeft().getLeft());
		System.out.println(coord.getLeft().getLeft().getId() + " " + coord.getLeft().getLeft().getValue());
		System.out.println(coord.getLeft().getRight().getId() + " " + coord.getLeft().getRight().getValue());
		*/
		
		/*
		for(Integer stage : drawer.get().keySet()) {
			System.out.println("stage " + stage);
			for(Coordination c : drawer.get().get(stage)) {
				System.out.print(c.getValue() + " ");
			}
			System.out.println();
		}
		*/

		boolean success = drawer.drawToFile("beautiful_binary_tree.txt", root);
		
		if(success) System.out.print("Open your file to see the masterpiece!");
		else System.out.print("Oh! Somethings went wrong!");
	}
}
