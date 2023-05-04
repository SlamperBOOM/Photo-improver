package graphics.imageProcessing;

import graphics.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StackedImageWindow extends JFrame implements ActionListener, ImgWindow {
    private final View view;
    private Image img;
    private final TimerLabel timeShower;

    private final ImageDotsPanel imagePanel;

    public StackedImageWindow(View view){
        this.view = view;
        setSize(1360, 700);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        imagePanel = new ImageDotsPanel();
        imagePanel.setPreferredSize(new Dimension(800, 600));
        mainPanel.add(imagePanel);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JButton smoothButton = new JButton();
        smoothButton.setActionCommand("smooth");
        smoothButton.addActionListener(this);
        smoothButton.setText("Уменьшить шумы");
        smoothButton.setMinimumSize(new Dimension(120, 30));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        controlPanel.add(smoothButton, constraints);

        JButton save2Button = new JButton();
        save2Button.setActionCommand("save disk");
        save2Button.addActionListener(this);
        save2Button.setText("Сохранить на диск");
        save2Button.setMinimumSize(new Dimension(120, 30));
        constraints.gridx = 1;
        constraints.gridy = 0;
        controlPanel.add(save2Button, constraints);

        timeShower = new TimerLabel();
        constraints.gridx = 0;
        constraints.gridy = 1;
        controlPanel.add(timeShower, constraints);

        JButton resetButton = new JButton();
        resetButton.setActionCommand("reset");
        resetButton.addActionListener(this);
        resetButton.setText("Восстановить масштаб");
        resetButton.setMinimumSize(new Dimension(120, 30));
        constraints.gridx = 1;
        constraints.gridy = 2;
        controlPanel.add(resetButton, constraints);

        mainPanel.add(controlPanel);
        setContentPane(mainPanel);
        setTitle("Stacked image");
    }

    public void setImg(Image img) {
        this.img = img;
        imagePanel.setImg(img);
        imagePanel.resetScale();
        imagePanel.reset();
    }

    public void open() {
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command){
            case "smooth" : {
                view.smoothImage(img, this);
                timeShower.start();
                JOptionPane.showMessageDialog(
                        this, "Пожалуйста, не закрывайте это окно до завершения обработки изображения");
                break;
            }
            case "save disk": {
                JFileChooser saver = new JFileChooser();
                if(saver.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
                    view.saveImage(saver.getSelectedFile().getAbsolutePath(), img);
                }
                break;
            }
            case "reset":{
                imagePanel.resetScale();
                imagePanel.reset();
                break;
            }
        }
    }

    @Override
    public void setProcessedImage(Image img) {
        timeShower.stop();
        if(img == null){
            JOptionPane.showMessageDialog(this, "Не удалось убрать шумы у изображения");
        }else {
            setImg(img);
            JOptionPane.showMessageDialog(this, "Убирание шумов завершено");
        }
    }
}
