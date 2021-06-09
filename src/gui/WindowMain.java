package gui;

import AI.AIManager;
import config.ConfigManager;
import entities.*;
import logic.*;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.stream.IntStream;

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
    private JToggleButton showInformationToggleButton;
    private JLabel simulationTimeLabel;
    private boolean showInfo = false;
    private boolean isInit = false;

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
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent event) {
                ConfigManager.SaveHabitatConfig();
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
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
        setVisible(true);

        StatisticsCollector.instance().reset();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initComponents() {
        initMenu();
        initToolsBar();
        initSettingsPanel();
        initSimulationPanel();

        Habitat.instance().setWorkspacePosition(simulationPanel.getSize().width, simulationPanel.getSize().height);
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

        JMenu consoleMenu = new JMenu("Консоль");
        menuBar.add(consoleMenu);

        consoleMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                new Console();
            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });

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
    }

    private void initButtonPanel() {
        buttonPanel = new JPanel(new GridLayout(7,2, 10, 10));
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

        showInformationToggleButton = new JToggleButton("Показывать информацию", false);
        showInformationToggleButton.setFocusable(false);
        buttonPanel.add(showInformationToggleButton);
        showInformationToggleButton.addItemListener(evt -> showInfo = evt.getStateChange() == ItemEvent.SELECTED);

        JButton showActiveVehicleButton = new JButton("Текущие объекты");
        showActiveVehicleButton.setFocusable(false);
        buttonPanel.add(showActiveVehicleButton);
        showActiveVehicleButton.addActionListener(evt -> showActiveVehicle());

        JToggleButton CarAI = new JToggleButton("Остановить ИИ легковых", false);
        buttonPanel.add(CarAI);
        CarAI.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                CarAI.setText("Возобновить ИИ легковых");
                AIManager.instance().StopAIForVehicleType(VehicleType.CAR);
            }
            else {
                CarAI.setText("Остановить ИИ легковых");
                AIManager.instance().StartAIForVehicleType(VehicleType.CAR);
            }
        });
        CarAI.setFocusable(false);

        JToggleButton TruckAI = new JToggleButton("Остановить ИИ грузовых", false);
        buttonPanel.add(TruckAI);
        TruckAI.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                TruckAI.setText("Возобновить ИИ грузовых");
                AIManager.instance().StopAIForVehicleType(VehicleType.TRUCK);
            }
            else {
                TruckAI.setText("Остановить ИИ грузовых");
                AIManager.instance().StartAIForVehicleType(VehicleType.TRUCK);
            }
        });
        TruckAI.setFocusable(false);

        String[] PriorityArray = IntStream.range(1, 10).mapToObj(String::valueOf).toArray(String[]::new);

        buttonPanel.add(new JLabel("Приоритет легкового транспорта"));
        buttonPanel.add(new JLabel("Приоритет грузового транспорта"));
        JComboBox<String> CarAIPriority = new JComboBox<>(PriorityArray);
        CarAIPriority.setSelectedItem(Integer.toString(AIManager.instance().GetAIPriorityForVehicleType(VehicleType.CAR)));
        CarAIPriority.addActionListener(e -> AIManager.instance().SetAIPriorityForVehicleType(VehicleType.CAR, Integer.parseInt((String)CarAIPriority.getSelectedItem())));
        CarAIPriority.setFocusable(false);
        buttonPanel.add(CarAIPriority);
        JComboBox<String> TruckAIPriority = new JComboBox<>(PriorityArray);
        TruckAIPriority.setSelectedItem(Integer.toString(AIManager.instance().GetAIPriorityForVehicleType(VehicleType.TRUCK)));
        TruckAIPriority.addActionListener(e -> AIManager.instance().SetAIPriorityForVehicleType(VehicleType.TRUCK, Integer.parseInt((String)TruckAIPriority.getSelectedItem())));
        TruckAIPriority.setFocusable(false);
        buttonPanel.add(TruckAIPriority);

        JMenuItem loadMenuItem = new JMenuItem("Загрузить");
        loadMenuItem.addActionListener(actionEvent -> {
            startSimulation();
            Habitat.instance().pauseSimulation();

            JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(null);
            File file = fc.getSelectedFile();
            ;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                for(var entity : (ArrayList<Vehicle>) ois.readObject()){
                    EntityManager.instance().addEntity(entity);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            Habitat.instance().startSimulation();
        });
        buttonPanel.add(loadMenuItem);

        JMenuItem saveMenuItem = new JMenuItem("Сохранить");
        saveMenuItem.addActionListener(actionEvent -> {
            Habitat.instance().pauseSimulation();

            JFileChooser fc = new JFileChooser();
            fc.showSaveDialog(null);
            File file = fc.getSelectedFile();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file, false));){
                oos.writeObject(Habitat.instance().getVehicleList());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Habitat.instance().startSimulation();
        });
        buttonPanel.add(saveMenuItem);


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

        truckPanel = new TransportSettingPanel("Грузовой транспорт", VehicleType.TRUCK);
        transportPanel.add(truckPanel);
        carPanel = new TransportSettingPanel("Легковой транспорт", VehicleType.CAR);
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

    public void onFrame(long dt) {
        if(!isInit)
        {
            ConfigManager.LoadHabitatConfig();
            initTransportPanel();
            pack();
            isInit = true;
        }
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

    public void setShowSimulationTime(boolean show) {
        if(showSimulationTime == show)
            return;

        showSimulationTime = show;
        hideSimulationTimeToggle.setSelected(!show);
        showSimulationTimeToggle.setSelected(show);
        Main.printLog(show ? "Show simulation time" : "Hide simulation time");
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

    public boolean getShowInfo() { return showInfo; }
    public void setShowInfo(boolean showInfo) {
        this.showInfo = showInfo;
        showInformationToggleButton.setSelected(showInfo);
    }
    public boolean getShowSimulationTime() { return showSimulationTime; }

}