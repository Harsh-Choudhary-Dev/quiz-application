#!/bin/bash

# Update package index
echo "Updating package index..."
sudo apt update -y

# Install OpenJDK 17
echo "Installing OpenJDK 17..."
sudo apt install openjdk-17-jdk -y

# Verify installation
echo "Verifying OpenJDK 17 installation..."
java -version

# Set OpenJDK 17 as default (Optional)
echo "Setting OpenJDK 17 as default Java version..."
sudo update-alternatives --config java

# Set JAVA_HOME environment variable (Optional)
echo "Setting JAVA_HOME environment variable..."
echo "export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" >> ~/.bashrc
echo "export PATH=\$JAVA_HOME/bin:\$PATH" >> ~/.bashrc

# Reload shell configuration
echo "Reloading shell configuration..."
source ~/.bashrc

# Install Maven
echo "Installing Maven..."
sudo apt install maven -y

# Install Nginx
echo "Installing Nginx..."
sudo apt install nginx -y

# Allow Nginx Full through UFW
echo "Allowing Nginx Full through UFW..."
sudo ufw allow 'Nginx Full'

# Check Nginx status
echo "Checking Nginx status..."
systemctl status nginx

echo "OpenJDK 17, Maven, and Nginx installation complete!"
