package com.galactic.util

import org.slf4j.LoggerFactory
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.group.DefaultChannelGroup
import java.util.Scanner
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.logging.LogLevel
import io.netty.util.concurrent.GlobalEventExecutor
import com.github.alexvictoor.livereload.Broadcaster
import com.github.alexvictoor.livereload.WebSocketServerInitializer
import io.netty.channel.group.ChannelGroup
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.ActorRef
import akka.actor.Props

/**
 * @author HAYOONG
 */
object LiveReloadServer {
    
  private val bossGroup = new NioEventLoopGroup(1);
  private val workerGroup = new NioEventLoopGroup();
  private val system = ActorSystem("NettyServer")
  private val singleAppCall: ActorRef = system.actorOf(Props[NettyServer], "NettyServer")
  
  def startServer(allChannels: ChannelGroup) {
    singleAppCall.!((allChannels, bossGroup, workerGroup))
  }
  
  def shutdownServer(){
    bossGroup.shutdownGracefully()
    workerGroup.shutdownGracefully()
  }
}

class NettyServer extends Actor{
  
  val port = 35729;
  
  def receive() = {
    case (allChannels:ChannelGroup, bossGroup:NioEventLoopGroup, workerGroup:NioEventLoopGroup) =>{
      try {
        val jsContent = new com.github.alexvictoor.livereload.FileReader().readFileFromClassPath("/livereload.js")
        
        val bootStrap = new ServerBootstrap()
        bootStrap.group(bossGroup, workerGroup).channel(new NioServerSocketChannel().getClass).handler(new LoggingHandler(LogLevel.INFO))
        .childHandler(new WebSocketServerInitializer(allChannels, jsContent))
        
        val ch = bootStrap.bind(port).sync().channel()
        ch.closeFuture().sync()
        
      }
      finally {
        bossGroup.shutdownGracefully()
        workerGroup.shutdownGracefully()
        
      }
    }
  }
}