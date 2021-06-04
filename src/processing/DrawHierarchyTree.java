package processing;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class DrawHierarchyTree extends Draw{
	
	private static class Coordination {
		private Coordination left, right;
		
		private String value;
		
		private int coordination;
		
		Coordination(Coordination left, Coordination right, String value, int coordination) {
			this.left = left;
			this.right = right;
			this.value = value;
			this.coordination = coordination;
		}
		
		Coordination getLeft() {
			return this.left;
		}
		
		Coordination getRight() {
			return this.right;
		}
		
		String getValue() {
			return this.value;
		}
		
		int getCoordination() {
			return this.coordination;
		}
		
		void setCoordination(int coordination) {
			this.coordination = coordination;
		}
	}
	
	private int minIndentDistance;
	
	private HashMap<Integer, LinkedList<Coordination>> nodesInStages;
	
	public DrawHierarchyTree(int minIndentDistance) {
		this.minIndentDistance = minIndentDistance;
		this.nodesInStages = new HashMap<>();
	}
	
	private void clear() {
		this.nodesInStages.clear();
	}
	
	private <T> Coordination clone(TreeNode<T> root, int coordination, int stage) {
		
		if(root == null) return null;
		
		Coordination left = root.getLeft() != null ? this.clone(root.getLeft(), coordination - root.getLeft().getValue().toString().length(), stage+1) : null,
					right = root.getRight() != null ? this.clone(root.getRight(), coordination + root.getValue().toString().length(), stage+1) : null;
		
		Coordination result = new Coordination(left, right, root.getValue().toString(), coordination);
		LinkedList<Coordination> nodes = null;
		
		if(this.nodesInStages.containsKey(stage)) {
			nodes = this.nodesInStages.get(stage);
		}
		else {
			nodes = new LinkedList<>();
			this.nodesInStages.put(stage, nodes);
		}
		
		nodes.add(result);
		
		return result;
	}
	
	private void shiftCoordinations(Coordination coordination, int toShift) {
		
		coordination.setCoordination(coordination.getCoordination() + toShift);
		
		if(coordination.getLeft() != null) {
			this.shiftCoordinations(coordination.getLeft(), toShift);
		}
		
		if(coordination.getRight() != null) {
			this.shiftCoordinations(coordination.getRight(), toShift);
		}
	}
	
	private LinkedList<Coordination[]> setCoordinations(Coordination coordination) {
		LinkedList<Coordination[]> result = new LinkedList<>();
		
		LinkedList<Coordination[]> 
			left = coordination.getLeft() != null ? this.setCoordinations(coordination.getLeft()) : null,
			right = coordination.getRight() != null ? this.setCoordinations(coordination.getRight()) : null;
		
		if(left != null && right != null) {
			Iterator<Coordination[]> it1 = left.iterator(), it2 = right.iterator();
			int toShift = 0;
			while(it1.hasNext() && it2.hasNext()) {
				Coordination[] c1 = it1.next(), c2 = it2.next();
				int distance = c1[1].getCoordination() + c1[1].getValue().length() - c2[0].getCoordination() + this.minIndentDistance;
				toShift = Math.max(toShift, distance);
				c1[1] = c2[1];
				result.add(c1);
			}
			
			while(it1.hasNext()) {
				result.add(it1.next());
			}
			
			while(it2.hasNext()) {
				result.add(it2.next());
			}
			
			this.shiftCoordinations(coordination.getRight(), toShift);
			
			int leftFoot = coordination.getLeft().getCoordination() + coordination.getLeft().getValue().length()/2;
			int rightFoot = coordination.getRight().getCoordination() + coordination.getRight().getValue().length()/2;
			coordination.setCoordination((leftFoot + rightFoot)/2 - coordination.getValue().length()/2);
		}
		else if(left != null) {
			result.addAll(left);
			int leftFoot = coordination.getLeft().getCoordination() + coordination.getLeft().getValue().length()/2;
			coordination.setCoordination(leftFoot + 2 - coordination.getValue().length()/2);
		}
		else if(right != null) {
			result.addAll(right);
			int rightFoot = coordination.getRight().getCoordination() + coordination.getRight().getValue().length()/2;
			coordination.setCoordination(rightFoot - 2 - coordination.getValue().length()/2);
		}
		
		result.addFirst(new Coordination[] {coordination, coordination});
		
		return result;
	}
	
	private <T> String draw(TreeNode<T> root) {
		
		Coordination coordination = this.clone(root, 0, 0);
		if(coordination == null) {
			return null;
		}
		
		this.setCoordinations(coordination);
		
		LinkedList<Coordination> queue = new LinkedList<>();
		queue.add(coordination);
		
		int minCoordination = coordination.getCoordination();
		
		while(queue.size() > 0) {
			Coordination head = queue.removeFirst();
			minCoordination = Math.min(minCoordination, head.getCoordination());
			
			if(head.getLeft() != null) {
				queue.add(head.getLeft());
			}
			
			if(head.getRight() != null) {
				queue.add(head.getRight());
			}
		}
		
		this.shiftCoordinations(coordination, -minCoordination);
		
		StringBuilder sb = new StringBuilder();
		
		LinkedList<int[]> edgeLine = new LinkedList<>();
		int curStage = 0;
		while(curStage < this.nodesInStages.size()) {
			LinkedList<Coordination> nodes = this.nodesInStages.get(curStage++);
			
			int coord = 0;
			while(nodes.size() > 0) {
				if(nodes.getFirst().getCoordination() > coord) {
					sb.append(" ");
					++coord;
				}
				else {
					Coordination removed = nodes.removeFirst();
					sb.append(removed.getValue());
					if(removed.getLeft() != null || removed.getRight() != null) {
						int[] edge = new int[] {-1, -1};
						if(removed.getLeft() != null) {
							edge[0] = removed.getLeft().getCoordination() + removed.getLeft().getValue().length()/2;
						}
						if(removed.getRight() != null) {
							edge[1] = removed.getRight().getCoordination() + removed.getRight().getValue().length()/2;
						}
						edgeLine.add(edge);
					}
					coord += removed.getValue().length();
				}
			}
			sb.append("\n");
			
			if(curStage == this.nodesInStages.size()) break;
			
			coord = 0;
			int[] head = edgeLine.getFirst();
			while(edgeLine.size() > 0) {
				if(head[0] != -1 && head[1] != -1) {
					if(coord < head[0]) {
						sb.append(" ");
						++coord;
					}
					else {
						sb.append("┌");
						int mid = (head[0] + head[1]) / 2;
						while(++coord < mid) {
							sb.append("─");
						}
						sb.append("┴");
						while(++coord < head[1]) {
							sb.append("─");
						}
						sb.append("┐");
						++coord;
						
						edgeLine.removeFirst();
						head = edgeLine.size() > 0 ? edgeLine.getFirst() : null;
					}
				}
				else if(head[0] != -1) {
					if(coord < head[0]) {
						sb.append(" ");
						++coord;
					}
					else {
						sb.append("┌─┘");
						
						coord += 3;
						edgeLine.removeFirst();
						head = edgeLine.size() > 0 ? edgeLine.getFirst() : null;
					}
				}
				else {
					if(coord < head[1] - 2) {
						sb.append(" ");
						++coord;
					}
					else {
						sb.append("└─┐");
						
						coord += 3;
						edgeLine.removeFirst();
						head = edgeLine.size() > 0 ? edgeLine.getFirst() : null;
					}
				}
			}
			
			sb.append("\n");
		}
		
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
			this.clear();
			return true;
		} catch (Exception e) {
			System.out.println(e);
			this.clear();
			return false;
		}
	}

}
