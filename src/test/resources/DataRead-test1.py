import json
from pykafka import KafkaClient
import sys

client = KafkaClient(hosts='192.168.160.103:9093')
topic = client.topics['esp24_AllSensorData']
producer = topic.get_sync_producer()

data = {"firefighters":[{"CO": int(sys.argv[1]), "temp": 40, "hum": 30, "bat": 50,
						"lat": 40.06483992, "long": -8.16039721, "alt": 1132,
						"hr": 70},
						{"CO": 100, "temp": 30, "hum": 35, "bat": 60,
						"lat": 40.06469093, "long": -8.16050738, "alt": 1139,
						"hr": 60},
						{"CO": 100, "temp": 35, "hum": 25, "bat": 40,
						"lat": 40.06479192, "long": -8.1604327, "alt": 1142,
						"hr": 65}],
						"alerts":[]}

producer.produce(json.dumps(data).encode())
