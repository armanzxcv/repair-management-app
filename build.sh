#!/bin/bash

# Android Repair Management App - Build Script

echo "🔨 Repair Management App - Build Script"
echo "=========================================="
echo ""

# Check if Android SDK is installed
if [ -z "$ANDROID_SDK_ROOT" ]; then
    echo "❌ ANDROID_SDK_ROOT not set"
    exit 1
fi

echo "✅ Android SDK found: $ANDROID_SDK_ROOT"
echo ""

# Clean project
echo "🧹 Cleaning project..."
./gradlew clean

# Build debug APK
echo "🏗️  Building debug APK..."
./gradlew assembleDebug

# Check if build was successful
if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    echo "📦 APK location: app/build/outputs/apk/debug/app-debug.apk"
else
    echo "❌ Build failed!"
    exit 1
fi
