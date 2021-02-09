import React from 'react';
import { NativeRouter, Route, Link } from "react-router-native";
import List from './components/ListItem';
import Details from './components/ItemDetails';
import { Header,Icon } from 'react-native-elements';


const DetailsWithId = ({match}) => {
  return(
      <Details id={match.params.id}/>
  );
}
export default function App({match}) {
  return (
    
    <NativeRouter>
      <Header
        leftComponent={{ icon: 'menu', color: '#fff' }}
        centerComponent={{ text: '  Home List', style: { color: '#fff' } }}
        rightComponent={<Link to="/"><Icon name='home' color='#fff'></Icon></Link> }
      />
        <Route exact path="/" component={List} />
        <Route path='/details/:id' component={DetailsWithId}/>    
    </NativeRouter>
  );
}

