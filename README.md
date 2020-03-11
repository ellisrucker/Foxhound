# Patient Data Migrator
Author: Ellis Rucker - m.ellisrucker@gmail.com 


## Purpose
This program is built to parse and migrate years of data from a single spreadsheet to a newly designed
relational database. The spreadsheet in question is used to store test and patient data for prenatal
genetic testing. For redundancy, a new copy of the spreadsheet is saved every day.

(Mock Spreadsheet Example)

This system poses a number of issues:
* The date associated a case will often be changed in order to move ongoing cases closer to the
bottom of the spreadsheet with other open cases
* A single cell often contains multiple entries and multiple forms of data
* Certain concepts are poorly defined and enforced within the spreadsheet
* The file can only be opened for editing on one computer at a time. Other useres who may be 
viewing it will not see any new changes made

## Goals
* Restore data integrity
* Standardize terms and concepts used within the laboratory
* Facilitate the creation of sales and testing reports
* Lay the foundation for an internal application for employee use

(Test & Migration Database ERD)

(Production Database ERD)
