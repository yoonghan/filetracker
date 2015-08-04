package com.galactic

import com.galactic.util.PropertyReader
import java.net.ServerSocket
import akka.actor._
import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import akka.util.Timeout

/**
 * @author HAYOONG
 */
object Application {
  
//  private val system = ActorSystem("SingleStartUp")
//  private val singleAppCall: ActorRef = system.actorOf(Props[SingleStartUp], "SingleStartUp")
//  
//  implicit val timeout = Timeout(24.hours)
//  
  def createSingleStart(){
    try{
      val port = PropertyReader.getSocketPort();
      val serverSocket = new ServerSocket(port);
      println("Connected to port:"+port)
      println("*"*30)
      println("To be killed with Ctrl-C only.")
      //singleAppCall.ask(serverSocket)
      while(true){
        val clientSocket = serverSocket.accept();
        clientSocket.close()
      }
    }catch{
      case e:Exception => {
        System.err.println("Similar service has been started. Exiting.");
        System.exit(-1)
      }
    } 
  }
}

class SingleStartUp extends Actor{
  def receive() = {
    case s:ServerSocket =>{
      val clientSocket = s.accept();
    }
  }
}