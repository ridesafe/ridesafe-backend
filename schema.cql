CREATE KEYSPACE ridesafe WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

use ridesafe;

CREATE COLUMNFAMILY acceleration (
device_id text,
user_id bigint,
"timestamp" bigint,
x decimal,
y decimal,
z decimal,
activity_type text,
bike_type text,
road_type text,
road_condition text,
PRIMARY KEY ((device_id, user_id), "timestamp")
);

CREATE INDEX activity_type_idx ON acceleration (activity_type);
CREATE INDEX bike_type_idx ON acceleration (bike_type);
CREATE INDEX road_type_idx ON acceleration (road_type);
CREATE INDEX road_condition_idx ON acceleration (road_condition);