package docker.demo_mvc;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// 模型（被观察者）
class PercentageModel {
    private List<PercentageObserver> observers = new ArrayList<>();
    private int part1;
    private int part2;

    public void addObserver(PercentageObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(PercentageObserver observer) {
        observers.remove(observer);
    }

    public void setParts(int part1, int part2) {
        if (part1 + part2 == 100) {
            this.part1 = part1;
            this.part2 = part2;
            notifyObservers();
        } else {
            throw new IllegalArgumentException("两部分之和必须等于100！");
        }
    }

    public int getPart1() {
        return part1;
    }

    public int getPart2() {
        return part2;
    }

    private void notifyObservers() {
        for (PercentageObserver observer : observers) {
            observer.update(part1, part2);
        }
    }
}

// 观察者接口
interface PercentageObserver {
    void update(int part1, int part2);
}

// 视图（观察者）- 饼状图
class PieChartView extends JPanel implements PercentageObserver {
    private int part1 = 50;
    private int part2 = 50;

    public PieChartView() {
        this.setPreferredSize(new Dimension(400, 400));
    }

    @Override
    public void update(int part1, int part2) {
        this.part1 = part1;
        this.part2 = part2;
        repaint(); // 重绘饼状图
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // 绘制饼状图
        int angle1 = (int) (part1 * 3.6); // 每个百分比对应3.6度
        int angle2 = (int) (part2 * 3.6);

        g2d.setColor(Color.BLUE);
        g2d.fillArc(50, 50, 300, 300, 0, angle1);

        g2d.setColor(Color.ORANGE);
        g2d.fillArc(50, 50, 300, 300, angle1, angle2);
    }
}

// 控制器
class PercentageController {
    private PercentageModel model;

    public PercentageController(PercentageModel model) {
        this.model = model;
    }

    // 更新模型中的数值
    public void updateParts(int part1, int part2) {
        model.setParts(part1, part2);
    }
}

// 主程序
public class Main {
    public static void main(String[] args) {
        PercentageModel model = new PercentageModel();
        PieChartView view = new PieChartView();

        // 注册观察者
        model.addObserver(view);

        PercentageController controller = new PercentageController(model);

        // 创建一个简单的Swing界面
        JFrame frame = new JFrame("动态饼状图 - 观察者模式");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // 设置视图
        frame.add(view, BorderLayout.CENTER);

        // 控制器按钮和输入
        JPanel inputPanel = new JPanel();
        JTextField part1Field = new JTextField(5);
        JTextField part2Field = new JTextField(5);
        JButton updateButton = new JButton("更新");

        inputPanel.add(new JLabel("Part1:"));
        inputPanel.add(part1Field);
        inputPanel.add(new JLabel("Part2:"));
        inputPanel.add(part2Field);
        inputPanel.add(updateButton);

        frame.add(inputPanel, BorderLayout.SOUTH);

        // 更新按钮的动作监听
        updateButton.addActionListener(e -> {
            try {
                int part1 = Integer.parseInt(part1Field.getText());
                int part2 = Integer.parseInt(part2Field.getText());
                controller.updateParts(part1, part2);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "输入必须是整数，且两数之和等于100。");
            }
        });

        frame.pack();
        frame.setVisible(true);
    }
}
