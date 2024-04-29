package continualAssistants;

import OSPABA.*;
import OSPRNG.ExponentialRNG;
import Osoby.Osoba;
import Osoby.StavyOsoby;
import simulation.*;
import agents.*;

//meta! id="27"
public class PlanovacPrichodov extends Scheduler
{
	private ExponentialRNG prichodLudiBezny = new ExponentialRNG((double)60*60/30);
	//private ExponentialRNG prichodLudiBezny = new ExponentialRNG((double)60*60/15);
	public PlanovacPrichodov(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentOkolia", id="28", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.novyZakaznik);
		hold(prichodLudiBezny.sample(), message);
	}

	//meta! sender="AgentOkolia", id="29", type="Notice"
	public void processNovyZakaznik(MessageForm message)
	{
		MyMessage msg = new MyMessage((MyMessage)message);
		hold(prichodLudiBezny.sample(), msg);

		((MyMessage)message).setZakaznik(new Osoba(mySim()));
		((MyMessage)message).getZakaznik().setStav(StavyOsoby.PRICHOD);
		assistantFinished(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:
			processStart(message);
		break;

		case Mc.novyZakaznik:
			processNovyZakaznik(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentOkolia myAgent()
	{
		return (AgentOkolia)super.myAgent();
	}

}