package continualAssistants;

import OSPABA.*;
import OSPRNG.*;
import Osoby.Osoba;
import Osoby.TypZakaznika;
import Rozdelenia.Triangularne;
import simulation.*;
import agents.*;
import OSPABA.Process;

import java.util.Arrays;

//meta! id="33"
public class ProcesObsluhy extends Process
{
	private UniformContinuousRNG casNaNadiktovanieObjednavky = new UniformContinuousRNG(60.0, 900.0);
	private TriangularRNG casNaOdovzadnieOnlineTovaru = new TriangularRNG(60.0,  120.0,480.0);
	private UniformContinuousRNG generovanieZlozitosti = new UniformContinuousRNG(0.0, 1.0);
	private EmpiricRNG casPripravaJednoduchej = new EmpiricRNG(
			new EmpiricPair(new UniformContinuousRNG(2.0*60, 5.0*60), 0.6),
			new EmpiricPair(new UniformContinuousRNG(5.0*60, 9.0*60), 0.4));
	private UniformContinuousRNG casPripravaMierneZlozitej = new UniformContinuousRNG(9.0*60, 11.0*60);
	private EmpiricRNG casPripravaZlozitej = new EmpiricRNG(
			new EmpiricPair(new UniformContinuousRNG(11.0*60, 12.0*60), 0.1),
		new EmpiricPair(new UniformContinuousRNG(12.0*60, 20.0*60), 0.6),
		new EmpiricPair(new UniformContinuousRNG(20.0*60, 25.0*60), 0.3));
	public ProcesObsluhy(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentObsluzneMiesta", id="34", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.obsluhaHotova);
		double trvanieVydavaniaTovaru;
		if (((MyMessage)message).getZakaznik().getTypZakaznika() == TypZakaznika.ONLINE) {
			int id  = ((MyMessage) message).getZakaznik().getIdObsluzneho();
			trvanieVydavaniaTovaru = casNaOdovzadnieOnlineTovaru.sample();
			myAgent().getPriemerVytazenostObsluznychOnline().get(id).addSample(trvanieVydavaniaTovaru);
			hold(trvanieVydavaniaTovaru, message);
		} else {
			int id  = ((MyMessage) message).getZakaznik().getIdObsluzneho();
			trvanieVydavaniaTovaru = getTravnieObjednavky() + casNaNadiktovanieObjednavky.sample();
			myAgent().getPriemerVytazenostObsluznychOstatne().get(id).addSample(trvanieVydavaniaTovaru);
			hold(trvanieVydavaniaTovaru, message);
		}
	}
	public double getTravnieObjednavky() {
		double typObjednavky = generovanieZlozitosti.sample();
		if (typObjednavky < 0.3) {
			return (double) casPripravaJednoduchej.sample();
		} else if (typObjednavky < 0.7) {
			return (double) casPripravaMierneZlozitej.sample();
		} else {
			return (double) casPripravaZlozitej.sample();
		}
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentObsluzneMiesta", id="109", type="Notice"
	public void processObsluhaHotova(MessageForm message)
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

		case Mc.obsluhaHotova:
			processObsluhaHotova(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentObsluzneMiesta myAgent()
	{
		return (AgentObsluzneMiesta)super.myAgent();
	}

}