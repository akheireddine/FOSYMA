package tools;

import java.util.HashSet;
import java.util.List;

import env.Attribute;

public class GraphAKCollector  {

	private static final long serialVersionUID = -2476993587900455682L;
	private final String type;
	
	public GraphAKCollector(String type) {
		super();
		this.type=type;
	}
	
	public boolean addVertex(String name,List<Attribute> obs){
		if(!nodes.containsKey(name)){
			nodes.put(name, obs);
			dictAdjacences.put(name, new HashSet<String>());
		}
		if(this.containsTreasur(name,this.type))
			this.treasures.add(name);
		return super.addVertex(name);
	}
	
	
	public void updateNode(String node,List<Attribute> obs) {
		if(this.containsTreasur(node,this.type))
			this.treasures.add(node);
		this.nodes.replace(node, obs);
	}

}
