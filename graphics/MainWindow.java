package graphics;

import graphics.imageProcessing.ImageShower;
import model.SimplePair;
import model.container.ImagePair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class MainWindow extends JFrame implements ActionListener{
    private final View view;
    private final ImageShower imageShower;

    public MainWindow(View view){
        this.view = view;

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JButton button = new JButton("Выбрать файлы");
        button.setActionCommand("choose");
        button.addActionListener(this);
        button.setPreferredSize(new Dimension(100, 30));
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 0.5;
        mainPanel.add(button, constraints);

        imageShower = new ImageShower(view);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weighty = 0.5;
        mainPanel.add(imageShower, constraints);

        JButton stackImages = new JButton("Сложить выбранные изображения");
        stackImages.addActionListener(this);
        stackImages.setActionCommand("stack");
        stackImages.setPreferredSize(new Dimension(150, 30));
        constraints.gridy = 2;
        constraints.gridx = 0;
        mainPanel.add(stackImages, constraints);

        setContentPane(mainPanel);
        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                view.exit();
                System.exit(0);
            }
        });
        setLocation(200, 200);
        setVisible(true);
        setTitle("Simple Image Stacker");
    }

    public void showImages(List<SimplePair<ImagePair, Image>> images){
        imageShower.showImages(images);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "choose": {
                FileChooser chooser = new FileChooser();
                String[] paths = chooser.readImages(this);
                if (paths != null) {
                    view.sendImages(paths);
                }
                break;
            }
            case "stack":{
                int opt = JOptionPane.showOptionDialog(this,
                        "Выполнить автоматическую нормализацию изображения?",
                        "Нормализация", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, null, JOptionPane.YES_OPTION);
                view.stackImages(imageShower.getImagesToStack(), opt);
                break;
            }
        }
    }
}
