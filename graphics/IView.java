package graphics;

import model.SimplePair;
import model.container.ImagePair;

import java.awt.*;
import java.util.List;

public interface IView {
    void showImageList(List<SimplePair<ImagePair, Image>> images);
    void showStackedImage(Image img);
}
