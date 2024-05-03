package continualAssistants;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import Osoby.TypZakaznika;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="138"
public class SpatnePrevzatie extends Process
{
	private UniformContinuousRNG spatnePrevzatieVelkehoTovaru = new UniformContinuousRNG(30.0, 70.0);
	public SpatnePrevzatie(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentObsluzneMiesta", id="140", type="Notice"
	public void processSpatnePrevzatieHotove(MessageForm message)
	{
		MyMessage sprava = new MyMessage((MyMessage) message);
		if (sprava.getZakaznik().getTypZakaznika() == TypZakaznika.ONLINE) {
			myAgent().getOnlineObsluzne()[sprava.getZakaznik().getIdObsluzneho()] = true;
		} else {
			myAgent().getNormalneObsluzne()[sprava.getZakaznik().getIdObsluzneho()] = true;
		}
		assistantFinished(message);
	}

	//meta! sender="AgentObsluzneMiesta", id="139", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.spatnePrevzatieHotove);
		hold(spatnePrevzatieVelkehoTovaru.sample(), message); // naplanuje ukoncenie nakupu na cas simCas + casNakupu
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

		case Mc.spatnePrevzatieHotove:
			processSpatnePrevzatieHotove(message);
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