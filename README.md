# M7016E_Project

The service is built as a part of [M7016E] course . The project architecture .  

![](https://raw.github.com/haidarchikh/M7016E_Project/master/resources/Architecture.png)


This service is built to get out of Openhab environment restrictions, as a higher layer where we the service is able to interact with multiple Openhab instances . The service's goal is to analyze the data generated over a period of time and actuate according to it. This make it possible to observe behaviors.

The service is built with loosely coupled component in mind, making it easier to add new features. There are three core components give the service flexibility to easily expand.  
* OpenHABClient    : pull and push values to/from OpenHAB instance.
* MongoDB	   : get data from MongoDB database.
* PushbulletClient : send notifications to the user. 

##Features

#### Pull/Push values to/from Openhab
Using Openhab REST API we can push or pull a value to Openhab. The feature allows us to control the home environment, turn on/off objects , get the current object's value . 
* **pull a value** 

```java
OpenhabClient openHab = new OpenhabClient(OPENHAB_IP, OPENHAB_PORT);
openHab.pullItemValue(METER_SWITCH);

```

* **push a value** 
```java
OpenhabClient openHab = new OpenhabClient(OPENHAB_IP, OPENHAB_PORT);
openHab.pushItemValue(METER_SWITCH , OFF);
```
#### Get weather status
The service uses OpenWeatherMap API to get the weather status.  
**Example:**
```java
OpenWeatherMap weather = new OpenWeatherMap();
weather.getWeather();
```
#### Send notifications
Pushbullet is an app syncs phone and browser notification , Using its API we send notifications to the user.  
**Example:**
```java
PushbulletClient sendBullet = new PushbulletClient();
sendBullet.sendNote(String noteTitle , String noteCotent);
```
#### Calculate activities time
To support the service aim ( i.e. analysis data over a period of time ) the service has findActivityTime function, which calculate how long a sensor's value was ON “true” within a given date.  
**Example:**
```java
MongoDB db = new MongoDB(String hostAddress,int hostPort , String database , String collection);
database.connect();
ActivitiesTime getTime = new ActivitiesTime();
long sleep = getTime.findActivityTime(startTime, endTime, MATTRESS_SENSOR);
```
#### Log activity  
give the user a weekly log of his activities at home , currently  monitored activities are (sleep , presence at home , moving at home ,watching  TV , cooking).  
**Example:**
```java
MongoDB db = new MongoDB(String hostAddress,int hostPort , String database , String collection);
database.connect();
LogActivity log = new LogActivity(startTime , endTime);
log.getLog();
log.sendLog();
```
#### Suggest an activity   
if the weather is good and the user is at home it will suggest to him to go out.  
**Example:**
```java
MongoDB db = new MongoDB(String hostAddress,int hostPort , String database , String collection);
database.connect();
SuggestActivity activity = new SuggestActivity();
activity.suggestWeather();
```

##How to install  
1- Using terminal, clone the project into an arbitrary directory or just download the project as a zip file.
``` Shell
git clone https://github.com/haidarchikh/M7016E_Project
```
2- Step into M7016E_Project folder

3- Build the project
``` shell
gradle build
```
You will get
* Executable jar in <arbitrary_directory>/M7016E_Project/build/libs
* Eclipse project that you can import (import existing project into workspace)
