package mas.agents;


import jade.domain.DFService;
import jade.domain.FIPAException;

import env.EntityType;
import env.Environment;
import mas.behaviours.collector.CFSMBehaviour;
import tools.DFDServices;

public class AK_Collector extends AK_Agent{

	private static final long serialVersionUID = -957939931191274774L;
	private boolean picked = false;//if i picked something that ive to empty
	public String goal = "";
	

	
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
		System.out.println("the agent "+this.getLocalName()+ " is started TYPE : "+this.getMyTreasureType());

	}
	

	protected void takeDown(){
		try{
			DFService.deregister(this);
		}catch(FIPAException fe) {
			fe.printStackTrace();
		}
	}

}
