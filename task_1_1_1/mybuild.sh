#!/bin/sh

# Tasks.
TASK_JAVADOC=false
TASK_BUILD=false
TASK_RUN=false

# Other variables.
APP_NAME=App
DEST=mybuild
MAIN_CLASS=Main
RUN_ARGV=""

# Help message.
helper() {
    echo "Usage: $0 [parameters]"
    echo "At least one task (-b, -r, -d) should be executed."
    echo "-d - generate documentation using Javadoc tool"
    echo "-b - build project (result is APPNAME.jar)"
    echo "-r - run project (includes build)"
    echo "-h - show this message"
    echo "-m MAINCLASS - set main class name (default is Main)"
    echo "-n APPNAME - set app name (default is App)"
    echo "-o DEST - output directory (default is mybuild)"
}

# Parsing input arguments.
while getopts "dbraho:m:" arg
do 
    case $arg in 
        d)
            TASK_JAVADOC=true
            ;;
        b)
            TASK_BUILD=true
            ;;
        r)
            TASK_BUILD=true
            TASK_RUN=true
            ;;
        o)
            DEST=$OPTARG
            ;;
        n)
            MAIN_CLASS=$OPTARG
            ;;
        a)
            APP_NAME=$OPTARG
            ;;
        *)
            helper
            exit 0
            ;;

    esac
done


if [ $TASK_BUILD == false ] && [ $TASK_JAVADOC == false ]
then
    helper
    exit 0
fi


# Creating a list of sources.
mkdir -p $DEST
cd $DEST

# Build task.
if [ $TASK_BUILD == true ] 
then
    echo "TASK: Building..."
    # Finding sources
    find ../src/main -name "*.java" > sources.list

    # Compile sources and put them into DEST
    javac -d . @sources.list

    # Finding classes
    find -name "*.class" > classes.list

    # Generating manifest.mf
    printf "Main-Class: " > manifest.mf
    # If MAINCLASS is not unique, the first found instance will be used
    find * -name "$MAIN_CLASS.class" | awk '{print substr($1, 0, length($1)-6)}' >> manifest.mf
    printf "Class-Path: $APP_NAME.jar\n" >> manifest.mf

    # Generating jar
    jar -c -m manifest.mf -f $APP_NAME.jar @classes.list

    # Cleaning up
    find * ! -name $APP_NAME.jar -exec rm -rfd {} +
fi

if [ $TASK_RUN == true ] 
then
    echo "TASK: Running..."
    java -jar $APP_NAME.jar
fi

if [ $TASK_JAVADOC == true ]
then
    echo "TASK: Creating javadoc..."

    # Creating Javadoc
    find ../src/main -name "*.java" > sources.list
    javadoc -d docs @sources.list
    
    # Cleaning up
    rm sources.list
fi
