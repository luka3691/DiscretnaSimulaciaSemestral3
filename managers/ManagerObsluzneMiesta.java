package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="7"
public class ManagerObsluzneMiesta extends Manager
{
	public ManagerObsluzneMiesta(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentPredajna", id="72", type="Notice"
	public void processUvolneniePredajne(MessageForm message)
	{
	}

	//meta! sender="AgentPredajna", id="46", type="Request"
	public void processObsluhaZakaznika(MessageForm message)
	{
	}

	//meta! sender="AgentPredajna", id="69", type="Notice"
	public void processStartObednejPrestavky(MessageForm message)
	{
	}

	//meta! sender="ProcesObsluhy", id="34", type="Finish"
	public void processFinishProcesObsluhy(MessageForm message)
	{
	}

	//meta! sender="Scheduler1", id="60", type="Finish"
	public void processFinishScheduler1(MessageForm message)
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
		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.procesObsluhy:
				processFinishProcesObsluhy(message);
			break;

			case Id.scheduler1:
				processFinishScheduler1(message);
			break;
			}
		break;

		case Mc.obsluhaZakaznika:
			processObsluhaZakaznika(message);
		break;

		case Mc.uvolneniePredajne:
			processUvolneniePredajne(message);
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
	public AgentObsluzneMiesta myAgent()
	{
		return (AgentObsluzneMiesta)super.myAgent();
	}

}
