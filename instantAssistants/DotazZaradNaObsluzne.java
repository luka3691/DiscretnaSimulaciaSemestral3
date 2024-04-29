package instantAssistants;

import OSPABA.*;
import Osoby.Osoba;
import Osoby.TypZakaznika;
import simulation.*;
import agents.*;

//meta! id="91"
public class DotazZaradNaObsluzne extends Query
{
	public DotazZaradNaObsluzne(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void execute(MessageForm message)
	{
		MyMessage sprava = (MyMessage) message;
		int cisloObsluzneho = getIDVolneObsluzne(sprava.getZakaznik());
		if (cisloObsluzneho != -1) {
			((MyMessage)message).getZakaznik().setIdObsluzneho(cisloObsluzneho);
			((MyMessage)message).setCisloObsluzneho(cisloObsluzneho);
		} else {
			((MyMessage)message).setCisloObsluzneho(cisloObsluzneho);
		}

	}

	@Override
	public AgentObsluzneMiesta myAgent()
	{
		return (AgentObsluzneMiesta)super.myAgent();
	}

	public int getIDVolneObsluzne(Osoba osoba) {
		if (osoba.getTypZakaznika() == TypZakaznika.ONLINE){
			return getVolneOnline();
		} else {
			return getVolneNormalne();
		}
	}

	private int getVolneNormalne() {
		for (int i = 0; i < myAgent().getNormalneObsluzne().length; i++) {
			if (myAgent().getNormalneObsluzne()[i]) {
				return i;
			}
		}
		return -1;
	}
	private int getVolneOnline() {
		for (int i = 0; i < myAgent().getOnlineObsluzne().length; i++) {
			if (myAgent().getOnlineObsluzne()[i]) {
				return i;
			}
		}
		return -1;
	}
}