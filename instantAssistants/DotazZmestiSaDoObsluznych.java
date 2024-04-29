package instantAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="116"
public class DotazZmestiSaDoObsluznych extends Query
{
	public DotazZmestiSaDoObsluznych(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void execute(MessageForm message)
	{
	}

	@Override
	public AgentObsluzneMiesta myAgent()
	{
		return (AgentObsluzneMiesta)super.myAgent();
	}



}