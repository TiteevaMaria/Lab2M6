package Lab2M;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.awt.*;
import java.util.HashMap;

@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<String>
{

    private String name;
    private HashMap<String, Point> players = new HashMap<>();
    private ChannelHandlerContext chc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception
    {
        String[] splited = s.split(",");
        for (int i = 0; i < splited.length - 1; i += 3)
        {
            String n = splited[i];
            Point pl = new Point();
            pl.x = Integer.parseInt(splited[1 + i]);
            pl.y = Integer.parseInt(splited[2 + i]);
            players.put(n, pl);
        }
    }

    public Point getPlayer() {
        return players.get(name);
    }

    public HashMap<String, Point> getPlayers() {
        return players;
    }

    public void sendMessage(String s) {
        chc.writeAndFlush(s);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        this.chc = ctx;
    }

}
