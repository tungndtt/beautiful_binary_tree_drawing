package processing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

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
	
	public static TreeNode<String> parseTreeFromFile(String filePath) {
		if(!filePath.endsWith(".txt")) {
			return null;
		}
		FileInputStream fstream;
		try {
			TreeNode<String> result = null;
			fstream = new FileInputStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			
			String strLine;
			while ((strLine = br.readLine()) != null) {
				String[] splited = strLine.split(",");
				
				if(splited.length != 2) {
					System.out.println("Wrong format for parsing!");
					br.close();
					fstream.close();
					return null;
				}
				splited[0] = splited[0].trim();
				splited[1] = splited[1].trim();
				
				if(splited[0].length() == 1 && splited[0].equals("t")) {
					if(result == null) {
						result = new TreeNode<String>(null, null, splited[1]);
					}
					else {
						result.setValue(splited[1]);
					}
				}
				else {
					if(result == null) {
						result = new TreeNode<String>(null, null, "Unknown"); 
					}
					TreeNode<String> it = result;
					for(int i=0; i<splited[0].length(); i++) {
						if(splited[0].charAt(i) == 'l') {
							if(it.left == null) {
								it.left = new TreeNode<String>(null, null, "Unknown");
							}
							it = it.left;
						}
						else if(splited[0].charAt(i) == 'r') {
							if(it.right == null) {
								it.right = new TreeNode<String>(null, null, "Unknown");
							}
							it = it.right;
						}
						else {
							System.out.println("Upps! The provided format is wrong for parsing!");
							br.close();
							fstream.close();
							return null;
						}
					}
					it.value = splited[1];
				}
			}
			br.close();
			fstream.close();
			
			return result;
		} catch (Exception e) {
			System.out.print(e);
			return null;
		}
	}
}
