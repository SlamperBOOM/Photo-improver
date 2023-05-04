package graphics.imageProcessing;

import model.DotPoint;
import model.container.ImagePair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImageDotsPanel extends JPanel implements MouseWheelListener, MouseListener {
    private Image img;
    private final List<DotPoint> pointsForImage;

    private DotPoint leftTopPoint;
    private DotPoint size;

    private BufferedImage canvas;

    private final boolean addingDots;

    public ImageDotsPanel(){
        addMouseWheelListener(this);
        addMouseListener(this);

        pointsForImage = new ArrayList<>();
        addingDots = false;
    }

    public ImageDotsPanel(ImagePair pair){
        addMouseWheelListener(this);
        addMouseListener(this);

        pointsForImage = pair.getPoints();
        addingDots = true;
    }

    public void setImg(Image img){
        this.img = img;
    }

    public void drawDots(){
        Graphics g = canvas.getGraphics();
        for(int i=0; i<pointsForImage.size(); ++i){
            DotPoint point = pointsForImage.get(i);
            int x = (int) Math.round(
                    (point.getX()-leftTopPoint.getX())
                    *getPreferredSize().width*1.0/size.getX()
            );
            int y = (int) Math.round(
                    (point.getY()-leftTopPoint.getY())
                    *getPreferredSize().height*1.0/size.getY()
            );

            if(!(x < 0 || x > getPreferredSize().width ||
                    y < 0 || y > getPreferredSize().height)){
                g.setColor(Color.RED);

                int offset = 7;
                g.drawLine(x-offset, y, x+offset, y);
                g.drawLine(x, y-offset, x, y+offset);

                g.setColor(Color.WHITE);
                g.drawString("("+ (i + 1) +")", x+5, y-10);
            }
        }
        repaint();
    }

    public void drawImage(){
        Graphics g = canvas.getGraphics();

        BufferedImage crop = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        crop.getGraphics().drawImage(img, 0, 0, null);
        if(size.getX() + leftTopPoint.getX() <= img.getWidth(null)
                && size.getY() +leftTopPoint.getY() <= img.getHeight(null)){
            crop = crop.getSubimage(leftTopPoint.getX(), leftTopPoint.getY(),
                    size.getX(), size.getY());
        }else{
            resetScale();
        }

        /*Image cropImage = img.getScaledInstance(
                canvas.getWidth()*img.getWidth(null)/size.getX(),
                canvas.getHeight()*img.getHeight(null)/size.getY(),
                Image.SCALE_SMOOTH);
        g.drawImage(cropImage,
                -leftTopPoint.getX()*cropImage.getWidth(null)/img.getWidth(null),
                -leftTopPoint.getY()*cropImage.getHeight(null)/img.getHeight(null),
                null);*/
        g.drawImage(
                crop.getScaledInstance(getPreferredSize().width, getPreferredSize().height, Image.SCALE_SMOOTH),
                0, 0, null);
    }

    public void resetScale(){
        leftTopPoint = new DotPoint(0, 0);
        size = new DotPoint(img.getWidth(null), img.getHeight(null));
    }

    public void reset(){
        canvas = new BufferedImage(getPreferredSize().width, getPreferredSize().height, BufferedImage.TYPE_INT_RGB);
        drawImage();
        drawDots();//в конце метода вызывается repaint()
    }

    public void deleteLastPoint(){
        if(pointsForImage.size() > 0) {
            pointsForImage.remove(pointsForImage.size() - 1);
            reset();
        }
    }

    public List<DotPoint> getPointsForImage(){
        return pointsForImage;
    }

    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        graphics.drawImage(canvas, 0, 0, this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(addingDots) {
            float x = e.getX();
            float y = e.getY();
            x = (x * size.getX() / getPreferredSize().width) + leftTopPoint.getX();
            y = (y * size.getY() / getPreferredSize().height) + leftTopPoint.getY();

            pointsForImage.add(new DotPoint(Math.round(x), Math.round(y)));
            drawDots();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int rotation = e.getWheelRotation(); //-1 - up/zoom+, 1 - down/zoom-
        double x = e.getX();
        double y = e.getY();
        if(!(x < 0 || x > getPreferredSize().width ||
                y < 0 || y > getPreferredSize().height)) {
            x = (x*size.getX()/getPreferredSize().width) + leftTopPoint.getX();
            y = (y*size.getY()/getPreferredSize().height) + leftTopPoint.getY();
            double k;
            switch (rotation){
                case -1: {
                    k = 0.8;
                    break;
                }
                case 1:{
                    k =1/0.8;
                    break;
                }
                default:{
                    k=1;
                    break;
                }
            }
            int topRange = (int) (y - (y-leftTopPoint.getY())*k);
            int bottomRange = (int) (y + (leftTopPoint.getY() + size.getY() - y)*k);
            int leftRange = (int) (x - (x-leftTopPoint.getX())*k);
            int rightRange = (int) (x + (leftTopPoint.getX() + size.getX() - x)*k);

            if(topRange < 0){
                bottomRange -= topRange;
                topRange = 0;
            }
            if(leftRange < 0){
                rightRange -= leftRange;
                leftRange = 0;
            }
            leftTopPoint = new DotPoint(leftRange, topRange);
            size = new DotPoint(rightRange - leftRange, bottomRange - topRange);
            reset();
        }
    }
}
