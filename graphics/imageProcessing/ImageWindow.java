package graphics.imageProcessing;

import graphics.View;
import model.container.ImagePair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ImageWindow extends JFrame implements ActionListener, ImgWindow {
    private final View view;
    private final ImagePair pair;
    private Image img;
    private final TimerLabel timeShower;

    private final ImageDotsPanel imagePanel;

    public ImageWindow(View view, ImagePair imgPair){
        this.view = view;
        this.pair = imgPair;
        setSize(1360, 700);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        imagePanel = new ImageDotsPanel(pair);
        imagePanel.setPreferredSize(new Dimension(800,600));
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

        JButton save1Button = new JButton();
        save1Button.setActionCommand("save local");
        save1Button.addActionListener(this);
        save1Button.setText("Сохранить результат");
        save1Button.setMinimumSize(new Dimension(120, 30));
        constraints.gridx = 1;
        constraints.gridy = 0;
        controlPanel.add(save1Button, constraints);

        JButton save2Button = new JButton();
        save2Button.setActionCommand("save disk");
        save2Button.addActionListener(this);
        save2Button.setText("Сохранить на диск");
        save2Button.setMinimumSize(new Dimension(120, 30));
        constraints.gridx = 2;
        constraints.gridy = 0;
        controlPanel.add(save2Button, constraints);

        timeShower = new TimerLabel();
        constraints.gridx = 0;
        constraints.gridy = 1;
        controlPanel.add(timeShower, constraints);

        JButton deleteButton = new JButton();
        deleteButton.setText("Удалить последнюю точку");
        deleteButton.addActionListener(this);
        deleteButton.setActionCommand("delete");
        deleteButton.setMinimumSize(new Dimension(120, 30));
        constraints.gridx = 0;
        constraints.gridy = 2;
        controlPanel.add(deleteButton, constraints);

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
        setTitle(pair.getFile().getName());
    }

    public void setImg(Image img) {
        this.img = img;
        imagePanel.setImg(img);
        imagePanel.resetScale();
        imagePanel.reset();
    }

    @Override
    public void setProcessedImage(Image img){
        timeShower.stop();
        if(img == null){
            JOptionPane.showMessageDialog(this, "Не удалось убрать шумы у изображения");
        }else {
            setImg(img);
            JOptionPane.showMessageDialog(this, "Убирание шумов завершено");
        }
    }

    public void open(){
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
            case "save local": {
                view.saveLocal(pair.getFile().getName(), img, imagePanel.getPointsForImage());
                break;
            }
            case "reset":{
                imagePanel.resetScale();
                imagePanel.reset();
                break;
            }
            case "delete": {
                imagePanel.deleteLastPoint();
                break;
            }
        }
    }
}
