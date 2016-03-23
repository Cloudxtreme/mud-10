import {startServer} from './server';

startServer()
    .then((server) => {
        console.log('Started server');
    }, (err) => {
        console.log(`Error starting server: ${err}`);
    });
