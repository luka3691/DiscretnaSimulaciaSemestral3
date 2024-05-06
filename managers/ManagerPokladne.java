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
		if (Config.budePrestavka) {
			MyMessage msg = new MyMessage((MyMessage) message);
			msg.setAddressee((myAgent()).findAssistant(Id.planovacPrestavkaPokladne));
			msg.stack().add(mySim().findAgent(Id.agentModelu));
			msg.stack().add(mySim().findAgent(Id.agentPredajna));
			msg.stack().add(myAgent());
			startContinualAssistant(msg);
		}

	}

	//meta! sender="AgentPredajna", id="24", type="Notice"
	public void processStartObednejPrestavky(MessageForm message)
	{
		MyMessage msg = new MyMessage((MyMessage) message);
		msg.setAddressee(myAgent().findAssistant(Id.planovacPrestavkaPokladne));
		startContinualAssistant(msg);
	}

	//meta! sender="PlanovacPrestavkaPokladne", id="62", type="Finish"
	public void processFinishPlanovacPrestavkaPokladne(MessageForm message)
	{
		myAgent().setZablokovane(false);
		for (int i = 1; i < myAgent().getPokladne().length; i++) {
			myAgent().getPokladne()[i] = true;
		}
	}

	//meta! sender="ProcesPlatenia", id="37", type="Finish"
	public void processFinishProcesPlatenia(MessageForm message)
	{

		if (myAgent().isZablokovane()) {
			obedovaPrestvakaZapnuta(message);
		} else {
			ziadnaObedovaPrestvaka(message);
		}
	}

	//meta! sender="AgentPredajna", id="49", type="Request"
	public void processPlatenieUPokoladne(MessageForm message)
	{

		MyMessage sprava = new MyMessage((MyMessage) message);
		if (myAgent().isZablokovane()) {
			zaradPriZablokovani(sprava);
		} else {
			zaradNormalne(sprava);
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

	//meta! sender="PlanovacPrestavkaPokladne", id="163", type="Notice"
	public void processZablokujPokladne(MessageForm message)
	{
		myAgent().setZablokovane(true);
		for (int i = 1; i < myAgent().getPokladne().length; i++) {
			myAgent().getPokladne()[i] = false;
			myAgent().getRady()[0].addAll(myAgent().getRady()[i]);
			myAgent().getRady()[i].clear();
		}
		for (Osoba osoba : myAgent().getRady()[0]) {
		osoba.setIdPokladne(0);
		}

	}

	//meta! sender="AgentPredajna", id="172", type="Notice"
	public void processPrichodZObsluzneho(MessageForm message)
	{
		myAgent().setPrisielZObsluzneho(true);
		if (message.stack().isEmpty()) {
			message.stack().add(mySim().findAgent(Id.agentPredajna));
			message.stack().add(mySim().findAgent(Id.agentModelu));
		}
		if (!myAgent().getRady()[0].isEmpty()) {
			MyMessage spravaCopy = new MyMessage((MyMessage) message);
			Osoba osobaNova = myAgent().getRady()[0].poll();
			spravaCopy.setCisloPokladne(0);
			spravaCopy.setZakaznik(osobaNova);
			myAgent().getPokladne()[0] = false;
			spravaCopy.setCode(Mc.platenieUPokoladne);
			spravaCopy.setAddressee(myAgent().findAssistant(Id.procesPlatenia));
			startContinualAssistant(spravaCopy);
			//statistiky
			myAgent().getPriemerDlzkaRadovPriPokladniach().get(0).addSample(myAgent().getRady()[0].size());
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
			case Id.procesPlatenia:
				processFinishProcesPlatenia(message);
			break;

			case Id.planovacPrestavkaPokladne:
				processFinishPlanovacPrestavkaPokladne(message);
			break;
			}
		break;

		case Mc.platenieUPokoladne:
			processPlatenieUPokoladne(message);
		break;

		case Mc.startObednejPrestavky:
			processStartObednejPrestavky(message);
		break;

		case Mc.prichodZObsluzneho:
			processPrichodZObsluzneho(message);
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
	/*
	public ContinualAssistant proces()
	{ return ((AgentPokladne)myAgent()).f; }

	 */
	private void zaradPriZablokovani(MyMessage message)
	{

		MyMessage sprava = new MyMessage((MyMessage) message);
		int idRaduNaZaradenie = 0;
		sprava.getZakaznik().setIdPokladne(idRaduNaZaradenie);
		sprava.setCisloPokladne(idRaduNaZaradenie);
		if (myAgent().getPokladne()[0]) {
			myAgent().getPokladne()[idRaduNaZaradenie] = false;
			sprava.getZakaznik().setStav(StavyOsoby.JE_OBSLUHOVANY_V_POKLADNI);
			sprava.setAddressee(myAgent().findAssistant(Id.procesPlatenia));
			startContinualAssistant(sprava);
		} else {
			sprava.getZakaznik().setStav(StavyOsoby.V_RADE_PRED_POKLADNOU);
			myAgent().getRady()[idRaduNaZaradenie].add(sprava.getZakaznik());
			myAgent().getPriemerDlzkaRadovPriPokladniach().get(idRaduNaZaradenie).addSample(myAgent().getRady()[idRaduNaZaradenie].size());
		}
	}

	private void zaradNormalne(MyMessage message)
	{
		MyMessage sprava = new MyMessage((MyMessage) message);
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
	}
	private void ziadnaObedovaPrestvaka(MessageForm message)
	{
		MyMessage sprava = new MyMessage((MyMessage) message);
		AgentPokladne pokladne = (AgentPokladne)myAgent();
		int idPokladne = sprava.getZakaznik().getIdPokladne();
		//ak si nechal tovar na vydajni musi si pre neho ist
		//ak nie je prazdny rad pred danou pokladnou tak naplanuj zaciatok platenia
		if (!pokladne.getRady()[idPokladne].isEmpty()) {
			MyMessage spravaCopy = new MyMessage((MyMessage) message);
			Osoba osobaNova = pokladne.getRady()[idPokladne].poll();
			spravaCopy.setZakaznik(osobaNova);
			pokladne.getPokladne()[idPokladne] = false;
			spravaCopy.setCode(Mc.platenieUPokoladne);
			spravaCopy.setAddressee(myAgent().findAssistant(Id.procesPlatenia));
			startContinualAssistant(spravaCopy);
			//statistiky
			myAgent().getPriemerDlzkaRadovPriPokladniach().get(idPokladne).addSample(myAgent().getRady()[idPokladne].size());
		} else {
			pokladne.getPokladne()[idPokladne] = true;
		}
		if (sprava.getZakaznik().getIdPokladne() == 0 && myAgent().isPrisielZObsluzneho()) {
			myAgent().setPrisielZObsluzneho(false);
			MyMessage odoslanieZPokladne = new MyMessage((MyMessage) message);
			odoslanieZPokladne.setAddressee(mySim().findAgent(Id.agentPredajna));
			odoslanieZPokladne.setCode(Mc.navratZPokladne);
			notice(odoslanieZPokladne);
		}

		((MyMessage) message).getZakaznik().setStav(StavyOsoby.ODCHADZA);
		message.setCode(Mc.spatnePrevzatie);
		response(message);
		((MySimulation)mySim()).setStavyOsob(((MyMessage) message).getZakaznik().toArray());
	}
	private void obedovaPrestvakaZapnuta(MessageForm message)
	{
		int idPokladne = 0;
		AgentPokladne pokladne = (AgentPokladne)myAgent();
		if (myAgent().isPrisielZObsluzneho()) {
			pokladne.getPokladne()[idPokladne] = true;
		}
		//ak si nechal tovar na vydajni musi si pre neho ist
		//ak nie je prazdny rad pred danou pokladnou tak naplanuj zaciatok platenia
		if ((!pokladne.getRady()[idPokladne].isEmpty() && myAgent().isPrisielZObsluzneho())) {
			MyMessage spravaCopy = new MyMessage((MyMessage) message);
			Osoba osobaNova = pokladne.getRady()[idPokladne].poll();
			spravaCopy.setZakaznik(osobaNova);
			pokladne.getPokladne()[idPokladne] = false;
			spravaCopy.setCode(Mc.platenieUPokoladne);
			spravaCopy.setAddressee(myAgent().findAssistant(Id.procesPlatenia));
			startContinualAssistant(spravaCopy);
			//statistiky
			myAgent().getPriemerDlzkaRadovPriPokladniach().get(idPokladne).addSample(myAgent().getRady()[idPokladne].size());
		}
		MyMessage sprava = new MyMessage((MyMessage) message);
		sprava.getZakaznik().setStav(StavyOsoby.ODCHADZA);
		sprava.setCode(Mc.spatnePrevzatie);
		response(sprava);

		((MySimulation)mySim()).setStavyOsob(((MyMessage) sprava).getZakaznik().toArray());
	}
}