#!/bin/bash

ROOT=$(pwd)
CONF=$ROOT/conf/server.conf
bin=chat_system
pid=''
ip=$(awk -F: '/^IP/{print $NF}' $CONF)
port=$(awk -F: '/^PORT/{print $NF}' $CONF)

proc=$(basename $0)
function usage()
{
	printf "Usage:\n\t%s [start(-s)|stop(-t)|restart(-rt)|status(-st)]\n" "$proc"
}

is_exist()
{
	pid=$(pidof $bin)
	if [ -z "$pid" ];then
		return 1
	else
		return 0
	fi
}

function server_status()
{
	if is_exist; then
		echo "server is running... pid: $pid"
	else
		echo "server is not running..."
	fi
}
function server_start()
{
	if is_exist; then
		echo "server is running... pid: $pid"
	else
		$ROOT/bin/$bin $ip $port
		echo "server running ... done"
	fi
}

function server_stop()
{
	if is_exist; then
		kill -9 $pid
		echo "server stop ... done"
	else
		echo "server is not exist!"
	fi
}

function server_restart()
{
	server_stop
	server_start
}


if [ $# -ne 1 ];then
	usage;
	exit 1
fi

case $1 in
	start | -s )
		server_start
	;;
	stop | -t )
		server_stop
	;;
	restart | -rt )
		server_restart
	;;
	status | -st )
		server_status
	;;
	* )
	usage
	exit 2
	;;
esac
