package model;

import graphics.IView;
import graphics.View;
import graphics.imageProcessing.ImgWindow;
import model.container.ImageContainer;
import model.container.ImagePair;
import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Model {
    private final IView view;
    private final ImageContainer container;

    public Model(){
        view = new View(this);
        container = new ImageContainer();
    }

    public void smoothImage(Image img, ImgWindow parent){
        Mat mat = null;
        try{
            File tempFile = new File("tmp.png");
            tempFile.createNewFile();

            BufferedImage imageToWrite = new BufferedImage(
                    img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB
            );
            imageToWrite.getGraphics().drawImage(img, 0, 0, null);
            ImageIO.write(imageToWrite, "png", tempFile);

            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            mat = Imgcodecs.imread(tempFile.getPath());
            tempFile.delete();
        }catch (IOException e){
            e.printStackTrace();
        }
        if(mat == null){
            parent.setProcessedImage(null);
        }
        Mat finalMat = mat;
        Thread thread = new Thread(() -> {
            org.opencv.photo.Photo.fastNlMeansDenoisingColored(finalMat, finalMat, 10, 10, 7, 21);

            MatOfByte mob = new MatOfByte();
            Imgcodecs.imencode(".png", finalMat, mob);
            try{
                parent.setProcessedImage(ImageIO.read(new ByteArrayInputStream(mob.toArray())));
            }catch (IOException e){
                parent.setProcessedImage(null);
            }
        });
        thread.start();
    }

    public void saveImage(String newName, Image image){
        File savedImage = new File(newName);
        try {
            savedImage.createNewFile();
            BufferedImage imageToWrite = new BufferedImage(
                    image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB
            );
            imageToWrite.getGraphics().drawImage(image, 0, 0, null);
            ImageIO.write(imageToWrite, newName.substring(newName.lastIndexOf('.')+1), savedImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveLocal(String imgName, Image image, List<DotPoint> points){
        File tempImage = new File("temp/"+imgName);
        try{
            tempImage.delete();
            tempImage.createNewFile();
            BufferedImage imageToWrite = new BufferedImage(
                    image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB
            );
            imageToWrite.getGraphics().drawImage(image, 0, 0, null);
            ImageIO.write(imageToWrite, imgName.substring(imgName.lastIndexOf('.')+1), tempImage);

        }catch (IOException e){
            throw new RuntimeException(e);
        }
        container.updateImage(imgName, tempImage.getPath());
        container.setPoints(imgName, points);
        view.showImageList(container.getImages());

        //сохранение точек
        File dir = new File("dots/");
        dir.mkdir();
        File dostImage = new File("dots/"+imgName);
        if(points.size() > 0) {
            try {
                dostImage.createNewFile();
                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dostImage)))) {
                    for (DotPoint point : points) {
                        writer.write(point.getX() + ";" + point.getY());
                        writer.newLine();
                    }
                    writer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void processImages(String[] imagesPath){
        container.setImages(imagesPath);
        view.showImageList(container.getImages());
    }

    public void stackImages(List<String> imagesNames, int opt){
        if(imagesNames.size() == 0){
            return;
        }
        List<ImagePair> pairs = container.getImagePairListForNames(imagesNames);
        List<DotPoint> mainPoints = pairs.get(0).getPoints();

        //применяем полученные данные
        List<Mat> mats = new ArrayList<>();
        mats.add(pairs.get(0).getImage());
        int size = mainPoints.size()-1;
        MatOfPoint2f dst = new MatOfPoint2f(
                new Point(mainPoints.get(0).getX(), mainPoints.get(0).getY()),
                new Point(mainPoints.get(size/2).getX(), mainPoints.get(size/2).getY()),
                new Point(mainPoints.get(size).getX(), mainPoints.get(size).getY())
        );
        for(int i=1; i<pairs.size(); ++i){
            List<DotPoint> points = pairs.get(i).getPoints();
            int size1 = points.size()-1;
            MatOfPoint2f src = new MatOfPoint2f(
                    new Point(points.get(0).getX(), points.get(0).getY()),
                    new Point(points.get(size1/2).getX(), points.get(size1/2).getY()),
                    new Point(points.get(size1).getX(), points.get(size1).getY())
            );
            Mat newImg = new Mat();
            Imgproc.warpAffine(pairs.get(i).getImage(), newImg , Imgproc.getAffineTransform(src, dst),
                    new Size(pairs.get(0).getImage().width(), pairs.get(0).getImage().height()));
            mats.add(newImg);
        }

        //выполняем сложение
        Mat result1 = new Mat(pairs.get(0).getImage().size(), CvType.CV_8UC3);
        result1.setTo(new Scalar(0, 0, 0));
        if(opt == 2) {
            for (Mat mat : mats) {
                Core.add(result1, mat, result1);
                Core.normalize(result1, result1, 0, 255, Core.NORM_MINMAX); //хороший результат
            }
        }else{
            for (Mat mat : mats) {
                Core.add(result1, mat, result1);
            }
        }

        MatOfByte matOfByte1 = new MatOfByte();
        Imgcodecs.imencode(".png", result1, matOfByte1);
        try {
            Image image = ImageIO.read(new ByteArrayInputStream(matOfByte1.toArray()));
            view.showStackedImage(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void exit(){
        container.deleteFromDisk();
    }
}
