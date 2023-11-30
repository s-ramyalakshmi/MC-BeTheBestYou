CSE 535: Mobile Computing
Project 4

Name: Pallav Devang Raval
Email : praval2@asu.edu
ASU ID: 1222559364
GitHub Repo: https://github.com/s-ramyalakshmi/MC-BeTheBestYou/tree/pallav_raval

1.	Alignment with guardian angel:

The guardian angel of our project 5 is a context driven overall health driven application. It is a one stop solution for all mobile based physical and mental health monitoring data. My individual contribution (project 4) fits into the larger part of project 5 in a big way, project 4 basically handles the location context data of project 5. Our application will be used by a large number of users and all the users will not have the same context or location between each other. Project 4 basically uses the location data to detect and report nearby pharmacies for that particular user. Once I am able to detect the nearby pharmacies, I proceed to integrate this solution to the guardian angel. The guardian angel will activate this code if there is any anomaly detected in the user’s vitals.

2.	Specifications:

These are the AADL descriptions that I would provide for my project 4 :

system implementation Sensor.GPS
subcomponents
latitude: process Algorithm.axis_1;
longitude: process Algorithm.axis_2;
Comm: system Communication.Sensor_1;
connections
AlgOutput1: data port latitude.Output ->
OutputData;
AlgOutput2: data port longitude.Output ->
OutputData;
Com: data port OutputData ->
Comm.Send_Data;
Destin: data access SendAddress.neighbor ->
Comm.Destination_ID;
properties
SensorParameter::SamplingFrequency => 10s;
SensorParameter::Sensitivity => 1 minute;
end Sensor.GPS;
 
The Above specifications basically detect the location of the phone, and it can be implemented in java using the below code:

LocationRequest locationRequest = LocationRequest.create();
locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
locationRequest.setInterval(10000); // 10 seconds

FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
    @Override
    public void onLocationResult(LocationResult locationResult) {
        if (locationResult == null) {
            return;
        }
        for (Location location : locationResult.getLocations()) {
            // Get the location here
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            // Use the latitude and longitude
        }
    }
}, Looper.getMainLooper());

The above code is a boiler plate code to detect the location of the phone.







3.	Design:

Tech stack:
Android studio
Emulator
SDK 26 and above
Location permission
Android phone to test
Google Maps API access
Google Places API access


Architecture diagram:








                                                                                                                                                                                              







4.	Testing strategies:

My solution to project 4 is basically used to detect pharmacies within a 10-mile radius of the user’s location. To test if the code works, I first tested it out using an in-built emulator provided by Android Studio software, it worked successfully. I then proceeded to test the software on my Pixel 6A android phone, where it worked as intended. Sind Android has a variety of version numbers. I tested it out in multiple android phones like OnePlus 5 which has lower android version, I also tested it out on Pixel 7 pro, which has a higher version number, the app worked successfully in both of these versions. Since the app is limited to finding pharmacies within a 10-mile radius I tested the phone once at my residence and once from Hayden library, and the output pharmacies were different for these locations, so yes, the code was working as expected.

5.	Navigating challenges:

I encountered multiple challenges, while doing this project, I faced several coding challenges in android studio, having started development on android at the beginning of this course, there were many things that I had to learn in the software. The biggest challenge that I faced was related to integrating the location access to the application. I had to handle a lot of cases for example, location access was not granted to the app, location access was granted but the location was not on, etc. All these cases raise different errors in android studio if they are not handled properly so I had to take care of the error handling carefully.  The learning I got this from experience was having an adaptive strategy towards error handling, I applied Try and catch blocks to detect error and this was my biggest understanding from it.
