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

    const { setLoading } = useContext(LoadingContext);
  const { setSnackbar } = useContext(SnackbarContext);
  const [flat,SetFlat] = useState([]);
    useEffect(() => {
        async function fetchData() {
            setLoading(true);
            try {
                const flatData = await axios.get('http://localhost:8080/flats/'+ flatId);
                SetFlat(flatData.data);
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

  return (
    
    <ThemeProvider theme={theme}>
      <CssBaseline/>
      
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
          image="https://bi.im-g.pl/im/85/f0/17/z25104261AMP,Mieszkanie--rynek-wtorny---zdjecie-pogladowe.jpg"
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
        </CardContent>
      </CardActionArea>
      <CardActions>
        <Typography variant="body2" color="secondary" component="p">
            Flat price per night: {flat.pricePerNight}
        </Typography>
        <Typography variant="body2" color="secondary" component="p">
            Rating: {flat.rating}/10
        </Typography>
      </CardActions>
    </Card>
    </main>
    </ThemeProvider>

  );
}