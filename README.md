# Simyo automatic mobile tariff switching

Author: Tomé Maseda Dorado

## Motivation

The Simyo website loads extremely slowly when you have a high number of registered SIMs, the idea is to use this software so you don't have to wait for those long loading times, you just leave the software running and it will do the work for you.

## Description

This software automates the change of mobile tariffs on [Simyo's website](https://www.simyo.es/).
For the given numbers, if they have a 7GB tariff it changes it to a 100MB tariff and conversely, if a number has a 100MB tariff it changes it to a 7GB tariff.
These changes are configurable, you can edit the software to change to different tariffs.

## Instructions for execution

To run the software you must first edit the following configuration files:

* **DNIs.txt** : File containing the DNIs of the users you want to log in to later modify their rates.

* **numbers_*name*.txt**: This file contains the numbers in which you want to modify your tariff for the user with the specified *name*. You can create a new file numbers_*name*, to use it you just have to add your DNI in the file DNIs.txt and add it to the HashMap (declared at the beginning of the program) together with the *name* of the new file numbers_*name* created.

**_NOTE_**: Telephone numbers and DNIs used in the code are fictitious and are given as an example.

Once you have configured the software you must make sure that you have the dependencies installed, to do this run (in the main project directory):

    gradle build

Once the dependencies are installed, you can run the program:

    gradle run

