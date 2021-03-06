package com.galactic.util

import java.util.Properties

/**
 * @author HAYOONG
 */
object PropertyReader {
  
  val scpPropFile = "configuration.properties"
  val appPropFile = "application.properties"
  
  def propRead(fileName:String):Properties = {
    val prop = new Properties()
    try{
      var input = this.getClass().getResourceAsStream(fileName)
      if(input == null){
        input = this.getClass().getClassLoader().getResourceAsStream(fileName)
      }
      prop.load(input)
      input.close()
    }catch{
      case e:Exception => {
        Log.errorLog("Missing configuration file >> " + fileName)
        System.exit(-1)        
      }
    }
    prop
  }
  
  private val propConfig = {
    propRead(scpPropFile)
  }
  
  private val propApp = {
    propRead(appPropFile)
  }
  
  private def getProperty(key:String):String = {
    val value= propConfig.getProperty(key)
    if(value == null || value.isEmpty()){
      Log.errorLog("Please check configuration file, prop:" + key + "is not found")
      System.exit(-1)
    }
    return value
  }
  
  def getServerIp() = getProperty("server.ip")
  def getConfigIndex():Int = {
    
    val index = try{
      Integer.parseInt(propConfig.getProperty("config.index"),10)
    }catch {
      case e:Exception => -1
    }
    
    if (index < 0 ){
      Log.errorLog("Config index must be non negative")
      System.exit(-1)
      -1
    }else{
      index
    }
  }
  def getServerPath(index:Int) = getProperty("server.path."+index)
  def getClientPath(index:Int) = getProperty("client.path."+index)
  def getConnectUserName() = getProperty("connect.username")
  def getConnectPassword() = getProperty("connect.password")
  def getIsLiveReload() = {
    val isLiveReload = getProperty("config.livereload")
    if("1".equals(isLiveReload)) {
      true
    }
    else {
      false
    }
  }
  
  def getSocketPort():Int = {
    val port = 
      try{
       val value = propApp.getProperty("socket.port","1025")
       val intVal = Integer.parseInt(value, 10)
       if(intVal > 1024 && intVal < 64435){
         intVal
       }else{
         1025
       }
      }catch{
        case e:NumberFormatException => Log.errorLog("Invalid port provided, default value");1025
      }
    
    return port;
  }
  
  def getJschConfiguration:Properties = {
    val config = new Properties()
    config.put("StrictHostKeyChecking","no")
    config
  }
}