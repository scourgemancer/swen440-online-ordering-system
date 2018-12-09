create database javaTestDB;
use javaTestDB;
drop user 'javauser'@'localhost';
flush privileges;
CREATE USER 'javauser'@'localhost' IDENTIFIED BY 'javapass';
Grant All on javaTestDB.* TO 'javauser'@'localhost';

CREATE TABLE Log( id INT AUTO_INCREMENT PRIMARY KEY, Timestamp DATETIME NOT NULL, SKU_Code INT NOT NULL, Quantity INT NOT NULL, User_Type VARCHAR(20) NOT NULL, Type VARCHAR(20) NOT NULL );

CREATE TABLE `Product` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `SKU_Code` int(11) NOT NULL,
 `Item_Count` int(11) NOT NULL,
 `Reorder_Threshold` int(11) NOT NULL,
 `Reorder_Amount` int(11) NOT NULL,
 `Title` varchar(64) NOT NULL,
 `Description` varchar(1024) DEFAULT NULL,
 `Cost` decimal(11,2) NOT NULL,
 `Category` int(11) NOT NULL,
 PRIMARY KEY (`id`),
 UNIQUE KEY `SKU_Code` (`SKU_Code`)
);

CREATE TABLE `Category` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `Name` varchar(32) NOT NULL,
 `Description` varchar(1024) NOT NULL,
 PRIMARY KEY (`id`)
);

INSERT INTO Category (Name, Description) VALUES ('Eight Track Tapes', 'Our collection of the best 8-track tapes of era.  Listen to the BeeJees, Peter Frampton and other classics.');

INSERT INTO Category (Name, Description) VALUES ('Toys', 'Our collection of current and vintage toys.  Here youll find highly rated toys that will give years of playing pleasure.');

INSERT INTO Product VALUES (NULL, 13, 2, 1, 10, 'Dark Side of the Moon', 'A classic Pink Floyd album', 8.99, 1);

INSERT INTO Product VALUES (NULL, 14, 4, 2, 10, 'The Cars', 'The Cars is the debut album by the American new wave band the Cars. It was released on June 6, 1978 on Elektra Records. The album, which featured the three charting singles Just What I Needed, My Best Friends Girl, and Good Times Roll, as well as several album-oriented rock radio hits, was a major success for the band, remaining on the charts for 139 weeks. It has been recognized as one of the bands best albums. -- wikipedia', 4.95, 1);

INSERT INTO Product VALUES (NULL, 12, 45, 10, 50, 'Rubic''s Cube', 'A mindbending toy, turn the sides to move color tiles.  The objective is to arange the cube so that each side is one color.', 5.00, 2);