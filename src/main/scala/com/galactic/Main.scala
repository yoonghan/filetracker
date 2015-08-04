
package com.galactic

import com.galactic.util._
import java.io._
import com.galactic.util.{Scheduler}

/**
 * @author HAYOONG
 */
object Main extends App {
  
  main();
   
  def main(){
    
    val max = PropertyReader.getConfigIndex()
    
    for(index <- 0 until max){
      val m = new Scheduler(index)
      m.start()
      if(index == 0)
        m.trackMemory()
    }
    
    //App will forever not die
    Application.createSingleStart();
    
  }  
}
