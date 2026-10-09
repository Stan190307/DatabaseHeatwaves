# Heatwave Database

Group project for the Databases course, DSAI, Maastricht University.

This database stores information about heatwaves, their temperatures, mortality, locations, prevention measures, infrastructure impact and victims.

## Project overview

Everything we did so far, week by week:

| Week | Activity | Where to find it |
|---|---|---|
| 1 | **Societal problem definition** | [`Week 1 report`](heatwave-java/Weekly%20assignments/Week%201%20Societal%20Problem%20Definition/Databases%20group%20report%20-%20Heatwaves%20(2).docx) |
| 2 | **Data modeling:** entities, relationships, ERD and normalization | [`Week 2 ERD`](heatwave-java/Weekly%20assignments/Week%202%20ERD/Assignment%202%20Databases%20(1).docx) |
| 3 | **Database implementation:** schema, CRUD, mock data and advanced queries | [`source`](heatwave-java/heatwave-java/) |
| 4 | **Stakeholder video and real-world data integration** | [`real life data`](heatwave-java/real%20life%20data/) |

### Week 4: Stakeholder video

Our Week 4 stakeholder video can be found here:

[`Stakeholder video`](heatwave-java/Weekly%20assignments/Week%204%20Stakeholder%20Video/WhatsApp%20Video%202026-10-02%20at%2013.38.26.mp4)

## Files

Main project files:

- `heatwave-java/heatwave-java/schema.sql` - creates the MySQL database and all tables
- `heatwave-java/heatwave-java/src/DatabaseConnection.java` - MySQL connection
- `heatwave-java/heatwave-java/src/HeatwaveDAO.java` - CRUD operations and advanced queries
- `heatwave-java/heatwave-java/src/Main.java` - tests the database operations
- `heatwave-java/heatwave-java/src/MockDataGenerator.java` - generates mock data
- `heatwave-java/real life data/real_world_data.sql` - inserts the real-world data
- `heatwave-java/real life data/raw/` - temperature and mortality data used for the integration
- `heatwave-java/real life data/raw/heatwave_joined.csv` - final joined real-world dataset

## How to run

### 1. Create the database

Open MySQL Workbench and run:

`heatwave-java/heatwave-java/schema.sql`

This creates the `heatwave_db` database and all required tables.

### 2. Configure Java

In `DatabaseConnection.java`, replace:

`REPLACE_WITH_YOUR_OWN_PASSWORD`

with your local MySQL password.

### 3. Load the real-world data

Run:

`heatwave-java/real life data/real_world_data.sql`

This adds the Paris and Madrid records and inserts the real-world weekly observations.

### 4. Run the Java project

Run `Main.java` to test the connection and CRUD/query functionality.

---

## Real-world data

Two complementary datasets were used.

### Eurostat

Eurostat dataset `demo_r_mwk3_t` was used for weekly mortality data.

Regions:

- FR10 - Île-de-France
- ES30 - Comunidad de Madrid

License: **CC BY 4.0**

Downloaded: **2026-10-02**

Source: [Eurostat bulk download facility](https://ec.europa.eu/eurostat/databrowser/bulk)

### ECA&D

ECA&D daily temperature data was used for:

- `TX` - maximum temperature
- `TN` - minimum temperature
- `TG` - mean temperature

Stations:

- Orly / Paris - STAID 11249
- Madrid Retiro - STAID 230

Downloaded: **2026-10-02**

Source: [ECA&D](https://www.ecad.eu)

The two datasets are complementary: Eurostat provides mortality data while ECA&D provides temperature measurements.

## Data integration and cleaning

The final dataset contains **471 weekly observations**:

- Paris: **236**
- Madrid: **235**

Cleaning included:

- removing missing ECA&D values (`-9999`)
- handling missing and provisional Eurostat values
- converting Eurostat ISO weeks to dates
- aggregating daily temperature measurements into weekly values
- keeping weeks with at least five valid temperature days
- standardizing city and region names
- checking for duplicate city-week records

The years **2020 and 2021** were excluded because mortality during those years was strongly affected by COVID-19.

### Mapping to the database

- Eurostat weekly deaths → `Mortality`
- ECA&D TN → `Minimal_Temperature`
- ECA&D TX → `Maximum_Temperature`
- ECA&D TG → `Avg_Temperature`
- `Heat_Index` → `0` because no heat-index source was included

Each `(city, week)` is stored as one row in `Heatwave`.

These rows represent weekly observations and are not necessarily individual heatwave events.

## Query validation

The Week 3 queries were run again after adding the real-world data.

- **Heatwave statistics by country:** now includes the Paris and Madrid weekly records
- **Temperatures above country average:** successfully works with the real temperature data
- **Injury survival statistics:** still uses mock data because the real-world datasets contain no individual victim or injury records

## Normalization

The schema was checked again after integrating the real-world data.

### Victim

`Victim` stores `Postal_Code` instead of storing both `Postal_Code` and `City_ID`.

The city can be found through:

`Victim → Postal_Area → City`

This avoids redundant data.

### Prevention

`Prevention` references the related `Heatwave`.

Its location can already be determined through:

`Prevention → Heatwave → Geography`

Therefore a separate `Location_ID` is not needed.

### Heatwave

`Avg_Temperature` and `Mortality` are kept in `Heatwave` because they describe the weekly observation represented by that row.

They do not create a transitive dependency by themselves.

## Limitations

The current real-world integration has several limitations:

- only Paris and Madrid are included
- weekly mortality does not mean that every death was caused by heat
- city-week rows are observations, not necessarily separate heatwave events
- no real-world victim or injury data is included
- no real-world prevention or infrastructure-impact data is included
- no real heat-index source was included

## Future work

Possible improvements include adding:

- more cities and countries
- officially classified heatwave events
- heat-attributable mortality data
- hospital and injury data
- prevention and infrastructure data
- a real heat-index dataset