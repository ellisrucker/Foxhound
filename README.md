# Patient Data Migrator
**Author:** Ellis Rucker - m.ellisrucker@gmail.com 

## Purpose
This program is built to parse and migrate years of data from a single spreadsheet to a newly designed
relational database. The spreadsheet in question is used to store test and patient data for prenatal
genetic testing. For redundancy, a new copy of the spreadsheet is saved every day. *Table 1* below shows an example of the spreadsheet layout and how data is typically stored within it. Fictional names and identifiers are used for patients, personnel, and third-party companies.

|Date     |M patient name  |M patient ID             |P patient ID                                   |Gestation/Gender  |test type/ cost     |Referral             |A Geno          |B Geno           |1st Draw          |2nd Draw                    |3rd Draw|4th Draw|5th Draw|Result              |Confirmation|
|---------|----------------|-------------------------|-----------------------------------------------|------------------|--------------------|---------------------|----------------|-----------------|------------------|----------------------------|--------|--------|--------|--------------------|------------|
|1/7/2019 |Grace Riggs     |GRAR1901008              |GREP1901016                                    |15w               |3wk, 950            |Binghamton DNA       |1/7 CM          |1/8 CM           |A/B-1/11 JD       |                            |        |        |        |match      |            |
|4/4/2019 |Erica Tate      | ERIT1812121             |LANR1812120                                    |10w               |3wk, 950            |EverGen              | 4/8 CM         |4/9 CM           |A-4/10 JD         |KATN1904033(23w) A/B-4/11 JD|        |        |        |mismatch            |            |
|6/7/2019 |Karissa Sparks  |KARS1906031              |CARW1906032(P1) VONH1906033(P2)                |08w               |early, 1650         |QuickTest Albuquerque|6/7 CM          |6/10 P2 CM       |A/B(p1)-6/12 JD   |                            |        |        |        |P1 match P2 mismatch|KARS1907033 |
|6/10/2019|Amanda Smith    |AMAS1906035              |MARL1906036                                    |15w boy/girl twins|3 wk, 950 1wk, 950  |Binghamton DNA       |*               |                 |                  |                            |        |        |        |                    |            |
|6/11/2019|Eliza Camire    |ELIC1906040              |JACG1906041(P1) ALEO1906042(P2) brsh1906043(P2)|22w twin girls    |1wk, 1250           |QuickTest Worcester  |6/11 CM         |*                |                  |                            |        |        |        |                    |            |
|6/12/2019|Stephanie Fowler|STEF1906045* STEF1906048 |MATP1906046                                    |07w 15w           |early, 1050         |Accurate Diagnostics |                |                 |thurs-mon *       |STEF1906048                 |        |        |        |                    |            |
|6/15/2019|Sherry Carter   |(SHEC1701017) SHEC1906026|(ARTJ1701018) ANDR1906027                      |(18w boy) 11w     |(1wk, 1250) 3wk, 950|Raleigh DNA          |(1/9 BW) 6/21 CM|(1/10 BW) 6/24 CM|(A/B-1/11 JD) A/B-|                            |        |        |        |(mismatch)          |            |
|6/16/2019|Simone Craig    |SIMC1902028              |HARD1902029                                    |22w, girl         |1wk, 1250           |EverGen              |6/16 CM         |2/7 CM           |A/B-2/11 JD       |                            |        |        |        |match               |            |
|6/16/2019|Tasha Grimshaw  |TASG1906056              |HECE1906057                                    |29w girl          |1wk, 1050           |Western Diagnostics  |*               |                 |                  |                            |        |        |        |                    |            |
|6/17/2019|Jaycee Sheppard |JAYS1902030              |STAN1902031                                    |13w               |1wk, 1050           |Kentucky Testing     |6/17 CM         |6/18 CM          |A/B-6/19 JD       |                            |        |        |        |match               |            |


*Table 1.*

## Key Concepts
**Identifiers**: Every sample the lab receives is assigned a unique identifier comprising of the first 3 initials of the patient's first name, the last initial of their last name, and a 7 digit number representing the year, month, and accession number associated with the sample. Patients are identified by the ID of their first submitted sample. Cases are identified by the mother’s patient ID. As such, a single ID could potentially refer to either a specific sample, patient, or case depending on the context.

**Relationships**: All patients involved with a test are assigned a tag signifying their relationship to the unborn child (M = maternal, P = paternal, D = egg donor, MP = mother's father, etc.). In the event that multiple patients share a relationship, they are differentiated by a number following the letter (e.g. C1 & C2)

**Lab Procedures**: The terms Genotype and Plasma refer to specific lab procedures used to conduct testing. The spreadsheet typically contains details such as the month and day that the procedure was conducted as well as the initials of the employee who performed it. Asterisks within a procedure cell signifies that the given procedure needs to be run in order to advance the test.


This system poses a number of issues:
* The date associated a case will often be changed in order to move ongoing cases closer to the
bottom of the spreadsheet with other open cases
* While the dates that samples are received are documented in physical records, they are not recorded digitally 
* A single cell often contains multiple entries and multiple forms of data
* Certain concepts are poorly defined and enforced within the spreadsheet
* The file can only be opened for editing on one computer at a time. Other useres who may be 
viewing it will not see any new changes made

## Goals
The primary goal of this program is to restore data integrity by iterating through the spreadsheet backup files. As the files are read chronologically, the program parses out individual elements, records the original date that they were added, and reorganizes the data  into the six database tables shown below in *Figure 1*. 

![Development Database 1](Media/Migration%20Database%20ERD.jpg)
*Figure 1.*


**Cases vs. Tests**: The new schema draws a distinction between a Case and a Test. A Case represents a single pregnancy. A Test represents an examination performed on that pregnancy. The convention accounts for scenarios in which a patient returns for additional testing during the same pregnancy, while also keeping patient relationships consistant throughout repeated tests.


*Figure 2*, shown below, shows a group of auxillary tables that are used for data migration and debugging.


![Development Database 2](Media/Test%20Database%20ERD.jpg)
*Figure 2.*


**Filtered table**: Contains string representations of rows that will likely require manual review. Before iterating through each file, the program first reads the most recent spreadsheet and adds any row that fits certain criteria to be considered a fringe case. Such cases are then skipped over when parsing each file.

**Hash table**: Contains a hashed representation of each cell within a row that has been entered into the database. After a row has been parsed, the program stores a hash of each cell that is later used to determine which cells, if any, been altered since it was last parsed.

**Log table**: Each time a pre-existing row is updated, a log is created detailing the cells that were changed and name of the file from which it was updated.

**Error table**: To assist with debugging, the stacktrace of any exception encountered while parsing is logged along side the name of the file that triggered the error.


## Project Status
* Refactoring code
* Debugging


