# send by one message at a time

# run from ./shop_kafka_producer directory
# Example:
# /shop_kafka_producer$ ./doc/send_many_messages.sh

max=100
rm -f many.json
for i in $(seq 1 $max)
do
#    u = `uuidgen`
#    echo $u
    echo $(uuidgen) 
#   ./doc/run-producer.sh product_ext_dto_topic < ./doc/product.json
    echo "{\"id\": \"$(uuidgen)\", \"name\": \"NAME$i\", \"description\": \"DESCRIPTION$i\"}" >> many.json
#    echo "\"{\"n\": $i, \"name\": \"NAME_$i\", \"groupDtoN\":100}\"" > ./many_messages.json
#  echo "{\"n\": $i, \"name\": \"NAME_$i\", \"groupDtoN\":100}"
#  echo "{\"n\": 1, \"name\": \"NAME_1\", \"groupDtoN\":100}"
#    ./doc/run-producer.sh product_ext_dto_topic < echo "{\"n\": $i, \"name\": \"NAME_$i\", \"groupDtoN\":100}"
#  echo "{'n': 1, 'name': 'NAME_1', 'groupDtoN':100}\n{'n': 2, 'name': 'NAME_2', 'groupDtoN':200}" | ./doc/run-producer.sh product_ext_dto_topic
done
jq -rc . many.json | /home/vasi/tools/kafka/bin/kafka-console-producer.sh --broker-list 192.168.1.20:9092 --topic camel-integration