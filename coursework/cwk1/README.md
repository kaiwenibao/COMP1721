# COMP1721 Coursework 1

* [Using Gradle](#using-gradle)
* [Tests & Style Checking](#tests-and-style-checking)
* [ActiveCases Application](#activecases-application)
* [Chart Application](#chart-application)  
* [Cleaning Up](#cleaning-up)
* [Submitting](#submitting)

## Using Gradle

Everything you need to do in this assignment, aside from editing code, is
handled by the Gradle build system.  You can run Gradle from the command
line, or use it from within VS Code or IntelliJ IDE if you prefer (see below).

**You do not need to install Gradle yourself**, as we've included a
'Gradle wrapper' that will download a compatible version of the tool, along
with various library dependencies, if required.  **This will be very slow
the first time that it runs!**

You should run Gradle commands in the `cwk1` directory.  On Linux and macOS,
use `./gradlew`.  On Windows, use `gradlew`. (The commands below assume
the use of Linux or macOS.)

The different coursework artifacts are organised into separate projects.
You can list these projects with `./gradlew projects`.  The project name
needs to be included when running tasks within a specific project.

### If Using Visual Studio Code

You can simplify the use of Gradle by installing the Gradle Tasks extension.
This will add a Gradle button to the side bar.  Clicking on this will
active a panel in which you can see and run the Gradle tasks for the
various projects.

### If Using IntelliJ IDEA

**Please use the most recent version of the IntelliJ, on your own PC.
We do not support the use of older versions for the coursework, and may
not be able to help you if you encounter problems.**

Use the *New Project* button to import `cwk1` as the project directory.
After the project has been imported, you should be able to use the Gradle
button on the right-hand edge of the IntelliJ window to active the Gradle
tool window.  From here you can access a hierarchical list of all the
supported Gradle tasks.  The main list entries you will need to use for the
coursework are

    core/Tasks/verification/test
    app/Tasks/application/run
    app/Tasks/application/runNoArgs

Note: once you've used one of these entries, IntelliJ will create a run
configuration for it, allowing you to rerun it in future in a more
convenient way.

## Tests and Style Checking

To run all the unit tests, recompiling code where necessary, do

    ./gradlew :core:test

To run only the tests for the basic solution, do

    ./gradlew :core:test --tests Basic

Substitute `Full` for `Basic` in the above command to run only the tests
for the full solution.

Test names and status (PASSED or FAILED) are listed in the terminal.
A **test report** is also generated in HTML, containing detailed information
about the tests and any failures that have occurred.  You can access this
by pointing your browser at `core/build/reports/tests/test/index.html`.
We suggest you keep this page open in a browser tab, refreshing it
as necessary, so that you always have full information available on test
results.

Note that Gradle won't normally rerun tests if they have all passed, unless
you've changed something in your code.  You can force it to recompile
everything and rerun the tests using

    ./gradlew :core:test --rerun-tasks

To check coding style for `CaseRecord` and `CovidDataset`, do

    ./gradlew :core:style

If there are any style violations, you'll see a count reported in the
terminal window.  A full report on the violations can be viewed by pointing
your browser at `core/build/reports/checkstyle/main.html`.

**Treat this as a rough guide.** Note that style checking is not exhaustive,
and we may still penalise you on style grounds  even if no violations are
reported here.  As with unit tests, Gradle normally won't rerun the style
check unless you change something in your code.

## ActiveCases Application

You can check coding style for this application with

    ./gradlew :app:style

You can run the application like this:

    ./gradlew :app:run

This will use `datafiles/2020-daily.csv` as the input file and
`datafiles/2020-active.csv` as the output file.

You can run the application without any command line arguments (e.g.,
to check that it issues a helpful error message) like so:

    ./gradlew :app:runNoArgs

You can create a redistributable version of the application with

    ./gradlew :app:distZip

This creates a Zip archive `app.zip`, in `app/build/distributions`.
This Zip archive contains the compiled code for the core class library and
application, bundled into a pair of JAR files, plus a shell script and batch
file that can be used to run the program on Linux, macOS or Windows.

## Chart Application

*Note, since this involves a GUI, you won't be able to work on this with
a 'headless' JDK - as would typically be used inside Windows Subsystem for
Linux, for example.*

You can check coding style for the chart application with

    ./gradlew :chart:style

You can run the chart application with

    ./gradlew :chart:run

This will run the program with `datafiles/2020-daily.csv` as a command line
argument.

## Cleaning Up

To remove all files generated by the build process, use

    ./gradlew clean

## Submitting

Your coursework solution must be submitted to Minerva **and** must also be
tagged as a solution in your GitLab repository.

To generate your Minerva submission, do this:

    ./gradlew submission

This will produce a Zip archive named `cwk1.zip`, containing everything that
you need to submit.  **You must then submit this manually to Minerva**, using
the link provided in the *Submit My Work* area.

The remaining steps depend upon the platform you are using.

### On Linux or macOS

First, make sure that you've committed any outstanding changes to your
repository.  Check this with `git status`.

Next, cd to the top level of your repository and run the `finished` script:

    ./finished cwk1

This will push commits up to gitlab.com.  It will also tag the most recent
commit as the one that we should look at as your coursework solution.

#### If you make a mistake...

If you ran the `finished` script too early and need to make further
changes to your solution, do this:

    ./notfinished cwk1

Then make and commit the necessary changes.  You can then run the `finished`
script again.

### On Windows

First, make sure that you've committed any outstanding changes to your
repository.  Check this with `git status`.

Next, cd to the top level of your repository and run the following Git
commands:

    git push origin main
    git tag -a cwk1 -m "cwk1 solution"
    git push origin cwk1

These commands will ensure that all your commits are available on gitlab.com,
and that the most recent commit is tagged as your solution.

#### If you make a mistake...

If you ran the above commands too early and need to make further changes
to your solution, do the following:

    git tag -d cwk1
    git push origin :refs/tags/cwk1

Then make and commit the necessary changes. After that, you will need to
run the previous set of three Git commands again.
