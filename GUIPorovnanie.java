import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import simulation.Config;
import simulation.MySimulation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
public class GUIPorovnanie implements ISimDelegate {
    private int pocetObsluznych;
    private int pocetReplikacii;
    private ArrayList<Integer> replicationCounter;
    private final int minPokladni = 2;
    private final int maxPokladni = 6;
    private XYChart chart;

    private ArrayList<JLabel> valueLabels;
private ArrayList<List<Integer>> xValues;
private ArrayList<List<Double>> yValues;
    private XChartPanel<XYChart> chartPanel;
    public GUIPorovnanie(int pocetObsluznych, int pocetReplikacii) {
        this.pocetObsluznych = pocetObsluznych;
        this.pocetReplikacii = pocetReplikacii;
        valueLabels = new ArrayList<>();
        JFrame frame = new JFrame("Porovnanie");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        chart = new XYChartBuilder().width(800).height(600).title("Porovnanie závislosti počtu pokladní na dĺžku radu pred automatom.").xAxisTitle("Replikácia").yAxisTitle("Čakanie v rade").build();
        JPanel panel = new JPanel();
        chartPanel = new XChartPanel<>(chart);
        frame.add(chartPanel, BorderLayout.NORTH);
        JPanel hodontyPanel = new JPanel(new GridLayout(1, 5));
        for (int i = 0; i < 5; i++) {
            JLabel labelValue = new JLabel();
            hodontyPanel.add(labelValue);
            valueLabels.add(labelValue);
        }
        frame.add(hodontyPanel, BorderLayout.SOUTH);
        frame.setSize(800, 700);
        frame.setVisible(true);
        //chart.getStyler().setXAxisTickMarkSpacingHint(1000);
        chartPanel.revalidate();
        chartPanel.repaint();
        startSimulation();
    }

    public void startSimulation() {
        replicationCounter = new ArrayList<>();
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();

        for (int i = minPokladni; i <= maxPokladni; i++) {
            Config.pocetReplikacii = 20000;
            MySimulation predajna = new MySimulation(8,  i);
            predajna.registerDelegate(this);
            predajna.simulateAsync(Config.pocetReplikacii, Config.trvanieReplikacie);

            predajna.onSimulationWillStart(sim
                    ->{ ; });

            predajna.onReplicationWillStart(sim
                    ->{
                predajna.setMaxSimSpeed();
            });

            predajna.onReplicationDidFinish((simu)
                    ->{
                MySimulation sim = (MySimulation) simu;
                double aktualnyCas = sim.currentReplication();
                double celkovyCas = sim.replicationCount();

                    if ((int)(100*aktualnyCas/celkovyCas) != (int)(100*(aktualnyCas+1)/celkovyCas))  // prekresli graf 100 krat nezavisle na trvani simulacneho behu
                    {
                        int tempPocetPokladni = sim.getPocetPokladni();
                        int m = tempPocetPokladni - minPokladni;
                        if (replicationCounter.get(m) == 0) {
                            xValues.get(m).removeFirst();
                            yValues.get(m).removeFirst();
                        }
                        xValues.get(m).add(replicationCounter.get(m));
                        replicationCounter.set(m, replicationCounter.get(m)+(int)celkovyCas/100);
                        yValues.get(m).add(sim.getPriemerDlzkaRaduAutomatCelkove().mean());
                        chart.updateXYSeries(String.valueOf(tempPocetPokladni), xValues.get(m), yValues.get(m), null);
                        valueLabels.get(m).setText(tempPocetPokladni + " pokladne: " + String.valueOf(Math.round(sim.getPriemerDlzkaRaduAutomatCelkove().mean()*1000.0)/1000.0));
                        chartPanel.revalidate();
                        chartPanel.repaint();
                    }

            });
            replicationCounter.add(0);
            List<Integer> x = new ArrayList<>();
            x.add(0);
            xValues.add(x);
            List<Double> y = new ArrayList<>();
            y.add(0.0);
            yValues.add(y);
            chart.addSeries(String.valueOf(i), xValues.get(i - minPokladni), yValues.get(i - minPokladni));

        }
    }

    @Override
    public void simStateChanged(Simulation simulation, SimState simState) {

    }

    @Override
    public void refresh(Simulation simulation) {
        double aktualnyCas = simulation.currentReplication();
        double celkovyCas = simulation.replicationCount();
        SwingUtilities.invokeLater(() -> {
            if ((int)(200*aktualnyCas/celkovyCas) != (int)(200*(aktualnyCas+1)/celkovyCas))  // prekresli graf 200 krat nezavisle na trvani simulacneho behu
            {
                MySimulation sim = (MySimulation) simulation;
                int tempPocetPokladni = sim.getPocetPokladni();
                int i = tempPocetPokladni - minPokladni;
                if (replicationCounter.get(i) == 0) {
                    xValues.get(i).removeFirst();
                    yValues.get(i).removeFirst();
                }
                xValues.get(i).add(replicationCounter.get(i));
                replicationCounter.set(i, replicationCounter.get(i) +1000);
                yValues.get(i).add(sim.getPriemerDlzkaRaduAutomatCelkove().mean());
                chart.updateXYSeries(String.valueOf(tempPocetPokladni), xValues.get(i), yValues.get(i), null);
                valueLabels.get(i).setText(tempPocetPokladni + " pokladne: " + String.valueOf(Math.round(sim.getPriemerDlzkaRaduAutomatCelkove().mean()*1000.0)/1000.0));
                chartPanel.revalidate();
                chartPanel.repaint();
            }

        });
    }
}
