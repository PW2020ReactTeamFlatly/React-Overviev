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
  const { flatId } = props;
  const [activeStep, setActiveStep] = React.useState(0);
  const [name, setName] = React.useState("");
  const [address, setAddress] = React.useState("");
  const [city, setCity] = React.useState("");
  const [price, setPrice] = React.useState(0);
  const [info, setInfo] = React.useState("");
  const [sleeps, setSleeps] = React.useState(0);
  const [rating, setRating] = React.useState(0);
  const [posted, setPosted] = useState(true);
  const { setLoading } = useContext(LoadingContext);
  const { setSnackbar } = useContext(SnackbarContext);
  const [from, setFrom] = useState("");
  const [to, setTo] = useState("");
  const [file, SetFile] = useState(null);


  useEffect(() => {
    async function fetchData() {
      setLoading(true);
      try {
        console.log('http://localhost:8080/flats/' + flatId);
        const flatData = await axios.get('http://localhost:8080/flats/' + flatId);
        if (flatData.data.name != null)
          setName(flatData.data.name);
        if (flatData.data.address != null)
          setAddress(flatData.data.address);
        if (flatData.data.city != null)
          setCity(flatData.data.city);
        if (flatData.data.pricePerNight != null)
          setPrice(flatData.data.pricePerNight);
        if (flatData.data.information != null)
          setInfo(flatData.data.information);
        if (flatData.data.rating != null)
          setRating(flatData.data.rating);
        if (flatData.data.sleeps != null)
          setSleeps(flatData.data.sleeps);
        if (flatData.data.availableFrom != null)
          setFrom(flatData.data.availableFrom.substring(0, 10));
         if (flatData.data.availableTo != null)
          setTo(flatData.data.availableTo.substring(0, 10));
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
  }, [ setLoading, setSnackbar,]);

  const handleNameChange = (event) => {
    setName(event.target.value);
  }

  const handleAddressChange = (event) => {
    setAddress(event.target.value);
  }
  const handleCityChange = (event) => {
    setCity(event.target.value);
  }
  const handlePriceChange = (event) => {
    setPrice(event.target.value);
  }

  const handleInfoChange = (event) => {
    setInfo(event.target.value);
  }

  const handleSleepsChange = (event) =>{
    setSleeps(event.target.value);
}
const handleRatingChange = (event) =>{
  setRating(event.target.value);
}

const handleFromChange = (event) => {
  setFrom(event.target.value);
}

const handleToChange = (event) => {
  setTo(event.target.value);
  console.log(event.target.value);
}

const handleFileChange = (event) => {
  SetFile(event.target.files[0]);
  console.log(event.target.value);
}
  const variable = {
    id: flatId,
    name: name,
    city: city,
    pricePerNight: price,
    address: address,
    information: info,
    sleeps:sleeps,
    rating:rating,
    availableFrom: from+"T00:00:00.000",
    availableTo: to+"T00:00:00.000",
  };

  const handleNext = async () => {

    console.log(variable);
    try {
      await axios.put('http://localhost:8080/flats/' + flatId, variable);
      if(file!==null)
      {
      const dataForm = new FormData();
      dataForm.append('file', file);
      await axios.post("http://localhost:8080/flats/"+flatId+"/photo", dataForm);
      }
    }
    catch (error) {
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
                  {posted === true ? "Thank you for editing flat." : "Unfortunetly there was a problem with editing flat."}
                </Typography>


              </React.Fragment>
            ) : (
                <React.Fragment>
                  <React.Fragment>
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
                          id="sleeps"
                          name="sleeps"
                          label="Sleeps"
                          fullWidth
                          required
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
                      <Grid item xs={12} sm={6}>
                        <TextField
                          id="rating"
                          name="rating"
                          label="Rating"
                          fullWidth
                          required
                          value={rating}
                          onChange={(event) => handleRatingChange(event)}
                        />
                      </Grid>
                      <Grid item xs={12} sm={6}>
            <TextField
              id="dateFrom"
              type="date"
              label="Available from:"
              fullWidth
              className={classes.textField}
              InputLabelProps={{
                shrink: true,
              }}
              value={from}
              onChange={(event) => handleFromChange(event)}
            />
      </Grid>
      <Grid item xs={12} sm={6}>
            <TextField
              id="dateTo"
              type="date"
              label="Available to:"
              fullWidth
              className={classes.textField}
              InputLabelProps={{
                shrink: true,
              }}
              value={to}
              onChange={(event) => handleToChange(event)}
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
                      <Grid item xs={12} sm={12}>
                      <Button
          variant="contained"
          component="label"
          onChange={handleFileChange}
        >
          Change File
          <input
            type="file"
            hidden
          />
        </Button>
                      </Grid>
        <Grid item xs={12} sm={12}>
          <label>{file ? file.name : ""}</label>
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