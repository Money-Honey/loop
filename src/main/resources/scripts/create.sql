CREATE TABLE IF NOT EXISTS ADS(
  ID INT PRIMARY KEY,
  DESCRIPTION VARCHAR(255),
  URL VARCHAR (255),
  OS VARCHAR (255),
  COUNTRY VARCHAR (255)
);

Create Index IX_ADS On ADS (OS Asc, COUNTRY Asc);

INSERT INTO ADS (ID,DESCRIPTION,URL,OS,COUNTRY) VALUES (1,'funny ads','http://google.com','ios','UK');
INSERT INTO ADS (ID,DESCRIPTION,URL,OS,COUNTRY) VALUES (2,'funny ads','http://google.com','android','US');
INSERT INTO ADS (ID,DESCRIPTION,URL,OS,COUNTRY) VALUES (3,'funny ads1','http://google.com','android','US');
INSERT INTO ADS (ID,DESCRIPTION,URL,OS,COUNTRY) VALUES (4,'funny ads2','http://google.com','android','US');
INSERT INTO ADS (ID,DESCRIPTION,URL,OS,COUNTRY) VALUES (5,'funny ads3','http://google.com','android','US');
INSERT INTO ADS (ID,DESCRIPTION,URL,OS,COUNTRY) VALUES (6,'funny ads4','http://google.com','android','US');

