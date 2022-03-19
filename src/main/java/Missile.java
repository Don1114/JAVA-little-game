import java.awt.*;

//繪製砲彈、雷射光、光線的class
public class Missile {
    private int x,y;
    public Missile(int x,int y){
        this.x = x;
        this.y = y;
    }
    public void draw(Graphics g){
        g.setColor(Color.DARK_GRAY);
        g.fillOval(x,y,30,30);
    }

    public void bullet(Graphics g){
        g.setColor(Color.YELLOW);
        g.fillOval(x-3,y,10,10);
        g.fillRect(x-3,y+2,50,5);
    }

    public void lazercome(Graphics g){
        g.setColor(Color.GREEN);
        g.fillRect(x,0,30,40);
    }

    public void lazer(Graphics g){
        g.setColor(Color.GREEN);
        g.fillRect(x,0,30,400);
    }
}
