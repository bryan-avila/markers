# What is this? 
This is an Android application that utilizes the Room library to store data locally. 

This app also utilizes the Google Maps API in order to display locations at CSUN that a user may wish to save.

Saved locations, or "spots", are then displayed on the main screen, where a user may choose to acquire directions or delete them from the database. 


# The Set Up 
1) Before you begin, it is __required__ that you obtain a Maps API key. Create a new Google Cloud Project, which is linked [here](https://developers.google.com/maps/documentation/android-sdk/cloud-setup).

    * With your new project created, go to "API & Services" and press the button to enable them. 

    * Next, select Maps SDK for Android and ensure it is enabled.

    * Finally, return to "API & Services" and select "Credentials". Your API key should be found there.

2) Next, find `local.properties` within this app project.
   * Create a variable `MAPS_API_KEY`.
   * Set `MAPS_API_KEY` equal to your API key.
   
# Credits 
The Room portion of this code, written by Florina Mutenescu, can be found [here](https://developer.android.com/codelabs/android-room-with-a-view-kotlin#0). For a more in-depth explanation of Room databases and how to connect them to your UI, please visit that code lab! 

All original image locations may be found under `Links.txt`.

__NOTE:__ This app was created with the intention of learning more about Android Studio, Android Development, Room Library, and Maps API. This is not an officially deployed app, merely a showcase of these tools.




