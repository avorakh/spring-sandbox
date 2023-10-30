#!/bin/bash
set -e

# This script is used to drop and recreate the meta-data tables
docker cp $(pwd)/../src/sql/schema-drop-postgresql.sql postgres:/docker-entrypoint-initdb.d
docker cp $(pwd)/../src/sql/schema-postgresql.sql postgres:/docker-entrypoint-initdb.d
docker cp $(pwd)/../src/sql/schema-billing.sql postgres:/docker-entrypoint-initdb.d

docker exec postgres psql -f /docker-entrypoint-initdb.d/schema-drop-postgresql.sql -U postgres
docker exec postgres psql -f /docker-entrypoint-initdb.d/schema-postgresql.sql -U postgres
docker exec postgres psql -f /docker-entrypoint-initdb.d/schema-billing.sql -U postgres