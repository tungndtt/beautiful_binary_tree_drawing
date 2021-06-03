package processing;

import java.io.FileWriter;

public class DrawTreeDirectory extends Draw{
	
	public static class NodeInfo {
		private NodeInfo left, right;
		
		private String value;
		
		private String path;
		
		public NodeInfo(NodeInfo left, NodeInfo right, String value, String path) {
			this.left = left;
			this.right = right;
			this.value = value;
			this.path = path;
		}
		
		public NodeInfo getLeft() {
			return this.left;
		}
		
		public NodeInfo getRight() {
			return this.right;
		}
		
		public String getValue() {
			return this.value;
		}
		
		public String getPath() {
			return this.path;
		}
	}
	
	private <T> NodeInfo clone(TreeNode<T> root, String path) {
		
		if(root == null) return null;
		
		NodeInfo left = root.getLeft() != null ? this.clone(root.getLeft(), path + '0') : new NodeInfo(null, null, "null", path + '0'),
				right = root.getRight() != null ? this.clone(root.getRight(), path + '1') : new NodeInfo(null, null, "null", path + '1');
		
		return new NodeInfo(left, right, root.getValue().toString(), path);
	}
	
	private String drawLine(NodeInfo nodeInfo) {
		
		String path = nodeInfo.getPath();
		
		StringBuilder line = new StringBuilder();
		
		for(int i=0; i<path.length()-1; i++) {
			line.append(path.charAt(i) == '0' ? "│  " : "   ");
		}
		
		if(path != null && path.length() > 0) {
			line.append(path.charAt(path.length()-1) == '0' ? "├──" : "└──");
		}
		
		line.append(nodeInfo.getValue() + "\n");
		
		return line.toString();
	}
	
	private void preorderRun(NodeInfo node, StringBuilder sb) {
		sb.append(this.drawLine(node));
		if(node.getLeft() != null) {
			this.preorderRun(node.getLeft(), sb);
		}
		if(node.getRight() != null) {
			this.preorderRun(node.getRight(), sb);
		}
	}
	
	private <T> String draw(TreeNode<T> root) {
		NodeInfo nodeInfo = this.clone(root, "");
		
		StringBuilder sb = new StringBuilder();
		this.preorderRun(nodeInfo, sb);
		
		return sb.toString();
	}
	
	@Override
	public <T> boolean drawToFile(String filePath, TreeNode<T> root) {
		FileWriter writer;
		try {
			String content = this.draw(root);
			writer = new FileWriter(filePath);
			writer.write(content);
			writer.close();
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
}
