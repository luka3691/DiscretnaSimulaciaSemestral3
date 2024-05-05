package continualAssistants;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="30"
public class ProcesZadavaniaDoAutomatu extends Process
{
	private UniformContinuousRNG casZadavaniaDoAutomatu = new UniformContinuousRNG(30.0, 120.0);
	public ProcesZadavaniaDoAutomatu(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentAutomat", id="31", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.zadavanieDoAutomatuUkoncene);
		double zadavanieDoAutomatu = casZadavaniaDoAutomatu.sample();
		myAgent().getPriemerCakanieVRadePredAutomatom().addSample(mySim().currentTime() - ((MyMessage)message).getZakaznik().getCasPrichodu());
		myAgent().getPriemerVytazenieAutomatu().addSample(zadavanieDoAutomatu);
 		hold(zadavanieDoAutomatu, message); // naplanuje ukoncenie nakupu na cas simCas + casNakupu
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentAutomat", id="103", type="Notice"
	public void processZadavanieDoAutomatuUkoncene(MessageForm message)
	{
		assistantFinished(message);
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.zadavanieDoAutomatuUkoncene:
			processZadavanieDoAutomatuUkoncene(message);
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
	public AgentAutomat myAgent()
	{
		return (AgentAutomat)super.myAgent();
	}

}