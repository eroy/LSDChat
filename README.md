# Android Agilie Internship

### Project description

LSD Android chat app has the most common features like: 
- Users and friends; 
- Auth & Facebook; 
- Chat messages and all the actions like remove and edit; 
- Push messages and local data cache.  


## Project Specification

### Project Requirements

- OS: Android (min API 19)
- Device types: mobile only
- Orientation support: portrait + landscape
- localization: en

### Backend API (QuickBlox)

Backend is provided as [QuickBlox](https://quickblox.com) service. 

* QuickBlox Admin panel:
https://admin.quickblox.com

* Backend API documentation: 
http://quickblox.com/developers/Overview

* Error codes and rate limits API documentation:
http://quickblox.com/developers/Errors

* Authentication and Authorization API documentation:
http://quickblox.com/developers/Authentication_and_Authorization

* Users API documentation:
http://quickblox.com/developers/Users

* Chat API documentation:
http://quickblox.com/developers/Chat

* Content API documentation:
http://quickblox.com/developers/Content

* Push notifications API documentation:
http://quickblox.com/developers/Messages

### Functional requirements

If you’re interested on getting the requirements to the project, feel free to send an email to info@agilie.com

### Design requirements

Application’s UI should be implemented based on Material Design principles. Next points should be taken into consideration during app’s design implementation:
* use Material Theme
* use native Android SDK’s UI widgets
* ripple effect should be implemented for all clickable elements (rows, buttons, clickable texts etc) for Android 5.0+
selected/unselected/enabled/disabled states should be implemented for all clickable elements for Android with version lower than 5.0
* use animations for changing UI elements’ properties (i. e. show/hide, move). Switching between screens should be also animated.
* User should be notified in case of background processes (network, db operations). Use loading indicators. Do not block UI with loading elements, User should always have possibility to navigate the app 
* User should be notified in case of any kind of errors occur
* Use Crouton and Toast UI Widgets

## About project

### Architecture specs

Here we've used [MVP](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter) pattern, which allows us to separate view elements from the logic and retrieving data.
In _View_ we contain only View elements, lists, specific Android parts which need to be displayed to user. Also we have reference on Presenter class.
In _Model_ we've implemented retrieving of data with specific methods which needs Context for perform.
In _Presenter_ we contain references on Model, View and UseCase classes. Here we implement all the logic that is required for concrete View.

Api:
In this layer we've implemented retrieving data from net, database, cache or another source. Also, in this layer we've implemented choosing of source. For example if we have no cached data, we fetch one from network.

### Used Technologies

* Retrofit 2
https://github.com/square/retrofit
* Okhttp
https://github.com/square/okhttp
* RxAndroid
https://github.com/ReactiveX/RxAndroid
* Realm database
https://github.com/realm/realm-java
* Mockito
https://github.com/mockito/mockito
* Powermock
https://github.com/powermock/powermock





### License

[Apache-2.0](https://github.com/eroy/LSDChat/blob/master/LICENSE.txt)
