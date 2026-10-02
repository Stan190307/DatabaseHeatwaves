# Real-world data integration

This folder contains the real-world data we used to test our database.

## 2. Integration, cleaning and constraint checks

For the real-world data we used two sources: Eurostat mortality data and ECA&D temperature data. The data was cleaned and combined before inserting it into the database.

The final dataset contains 471 weekly records for Paris and Madrid.

### Schema and constraints

Before inserting the data, we checked if the records matched the constraints in the `Heatwave` table.

We checked:
- `End_Date >= Start_Date`
- `Mortality >= 0`

All 471 records passed these checks, so no changes to the schema were needed.

### Missing data

The two datasets used different values for missing data.

Eurostat uses `:` for missing values. It can also use a `p` flag for provisional values. These were handled during the cleaning process.

ECA&D uses `-9999` for missing temperature values. Rows with missing temperature data or missing quality information were removed.

### Dates

Eurostat gives mortality data per ISO week, while ECA&D gives temperature data per day.

The ISO weeks were converted to actual dates. The daily temperature data was then grouped into weeks so both datasets could be combined.

### Duplicates

The datasets were joined using city and ISO week.

The final combined dataset contains 471 unique city-week records.

### Naming

City and region names were kept consistent between the two datasets so they could be joined correctly.

Paris was linked to Île-de-France and Madrid to Comunidad de Madrid.

### Transformations

The ECA&D temperature data was daily, so it had to be converted to weekly values.

TX was used for the weekly maximum temperature, TN for the weekly minimum temperature and TG for the weekly average temperature.

Only weeks with at least five valid days of temperature data were kept.

After that, the temperature data was joined with the mortality data using city and ISO week.

This resulted in:
- 236 records for Paris
- 235 records for Madrid

### Other decisions

The years 2020 and 2021 were excluded because mortality in those years was strongly affected by COVID-19.

There was no real-world source for `Heat_Index` in the selected datasets, so this value was set to `0`.

