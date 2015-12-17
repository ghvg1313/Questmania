# Questmania

## What's this?

Team Akili is developing a tablet trivia game for High School students in Boston and Tanzania. <br />
Working with the Opportunity Education Foundation, Akili (Swahili for “smart”) will develop a game for Samsung Galaxy 4 that focuses on self-assessment and life skill development. <br /> <br />
Established in 2005 by Ameritrade founder and CEO Joe Ricketts, Opportunity Education Foundation is developing Quest, a learning platform that will enable young people around the globe. Akili’s game will build off of this learning platform and provide additional opportunities to use the knowledge gained through Quest. The game will feature solo and competition trivia challenges built from the Quest App Content. The game will also feature a unique challenge mode, where players will complete specially designed real world challenges to aid their game progress. <br /> <br />
As part of the design and development process, Akili will also conduct surveys and playtests with target audiences, including high school students in Tanzania, in order to design an experience that can be enjoyed globally. <br /> <br />
Akili seeks to foster the knowledge and skills students need to succeed.

## A little description
Four Singltons are the most important part of the project

### ```CenterController```
It controls the behaviours of the app stores all the information needed in solo mode, including quizes, solo challenges, player info, and all the local data, notice that there is a ```PartyGame``` instance need to be initialized here using ```startPartyGame(ArrayList<String> names)``` method.

### ```BackgroundController```
This is the music system of the app

### ```AchievementSystem```
This holds all the information about how to categorize different identities and achievement info about a player, a lot of utility method can be found here

### ```ProfileController```
This is responsible for profile log changes.

## Utility Class
All the unility class can be found under ```extension``` package.
```PartyModeActivity``` and ```SoloModeActivity``` is the parents classes which mostly define the behaviours of activity bar.</br>
```ResourceTool``` is a class provide different assets for the same content under different identities
