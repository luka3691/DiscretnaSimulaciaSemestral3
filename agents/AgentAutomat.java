package agents;

import OSPABA.*;
import OSPStat.Stat;
import OSPStat.WStat;
import Objects.Statistika;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="9"
public class AgentAutomat extends Agent
{
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
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerAutomat(Id.managerAutomat, mySim(), this);
		new ProcesZadavaniaDoAutomatu(Id.procesZadavaniaDoAutomatu, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.zadavanieDoAutomatu);
		addOwnMessage(Mc.uvolnenieAutomatu);
	}
	//meta! tag="end"
}