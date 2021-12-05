# Changelog

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

### Williamstown Meet (2021-12-05)


### ⚠ BREAKING CHANGES

* break apart autonomous into sequences
* use bulk update manual mode to not read sesnsor data more than once per update loop
* completely redesign the red shipping hub autonomous

### Features

    No New Features

### Bug Fixes

* use bulk update manual mode to not read sesnsor data more than once per update loop ([cc2f959](https://github.com/Nick-Fanelli/FtcRobotController/commit/cc2f959f74b26506bdca6274535aa1809eda8e6e))

---

## Farmingdale Meet (2021-11-28)


### ⚠ BREAKING CHANGES

* add powerscale to drive motors
* migrate all team code to TEAM_IDENTIFIER.freightfrenzy and all common code to TEAM_IDENTIFIER.common
* refactor so drive system in operation mode is directly the one specified and not generic

### Features

* add all operator and driver teleop controls ([f9bd698](https://github.com/Nick-Fanelli/FtcRobotController/commit/f9bd698b284904e8f9d45f3db399106eb54ae3d2))
* add automatic lift zeroing system ([2be73b3](https://github.com/Nick-Fanelli/FtcRobotController/commit/2be73b3d77f66d4ccbeedfe1f8c12e3e9cd27259))
* add autonomous operation mode ([dde936e](https://github.com/Nick-Fanelli/FtcRobotController/commit/dde936e145913de379db43d0224097d5ee72dc1a))
* add basic operation mode and robot class ([04e48ac](https://github.com/Nick-Fanelli/FtcRobotController/commit/04e48ac2d15e7d252d25a3a14ba4c908f4e268be))
* add bravenator runtime exception ([86880dd](https://github.com/Nick-Fanelli/FtcRobotController/commit/86880dd75f04137a15eaae1e7b522606e45e49ff))
* add competition motors to teleop ([b318d92](https://github.com/Nick-Fanelli/FtcRobotController/commit/b318d9271bf7592b79cc2372bf65712fbc548b60))
* add configuration opmode ([c5c28a1](https://github.com/Nick-Fanelli/FtcRobotController/commit/c5c28a109eceabab0be3407275fda27631305948))
* add ease of use generate motors method to four wheel drive class ([3290b92](https://github.com/Nick-Fanelli/FtcRobotController/commit/3290b9232409375701305a7b9e79e4573d9fb131))
* add gamepad support in teleop operation mode ([465b51b](https://github.com/Nick-Fanelli/FtcRobotController/commit/465b51b6a09e5ae0726f66cc487acdd6cc817561))
* add in calculated position methods to four wheel drive ([8f4f4d7](https://github.com/Nick-Fanelli/FtcRobotController/commit/8f4f4d77c1c09756279b32842faf161c22109229))
* add in saftey check system to insure the robot specifications are configured correctly ([75db2ee](https://github.com/Nick-Fanelli/FtcRobotController/commit/75db2ee3ff648b2530ec83769925c21ee4103e49))
* add max velocity test ([d86be1c](https://github.com/Nick-Fanelli/FtcRobotController/commit/d86be1c823231f91683297f9a055da0bd4798c1f))
* add power scale class to emulate a power scale ([c5a66fe](https://github.com/Nick-Fanelli/FtcRobotController/commit/c5a66feb9f41cfbf82ba80d98956b6b6f92ea508))
* add powerscale to drive motors ([079fd6b](https://github.com/Nick-Fanelli/FtcRobotController/commit/079fd6b821e35b54e5db7e585af2ac582efa2e09))
* add starting position to robot config ([e9403e8](https://github.com/Nick-Fanelli/FtcRobotController/commit/e9403e81562c92c1acaf3c41f2dbab5c772b1d15))
* add subsystem system ([0e88f75](https://github.com/Nick-Fanelli/FtcRobotController/commit/0e88f75d0c70b98f922c39afa3ffd2a30febf185))
* add tensorflow object detector class (not fully implemented yet) ([aae8734](https://github.com/Nick-Fanelli/FtcRobotController/commit/aae87349c955059c5ffa0773ca73495c94d6917a))
* add unknown saftey to force the duck position to end in either left, right or center ([2727438](https://github.com/Nick-Fanelli/FtcRobotController/commit/2727438c554c19e8732280d689a0915898c236d4))
* add warehouse park code ([b36f5f2](https://github.com/Nick-Fanelli/FtcRobotController/commit/b36f5f2c46e01314b8d5df0ccfd86d6f3e4a4371))
* automatic robot pidf tuning ([58dc12c](https://github.com/Nick-Fanelli/FtcRobotController/commit/58dc12c9bc0d31cc6891003a91b7ad16012fd954))
* calculate deltaTime information in TeleopMode ([9717cac](https://github.com/Nick-Fanelli/FtcRobotController/commit/9717cac618a8dda297ec63507abb10f693a09c52))
* check for lift's encoder position until physical switch is installed ([29ee5b6](https://github.com/Nick-Fanelli/FtcRobotController/commit/29ee5b68dc903056af0deae208391269b3c57cc9))
* enable use velocity as default ([ec08f8c](https://github.com/Nick-Fanelli/FtcRobotController/commit/ec08f8ca59f4232776d026be6d80f769fde6ff5d))
* four wheel drive turning ([47d7152](https://github.com/Nick-Fanelli/FtcRobotController/commit/47d71523d88e4c15a562bdc3455cabba06137b00))
* ignore the lift zero after 1.5 seconds ([7c689b7](https://github.com/Nick-Fanelli/FtcRobotController/commit/7c689b742f6640b9ce8f7e278973af59a10973fe))
* lift touch sensor encoder system ([1abf516](https://github.com/Nick-Fanelli/FtcRobotController/commit/1abf5161269440a219e9bdf271cf61772e3d987b))
* mecanum drive ([e7a9338](https://github.com/Nick-Fanelli/FtcRobotController/commit/e7a93382baac7a0504185c8ab2cae3bb4d1d2505))
* red side warehouse autonomous ([d456b7e](https://github.com/Nick-Fanelli/FtcRobotController/commit/d456b7ef3efd5c9a06928a82173f58b99337ec6f))
* require wheel specs ([80a4ef0](https://github.com/Nick-Fanelli/FtcRobotController/commit/80a4ef068d7fca0380b0caa6c4d03aa9b66e5c76))
* reverse intake control ([3c3baae](https://github.com/Nick-Fanelli/FtcRobotController/commit/3c3baae73d65d3b7884a534e96663522001c9ae4))
* saftey check for motor count mismatch ([4d2f7ce](https://github.com/Nick-Fanelli/FtcRobotController/commit/4d2f7ce62d06f76c239f597b78dd5900044a4acf))
* selectable drive system ([8c28612](https://github.com/Nick-Fanelli/FtcRobotController/commit/8c286120f094cfe6c03fc1fbcb0695a76aa78036))
* set maximum movement speed in teleop ([b5596ae](https://github.com/Nick-Fanelli/FtcRobotController/commit/b5596ae8a59f7c3b01a0f5ddff09249f8d6dbec2))
* telemetry important information ([c8d58d4](https://github.com/Nick-Fanelli/FtcRobotController/commit/c8d58d4af810573135b5fe33b83ac8f9a81731ef))
* tensor flow object detection recognition support ([070b6e2](https://github.com/Nick-Fanelli/FtcRobotController/commit/070b6e284e037b98224b3f23a53925f61819eb0f))
* two wheel drive ([4fd2712](https://github.com/Nick-Fanelli/FtcRobotController/commit/4fd27120065ea744fb7410db7900d99bdb0e9ef8))


### Bug Fixes

* adjust for the alliance shipping hub not being in the place I thought it was (oops) ([cffe293](https://github.com/Nick-Fanelli/FtcRobotController/commit/cffe2935950a9d3ff81c2008149d3ef7d4e85fd9))
* autonomous differences ([7593d52](https://github.com/Nick-Fanelli/FtcRobotController/commit/7593d52285e14c16157ecf0a9d5d5afeb573aaab))
* calculate delta time before updading the gamepads ([a1f4c7b](https://github.com/Nick-Fanelli/FtcRobotController/commit/a1f4c7b43a3a6fa3e7e33dfa18cc9d8e214dc56d))
* change the auto update recognition time delay to 5ms ([dfbdeb5](https://github.com/Nick-Fanelli/FtcRobotController/commit/dfbdeb5296f9af6e6513e483076eced6c6718a6d))
* control information output ([72643d2](https://github.com/Nick-Fanelli/FtcRobotController/commit/72643d27c452c452f9acc5cc4cf2f57610648aa1))
* create new motors before assigning them to a drive system ([c7188a8](https://github.com/Nick-Fanelli/FtcRobotController/commit/c7188a8963ac0374c774cea6f55e8e6ca86b9b89))
* create robot each operation mode ([0ab8b78](https://github.com/Nick-Fanelli/FtcRobotController/commit/0ab8b78570068321fc2a6380ea8dab30c4944896))
* deprecate unsupported FtcGamePad constructors ([c2bf32d](https://github.com/Nick-Fanelli/FtcRobotController/commit/c2bf32d5961063e8dfcd95c1e968ee1e87dff087))
* four wheel drive's drive method and robot creation ([5a80d86](https://github.com/Nick-Fanelli/FtcRobotController/commit/5a80d862523d1436a73680eb163790289bee2e1d))
* lift power speed and strafe and turn direction ([25fa0ad](https://github.com/Nick-Fanelli/FtcRobotController/commit/25fa0ad6dc47dbd079b4293e72222770ea622738))
* only reset the robot in auto mode ([a7dc1b9](https://github.com/Nick-Fanelli/FtcRobotController/commit/a7dc1b9f99fd6dd3616d2a97e5b980f45752029b))
* red shipping hub autonomous ([2978904](https://github.com/Nick-Fanelli/FtcRobotController/commit/29789042316b161065964ed1727137fabe732448))
* refactor shipping hub to warehouse ([40b4e7d](https://github.com/Nick-Fanelli/FtcRobotController/commit/40b4e7d043a27aa6fe9aaff81820353ccf051cde))
* refactor shipping hub to warehouse ([52bc5dd](https://github.com/Nick-Fanelli/FtcRobotController/commit/52bc5dd06fc927113b22872ee8769a9e771ce071))
* rename shipping hub to warehouse ([7398ea4](https://github.com/Nick-Fanelli/FtcRobotController/commit/7398ea43161c595280a10f3f2b56d08112144427))
* reverse alliance shipping element detection for blue side ([7238927](https://github.com/Nick-Fanelli/FtcRobotController/commit/723892753ef30eada2eb770a744ee7ed8093aa47))
* teleop ([84c71da](https://github.com/Nick-Fanelli/FtcRobotController/commit/84c71da73b7051d8ff3376582f6a775a2223afee))
* turning calculation ([dfadfd0](https://github.com/Nick-Fanelli/FtcRobotController/commit/dfadfd00ddb6d786ed16f0a9ae8b783d29703401))
* update to use velocity ([2067725](https://github.com/Nick-Fanelli/FtcRobotController/commit/20677250600e54947d95858aec80a3e3777c3c73))
* use new updated wheel diameter math to calculate turning distance ([490d283](https://github.com/Nick-Fanelli/FtcRobotController/commit/490d28331e0e266978872d30fa9159fa855739a2))
* migrate all team code to TEAM_IDENTIFIER.freightfrenzy and all common code to TEAM_IDENTIFIER.common ([a71e457](https://github.com/Nick-Fanelli/FtcRobotController/commit/a71e457d9543e7042670c94924d50da9c0b0b1b6))
* refactor so drive system in operation mode is directly the one specified and not generic ([ff04230](https://github.com/Nick-Fanelli/FtcRobotController/commit/ff0423012d9357ace4cbcb91f864bb257b910518))