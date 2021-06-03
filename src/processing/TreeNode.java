package processing;

public class TreeNode<T> {
	private TreeNode<T> left;
	
	private TreeNode<T> right;
	
	private T value;
	
	public TreeNode(TreeNode<T> left, TreeNode<T> right, T value) {
		this.left = left;
		this.right = right;
		this.value = value;
	}
	
	public TreeNode<T> getLeft() {
		return this.left;
	}
	
	public TreeNode<T> getRight() {
		return this.right;
	}
	
	public T getValue() {
		return this.value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
}
