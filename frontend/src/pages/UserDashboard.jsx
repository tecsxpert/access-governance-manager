import { useEffect, useState } from "react";
import axios from "axios";
import Navbar from "../components/NavBar";

function UserDashboard() {

  const [resourceName, setResourceName] = useState("");
  const [accessType, setAccessType] = useState("");

  const [requests, setRequests] = useState([]);

  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

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

      alert(
        error.response?.data?.message ||
        "Request Failed"
      );
    }
  };

  return (
    <div>
      <Navbar />
      <div className="dashboard-layout">
        <div className="dashboard-hero">
          <div>
            <h1>User Dashboard</h1>
            <p>
              Welcome back, <strong>{role}</strong>. Create access requests and track approval status.
            </p>
          </div>
          <div className="info-card">
            <h3>Create a new request</h3>
            <p>Enter the resource and access type to submit an access governance request.</p>
          </div>
        </div>

        <div className="form-panel">
          <input
            type="text"
            placeholder="Resource Name"
            value={resourceName}
            onChange={(e) => setResourceName(e.target.value)}
            className="form-input"
          />
          <input
            type="text"
            placeholder="Access Type"
            value={accessType}
            onChange={(e) => setAccessType(e.target.value)}
            className="form-input"
          />
          <button onClick={createRequest} className="primary-button" type="button">
            Create Request
          </button>
        </div>

        <div className="table-card">
          <table className="data-table">
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
                  <td>
                    <span className={`status-pill status-${request.status?.toLowerCase()}`}>
                      {request.status}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default UserDashboard;