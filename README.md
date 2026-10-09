# Heatwave Database

Group project for the Databases course, DSAI, Maastricht University.

This database stores information about heatwaves, temperatures, mortality, locations, prevention measures, infrastructure impact and victims.

## Project overview

Everything we did so far, week by week:

| Week | Activity | Where to find it |
|---|---|---|
| 1 | **Societal problem definition** | [`Week 1 report`](heatwave-java/Weekly%20assignments/Week%201%20Societal%20Problem%20Definition/Databases%20group%20report%20-%20Heatwaves%20(2).docx) |
| 2 | **Data modeling:** entities, relationships, ERD and normalization | [`Week 2 ERD`](heatwave-java/Weekly%20assignments/Week%202%20ERD/Assignment%202%20Databases%20(1).docx) |
| 3 | **Database implementation:** schema, CRUD, mock data and advanced queries | [`source`](heatwave-java/heatwave-java/) |
| 4 | **Stakeholder video:** what the database can answer and its limitations | [`Stakeholder video`](heatwave-java/Weekly%20assignments/Week%204%20Stakeholder%20Video/WhatsApp%20Video%202026-10-02%20at%2013.38.26.mp4) |

## Files

Main project files:

- `heatwave-java/heatwave-java/schema.sql` - creates the MySQL database and tables
- `heatwave-java/heatwave-java/src/DatabaseConnection.java` - MySQL connection
- `heatwave-java/heatwave-java/src/HeatwaveDAO.java` - CRUD operations and advanced queries
- `heatwave-java/heatwave-java/src/Main.java` - basic CRUD demonstration
- `heatwave-java/heatwave-java/src/MockDataGenerator.java` - generates the larger Week 3 mock dataset
- `heatwave-java/real life data/real_world_data.sql` - inserts the real-world data
- `heatwave-java/real life data/raw/` - files used for the real-world data integration
- `heatwave-java/real life data/raw/heatwave_joined.csv` - final joined dataset

## How to run

### 1. Create the database

Open MySQL Workbench and run:

`heatwave-java/heatwave-java/schema.sql`

This creates the `heatwave_db` database and all required tables.

Running `schema.sql` again resets the database.

### 2. Configure Java

In `DatabaseConnection.java`, replace:

`REPLACE_WITH_YOUR_OWN_PASSWORD`

with your local MySQL password.

Do not commit your real password.

### 3. Choose which data to use

For the basic CRUD example, run:

`Main.java`

For the larger Week 3 mock dataset, run:

`MockDataGenerator.java`

For the real-world Paris and Madrid dataset, run:

`heatwave-java/real life data/real_world_data.sql`

`Main.java`, `MockDataGenerator.java` and `real_world_data.sql` can contain overlapping countries and cities, so run `schema.sql` again before switching between them.

---

## Real-world data integration

For the current assignment, two complementary real-world datasets were integrated into the database.

### Eurostat

Dataset: `demo_r_mwk3_t` - weekly deaths by region.

Regions used:

- FR10 - Île-de-France
- ES30 - Comunidad de Madrid

License: **CC BY 4.0**

Downloaded: **2026-10-02**

Source: [Eurostat bulk download facility](https://ec.europa.eu/eurostat/databrowser/bulk)

### ECA&D

Daily temperature data from the European Climate Assessment & Dataset.

Elements used:

- `TX` - maximum temperature
- `TN` - minimum temperature
- `TG` - mean temperature

Stations used:

- Orly / Paris - STAID 11249
- Madrid Retiro - STAID 230

License: free for non-commercial research and education under the ECA&D data policy.

Downloaded: **2026-10-02**

Source: [ECA&D](https://www.ecad.eu)

The datasets are complementary because Eurostat provides mortality data while ECA&D provides temperature measurements.

## Data integration and cleaning

The final dataset contains **471 weekly observations**:

- Paris: **236**
- Madrid: **235**

The following cleaning and transformations were done:

- missing ECA&D values (`-9999`) were removed
- missing Eurostat values (`:`) were handled
- provisional Eurostat values were handled
- Eurostat ISO weeks were converted to dates
- daily ECA&D temperature data was grouped into weeks
- only weeks with at least five valid temperature days were kept
- city and region names were standardized
- duplicate city-week records were checked

The years **2020 and 2021** were excluded because mortality during those years was strongly affected by COVID-19.

### Mapping to the database

- Eurostat weekly deaths → `Mortality`
- ECA&D TN → `Minimal_Temperature`
- ECA&D TX → `Maximum_Temperature`
- ECA&D TG → `Avg_Temperature`
- `Heat_Index` → `0`

There was no heat-index value available in the selected datasets, so `Heat_Index` was set to `0`.

Each `(city, week)` combination is stored as one row in `Heatwave`.

These rows represent weekly observations and are not necessarily separate heatwave events.

## Schema and constraint checks

Before inserting the real-world data, the records were checked against the database constraints.

We checked:

- `End_Date >= Start_Date`
- `Mortality >= 0`

All **471 records** passed these checks.

## Query validation

The Week 3 advanced queries were run again after adding the real-world data.

### Query 1 - Heatwave statistics by country

The query still works after adding the real-world data.

France and Spain now contain many weekly observations from Paris and Madrid.

The count represents weekly records rather than separate heatwave events.

### Query 2 - Temperatures above the country average

The query still works with the real-world temperature data.

Paris and Madrid were used to check that the joins and temperature values work correctly.

### Query 3 - Injury survival statistics

This query still works, but the real-world datasets do not contain individual victim or injury data.

Because of this, the injury statistics are based on the mock data.

## Normalization

The schema was checked again after integrating the real-world data.

### Victim

Previously, storing both `City_ID` and `Postal_Code` in `Victim` would create redundant information because a postal code already determines the city.

The current structure only stores `Postal_Code`.

The city can be found through:

`Victim → Postal_Area → City`

### Prevention

`Prevention` references its related `Heatwave`.

The location can already be found through:

`Prevention → Heatwave → Geography`

A separate `Location_ID` in `Prevention` is therefore not needed.

### Heatwave

`Avg_Temperature` and `Mortality` are stored in `Heatwave` because they describe the weekly observation represented by that row.

Keeping these values does not by itself create a transitive dependency.

## Limitations

The current real-world integration has several limitations:

- only Paris and Madrid are included
- weekly mortality does not mean that every death was caused by heat
- city-week rows are observations and not necessarily separate heatwave events
- no real-world individual victim or injury data is included
- no real-world prevention data is included
- no real-world infrastructure-impact data is included
- no real heat-index source was included

## Future work

Possible improvements include:

- adding more cities and countries
- using officially classified heatwave events
- adding heat-attributable mortality data
- adding real hospital and injury data
- adding prevention and infrastructure-impact data
- adding a real heat-index dataset

## Data publication

The final MySQL database dump has been published on Zenodo.

**DOI:** [10.5281/zenodo.23270458](https://doi.org/10.5281/zenodo.23270458)

The published dump contains the database schema and 471 real-world weekly observations for Paris and Madrid. It contains no individual victim records or personal data.