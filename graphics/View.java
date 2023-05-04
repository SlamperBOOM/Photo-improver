package graphics;

import graphics.imageProcessing.ImageWindow;
import graphics.imageProcessing.ImgWindow;
import graphics.imageProcessing.StackedImageWindow;
import model.DotPoint;
import model.Model;
import model.SimplePair;
import model.container.ImagePair;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class View implements IView{
    private final MainWindow window;
    private final Model model;

    public View(Model model){
        this.model = model;
        window = new MainWindow(this);
    }

    protected void sendImages(String[] imagesPath){
        model.processImages(imagesPath);
    }

    public void exit(){
        model.exit();
    }

    public void showImage(Image img, ImagePair imgPair){
        ImageWindow imgWindow = new ImageWindow(this, imgPair);
        imgWindow.setImg(img);
        imgWindow.open();
    }

    public void stackImages(List<String> images, int opt){
        model.stackImages(images, opt);
    }

    public void smoothImage(Image img, ImgWindow parent){
        model.smoothImage(img, parent);
    }

    public void saveImage(String newName, Image image){
        model.saveImage(newName, image);
    }

    public void saveLocal(String imgName, Image image, List<DotPoint> points){
        model.saveLocal(imgName, image, points);
    }

    @Override
    public void showImageList(List<SimplePair<ImagePair, Image>> images) {
        window.showImages(images);
    }

    @Override
    public void showStackedImage(Image img) {
        StackedImageWindow imgWindow = new StackedImageWindow(this);
        imgWindow.setImg(img);
        imgWindow.open();
    }
}
