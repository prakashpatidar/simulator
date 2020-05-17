# simulator
####Simulate Data based on registered json Configuration
##### Algorithm for Data Simulation:
###### UUID
###### Random Long Number between Range
###### Random Alpha Numeric String for range of length
###### Timestamp Range
###### Geo Data
##### Sink Supported:
###### Console
###### File
###### Kafka
###### ElasticSearch
###### HDFS
###### Cassandra
    Example Schema to be created in cassandra based on config
    drop keyspace cdr;
    CREATE KEYSPACE cdr WITH replication = {'class': 'NetworkTopologyStrategy', 'datacenter1': '1'}  AND durable_writes = false;
    CREATE TABLE cdr.calls (
        from_number bigint PRIMARY KEY,
        call_duration bigint,
        call_date text,
        call_time bigint,
        from_imei bigint,
        from_loc text,
        to_imei bigint,
        to_loc text,
        to_number bigint
    ) WITH bloom_filter_fp_chance = 0.01
        AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
        AND comment = ''
        AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
        AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
        AND crc_check_chance = 1.0
        AND dclocal_read_repair_chance = 0.1
        AND default_time_to_live = 0
        AND gc_grace_seconds = 864000
        AND max_index_interval = 2048
        AND memtable_flush_period_in_ms = 0
        AND min_index_interval = 128
        AND read_repair_chance = 0.0
        AND speculative_retry = '99PERCENTILE';

