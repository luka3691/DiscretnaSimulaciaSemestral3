package continualAssistants;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import OSPRNG.UniformDiscreteRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="36"
public class ProcesPlatenia extends Process
{
	private UniformContinuousRNG generovanieTypuPlatby = new UniformContinuousRNG(0.0, 1.0);
	private UniformDiscreteRNG trvaniePlatbyHotovost = new UniformDiscreteRNG(180/60, 480/60);
	private UniformDiscreteRNG trvaniePlatbyKrata = new UniformDiscreteRNG(180/60, 360/60);
	public ProcesPlatenia(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentPokladne", id="37", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.platenieHotove);
		double typPlatby = generovanieTypuPlatby.sample();
		if (typPlatby < 0.4) {
			hold(trvaniePlatbyHotovost.sample(), message);
		} else {
			hold(trvaniePlatbyKrata.sample(), message);
		}
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentPokladne", id="111", type="Notice"
	public void processPlatenieHotove(MessageForm message)
	{
		assistantFinished(message);
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

		case Mc.platenieHotove:
			processPlatenieHotove(message);
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