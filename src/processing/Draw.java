package processing;

public abstract class Draw {
	public abstract <T> boolean drawToFile(String filePath, TreeNode<T> root);
}
