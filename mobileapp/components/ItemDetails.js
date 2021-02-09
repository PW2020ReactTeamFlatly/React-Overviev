import React, {  useEffect,useState } from "react";
import { StyleSheet, Text, View } from "react-native";
import { Tile, ListItem, Button } from 'react-native-elements';
import { Icon } from 'react-native-elements';
import axios from 'axios';


export default function Details(props) {
  const [reservations, setReservations] = useState([]);
  const [details, setDetails] = useState({});


  useEffect(() => {
    async function fetchData() {
        try{
            const res = await axios.get('http://flatly.us-east-2.elasticbeanstalk.com/flats/mobile/res/' + props.id);
            
            setReservations(res.data);
            console.log(res.data);
            const det = await axios.get('http://flatly.us-east-2.elasticbeanstalk.com/flats/mobile/' + props.id);
            setDetails(det.data);
        } catch (error){
            console.error(error);
        }
    }
    fetchData();
},[setReservations,setDetails]);  
  return (
      <View>
      <Tile
      imageSrc={{
        uri: 'http://flatly.us-east-2.elasticbeanstalk.com/flats/mobile/'+props.id +'/photo'
      }}
      title={details.name}
      contentContainerStyle={{ height: 150 }}
    >
      <View
        style={{ flex: 1,   }}
      >
        <Text >{details.city + ' ' + details.address}</Text>
        <Text>{details.pricePerNight} $</Text>
        <Text>{details.rating}/10</Text>
      </View>
    </Tile>
    <View>
    <Text h1>Reservations List</Text>
 {
  reservations ? reservations.map((l) => (
    <ListItem  bottomDivider>
      <ListItem.Content>
        <ListItem.Title>{l.customerName}</ListItem.Title>
        <ListItem.Subtitle>{'Start   - ' + l.startDateTime  + '\nLeave - ' + l.endDateTime}</ListItem.Subtitle>
      </ListItem.Content>
      <Button
        onPress={async (e)=> {
          try{
            await axios.delete('http://flatly.us-east-2.elasticbeanstalk.com/reservations/mobile/' + l.id);
            const res = await axios.get('http://flatly.us-east-2.elasticbeanstalk.com/flats/mobile/res/' + props.id);
            setReservations(res.data);
        } catch (error){
            console.error(error);
        }

        }}
        icon={

          <Icon
            name="delete"
            color="white"
          />
        }

      />

    </ListItem>
  )):null
 }
</View>
</View>
    );
  }
  const styles = StyleSheet.create({
    container: {
      flex: 1,
      backgroundColor: '#fff',
      alignItems: 'center',
      justifyContent: 'center',
    },
  });