## Smoke tests for local setup
Validate schema and functionality of stored procedures.
Error cases are covered in DAO integration tests.

#### Instructions
- Setup local database
- mysql -uroot -p < ../setup.sql
- pip install -r requirements.txt
- robot database-test.robot
