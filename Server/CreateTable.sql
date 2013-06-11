CREATE  TABLE `simnalamburt`.`ScreenLog` (
  `ID` INT NOT NULL AUTO_INCREMENT ,
  `Time` BIGINT NOT NULL ,
  `Duration` INT NOT NULL ,
  `Age` TINYINT NULL ,
  `Job` TINYINT NULL ,
  `Sex` TINYINT NULL ,
  PRIMARY KEY (`ID`) ,
  UNIQUE INDEX `Index_ID` (`ID` ASC) ,
  INDEX `Index_Age` (`Age` ASC) ,
  INDEX `Index_Job` (`Job` ASC) ,
  INDEX `Index_Sex` (`Sex` ASC) );

