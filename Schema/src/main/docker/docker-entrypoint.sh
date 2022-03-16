#!/bin/bash

set -e

psql=( psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" --host "$POSTGRES_HOST" )

until pg_isready -h $POSTGRES_HOST
do
	echo "Sleep 1s and try again..."
	sleep 1
done

for f in scripts/*; do
	case "$f" in
		*.sh)     echo "$0: running $f"; . "$f" ;;
		*.sql)    echo "$0: running $f"; "${psql[@]}" -f "$f"; echo ;;
		*.sql.gz) echo "$0: running $f"; gunzip -c "$f" | "${psql[@]}"; echo ;;
		*)        echo "$0: ignoring $f" ;;
	esac
	echo
done
