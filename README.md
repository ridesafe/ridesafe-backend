<p align="center"><img src="https://raw.githubusercontent.com/ridesafe/project/gh-pages/ridesafe_256.jpg"></p>

[![Build Status](https://travis-ci.org/ridesafe/ridesafe-backend.svg)](https://travis-ci.org/ridesafe/ridesafe-backend)

## RideSafe
[RideSafe](http://www.ridesafe.io) is an open source project which detects bikers' falls. This is possible thanks to intelligent algorithms and data collection.

Our smartphones have accelerometers to measure acceleration and gyroscope forces of individuals, these data can be used to analyse the behaviour: when walking, running, biking and even falling !
The self learning algorithms are able to improve the detection of a fall by analysing such data.

Go to:
* [RideSafe Backend](#ridesafe-backend)
* [How it works](#how-it-works)
* [Running locally](#running-locally)
* [Our open backend](#our-open-backend)
* [Contribute](#contribute)
* [Demo](#demo)
* [Who are we](#who-are-we-)
* [Partners](#partners)
* [More](#more)
* [Contact](#contact)

## RideSafe Backend
The RideSafe backend is in charge of receiving data from clients. It is [Spring Boot](http://projects.spring.io/spring-boot/) + [Kotlin](https://kotlinlang.org/) based application backed by [Cassandra](http://cassandra.apache.org/) database

## How it works
This is a simple service that only receipts Acceleration data and store it into its database.

Examples
```bash
curl -X POST --data '{"timestamp": 1234567890, "acc_x": 0.323149, "acc_y": -2.5231, "acc_z": 9.28387237, "gyr_x": 917, "gyr_y": -46, "gyr_z": -148}' -H "Device-Id: your_device_uuid" -H "Content-type: application/json" http://localhost:8093/api/v1/acceleration
curl -X POST --data '[{"timestamp": 1234567890, "acc_x": 0.323149, "acc_y": -2.5231, "acc_z": 9.28387237, "gyr_x": 917, "gyr_y": -46, "gyr_z": -148}, ...]' -H "Device-Id: your_device_uuid" -H "Content-type: application/json" http://localhost:8093/api/v1/accelerations
```

Basically, you need to generate an unique device id (mandatory) and put it as request header to store data correctly into database.
```
... -H "Device-Id: your_device_uuid" ...
```

To make data storing valuable, sender can categorize it's activity to make predictive model more accurate.
```bash
curl -X POST --data '{"start_timestamp": 1234567890, "end_timestamp": "2334567890", "activity_type": "MOTORBIKING", "bike_type": "ROADSTER", "road_type": "CITY", "road_condition": "GOOD"}' -H "Device-Id: your_device_uuid" -H "Content-type: application/json" http://localhost:8093/api/v1/acceleration/form
```

* **start_timestamp** <-> **end_timestamp** is the range date in milli seconds of your activity.
* **activity_type** has to be chosen between the followings: MOTORBIKING, MOTORBIKE_FALLING, MOTORBIKE_PUSHING, WALKING, JOGGING, SITTING, STANDING, SLEEPING, USING_PHONE
* **bike_type** can be null or: ROADSTER, TRAIL, SPORT, TOURING, SPORT_TOURING, CUSTOM, COLLECTOR
* **road_type** can be null or: PRIVATE_ROAD, TRACK, CITY, ROAD_ONE_LANE, ROAD_MULTIPLE_LANE, FREEWAY, TRACK_RACE
* **road_condition** can be null or: GOOD, FAIR, BAD, VERY_BAD

## Running locally
Note: If you aim to use your own RideSafe backend, keep in mind that our data are open as well. You are better to use **our provided Android connector** and pushing data to [our open backend](#Open backend).

Prerequisites:
* [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Cassandra database](http://cassandra.apache.org/download/)

Cassandra schema
```SQL
CREATE KEYSPACE ridesafe WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

use ridesafe;

CREATE COLUMNFAMILY data (
device_id text,
user_id bigint,
"timestamp" bigint,
acc_x decimal,
acc_y decimal,
acc_z decimal,
gyr_x decimal,
gyr_y decimal,
gyr_z decimal,
activity_type text,
bike_type text,
road_type text,
road_condition text,
PRIMARY KEY ((device_id, user_id), "timestamp")
);

CREATE INDEX data_activity_type_idx ON data (activity_type);
CREATE INDEX data_bike_type_idx ON data (bike_type);
CREATE INDEX data_road_type_idx ON data (road_type);
CREATE INDEX data_road_condition_idx ON data (road_condition);
```

Build app
```bash
# Clone this repository
cd ridesafe-backend

# Build app
chmod +x gradlew
./gradlew build

# Run app
java -jar build/libs/rs-backend-0.1.jar

# it's now live on http://localhost:8093
```

You can configure application
```bash
cd ridesafe-backend/src/main/resources
vi application.yml

# then you have to rebuild the app
```

## Our open backend
Instead of locally install the backend, you can target our backend server which is available at the following web address:
```
https://api.ridesafe.io
```

## Contribute
There are many ways to contribute to RideSafe.
* **You are developer**: You have an idea to make RideSafe better ? You want to clean the code ?.. Just send us a Pull Request :)
* **You are data scientist**: Take a look at our activity recognition algorithms and tell us what do you think and how to improve them.
* **You are motorcycling professional**: More we can test, more accurate are algorithms and fall detection. Contact us to see how you can contribute !

## Demo
RideSafe is used into our [Nousmotards](https://play.google.com/store/apps/details?id=com.nousmotards.android) app.


## Who are we ?
RideSafe has been launched by [Nousmotards](https://www.nousmotards.com).
It is a service platform for bikers, created by [4 engineers and bikers](http://blog.nousmotards.com/2015/04/24/ouverture-du-blog-nousmotards/):
* [Romaric Philogène](https://fr.linkedin.com/in/romaricphilogene)
* [Rémi Demol](https://www.linkedin.com/in/demolremi/fr)
* [Pierre Mavro](https://fr.linkedin.com/in/pmavro/fr)
* [Ludwine Probst](https://www.linkedin.com/in/ludwineprobst)
 
We are creating a set of services tailored exclusively to the world of motorcycling!
Nousmotards app is available on Mobile ([Android](https://play.google.com/store/apps/details?id=com.nousmotards.android), iOS) and on your [browser](https://www.nousmotards.com).

## Partners
We are looking for worldwide partners specialised in motorbike and/or technology.

## More
The "Activity Recognition" project is based on the work of [Ludwine Probst](https://github.com/nivdul), which detects one type of activity from the accelerometer integrated in smartphones and a self-learning algorithm called [machine learning](https://en.wikipedia.org/wiki/Machine_learning).

The data and algorithms are available under the Apache license, this means that the changes and improvements made will be communicated to the community.
Commercial use is unrestricted.

## Contact

Contact us at [contact@ridesafe.io](mailto:contact@ridesafe.io) or [contact@nousmotards.com](mailto:contact@nousmotards.com)

Follow us: [Twitter](https://twitter.com/Nousmotards) [Facebook](https://www.facebook.com/nousmotardsapp)
