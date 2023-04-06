# Weather_Station_App

## About mobile and server application

This mobile application enables the monitoring of a distributed system that measures environmental factors such as soil moisture, air temperature and insolation level. The application allows users to define their own areas and view the readings of the mentioned factors within the defined areas. The application is dedicated to the Android operating system and was developed using Java and XML, while the server application was created using PHP. Server use hosting AwardSpace.

## Mobile Application - Introduction

https://user-images.githubusercontent.com/70532979/230366676-58ff6915-b08c-4c65-91a0-2c3fb638867a.mp4

## System Block Diagram - Description
<p align="center">
![Structure project](https://user-images.githubusercontent.com/70532979/227450117-b2d947b0-8ba6-4156-8011-e4fc40d6a6b5.png)
</p>

<b>Sensors</b> - Responsible for measuring environmental factors and sending them to a server. <br>
<b>Server</b> - Resposible for storing data about users and measured environmental factors. <br>
<b>Mobile application</b> - Resposible for authentication user, enabling defined area by the user, visualization measurements factors on charts and lists. <br>

## Entity Relationship Diagram (ERD)

![schemat - baza danych ](https://user-images.githubusercontent.com/70532979/227714144-cc9808d6-54c3-42f6-a6d8-72f22fcc7564.png)

Table <b>users</b> - Storing data about registrated users. <br>
Table <b>sensors</b> - Storing data about measured environmental factors, measurement location and date.<br>
Table <b>pins</b> - Storing data about pins that define regions. <br>
Table <b>regions</b> - Storing data about regions and their owners. <br>


