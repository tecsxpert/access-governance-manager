import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function UserDashboard() {

  const navigate = useNavigate();

  const [resourceName, setResourceName] = useState("");
  const [accessType, setAccessType] = useState("");

  const role = localStorage.getItem("role");

  // ✅ Logout
  const logout = () => {

    localStorage.removeItem("token");
    localStorage.removeItem("role");

    navigate("/");
  };

  // ✅ Create Request
  const createRequest = async () => {

    try {

      const token = localStorage.getItem("token");

      console.log("TOKEN:", token);

      const response = await axios.post(
        "http://localhost:8080/access/request",
        {
          resourceName,
          accessType,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );

      console.log(response.data);

      alert("Access Request Created");

      // ✅ clear inputs
      setResourceName("");
      setAccessType("");

    } catch (error) {

      console.log(error);

      console.log(error.response);

      alert("Request Failed");
    }
  };

  return (

    <div
      style={{
        textAlign: "center",
        marginTop: "50px",
      }}
    >

      <h1>User Dashboard</h1>

      <h3>Welcome {role}</h3>

      <div
        style={{
          width: "350px",
          margin: "30px auto",
          display: "flex",
          flexDirection: "column",
          gap: "15px",
          backgroundColor: "#f4f4f4",
          padding: "30px",
          borderRadius: "10px",
        }}
      >

        <input
          type="text"
          placeholder="Enter Resource Name"
          value={resourceName}
          onChange={(e) => setResourceName(e.target.value)}
          style={{
            padding: "12px",
            borderRadius: "5px",
            border: "1px solid gray",
          }}
        />

        <input
          type="text"
          placeholder="Enter Access Type"
          value={accessType}
          onChange={(e) => setAccessType(e.target.value)}
          style={{
            padding: "12px",
            borderRadius: "5px",
            border: "1px solid gray",
          }}
        />

        <button
          onClick={createRequest}
          style={{
            padding: "12px",
            backgroundColor: "#4CAF50",
            color: "white",
            border: "none",
            borderRadius: "5px",
            cursor: "pointer",
            fontSize: "16px",
          }}
        >
          Create Request
        </button>

        <button
          onClick={logout}
          style={{
            padding: "12px",
            backgroundColor: "red",
            color: "white",
            border: "none",
            borderRadius: "5px",
            cursor: "pointer",
            fontSize: "16px",
          }}
        >
          Logout
        </button>

      </div>

    </div>
  );
}

export default UserDashboard;