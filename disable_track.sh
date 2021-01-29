#!/bin/bash
git update-index --assume-unchanged build.gradle gradle.properties gradlew gradlew.bat LICENSE settings.gradle $(git ls-files app | tr '\n' ' ') $(git ls-files dagger_library | tr '\n' ' ') $(git ls-files gradle | tr '\n' ' ') 
