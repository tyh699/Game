package com.tyh.ui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

//游戏主窗口界面
public class GameJFrame extends JFrame  implements KeyListener, ActionListener {

    //创建一个二维数组
    //目的：用来管理数据
    //加载图片的时候会根据二维数组中的数据进行加载
    int[][] data = new int[4][4];

    //记录空白方块在二维数组中的位置(横为y，竖为x)
    int x = 0;
    int y = 0;
    String path = "image\\animal\\animal3\\";

    //创建二维数组记录正确顺序
    int win[][] = new int[][]{
            {1,2,3,4},
            {5,6,7,8},
            {9,10,11,12},
            {13,14,15,0}
    };

    //设置计步器
    int step = 0;

    //创建选项下面的条目对象
    JMenuItem replayItem = new JMenuItem("重新游戏");
    JMenuItem reLoginItem = new JMenuItem("重新登录");
    JMenuItem closeItem = new JMenuItem("关闭游戏");

    public GameJFrame() {
        //初始化界面
        initJFrame();
        //初始化菜单
        initJmenuBar();
        //初始化数据
        initData();
        //初始化图片
        initImage();

        //设置界面打开

        this.setVisible(true);
    }

    private void initData() {
        //定义一个一维数组
        int[] tempArr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        //打乱数组中的数据的顺序
        //遍历数组，得到每一个元素，跟随机索引的数据交换
        Random random = new Random();
        for (int i = 0; i < tempArr.length; i++) {
            //获取到随机索引
            int index = random.nextInt(tempArr.length);
            //交换
            int temp = tempArr[i];
            tempArr[i] = tempArr[index];
            tempArr[index] = temp;
        }
        //给二维数组添加数据
        //遍历一维数组的每一个元素，添加到二维数组
        for (int i = 0; i < tempArr.length; i++) {
            if(tempArr[i] == 0)
            {
                x = i/4;
                y = i%4;
            }
            data[i/4][i%4] = tempArr[i];
        }
    }

    private void initImage() {

        //清除原本已经出现的所有图片
        this.getContentPane().removeAll();

        //细节：先加载的图片在最上面，后加载的图片在下面

        if (victory()) {
            //显示胜利图标
            JLabel winJLabel = new JLabel(new ImageIcon("image\\win.png"));
            winJLabel.setBounds(203,283,197,73);
            this.getContentPane().add(winJLabel);
        }

        JLabel stepCount = new JLabel("步数："+step);
        stepCount.setBounds(50,30,100,20);
        this.getContentPane().add(stepCount);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int number = data[i][j];
                ImageIcon icon = new ImageIcon(path + number + ".jpg");
                //创建一个JLabel的对象（管理容器）
                JLabel jLabel = new JLabel(icon);
                //指定图片位置
                jLabel.setBounds(105 * j+83, 105 * i+134, 105, 105);
                //给图片添加边框
                jLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
                //把管理容器添加到界面中
                this.getContentPane().add(jLabel);
            }
        }

        //添加背景图片
        ImageIcon bg = new ImageIcon("image\\background.png");
        JLabel background = new JLabel(bg);
        //设置背景图片
        background.setBounds(40,40,508,560);
        //把背景图片添加到窗口中
        this.getContentPane().add(background);

        //刷新一下界面
        this.getContentPane().repaint();

    }

    private void initJmenuBar() {
        //创建整个菜单对象
        JMenuBar jMenuBar = new JMenuBar();
        //创建菜单上面选项的对象
        JMenu functionJMenu = new JMenu("功能");


        //将条目添加到选项当中
        functionJMenu.add(replayItem);
        functionJMenu.add(reLoginItem);
        functionJMenu.add(closeItem);

        replayItem.addActionListener(this);
        reLoginItem.addActionListener(this);
        closeItem.addActionListener(this);

        //将选项添加到菜单当中
        jMenuBar.add(functionJMenu);
        //给整个界面设置菜单
        this.setJMenuBar(jMenuBar);
    }

    private void initJFrame() {
        //设置标题
        this.setTitle("拼图小游戏");
        //设置窗口大小
        this.setSize(603, 680);
        //设置窗口居中
        this.setLocationRelativeTo(null);
        //设置关闭模式:关闭一个界面则结束
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //设置默认布局
        this.setLayout(null);
        //给整个界面添加键盘监听事件
        this.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == 65)
        {
            //把页面中照片清空
            this.getContentPane().removeAll();
            //加载完整照片
            JLabel all = new JLabel(new ImageIcon(path+"all.jpg"));
            all.setBounds(83,134,420,420);
            this.getContentPane().add(all);
            //添加背景图片
            ImageIcon bg = new ImageIcon("image\\background.png");
            JLabel background = new JLabel(bg);
            //设置背景图片
            background.setBounds(40,40,508,560);
            //把背景图片添加到窗口中
            this.getContentPane().add(background);

            //刷新一下界面
            this.getContentPane().repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        //判断游戏是否胜利，如果胜利，此方法需要直接结束，不能再执行下面移动的代码
        if (victory()) {
            return;
        }

        //左：37 上：38 右：39 下：40
        int code = e.getKeyCode();
        if(code == 37)//左
        {
            //将空白方格右边的向左移动
            //x,y代表空白方格
            //x,y+1代表空白方格右边的方格
            if(y == 3)
            {
                ;//此时空白方格已经在最右边，不能再向左否则会数组越界
            }
            else
            {
                //将空白方格右边的方格赋值给空白方格
                data[x][y] = data[x][y+1];
                data[x][y+1] = 0;
                y++;
                step++;
            }

            //调用方法按照最新数据加载照片
            initImage();

        }
        else if(code == 38)//上
        {
            //将空白方格下边的方格向上移动
            //x,y代表空白方格
            //x+1,y代表空白方格下面的方格
            if(x == 3)
            {
                ;//此时空白方格已经在最下边，不能再向上否则会数组越界
            }
            else
            {
                //将空白方格下边的方格赋值给空白方格
                data[x][y] = data[x+1][y];
                data[x+1][y] = 0;
                x++;
                step++;
            }

            //调用方法按照最新数据加载照片
            initImage();
        }
        else if(code == 39)//右
        {
            //将空白方格左边的向右移动
            //x,y代表空白方格
            //x,y-1代表空白方格左边的方格
            if(y == 0)
            {
                ;//此时空白方格已经在最左边，不能再向左否则会数组越界
            }
            else
            {
                //将空白方格左边的方格赋值给空白方格
                data[x][y] = data[x][y-1];
                data[x][y-1] = 0;
                y--;
                step++;
            }

            //调用方法按照最新数据加载照片
            initImage();
        }
        else if(code == 40)//下
        {
            //将空白方格上边的向下移动
            //x,y代表空白方格
            //x-1,y代表空白方格右边的方格
            if(x == 0)
            {
                ;//此时空白方格已经在最上边，不能再向下否则会数组越界
            }
            else
            {
                //将空白方格上边的方格赋值给空白方格
                data[x][y] = data[x-1][y];
                data[x-1][y] = 0;
                x--;
                step++;
            }

            //调用方法按照最新数据加载照片
            initImage();
        }
        else if(code == 65)
        {
            //调用方法按照最新数据加载照片
            initImage();
        }
        else if(code == 87)
        {
            data = new int[][]{
                    {1,2,3,4},
                    {5,6,7,8},
                    {9,10,11,12},
                    {13,14,15,0}
            };
            x = 3;
            y = 3;
            initImage();
        }
    }

    public boolean victory(){
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                if( data[i][j] != win[i][j] )
                {
                    //只要有一个数据不一样则返回false
                    return false;
                }
            }
        }
        //循环结束表示遍历完毕，全部一样返回true
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if(obj == replayItem)
        {
            step = 0;
            initData();
            initImage();
        }
        else if(obj == reLoginItem)
        {
            this.setVisible(false);
            new LoginJFrame();
        }
        else if(obj == closeItem)
        {
            System.exit(0);
        }

    }
}
