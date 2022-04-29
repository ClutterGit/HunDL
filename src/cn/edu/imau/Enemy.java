package cn.edu.imau;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * @author HBYang
 * @Package cn.edu.imau
 * @date 2022/4/13 18:20
 * @Copyright © 内蒙古农业大学职业技术学院计算机技术与信息管理系
 */
public class Enemy extends HunDLObject{
    static BufferedImage[] EnemyRImg = new BufferedImage[5];
    static BufferedImage[] EnemyLImg = new BufferedImage[5];
    BufferedImage this_img;//当前敌人的图片
    String EnemyLocation ;//敌人出现的位置
    int index = 0;//图片数组的下标
    int step = 20;//敌人走路的速度
    static {
        try {
            for (int i = 1; i < 5; i++) {
                EnemyRImg[i]= ImageIO.read(new File("images/VR"+i+".png"));
            }
            for (int i = 1; i < 5; i++) {
                EnemyLImg[i]= ImageIO.read(new File("images/VL"+i+".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    Random random = new Random();

    /**
     * 构造方法
     * @param EnemyLocation
     */
    public Enemy(String EnemyLocation){
        this.EnemyLocation = EnemyLocation;//初始化敌人出现的位置
        this.lifes = random.nextInt(5)+1;//随机出敌人的血量  1-5
        if(EnemyLocation.equals("R")){
            this_img = EnemyRImg[1];
            this.x=800;//初始化出现的敌人x坐标
        }else{
            this_img = EnemyLImg[1];
            this.x =-10;
        }
        this.width = this_img.getWidth();//初始化敌人的宽高
        this.height = this_img.getHeight();
        this.y= 386;
    }

    /**
     * 敌人的移动方法
     */
    public void move() {
        if(EnemyLocation.equals("R")){
            this.x-=step;
            this_img = EnemyRImg[index++];
        }else{
            this.x+=step;
            this_img = EnemyLImg[index++];
        }
        if(index > 4){
            index = 1;
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(this_img,this.x,this.y,null);
    }

    public int getScore() {
        return 5;
    }
}
