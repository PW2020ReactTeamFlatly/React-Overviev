import React from "react";
import { Switch, Route, Redirect,BrowserRouter } from 'react-router-dom';
import FlatView from './FlatView';
import FlatList from './FlatList';
import CreateFlat from './CreateFlat';
import BookingsList from './BookingsList';
import BookingsListId from './BookingsListId';
import EditFlat from './EditFlat'


const BookingsListWithId = ({match}) =>{
    return(
        <BookingsListId flatId={match.params.id}/>
    )
}

const EditFlatWithId = ({match}) => {
    return(
        <EditFlat flatId={match.params.id}/>
    );
}

const FlatListWithId = ({match}) => {
    return(
        <FlatView flatId={match.params.id}/>
    );
}

function Main(){
    return (
        <BrowserRouter>
            <Switch>
                <Route path='/editflat/:id' component={EditFlatWithId}/>
                <Route path='/flatlist/:id' component={FlatListWithId}/>
                <Route path='/flatlist' component={FlatList}/>
                <Route path='/addflat' component={CreateFlat}/>
                <Route path='/bookings' component={BookingsList}/>
                <Route path='/booking/:id' component={BookingsListWithId}/>
                <Redirect to='/flatlist' />
            </Switch>
        </BrowserRouter>
    );
}

export default Main;