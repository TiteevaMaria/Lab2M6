package Lab2M;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private ClientHandler clientHandler = new ClientHandler();
    private Maze maze = new Maze();

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    public void run() {
        Random random = new Random();
        clientHandler.setName(String.valueOf(random.nextInt(1000)));
        ExecutorService executor = Executors.newFixedThreadPool(10);
        executor.execute(() -> {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(new NioEventLoopGroup());
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel channel) throws Exception {
                    channel.pipeline().addLast(
                            new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("\n", StandardCharsets.UTF_8)),
                            new StringDecoder(StandardCharsets.UTF_8),
                            new StringEncoder(StandardCharsets.UTF_8),
                            clientHandler
                    );
                }
            });

            ChannelFuture f = null;
            try {
                f = bootstrap.connect("localhost", 3000).sync();
                clientHandler.sendMessage(clientHandler.getName() + ",1,1\n");
                Thread.sleep(1000);
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        executor.execute(() -> {
            maze.setPlayers(clientHandler.getPlayers());
            JFrame window = new JFrame("Maze");
            window.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {
                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    Point player = clientHandler.getPlayer();
                    int X = player.x;
                    int Y = player.y;
                    if (keyEvent.getKeyCode() == 37) {
                        if (!maze.isWall(X , Y- 1)) {
                            Y--;
                            clientHandler.sendMessage(clientHandler.getName() + "," + X + "," + Y + "\n");
                        }
                    }
                    if (keyEvent.getKeyCode() == 38) {
                        if (!maze.isWall(X - 1, Y)) {
                            X--;
                            clientHandler.sendMessage(clientHandler.getName() + "," + X + "," + Y + "\n");
                        }
                    }
                    if (keyEvent.getKeyCode() == 39) {
                        if (!maze.isWall(X, Y + 1)) {
                            Y++;
                            clientHandler.sendMessage(clientHandler.getName() + "," + X + "," + Y + "\n");
                        }
                    }
                    if (keyEvent.getKeyCode() == 40) {
                        if (!maze.isWall(X + 1, Y)) {
                            X++;
                            clientHandler.sendMessage(clientHandler.getName() + "," + X + "," + Y + "\n");
                        }
                    }
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {

                }
            });

            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setLayout(new BorderLayout());
            window.add(maze, BorderLayout.CENTER);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);

            executor.execute(() -> {
                while (true) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    maze.setPlayers(clientHandler.getPlayers());
                    window.repaint();
                }
            });
        });
    }
}
