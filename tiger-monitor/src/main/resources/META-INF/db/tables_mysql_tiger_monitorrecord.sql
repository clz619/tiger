#
# Tiger seems to work best with the mysql-5.5.49 href:http://dev.mysql.com/downloads/mysql/5.5.html#downloads
#
# In Tiger-Demo, you will need to create following table

# DROP TABLE IF EXISTS Tiger_MonitorRecord;

CREATE TABLE Tiger_MonitorRecord (
	`id` bigint(12) NOT NULL AUTO_INCREMENT,
	`addTime` datetime NOT NULL,
	`updateTime` datetime NOT NULL,
	`handlerGroup` varchar(64) NOT NULL,
	`handlerName` varchar(64) NOT NULL,
	`monitorTime` datetime NOT NULL,
	`hostName` varchar(64) DEFAULT NULL,
	`totalNum` int(11) NOT NULL,
	`sucNum` int(11) NOT NULL,
	`failNum` int(11) NOT NULL,
	`avgCost` int(11) NOT NULL,
	`maxCost` int(11) NOT NULL,
	`minCost` int(11) NOT NULL,
	 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

commit;
