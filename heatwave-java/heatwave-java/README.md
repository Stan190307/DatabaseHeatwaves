# DatabaseHeatwaves

Database project about heatwaves and their impact on cities, infrastructure and people.

The repository contains the database schema, Java code, mock data, previous weekly assignments and the real-world data integration.

---

## Project structure

- `heatwave-java/heatwave-java/schema.sql`  
  Creates the complete MySQL database schema.

- `heatwave-java/heatwave-java/src/`  
  Contains the Java database connection, DAO, main program and mock data generator.

- `heatwave-java/real life data/`  
  Contains the real-world datasets and SQL file used to insert the cleaned data.

- `heatwave-java/Weekly assignments/`  
  Contains the previous assignments, including the societal problem, ERD and stakeholder video.

---

## Setup

1. Open MySQL Workbench.
2. Run `schema.sql` to create the `heatwave_db` database.
3. In `DatabaseConnection.java`, replace `REPLACE_WITH_YOUR_OWN_PASSWORD` with your local MySQL password.
4. Run the Java project to test the database connection and CRUD operations.

To add the real-world dataset:

1. Run `schema.sql`.
2. Run `real life data/real_world_data.sql`.
3. Check the inserted data using the example queries in `HeatwaveDAO.java`.

The real-world SQL file creates the required Paris and Madrid records before inserting the weekly heatwave data.

---

# Real-world data integration

For the real-world data we used two complementary datasets:

- **Eurostat** for weekly mortality data
- **ECA&D** for daily temperature data

The datasets contain different information and were combined using location and week.

The final dataset contains **471 weekly records** for Paris and Madrid.

Each `(city, week)` combination is stored as one row in `Heatwave`.

These rows represent weekly observations and should not necessarily be interpreted as 471 separate heatwave events.

---

## Data sources

### Eurostat mortality data

**Dataset:** `demo_r_mwk3_t` – weekly deaths by region

Regions used:

- FR10 – Île-de-France
- ES30 – Comunidad de Madrid

Source: Eurostat bulk download facility

License: **CC BY 4.0**

Downloaded: **2026-10-02**

The weekly death count is stored as `Mortality`.

---

### ECA&D temperature data

Daily blended temperature data from the European Climate Assessment & Dataset.

Stations used:

- Orly / Paris – STAID 11249
- Madrid Retiro – STAID 230

Elements used:

- `TX` – maximum temperature
- `TN` – minimum temperature
- `TG` – mean temperature

License: free for non-commercial research and education under the ECA&D data policy.

Downloaded: **2026-10-02**

---

## Mapping to the database

The datasets were combined and mapped to the `Heatwave` table as follows:

- Eurostat weekly deaths → `Mortality`
- ECA&D TN → `Minimal_Temperature`
- ECA&D TX → `Maximum_Temperature`
- ECA&D TG → `Avg_Temperature`
- `Heat_Index` → `0`

There was no heat-index value available in the selected datasets, so `Heat_Index` is set to `0` for the real-world records.

---

# Data cleaning and transformation

The two datasets use different formats, so they were cleaned before being combined.

## Missing data

Eurostat uses `:` for missing values and may contain a `p` flag for provisional values.

ECA&D uses `-9999` for missing temperature measurements.

Rows with unusable temperature values were removed before aggregation.

---

## Date formatting

Eurostat reports mortality per ISO week.

ECA&D reports temperature per day.

Eurostat ISO weeks were converted into start and end dates, while the ECA&D daily measurements were grouped into weekly values.

---

## Temperature transformation

For every week:

- TX was used for the weekly maximum temperature
- TN was used for the weekly minimum temperature
- TG was averaged for the weekly average temperature

Only weeks containing at least five valid days of temperature data were kept.

---

## Duplicates

The datasets were joined using city and ISO week.

The final data contains **471 unique city-week records**:

- Paris: **236**
- Madrid: **235**

---

## Naming conventions

Names were made consistent before joining the datasets.

Paris was linked to **Île-de-France** and Madrid to **Comunidad de Madrid**.

---

## Other cleaning decisions

The years **2020 and 2021** were excluded because mortality during these years was strongly affected by COVID-19 and would make comparison with normal years less meaningful.

---

# Schema and constraint checks

Before inserting the real-world data, the data was checked against the database schema.

Important checks included:

- `End_Date >= Start_Date`
- `Mortality >= 0`

All 471 real-world records passed these checks.

The current schema also contains the required `City` table and supports the real-world attributes:

- `Avg_Temperature`
- `Mortality`

---

# Query validation

The Week 3 queries were tested again after inserting the real-world data.

## Query 1 – Heatwave statistics by country

The query still produces results after adding the real-world data.

France and Spain now contain many more rows because the real-world dataset contains weekly observations for Paris and Madrid.

An important limitation is that the count represents database records rather than necessarily separate heatwave events.

The mortality values are weekly mortality counts from Eurostat.

---

## Query 2 – Temperatures above the country average

The query correctly returns heatwave records where the maximum temperature is greater than or equal to the average maximum temperature for that country.

The real-world Paris and Madrid records were used to verify that the joins and temperature values work correctly.

---

## Query 3 – Injury survival statistics

The query still works, but the real-world datasets do not contain individual victim or injury information.

Therefore this query continues to use the existing mock victim and injury data.

---

# Normalization check

The database was checked again after integrating the real-world data.

## Victim

Previously, storing both `City_ID` and `Postal_Code` in `Victim` would create unnecessary redundancy because the city can already be determined through the postal area.

The current schema stores only `Postal_Code` in `Victim`.

The city can be found through:

`Victim -> Postal_Area -> City`

This avoids storing the same relationship twice.

---

## Prevention

`Prevention` only stores its relationship to `Heatwave`.

The location of the prevention can already be determined through:

`Prevention -> Heatwave -> Geography`

Therefore a separate `Location_ID` in `Prevention` is not required.

---

## Heatwave

`Avg_Temperature` and `Mortality` are kept in the `Heatwave` table because they describe the weekly observation represented by that row.

Keeping these attributes does not by itself create a transitive dependency.

After the identified redundant relationships were removed, the schema remains consistent with the real-world data integration.

---

# Limitations

The real-world integration still has several limitations.

The records represent **weekly city observations**, rather than individually identified heatwave events.

The Eurostat mortality value represents **weekly deaths**, and does not prove that every death was caused directly by heat.

No real-world source was included for:

- individual victims
- injury types
- infrastructure impacts
- prevention measures
- heat index

These parts of the database therefore still rely on mock data or default values.

The selected real-world dataset also only covers Paris and Madrid.

---

# Future work

Possible improvements include:

- adding real heatwave event classifications instead of treating every week as an observation;
- using mortality specifically attributed to heat;
- adding more cities and countries;
- finding real-world injury and hospital data;
- adding real prevention and infrastructure-impact data;
- adding a real heat-index source.

---

## Modifying queries

Queries can be added or changed in:

`heatwave-java/heatwave-java/src/HeatwaveDAO.java`

When changing the schema, make sure that the Java DAO, mock data and real-world data SQL remain consistent with the updated tables.