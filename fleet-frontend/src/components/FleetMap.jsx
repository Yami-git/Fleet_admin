import { MapContainer, TileLayer, Marker, Popup,Polyline } from 'react-leaflet';
import { useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';
import L from 'leaflet';

// (Icon fix code is hidden at the bottom for clarity)

function FleetMap() {

  // --- CONCEPT 1: STATE (Memory) ---
  // We declare a variable 'truckLocation' and a function to update it.
  // Initial State: We start at Cape Town so the map isn't blank on load.
  const [truckPath, setTruckPath] = useState([{lat: -33.9249,lng: 18.4241}]);

  // --- CONCEPT 2: EFFECT (Lifecycle) ---
  // The '[]' at the end means "Run this once when the page loads".
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

          // Step D: Process the Incoming Message
          // The message comes as a text string (JSON). We convert it to an Object.
          const data = JSON.parse(message.body);
          const newPoint={lat:data.latitude, lng:data.longitude};

          // Step E: Update State
          // This is the Magic Moment. Calling this function does two things:
          // 1. Updates 'truckLocation' with the new coordinates.
          // 2. Triggers React to RE-RENDER the map with the new position.


          console.log("📍 New Location Received:", data.latitude, data.longitude);
          setTruckPath((prevPath)=>[...prevPath, newPoint]);
        });
      },
    });

    //Step F: Actually start the connection
    client.activate();

    // Step G: Cleanup
    // If the user closes this tab or component, this runs to close the connection.
    return () => {
      client.deactivate();
    };
  }, []);
const currentPosition=truckPath[truckPath.length-1]
// If currentPosition is missing (undefined), show a loading message instead of crashing.
if (!currentPosition){
    return <div>Waiting for truck signal..</div>;
    }
  // --- CONCEPT 3: RENDERING (The UI) ---
  return (
    <MapContainer
      // We bind the center of the map to our State.
      // When 'truckLocation' changes, the map center updates automatically.
      center={[currentPosition.lat, currentPosition.lng]}
      zoom={15}
      style={{ height: "100vh", width: "100vw" }}
    >
      <TileLayer
        attribution='&copy; OpenStreetMap contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />

      {/* 'positions' expects an array of coordinates, which is exactly what our State is*/}
      <Polyline positions={truckPath} color="blue" weight={5}/>



      {/* The Marker also follows the State variables */}
      <Marker position={[currentPosition.lat, currentPosition.lng]}>
        <Popup>
          🚚 <b>Delivery Truck</b><br />
          Lat: {currentPosition.lat.toFixed(4)}<br />
          Lng: {currentPosition.lng.toFixed(4)}
        </Popup>
      </Marker>
    </MapContainer>
  );
}

// --- BOILERPLATE: The Icon Fix (Just copy this part) ---
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
// -------------------------------------------------------

export default FleetMap;