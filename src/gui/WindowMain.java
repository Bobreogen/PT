package gui;

import entities.*;
import logic.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WindowMain extends JFrame {

    private TransportSettingPanel truckPanel;
    private TransportSettingPanel carPanel;
    private JPanel simulationPanel;
    private JToggleButton showSimulationTimeToggle;
    private JToggleButton hideSimulationTimeToggle;
    private JLabel simulationTimeLabel;
    private boolean showInfo = false;
    private boolean showSimulationTime = false;
    private long systemTime = 0;

    private enum Instances {
        INSTANCE(new WindowMain());
        WindowMain window;
        Instances(WindowMain window) {
            this.window = window;
        }
    }

    public static WindowMain Instance() {
        return Instances.INSTANCE.window;
    }

    private WindowMain() {
        super();
        initComponents();
        setFocusable(true);
        requestFocusInWindow();
        this.addKeyListener(new WindowKeyListener());
        StatisticsCollector.instance().reset();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void start() {
        setVisible(true);
    }

    private void showInfo() {
        new WindowSimulationInformation(this, StatisticsCollector.instance().getStatistics());
    }

    private void initComponents() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                e.getComponent().requestFocus();
            }
        });

        systemTime = System.currentTimeMillis();

        setSize(1024,768);
        setResizable(false);
        this.setLayout(new BorderLayout(0, 10));
        JPanel settingsPanel = new JPanel(new GridLayout(1,2));
        settingsPanel.setFocusable(true);
        add(settingsPanel, BorderLayout.NORTH);

        simulationPanel = new JPanel();
        simulationPanel.setPreferredSize(new Dimension(500, 300));
        simulationPanel.setBackground(Color.LIGHT_GRAY);
        add(simulationPanel, BorderLayout.CENTER);

        (new Timer(1000/30, evt -> update())).start();

        simulationTimeLabel = new JLabel("");
        simulationPanel.add(simulationTimeLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(3,2, 10, 10));
        settingsPanel.add(buttonPanel);

        JPanel transportPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 20, 0));
        settingsPanel.add(transportPanel);

        JButton startSimulationButton = new JButton("Старт симуляции");
        buttonPanel.add(startSimulationButton);
        startSimulationButton.addActionListener(e -> startSimulation());
        startSimulationButton.setFocusable(false);

        JButton stopSimulationButton = new JButton("Стоп симуляции");
        buttonPanel.add(stopSimulationButton);
        stopSimulationButton.addActionListener(e -> stopSimulation());
        stopSimulationButton.setFocusable(false);
        stopSimulationButton.setEnabled(false);

        showSimulationTimeToggle = new JToggleButton("Показывать время симуляции", false);
        showSimulationTimeToggle.setFocusable(false);
        buttonPanel.add(showSimulationTimeToggle);

        hideSimulationTimeToggle = new JToggleButton("Скрывать время симуляции", true);
        hideSimulationTimeToggle.setFocusable(false);
        buttonPanel.add(hideSimulationTimeToggle);

        showSimulationTimeToggle.addItemListener(evt -> setShowSimulationTime(evt.getStateChange() == ItemEvent.SELECTED));
        hideSimulationTimeToggle.addItemListener(evt -> setShowSimulationTime(evt.getStateChange() != ItemEvent.SELECTED));

        JToggleButton showInformationToggleButton = new JToggleButton("Показывать информацию", false);
        showInformationToggleButton.setFocusable(false);
        buttonPanel.add(showInformationToggleButton);
        showInformationToggleButton.addItemListener(evt -> showInfo = evt.getStateChange() == ItemEvent.SELECTED);

        truckPanel = new TransportSettingPanel("Грузовой транспорт");
        transportPanel.add(truckPanel);
        carPanel = new TransportSettingPanel("Легковой транспорт");
        transportPanel.add(carPanel);

        Habitat.instance().addListener(e -> {
            if (e.getClass() == SimulationEvent.class) {
                switch (((SimulationEvent) e).getAction()) {
                    case SIMULATION_START -> {
                        startSimulationButton.setEnabled(false);
                        stopSimulationButton.setEnabled(true);
                        carPanel.setEnabled(false);
                        truckPanel.setEnabled(false);
                    }
                    case SIMULATION_STOP -> {
                        startSimulationButton.setEnabled(true);
                        stopSimulationButton.setEnabled(false);
                        carPanel.setEnabled(true);
                        truckPanel.setEnabled(true);
                    }
                }
            }
        });


        pack();
    }

    void update() {
        long curTime = System.currentTimeMillis();
        long dt = curTime - systemTime;
        systemTime = curTime;
        Habitat.instance().onFrame(dt);

        simulationTimeLabel.setVisible(showSimulationTime);
        simulationTimeLabel.setText(new SimpleDateFormat("mm:ss").format(new Date(Habitat.instance().getSimulationTime())));
    }

    void startSimulation() {
        Habitat.instance().setVehicleProperty(new VehicleProperty(VehicleType.CAR, carPanel.getTimeGeneration(), carPanel.getProbability()));
        Habitat.instance().setVehicleProperty(new VehicleProperty(VehicleType.TRUCK, truckPanel.getTimeGeneration(), truckPanel.getProbability()));

        Habitat.instance().setWorkspacePosition(simulationPanel.getSize().width, simulationPanel.getSize().height);

        Habitat.instance().startSimulation();
    }

    void stopSimulation() {
        if(showInfo) {
            Habitat.instance().pauseSimulation();
            showInfo();
        }
        else
            Habitat.instance().stopSimulation();
    }

    void setShowSimulationTime(boolean show) {
        if(showSimulationTime == show)
            return;

        showSimulationTime = show;
        hideSimulationTimeToggle.setSelected(!show);
        showSimulationTimeToggle.setSelected(show);
        Main.printLog(show ? "Show simulation time" : "Hide simulation time");
    }

    boolean getShowSimulationTime() {
        return showSimulationTime;
    }
}