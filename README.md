# Patient Data Migrator
**Author:** Ellis Rucker - m.ellisrucker@gmail.com 

## Purpose
This program is built to parse and migrate years of data from a single spreadsheet to a newly designed
relational database. The spreadsheet in question is used to store test and patient data for prenatal
genetic testing. For redundancy, a new copy of the spreadsheet is saved every day.

(Mock Spreadsheet Example)

This system poses a number of issues:
* The date associated a case will often be changed in order to move ongoing cases closer to the
bottom of the spreadsheet with other open cases
* While the dates that samples are received are documented in physical records, they are not recorded digitally 
* A single cell often contains multiple entries and multiple forms of data
* Certain concepts are poorly defined and enforced within the spreadsheet
* The file can only be opened for editing on one computer at a time. Other useres who may be 
viewing it will not see any new changes made

## Goals
The primary goal of this program is to restore data integrity by iterating through the spreadsheet backup files... (parse elements, record dates, and organize data into 6 tables) 
* Standardize terms and concepts used within the laboratory
* Facilitate the creation of sales and testing reports
* Lay the foundation for an internal application for employee use

## Key Concepts
**Identifiers**: Every sample the lab receives is assigned a unique identifier comprising of the first 3 initials of the patient's first name, the last initial of their last name, and 7 numbers representing the year, month, and accession number associated with the sample. Patients are identified by the ID of their first submitted sample. Cases are identified by the mother’s patient ID. As such, a single ID could potentially refer to either a specific sample, patient, or case depending on the context.

**Relationships**: All patients involved with a test are assigned a tag signifying their relationship to the unborn child (M = maternal, P = paternal, C = children of mother, MP = mother's father, etc.). In the event that multiple patients share a relationship, they are differentiated by a number following the letter (e.g. C1 & C2)

**Lab Procedures**: The terms Genotype and Plasma refer to specific lab procedures used to conduct testing. The spreadsheet typically contains details such as the the month and day the procedure was conducted as well as the initials of the employee who performed it. Asterisks within a procedure cell signifies that the given procedure needs to be run in order to advance the test.



(Test & Migration Database ERD)

(Production Database ERD)
