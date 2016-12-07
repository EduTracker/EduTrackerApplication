# EduTrackerApplication

Our goal is to implement an application, which tracks the attendance of a student from ITMO University. The student enters its group name, the schedule 
for entered group is downloaded and parsed from ITMO website. Then during every period in schedule an application checks the geolocation of the smartphone and
compares it with the ITMO University address. Then the student can see his charts of attendance. We hope, that this will motivate a student to attend all the classes.

The application is a semester project for Android Applications Development course in ITMO University made by Vitalii Karavaev, Aleksandr Tukallo, Mekhrubon Turaev.

For drawing charts in an app we are using [**MPAndroidChart**](https://github.com/PhilJay/MPAndroidChart) library.

Current progress:
* ChooseGroupActivity is implemented. The schedule is being successfully downloaded from ITMO website, parsed and saved to a file.

Short-term goals:
* Design ScheduleActivity. Place a scrollview\recyclerview with a schedule there.
* Design and implement StatisticsActivity (partially already done). 
* Implement a method which will check the geolocation of the smarthpone during every period and compare it to the address mentioned in schedule.
