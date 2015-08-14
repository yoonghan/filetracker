package com.galactic.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author HAYOONG
 */
object Log {
  val log = LoggerFactory.getLogger(this.getClass)
  
  val logEnabled:Boolean = {
    val on = System.getProperty("log")
    
    if("1" == on){
      println("Log is turned ON")
      true
    } else
      false
  }
  
  def infoLog(message:String){
    if(logEnabled){
      log.info(message)
    }
  }
  
  def errorLog(message:String){
    if(logEnabled){
      log.error(message)
    }
  }
  
  def errorThrowableLog(message:Throwable){
    if(logEnabled){
      log.error("Unknown exception:", message)
    }
  }
  
  def warnLog(message:String){
    if(logEnabled){
      log.warn(message)
    }
  }
}