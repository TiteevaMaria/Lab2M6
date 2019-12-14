package Lab2M;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ConcurrentHashMap;

public class ServerHandler extends SimpleChannelInboundHandler<String>
{
    private static ConcurrentHashMap<ChannelHandlerContext, Player> players = new ConcurrentHashMap<>();
    Player player = new Player();


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception
    {
        System.out.println("message " + s);
        String[] mes = s.split(",");
        player.name = mes[0];
        player.point.x = Integer.parseInt(mes[1]);
        player.point.y = Integer.parseInt(mes[2]);
        players.put(ctx, player);
        System.out.println("players " + players);

        StringBuilder stringBuilder = new StringBuilder();
        for (ChannelHandlerContext chc : players.keySet())
        {
            stringBuilder.append(players.get(chc).toString()).append(",");
        }
        stringBuilder.delete(stringBuilder.length(), stringBuilder.length());
        stringBuilder.append("\n");

        for (ChannelHandlerContext chc : players.keySet())
        {
            chc.executor().submit(() ->
            {
                chc.writeAndFlush(stringBuilder);
            });
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception
    {
        players.put(ctx, player);
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception
    {
        players.remove(ctx);
        super.channelUnregistered(ctx);
    }
}
