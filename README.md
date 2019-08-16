# EinsteinsWorkshopEDU Minecraft Sponge Mod

## Description
This mod was developed for Einsteins Workshop to provide administrative features to facilitate an educational environment.

## Author
Pieter Svenson
Website: www.pietersvenson.com

## Contact
Discord:

## Permissions
- einsteinsworkshop
  - einsteinsworkshop.command.student
    - Allows usage of student-level commands
  - einsteinsworkshop.command.instructor
    - Allows usage of instructor-level commands
    - Instructors must also inherit the student permission
  - einsteinsworkshop.immunity
    - Causes immunity of student-altering effects
    - Required for instructors
    - A feature added for testing purposes


## Commands
### Command Syntax Format
- "|": Delimiter for a list of synonymous subcommands
- "<foo>": Foo is a required argument
- "[bar]": Bar is an optional argument

### Command Usage
- **/einsteinsworkshop|ew**
  - **freeze**
    - Students cannot interact, be interacted with, or chat
    - **all**
      - Freezes all students in place
      - Students who join into the world will be frozen, even if they were individually unfrozen before
    - **player <player>**
      - Freezes a specific player
      - Students cannot interact, be interacted with, or chat
