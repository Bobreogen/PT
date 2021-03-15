package gui;

import entities.*;
import logic.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class WindowMain extends JFrame {

    private JMenuBar menuBar;
    private JToolBar toolBar;
    private JPanel settingsPanel;
    private TransportSettingPanel truckPanel;
    private TransportSettingPanel carPanel;
    private JPanel simulationPanel;
    private JPanel buttonPanel;
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
        initConfig();
        initComponents();
        (new Timer(1000/30, evt -> update())).start();
    }

    public void start() {
        setVisible(true);
    }

    private void showInfo() {
        new WindowSimulationInformation(this, StatisticsCollector.instance().getStatistics());
    }

    private void initConfig() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                e.getComponent().requestFocus();
            }
        });
        this.addKeyListener(new WindowKeyListener());

        systemTime = System.currentTimeMillis();

        setSize(1024,768);
        setResizable(false);
        this.setLayout(new BorderLayout(0, 10));

        setFocusable(true);
        requestFocusInWindow();

        StatisticsCollector.instance().reset();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initComponents() {
        initMenu();
        initToolsBar();
        initSettingsPanel();
        initSimulationPanel();
        pack();
    }

    private void initMenu() {
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu simulationMenu = new JMenu("Симуляция");
        menuBar.add(simulationMenu);

        JMenuItem startSimulation = new JMenuItem("Начать симуляцию");
        startSimulation.addActionListener(e -> startSimulation());
        simulationMenu.add(startSimulation);

        JMenuItem stopSimulation = new JMenuItem("Остановить симуляцию");
        stopSimulation.addActionListener(e -> stopSimulation());
        simulationMenu.add(stopSimulation);
    }

    private void initToolsBar() {
        toolBar = new JToolBar();
        add(toolBar, BorderLayout.SOUTH);

        JButton startSimulationButton = new JButton("Старт симуляции");
        toolBar.add(startSimulationButton);
        startSimulationButton.addActionListener(e -> startSimulation());

        JButton stopSimulationButton = new JButton("Стоп симуляции");
        toolBar.add(stopSimulationButton);
        stopSimulationButton.addActionListener(e -> stopSimulation());
    }

    private void initSettingsPanel() {
        settingsPanel = new JPanel(new GridLayout(1,2));
        settingsPanel.setFocusable(true);
        add(settingsPanel, BorderLayout.NORTH);

        initButtonPanel();
        initTransportPanel();
    }

    private void initButtonPanel() {
        buttonPanel = new JPanel(new GridLayout(3,2, 10, 10));
        settingsPanel.add(buttonPanel);

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
        showSimulationTimeToggle.setToolTipText("Показывать время симуляции (подсказка)");
        showSimulationTimeToggle.setFocusable(false);
        buttonPanel.add(showSimulationTimeToggle);

        hideSimulationTimeToggle = new JToggleButton("Скрывать время симуляции", true);
        hideSimulationTimeToggle.setFocusable(false);
        hideSimulationTimeToggle.setToolTipText("Скрывать время симуляции (подсказка)");
        buttonPanel.add(hideSimulationTimeToggle);

        showSimulationTimeToggle.addItemListener(evt -> setShowSimulationTime(evt.getStateChange() == ItemEvent.SELECTED));
        hideSimulationTimeToggle.addItemListener(evt -> setShowSimulationTime(evt.getStateChange() != ItemEvent.SELECTED));

        JToggleButton showInformationToggleButton = new JToggleButton("Показывать информацию", false);
        showInformationToggleButton.setFocusable(false);
        buttonPanel.add(showInformationToggleButton);
        showInformationToggleButton.addItemListener(evt -> showInfo = evt.getStateChange() == ItemEvent.SELECTED);

        JButton showActiveVehicleButton = new JButton("Текущие объекты");
        showActiveVehicleButton.setFocusable(false);
        buttonPanel.add(showActiveVehicleButton);
        showActiveVehicleButton.addActionListener(evt -> showActiveVehicle());

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
    }

    private void initTransportPanel() {
        JPanel transportPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 20, 0));
        settingsPanel.add(transportPanel);

        truckPanel = new TransportSettingPanel("Грузовой транспорт");
        transportPanel.add(truckPanel);
        carPanel = new TransportSettingPanel("Легковой транспорт");
        transportPanel.add(carPanel);
    }

    private void initSimulationPanel() {
        simulationPanel = new JPanel() {
            @Override
            public void paint(Graphics g)
            {
                super.paint(g);
                RenderManager.instance().render(g);
            }
        };

        simulationPanel.setPreferredSize(new Dimension(500, 300));
        simulationPanel.setBackground(Color.LIGHT_GRAY);
        add(simulationPanel, BorderLayout.CENTER);

        simulationTimeLabel = new JLabel("");
        simulationPanel.add(simulationTimeLabel);
    }

    void update() {
        long curTime = System.currentTimeMillis();
        long dt = curTime - systemTime;
        systemTime = curTime;
        Habitat.instance().onFrame(dt);

        simulationTimeLabel.setVisible(showSimulationTime);
        simulationTimeLabel.setText(new SimpleDateFormat("mm:ss").format(new Date(Habitat.instance().getSimulationTime())));
        simulationPanel.repaint();
    }

    void startSimulation() {
        Habitat.instance().setVehicleProperty(new VehicleProperty(VehicleType.CAR, carPanel.getTimeGeneration(), carPanel.getProbability(), carPanel.getLifeTime()));
        Habitat.instance().setVehicleProperty(new VehicleProperty(VehicleType.TRUCK, truckPanel.getTimeGeneration(), truckPanel.getProbability(), truckPanel.getLifeTime()));

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

    void showActiveVehicle()
    {
        StringBuilder text = new StringBuilder();
        var vehicleCollection = Habitat.instance().getVehicleCreatedTimeMap();
        for (Map.Entry<Long, Vehicle> entry : vehicleCollection.entrySet()) {
            Long aLong = entry.getKey();
            Vehicle vehicle = entry.getValue();
            text.append((float) aLong/1000).append("  ").append(vehicle.getName()).append(" ").append(vehicle.getId()).append("\n");
        }
        JOptionPane.showMessageDialog(this, text, "Текущие объекты", JOptionPane.INFORMATION_MESSAGE, null);
    }
}