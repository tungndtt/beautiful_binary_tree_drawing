package processing;

import java.io.FileWriter;

public class DrawDirectoryTree extends Draw{
	
	private static class NodeInfo {
		private NodeInfo left, right;
		
		private String value;
		
		private String path;
		
		NodeInfo(NodeInfo left, NodeInfo right, String value, String path) {
			this.left = left;
			this.right = right;
			this.value = value;
			this.path = path;
		}
		
		NodeInfo getLeft() {
			return this.left;
		}
		
		NodeInfo getRight() {
			return this.right;
		}
		
		String getValue() {
			return this.value;
		}
		
		String getPath() {
			return this.path;
		}
	}
	
	private int stageDistance;
	
	private int widthIndent;
	
	public DrawDirectoryTree(int stageDistance, int widthIndent) {
		this.stageDistance = stageDistance >= 0 ? stageDistance : 0;
		this.widthIndent = widthIndent > 0 ? widthIndent : 1;
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
		
		String spaceIndent = DrawDirectoryTree.repeat(' ', this.widthIndent);
		
		if(nodeInfo.getPath() != null && nodeInfo.getPath().length() > 0) {
			String component = "";
			
			for(int i=0; i<path.length()-1; i++) {
				component += path.charAt(i) == '0' ? "│" + spaceIndent : " " + spaceIndent;
			}
			component += "│" + spaceIndent + "\n";
			
			for(int i=0; i<this.stageDistance; i++) {
				line.append(component);
			}
		}
		
		for(int i=0; i<path.length()-1; i++) {
			line.append(path.charAt(i) == '0' ? "│" + spaceIndent : " " + spaceIndent);
		}
		
		if(path != null && path.length() > 0) {
			String slack = DrawDirectoryTree.repeat('─', this.widthIndent);
			line.append(path.charAt(path.length()-1) == '0' ? "├" + slack : "└" + slack);
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
	
	private static String repeat(char character, int number) {
		String result = "";
		while(number-- > 0) {
			result += character;
		}
		return result;
	}
}
