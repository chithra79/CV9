# CV9
Place sample.html in the source directory <your_windows_dir>\source
Create a destination folder <your_windows_dir>\dest
Import project to STS
Right click and Run DemoApplication.java as a springboot application.
Open http://localhost:8080 in browser.
Select sample.html from the source directory <your_windows_dir>\source and upload the file.
Check STS console for log messages
Open http://localhost:8080/h2 in browser and login.
Run select * from doc_meta_data
The meta fields in the sample.html would have been inserted in the h2 db table.

Automated Testing for DB
-----------------------
Run testfindByAuthor() test method. Junit will complete with success.
