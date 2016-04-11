-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema common
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `common` DEFAULT CHARACTER SET utf8 ;
USE `common` ;

-- -----------------------------------------------------
-- Table `common`.`AuditLogs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `common`.`AuditLogs` ;

CREATE TABLE IF NOT EXISTS `common`.`AuditLogs` (
  `logId` INT(11) NOT NULL AUTO_INCREMENT,
  `appUser` VARCHAR(150) NOT NULL,
  `activity` VARCHAR(45) NOT NULL,
  `clientIp` VARCHAR(45) NOT NULL,
  `activityTs` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`logId`))
ENGINE = InnoDB
AUTO_INCREMENT = 51
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `common`.`Role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `common`.`Role` ;

CREATE TABLE IF NOT EXISTS `common`.`Role` (
  `roleId` INT(11) NOT NULL AUTO_INCREMENT,
  `role` VARCHAR(15) NOT NULL,
  `roleDescription` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`roleId`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `common`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `common`.`User` ;

CREATE TABLE IF NOT EXISTS `common`.`User` (
  `userId` INT(11) NOT NULL AUTO_INCREMENT,
  `password` VARCHAR(250) NOT NULL,
  `firstName` VARCHAR(45) NOT NULL,
  `middleName` VARCHAR(45) NULL DEFAULT NULL,
  `lastName` VARCHAR(45) NOT NULL,
  `gender` CHAR(1) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `verified` TINYINT(1) NULL DEFAULT '0',
  `failedLogins` SMALLINT(6) NULL DEFAULT '0',
  `lastLogin` DATETIME NULL DEFAULT NULL,
  `versionNo` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`userId`),
  UNIQUE INDEX `uk_UserEmail` (`email` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `common`.`UserRole`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `common`.`UserRole` ;

CREATE TABLE IF NOT EXISTS `common`.`UserRole` (
  `userId` INT(11) NOT NULL,
  `roleId` INT(11) NOT NULL,
  UNIQUE INDEX `index1` (`userId` ASC, `roleId` ASC),
  INDEX `fk_PersonRole_2_idx` (`roleId` ASC),
  CONSTRAINT `fk_UserRole_1`
    FOREIGN KEY (`userId`)
    REFERENCES `common`.`User` (`userId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserRole_2`
    FOREIGN KEY (`roleId`)
    REFERENCES `common`.`Role` (`roleId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
