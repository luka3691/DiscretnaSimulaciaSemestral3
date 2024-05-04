package continualAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;

import java.io.ObjectInputFilter;

//meta! id="127"
public class ZatvaraniePredajneAutomat extends Scheduler
{
	public ZatvaraniePredajneAutomat(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentAutomat", id="128", type="Start"
	public void processStart(MessageForm message)
	{
		MyMessage sprava = new MyMessage((MyMessage) message);
		sprava.setCode(Mc.vyhodZRadu);
		hold(Config.casKoncaVydavaniaListkov, sprava); // naplanuje ukoncenie nakupu na cas simCas + casNakupu
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentAutomat", id="133", type="Notice"
	public void processVyhodZRadu(MessageForm message)
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

		case Mc.vyhodZRadu:
			processVyhodZRadu(message);
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