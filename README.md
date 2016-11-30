# EduTrackerApplication

Current short-term goals for this branch:
* Write code for ChooseGroupActivity. Structure of the code as I see it now: our main activity is ScheduleActivity. It starts as soon as an app starts.
If the schedule of periods is not saved on the device, it immidiately starts ChooseGroupActivity. ChooseGroupActivity has 2 main views: Spinner to choose a
group and Button "Ok". We are working only with ITMO university now. As soon as ChooseGroupActivity starts, it starts a Loader, to get JSON file from ITMO api.
While the loader is working, ProgressBar is displayed. Then we parse JSON file and get names of all the groups. They array of names is transferred to Adapter for
Spinner. When it is done, the Spinner is made Visible, the user is able to choose his group and press the button to return to ScheduleActivity and see his schedule.