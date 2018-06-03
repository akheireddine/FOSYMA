package mas.agents;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jgrapht.alg.util.Pair;

import env.Attribute;
import jade.lang.acl.ACLMessage;
import mas.abstractAgent;
import scala.Tuple5;
import tools.GraphAK;

public abstract class AK_Agent extends abstractAgent {

	private static final long serialVersionUID = -383346975870383010L;
	protected GraphAK G = new GraphAK();
	private ACLMessage toread = null; /** If there's something to read **/
	private boolean exploDone = false;
	private int cpt_exploration = 0;
	
	private int nombreDeCollision = 0;
	
	private static int cpt = 0;
	public int id = ++cpt;
	public String myGoal = "";
	public List<String> pathToGoal = new ArrayList<String>();

	private String last_move="";
	
	
	public void setLastMove(String m){
		this.last_move = m;
	}

	public String getLastMove(){
		return this.last_move;
	}
	
	public int getID() {
		return this.id;
	}
	public GraphAK getGraph(){
		return G;
	}
	
	public void CptPlus(){
		this.cpt_exploration ++;
	}
	public void RAZCpt(){
		this.cpt_exploration = 0;
	}
	public int getCpt(){
		return this.cpt_exploration;
	}
	
	
	public boolean isExplorationDone(){
		return this.exploDone;
	}
	
	public void exploration_is_done(){
		this.exploDone  = true;
	}

	
	public void setToread(ACLMessage msg){
		this.toread = msg;
	}
	
	public ACLMessage getMessage(){
		return toread;
	}
	
	public int getNombreDeCollision() {
		return nombreDeCollision;
	}

	public void setNombreDeCollision(int nombreDeCollision) {
		this.nombreDeCollision = nombreDeCollision;
	}



	public Object getObjectToSend(){
		Tuple5<HashMap<String,List<Attribute>>, HashMap<String,Set<String>>,Set<String>,Set<String>,HashMap<String, List<Pair<Attribute, Long>>>> obj = 
				new Tuple5<HashMap<String,List<Attribute>>, HashMap<String,Set<String>>,Set<String>,Set<String>,HashMap<String, List<Pair<Attribute, Long>>>>(G.getHashNode(),G.getDictAdjacences(),G.getOuverts(),G.getFermes(),G.getTreasures());
		return obj;
	}
	


}
