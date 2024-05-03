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
		if (myAgent().getOsobyQueue().size() + myAgent().getOnlineQueue().size() <= 7) {
			message.setMsgResult(1);
		} else {
			message.setMsgResult(0);
		}
	}

	@Override
	public AgentObsluzneMiesta myAgent()
	{
		return (AgentObsluzneMiesta)super.myAgent();
	}


}