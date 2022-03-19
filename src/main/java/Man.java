import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.*;


public class Man extends JFrame{
    MyPanel mp ;
    JButton jb ;
    JLabel jl;
    boolean start = false;
    static Man man;
    int go = 0;
    int Higher = 0;

    public static void main(String[] args) {
        man = new Man();
    }



    public Man(){
        mp = new MyPanel();
        jl = new JLabel();
        this.setLayout(new  BorderLayout());
        if(!start) {
            //System.out.println("&");
            init();
        }
        jb = new JButton("start");
        jb.setFocusable(false);
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jb.setEnabled(false);
                start=true;
                mp.Shoot();
                go = 1;
            }
        });
        this.add(jb,BorderLayout.NORTH);
        this.setSize(700,400);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    //初始化畫面
    public void init(){
//        mp = new MyPanel();
//        System.out.println("@");
        mp.set();
        this.add(mp,BorderLayout.CENTER);
        this.addKeyListener(mp);
        jl.setText("The Higher Score: "+Higher);
        mp.add(jl);
    }

    class MyPanel extends JPanel implements KeyListener, Runnable {
        Thread shoot;
        Missile ms;
        Missile ms2;
        Missile bullet;
        Missile lazer;
        explode explode = new explode();
        Jump jump = new Jump();
        static int Mx = 600; //砲彈一
        static int My = 200;
        static int Mx2 = 600; //砲彈二
        static int My2 = 120;
        static int count = 0; //砲彈發射時間
        static int cd = 0; //攻擊冷卻時間
        static int wt = 0; //不讓下連續按
        static int x = 230; //人物的
        static int y = 250;
        static int bx = 230; //子彈的
        static int by = 250;
        static int bh = 40;
        static int w = 10; //舉砲管
        static int h = 20;
        static int a = 0; //砲管微調
        static int lx = 0; //雷射光
        static int lw = 0;
        static int WM = 0;
        boolean l = true;

        public MyPanel(){

        }

        //重製參數
        public void set(){
            Mx = 600; //砲彈一
            My = 200;
            Mx2 = 600; //砲彈二
            My2 = 120;
            count = 0; //砲彈發射時間
            cd = 0; //攻擊冷卻時間
            wt = 0; //不讓下連續按
            x = 230; //人物的
            y = 250;
            bx = 230; //子彈的
            by = 250;
            bh = 40;
            w = 10; //舉砲管
            h = 20;
            a = 0; //砲管微調
            lx = 0; //雷射光
            lw = 0;
            WM = 0;
            l=true;
        }

        public void paint(Graphics g) {
            super.paint(g);
            if(count%20==0) Mx=600;
            ms = new Missile(Mx, My);
            ms.draw(g);
            if(count>10){
                if(count%20==10) Mx2=600;
                ms2 = new Missile(Mx2,My2);
                ms2.draw(g);
            }
            if(cd!=0){
                bullet = new Missile(bx+45,by-40);
                bullet.bullet(g);
            }
            else by = y;
            if(lx!=0) {
                lazer = new Missile(lx,0);
                lazer.lazercome(g);
                if(lw==1){
                    lazer.lazer(g);
                }
            }
            g.setColor(Color.red);
            g.fillOval(x, y - 60, 20, 20); //head
            g.setColor(Color.BLACK);
            g.drawRect(x, y - 40, 20, 40); //body
            if (y < 230) {
                g.drawRect(x - 10, y - 50, 10, 20); //hand
                g.drawRect(x + 20, y - 50 + a, w, h);
            } else {
                g.drawRect(x - 10, y - 40, 10, 20); //hand
                g.drawRect(x + 20, y - 40, w, h);
            }
            g.drawRect(x + 10, y, 10, bh); //feet
            g.drawRect(x, y, 10, bh);
            g.fillRect(0, 272, 700, 160);
            touch();
//            System.out.println(count);
        }

        //鍵盤的動作
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT && go==1 && x>20) {
                x -= 15;
                if(cd == 0) bx = x;
            } else if (e.getKeyCode() == KeyEvent.VK_UP && go==1) {
                if (y == 250){
                    jump.DropStart();
                }
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && go==1) {
                x += 15;
                if(cd == 0) bx = x;
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE && go==1){
                w = 20;
                h = 10;
                if(cd==0) {
                    cd = 1;
                    Bullet Bullet = new Bullet();
                    Bullet.BulletStart();
                }
                if(y<250) a=10;
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN && go==1){
                if(wt == 0){
                    bh-=20;
                    y+=20;
                    if(cd==0) by = y;
                    wt = 1;
                }
            }
            this.repaint();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_SPACE && go==1){
                w = 10;
                h = 20;
                if(y<250) a=0;
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN && go==1){
                bh=20;
                y-=20;
                wt = 0;
            }
        }

        //判定是否被砲彈或雷射光打到
        public void touch(){
            Bomb Bomb = new Bomb();
            Lose lose = new Lose();
            if(l){
                if(explode.touch(x,y,Mx,My,bx,by,lx,lw,cd)){
                    Bomb.BombStart();
                    WM = 0;
                    if(!explode.getT()){
                        l=false;
                        listener();
                        lose.loseStart();
                    }
                }
                else if(explode.touch(x,y,Mx2,My2,bx,by,lx,lw,cd)){
                    Bomb.BombStart();
                    WM = 1;
                    if(!explode.getT()){
                        l=false;
                        listener();
                        lose.loseStart();
                    }
                }
            }
        }

        //判定光線是否打到砲彈
        class Bomb extends Thread implements Runnable{
            Thread Bomb;
            public void BombStart(){
                if(explode.getT()){
                    Bomb = new Thread(this);
                    Bomb.start();
                }
            }
            public void run(){
                System.out.println("shoot in Thread");
            }
        }

        //射光線的動作
        class Bullet extends Thread implements Runnable{
            Thread bullet;
            public void BulletStart(){
                if(w==20||h==10){
                    bullet = new Thread(this);
                    bullet.start();
                }
            }
            @Override
            public void run() {
                    while (cd<5){
                        try {
                            Thread.sleep(200);
                            cd+=1;
                            bx+=40;
                            mp.repaint();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    cd=0;
                    bx=x;
            }
        }

        //砲彈發射
        public void Shoot(){
            //System.out.println("%");
            if(start){
                shoot = new Thread(this);
                shoot.start();
            }
        }
        @Override
        public void run() {
            int difficult = 0;
            while (start){
                if(explode.getT() && WM == 0) Mx=-100;
                else Mx-=30;
                if(count>10){
                    if(explode.getT() && WM == 1) Mx2=-100;
                    else Mx2-=30;
                }
                if(count%20==0){
                    int r = (int)(Math.random() * 12);
                    switch(r%3){
                        case 0:
                            My = 180;
                            break;
                        case 1:
                            My = 200;
                            break;
                        case 2:
                            My = 220;
                            break;
                    }
                    if(r>7){
                        Lazer Lazer = new Lazer();
                        Lazer.lazerStart();
                    }
                }
                else if(count%20==10){
                    int r = (int)(Math.random() * 12);
                    switch(r%3){
                        case 0:
                            My2 = 180;
                            break;
                        case 1:
                            My2 = 200;
                            break;
                        case 2:
                            My2 = 220;
                            break;
                    }
                    if(r>7){
                        Lazer Lazer = new Lazer();
                        Lazer.lazerStart();
                    }
                }
                this.repaint();
                try {
                    if(count%40==0) difficult+=5;
                    Thread.sleep(100-difficult);
                    count+=1;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        public void stop(){
            start = false;
        }

        //跳的動作
        class Jump extends Thread implements Runnable {
            Thread drop;
            public void DropStart() {
                drop = new Thread(this);
                drop.start();
            }

            public void run() {
                while (y >= 150) {
                    try {
                        Thread.sleep(50);
                        y -= 20;
                        if(cd==0) by = y;
                        mp.repaint();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                while (y < 250) {
                    try {
                        Thread.sleep(50);
                        y += 20;
                        if(cd==0) by = y;
                        mp.repaint();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        //雷射光的動作
        class Lazer extends Thread implements Runnable{
            Thread light;
            public void lazerStart(){
                light = new Thread(this);
                light.start();
            }
            public void run(){
                lx =((int)(Math.random() * 5))* 50;
                mp.repaint();
                try {
                    Thread.sleep(500);
                    lw = 1;
                    Thread.sleep(1000);
                    lw = 0;
                    lx = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        //被打到後的動作
        class Lose extends Thread implements Runnable{
            Thread lose ;
            public void loseStart(){
                lose = new Thread(this);
                lose.start();
            }
            public void run(){
                System.out.println("You lose");
                mp.stop();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //跳出分數的視窗
                JOptionPane.showMessageDialog(mp,"Your score is: "+count);
                System.out.println(count);
                //紀錄最高分
                if(count>Higher) Higher=count;
                ReBuilt();
            }
        }
    }

    //移除按鍵
    public void listener(){
        this.removeKeyListener(mp);
    }

    //重新建構遊戲畫面
    public void ReBuilt(){
        this.remove(mp);
        this.repaint();
        jb.setEnabled(true);
        init();
        go=0;
        this.revalidate();
    }
}
