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
	private double casOdchoduPosledneho;

	private int _pocetObsluzenych;


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
		_pocetObsluzenych = 0;
		casOdchoduPosledneho = 0;
		priemerCasVObchode = new Stat();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerOkolia(Id.managerOkolia, mySim(), this);
		new PlanovacPrichodov(Id.planovacPrichodov, mySim(), this);
		new ZatvaraniePredajneOkolie(Id.zatvaraniePredajneOkolie, mySim(), this);
		new PlanovacPrichodovOnline(Id.planovacPrichodovOnline, mySim(), this);
		new PlanovacPrichodovBizinis(Id.planovacPrichodovBizinis, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.novyZakaznik);
		addOwnMessage(Mc.odchodZakaznika);
	}
	//meta! tag="end"

	public int pocetObsluzenychZakaznikov()
	{ return _pocetObsluzenych; }

	public void incPocetObsluzenychZakaznikov()
	{ ++_pocetObsluzenych; }

	public double getCasOdchoduPosledneho() {
		return casOdchoduPosledneho;
	}

	public void setCasOdchoduPosledneho(double casOdchoduPosledneho) {
		this.casOdchoduPosledneho = casOdchoduPosledneho;
	}

	public int pocetZakaznikov()
	{ return _pocetZakaznikov; }

	public void incPocetZakaznikov()
	{ ++_pocetZakaznikov; }

	public Stat getPriemerCasVObchode() {
		return priemerCasVObchode;
	}


}