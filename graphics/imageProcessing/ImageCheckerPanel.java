package graphics.imageProcessing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ImageCheckerPanel extends JPanel implements ActionListener {
    private final ImageButton button;
    private final JButton checkBox;

    private boolean isChecked = false;

    public ImageCheckerPanel(ImageButton button){
        this.button = button;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        checkBox = new JButton("◻");
        checkBox.setMinimumSize(new Dimension(20, 20));
        checkBox.addActionListener(this);

        add(button);
        add(checkBox);
    }

    public boolean getCheckedStatus(){
        return isChecked;
    }

    public String getImgName(){
        return button.getImgName();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(!isChecked){
            checkBox.setText("◼");
            isChecked = true;
        }else{
            checkBox.setText("◻");
            isChecked = false;
        }
    }
}
