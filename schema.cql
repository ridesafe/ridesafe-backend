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

CREATE COLUMNFAMILY device (
user_id bigint,
device_id text,
device_brand text,
device_model text,
device_raw_model text,
"timestamp" bigint,
PRIMARY KEY ((user_id), device_id)
);