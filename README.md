#File Tracker

##TOOLS NEEDED
***
Make sure java 7 or above is installed. JRE version will be just fine.

##INSTRUCTIONS
1. Copy file filetracker folder/ to you desktop
2. Configure conf/configuration.properties to point the correct folders you need to monitor.
3. Double-click or run executor.bat


##CONFIGURATION
Configuration as of table below:

Property           | Description |
--- | --- |
server.ip          | VM box to be connected to |
connect.username   | VM box username, default is root for everyone's VM |
connect.password   | VM box password, default is root for everyone's VM |
config.index       | To determine how many [server.path] and [client.path] to be monitored. Index of [*.path.*] starts from 0 |
server.path.[index]| Once client file changes it detects, it copies to the server |
client.path.[index]| Folder to be monitored for new files and modified files |
livereload| Enable live reload, however pages must contain the javascript 

##Markup for live reload
```document.write('<script src="http://' + ('localhost').split(':')[0] + ':35729/livereload.js?snipver=1"></' + 'script>')```

##TROUBLESHOOT
1. Application normally end with world - i.e. just dies
2. Common errors includes :

Error | Solution |
--- | --- |
"Similar service has been started. Exiting." | Close the other application that had been running. |
"Server Port has been used" | Modify conf/application.properties from port that is not being used. |
"Major Minor version..." | Make sure you have JAVA 7 or above installed. |
"11231283910238........" | If a long list of number is appearing, your folder is incorrect. |
