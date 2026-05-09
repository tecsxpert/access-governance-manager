import { useEffect, useMemo, useState } from "react";
import api, { getAuthHeaders } from "../api";
import Navbar from "../components/NavBar";

function UserDashboard() {

  const [resourceName, setResourceName] = useState("");
  const [accessType, setAccessType] = useState("");
  const [requests, setRequests] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [isLoading, setIsLoading] = useState(false);

  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  // ✅ Fetch My Requests
  const fetchMyRequests = async () => {
    setIsLoading(true);

    try {
      const response = await api.get("/access/my", {
        headers: getAuthHeaders(),
      });

      setRequests(response.data.data || []);
    } catch (error) {
      console.log(error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchMyRequests();
  }, []);

  const filteredRequests = useMemo(() => {
    const normalizedSearch = searchTerm.trim().toLowerCase();

    return requests.filter((request) => {
      const matchesStatus =
        statusFilter === "ALL" || request.status === statusFilter;

      const matchesSearch =
        !normalizedSearch ||
        [request.id, request.resourceName, request.accessType, request.status]
          .some((value) =>
            String(value || "").toLowerCase().includes(normalizedSearch)
          );

      return matchesStatus && matchesSearch;
    });
  }, [requests, searchTerm, statusFilter]);

  const totalCount = requests.length;
  const pendingCount = requests.filter((request) => request.status === "PENDING").length;
  const approvedCount = requests.filter((request) => request.status === "APPROVED").length;
  const rejectedCount = requests.filter((request) => request.status === "REJECTED").length;

  // ✅ Create Request
  const createRequest = async () => {

    try {

      await api.post(
        "/access/request",
        {
          resourceName,
          accessType,
        },
        {
          headers: {
            ...getAuthHeaders(),
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

        <div className="dashboard-cards">
          <div className="info-card">
            <h3>Total requests</h3>
            <p>{totalCount}</p>
          </div>
          <div className="info-card">
            <h3>Pending</h3>
            <p>{pendingCount}</p>
          </div>
          <div className="info-card">
            <h3>Approved</h3>
            <p>{approvedCount}</p>
          </div>
          <div className="info-card">
            <h3>Rejected</h3>
            <p>{rejectedCount}</p>
          </div>
        </div>

        <div className="dashboard-controls">
          <input
            type="search"
            placeholder="Search by ID, resource, type, or status"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="form-input"
          />
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="form-input"
          >
            <option value="ALL">All Statuses</option>
            <option value="PENDING">PENDING</option>
            <option value="APPROVED">APPROVED</option>
            <option value="REJECTED">REJECTED</option>
          </select>
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
              {isLoading ? (
                <tr>
                  <td colSpan="4" className="empty-state">
                    Loading your requests...
                  </td>
                </tr>
              ) : filteredRequests.length === 0 ? (
                <tr>
                  <td colSpan="4" className="empty-state">
                    {requests.length === 0
                      ? "No access requests yet. Submit one to get started."
                      : "No requests match your search or selected status."}
                  </td>
                </tr>
              ) : (
                filteredRequests.map((request) => (
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
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default UserDashboard;