# You can add "-X" for debug
call mvn clean pmd:check jacoco:prepare-agent package jacoco:check
pause