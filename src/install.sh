#!/bin/sh

if [ "$(id -u)" != "0" ]; then
	printf "\e[31m[+] Installation must be executed as root. \e[0m \n"
	exit 1
fi

git --version 2>&1 >/dev/null
GIT_IS_AVAILABLE=$?

if [ $GIT_IS_AVAILABLE -eq 0 ]; then
    printf "\e[32mGreen[+] Installing git: \e[0m \n"
    sudo apt-get install git-core
fi

printf "\e[32mGreen[+] Update apt-get: \e[0m \n"
sudo apt-get update


printf "\e[32mGreen[+] Upgrade apt-get: \e[0m \n"
sudo apt-get upgrade


printf "\e[32mGreen[+] Clone wiringPi: \e[0m \n"
git clone git://git.drogon.net/wiringPi


printf "\e[32m[+] WiringPi building: \e[0m \n"
cd wiringPi
./build

printf "\e[32m[+] Done! \e[0m \n"