# EinsteinsWorkshopEDU Minecraft Sponge Mod

## Description
This mod was developed for Einsteins Workshop to provide administrative features to facilitate an educational environment.

## Author
Pieter Svenson  
Website: www.pietersvenson.com

## Contact
Discord: https://discord.gg/gJXyaU

## Permissions
- einsteinsworkshop
  - einsteinsworkshop.student
    - Allows usage of student-level commands
  - einsteinsworkshop.instructor
    - Allows usage of instructor-level commands
    - Instructors must also inherit the student permission
  - einsteinsworkshop.immunity
    - Causes immunity of student-altering effects
    - Required for instructors
    - A feature added for testing purposes


## Commands
### Command Usage Syntax
- "|": Delimiter for a list of synonymous subcommands
- "<foo>": foo is a required argument
- "[bar]": bar is an optional argument
- "...": This argument can take multiple words
- "{do, re}": *do* and *re* are the only two options

### Command Usage
- **/einsteinsworkshop|ew**
  - **freeze|f**
    *Permission: einsteinsworkshop.instructor*
    Students cannot interact, be interacted with, or chat
    - **all|a**
      - Freezes all students in place
      - Students who join into the world will be frozen, even if they were individually unfrozen before
    - **player|p <player>**
      - Freezes a specific player
  - **unfreeze|uf**
    - *Permission: einsteinsworkshop.instructor*
    - Student can move and chat freely
    - **all|a**
      - Unfreezes all students
    - **player|p <player>**
        - Unfreezes a specific player
  - **assignment|a**
    - **list**
      - *Permission: einsteinsworkshop.student*
      Lists all assignments
    - **complete <id>**
      - *Permission: einsteinsworkshop.student*
      - Completes the assignment with the given id
    - **add <type> <title...>**
      - *Permission: einsteinsworkshop.instructor*
      - Add an assignment with the given title
    - **remove <id>**
      - *Permission: einsteinsworkshop.instructor*
      - Remove an assignment with the given id
    - **info <id>**
      - *Permission: einsteinsworkshop.instructor*
      - Get a detailed information message about the assignment with the given id
    - **edit**
      - *Permission: einsteinsworkshop.instructor*
      - **title <id> <title...>**
        - Edit the title of the assignment with the given id and title
      - **body <id> <body...>**
        - Edit the body of the assignment with the given id and body
      - **type <id> <type>**
        - Edit the type of the assignment with the given type
  - **box**
    - *Permission: einsteinsworkshop.instructor*
    - **list**
      - Returns a list of all saved boxes
    - **position1|pos1**
      - Sets the location of the first position for selecting a box region to your current location
    - **position2|pos2**
      - Sets the location of the second position for selecting a box region to your current location
    - **create**
      - Generates a box in the currently selected region
    - **destroy <id>**
      - Deletes a box with the given id
    - **info [id]**
      - Returns detailed information about the box with the given id
      - If no id is given, it returns information about the box you are currently occupying
    - **edit**
      - **movement <id> {true,false}**
        - Sets the student's movement ability inside the box
      - **building <id> {true,false}**
        - Sets the students' editing ability inside the box
    - **show**
      - **all**
        - Displays a brief border around all boxes saved
      - **<id**
        - Displays a brief border around the box with the specified id
    - **teleport|tp <id>**
      - Teleport yourself to the corner of a specific box *(corner is at minimum of x, y, and z coordinates)*
    - **wand**
      - Gives yourself the wand associated with box position selection.
      - By default, this is a brick. This cannot be changed at this time.
  - **home**
    - *Permission: einsteinsworkshop.student*
    - Teleports you to your home, if you set 
      - **set**
        - Sets your home location to your current location
      - **set <x y z>
        - *Permission: einsteinsworkshop.instructor*
        - If x, y, and z coordinates are specified, then sets your location to that coordinate location in the world you are in
      - **player**
        - *Permission: einteinsworkshop.instructor*
        - Teleport you to the home of the specified player
