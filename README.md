# GameGuesser

The GameGuesser is a quiz styled app where the users will try and guess the name of the video game based on hints and clues
This project has been developed for the course PROG7314

### Here is a Guide for the developers : [GUIDE.md](https://github.com/ST10258256/PROG7314-POE-PART_TWO-GameGuesser/blob/main/GUIDE.md)

--- 

## Features 
The features for this project include 
- Two Game modes where the user will guess the game's name from the given criteria that they give you
-  A Enclclopedia where it will list all the games from the database, and the user will be able to filter the games to met some criteria
  - A chatbot that will allow the user to query it.
- There is a settings page where the user can change the language for the app, the user details, log out

---

## Software used
The Software that was used for this project
- Android Studio
- React
- NPM
- Google Play Services
- MongoDb
- Render

--- 

## How you get started using the app
First either Clone or Fork the App.

```bash
git clone https://github.com/ST10258256/PROG7314-POE-PART_TWO-GameGuesser.git
cd adroid-app
```

Open the Folder called android-app as shown below 


<img width="776" height="397" alt="image" src="https://github.com/user-attachments/assets/16fa5306-20f2-44fe-b33c-e4ebbff3f709" />

This is the project for android studio and this is the project that you will run on android studio.

Now that it is open in android studio it should look somehting like this 
<img width="1920" height="1032" alt="image" src="https://github.com/user-attachments/assets/a467747b-a2e6-4b13-8a9e-5b157dd311a4" />
You will have to build Gradle first then Click the run button at the top to start the emulator of the app.

Your app should work perfetly fine, log in with SSO and then since the API is running on render it should be able to make the API requests to the database that is running online. You are able to change the backend and make the app take requests from there instead

If there is an issue with the api you will have to launch the backend api locally with 

```bash
npm run dev
```

You will also have to change this line in this code to your local port
<img width="1920" height="1032" alt="image" src="https://github.com/user-attachments/assets/577ce1ed-15dd-4b41-817b-433efffc7ab2" />


---

### Prerequisites
Make sure you have a newer version of android studio, preferable narwhal
Make sure you have git 
Have a google account

---

### Purpose of the App

The app is supposed to be a game where the user has to use their mind to guess what the game is based on one of the two game modes. They can either play the key words game mode where they have to guess what the game is based off of the keywords. The other game mode is is where you guess what the game is based off the facts based off of the game that guessed. There is also an enclyclopedia  feature where the user can look at all the games and read up about them, and there is a chatbot so that the user can talk about games with the chat.

---

### Utilisation of Github and Github actions

We used github to build the project as we are a team, github made it easier for us to work together as we would each create branches to work on their parts of the project whilst keeping the main project clean and then building upon it with each of their parts. Secuirty was put into place where people could not just add to the main branch without first having to make a pull request. We also made use of github actions where it would check that the app builds correctly and that there are no errors in the project.

---

### Design Considerations

Since we wanted to do a Restful API we decided that it would be better to separate the android app from the api. The API is hosted on render so that those api calls can be made all the time from the api so it will always be online and we make it so the user does not always have to run the api locally to get the app to work. There is also an online database at the moment so that the user does not have to generate the games themselves.

--- 

---

## License

Copyright (c) 2025 Group 14

All rights reserved.  

This project was created as part of a school assignment.  
The code, documentation, and other materials in this repository are provided for educational purposes only.  

You may **not** copy, modify, distribute, or use this project, in whole or in part,  
without explicit written permission from the authors.
