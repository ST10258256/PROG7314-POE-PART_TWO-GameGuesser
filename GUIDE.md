# GitHub Setup Guide

This is how to **clone, work on branches, create pull requests, and merge code** safely for our GameGuesser app project.

---

## 1 Repository Access
- You will be invited as a **collaborator** to the repository.  
- Accept the invitation from the email or GitHub notification.  

---

##  2 Cloning the Repository
Open your terminal or Git Bash and run:

```bash
git clone https://github.com/ST10258256/PROG7314-POE-PART_TWO-GameGuesser.git
cd GameGuesser 
```

--- 

## 3 Create a Branch

```bash
git checkout -b [branch name]
```

---

## 4 Work in your Branch
- Working in your branch lets you test your features separate from the main
- This all can be done locally on your side

---

## 5 Commit the changes to your branch

```bash
git add .
git commit -m "[Description of changes/commit]"
```

---

## 6 Push to your branch

```bash
git push origin [branch name]
```

---

## 7 How to create a pull request 
1. Go to the repository on github
2. There should be a tab for pull requests
3. You can click compare and pull request button for your branch
4. You will have to give a title for the pull request and a description if you want
5. You can click the create pull request button at that point

---

## 8 How the pull request will be merged
An admin will review the code to see if it is okay and once they approve of it they will be able to allow it to be merged into main where everyine can then update their project with the latest changes

---
## 9 How to get the latest changes 
You can fetch the changes in android studio in the git branch tab and then update the project with the latest changes 
Or
If you want to start on with new work you can checkout to main and then create a new branch 
```bash
git checkout main
git pull origin main
```

---

# Rules 

## Branch Protection 
- You will not be able to push directly into main
- You will have to create pull request before you want your changes to be made to main
- Approval will be required before the pull request will be merged

## License
You mayb not copy, modify, ditribute or reuse the code without the permission from the authors. You can look at the LICENSE for more details.
