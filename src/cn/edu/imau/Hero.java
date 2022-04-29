package cn.edu.imau;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author HBYang
 * @Package cn.edu.imau
 * @date 2022/4/13 18:19
 * @Copyright © 内蒙古农业大学职业技术学院计算机技术与信息管理系
 */
public class Hero extends HunDLObject {
    static BufferedImage[] HeroLImage = new BufferedImage[7];//面朝左
    static BufferedImage[] HeroRImage = new BufferedImage[8];//面朝右
    static BufferedImage[] HeroUImage = new BufferedImage[4];//跳
    int index = 5;//当前模型下标
    BufferedImage this_img;//当前模型
    String direction = "";//模型朝向
    int step = 10;//英雄移动速度
    public void setDirection(String str){
        if(HeroJumpLock){
            direction = str;
        }

    }
    //按键判断
    public void JudgeDirection(){
            switch (direction){
                case "R" :
                    if(index == 6){
                        index = 0;
                    }
                    this_img = HeroRImage[index++];
                    x=x+step;
                    break;
                case "L":
                    direction = "L";
                    if(index >= 5){
                        index = 0;
                    }
                    this_img = HeroLImage[index++];
                    x=x-step;
                    break;
                case "U":
                    direction = "U";
                    HeroJump();
                    break;
            }


    }
    boolean HeroJumpLock = true;
    boolean isSkyRight = false;
    /**
     * 英雄跳
     */
    public void HeroJump(){
        if(HeroJumpLock){
            if(Arrays.asList(HeroRImage).contains(this_img)){//判断当前英雄图片是否存在于HeroRImage，从而判断hero朝向
                this_img = HeroUImage[0];
                y-=140;
                x+=100;
                isSkyRight = true;
            }else if(Arrays.asList(HeroLImage).contains(this_img)){
                this_img = HeroUImage[2];
                y -=140;
                x -=100;
                isSkyRight = false;
            }
            HeroJumpLock = false;
        }

    }
    /**
     * 英雄落
     */
    public void HeroDown(){
        if(this_img == HeroUImage[0] ){
            this_img = HeroUImage[1];
                y+=140;
                x+=40;
            direction = "R";
        }else if (this_img == HeroUImage[2]){
            this_img = HeroUImage[3];
                y += 140;
                x-=40;
            direction = "L";
        }
        HeroJumpLock = true;
    }
    /**
     * 英雄站立方法
     */
    public void setStandup(){
        if(direction.equals("R")){
            this_img = HeroRImage[5];
        }else if(direction.equals("L")){
            this_img = HeroLImage[5];
        }

    }
    /**
     *  设置英雄的xy坐标
     */
    public void setHeroX_y(int x,int y){
        this.x=x;
        this.y=y;
    }
    /**
     *  构造方法载入图片、设置默认位置
     */
    public Hero(){
        try {
            for (int i = 0; i < 7; i++) {
                HeroLImage[i] = ImageIO.read(new File("images/L"+i+".png"));
            }
            for (int i = 0; i < 8; i++) {
                HeroRImage[i] = ImageIO.read(new File("images/R"+i+".png"));
            }
            for (int i = 0; i < 4; i++) {
                HeroUImage[i] = ImageIO.read(new File("images/u"+i+".png"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        this_img = HeroRImage[5];//默认为面向右的模型
        setHeroX_y(400,386);//默认位置
        setDirection("R");//默认朝向
        this.width = this_img.getWidth();
        this.height = this_img.getHeight();
        this.lifes = 3;

    }
    /**
     * 设置英雄射击时的动作
     */
    public void HeroShoot(){
        if(direction.equals("R")){
            this_img = HeroRImage[5];
        }else if(direction.equals("L")){
            this_img = HeroLImage[5];
        }
    }
    @Override
    public void paint(Graphics g) {

    }

}
