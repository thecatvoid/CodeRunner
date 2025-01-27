#!/bin/bash
sudo apt update
sudo apt install -y openjdk-17-jdk wget curl unzip git neovim
sudo apt-get update
sudo mkdir /opt/android-sdk
sudo chown -R $USER:$USER /opt/
wget "https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip" -O latest.zip
unzip latest.zip
rm -f latest.zip
mv cmdline-tools /opt/
export PATH="/opt/cmdline-tools/bin:$PATH"
yes | sdkmanager --licenses --sdk_root=/opt/android-sdk
sdkmanager --sdk_root=/opt/android-sdk "platform-tools" "build-tools;30.0.3" "platforms;android-30"
cd $HOME
git clone https://www.github.com/thecatvoid/dot
cp -a dot/.* ./
nvim -c 'PlugInstall' -c 'qa'
nvim -c 'TSInstall kotlin' -c 'qa'
