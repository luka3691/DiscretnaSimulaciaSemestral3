package agents;

import OSPABA.*;
import OSPStat.Stat;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="2"
public class AgentOkolia extends Agent
{
	private int _pocetZakaznikov;
	private Stat priemerCasVObchode;

	public AgentOkolia(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		_pocetZakaznikov = 0;
		priemerCasVObchode = new Stat();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerOkolia(Id.managerOkolia, mySim(), this);
		new PlanovacPrichodov(Id.planovacPrichodov, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.novyZakaznik);
		addOwnMessage(Mc.odchodZakaznika);
	}
	//meta! tag="end"

	public int pocetZakaznikov()
	{ return _pocetZakaznikov; }

	public void incPocetZakaznikov()
	{ ++_pocetZakaznikov; }

	public Stat getPriemerCasVObchode() {
		return priemerCasVObchode;
	}
}