# Mobile

## Info

* Apk can run on android devices with API level 25 to API level 30

## Unique Selling Point

* Back-And-Forth Motion

⋅⋅⋅* Game player char moves Back-And-Forth
⋅⋅⋅* Cam moves Back-And-Forth
⋅⋅⋅* Plats move Back-And-Forth
⋅⋅⋅* Coins move Back-And-Forth
//* Enemies move Back-And-Forth (too OP so removed lol)

## Jingle lyrics

She is a boss~
She is a girl~
She is a BOSS Girl~

## Task list of each member

- Same as "## Tasks done"

## Tasks done

### A1

* Splash screen
* Menu screen
* Game screen
* Exit button

* "Camera" scroll
* Game audio (music and sounds)
* Animations in splash screen
* Collision detection and response (between player and platforms)
* Screen transition (for all existing screens)
* Sprite animation
* Text on screen
* Pause button
* Level generation
* Scoring (basic)
* Player controls
* Player mechanics (jumping, falling, moving back and forth on platform)
* Coloring of platforms
* Detection of player death
* Event system
* Adaptive icon
* Using custom font

### A2

* Game Over screen
	- Display of score
	- Checkbox to indicate whether or not to save score
	- Text input box to indicate player name
	- Continue button

* Game screen
	- Gif BG
	- Game player character dies when hitting enemy, top of screen and bottom of screen
	- Vibrational feedback on game player character's death
	- Player can collect coins
	- Particle system
	- Platform pop with easing
	- Moving platforms move horizontally with random easing function
	- Improved level generation
	- Improved scrolling of level
	- Created a camera and made it follow the game player character horizontally
	- CircleAABB (collision between game player character and coin)
	- PlatAABB (collision between game player character and platform)
	- AABBAABB (collision betwen game player character and enemy)
	- CircleCircle (not used)
	- Sound for collecting coins
	- Sound for jumping
	- Cam tries to catch up if game player character goes too high

* Menu screen
	- Pressing the built-in android back button opens a dialog
	- Gif BG
	- Show UpdateFPS and RenderFPS on screen
	- Adjusted menu player character
	- Ball with accelerometer
	- Buttons for going to different screens

* Options screen
	- Music and sound vol can be adjusted
	- Stylized sliders
	- Percentage is shown on screen and follows thumb
	- Save button
	- Reset button
	- Left Arrow button
	- Pressing Left Arrow button without saving causes a dialog to pop up

* Rankings screen
	- Sorting of rankings from highest to lowest
	- Vertical scrolling (only works if there are enough rankings shown)
	- Allows for multiple different players to have the same score
	- Allows for the same player to have multiple different scores
	- Left Arrow button

* Shop screen
	- Horizontal scrolling
	- Display of amount of coins
	- Buy button
	- Cancel button
	- Equip/Unequip button
	- Back button
	- Left Arrow button

* Splash screen
	- Smoother transition between opening the app to splash screen and between splash screen to menu screen

* Misc
	- Exit button
	- Button animations with easing
	- Social Media (connecting to Facebook)
	- Used 1 thread for updating and 1 thread for rendering
	- All elements scale according to screen dimensions
	- Object Pooling
	- Saving and loading of music and sound vol with shared preferences
	- Saving and loading of amount of coins with shared preferences
	- Saving and loading of backgrounds bought and equipped with serialization
	- Saving and loading of rankings with serialization

## Tasks not done

### A1

* Lvl selection and lvl selection screen (not gonna do in A2 as it's too much work for too little value)
* End screen
* Options screen (10% done)
* Moving platforms
* Collectables
* Feedback
* Button animation

### A2

* Polish for several areas (moving on because I'm gonna run out of time if I don't)