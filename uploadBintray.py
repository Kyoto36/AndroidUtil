# -*- coding: utf-8 -*-

import os
import os.path

def execWinCommand(cmd):
	os.system(cmd)

def main():
	execWinCommand("gradlew clean build bintrayUpload -PbintrayUser=kyoto36 -PbintrayKey=8d745c176beb2a850791443ba99f505326b711cf -PdryRun=false")