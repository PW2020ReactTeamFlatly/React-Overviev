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
    
  export default function EditFlat(props) {
    const classes = useStyles();
    const {flatId} = props;
    const [activeStep, setActiveStep] = React.useState(0);
    const [name, setName] = React.useState("");
    const [address, setAddress] = React.useState("");
    const [city, setCity] = React.useState("");
    const [price, setPrice] = React.useState("");
    const [btnDisabled, setBtnDisabled] = useState(true)
    const [info, setInfo] = React.useState("");
    const [posted, setPosted] = useState(true);
    const [flat,SetFlat] = useState([]);
    const { setLoading } = useContext(LoadingContext);
    const { setSnackbar } = useContext(SnackbarContext);

    useEffect(() => {
        async function fetchData() {
            setLoading(true);
            try {
                console.log('http://localhost:8080/flats/'+ flatId);
                const flatData = await axios.get('http://localhost:8080/flats/'+ flatId);
                
                SetFlat(flatData.data);
                if(flat.name!=null)
                    setName(flat.name);
                if(flat.address!=null)
                    setAddress(flat.address);
                if(flat.city!=null)
                    setCity(flat.city);
                if(flat.pricePerNight!=null)
                    setPrice(flat.pricePerNight);
                if(flat.information!=null)
                    setInfo(flat.information);
                
            } catch (error) {
                console.error(error);
                setSnackbar({
                    open: true,
                    message: "Błąd ładowania danych",
                    type: "error"
                });
            }
            setLoading(false);
            
        }

        fetchData();
    }, [SetFlat, setLoading, setSnackbar,]);

    const handleNameChange = (event) =>{
        setName(event.target.value);
        btnEnablingCheck();
    }

    const handleAddressChange = (event) =>{
        setAddress(event.target.value);
        btnEnablingCheck();
    }
    const handleCityChange = (event) =>{
        setCity(event.target.value);
        btnEnablingCheck();
    }
    const handlePriceChange = (event) =>{
        setPrice(event.target.value);
        btnEnablingCheck();
    }

    const handleInfoChange = (event) =>{
      setInfo(event.target.value);
      btnEnablingCheck();
  }

    const btnEnablingCheck = () =>
    {
      if(name.length>0 && address.length>0 && city.length>0 && price.length>0 && info.length>0)
        setBtnDisabled(false);
    }

const variable = {
    id:flatId,
    name:name,
    city:city,
    pricePerNight:price,
    address:address,
    information:info,
};

    const handleNext = async () => {


      console.log(name);
        try
        {       
        await axios.put('http://localhost:8080/'+flatId,variable);
        }
        catch(error) {
          setPosted(false);
          setSnackbar({
            open: true,
            type: 'error',
            message: 'Not able to post changes to flat'
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
              Edit flat view
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
            Edit flat
            </Typography>
            <React.Fragment>
              {activeStep === steps.length ? (
                
                <React.Fragment>
                  <Typography variant="h5" gutterBottom>
                  {posted === true ? "Thank you for adding new flat.":"Unfortunetly there was a problem with adding new flat."}
                  </Typography>
                  
                  
                </React.Fragment>
              ) : (
                <React.Fragment>
                  <React.Fragment>a
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
        <Grid item xs={12} sm={6}>
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
            id="pricePerNight" 
            name="pricePerNight" 
            label="Price per night" 
            fullWidth 
            required
            value={price}
            onChange={(event) => handlePriceChange(event)} 
          />
        </Grid>

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
                      disabled={btnDisabled}
                    >
                      {activeStep === steps.length - 1 ? 'Confirm changes' : 'Next'}
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