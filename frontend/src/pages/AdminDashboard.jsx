import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function AdminDashboard() {

  const navigate = useNavigate();

  const [requests, setRequests] = useState([]);

  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  const logout = () => {

    localStorage.removeItem("token");
    localStorage.removeItem("role");

    navigate("/");
  };

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

    <div style={{ padding: "30px" }}>

      <h1>Admin Dashboard</h1>

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

      <table
        border="1"
        cellPadding="10"
        width="100%"
      >

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
              <td>{request.status}</td>

              <td>

                <button
                  onClick={() => approveRequest(request.id)}
                >
                  Approve
                </button>

                <button
                  onClick={() => rejectRequest(request.id)}
                  style={{
                    marginLeft: "10px",
                  }}
                >
                  Reject
                </button>

              </td>

            </tr>

          ))}

        </tbody>

      </table>

    </div>
  );
}

export default AdminDashboard;