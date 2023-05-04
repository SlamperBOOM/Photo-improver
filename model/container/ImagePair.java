package model.container;

import model.DotPoint;
import org.opencv.core.Mat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImagePair {
    private final Mat image;
    private final File path;
    private List<DotPoint> points = new ArrayList<>();

    public ImagePair(String path, Mat image){
        this.path = new File(path);
        this.image = image;
    }

    public Mat getImage() {
        return image;
    }

    public File getFile() {
        return path;
    }

    public List<DotPoint> getPoints() {
        return points;
    }

    public void setPoints(List<DotPoint> points) {
        this.points = points;
    }

    public String getExtension(){
        if(path.getName().lastIndexOf('.') == -1){
            return null;
        }else {
            return path.getName().substring(path.getName().lastIndexOf('.'));
        }
    }
}
