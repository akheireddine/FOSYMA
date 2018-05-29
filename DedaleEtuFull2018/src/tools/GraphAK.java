package tools;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import env.Attribute;

public class GraphAK extends SimpleGraph<String,DefaultEdge> {

	private static final long serialVersionUID = 4776957678167008687L;

	private HashMap<String, List<Attribute>> nodes = new HashMap<String, List<Attribute>>();
	private HashMap<String,Set<String>> dictAdjacences = new HashMap<String,Set<String>>();
	private HashMap<String,Pair<Attribute,Long>>  treasures = new HashMap<String,Pair<Attribute,Long>>();
	private Set<String> ouverts;
	private Set<String> fermes;
	private String Silo_position="";
	private List<String> ens_position_silo= new ArrayList<String>();

	private Set<String> fermesAgent= new HashSet<String>();

	public GraphAK() {
		super(DefaultEdge.class);
		this.fermes = new HashSet<String>();
		this.ouverts = new HashSet<String>();
	}
	
	
	public void openVertices(Set<String> f) {
		for(String n : f) {
			this.fermes.remove(n);
			this.ouverts.add(n);
		}
	}
	
	
	public int getDegreeOfNode(String node_name){
		return this.degreeOf(node_name);
	}
	
	public HashMap<String, List<Attribute>> getHashNode(){
		return this.nodes;
	}
	
	public Set<String> getOuverts() {
		return ouverts;
	}

	public Set<String> getFermes() {
		return fermes;
	}
	
	public void clearFermes(){
		this.fermes.clear();
	}
	
	public void addFermesAgent(Set<String> f){
		this.fermesAgent = f;
	}
	
	public void clearMesFermesSeulement(){
		Set<String> f_tmp = this.fermes;
		for(String i : f_tmp){
			if(!this.fermesAgent.contains(i)){
				this.fermes.remove(i);
				this.ouverts.add(i);
			}
		}
		this.fermesAgent.clear();
	}
	
	public void updateOF(Set<String> o, Set<String> f){
		this.fermes.addAll(f);
		for(String n : o){
			if(!this.fermes.contains(n))
				this.ouverts.add(n);
		}
		this.ouverts.removeAll(this.fermes);
	}
	
	
	
	

	public List<Attribute> getAttrOfNode(String myPosition) {
		if( nodes.containsKey(myPosition))
			return nodes.get(myPosition);
		return null;
	}

	public HashMap<String, Set<String>> getDictAdjacences() {
		return this.dictAdjacences;
	}
	
	
	
	public int getNbOpenNeighborVertex(String node_name){
		int i = 0;
		if(super.containsVertex(node_name) ) {
			for(DefaultEdge e: super.edgesOf(node_name)){
				String src = this.getEdgeSource(e);
				String trg = this.getEdgeTarget(e);
				if (src.equals(node_name)){
					if(this.ouverts.contains(trg))
						i +=1;
				}
				else if (trg.equals(node_name)){
					if(this.ouverts.contains(src)){
						i +=1;
					}
				}
			}
		}
		return i;
	}
	
	
	
	public boolean addVertex(String name,List<Attribute> obs){
		if(!nodes.containsKey(name)){
			nodes.put(name, obs);
			dictAdjacences.put(name, new HashSet<String>());
		}
		else{
			this.updateNode(name, obs);
		}
		Attribute a = this.containsTreasur(name, "");
		if(a!=null){
			Pair<Attribute,Long> v = new Pair<Attribute,Long>(a,System.currentTimeMillis());
			this.treasures.put(name,v);
		}
		return super.addVertex(name);
	}
	
	public void updateNode(String node,List<Attribute> obs) {
		this.nodes.replace(node, obs);
		Attribute a = this.containsTreasur(node,"");
		if(a!=null){
			Pair<Attribute,Long> v = new Pair<Attribute,Long>(a,System.currentTimeMillis());
			this.treasures.put(node,v);
		}
	}

	public void addAllOuverts(String myPosition) {
		for(String node:this.nodes.keySet()){
			if(!node.equals(myPosition))
				this.ouverts.add(node);
		}
	}	
	
	public DefaultEdge addEdge(String src,String dst){
		Set<String> l_adj = null;
		if(dictAdjacences.containsKey(src)){
			l_adj = this.dictAdjacences.get(src);
			l_adj.add(dst);
			this.dictAdjacences.replace(src, l_adj);
		}else{
			l_adj = new HashSet<String>();
			l_adj.add(dst);
			this.dictAdjacences.replace(src,l_adj);
		}
		
		if(dictAdjacences.containsKey(dst)){
			l_adj = this.dictAdjacences.get(dst);
			l_adj.add(src);
			this.dictAdjacences.replace(dst, l_adj);
		}else{
			l_adj = new HashSet<String>();
			l_adj.add(src);
			this.dictAdjacences.put(dst,l_adj);
		}
		return super.addEdge(src, dst);
	}
	

	public void addToFermes(Set<String> closeSet) {
		this.fermesAgent = closeSet;
		this.fermes = new HashSet<String>();
		for(String i : closeSet) {
			this.ouverts.remove(i);
			this.fermes.add(i);
		}
//		if (this.ouverts.isEmpty()){
//			this.addAllOuverts(myPosition);
//		}
	}

	
	public void switchOF(Set<String> opened, Set<String> closed) {
		if(closed.contains(this.ouverts))
			System.out.println(" JAI TOUT EXPLORE");
//		else{
//			System.out.println(" ouverts : "+ouverts+"\n ses fermes : "+closed);
//		}
		this.ouverts = new HashSet<String>(opened);
		this.fermes = new HashSet<String>(closed);
		
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Set<String> isGolemAround(String src) {
		Set<String> detected = new HashSet<String>();
		for(String adj: this.dictAdjacences.get(src)) {
			if(isGolemIn(adj))
				detected.add(adj);
			}
		return detected;
	}

	
	public boolean isGolemIn(String node) {
		for(Attribute a : this.nodes.get(node)) {
			switch(a) {
			case STENCH:
				return true;
			default:
				break;
			}
		}
		return false;
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Attribute containsTreasur(String position,String type) {
		List<Attribute> obs = this.nodes.get(position);
		for(Attribute a : obs) {
			switch(a) {
			case TREASURE: case DIAMONDS:
				if(type.equals(""))
					return a;
				else if (a.getName().equals(type)) 
					return a;
				break;
			default:
				break;					
			}
		}
		return null;
	}
	
	
	
	public void MAJAttributeNode(String n , List<Attribute> obs){
//		if(this.containsTreasur(n, "") != null){
//			for(Attribute a : obs) {
//				switch(a) {
//				case TREASURE: case DIAMONDS:
//					a.getValue()
//					break;
//				default:
//					break;					
//				}
//			}
//		}
	}
	
	
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean isSiloPositionKnown() {
		return !this.ens_position_silo.isEmpty();
	}
	
	public String changer_de_noeud_silo(int i ) {
		return this.ens_position_silo.get(i);
	}
	
	
	public List<String> siloPositions(){
		return this.ens_position_silo;
	}
	
	public void addPossiblePositionSilo(String p) {
		this.ens_position_silo.add(p);
	}
	
	public void addAllPositionSilo(List<String> p) {
		for(String a: p) {
			this.addPossiblePositionSilo(a);
		}
	}
	
	
	
	
	

	//	public void resetVertex(String removedVertexName) {
//		this.addVertex(removedVertexName, this.nodes.get(removedVertexName));
//		if(this.dictAdjacences.get(removedVertexName)==null)
//			System.out.println("_______________________________smthing is wrong here cuz "+this.dictAdjacences.get(removedVertexName));
//		for(String adj: this.dictAdjacences.get(removedVertexName)) {
//			if(!this.containsVertex(adj))
//				this.addVertex(adj,null);
//			this.addEdge(removedVertexName, adj);
//		}
//	}

//	public void resetVertices(Set<String> removedVerticesName) {
//		for(String node:removedVerticesName){
//			if (!this.nodes.containsKey(node))
//				this.addVertex(node, new ArrayList<Attribute>());
//			else
//				super.addVertex(node);
//		}
//		for(String node : removedVerticesName) {
//			for(String adj: this.dictAdjacences.get(node)){
//				this.addEdge(node, adj);
//			}
//		}
//
//	}
		
	
	


	


	
}
