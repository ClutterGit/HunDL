package cn.edu.imau;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author HBYang
 * @Package cn.edu.imau
 * @date 2022/4/13 18:20
 * @Copyright © 内蒙古农业大学职业技术学院计算机技术与信息管理系
 */
public class Bullet extends HunDLObject{
    public  static BufferedImage bulletImg;
    String bulletState ;//表示子弹射击的方向

    /**
     * 静态代码块 用于加载图片到变量中
     */
    static {
        try {
            bulletImg= ImageIO.read(new File("images/litterbullet.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 子弹类的构造方法
     * 用于初始化子弹出现的位置、子弹的宽高及射击的方向
     * @param x
     * @param y
     * @param bulletState
     */
    public Bullet(int x, int y,String bulletState){
        this.x=x;
        this.y=y;
        this.width=bulletImg.getWidth();
        this.height=bulletImg.getHeight();
        this.bulletState = bulletState;
    }
    /**
     * 重写父类的move移动方法
     */
    public void move() {
        if(bulletState.equals("R")){
            this.x+=15;
        }else if(bulletState.equals("L")){
            this.x-=15;
        }
    }

    /**
     * 子弹绘制方法
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        g.drawImage(bulletImg,this.x,this.y,null);
    }
}
