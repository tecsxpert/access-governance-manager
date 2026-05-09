import { useEffect, useState } from "react";
import axios from "axios";
import Navbar from "../components/NavBar";

function AdminDashboard() {

  const [requests, setRequests] = useState([]);

  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  // ✅ Fetch Requests
  const fetchRequests = async () => {

    try {

      const response = await axios.get(
        "http://localhost:8080/access/all",
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      console.log(response.data);

      setRequests(response.data.data);

    } catch (error) {

      console.log(error);
    }
  };

  useEffect(() => {

    fetchRequests();

  }, []);

  // ✅ Approve
  const approveRequest = async (id) => {

    try {

      await axios.put(
        `http://localhost:8080/access/approve/${id}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      alert("Request Approved");

      fetchRequests();

    } catch (error) {

      console.log(error);
    }
  };

  // ✅ Reject
  const rejectRequest = async (id) => {

    try {

      await axios.put(
        `http://localhost:8080/access/reject/${id}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      alert("Request Rejected");

      fetchRequests();

    } catch (error) {

      console.log(error);
    }
  };

  return (
    <div>
      <Navbar />
      <div className="dashboard-layout">
        <div className="dashboard-hero">
          <div>
            <h1>Admin Dashboard</h1>
            <p>Manage pending requests, approve access, and keep governance flowing smoothly.</p>
          </div>
          <div className="info-card">
            <h3>Current role</h3>
            <p>{role}</p>
          </div>
        </div>

        <div className="table-card">
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>User</th>
                <th>Resource</th>
                <th>Access Type</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {requests.map((request) => (
                <tr key={request.id}>
                  <td>{request.id}</td>
                  <td>{request.userName}</td>
                  <td>{request.resourceName}</td>
                  <td>{request.accessType}</td>
                  <td>
                    <span className={`status-pill status-${request.status?.toLowerCase()}`}>
                      {request.status}
                    </span>
                  </td>
                  <td>
                    <div className="action-buttons">
                      <button
                        className="action-button approve"
                        onClick={() => approveRequest(request.id)}
                        type="button"
                      >
                        Approve
                      </button>
                      <button
                        className="action-button reject"
                        onClick={() => rejectRequest(request.id)}
                        type="button"
                      >
                        Reject
                      </button>
                    </div>
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

export default AdminDashboard;