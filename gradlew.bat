@ECHO OFF
SETLOCAL

SET "DIRNAME=%~dp0"
IF "%DIRNAME%"=="" SET "DIRNAME=.\"
SET "APP_HOME=%DIRNAME%"
SET "CLASSPATH=%APP_HOME%gradle\wrapper\gradle-wrapper.jar"
SET "JAVA_EXE="

IF DEFINED JAVA_HOME IF EXIST "%JAVA_HOME%\bin\java.exe" SET "JAVA_EXE=%JAVA_HOME%\bin\java.exe"

IF NOT DEFINED JAVA_EXE FOR %%I IN (java.exe) DO SET "JAVA_EXE=%%~$PATH:I"

IF NOT DEFINED JAVA_EXE IF EXIST "%ProgramFiles%\Android\Android Studio\jbr\bin\java.exe" SET "JAVA_EXE=%ProgramFiles%\Android\Android Studio\jbr\bin\java.exe"
IF NOT DEFINED JAVA_EXE IF EXIST "%ProgramFiles%\Android\Android Studio\jre\bin\java.exe" SET "JAVA_EXE=%ProgramFiles%\Android\Android Studio\jre\bin\java.exe"

IF NOT DEFINED JAVA_EXE (
    ECHO ERROR: JAVA_HOME no esta configurado y no se encontro un runtime de Java. 1>&2
    EXIT /B 1
)

"%JAVA_EXE%" %JAVA_OPTS% %GRADLE_OPTS% "-Dorg.gradle.appname=%~nx0" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*

ENDLOCAL