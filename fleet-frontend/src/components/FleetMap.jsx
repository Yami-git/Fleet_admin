import React, {useEffect, useState} from 'react';
import { MapContainer, TileLayer, Marker, Popup,Polyline } from 'react-leaflet';
import { Client } from '@stomp/stompjs';
import L from 'leaflet';

// --- BOILERPLATE: The Icon Fix ---
import icon from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';
let DefaultIcon = L.icon({
    iconUrl: icon,
    shadowUrl: iconShadow,
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
});
L.Marker.prototype.options.icon = DefaultIcon;
// 


function FleetMap() {

  // --- CONCEPT 1: STATE (Memory) ---
  //We now use an Object {} instead of an Array[].
  const [vehicles, setVehicles] = useState({});

  // --- CONCEPT 2: EFFECT (Lifecycle) ---
  
  useEffect(() => {

    // Step A: Configure the Connection
    const client = new Client({
      brokerURL: 'ws://localhost:8080/ws', // The "Door" we opened in Spring Boot

      // Step B: What to do when we successfully connect?
      onConnect: () => {
        console.log('✅ Connected to WebSocket!');

        // Step C: Subscribe to the Topic
        // We tell the server: "Send me anything posted to /topic/updates"
        client.subscribe('/topic/updates', (message) => {

          // 1. Parse the incoming message safely FIRST.
          const parsdeData=JSON.parse(message.body);

          console.log("📍 NEW TRUCK DATA:", parsdeData);

          const truckId=parsdeData.truckId || parsdeData.id|| "Unkown-Truck";
          const newPoint={lat:parsdeData.latitude, lng:parsdeData.longitude};

          //Update State cleanly per truck
          setVehicles((prevVehicles) => {
            //Get the past history of THIS specific truck (or an empty array if it's new)
            const existingTruck=prevVehicles[truckId] || {path:[]};

            return{
              ...prevVehicles, //Keep all the other trucks unchanged
              [truckId]:{
                currentLocation: newPoint,
                //Add the new point specifically to THIS truck's path history
                path:[...existingTruck.path, newPoint]
              }
            };
          });
        });
      },
    });

          // Step D: Process the Incoming Message
          // The message comes as a text string (JSON). We convert it to an Object.
          //const data = JSON.parse(message.body);
          //const newPoint={lat:data.latitude, lng:data.longitude};

          // Step E: Update State
          // This is the Magic Moment. Calling this function does two things:
          // 1. Updates 'truckLocation' with the new coordinates.
          // 2. Triggers React to RE-RENDER the map with the new position.



    //Step F: Actually start the connection
    client.activate();

    // Step G: Cleanup
    // If the user closes this tab or component, this runs to close the connection.
    return () => client.deactivate();
  }, []);


  // --- CONCEPT 3: RENDERING (The UI) ---
  return (
    <MapContainer
      // We bind the center of the map to our State.
      // When 'truckLocation' changes, the map center updates automatically.
      center={[-33.9249, 18.4241]}
      zoom={13}
      style={{ height: "100vh", width: "100vw" }}
    >
      <TileLayer
        attribution='&copy; OpenStreetMap contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />

      {/* We loop through our 'vehicles' dictionary.
          For each truck, we draw a Polyline of its path history and a Marker at its current location.
      */}

      {Object.entries(vehicles).map(([truckId, truckData])=>(
        <React.Fragment key={truckId}>
          {/*This is the truck's specific tail*/}
          <Polyline positions={truckData.path} color="blue" weight={3}/>

          {/*This is the truck's specific marker*/}
          <Marker position={[truckData.currentLocation.lat, truckData.currentLocation.lng]}>
            <Popup>
              🚚 <b>{truckId}</b><br />
              Lat: {truckData.currentLocation.lat.toFixed(4)}<br />
              Lng: {truckData.currentLocation.lng.toFixed(4)}
            </Popup>
            
            </Marker>
        </React.Fragment>
            
  
        ))}
      
    </MapContainer>
  );
}


export default FleetMap;