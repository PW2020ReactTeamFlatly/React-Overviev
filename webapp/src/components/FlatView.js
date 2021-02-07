import React, { useState, useEffect, useContext } from 'react';
import AppBar from '@material-ui/core/AppBar';
import Button from '@material-ui/core/Button';
import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import CssBaseline from '@material-ui/core/CssBaseline';
import Grid from '@material-ui/core/Grid';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import Link from '@material-ui/core/Link';
import HomeIcon from '@material-ui/icons/Home';
import { Link as RouterLink } from 'react-router-dom';
import SnackbarContext from '../contexts/SnackbarContext';
import LoadingContext from '../contexts/LoadingContext';
import axios from 'axios';
import CardActionArea from '@material-ui/core/CardActionArea';
import { createMuiTheme, ThemeProvider } from '@material-ui/core/styles';
import purple from '@material-ui/core/colors/purple';
import indigo from '@material-ui/core/colors/indigo';
import LoginContext from '../contexts/LoginContex';

const useStyles = makeStyles((theme)=>({
  root: {
    maxWidth: 800,
  },
  appBar: {
    position: 'relative',
  },
  layout: {
    width: 'auto',
    marginLeft: theme.spacing(2),
    marginRight: theme.spacing(2),
    marginTop: theme.spacing(2),
    [theme.breakpoints.up(600 + theme.spacing(2) * 2)]: {
      width: 600,
      marginLeft: 'auto',
      marginRight: 'auto',
    },
  },
}));



const theme = createMuiTheme({
  palette: {
    primary: {
      main: '#0d47a1',
    },
    secondary: {
      main: '#115293',
    },
  },
});

export default function FlatView(props) {
  const classes = useStyles();

  const {flatId} = props;
  const { token, setToken} = useContext(LoginContext);
  const { setLoading } = useContext(LoadingContext);
  const { setSnackbar } = useContext(SnackbarContext);
  const [flat,SetFlat] = useState([]);
  const [photo, SetPhoto] = useState(undefined);

    useEffect(() => {
        async function fetchData() {
            setLoading(true);
            var config = {
              method: 'get',
              url: 'http://localhost:8080/flats/'+flatId,
              headers: { 
                'security-header': token
              }
            };
            
            try
            {
              const response = await axios(config);
              SetFlat(response.data);

              var config2 = {
                method: 'get',
                url: 'http://localhost:8080/flats/'+flatId+'/photo2',
                headers: { 
                  'security-header': token
                },
                responseType: 'blob'
              };
              const blob = await axios(config2)            
              SetPhoto(URL.createObjectURL(blob.data));
            }
            catch(error)
            {
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
    }, [SetFlat, setLoading, setSnackbar, SetPhoto]);

  return (
    
    <ThemeProvider theme={theme}>
      <CssBaseline/>
      
      <AppBar position="absolute" color="default" className={classes.appBar}>
          <Toolbar>
            <Typography variant="h6" color="inherit" noWrap>
              Flat details
            </Typography>

            
            <Button
            variant="contained"
            color="primary" 
            style={{ marginLeft: "auto" }}
            component={RouterLink} to="/flatlist">
              {/* <RouterLink  to={"/flatlist"} color="initial"> */}
                        Back
              {/* </RouterLink> */}
            </Button>
          </Toolbar>
        </AppBar>

    <main className={classes.layout}>
    <Card className={classes.root}>
      <CardActionArea>
        <CardMedia
          component="img"
          alt="Contemplative Reptile"
          height="140"
          image= {photo}
          title=""
        />
        <CardContent>
          <Typography gutterBottom variant="h5" component="h2" color="primary">
            {flat.name}
          </Typography>
          <Typography variant="h6" color="light" component="p">
            {flat.information}
          </Typography>
          <Typography variant="body2" color="textPrimary" component="p">
            City: {flat.city}
          </Typography>
          <Typography variant="body2" color="textPrimary" component="p">
            Address: {flat.address}
          </Typography>
          <Typography variant="body2" color="textPrimary" component="p">
            Available from: {flat.availableFrom ? flat.availableFrom.substring(8,10) + "." + flat.availableFrom.substring(5,7) + "." + flat.availableFrom.substring(0,4) : ""}
          </Typography>
          <Typography variant="body2" color="textPrimary" component="p">
            Available to: {flat.availableTo ? flat.availableTo.substring(8,10) + "." + flat.availableTo.substring(5,7) + "." + flat.availableTo.substring(0,4) : ""}
          </Typography>
        </CardContent>
      </CardActionArea>
      <CardActions>
        <Typography variant="body2" color="secondary" component="p">
            Flat price per night: {flat.pricePerNight}
        </Typography>
        <Typography variant="body2" color="secondary" component="p">
            Rating: {flat.rating}/10
        </Typography>
        <Typography variant="body2" color="secondary" component="p">
            Sleeps: {flat.sleeps}
        </Typography>
      </CardActions>
    </Card>
    </main>
    </ThemeProvider>

  );
}