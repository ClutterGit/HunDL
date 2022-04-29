package cn.edu.imau;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author HBYang
 * @Package cn.edu.imau
 * @date 2022/4/13 18:20
 * @Copyright © 内蒙古农业大学职业技术学院计算机技术与信息管理系
 */
public abstract class HunDLObject {
    int x;
    int y;
    int width;
    int height;
    /**
     * 状态
     * life=0活着   die=1死了
     */
    protected static final int LIFE=0,DIE=1;
    /**
     * 对象当前的状态
     */
    protected int state=LIFE;
    /**
     * 生命值
     */
    protected int lifes=1;
    /**
     * 绘制对象的抽象方法
     * @param g
     */
    public abstract void paint(Graphics g);
    /**
     * 判断碰撞方法
     *
     * @param obj 被检查的对象
     * @return 返回true表是obj碰撞了, false表示没有碰撞
     */
    public boolean hit(HunDLObject obj) {
        int x1 = this.x - obj.width;
        int x2 = this.x + this.width;
        return obj.x > x1 && obj.x < x2 && this.y < obj.y+height && this.y+height > obj.y;
    }
    /**
     *  判断是否越界
     */
    protected boolean isOut(){
        //角色完全超出屏幕,表示越界
        return this.x<0-this.width;
    }
}
