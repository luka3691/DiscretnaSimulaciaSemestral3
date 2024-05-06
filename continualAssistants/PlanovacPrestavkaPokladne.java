package continualAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="61"
public class PlanovacPrestavkaPokladne extends Scheduler
{
	public PlanovacPrestavkaPokladne(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentPokladne", id="62", type="Start"
	public void processStart(MessageForm message)
	{
		MyMessage msg = new MyMessage((MyMessage) message);
		msg.setCode(Mc.zablokujPokladne);
		hold(Config.zaciatokPrestavky, msg);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentPokladne", id="162", type="Notice"
	public void processZablokujPokladne(MessageForm message)
	{
		MyMessage odoslanie = new MyMessage((MyMessage) message);
		odoslanie.setCode(Mc.zablokujPokladne);
		odoslanie.setAddressee(this.myAgent());
		notice(odoslanie);
		MyMessage msg = new MyMessage((MyMessage) message);
		msg.setCode(Mc.ukonciZablokovanie);
		hold(Config.trvaniePrestavky, msg);
	}

	//meta! sender="AgentPokladne", id="165", type="Notice"
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

		case Mc.start:
			processStart(message);
		break;

		case Mc.zablokujPokladne:
			processZablokujPokladne(message);
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