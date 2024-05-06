

import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;
import simulation.Config;
import simulation.MySimulation;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


public class GUIapp implements ISimDelegate {
    private AtomicBoolean isStopped;
    private AtomicBoolean isPaused;
    private JPanel panel1;
    private JTextField pocetObsluzField;
    private JTextField pocetPokladField;
    private JTextField pocetReplikField;
    private JRadioButton normálnyBehRadioButton;
    private JRadioButton zrýchlenýBehRadioButton;
    private JButton štartButton;
    private JButton pauzaButton;
    private JButton stopButton;
    private JLabel pocetObsluzLabel;
    private JPanel normalnyBeh;
    private JPanel zrychlenyBeh;
    private JTable tableZakaznici;
    private JTable tableOdber;
    private DefaultTableModel odberModel = new DefaultTableModel(new String[]{"ID", "Typ pokladne", "Je volna?", "Vytazenie"}, 0);
    private DefaultTableModel pokladneModel = new DefaultTableModel(new String[]{"ID", "Je volna?", "V rade pred" , "Vytazenie"}, 0);
    private DefaultTableModel zakazniciModel = new DefaultTableModel(new String[]{"ID", "Typ zákazníka", "Stav"}, 0);
    private JTable tablePokladne;
    private JLabel stavyZakazLabel;
    private JLabel stavyOdberLabel;
    private JLabel stavyPokladLabel;
    private JLabel pocetPokladLabel;
    private JLabel PocetReplikLabel;
    private JSlider rychlostSlider;
    private JLabel pocetLudiPredAutomatom;
    private JLabel pocetOnlineZakaznikovRad;
    private JLabel pocetOstatnychZakaznikovRad;
    private JLabel casLabel;
    private JPanel prepinaciPanel;
    private JLabel obsadenyAutomatLabel;
    private JCheckBox porovananieCheckbox;
    private JLabel cisloReplikacieLabel;
    private JLabel priemerZakaznikovLabel;
    private JLabel priemerCasVSystemeLabel;
    private JLabel priemerCasOdchodLabel;
    private JLabel intervalSpolahlivostiLabel;
    private JLabel casCakaniaPredAutomatomLabel;
    private JLabel priemerDlzkaFrontuPredAutomatomLabel;
    private JLabel vytazeneiAutomatuLabel;
    private JLabel vytazenieObsluznychLabel;
    private JLabel vytazeniePokladniLabel;
    private JLabel dlzkyRadovPriPokladniachLabel;
    private JLabel dlzkyRadovPredObsluznymiLabel;
    private JLabel priemerObsluzenychLabel;
    private JLabel vytazenostAutomatuLabel;
    private JCheckBox prestavkaCheckbox;
    private JLabel priemerZakaznikovIntervalLabel;
    private JLabel priemerCasOdchodIntervalLabel;
    private JLabel casCakaniaPredAutomatomIntervalLabel;
    private JLabel vytazeneiAutomatuIntervalLabel;
    private JLabel vytazeniePokladniIntervalLabel;
    private JLabel priemerDlzkaFrontuPredAutomatomIntervalLabel;
    private JLabel vytazenieObsluznychIntervalLabel;
    private JLabel dlzkyRadovPriPokladniachIntervalLabel;
    private JLabel priemerObsluzenychIntervalLabel;
    private JCheckBox zmenenieTokuCheckbox;
    private JCheckBox zvysenieTokuCheckbox;

    int pocetPokladni;
    int pocetObsluz ;
    int replikacieCounter;

    private boolean pomalyBeh;
    private MySimulation simulacia;

    public GUIapp() {
        JFrame frame = new JFrame("Simulácia");
        tableOdber.setModel(odberModel);
        tablePokladne.setModel(pokladneModel);
        tableZakaznici.setModel(zakazniciModel);
        pocetObsluzField.setText("13");
        pocetPokladField.setText("4");
        pocetReplikField.setText("20000");
        pauzaButton.setEnabled(false);
        stopButton.setEnabled(false);
        pomalyBeh = true;
        isStopped = new AtomicBoolean(false);
        isPaused = new AtomicBoolean(false);

        štartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Config.pocetReplikacii = Integer.parseInt(pocetReplikField.getText());
                    int pocetPokladni = Integer.parseInt(pocetPokladField.getText());
                    int pocetObsluznychMiest = Integer.parseInt(pocetObsluzField.getText());
                    initSimulation(pocetPokladni, pocetObsluznychMiest);
                    simulacia.simulateAsync(Config.pocetReplikacii, Config.trvanieReplikacie);
                    if (prestavkaCheckbox.isSelected()) {
                        Config.budePrestavka = true;
                    } else {
                        Config.budePrestavka = false;
                    }
                    if (zmenenieTokuCheckbox.isSelected()) {
                        Config.zmenenyTok = true;
                    } else {
                        Config.zmenenyTok = false;
                    }
                    if (zvysenieTokuCheckbox.isSelected()) {
                        Config.zvysenyTok = 1.3;
                    } else {
                        Config.zvysenyTok = 1.0;
                    }
                    if (porovananieCheckbox.isSelected()) {
                        GUIPorovnanie porovanie = new GUIPorovnanie(pocetObsluznychMiest, Config.pocetReplikacii);
                        porovanie.startSimulation();
                    }

                    //startSimulation();
                    double speedMax = 20 * .1;
                    double speedValue = 10 * .1;
                    double intervalValue = rychlostSlider.getValue();
                    simulacia.setSimSpeed(intervalValue * .01, (speedMax - speedValue + .001) * .05);
                    štartButton.setEnabled(false);
                    pauzaButton.setEnabled(true);
                    stopButton.setEnabled(true);
                    porovananieCheckbox.setEnabled(false);
                    prestavkaCheckbox.setEnabled(false);
                    zmenenieTokuCheckbox.setEnabled(false);
                    zvysenieTokuCheckbox.setEnabled(false);

                }
                catch (NumberFormatException i) {
                    //Nebolo zadane cislo
                }
            }
        });
        pauzaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    if (isPaused.get()) {
                        isPaused.set(false);
                        simulacia.resumeSimulation();
                    } else {
                        isPaused.set(true);
                        simulacia.pauseSimulation();
                    }
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulacia.stopSimulation();
                porovananieCheckbox.setEnabled(true);
                štartButton.setEnabled(true);
                pauzaButton.setEnabled(false);
                stopButton.setEnabled(false);
                isStopped.set(true);
                prestavkaCheckbox.setEnabled(true);
                zmenenieTokuCheckbox.setEnabled(true);
                zvysenieTokuCheckbox.setEnabled(true);
            }
        });

        zrýchlenýBehRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (simulacia != null)
                {
                    if (zrýchlenýBehRadioButton.isSelected()) {
                        zrychliBeh();
                    } else  {
                        spomalBeh();
                    }
                }
            }
        });
        rychlostSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (simulacia != null)
                {
                    spomalBeh();
                }
            }
        });

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel1,BorderLayout.PAGE_START);
        switchPanel("normalBehCard");


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);

        frame.setVisible(true);

    }
    public void initSimulation(int pocetPokladni, int pocetObsluz)
    {
        simulacia = new MySimulation(pocetObsluz, pocetPokladni);
        simulacia.registerDelegate(this);
        simulacia.onSimulationWillStart(sim
                ->{ ; });

        simulacia.onReplicationWillStart(sim
                ->{
            changeSimSpeed(simulacia);
            startSimulation();
        });

        simulacia.onReplicationDidFinish((simu)
                ->{
            MySimulation sim = (MySimulation) simu;
            cisloReplikacieLabel.setText(String.valueOf(sim.currentReplication()));
            //refresh(simulacia);
            cisloReplikacieLabel.setText(String.valueOf(sim.currentReplication()));
            priemerZakaznikovLabel.setText(String.valueOf(Math.round(sim.getPriemerPocetLudiCelkovy().mean() * 1000.0) / 1000.0));
            double casVSyteme = sim.getPriemerCasVObchodeCelkovy().mean();
            priemerCasVSystemeLabel.setText(formatTime(casVSyteme));
            double casOdchodu = sim.getPriemerPoslednyOdchod().mean() + 9 * 60 * 60;
            priemerCasOdchodLabel.setText(formatTime(casOdchodu));
            double casCakaniaVRade = sim.getPriemerCakanieVRadePredAutomatomCalkovy().mean();
            casCakaniaPredAutomatomLabel.setText(formatTime(casCakaniaVRade));
            priemerDlzkaFrontuPredAutomatomLabel.setText(String.valueOf(Math.round(sim.getPriemerDlzkaRaduAutomatCelkove().mean() * 1000.0) / 1000.0));
            vytazeneiAutomatuLabel.setText(Math.round(sim.getPriemerVytazenieAutomatuCelkove().mean()* 100.0)  + "%");
            ArrayList<String> vytazenieObsluznych = new ArrayList<>();

            for (int i = 0; i < sim.agentObsluzneMiesta().getOnlineObsluzne().length; i++) {
                vytazenieObsluznych.add(Math.round(sim.getPriemerVytazenostObsluznychOnlineCelkove().get(i).mean()*100) + "%");
            }

            for (int i = 0; i < sim.agentObsluzneMiesta().getNormalneObsluzne().length; i++) {
                vytazenieObsluznych.add(Math.round(sim.getPriemerVytazenostObsluznychOstatneCelkove().get(i).mean()*100) + "%");
            }
            vytazenieObsluznychLabel.setText(vytazenieObsluznych.toString());
            ArrayList<String> vytazeniePokladni = new ArrayList<>();
            ArrayList<String> dlzkyRadovPriPokladniach = new ArrayList<>();
            for (int i = 0; i < sim.agentPokladne().getPokladne().length; i++) {
                vytazeniePokladni.add(Math.round(sim.getPriemerVytazenostPokladniCelkove().get(i).mean()*100) + "%");
                dlzkyRadovPriPokladniach.add(String.valueOf(Math.round(sim.getPriemerDlzkaRadovPriPokladniachCelkove().get(i).mean()*1000.0)/1000.0));
            }
            vytazeniePokladniLabel.setText(vytazeniePokladni.toString());
            dlzkyRadovPriPokladniachLabel.setText(dlzkyRadovPriPokladniach.toString());
            priemerObsluzenychLabel.setText(String.valueOf(Math.round(sim.getPocetObsluzenychZakaznikovCelkove().mean()*1000.0)/1000.0));
            if (simu.currentReplication() > 10) {
                intervalSpolahlivostiLabel.setText(formatTime(sim.getPriemerCasVObchodeCelkovy().confidenceInterval_95()[0]) + "." + formatTime(sim.getPriemerCasVObchodeCelkovy().confidenceInterval_95()[1]));
                casCakaniaPredAutomatomIntervalLabel.setText(formatTime(sim.getPriemerCakanieVRadePredAutomatomCalkovy().confidenceInterval_95()[0]) + "." + formatTime(sim.getPriemerCakanieVRadePredAutomatomCalkovy().confidenceInterval_95()[1]));
                priemerCasOdchodIntervalLabel.setText(Math.round(sim.getPriemerPoslednyOdchod().confidenceInterval_95()[0]* 100.0)/ 100.0 + "." + Math.round(sim.getPriemerPoslednyOdchod().confidenceInterval_95()[1]* 100.0)/ 100.0 );
                priemerZakaznikovIntervalLabel.setText(Math.round(sim.getPriemerPocetLudiCelkovy().confidenceInterval_95()[0]* 100.0)/ 100.0  + "." + Math.round(sim.getPriemerPocetLudiCelkovy().confidenceInterval_95()[1]* 100.0)/ 100.0 );
                priemerObsluzenychIntervalLabel.setText(Math.round(sim.getPocetObsluzenychZakaznikovCelkove().confidenceInterval_95()[0]* 100.0)/ 100.0  + "." + Math.round(sim.getPocetObsluzenychZakaznikovCelkove().confidenceInterval_95()[1]* 100.0)/ 100.0 );
                vytazeneiAutomatuIntervalLabel.setText(Math.round(sim.getPriemerVytazenieAutomatuCelkove().confidenceInterval_95()[0]* 100.0 *100.0)/100.0  + "%," + Math.round(sim.getPriemerVytazenieAutomatuCelkove().confidenceInterval_95()[1] * 100.0 * 100.0) / 100.0  + "%");
            }
        });


    }
    public void changeSimSpeed(Simulation sim)
    {
        double speedMax = 20 * .1;
        double speedValue = 10 * .1;
        double intervalValue = rychlostSlider.getValue();



        if (sim != null)
        {
            if (! zrýchlenýBehRadioButton.isSelected())
            {
                sim.setSimSpeed(intervalValue * .01, (speedMax - speedValue + .001) * .05);
            }
            else
            {
                sim.setMaxSimSpeed();
            }
        }
    }
    public static String formatTime(double time)
    {
        if (time == 0) return "0";

        DecimalFormat df_mh = new DecimalFormat("00");
        DecimalFormat df_s = new DecimalFormat("00.00");

        double h = (int)time / 3600;
        double m = ((int)time / 60) % 60;
        double s = ((int)time) % 60 + time - (int)time;

        return ((int)h == 0 ? "" : df_mh.format(h) + ":")
                + ((int)h == 0 && (int)m == 0 ? "" : df_mh.format(m) + ":")
                + df_s.format(s);
    }

    private void zrychliBeh() {
        pomalyBeh = false;
        switchPanel("zrychlenyBehCard");
        simulacia.setMaxSimSpeed();

    }
    private void spomalBeh() {
        pomalyBeh = true;
        switchPanel("normalBehCard");
        double speedMax = 20 * .1;
        double speedValue = 10 * .1;
        double intervalValue = rychlostSlider.getValue();
        simulacia.setSimSpeed(intervalValue * .01, (speedMax - speedValue + .001) * .05);
    }

    private void startSimulation() {
        isPaused.set(false);
        isStopped.set(false);
        pokladneModel.setRowCount(0);
        odberModel.setRowCount(0);
        zakazniciModel.setRowCount(0);
        for (int i = 0; i < simulacia.getPocetPokladni(); i++) {
            pokladneModel.addRow(new Object[]{i, simulacia.agentPokladne().getPokladne()[i], 0 ,0.0});
        }
        for (int i = 0; i < simulacia.getPocetOnlineObsluznych(); i++) {
            odberModel.addRow(new Object[]{i, "ONLINE", simulacia.agentObsluzneMiesta().getOnlineObsluzne()[i], 0.0});
        }
        for (int i = 0; i < simulacia.getPocetNormalObsluznych(); i++) {
            odberModel.addRow(new Object[]{i+ simulacia.getPocetOnlineObsluznych(), "NORMALNE", simulacia.agentObsluzneMiesta().getNormalneObsluzne()[i], 0.0});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUIapp::new);
    }

    @Override
    public void refresh(Simulation simJadro) {
        SwingUtilities.invokeLater(() -> {
        if (!zrýchlenýBehRadioButton.isSelected() && simJadro.currentTime() > 0) {
            spomalBeh();
            MySimulation sim = simulacia;
            final double simulacnyCas = sim.currentTime();
            casLabel.setText(formatTime(simulacnyCas + 9 * 60 * 60));
            if (!sim.getStavyOsob().isEmpty()) {
                zakazniciModel.addRow(new Object[]{sim.getStavyOsob().get(0), sim.getStavyOsob().get(1), sim.getStavyOsob().get(2)});
                sim.getStavyOsob().clear();
            }

            pocetLudiPredAutomatom.setText(String.valueOf(sim.agentAutomat().getFrontZakaznikov().size()));
            for (int i = 0; i < sim.getPocetPokladni(); i++) {
                pokladneModel.setValueAt(sim.agentPokladne().getPokladne()[i], i, 1);
                pokladneModel.setValueAt(sim.agentPokladne().getRady()[i].size(), i, 2);
                pokladneModel.setValueAt(Math.round((sim.agentPokladne().getPriemerVytazenostPokladni().get(i).sum()/simulacnyCas)*100) + "%", i, 3);
                //pridat vytazenie
            }
            for (int i = 0; i < sim.agentObsluzneMiesta().getOnlineObsluzne().length; i++) {
                odberModel.setValueAt(sim.agentObsluzneMiesta().getOnlineObsluzne()[i], i, 2);
                odberModel.setValueAt(Math.round((sim.agentObsluzneMiesta().getPriemerVytazenostObsluznychOnline().get(i).sum()/simulacnyCas)*100) + "%", i, 3);

            }
            for (int i = 0; i < sim.agentObsluzneMiesta().getNormalneObsluzne().length; i++) {
                odberModel.setValueAt(sim.agentObsluzneMiesta().getNormalneObsluzne()[i], i + sim.agentObsluzneMiesta().getOnlineObsluzne().length, 2);
                odberModel.setValueAt(Math.round((sim.agentObsluzneMiesta().getPriemerVytazenostObsluznychOstatne().get(i).sum()/simulacnyCas)*100) + "%", i + sim.agentObsluzneMiesta().getOnlineObsluzne().length, 3);

            }

            pocetOnlineZakaznikovRad.setText(String.valueOf(sim.agentObsluzneMiesta().getOnlineQueue().size()));
            pocetOstatnychZakaznikovRad.setText(String.valueOf(sim.agentObsluzneMiesta().getOsobyQueue().size()));
            obsadenyAutomatLabel.setText(String.valueOf(!sim.agentAutomat().isAutomatIsEmpty()));

            vytazenostAutomatuLabel.setText(Math.round((sim.agentAutomat().getPriemerVytazenieAutomatu().sum()/simulacnyCas) *100) + "%");
        }
    });
    }
    private void switchPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) prepinaciPanel.getLayout();
        cardLayout.show(prepinaciPanel, panelName);
    }

    private void unpause() {
        isPaused.set(false);
    }

    @Override
    public void simStateChanged(Simulation simulation, SimState simState) {

    }

}
