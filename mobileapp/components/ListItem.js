import React, {  useEffect,useState } from "react";
import { StyleSheet,  View,Text } from "react-native";
import { ListItem, Avatar,Header } from 'react-native-elements';
import {Link } from "react-router-native";
import { ScrollView } from 'react-native';
import axios from 'axios';




export default function List() {
  const [flats, setData] = useState([]);

  useEffect(() => {
    async function fetchData() {
        try{
            const response = await axios.get('http://flatly.us-east-2.elasticbeanstalk.com/flats/mobile');
            setData(response.data);
        } catch (error){
            console.error(error);
        }

    }
    fetchData();
},[setData]);
  
  return (
        <View style={{flex: 1}}>
          <ScrollView>
  {
   flats.data ?
   flats.data.map((l) => (
    
    <Link to={"details/" + l.id}>
    <ListItem  bottomDivider>
      <Avatar source={{
    uri: 'http://flatly.us-east-2.elasticbeanstalk.com/flats/mobile/'+l.id +'/photo'
  }}/>
      <ListItem.Content>
        <ListItem.Title>{l.name}</ListItem.Title>
        <ListItem.Subtitle>{l.city}</ListItem.Subtitle>
      </ListItem.Content>
    </ListItem>
    </Link>
  )):null    
  }
    </ScrollView> 
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