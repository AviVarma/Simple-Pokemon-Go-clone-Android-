Find the lyrics readme

Avi Varma (957552)
------------------------------------------------------------------------------------------
Project brief architecture:

- MainActivity.kt
- MapsActivity.kt
- GuessActivity.kt
	-GuessModel.kt
	-GuessAdapter.kt
- AchievementsActivity.kt
	-AchievementsModel.kt
	-AchievementsAdapter.kt
- SettingsActivity.kt
------------------------------------------------------------------------------------------
Start Up Instructions:

WARNING: On pressing the play button, you will be briefly tabbed out of the game first.
         You need to first grant access to location so that you can play the game.

1. Open the app where you will be greeted with the home page.
2. Select the game mode you wish to play by clicking on the toggle button.
3. Once you have selected youre game mode press the play button to the right of the button.
4. Move around the campus with the maps open. when a lyric is withing your displayed blue
   circle range, you can clikc on the lyric to collect it. You wil be notified when you
   have successfully collected a lyric. you can choose to collect more or continue to
   the next step.
5. I you would like to guess the song, click the options icon on the top right of the
   screen. this will take you to the guessing screen where you can see the lyrics that
   you have colleced on to in a list. Underneath the list you will be able to input your
   guess. If youre guess is correct, then the streak will increase and would will gain
   points that can be displyed by clicking the streaks button. If you get the guess wrong,
   Your streak will reset to 0 and 5 points will be deducted.
6. Once you have guessed the song correctly you can view the ong in the achievements page.
   You can access the page by clicking the options button on the top right of the guessing
   page. 
7. Here, you can see the list of songs that you have corectly guessed so you can then
   search them on google and listen to the song!
8. now you can go to the home page and collect another song!   

------------------------------------------------------------------------------------------
Home Activity:
The Homepage utilises the following files: MainActivity.kt (located in the java folder),
activity_home.xml and content_home.xml files (located in layout folder) and finally 
menu_home.xml(menu folder).

The home activity utilises a toggle button which will let the user pick between the two
modes provided: Curerent or classic. this will give you a song depending on the mode to 
hunt. From the home activity you can access the settings page through the button on the
screen or the options button on the top right of the screen. You can press the play
image button to then play the game with the game mode selected.

Find the Song (Maps Activity):
The Find the Song! page utilises the Google maps API which will the map of Bay campus.
on top of this you will be able to see 6 randomly placed google markers with a music
note icon. Your current location is represented by the blue arrow on the map with the
collection fence displayed as the blue circle around your marker. When a lyric is within
your collection radius, you can click on the lyric. You will be notified on the action
with the snackbar notification and you can now view the collected lyric on the next page.
You cannot pass to the next page until you have collected a minimum of one lyric.
in order to progress to the next page, click on the top right options button and click
on the button Guess the song!

Guess The Song (Gues Activity):
Yow will be greeted with a recycler view filled with the collected lyrics. yu can scroll
down the list to see all the collected lyrics. With these lyrics for aid, you can make a
guess on what the song or artist name is. If you get the name correct, youre streak
score which is displayd below will increae by 1. if you get youre guess wrong, then the
streak resets to 0. The streak icon can be clicked to display the amount of points you
have collected from playing the song. Each correct guess gains 5 points and each
incorrect guess removes 5 points. If you have colleced all the lyrics and still get the
song name incorrect 10 times then you will be presented with a give up button which wil
take ou back to the home screen where you can try to find another song
once you have guessed the song correctly, then you can proceed to the achievements page.

Achievements (Achievements Activity):
You can view all the songs that you have collected since downloading the app. this is a
scrollable list with songs from the current mode and the classic mode. When you click
on the song, a link will be shown which you can use to google the song and listne to it.
from this page you can press the back button to go back to the home page and start over
and find another song.
---------------------------------------------------------------------------------------
Components:

Components that have been implemented to make the game usable and improove
functionality.

Google Maps:
In order to show where the lyrics are located, they are visulaised using the Google Maps
API. This also uses your Wifi connection and GPS connection. if either are turned off,
you will be notfied to turn them back on and the game will be paused.
If wither WiFi or GPS is turned off, the current location will stop displaying and you
then cannot collect the congs lyrics. Current location on the map is also provided
by the Google Maps API which can then be used to compare the location between your
current location marker and the lyric marker to see if you are in range of picking up
the lyric. The in range circle is also provided by the Google maps API which is the
same ass the web version of google maps.

Recycler Views:
To show all the desired information for the collected lyrics and collected songs,
the recycler view will let you scroll through the information and in the achievements
page you can interact with the information displayed to the additional information.

Memory Percistence for Streaks, Points, Achievement:
The list above all have data percistence implemented wit shared prefrence which will
cache small amounts of data into your internal storage so that when the game is opened
again, the scores gained and the songs collected will not be lost. The streaks and 
points are stored as integers that will only be accessable by the guess activity where
you can view the statistics. This is the same for the achievemnts page where the list of
your collected songs are stored into the shared prefrence cache which will be displayed 
againg when you access the achievemtns page.

Notifications:
In order to communicate with the user on actions being performed, the snackbar has been
utilised. An exaple of this is when you pick up a marker, you will be notified with
a black card that will appear from the bottom with a message to inform the user about
the action or current state of the game. This is used to be in compliance of the 
androidx material design. 