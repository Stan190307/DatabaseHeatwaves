DROP DATABASE IF EXISTS heatwave_db;
CREATE DATABASE heatwave_db;
USE heatwave_db;

-- ---------------------------------------------------------------------
-- 1. Country
-- ---------------------------------------------------------------------
CREATE TABLE Country (
    Country_ID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL UNIQUE
);

-- ---------------------------------------------------------------------
-- 2. City
-- ---------------------------------------------------------------------
CREATE TABLE Postal_Area (
    Postal_Code VARCHAR(20) PRIMARY KEY,
    City_ID INT NOT NULL,
    FOREIGN KEY (City_ID) REFERENCES City(City_ID) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- 3. Geography
-- ---------------------------------------------------------------------
CREATE TABLE Geography (
    Location_ID INT AUTO_INCREMENT PRIMARY KEY,
    City_ID INT NOT NULL,
    Latitude DECIMAL(9, 6) NOT NULL,
    Longitude DECIMAL(9, 6) NOT NULL,
    Closest_Weather_Station VARCHAR(150),
    FOREIGN KEY (City_ID) REFERENCES City(City_ID) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- 4. Hospital
-- ---------------------------------------------------------------------
CREATE TABLE Hospital (
    Hospital_ID INT AUTO_INCREMENT PRIMARY KEY,
    Postal_Code VARCHAR(20) NOT NULL,
    Street_Address VARCHAR(255) NOT NULL,
    Capacity INT NOT NULL,
    FOREIGN KEY (Postal_Code) REFERENCES Postal_Area(Postal_Code) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- 5. Heatwave
-- Note: Prevention_ID is created here WITHOUT its foreign key.
-- The constraint is added at the bottom of this file, because
-- Heatwave and Prevention point at each other (circular reference).
-- ---------------------------------------------------------------------
CREATE TABLE Heatwave (
    Heatwave_ID INT AUTO_INCREMENT PRIMARY KEY,
    Location_ID INT NOT NULL,
    Start_Date DATE NOT NULL,
    End_Date DATE NOT NULL,
    Minimal_Temperature DECIMAL(5, 2),
    Maximum_Temperature DECIMAL(5, 2),
    Heat_Index DECIMAL(5, 2),
    FOREIGN KEY (Location_ID) REFERENCES Geography(Location_ID) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- 6. Prevention
-- ---------------------------------------------------------------------
CREATE TABLE Prevention (
    Prevention_ID INT AUTO_INCREMENT PRIMARY KEY,
    Heatwave_ID INT NOT NULL,
    Description TEXT,
    Deployment BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (Heatwave_ID) REFERENCES Heatwave(Heatwave_ID) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- 7. Infrastructure_Impact
-- ---------------------------------------------------------------------
CREATE TABLE Infrastructure_Impact (
    Impact_ID INT AUTO_INCREMENT PRIMARY KEY,
    Heatwave_ID INT NOT NULL,
    Power_Outage BOOLEAN DEFAULT FALSE,
    Water_Restrictions BOOLEAN DEFAULT FALSE,
    Transportation_Issues BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (Heatwave_ID) REFERENCES Heatwave(Heatwave_ID) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- 8. Victim
-- ---------------------------------------------------------------------
CREATE TABLE Victim (
    Victim_ID INT AUTO_INCREMENT PRIMARY KEY,
    Heatwave_ID INT NOT NULL,
    First_Name VARCHAR(100) NOT NULL,
    Last_Name VARCHAR(100) NOT NULL,
    Age INT,
    Sex VARCHAR(20),
    Postal_Code VARCHAR(20) NOT NULL,
    Street_Address VARCHAR(255),
    FOREIGN KEY (Heatwave_ID) REFERENCES Heatwave(Heatwave_ID) ON DELETE CASCADE,
    FOREIGN KEY (Postal_Code) REFERENCES Postal_Area(Postal_Code) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- 9. Injury_Type
-- ---------------------------------------------------------------------
CREATE TABLE Injury_Type (
    Injury_Type_ID INT AUTO_INCREMENT PRIMARY KEY,
    Type_Name VARCHAR(100) NOT NULL,
    Description TEXT
);

-- ---------------------------------------------------------------------
-- 10. Victim_Injury  (bridge table)
-- ---------------------------------------------------------------------
CREATE TABLE Victim_Injury (
    Victim_Injury_ID INT AUTO_INCREMENT PRIMARY KEY,
    Victim_ID INT NOT NULL,
    Injury_Type_ID INT NOT NULL,
    Hospital_ID INT NULL,
    Survivor BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (Victim_ID) REFERENCES Victim(Victim_ID) ON DELETE CASCADE,
    FOREIGN KEY (Injury_Type_ID) REFERENCES Injury_Type(Injury_Type_ID) ON DELETE CASCADE,
    FOREIGN KEY (Hospital_ID) REFERENCES Hospital(Hospital_ID) ON DELETE SET NULL
);