package graphics.imageProcessing;

import graphics.View;
import model.SimplePair;
import model.container.ImagePair;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ImageShower extends Panel{
    private final View view;

    private final JScrollPane scrollPane;
    private final JPanel imagesPanel;

    public ImageShower(View view){
        this.view = view;

        imagesPanel = new JPanel();
        imagesPanel.setLayout(new BoxLayout(imagesPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(imagesPanel);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        add(scrollPane);

        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public List<String> getImagesToStack(){
        List<String> names = new ArrayList<>();
        for(Component panel : imagesPanel.getComponents()){
            if(((ImageCheckerPanel)panel).getCheckedStatus()){
                names.add(((ImageCheckerPanel)panel).getImgName());
            }
        }
        return names;
    }

    public void showImages(List<SimplePair<ImagePair, Image>> images){
        imagesPanel.removeAll();
        imagesPanel.revalidate();
        for(SimplePair<ImagePair, Image> pair : images){
            ImageButton bt = new ImageButton(pair.getObj1(), pair.getObj2(), view);
            bt.repaint();
            imagesPanel.add(new ImageCheckerPanel(bt));
        }
        scrollPane.revalidate();
        scrollPane.updateUI();
    }
}
