// import React from "react";

// export default function CreateFlat() {
//     return (
//         <div> CreateFlat </div>
//     );
// }

import React, { useState, useEffect, useContext } from 'react';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import TextField from '@material-ui/core/TextField';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import { makeStyles } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Paper from '@material-ui/core/Paper';
import Stepper from '@material-ui/core/Stepper';
import Step from '@material-ui/core/Step';
import StepLabel from '@material-ui/core/StepLabel';
import Button from '@material-ui/core/Button';
import TextareaAutosize from '@material-ui/core/TextareaAutosize';
import Link from '@material-ui/core/Link';
import axios from 'axios';
import SnackbarContext from '../contexts/SnackbarContext';
import LoadingContext from '../contexts/LoadingContext';
import { Link as RouterLink } from 'react-router-dom';
//import AddressForm from './AddressForm';
//import PaymentForm from './PaymentForm';
//import Review from './Review';

const useStyles = makeStyles((theme) => ({
    appBar: {
      position: 'relative',
    },
    layout: {
      width: 'auto',
      marginLeft: theme.spacing(2),
      marginRight: theme.spacing(2),
      [theme.breakpoints.up(600 + theme.spacing(2) * 2)]: {
        width: 600,
        marginLeft: 'auto',
        marginRight: 'auto',
      },
    },
    paper: {
      marginTop: theme.spacing(3),
      marginBottom: theme.spacing(3),
      padding: theme.spacing(2),
      [theme.breakpoints.up(600 + theme.spacing(3) * 2)]: {
        marginTop: theme.spacing(6),
        marginBottom: theme.spacing(6),
        padding: theme.spacing(3),
      },
    },
    stepper: {
      padding: theme.spacing(3, 0, 5),
    },
    buttons: {
      display: 'flex',
      justifyContent: 'flex-end',
    },
    button: {
      marginTop: theme.spacing(3),
      marginLeft: theme.spacing(1),
    },
  }));
  
  const steps = ['New flat'];
  
//   function getStepContent(step) {
//     switch (step) {
//       case 0:
//         return <AddressFormm />;
//       case 1:
//         return <PaymentForm />;
//       case 2:
//         return <Review />;
//       default:
//         throw new Error('Unknown step');
//     }
//   }
  
  export default function Checkout() {
    const classes = useStyles();
    const [activeStep, setActiveStep] = React.useState(0);
    const [rating,setRating] = React.useState(2);
    const [name, setName] = React.useState("");
    const [address, setAddress] = React.useState("");
    const [city, setCity] = React.useState("");
    const [price, setPrice] = React.useState("");
    const [sleeps, setSleeps] = React.useState("");
    const [info, setInfo] = React.useState("");
    const [posted, setPosted] = useState(true);

    const { setSnackbar } = useContext(SnackbarContext);

    const handleNameChange = (event) =>{
        setName(event.target.value);
    }

    const handleAddressChange = (event) =>{
        setAddress(event.target.value);
    }
    const handleCityChange = (event) =>{
        setCity(event.target.value);
    }
    const handlePriceChange = (event) =>{
        setPrice(event.target.value);
    }

    const handleInfoChange = (event) =>{
      setInfo(event.target.value);
  }
  const handleSleepsChange = (event) =>{
    setSleeps(event.target.value);
}


const variable = [{
  name:name,
  city:city,
  pricePerNight:price,
  address:address,
  information:info,
  rating:rating,
  sleeps:sleeps,
}];

    const handleNext = async () => {


      console.log(name);
        try
        {       
        await axios.post('http://localhost:8080/flats',variable);
        }
        catch(error) {
          setPosted(false);
          setSnackbar({
            open: true,
            type: 'error',
            message: 'Not able to post new flat'
        });
    }
      
      setActiveStep(activeStep + 1);
    };
  
    const handleBack = () => {
      setActiveStep(activeStep - 1);
    };
  
    return (
      <React.Fragment>
        <CssBaseline />
        <AppBar position="absolute" color="default" className={classes.appBar}>
          <Toolbar>
            <Typography variant="h6" color="inherit" noWrap>
              Add flat view
            </Typography>

            
            <Button
            variant="contained"
            color="primary" 
            style={{ marginLeft: "auto" }}
            component={RouterLink} to="/flatlist">
              Back
            </Button>
            
          </Toolbar>
        </AppBar>
        <main className={classes.layout}>
          <Paper className={classes.paper}>
            <Typography component="h1" variant="h4" align="center">
            New flat
            </Typography>
            {/* <Stepper activeStep={activeStep} className={classes.stepper}>
              {steps.map((label) => (
                <Step key={label}>
                  <StepLabel>{label}</StepLabel>
                </Step>
              ))}
            </Stepper> */}
            <React.Fragment>
              {activeStep === steps.length ? (
                
                <React.Fragment>
                  <Typography variant="h5" gutterBottom>
                  {posted === true ? "Thank you for adding new flat.":"Unfortunetly there was a problem with adding new flat."}
                  </Typography>
                  
                  
                </React.Fragment>
              ) : (
                <React.Fragment>
                  <React.Fragment>
      {/* <Typography variant="h6" gutterBottom>
        Flat information
      </Typography> */}
      <Grid container spacing={3}>

        <Grid item xs={12} >
          <TextField
            required
            id="name"
            name="name"
            label="Name"
            fullWidth
            value={name}
            onChange={(event) => handleNameChange(event)}            
          />
        </Grid>
        
        <Grid item xs={12}>
          <TextField
            required
            id="address"
            name="address"
            label="Address"
            fullWidth
            autoComplete="shipping address-line1"
            value={address}
            onChange={(event) => handleAddressChange(event)} 
          />
        </Grid>
        <Grid item xs={12} sm={12}>
          <TextField
            required
            id="city"
            name="city"
            label="City"
            fullWidth
            autoComplete="shipping address-level2"
            value={city}
            onChange={(event) => handleCityChange(event)} 
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            required
            id="sleeps"
            name="sleeps"
            label="Sleeps"
            fullWidth
            value={sleeps}
            onChange={(event) => handleSleepsChange(event)} 
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField 
            id="pricePerNight" 
            name="pricePerNight" 
            label="Price per night" 
            fullWidth 
            required
            value={price}
            onChange={(event) => handlePriceChange(event)} 
          />
        </Grid>
        <Grid item xs={12} sm={12}>
        <TextField
          required
          id="informationTextField"
          label="Information"
          multiline
          rows={4}
          fullWidth
          value={info}
          onChange={(event) => handleInfoChange(event)} 
        />
        </Grid>
      </Grid>
    </React.Fragment>
                  <div className={classes.buttons}>
                    {activeStep !== 0 && (
                      <Button onClick={handleBack} className={classes.button}>
                        Back
                      </Button>
                    )}
                    
                    <Button
                      variant="contained"
                      color="primary"
                      onClick={handleNext}
                      className={classes.button}
                    >
                      {activeStep === steps.length - 1 ? 'Add flat' : 'Next'}
                    </Button>
                    
                  </div>
                </React.Fragment>
              )}
            </React.Fragment>
          </Paper>
          
        </main>
      </React.Fragment>
    );
  }