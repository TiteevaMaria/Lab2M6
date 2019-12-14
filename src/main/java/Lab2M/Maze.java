package Lab2M;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Maze extends JPanel
{
    private int[][] mazeTab;
    private Dimension dimension;
    private List<Shape> shapes;
    private ArrayList<Point> players = new ArrayList<>();
    private int N = 30;
    private Color[] color = new Color[4];

    Maze()
    {
        Random rand = new Random();
        color[0] = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
        color[1] = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
        color[2] = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
        color[3] = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
        mazeTab = new int[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 1, 0, 1, 1, 1},
                {1, 0, 1, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 0, 1, 1, 0, 1, 0, 1},
                {1, 0, 0, 0, 0, 1, 0, 1, 1, 1},
                {1, 1, 1, 1, 0, 1, 0, 0, 0, 1},
                {1, 0, 1, 1, 0, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };
        dimension = new Dimension();
        dimension.setSize(10 * N + 1, 10 * N + 1);
        shapes = new ArrayList<Shape>();
        for (int y = 0; y < 10; ++y)
        {
            for (int x = 0; x < 10; ++x)
            {
                if (!isWall(x, y))
                {
                    shapes.add(new Rectangle(y * N-10, x * N-10, N+10, N+10));
                }
            }
        }
    }

    public static void main(String[] args)
    {
        Maze maze = new Maze();
        JFrame window = new JFrame("Maze Example");
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(maze, BorderLayout.CENTER);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public ArrayList<Point> getPlayers() {
        return players;
    }

    public void setPlayers(HashMap<String, Point> players)
    {
        this.players.clear();
        for (String s : players.keySet())
        {
            this.players.add(players.get(s));
        }

    }

    Boolean isWall(int x, int y)
    {
        return mazeTab[x][y] == 1;
    }

    @Override
    public Dimension getPreferredSize() {
        return dimension;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (Shape s : shapes)
        {
            g2d.draw(s);
            g2d.fill(s);
        }
        int i = 0;
        for (Point player : players)
        {
            g2d.setColor(color[i]);
            i++;
            int x = player.x;
            int y = player.y;
            g2d.fillRect(y * N, x * N, N-10, N-10);
        }
    }
}
