package model.container;

import model.DotPoint;
import model.SimplePair;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ImageContainer {
    private final ImageMap images = new ImageMap();

    public void setImages(String[] paths){
        deleteFromDisk();
        images.clear();
        List<String> newPaths = transferImages(paths);
        for(String path : newPaths){
            images.add(path);
        }
    }

    public List<SimplePair<ImagePair, Image>> getImages(){
        List<SimplePair<ImagePair, Image>> imgs = new ArrayList<>();
        for(ImagePair img : images.getImages()){
            MatOfByte matOfByte = new MatOfByte();
            Imgcodecs.imencode(img.getExtension(), img.getImage(), matOfByte);
            try {
                imgs.add(new SimplePair<>(
                        img,
                        ImageIO.read(new ByteArrayInputStream(matOfByte.toArray()))
                ));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imgs;
    }

    public List<ImagePair> getImagePairListForNames(List<String> imgNames){
        List<ImagePair> pairs = new ArrayList<>();
        for(ImagePair pair : images.getImages()){
            for(String name : imgNames) {
                if (pair.getFile().getName().equals(name)){
                    pairs.add(pair);
                    break;
                }
            }
        }
        return pairs;
    }

    public void updateImage(String imgName, String newPath){
        String path = new File(newPath).getPath();
        List<ImagePair> list = images.getImages();
        int index = 0;
        for(ImagePair pair : list){
            if(pair.getFile().getName().equals(imgName)){
                index = list.indexOf(pair);
                list.remove(pair);
                break;
            }
        }
        images.add(path, index);
    }

    public void setPoints(String imgName, List<DotPoint> points){
        for(ImagePair pair : images.getImages()){
            if(pair.getFile().getName().equals(imgName)){
                pair.setPoints(points);
                break;
            }
        }
    }

    public Mat getCVImage(String imgName){
        return images.getCVImage(imgName);
    }

    public void deleteFromDisk(){
        for(ImagePair path : images.getImages()){
            File img = new File(path.getFile().getPath());
            img.delete();
        }
        images.clear();
    }

    private List<String> transferImages(String[] paths){
        List<String> newPaths = new ArrayList<>();
        for (String path : paths){
            File img = new File(path);
            newPaths.add(copyImageToTemp(img));
        }
        return newPaths;
    }

    private String copyImageToTemp(File image){
        File dir = new File("temp/");
        dir.mkdir();
        try(InputStream reader = new FileInputStream(image)){
            File outputImage = new File("temp/"+image.getName());
            outputImage.createNewFile();
            try(OutputStream writer = new FileOutputStream(outputImage)){
                writer.write(reader.readAllBytes());
            }
            return outputImage.getPath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
