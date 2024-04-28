package instantAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="91"
public class DotazZaradNaObsluzne extends Query
{
	public DotazZaradNaObsluzne(int id, Simulation mySim, CommonAgent myAgent)
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