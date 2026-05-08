import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function UserDashboard() {

  const navigate = useNavigate();

  const [resourceName, setResourceName] = useState("");
  const [accessType, setAccessType] = useState("");

  const [requests, setRequests] = useState([]);

  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  // ✅ Logout
  const logout = () => {

    localStorage.removeItem("token");
    localStorage.removeItem("role");

    navigate("/");
  };

  // ✅ Fetch My Requests
  const fetchMyRequests = async () => {

    try {

      const response = await axios.get(
        "http://localhost:8080/access/my",
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setRequests(response.data.data);

    } catch (error) {

      console.log(error);
    }
  };

  useEffect(() => {

    fetchMyRequests();

  }, []);

  // ✅ Create Request
  const createRequest = async () => {

    try {

      await axios.post(
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

      alert("Access Request Created");

      setResourceName("");
      setAccessType("");

      fetchMyRequests();

    } catch (error) {

      console.log(error);

      console.log(error.response);

      alert(error.response?.data?.message || "Request Failed");
    }
  };

  return (

    <div style={{ padding: "30px" }}>

      <h1>User Dashboard</h1>

      <h3>Welcome {role}</h3>

      <button
        onClick={logout}
        style={{
          padding: "10px",
          backgroundColor: "red",
          color: "white",
          border: "none",
          cursor: "pointer",
          marginBottom: "20px",
        }}
      >
        Logout
      </button>

      <div
        style={{
          width: "350px",
          display: "flex",
          flexDirection: "column",
          gap: "15px",
          marginBottom: "40px",
        }}
      >

        <input
          type="text"
          placeholder="Resource Name"
          value={resourceName}
          onChange={(e) =>
            setResourceName(e.target.value)
          }
          style={{ padding: "10px" }}
        />

        <input
          type="text"
          placeholder="Access Type"
          value={accessType}
          onChange={(e) =>
            setAccessType(e.target.value)
          }
          style={{ padding: "10px" }}
        />

        <button
          onClick={createRequest}
          style={{
            padding: "10px",
            cursor: "pointer",
          }}
        >
          Create Request
        </button>

      </div>

      <h2>My Requests</h2>

      <table
        border="1"
        cellPadding="10"
        width="100%"
      >

        <thead>
          <tr>
            <th>ID</th>
            <th>Resource</th>
            <th>Access Type</th>
            <th>Status</th>
          </tr>
        </thead>

        <tbody>

          {requests.map((request) => (

            <tr key={request.id}>

              <td>{request.id}</td>
              <td>{request.resourceName}</td>
              <td>{request.accessType}</td>
              <td>{request.status}</td>

            </tr>

          ))}

        </tbody>

      </table>

    </div>
  );
}

export default UserDashboard;