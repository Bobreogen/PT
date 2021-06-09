package gui;

import entities.VehicleType;
import logic.Habitat;

import javax.swing.*;
import java.awt.*;

public class TransportSettingPanel extends JPanel {
    private final JTextField timeGenerationValue;
    private final JSlider probabilityValue;
    private final JTextField lifeTimeValue;

    TransportSettingPanel(String name, VehicleType vt) {
        setLayout(new GridLayout(7,1,0,5));
        JLabel nameText = new JLabel(name);
        nameText.setFont(new Font("Arial",Font.BOLD,16));
        add(nameText);
        JLabel timeToGenerationText = new JLabel("Период генерации (сек)");
        add(timeToGenerationText);

        timeGenerationValue = new JTextField(String.valueOf(Habitat.instance().getTimeGenerationByType(vt)/1000));
        add(timeGenerationValue);


        JLabel probabilityText = new JLabel("Вероятность появления (%)");
        add(probabilityText);
        probabilityValue = new JSlider(JSlider.HORIZONTAL, 0, 100, (int)Habitat.instance().getGenerateProbabilityByType(vt));
        probabilityValue.setPaintTicks(true);
        probabilityValue.setMinorTickSpacing(10);
        probabilityValue.setMajorTickSpacing(50);
        probabilityValue.setSnapToTicks(true);
        probabilityValue.setFocusable(false);
        add(probabilityValue);

        JLabel lifeTimeText = new JLabel("Время жизни (сек)");
        add(lifeTimeText);
        lifeTimeValue = new JTextField(String.valueOf(Habitat.instance().getLifeTimeByType(vt)/1000));
        add(lifeTimeValue);

    }

    int getTimeGeneration() {
        if (numberValidation(timeGenerationValue.getText()) && Integer.parseInt(timeGenerationValue.getText()) > 0)
            return Integer.parseInt(timeGenerationValue.getText())*1000;
        else {
            JOptionPane.showMessageDialog(this, "Неправильно установленное значение, выставлено значение по умолчанию");
            timeGenerationValue.setText("2");
            return 2000;
        }
    }

    int getProbability() { return probabilityValue.getValue(); }

    boolean numberValidation(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public long getLifeTime() {
        if (numberValidation(lifeTimeValue.getText()) && Long.parseLong(lifeTimeValue.getText()) > 0)
            return Long.parseLong(lifeTimeValue.getText())*1000;
        else {
            JOptionPane.showMessageDialog(this, "Неправильно установленное значение, выставлено значение по умолчанию");
            lifeTimeValue.setText("5");
            return 5000;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        timeGenerationValue.setEnabled(enabled);
        probabilityValue.setEnabled(enabled);
        lifeTimeValue.setEnabled(enabled);
    }
}
