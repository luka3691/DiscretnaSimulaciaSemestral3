package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="6"
public class ManagerPokladne extends Manager
{
	public ManagerPokladne(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null)
		{
			petriNet().clear();
		}
	}

	//meta! sender="AgentPredajna", id="58", type="Notice"
	public void processInit(MessageForm message)
	{
	}

	//meta! sender="AgentPredajna", id="24", type="Notice"
	public void processStartObednejPrestavky(MessageForm message)
	{
	}

	//meta! sender="Scheduler2", id="62", type="Finish"
	public void processFinishScheduler2(MessageForm message)
	{
	}

	//meta! sender="ProcesPlatenia", id="37", type="Finish"
	public void processFinishProcesPlatenia(MessageForm message)
	{
	}

	//meta! sender="AgentPredajna", id="49", type="Request"
	public void processPlatenieUPokoladne(MessageForm message)
	{
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.init:
			processInit(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.scheduler2:
				processFinishScheduler2(message);
			break;

			case Id.procesPlatenia:
				processFinishProcesPlatenia(message);
			break;
			}
		break;

		case Mc.platenieUPokoladne:
			processPlatenieUPokoladne(message);
		break;

		case Mc.startObednejPrestavky:
			processStartObednejPrestavky(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentPokladne myAgent()
	{
		return (AgentPokladne)super.myAgent();
	}

}
