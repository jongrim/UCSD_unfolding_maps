package module3;

import processing.core.*;
/**
 * Created by Jon Grim on 2/14/2017.
 */
public class MyPApplet extends PApplet{

    PImage img;
    int h = hour();

    public void setup(){
        size(1600, 800);
        img = loadImage("GlacierPark.JPG");
        if (h < 9){
            //morning sun
            fill(230, 200, 10);
        } else if (h < 18) {
            //midday sun
            fill(255, 245, 0);
        } else {
            //late day sun
            fill(230, 90, 0);
            stroke(230,90,0);
        }
    }

    public void draw(){
        img.resize(1600, 800);
        image(img, 0, 0);
        background(img);

        ellipse(150, 100, 75, 75);
    }
}
