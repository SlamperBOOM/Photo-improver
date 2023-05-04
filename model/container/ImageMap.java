package model.container;

import model.DotPoint;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ImageMap {
    private final List<ImagePair> images = new ArrayList<>();

    public void add(String path){ //только при выборе изображений с диска
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat img = Imgcodecs.imread(path);
        ImagePair pair = new ImagePair(path, img);
        if(pair.getExtension() != null){
            images.add(pair);
        }

        File dotsForImage = new File("dots/"+pair.getFile().getName());
        if(dotsForImage.exists()){
            List<DotPoint> points = new ArrayList<>();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dotsForImage)))){
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] dots = line.split(";");
                    points.add(new DotPoint(
                            Integer.parseInt(dots[0]),
                            Integer.parseInt(dots[1])
                    ));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            pair.setPoints(points);
        }
    }

    public void add(String path, int index){ //только при обновлении изображения из программы
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat img = Imgcodecs.imread(path);
        ImagePair pair = new ImagePair(path, img);
        if(pair.getExtension() != null){
            images.add(index, pair);
        }
    }

    public void clear(){
        images.clear();
    }

    public List<ImagePair> getImages(){
        return images;
    }

    public Mat getCVImage(String imgName){
        for(ImagePair pair : images){
            if(pair.getFile().getName().equals(imgName)){
                return pair.getImage();
            }
        }
        return null;
    }
}
