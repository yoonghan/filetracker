package com.galactic.util

import com.jcraft.jsch._
import com.galactic.bean._
import java.io._

/**
 * @author HAYOONG
 */
object ScpFileTransfer {
  
  val session = connectSession(PropertyReader.getServerIp(), PropertyReader.getConnectUserName(), PropertyReader.getConnectPassword());
  
  def scp(filesToCopy:Array[FileProp], index:Int) {
    if(session.isConnected() == false){
      session.disconnect()
      session.connect()
    }
    copyFile(session, filesToCopy, PropertyReader.getServerPath(index)) 
    
    //session.disconnect()//never disconnect
  }
  
  private def copyFile(session:Session, filesMonitor:Array[FileProp], dest:String) {
    
     def checkAck(in:InputStream):Int = {
        val b=in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //          -1
        if(b == 0) return b;
        if(b == -1) return b;
    
        if(b == 1 || b == 2) {
          val sb=new StringBuffer();
          
          var c:Int = -1; 
          do {
            c=in.read();
            sb.append(c);
          }
          while(c != '\n');
            
          if(b==1) { // error
            print(sb.toString());
          }
          if(b==2){ // fatal error
            print(sb.toString());
          }
        }
        return b;
      }
    
    def callBuff(file:File, out:OutputStream){
      val fis = new FileInputStream(file)
      val buf = new Array[Byte](1024)
      
      while(true){
        val len = fis.read(buf, 0, buf.length)
        if(len <= 0)
          return
        out.write(buf, 0, len)
      }
      
      fis.close()
    }
    
    try{
      for(eachFile <- filesMonitor){
        val command="scp -t " + dest + eachFile.fileName;
        val channel = session.openChannel("exec");
        channel.asInstanceOf[ChannelExec].setCommand(command)
        
        val out=channel.getOutputStream();
        val in = channel.getInputStream();
        
        channel.connect()
        
        if(checkAck(in)!=0){
          println("NO");
        }
        
        val file = new File(eachFile.fileNameWithPath)
        val filesize = file.length();
        val fileCommand = "C0644 "+filesize+" "+eachFile.fileName+"\n";
        
        out.write(fileCommand.getBytes())
        out.flush()
        
        callBuff(file, out)
        
        val lastByte = new Array[Byte](1)
        out.write(lastByte, 0, 1)
        out.flush()
        
        if(checkAck(in)!=0){
          println("NO");
        }
        
        out.close()
        
        channel.disconnect()
      }
    }catch{
      case e:Exception =>e.printStackTrace() 
    }
  }
  
  
  private def connectSession(serverIp:String, userName: String, password:String):Session = {
    val jsch = new JSch()
    
    val session = jsch.getSession(userName, serverIp, 22)
    session.setPassword(password)
    val config = PropertyReader.getJschConfiguration
    session.setConfig(config)
    try{
        println("Connection to :"+serverIp)
        session.connect
    }catch{
      case e: Exception => e.printStackTrace();
    }
    return session    
  }
}