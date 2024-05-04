package managers;

import OSPABA.*;
import Osoby.Osoba;
import Osoby.StavyOsoby;
import Osoby.TypZakaznika;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

import java.util.Queue;

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

	//meta! sender="PlanovacPrestavkaPokladne", id="62", type="Finish"
	public void processFinishPlanovacPrestavkaPokladne(MessageForm message)
	{
	}

	//meta! sender="ProcesPlatenia", id="37", type="Finish"
	public void processFinishProcesPlatenia(MessageForm message)
	{
		MyMessage sprava = new MyMessage((MyMessage) message);
		AgentPokladne pokladne = (AgentPokladne)myAgent();
		int idPokladne = sprava.getCisloPokladne();
		pokladne.getPokladne()[idPokladne] = true;
		//ak si nechal tovar na vydajni musi si pre neho ist
		//ak nie je prazdny rad pred danou pokladnou tak naplanuj zaciatok platenia
		if (!pokladne.getRady()[idPokladne].isEmpty()) {
			MyMessage spravaCopy = new MyMessage((MyMessage) message);
			Osoba osobaNova = pokladne.getRady()[idPokladne].poll();
			spravaCopy.setZakaznik(osobaNova);
			pokladne.getPokladne()[idPokladne] = false;
			spravaCopy.setCode(Mc.platenieUPokoladne);
			//spravaCopy.setAddressee(proces());
			startContinualAssistant(spravaCopy);
			//statistiky
			myAgent().getPriemerDlzkaRadovPriPokladniach().get(idPokladne).addSample(myAgent().getRady()[idPokladne].size());
		}
		((MyMessage) message).getZakaznik().setStav(StavyOsoby.ODCHADZA);
		message.setCode(Mc.spatnePrevzatie);
		response(message);
		((MySimulation)mySim()).setStavyOsob(((MyMessage) message).getZakaznik().toArray());
	}

	//meta! sender="AgentPredajna", id="49", type="Request"
	public void processPlatenieUPokoladne(MessageForm message)
	{
		MyMessage sprava = new MyMessage((MyMessage) message);
		AgentPokladne pokladne = (AgentPokladne)myAgent();
		sprava.setAddressee(myAgent().findAssistant(Id.dotazNaZaradneieDoPokladne));
		execute(sprava);
		int idPokladneNaZaradenie = sprava.getCisloPokladne();
		if (idPokladneNaZaradenie != -1) {
			//nasla sa volna pokladna tak zarad osobu do tej pokladne
			myAgent().getPokladne()[idPokladneNaZaradenie] = false;
			sprava.getZakaznik().setStav(StavyOsoby.JE_OBSLUHOVANY_V_POKLADNI);
			sprava.setAddressee(myAgent().findAssistant(Id.procesPlatenia));
			startContinualAssistant(sprava);
		} else {
			//zarad osobu do najratsieho radu
			sprava.setAddressee(myAgent().findAssistant(Id.dotazNaZaradenieDoRaduPriPokladniach));
			execute(sprava);
			int idRaduNaZaradenie = sprava.getCisloPokladne();
			sprava.getZakaznik().setStav(StavyOsoby.V_RADE_PRED_POKLADNOU);
			myAgent().getRady()[idRaduNaZaradenie].add(sprava.getZakaznik());
			myAgent().getPriemerDlzkaRadovPriPokladniach().get(idRaduNaZaradenie).addSample(myAgent().getRady()[idRaduNaZaradenie].size());
		}
		((MySimulation)mySim()).setStavyOsob(((MyMessage) message).getZakaznik().toArray());

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
		case Mc.platenieUPokoladne:
			processPlatenieUPokoladne(message);
		break;

		case Mc.init:
			processInit(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.procesPlatenia:
				processFinishProcesPlatenia(message);
			break;

			case Id.planovacPrestavkaPokladne:
				processFinishPlanovacPrestavkaPokladne(message);
			break;
			}
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
	/*
	public ContinualAssistant proces()
	{ return ((AgentPokladne)myAgent()).f; }

	 */
}