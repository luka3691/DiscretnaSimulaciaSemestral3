package continualAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="59"
public class PlanovacPrestavkaObsluzne extends Scheduler
{
	public PlanovacPrestavkaObsluzne(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentObsluzneMiesta", id="60", type="Start"
	public void processStart(MessageForm message)
	{
		MyMessage msg = new MyMessage((MyMessage) message);
		msg.setCode(Mc.zablokujObsluzne);
		hold(Config.zaciatokPrestavky, msg);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentObsluzneMiesta", id="175", type="Notice"
	public void processZablokujObsluzne(MessageForm message)
	{

		MyMessage odoslanie = new MyMessage((MyMessage) message);
		odoslanie.setCode(Mc.zablokujObsluzne);
		odoslanie.setAddressee(this.myAgent());
		notice(odoslanie);
		MyMessage msg = new MyMessage((MyMessage) message);
		msg.setCode(Mc.ukonciZablokovanie);
		hold(Config.trvaniePrestavky, msg);
	}

	//meta! sender="AgentObsluzneMiesta", id="176", type="Notice"
	public void processUkonciZablokovanie(MessageForm message)
	{
		assistantFinished(message);
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.ukonciZablokovanie:
			processUkonciZablokovanie(message);
		break;

		case Mc.zablokujObsluzne:
			processZablokujObsluzne(message);
		break;

		case Mc.start:
			processStart(message);
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