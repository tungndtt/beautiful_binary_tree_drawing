package processing;

import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class DrawBTree {
	
	private int minWidthDistance, minHeightDistance;
	
	private int[] distancesBetweenStages;
	
	private HashMap<Integer, LinkedList<Coordination>> nodesInStages;
	
	private HashMap<Integer, Coordination> nodes;
	
	private HashMap<Integer, Integer> ancestors;
	
	public int[] get() {
		return this.distancesBetweenStages;
	}
	
	public DrawBTree(int minWidthDistance, int minHeightDistance) {
		this.minWidthDistance = minWidthDistance > 0 ? minWidthDistance : 1;
		this.minHeightDistance = minHeightDistance > 0 ? minHeightDistance : 1;
		this.nodesInStages = new HashMap<>();
		this.nodes = new HashMap<>();
		this.ancestors = new HashMap<>();
	}
	
	private void clear() {
		this.nodesInStages.clear();
		this.nodes.clear();
		this.ancestors.clear();
		this.distancesBetweenStages = null;
	}
	
	public <T> Coordination clone(TreeNode<T> root, int id, int stage) {
		
		if(root == null) return null;
		
		Coordination left = root.getLeft() != null ? this.clone(root.getLeft(), id, stage+1) : null;
		
		Coordination right = root.getRight() != null ? this.clone(root.getRight(), left != null ? left.getId()+1 : id, stage+1) : null;
		
		Coordination coordination = new Coordination(
				right != null ? right.getId()+1 : left != null ? left.getId()+1 : id, 
				stage, 
				left, 
				right, 
				root.getValue().toString()
		);
		
		this.nodes.put(coordination.getId(), coordination);
		
		LinkedList<Coordination> nodes = null;
		if(this.nodesInStages.containsKey(stage)) nodes = this.nodesInStages.get(stage);
		else {
			nodes = new LinkedList<>();
			this.nodesInStages.put(stage, nodes);
		}
		nodes.add(coordination);
		
		return coordination;
	}
	
	private <T> Coordination initTree(TreeNode<T> root) {
		
		Coordination coordination = this.clone(root, 0, 0);
		
		if(coordination == null) return null;
		
		int treeHeight = this.nodesInStages.size();
		
		this.distancesBetweenStages = new int[treeHeight-1];
		Arrays.fill(this.distancesBetweenStages, this.minHeightDistance);
		
		this.setCoordinations(coordination, treeHeight-1);
		
		return coordination;
	}
	
	private void setCoordinations(Coordination coordination, int lastStage) {
		LinkedList<Coordination> queue = new LinkedList<>();
		queue.add(coordination);
		
		while(queue.size() > 0) {
			Coordination head = queue.removeFirst();
			if(head.getLeft() != null) {
				int diff = this.distancesBetweenStages[head.getStage()] + head.getLeft().getValue().length();
				head.getLeft().setCoordination(head.getCoordination() - diff);
				if(head.getLeft().getStage() < lastStage) {
					queue.add(head.getLeft());
				}
			}
			if(head.getRight() != null) {
				int diff = this.distancesBetweenStages[head.getStage()] + head.getValue().length();
				head.getRight().setCoordination(head.getCoordination() + diff);
				if(head.getRight().getStage() < lastStage) {
					queue.add(head.getRight());
				}
			}
		}
	}
	
	private <T> Coordination run(TreeNode<T> root) {
		Coordination coordination = this.initTree(root);
		this.leastCommonAncestor(coordination);
		
		
		int curStage = this.nodesInStages.size()-1;
		while(curStage > 0) {
			LinkedList<Coordination> coordinations = this.nodesInStages.get(curStage);
			
			
			
			int[] addition = new int[curStage];
			Iterator<Coordination> iterator = coordinations.iterator();
			Coordination cur = iterator.next();
			while(iterator.hasNext()) {
				Coordination coord = iterator.next();
				int diff = coord.getCoordination() - cur.getCoordination() - cur.getValue().length();
				if(diff < this.minWidthDistance) {
					int unit = (this.minWidthDistance - diff + 1)/2;
					int encode = this.encode(cur.getId(), coord.getId());
					int stage = this.nodes.get(this.ancestors.get(encode)).getStage();
					addition[stage] = Math.max(unit, addition[stage]);
				}
				cur = coord;
			}
			int cumSum = 0;
			for(int i=curStage-1; i>=0; i--) {
				cumSum += addition[i];
				this.distancesBetweenStages[i] += cumSum;
			}
			--curStage;
			this.setCoordinations(coordination, curStage);
		}
		
		this.setCoordinations(coordination, this.nodesInStages.size()-1);
		
		return coordination;
	}
	
	private <T> String draw(TreeNode<T> root) {
		Coordination coordination = this.run(root);
		
		LinkedList<Coordination> queue = new LinkedList<>();
		queue.add(coordination);
		
		int minCoordination = coordination.getCoordination();
		while(queue.size() > 0) {
			Coordination coord = queue.removeFirst();
			minCoordination = Math.min(minCoordination, coord.getCoordination());
			if(coord.getLeft() != null) queue.add(coord.getLeft());
			if(coord.getRight() != null) queue.add(coord.getRight());
		}
		
		int toAdd = -minCoordination;
		queue.add(coordination);
		while(queue.size() > 0) {
			Coordination coord = queue.removeFirst();
			coord.setCoordination(coord.getCoordination() + toAdd);
			if(coord.getLeft() != null) queue.add(coord.getLeft());
			if(coord.getRight() != null) queue.add(coord.getRight());
		}
		
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<this.distancesBetweenStages.length; i++) {
			LinkedList<Coordination> coordinations = this.nodesInStages.get(i);
			LinkedList<Integer> nextLine = new LinkedList<>();
			int coord = 0;
			while(coordinations.size() > 0) {
				if(coordinations.getFirst().getCoordination() > coord) {
					sb.append(" ");
					++coord;
				}
				else {
					Coordination removed = coordinations.removeFirst();
					sb.append(removed.getValue());
					if(removed.getLeft() != null) nextLine.add(coord-1);
					if(removed.getRight() != null) nextLine.add(- coord - removed.getValue().length());
					coord += removed.getValue().length();
				}
				
			}
			sb.append("\n");
			
			int distance = this.distancesBetweenStages[i];
			while(distance-- > 0) {
				LinkedList<Integer> _nextLine = new LinkedList<>();
				int edgeIdx = Math.abs(nextLine.getFirst());
				coord = 0;
				while(nextLine.size() > 0) {
					if(coord < edgeIdx) sb.append(" ");
					else {
						int removed = nextLine.removeFirst();
						sb.append(removed > 0 ? "/" : "\\");
						_nextLine.add(removed-1);
						edgeIdx = nextLine.size() > 0 ? Math.abs(nextLine.getFirst()) : -1;
					}
					++coord;
				}
				nextLine = _nextLine;
				sb.append("\n");
			}
		}
		
		LinkedList<Coordination> coordinations = this.nodesInStages.get(this.distancesBetweenStages.length);
		int coord = 0;
		while(coordinations.size() > 0) {
			if(coordinations.getFirst().getCoordination() > coord) {
				sb.append(" ");
				++coord;
			}
			else {
				Coordination removed = coordinations.removeFirst();
				sb.append(removed.getValue());
				coord += removed.getValue().length();
			}
		}
		
		return sb.toString();
	}
	
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
	
	private LinkedList<Coordination[]> leastCommonAncestor(Coordination node) {
		LinkedList<Coordination[]> result = new LinkedList<>();
		LinkedList<Coordination[]> 
				left = node.getLeft() != null ? this.leastCommonAncestor(node.getLeft()) : null,
				right = node.getRight() != null ? this.leastCommonAncestor(node.getRight()) : null;
		
		if(left != null && right != null) {
			Iterator<Coordination[]> it1 = left.iterator(), it2 = right.iterator();
			
			while(it1.hasNext() && it2.hasNext()) {
				Coordination[] co1 = it1.next(), co2 = it2.next();
				
				//System.out.println("Find between " + co1[1].getValue() + " and " + co2[0].getValue() + " ancestor " + node.getId());
				
				this.ancestors.put(this.encode(co1[1].getId(), co2[0].getId()), node.getId());
				
				co1[1] = co2[1];
				result.add(co1);
			}
			
			while(it1.hasNext()) {
				result.add(it1.next());
			}
			while(it2.hasNext()) {
				result.add(it2.next());
			}
		}
		else if(left != null) {
			result.addAll(left);
		}
		else if(right != null) {
			result.addAll(right);
		}
		
		result.addFirst(new Coordination[] {node, node});
		
		
		return result;
	}
	
	private int encode(int left, int right) {
		return left*this.nodes.size() + right;
	}
}
