package agents;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPStat.Stat;
import OSPStat.WStat;
import Objects.Statistika;
import Osoby.Osoba;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="9"
public class AgentAutomat extends Agent
{
	private SimQueue<Osoba> frontZakaznikov;
	private boolean automatIsEmpty;
	private boolean jeBlokovany;
	private Stat priemerVytazenieAutomatu;
	private Stat priemerCakanieVRadePredAutomatom;
	private WStat priemerDlzkaRaduAutomat;
	public AgentAutomat(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		priemerVytazenieAutomatu = new Stat();
		priemerCakanieVRadePredAutomatom = new Stat();
		priemerDlzkaRaduAutomat = new WStat(mySim());
		frontZakaznikov = new SimQueue<>();
		automatIsEmpty = true;
		jeBlokovany = false;
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerAutomat(Id.managerAutomat, mySim(), this);
		new ProcesZadavaniaDoAutomatu(Id.procesZadavaniaDoAutomatu, mySim(), this);
		new ZatvaraniePredajneAutomat(Id.zatvaraniePredajneAutomat, mySim(), this);
		addOwnMessage(Mc.vyhodZRadu);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.zadavanieDoAutomatu);
		addOwnMessage(Mc.zadavanieDoAutomatuUkoncene);
		addOwnMessage(Mc.uvolnenieAutomatu);
		addOwnMessage(Mc.zablokovanieAutomatu);
	}
	//meta! tag="end"

	public SimQueue<Osoba> getFrontZakaznikov() {
		return frontZakaznikov;
	}

	public boolean isAutomatIsEmpty() {
		return automatIsEmpty;
	}

	public void setAutomatIsEmpty(boolean automatIsEmpty) {
		this.automatIsEmpty = automatIsEmpty;
	}

	public Stat getPriemerVytazenieAutomatu() {
		return priemerVytazenieAutomatu;
	}

	public Stat getPriemerCakanieVRadePredAutomatom() {
		return priemerCakanieVRadePredAutomatom;
	}

	public WStat getPriemerDlzkaRaduAutomat() {
		return priemerDlzkaRaduAutomat;
	}

	public boolean isJeBlokovany() {
		return jeBlokovany;
	}

	public void setJeBlokovany(boolean jeBlokovany) {
		this.jeBlokovany = jeBlokovany;
	}
}