DROP DATABASE IF EXISTS heatwave_db;
CREATE DATABASE heatwave_db;
USE heatwave_db;

-- ---------------------------------------------------------------------
-- 1. Country
-- ---------------------------------------------------------------------
CREATE TABLE Country (
    Country_ID   INT          NOT NULL AUTO_INCREMENT,
    Name         VARCHAR(100) NOT NULL,
    PRIMARY KEY (Country_ID),
    UNIQUE (Name)
);

-- ---------------------------------------------------------------------
-- 2. City
-- ---------------------------------------------------------------------
CREATE TABLE City (
    City_ID      INT          NOT NULL AUTO_INCREMENT,
    Name         VARCHAR(100) NOT NULL,
    Region       VARCHAR(100),
    Country_Code INT          NOT NULL,
    PRIMARY KEY (City_ID),
    FOREIGN KEY (Country_Code) REFERENCES Country(Country_ID)
);

-- ---------------------------------------------------------------------
-- 3. Geography
-- ---------------------------------------------------------------------
CREATE TABLE Geography (
    Location_ID             INT          NOT NULL AUTO_INCREMENT,
    City_ID                 INT          NOT NULL,
    Latitude                DOUBLE,
    Longitude               DOUBLE,
    Closest_Weather_Station VARCHAR(150),
    PRIMARY KEY (Location_ID),
    FOREIGN KEY (City_ID) REFERENCES City(City_ID)
);

-- ---------------------------------------------------------------------
-- 4. Hospital
-- ---------------------------------------------------------------------
CREATE TABLE Hospital (
    Hospital_ID    INT          NOT NULL AUTO_INCREMENT,
    City_ID        INT          NOT NULL,
    Street_Address VARCHAR(200),
    Postal_Address VARCHAR(20),
    Capacity       INT,
    PRIMARY KEY (Hospital_ID),
    FOREIGN KEY (City_ID) REFERENCES City(City_ID),
    CHECK (Capacity >= 0)
);

-- ---------------------------------------------------------------------
-- 5. Heatwave
-- Note: Prevention_ID is created here WITHOUT its foreign key.
-- The constraint is added at the bottom of this file, because
-- Heatwave and Prevention point at each other (circular reference).
-- ---------------------------------------------------------------------
CREATE TABLE Heatwave (
    Heatwave_ID         INT  NOT NULL AUTO_INCREMENT,
    Location_ID         INT  NOT NULL,
    Start_Date          DATE NOT NULL,
    End_Date            DATE NOT NULL,
    Minimal_Temperature DOUBLE,
    Maximum_Temperature DOUBLE,
    Avg_Temperature     DOUBLE,
    Mortality           INT,
    Heat_Index          DOUBLE,
    Prevention_ID       INT  NULL,
    PRIMARY KEY (Heatwave_ID),
    FOREIGN KEY (Location_ID) REFERENCES Geography(Location_ID),
    CHECK (End_Date >= Start_Date),
    CHECK (Mortality >= 0)
);

-- ---------------------------------------------------------------------
-- 6. Prevention
-- ---------------------------------------------------------------------
CREATE TABLE Prevention (
    Prevention_ID INT          NOT NULL AUTO_INCREMENT,
    Heatwave_ID   INT          NOT NULL,
    Location_ID   INT          NOT NULL,
    Description   VARCHAR(500),
    Deployment    BOOLEAN      NOT NULL DEFAULT FALSE,
    PRIMARY KEY (Prevention_ID),
    FOREIGN KEY (Heatwave_ID) REFERENCES Heatwave(Heatwave_ID),
    FOREIGN KEY (Location_ID) REFERENCES Geography(Location_ID)
);

-- ---------------------------------------------------------------------
-- 7. Infrastructure_Impact
-- ---------------------------------------------------------------------
CREATE TABLE Infrastructure_Impact (
    Impact_ID            INT     NOT NULL AUTO_INCREMENT,
    Heatwave_ID          INT     NOT NULL,
    Power_Outage         BOOLEAN NOT NULL DEFAULT FALSE,
    Water_Restrictions   BOOLEAN NOT NULL DEFAULT FALSE,
    Transportation_Issues BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (Impact_ID),
    FOREIGN KEY (Heatwave_ID) REFERENCES Heatwave(Heatwave_ID)
);

-- ---------------------------------------------------------------------
-- 8. Victim
-- ---------------------------------------------------------------------
CREATE TABLE Victim (
    Victim_ID      INT         NOT NULL AUTO_INCREMENT,
    Heatwave_ID    INT         NOT NULL,
    First_Name     VARCHAR(100),
    Last_Name      VARCHAR(100),
    Age            INT,
    Sex            VARCHAR(10),
    City_ID        INT         NOT NULL,
    Street_Address VARCHAR(200),
    Postal_Code    VARCHAR(20),
    PRIMARY KEY (Victim_ID),
    FOREIGN KEY (Heatwave_ID) REFERENCES Heatwave(Heatwave_ID),
    FOREIGN KEY (City_ID) REFERENCES City(City_ID),
    CHECK (Age >= 0 AND Age <= 120),
    CHECK (Sex IN ('Male', 'Female', 'Other'))
);

-- ---------------------------------------------------------------------
-- 9. Injury_Type
-- ---------------------------------------------------------------------
CREATE TABLE Injury_Type (
    Injury_Type_ID INT          NOT NULL AUTO_INCREMENT,
    Type_Name      VARCHAR(100) NOT NULL,
    Description    VARCHAR(500),
    PRIMARY KEY (Injury_Type_ID),
    UNIQUE (Type_Name)
);

-- ---------------------------------------------------------------------
-- 10. Victim_Injury  (bridge table)
-- ---------------------------------------------------------------------
CREATE TABLE Victim_Injury (
    Victim_Injury_ID INT     NOT NULL AUTO_INCREMENT,
    Victim_ID        INT     NOT NULL,
    Injury_Type_ID   INT     NOT NULL,
    Hospital_ID      INT,
    Survivor         BOOLEAN NOT NULL,
    PRIMARY KEY (Victim_Injury_ID),
    FOREIGN KEY (Victim_ID)      REFERENCES Victim(Victim_ID),
    FOREIGN KEY (Injury_Type_ID) REFERENCES Injury_Type(Injury_Type_ID),
    FOREIGN KEY (Hospital_ID)    REFERENCES Hospital(Hospital_ID)
);

-- ---------------------------------------------------------------------
-- Circular foreign key, added last
-- ---------------------------------------------------------------------
ALTER TABLE Heatwave
    ADD CONSTRAINT fk_heatwave_prevention
    FOREIGN KEY (Prevention_ID) REFERENCES Prevention(Prevention_ID);
