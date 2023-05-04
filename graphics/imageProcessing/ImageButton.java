package graphics.imageProcessing;

import graphics.View;
import model.container.ImagePair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ImageButton extends JButton implements ActionListener {
    private final ImagePair imgPair;
    private final Image img;
    private final View view;

    public ImageButton(ImagePair name, Image img, View view){
        int height = 150;
        int width = 200;
        setPreferredSize(new Dimension(width, height));

        this.imgPair = name;
        this.img = img;
        this.view = view;
        setIcon(new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        addActionListener(this);
    }

    public String getImgName(){
        return imgPair.getFile().getName();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        view.showImage(img, imgPair);
    }
}
