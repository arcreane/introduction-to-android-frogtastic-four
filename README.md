[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/JgDN2xrp)

# Our Future Goals and What We Got Done
During our presentation, we stated that we had quite a few different goals for the future, in the following list we will have marked which ones got completed and which ones didn't.
- [ ] Single Player
- [ ] Online Multiplayer
- [x] Custom Games
- [ ] Optimizations
- [ ] More Haptic Feedback
- [ ] Custom Games
- [x] Leaderboard
- [ ] Settings
- [x] Help Page
- [x] Improve Game UI
- [x] Use Player Names

Now most of the things above we had as long term goals, things we would've liked to added at some point but would not have had the chance to in the short amount of time provided to us for this project. That being said, the things that were short term goals were more or less all implemented, and we'll provide a quick rundown of what we added.

1. Leaderboard
   We stated that we would be adding a leaderboard to this game, this is in fact something we managed to add. Now when a user plays with their friends, they'll see their score on the leaderboard, with the top three players having medals next to their names.
   Please look at the Before You Start section for more information on how the leaderboard works.
2. Help Page
   Given the confusion that this game can cause, we did in fact add a Help Section to help explain the rules of the game before a user properly begins. This is done instead of being in a new page as a section that pops up on the home screen before the game begins if you click on the "?" button.
3. Use Player Names
   By nature of making the Leaderboard, we also made sure that we were now getting the user's names as well. Now while we previously had the text boxes for it and were simply not using them yet, we've since updated the game so that we are using them properly.
4. Custom Games
   In this case, Custom Games means customizing the game board, this includes the game board size (2x2, 3x3, etc) as well as the depth of the board (is it TicTacToe²? TicTacToe²²? So on and so forth). This was something we added as we were using player names, making sure that the user can provide their game size before the game begins and then making sure the board in the game page populates properly. We also made sure to make the board scrollable in case a user put in a board size that was far bigger than their phone screen is.
5. Improve the Game UI
   Finally, our last and one of the most important goals we had was to help fix the game so that it was more understandable to look at. Given the nature of the game, if you're not careful it's very easy to lose yourself in the boards and the cells, trying to win and forgetting everything you've done previously.
   For us, this meant keeping what we had initially as a Current Board & Cached Board, and showing the Depth, X&Y values of the cell, and which user was currently playing. That being said, we modified it slightly, now instead of showing a Cached Board of the last played move, that board is more of a "Preview" Board. It will show the main board with the option to navigate around it and see which boards have been played in and which ones haven't. In this way allowing the user to track their moves and where they've play a little easier.

# Before You Start
Our Leaderboard will not function as expected, this is due to the fact that you will not have a google-services.json file, which is what authenticates us with the firestore database. If you wish to test that feature, you can set up your own firestore database and place it in the app directory. If you do this, please ensure that the application has enough permissions read and write to the database.

If any help is needed with regards to getting this functioning, please message us and we will help make sure it is something that is properly set up.