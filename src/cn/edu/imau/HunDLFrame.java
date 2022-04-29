package cn.edu.imau;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author HBYang
 * @Package cn.edu.imau
 * @date 2022/4/13 18:17
 * @Copyright © 内蒙古农业大学职业技术学院计算机技术与信息管理系
 */
public class HunDLFrame extends JPanel implements KeyListener{
    //声明背景图片类型
    BufferedImage bgImg;
    BufferedImage StartBgImg;
    BufferedImage EndBgImg;
    BufferedImage WinImg;
    //实例化英雄
    Hero hero =new Hero();
    //创建子弹数组
    Bullet[] bullets = {};
    //创建敌人数组
    Enemy[] enemies = {};
    //分数
    static int SCORE=0;
    //子弹个数
    static int bullet_number=50;
    //敌人出现的位置
    String EnemyLocation;
    /**
     * 游戏的状态
     * 0开始   1运行   2暂停   3结束
     */
    static int START=0,RUN=1,PAUSE=2,GAME_OVER=3,Game_Win=4;
    //游戏当前状态
    static int STATE = START;
    //static int STATE=1;
    boolean isJump = false;//是否是跳跃状态默认为没有跳跃
    int Herocount = 0;//timer减速
    int winScore = 100;//定义胜利需要的分数
    /**
     * 利用构造方法载入背景图片
     */
    public HunDLFrame(){
        try {
            bgImg = ImageIO.read(new File("images/bg.png"));
            StartBgImg = ImageIO.read(new File("images/Startbg.png"));
            EndBgImg = ImageIO.read(new File("images/endframe.png"));
            WinImg = ImageIO.read(new File("images/win.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 主方法，创建窗体
     * @param args
     */
    public static void main(String[] args) {
        //创建一个JFrame窗体
        JFrame jFrame = new JFrame("简易魂斗罗");
        //创建画布
        HunDLFrame hunDLFrame = new HunDLFrame();
        //将画布添加到窗体中
        jFrame.add(hunDLFrame);
        //设置窗体大小
        jFrame.setSize(900, 623);
        //设置默认关闭
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置窗口居中
        jFrame.setLocationRelativeTo(null);
        //设置窗口可见
        jFrame.setVisible(true);
        jFrame.addKeyListener(hunDLFrame);
        hunDLFrame.Start();
        while (true){
            playMusic();
        }
    }

    /**
     * 绘制方法
     * @param g
     */
    public void paint(Graphics g){
        g.drawImage(bgImg,0,0,null);

        if(STATE==START){
            //设置字体样式
            g.drawImage(StartBgImg,0,0,null);
            g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,40));
            g.setColor(new Color(0xFFFFFF));
            g.drawString("请按空格开始游戏",150,400);
        }else if(STATE==RUN){
            g.drawImage(hero.this_img, hero.x, hero.y,null);
            paintObject(bullets,g);//子弹绘制
            paintObject(enemies,g);//敌人绘制
            paintString(g);//文本绘制
        }else if(STATE == PAUSE){
            g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,40));
            g.setColor(new Color(0xFFFFFF));
            g.drawString("游戏暂停",280,300);
            paintString(g);//文本绘制
        }else if(STATE == GAME_OVER){
            g.drawImage(EndBgImg,0,0,null);
            g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,40));
            g.setColor(new Color(0xFFFFFF));
            g.drawString("Game Over",420,200);
            g.drawString("Score:"+SCORE,420,300);
            g.drawString("请按空格重新开始游戏",420,400);
        }else if(STATE == Game_Win){
            g.drawImage(WinImg,50,-150,null);
        }
    }

    /**
     * 静态执行HeroJumpTimer线程
     */
    {
        HeroJump();
    }
    /**
     * 按k跳跃连续动作方法
     */
    public void HeroJump(){
        Timer timer= new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(Herocount>2){
                    Herocount = 0;
                }
                switch (Herocount){
                    case 0:
                        //System.out.println(0);
                        if(isJump){
                            hero.HeroJump();
                            Herocount++;
                        }
                        break;

                    case 1:
                        //System.out.println(3);
                        if(isJump) {
                            hero.HeroDown();
                            Herocount++;
                        }
                        break;
                    case 2:
                        //System.out.println(6);
                        if(isJump) {
                            hero.setStandup();
                            Herocount++;
                            isJump = false;
                        }
                        break;

                }
                repaint();//重绘
            }
        };
        timer.schedule(timerTask, 0, 200);
    }
        //public void HeroJump(Timer timer){
    //    if (Herocount % 5 ==0 && isJump){
    //        hero.HeroJump();
    //        repaint();
    //    }
    //    if (Herocount % 10 ==0){
    //        hero.HeroDown();
    //        isJump =false;
    //        repaint();
    //        timer.cancel();
    //        hero.setStandup();
    //        repaint();
    //    }
    //
    //
    //    Herocount++;
    //}
    /**
     * 遍历HunDLObject类型的数组,并调用数组中每个对象的绘制方法
     * @param objects 被遍历的数组
     * @param g 画笔
     */
    public void paintObject(HunDLObject[] objects,Graphics g){
        //遍历数组 并调用数组中每一个元素的绘制方法
        for (int i = 0; i < objects.length; i++) {
            objects[i].paint(g);
        }
    }
    /**
     * 添加子弹
     */
    private void addBullet() {
        if(bullet_number>0){
            //给子弹数组扩容
            bullets = Arrays.copyOf(bullets, bullets.length + 1);
            //创建子弹对象
            Bullet b;
            if(hero.direction.equals("R")){
                b = new Bullet(hero.x + hero.width, hero.y+hero.height/2-30,"R");
            }else if(hero.direction.equals("L")){
                b = new Bullet(hero.x , hero.y+hero.height/2-30,"L");
            }else {
                if(hero.isSkyRight){
                    b = new Bullet(hero.x + hero.width, hero.y+hero.height/2-30,"R");
                }else{
                    b = new Bullet(hero.x , hero.y+hero.height/2-30,"L");
                }
            }
            //将子弹对象添加到数组的末尾
            bullets[bullets.length - 1] = b;
            bullet_number--;
        }

    }
    Random random = new Random();
    int count;//定时器
    /**
     * 周期性添加敌人
     */
    private void addEnemy(){
        count++;
        if(count % 50 == 0){
            if(random.nextInt(2) == 0){
                EnemyLocation = "R";
            }else {
                EnemyLocation = "L";
            }
            Enemy e = new Enemy(EnemyLocation);
            //给敌人数组扩容
            enemies = Arrays.copyOf(enemies, enemies.length + 1);
            //添加敌人对象到数组中
            enemies[enemies.length - 1] = e;
        }
    }
    /**
     * 执行
     */
    public void Start(){
        Timer timer= new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(HunDLFrame.STATE==RUN) {
                    //角色移动方法
                    move();
                    //添加敌人
                    addEnemy();
                    count = random.nextInt(30)+20;
                    checkHit();
                    removeObject();
                    isOutOf();
                }
                repaint();//重绘
            }
        };
        timer.schedule(timerTask, 0, 100);
    }
    /**
     * 绘制文本的方法
     * @param g 画笔
     */
    private void paintString(Graphics g) {
        //设置字体样式
        g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,30));
        g.setColor(new Color(0xFFFFFF));
        //绘制分数
        g.drawString("分数:"+ HunDLFrame.SCORE,750,30);
        //绘制英雄生命
        g.drawString("生命值:"+hero.lifes,20,30);
        //绘制子弹个数
        g.drawString("子弹个数:"+bullet_number,160,30);
    }
    /**
     * 管理游戏中所有角色的移动方法
     */
    private void move() {
        //遍历敌人数组  并调用数组中每个元素的移动方法
        for (int i = 0; i < enemies.length; i++) {
            Enemy enemy = enemies[i];
            enemy.move();
        }
        //子弹移动
        for (int i = 0; i < bullets.length; i++) {
            Bullet bullet = bullets[i];
            bullet.move();//子弹移动方法
        }
    }
    /**
     * 所有角色判断碰撞的方法
     * 1.检查敌人是否与英雄碰撞
     * 敌人爆炸后消失,加分
     * 英雄爆炸后消失,检查命数,如果<=0就结束游戏
     * 2.子弹与敌人碰撞
     * 子弹消失
     * 敌人,减命1 如果命<=0,敌人爆炸消失
     * 3.英雄与救援潜艇碰撞
     * 救援潜艇消失,但当前的救援潜艇有奖励属性
     * 英雄得到奖励(救援潜艇属性的奖励)
     * <p>
     * <p>
     * 0 1 2
     * 0或者 --> 死亡(碰撞后/命<=0)  (何时显示爆炸效果?当死亡了就开始显示爆炸图片,何时爆炸图片显示完毕?下标大于等于元素个数)-->删除
     */
    public void checkHit() {
        //遍历敌人的数组,用每一个敌人与英雄检查碰撞,碰撞修改他们的状态
        for (int i = 0; i < enemies.length; i++) {
            Enemy enemy = enemies[i];
            //判断当前敌人是否与英雄碰撞 并且 当前的敌人状态为活着  与英雄碰撞直接死去
            if (enemy.hit(hero) && enemy.state==HunDLObject.LIFE) {
                //判断敌人是否死亡
                enemy.state = 1;
                //碰撞英雄减少生命值
                hero.lifes--;
                //如果英雄的生命小于等于0就结束游戏
                if(hero.lifes<=0){
                    HunDLFrame.STATE= HunDLFrame.GAME_OVER;
                }
                continue;
            }
            //遍历子弹数组,用当前的这个敌人与所有的子弹检查碰撞
            for (int j = 0; j < bullets.length; j++) {
                Bullet bullet = bullets[j];
                //如果敌人j的生命大于0  并且 子弹与敌人碰撞了  .就修改它们的状态
                if (bullet.hit(enemy) && enemy.lifes>0) {
                    //子弹消失
                    bullet.state = 1;
                    //敌人减命
                    //enemy.state=ObjectSubmarine.DIE;
                    if(enemy.state==0){
                        enemy.lifes-=1;
                        //判断敌人的生命值是否足够 没生命值则死亡
                        if(enemy.lifes<=0){
                            enemy.state=1;
                        }
                    }

                }
            }

        }
    }
    /**
     * 管理所有角色的删除方法
     */
    private void removeObject() {
        Enemy[] newEnemy = new Enemy[enemies.length];//1
        int index = 0;//2
        for (int i = 0; i < enemies.length; i++) {//3
            Enemy enemy = enemies[i];//获取一个元素(对象)
            if (enemy.state != 1) {//敌人存活
                newEnemy[index] = enemy;//将存活的敌人放入新数组
                index++;
            }else{
                SCORE+=enemy.getScore();
                if(SCORE==winScore){
                    STATE = Game_Win;
                }
            }
        }
        enemies = Arrays.copyOf(newEnemy, index);//将存活的敌人重新赋值到敌人数组中

        //删除子弹状态为remove
        Bullet[] newBullet = new Bullet[bullets.length];
        index = 0;
        //遍历源数组
        for (int i = 0; i < bullets.length; i++) {
            Bullet bullet = bullets[i];
            if (bullet.state != 1) {
                newBullet[index] = bullet;
                index++;
            }else{
                try {
                    //bulletMusic();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        bullets = Arrays.copyOf(newBullet, index);
    }
    /**
     * 管理所有角色的越界方法
     */
    private void isOutOf() {
        //遍历敌人数组 并调用敌人的越界判断方法
        for (int i = 0; i < enemies.length; i++) {
            //Enemy enemy=enemies[i];
            //enemy.isOut();
            if (enemies[i].isOut()) {//判断该对象是否越界  如果越界就从改变当前对象的状态为删除
                //改变当前对象的状态
                enemies[i].state = 1;
            }
        }
        //遍历子弹数组
        for (int i = 0; i < bullets.length; i++) {
            Bullet bullet = bullets[i];
            //判断子弹是否越界,如果越界将当前子弹的状态改为remove
            if (bullet.isOut()) {
                bullet.state = 1;
            }
        }

    }
    /**
     * 添加背景音乐
     * 参考：https://blog.csdn.net/weixin_43926171/article/details/90739074
     */
    static void playMusic() {// 背景音乐播放
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("audios/bgsound.mid"));
            AudioFormat aif = ais.getFormat();
            final SourceDataLine sdl;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(aif);
            sdl.start();
            FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
            // value可以用来设置音量，从0-2.0
            double value = 0.2;
            float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
            fc.setValue(dB);
            int nByte = 0;
            int writeByte = 0;
            final int SIZE = 1024 * 64;
            byte[] buffer = new byte[SIZE];
            while (nByte != -1) {
                if(STATE != PAUSE && STATE!=Game_Win && STATE!=GAME_OVER) {
                    nByte = ais.read(buffer, 0, SIZE);
                    sdl.write(buffer, 0, nByte);
                }else {
                    nByte = ais.read(buffer, 0, 0);
                }

            }
            sdl.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 添加子弹击中音效
     */
    static void bulletMusic(){
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("audios/m_gun.wav"));
            AudioFormat aif = ais.getFormat();
            final SourceDataLine sdl;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(aif);
            sdl.start();
            FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
            // value可以用来设置音量，从0-2.0
            double value = 1.3;
            float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
            fc.setValue(dB);
            int nByte = 0;
            int writeByte = 0;
            final int SIZE = 1024 * 64;
            byte[] buffer = new byte[SIZE];

                nByte = ais.read(buffer, 0, SIZE);
                sdl.write(buffer, 0, nByte);

            sdl.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 重新开始游戏
     */
    private void reSet(){
        hero=new Hero();
        enemies=new Enemy[0];
        bullets=new Bullet[0];
        SCORE=0;
        bullet_number=50;
        STATE= RUN;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override//按键
    public void keyPressed(KeyEvent e) {
        //System.out.println(e.getKeyCode());
        switch (e.getKeyCode()) {
            case 74://发射子弹
                    addBullet();
                    hero.HeroShoot();
                break;
            case 75://按键w 或者k
                hero.setDirection("U");
                hero.JudgeDirection();
                isJump = true;
                repaint();
                break;
            case 87://跳 按键w
                hero.setDirection("U");
                hero.JudgeDirection();
                isJump = true;
                repaint();
                break;
            case 65://左 按键A
                hero.setDirection("L");
                hero.JudgeDirection();
                repaint();
                break;
            case 68://右 按键 D
                hero.setDirection("R");
                hero.JudgeDirection();
                repaint();
                break;
            case 32://空格
               if(STATE==START){
                   STATE =RUN;
               }else if(STATE==RUN){
                   STATE =2;
               }else if(STATE == PAUSE){
                   STATE = RUN;
               }else if(STATE == GAME_OVER){
                   reSet();
               }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
