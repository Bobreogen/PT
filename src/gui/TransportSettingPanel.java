package gui;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.util.Objects;

public class TransportSettingPanel extends JPanel {
    private final JTextField timeGenerationValue;
    private final JSlider probabilityValue;


    TransportSettingPanel(String name) {
        setLayout(new GridLayout(5,1,0,5));
        JLabel nameText = new JLabel(name);
        nameText.setFont(new Font("Arial",Font.BOLD,16));
        add(nameText);
        JLabel timeToGenerationText = new JLabel("Период генерации (сек)");
        add(timeToGenerationText);

//        timeGenerationValue = new JFormattedTextField(new JFormattedTextField.AbstractFormatter() {
//            @Override
//            public Object stringToValue(String text) throws ParseException {
//                try{
//                    return Integer.valueOf(text);
//                } catch (Exception e) {
//                    ParseException parseException = new ParseException("", 0);
//                    parseException.addSuppressed(e);
//                    throw parseException;
//                }
//            }
//
//            @Override
//            public String valueToString(Object value) throws ParseException {
//                value = Objects.requireNonNullElse(value, 0);
//                return ((Integer)value).toString();
//            }
//        });
        timeGenerationValue = new JTextField("5");
//        timeGenerationValue.setValue(5);
        add(timeGenerationValue);


        JLabel probabilityText = new JLabel("Вероятность появления (%)");
        add(probabilityText);
        probabilityValue = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        probabilityValue.setPaintTicks(true);
        probabilityValue.setMinorTickSpacing(10);
        probabilityValue.setMajorTickSpacing(50);
        probabilityValue.setSnapToTicks(true);
        probabilityValue.setFocusable(false);

        add(probabilityValue);
    }

    int getTimeGeneration() {
        return Integer.parseInt(timeGenerationValue.getText())*1000;
    }

    int getProbability() { return probabilityValue.getValue(); }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        timeGenerationValue.setEnabled(enabled);
        probabilityValue.setEnabled(enabled);
    }
}
