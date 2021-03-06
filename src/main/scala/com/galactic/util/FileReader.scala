package com.galactic.util

import com.galactic.bean.FileProp
import java.io._

/**
 * @author HAYOONG
 */
class FileReader(folder:String) {
  
  val listOfFiles = readFileToMonitor(folder);
  
  private def readFileToMonitor(folder:String):Array[FileProp] = {
    val file = new File(folder)
    
    if(!file.isDirectory() || !file.exists()){
      Log.errorLog("Folder path " + folder + " does not exist/is not a folder. Please check")
      System.exit(-1)
    }
    
    val files = file.listFiles().filter { file => file.isFile() }
    if(files == null || files.length == 0){
      Log.errorLog("Folder path " + folder + " has nothing to monitor")
      System.exit(-1)
    }
    
    val result  = files
                  .map(file => FileProp(file.getName(), file.getAbsolutePath(), file.lastModified()))
    
    result
  }
}