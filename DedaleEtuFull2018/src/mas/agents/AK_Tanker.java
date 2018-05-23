package mas.agents;


import env.EntityType;
import env.Environment;
import mas.abstractAgent;
import mas.behaviours.tanker.CheckBoxTankerBehaviour;


public class AK_Tanker extends abstractAgent{


	private static final long serialVersionUID = -1593633423403750683L;


	protected void setup(){

		super.setup();

		//get the parameters given into the object[]. In the current case, the environment where the agent will evolve
		final Object[] args = getArguments();
		if(args!=null && args[0]!=null && args[1]!=null){
			deployAgent((Environment) args[0],(EntityType)args[1]);

		}else{
			System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
			System.exit(-1);
		}

		//Add the behaviours
		addBehaviour(new CheckBoxTankerBehaviour(this));

		System.out.println("the agent "+this.getLocalName()+ " is started");

	}

	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){

	}
}

