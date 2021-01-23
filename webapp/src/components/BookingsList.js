import React, { useState, useEffect, useContext } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import SnackbarContext from '../contexts/SnackbarContext';
import LoadingContext from '../contexts/LoadingContext';
import axios from 'axios';

const useStyles = makeStyles({
  table: {
    minWidth: 650,
  },
});

function createData(name, calories, fat, carbs, protein) {
  return { name, calories, fat, carbs, protein };
}

export default function BookingsList() {
  const classes = useStyles();
  const { setLoading } = useContext(LoadingContext);
  const { setSnackbar } = useContext(SnackbarContext);
  const [bookings, setBookings] = useState([]);
  useEffect(() => {
    async function fetchData() {
        setLoading(true);
        try {
            const bookingData = await axios.get('http://localhost:8080/reservations');
            setBookings(bookingData.data);
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
    fetchData(); }, [setBookings, setLoading, setSnackbar,]);

  return (
    <TableContainer component={Paper}>
      <Table className={classes.table} aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell>Customer name</TableCell>
            <TableCell align="right">Start date</TableCell>
            <TableCell align="right">End date</TableCell>
            <TableCell align="right">Price</TableCell>
            <TableCell align="right">Sleeps</TableCell>
            <TableCell align="right">Flat</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>ds
          {bookings.map((row) => (
            <TableRow key={row.name}>
              <TableCell component="th" scope="row">{row.customer_name}</TableCell>
              <TableCell align="right">{row.start_date}</TableCell>
              <TableCell align="right">{row.end_date}</TableCell>
              <TableCell align="right">{row.price}</TableCell>
              <TableCell align="right">{row.sleeps}</TableCell>
              <TableCell align="right">{row.flat_id}</TableCell>
              {console.log("Siema")}
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}