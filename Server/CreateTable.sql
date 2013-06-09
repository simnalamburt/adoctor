CREATE  TABLE `adoctor`.`ScreenLog` (
  `ID` INT NOT NULL AUTO_INCREMENT ,
  `Time` INT NOT NULL ,
  `Duration` INT NOT NULL ,
  `Age` INT NULL ,
  `Job` INT NULL ,
  `Sex` INT NULL ,
  PRIMARY KEY (`ID`) ,
  UNIQUE INDEX `Index_ID` (`ID` ASC) ,
  INDEX `Index_Age` (`Age` ASC) ,
  INDEX `Index_Job` (`Job` ASC) ,
  INDEX `Index_Sex` (`Sex` ASC) );
