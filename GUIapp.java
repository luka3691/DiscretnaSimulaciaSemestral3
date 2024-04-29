

import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;
import agents.AgentAutomat;
import simulation.Config;
import simulation.Id;
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
        pocetReplikField.setText("25000");
        pauzaButton.setEnabled(false);
        stopButton.setEnabled(false);
        pomalyBeh = false;
        isStopped = new AtomicBoolean(false);
        isPaused = new AtomicBoolean(false);

        štartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Config.pocetReplikacii = Integer.parseInt(pocetReplikField.getText());
                    Config.pocetPokladni = Integer.parseInt(pocetPokladField.getText());
                    Config.pocetObsluznychMiest = Integer.parseInt(pocetObsluzField.getText());
                    int pocetOnlineObsluznych =  Config.pocetObsluznychMiest / 3;
                    int poceNormalnychObsluznych = Config.pocetObsluznychMiest - pocetOnlineObsluznych;
                    Config.pocetOnlineObsluznych = pocetOnlineObsluznych;
                    Config.pocetNormalObsluznych = poceNormalnychObsluznych;
                    initSimulation();

                    simulacia.simulate(Config.pocetReplikacii, Config.trvanieReplikacie);
                    //startSimulation();
                    štartButton.setEnabled(false);
                    pauzaButton.setEnabled(true);
                    stopButton.setEnabled(true);
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

            }
        });
        zrýchlenýBehRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double speedMax = 20 * .1;
                double speedValue = 10 * .1;
                double intervalValue = rychlostSlider.getValue();

                if (simulacia != null)
                {
                    if (!zrýchlenýBehRadioButton.isSelected())
                    {
                        spomalBeh();
                        simulacia.setSimSpeed(intervalValue * .01, (speedMax - speedValue + .001) * .05);
                    }
                    else
                    {
                        zrychliBeh();
                        simulacia.setMaxSimSpeed();
                    }
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
    public void initSimulation()
    {
        simulacia = new MySimulation();
        simulacia.registerDelegate(this);


        simulacia.onReplicationWillStart((sim)
                ->{
            cisloReplikacieLabel.setText(String.valueOf(sim.currentReplication()));
            startSimulation();
            if (!zrýchlenýBehRadioButton.isSelected()) {
                simulacia.setSimSpeed(simSpeed_interval(), simSpeed_duration());
            }
        });

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
    private double simSpeed_duration()
    {
        return 0.02;
    }
    private double simSpeed_interval()
    {
        return rychlostSlider.getValue()*0.01;
    }

    private void zrychliBeh() {
        pomalyBeh = false;
        switchPanel("zrychlenyBehCard");
    }
    private void spomalBeh() {
        pomalyBeh = true;
        switchPanel("normalBehCard");
    }

    private void startSimulation() {
        isPaused.set(false);
        isStopped.set(false);
        replikacieCounter =1000;
        pokladneModel.setRowCount(0);
        odberModel.setRowCount(0);
        zakazniciModel.setRowCount(0);
        //int pocetReplikacii = Integer.parseInt(pocetReplikField.getText());
        //pocetPokladni = Integer.parseInt(pocetPokladField.getText());
        //pocetObsluz = Integer.parseInt(pocetObsluzField.getText());
        //Predajna predajna = new Predajna(pocetReplikacii,pocetObsluz, pocetPokladni);
        //Thread simulatcia1 = new Thread(predajna::simuluj);
        //predajna.registerDelegate(this);
        for (int i = 0; i < Config.pocetPokladni; i++) {
            pokladneModel.addRow(new Object[]{i, simulacia.agentPokladne().getPokladne()[i], 0 ,0.0});
        }
        for (int i = 0; i < Config.pocetOnlineObsluznych; i++) {
            odberModel.addRow(new Object[]{i, "ONLINE", simulacia.agentObsluzneMiesta().getOnlineObsluzne()[i], 0.0});
        }
        for (int i = 0; i < Config.pocetNormalObsluznych; i++) {
            odberModel.addRow(new Object[]{i+ Config.pocetOnlineObsluznych, "NORMALNE", simulacia.agentObsluzneMiesta().getNormalneObsluzne()[i], 0.0});
        }
//odstarovanie simulacie pre kazdu strategiu
        /*
        if (porovananieCheckbox.isSelected()) {
            GUIPorovnanie porovanie = new GUIPorovnanie(pocetObsluz, pocetReplikacii);
            porovanie.startSimulation();
        }
        simulatcia1.start();
*/
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUIapp::new);
    }

    @Override
    public void refresh(Simulation simJadro) {
        MySimulation sim =simulacia;
        final double simulacnyCas = sim.currentTime();
        if (!pomalyBeh) {
            if(!sim.getStavyOsob().isEmpty()) {
                zakazniciModel.addRow(new Object[]{sim.getStavyOsob().get(0), sim.getStavyOsob().get(1), sim.getStavyOsob().get(2)});
                sim.getStavyOsob().clear();
            }
            casLabel.setText(formatTime(simulacnyCas + 9 * 60* 60));

            pocetLudiPredAutomatom.setText(String.valueOf(sim.agentAutomat().getFrontZakaznikov().size()));
            for (int i = 0; i < Config.pocetPokladni; i++) {
                pokladneModel.setValueAt(sim.agentPokladne().getPokladne()[i], i, 1);
                pokladneModel.setValueAt(sim.agentPokladne().getRady()[i].size(), i, 2);
                //pokladneModel.setValueAt(Math.round(sim.getPriemerVytazenostPokladni().get(i).getVytazenie(sim.getSimCas() - sim.getZaciatokCasu())*100) + "%", i ,3);
                //pridat vytazenie
            }
            for (int i = 0; i < sim.agentObsluzneMiesta().getOnlineObsluzne().length; i++) {
                odberModel.setValueAt(sim.agentObsluzneMiesta().getOnlineObsluzne()[i], i, 2);
                //odberModel.setValueAt(Math.round(sim.getPriemerVytazenostObsluznychOnline().get(i).getVytazenie(sim.getSimCas() - sim.getZaciatokCasu()) * 100) + "%", i, 3);
            }
            for (int i = 0; i < sim.agentObsluzneMiesta().getNormalneObsluzne().length; i++) {
                odberModel.setValueAt(sim.agentObsluzneMiesta().getNormalneObsluzne()[i], i+sim.agentObsluzneMiesta().getOnlineObsluzne().length, 2);
                //odberModel.setValueAt(Math.round(sim.getPriemerVytazenostObsluznychOstatne().get(i).getVytazenie(sim.getSimCas() - sim.getZaciatokCasu()) * 100) + "%", i+sim.getObsluzneMiesta().getOnlineObsluzne().length, 3);

            }

            pocetOnlineZakaznikovRad.setText(String.valueOf(sim.agentObsluzneMiesta().getOnlineQueue().size()));
            pocetOstatnychZakaznikovRad.setText(String.valueOf(sim.agentObsluzneMiesta().getOsobyQueue().size()));
            /*
obsadenyAutomatLabel.setText(String.valueOf(sim.getAutomatIsEmpty()));
            vytazenostAutomatuLabel.setText(Math.round(sim.getPriemerVytazenieAutomatu().getVytazenie(sim.getSimCas() - sim.getZaciatokCasu())*100.0) + "%");

             */
        } else {
            /*
            cisloReplikacieLabel.setText(String.valueOf(sim.getCisloReplikacie()));
            priemerZakaznikovLabel.setText(String.valueOf(Math.round(sim.getPriemerPocetLudiCelkovy().vypocitaj() * 1000.0) / 1000.0));
            double casVSyteme = Math.round(sim.getPriemerCasVObchodeCelkovy().vypocitaj() * 1000.0) / 1000.0;
            priemerCasVSystemeLabel.setText((int)casVSyteme%60 + ":" + (int)(casVSyteme*60%60));
            double casOdchodu = sim.getPriemerPoslednyOdchod().vypocitaj();
            priemerCasOdchodLabel.setText(String.valueOf((int)casOdchodu/60 + ":" + (int)casOdchodu%60 + ":" + (int)(casOdchodu*60%60)));
            intervalSpolahlivostiLabel.setText(String.valueOf((int)Math.floor(sim.getPriemerCasVObchodeCelkovy().getIntervalSpolahlivosti()[0])) + ":" + String.valueOf((int)(sim.getPriemerCasVObchodeCelkovy().getIntervalSpolahlivosti()[0]*60%60)) + ";" + String.valueOf((int)Math.floor(sim.getPriemerCasVObchodeCelkovy().getIntervalSpolahlivosti()[1])) + ":" + String.valueOf((int)(sim.getPriemerCasVObchodeCelkovy().getIntervalSpolahlivosti()[1]*60%60)));
            double casCakaniaVRade = sim.getPriemerCakanieVRadePredAutomatomCalkovy().vypocitaj();
            casCakaniaPredAutomatomLabel.setText((int)casCakaniaVRade + ":" + (int)(casCakaniaVRade*60%60));
            priemerDlzkaFrontuPredAutomatomLabel.setText(String.valueOf(Math.round(sim.getPriemerDlzkaRaduCelkovy().vypocitaj() * 1000.0) / 1000.0));
            vytazeneiAutomatuLabel.setText(Math.round(sim.getPriemerVytazenieAutomatuCelkove().vypocitaj()*100* 1000.0) / 1000.0 + "%");
            ArrayList<String> vytazenieObsluznych = new ArrayList<>();

            for (int i = 0; i < sim.getObsluzneMiesta().getOnlineObsluzne().length; i++) {
                vytazenieObsluznych.add(Math.round(sim.getPriemerVytazenostObsluznychOnlineCelkove().get(i).vypocitaj()*100) + "%");
            }

            for (int i = 0; i < sim.getObsluzneMiesta().getNormalneObsluzne().length; i++) {
                vytazenieObsluznych.add(Math.round(sim.getPriemerVytazenostObsluznychOstatneCelkove().get(i).vypocitaj()*100) + "%");
            }
            vytazenieObsluznychLabel.setText(vytazenieObsluznych.toString());
            ArrayList<String> vytazeniePokladni = new ArrayList<>();
            ArrayList<String> dlzkyRadovPriPokladniach = new ArrayList<>();
            for (int i = 0; i < pocetPokladni; i++) {
                vytazeniePokladni.add(Math.round(sim.getPriemerVytazenostPokladniCelkove().get(i).vypocitaj()*100) + "%");
                dlzkyRadovPriPokladniach.add(String.valueOf(Math.round(sim.getPriemerDlzkaRadovPriPokladniachCelkove().get(i).vypocitaj()*1000.0)/1000.0));
            }
            vytazeniePokladniLabel.setText(vytazeniePokladni.toString());
            dlzkyRadovPriPokladniachLabel.setText(dlzkyRadovPriPokladniach.toString());
            priemerObsluzenychLabel.setText(String.valueOf(Math.round(sim.getPocetObsluzenychZakaznikovCelkove().vypocitaj()*1000.0)/1000.0));
            dlzkyRadovPredObsluznymiLabel.setText("Normálne: " + Math.round(sim.getPriemerDlzkaRaduPredObsluzNormalCelkove().vypocitaj()*1000.0)/1000.0 + ", Online: " + Math.round(sim.getPriemerDlzkaRaduPredObsluzOnlineCelkove().vypocitaj()*1000.0)/1000.0);
        */
        }


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
