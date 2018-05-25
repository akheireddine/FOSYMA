package mas.agents;


import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.util.leap.Serializable;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import env.Attribute;
import env.EntityType;
import env.Environment;
import mas.behaviours.collector.CFSMBehaviour;
import scala.Tuple4;
import tools.DFDServices;

public class AK_Collector extends AK_Agent{

	private static final long serialVersionUID = -957939931191274774L;
	private boolean picked = false;//if i picked something that ive to empty
	
	
	public Serializable getObjectToSend() {
		Tuple4<HashMap<String, List<Attribute>>, HashMap<String,Set<String>>,Set<String>,Set<String>> obj = 
				new Tuple4<HashMap<String, List<Attribute>>, HashMap<String,Set<String>>,Set<String>,Set<String>>(G.getHashNode(),G.getDictAdjacences(),G.getOuverts(),G.getFermes());
		return (Serializable) obj;
	}
	
	
	public void setPicked(boolean p) {
		this.picked=p;
	}
	public boolean iPicked() {
		return this.picked;
	}
	
	
	protected void setup(){

		super.setup();
		DFDServices.registerToService("collector", this);

		//get the parameters given into the object[]. In the current case, the environment where the agent will evolve
		final Object[] args = getArguments();
		if(args!=null && args[0]!=null && args[1]!=null){
			deployAgent((Environment) args[0],(EntityType)args[1]);
		}else{
			System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
			System.exit(-1);
		}

		//Add the behaviours
		addBehaviour(new CFSMBehaviour(this));
		System.out.println("the agent "+this.getLocalName()+ " is started");

	}
	

	protected void takeDown(){
		try{
			DFService.deregister(this);
		}catch(FIPAException fe) {
			fe.printStackTrace();
		}
	}

}
