
package com.galactic

import com.galactic.util._
import java.io._
import com.galactic.util.{Scheduler}
import io.netty.channel.group.DefaultChannelGroup
import com.github.alexvictoor.livereload.Broadcaster
import io.netty.util.concurrent.GlobalEventExecutor

/**
 * @author HAYOONG
 */
object Main extends App {
  
  main();
   
  def main(){
    
    
    val broadcaster = getLiveReloadBroadcast()
    
    val max = PropertyReader.getConfigIndex()
    
    for(index <- 0 until max){
      val m = new Scheduler(index, broadcaster)
      m.start()
      if(index == 0)
        m.trackMemory()
    }
    
    //App will forever not die
    Application.createSingleStart()
    
  }  
  
  private def getLiveReloadBroadcast():Option[Broadcaster] = {
     if(PropertyReader.getIsLiveReload()) {
      Log.infoLog("Live reload activated")
      val allChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
      val broadcast  = new Broadcaster(allChannels);
      LiveReloadServer.startServer(allChannels) 
      Option(broadcast)
    }else{
      Option.empty
    }
  }
}
