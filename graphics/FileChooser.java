package graphics;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class FileChooser {

    public String[] readImages(JFrame parent){
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle("Выберите снимки");
        chooser.setMultiSelectionEnabled(true);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Images", "jpg", "png", "jpeg");
        chooser.addChoosableFileFilter(filter);
        filter = new FileNameExtensionFilter(
                "TIFF files", "tif", "TIF", "tiff"
        );
        chooser.addChoosableFileFilter(filter);
        filter = new FileNameExtensionFilter(
                "Raw images", "dng", "raw"
        );
        chooser.addChoosableFileFilter(filter);

        if(chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION){
            File[] files = chooser.getSelectedFiles();
            String[] imagesPath = new String[files.length];
            for(int i=0; i<files.length; ++i){
                imagesPath[i] = files[i].getAbsolutePath();
            }
            return imagesPath;
        }else{
            return null;
        }
    }
}
