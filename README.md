# DJSQL

DJSQL is an open source library that is built on top of Java 1.8 version. The library is getting the most use of lambda expressions to build dynamic queries. If your query is getting slower because of the too many joins that you may not need, JSQL is your way to customize that. You can enable/disable joins, conditional statement, ordering, or even grouping using the JSQL. JSQL was released to the public on 09/28/2016 to start using/contributing to it. If you are a user of the library, please feel free to pass your feedback to us. If you like to contribute to the library or come up with ideas, please let us know and we will get you onboard for this. There is a list of pending items that are waiting for your contribution. 

Pending Updates:

  1- Add outer joins <br />
  2- Change String query to StringBuilder  <br />
  3- Add inline on statement with the join statement  <br />
  4- Fix the condition methods to accept (paramName, Operator, Value) other than (paramName, Value, Operator)  <br />
  5- Add order by  <br />
  6- Add Group By  <br />
  7- Add conditional columns on the Select clause  <br />

Note: Initially this libarary was built on top of hibernate framework & sybase database. Building queries and assigning parameters has been well tested; however it was not tested in any other data servers or datasource frameworks. There will be efforts on the way to make this libarary more generic and make it work under any other frameworks or data servers.
