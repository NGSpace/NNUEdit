@echo off
cd buildresults


rmdir /s /q NNUEdit-portable-linux
rmdir /s /q NNUEdit-portable-windows

echo:
set ver=1.4.2
for /f "delims=" %%x in (..\..\version) do set ver=%%x
echo NNUEdit Version: %ver%

set up-uuid=26e31545-6190-4265-abf6-e6d32b195f10
for /f "delims=" %%x in (..\..\NNUEdit-Upgrade-uuid.txt) do set up-uuid=%%x
echo Windows upgrade uuid: %up-uuid%


echo:
echo Creating win and linux app images
mkdir win
copy windows.jar win\windows.jar
mkdir linux
copy linux.jar linux\linux.jar



::WINDOWS
echo:
echo Building Windows Packages

:: MSI Installer
echo Building Windows MSI Installer

jpackage --vendor ngspace --app-version %ver% --copyright ngspace --description "the NNUEdit code editor"^
 --icon "..\NNUEdit.ico" --name NNUEdit^
 --type msi -i win --main-jar windows.jar^
 --win-dir-chooser --win-help-url https://github.com/NGSpace/NNUEdit/issues --win-menu --win-menu-group NNU^
 --win-shortcut-prompt --win-update-url https://github.com/NGSpace/NNUEdit/releases --win-upgrade-uuid %up-uuid%^
 --win-per-user-install

:: Portable
echo Building Windows Portable

jpackage --vendor ngspace --app-version %ver% --copyright ngspace --description "the NNUEdit code editor"^
 --icon "..\NNUEdit.ico" --name NNUEdit-portable-windows^
 --type app-image -i win --main-jar windows.jar




::LINUX
echo:
echo Building Linux Packages

:: Portable
echo Building Linux Portable

wsl jpackage --vendor ngspace --app-version %ver% --copyright ngspace --description "the NNUEdit code editor"^
 --icon "../NNUEdit.png" --name NNUEdit-portable-linux^
 --type app-image -i linux --main-jar linux.jar

:: DEB package
echo Building DEB Package

wsl jpackage --vendor ngspace --app-version %ver% --copyright ngspace --description "the NNUEdit code editor"^
 --icon "../NNUEdit.png" --name NNUEdit-portable-linux^
 --type deb -i linux --main-jar linux.jar^
 --linux-package-name nnuedit --linux-deb-maintainer ngspace --linux-menu-group NNUEdit --linux-shortcut^
 --linux-app-release %ver% --linux-app-category "NNUEdit"

:: RPM package
echo Building RPM Package

wsl jpackage --vendor ngspace --app-version %ver% --copyright ngspace --description "the NNUEdit code editor"^
 --icon "../NNUEdit.png" --name NNUEdit-portable-linux^
 --type RPM -i linux --main-jar linux.jar^
 --linux-package-name nnuedit --linux-menu-group NNUEdit --linux-shortcut^
 --linux-app-release %ver% --linux-app-category NNUEdit



::CLEANUP
echo:
echo cleaning up
rmdir /s /q win
rmdir /s /q linux

cd ..