package com.galactic.util

import akka.actor.ActorSystem
import scala.concurrent.duration._
import com.galactic.bean._
import java.io._
import com.galactic.util._

import scala.concurrent.ExecutionContext.Implicits.global
/**
 * @author HAYOONG
 */
class Scheduler(index:Int) {
  
  //set all the time
  val system = ActorSystem("Scheduler")
  val init = 5.seconds
  
  def readFiles():Array[FileProp] = {
    val fileReader = new FileReader(PropertyReader.getClientPath(index))
    val filesToMonitor = fileReader.listOfFiles
    return filesToMonitor
  }
  
  def start(){
    system.scheduler.scheduleOnce(init){monitorChanges(readFiles())}
  }
  
  def trackMemory(){
    val runtime = Runtime.getRuntime();
    system.scheduler.schedule(0.seconds, 5.minutes){println("Free memory with: " + runtime.freeMemory()/1024/1024 +"MB")}
  }
  
  private def monitorChanges(filesToMonitor:Array[FileProp]){
    val listOfFiles = checkChanges(filesToMonitor)
    
    if(! listOfFiles._1.isEmpty){
      println("Changes found to:[" + listOfFiles._1.mkString(",") + "]")
      ScpFileTransfer.scp(listOfFiles._1, index)
    }
    
    system.scheduler.scheduleOnce(init){monitorChanges(listOfFiles._2)}
  }
  
  def checkChanges(filesToMonitor:Array[FileProp]):Tuple2[Array[FileProp], Array[FileProp]] = {
    
    val newListedFile = readFiles()
    
    //handles files that have been added and modified. Removed are just ignored.
    val newListing = newListedFile.filter( file =>  { 
      val findFile = filesToMonitor.filter(p => p.fileName == file.fileName)
      if(findFile.isEmpty){
        true
      }else{
        findFile(0).dateModified != file.dateModified 
      }
    })
    
    return (newListing, newListedFile);
  }
}